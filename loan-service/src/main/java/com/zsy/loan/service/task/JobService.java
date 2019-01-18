package com.zsy.loan.service.task;

import com.zsy.loan.bean.entity.system.Task;
import com.zsy.loan.bean.vo.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 任务服务
 */
public interface JobService {

  /**
   * 获取单个任务
   */
  QuartzJob getJob(String jobName, String jobGroup) throws SchedulerException;

  /**
   * 获取所有任务
   */
  List<QuartzJob> getAllJobs() throws SchedulerException;

  /**
   * 所有正在运行的job
   */
  List<QuartzJob> getRunningJob() throws SchedulerException;

  /**
   * 查询任务列表
   */
  List<QuartzJob> getTaskList();

  QuartzJob getJob(Task task);

  /**
   * 添加任务
   */
  boolean addJob(QuartzJob job) throws SchedulerException;

  /**
   * 暂停任务
   */
  boolean pauseJob(QuartzJob job);

  /**
   * 恢复任务
   */
  boolean resumeJob(QuartzJob job);

  /**
   * 删除任务
   */
  boolean deleteJob(QuartzJob job);

  /**
   * 立即执行一个任务
   */
  void testJob(QuartzJob job) throws SchedulerException;

  /**
   * 更新任务时间表达式
   */
  void updateCronExpression(QuartzJob job) throws SchedulerException;

}
