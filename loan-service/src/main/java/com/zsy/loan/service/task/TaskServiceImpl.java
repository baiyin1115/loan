package com.zsy.loan.service.task;

import com.zsy.loan.bean.entity.system.Task;
import com.zsy.loan.bean.entity.system.TaskLog;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.exception.LoanExceptionEnum;
import com.zsy.loan.bean.vo.QuartzJob;
import com.zsy.loan.dao.system.TaskLogRepository;
import com.zsy.loan.dao.system.TaskRepository;
import com.zsy.loan.utils.factory.Page;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

  private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskLogRepository taskLogRepository;

  @Autowired
  private JobService jobService;

  @Override
  public Task save(Task task) {
    logger.info("新增定时任务%s", task.getName());
    task = taskRepository.save(task);
    try {
      jobService.addJob(jobService.getJob(task));
    } catch (SchedulerException e) {
      logger.error(e.getMessage(), e);
    }
    return task;
  }

  @Override
  public boolean update(Task task) {
    logger.info("更新定时任务{}", task.getName());
    taskRepository.save(task);
    try {
      QuartzJob job = jobService.getJob(task.getId().toString(), task.getJobGroup());
      if (job != null) {
        jobService.deleteJob(job);
      }
      jobService.addJob(jobService.getJob(task));
    } catch (SchedulerException e) {
      logger.error(e.getMessage(), e);
    }

    return true;
  }

  @Override
  public boolean simpleUpdate(Task task) {
    taskRepository.save(task);
    return true;
  }

  @Override
  public Task get(Long id) {
    return taskRepository.findOne(id);
  }


  @Override
  public Task disable(Long id) {
    Task task = get(id);
    task.setDisabled(true);
    taskRepository.save(task);
    logger.info("禁用定时任务{}", id.toString());
    try {
      QuartzJob job = jobService.getJob(task.getId().toString(), task.getJobGroup());
      if (job != null) {
        jobService.deleteJob(job);
      }
    } catch (SchedulerException e) {
      logger.error(e.getMessage(), e);
    }
    return task;
  }

  @Override
  public Task enable(Long id) {
    Task task = get(id);
    task.setDisabled(false);
    taskRepository.save(task);
    logger.info("启用定时任务{}", id.toString());
    try {
      QuartzJob job = jobService.getJob(task.getId().toString(), task.getJobGroup());
      if (job != null) {
        jobService.deleteJob(job);
      }
      if (!task.isDisabled()) {
        jobService.addJob(jobService.getJob(task));
      }
    } catch (SchedulerException e) {
      throw new LoanException(LoanExceptionEnum.TASK_CONFIG_ERROR);
    }
    return task;
  }

  @Override
  public Task delete(Long id) {
    Task task = get(id);
    task.setDisabled(true);
    taskRepository.delete(task);
    logger.info("删除定时任务{}", id.toString());

    try {
      QuartzJob job = jobService.getJob(task);
      if (job != null) {
        jobService.deleteJob(job);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return task;
  }

  @Override
  public Page<TaskLog> getTaskLogs(Page<TaskLog> page, Long taskId) {
    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC, "id");
    }

    org.springframework.data.domain.Page<TaskLog> taskLogPage = taskLogRepository
        .findAll(new Specification<TaskLog>() {

          @Override
          public Predicate toPredicate(Root<TaskLog> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder
                  criteriaBuilder) {
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(criteriaBuilder.equal(root.get("idTask").as(Long.class), taskId));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);
    page.setTotal(Integer.valueOf(taskLogPage.getTotalElements() + ""));
    page.setRecords(taskLogPage.getContent());
    return page;
  }

}
