package com.zsy.loan.service.task;


import com.zsy.loan.bean.entity.system.Task;
import com.zsy.loan.bean.entity.system.TaskLog;
import com.zsy.loan.utils.factory.Page;
import java.util.Optional;

/**
 * 任务计划服务
 */
public interface TaskService {

  /**
   * 新增任务计划
   */
  Task save(Task task);

  /**
   * 更新任务计划
   */
  boolean update(Task task);

  /**
   * 更新任务计划配置
   */
  boolean simpleUpdate(Task task);

  /**
   * 获取任务计划
   */
  Optional<Task> get(Long id);

  /**
   * 禁用任务计划
   */
  Task disable(Long id);

  /**
   * 启用任务计划
   */
  Task enable(Long id);

  /**
   * 删除任务计划
   */
  Task delete(Long id);

  Page<TaskLog> getTaskLogs(Page<TaskLog> page, Long taskId);
}
