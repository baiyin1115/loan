package com.zsy.loan.service.system;


import com.zsy.loan.bean.entity.system.LoginLog;
import com.zsy.loan.utils.factory.Page;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author enilu
 */
public interface LoginLogService {

  Page getLoginLogs(Page<LoginLog> page, final String beginTime, final String endTime,
      final String logName);
}
