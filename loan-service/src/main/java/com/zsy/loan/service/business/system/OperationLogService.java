package com.zsy.loan.service.business.system;

import com.zsy.loan.bean.entity.system.OperationLog;
import com.zsy.loan.utils.factory.Page;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author enilu
 */
public interface OperationLogService {

  Page getOperationLogs(Page<OperationLog> page, final String beginTime, final String endTime,
      final String logName, final String logType);
}
