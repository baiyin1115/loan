package com.zsy.loan.service.platform.log;

import com.zsy.loan.bean.enumeration.LogSucceedEnum;
import com.zsy.loan.bean.enumeration.LogTypeEnum;
import com.zsy.loan.bean.entity.system.LoginLog;
import com.zsy.loan.bean.entity.system.OperationLog;

import java.util.Date;

/**
 * 日志对象创建工厂
 *
 * @author fengshuonan
 * @date 2016年12月6日 下午9:18:27
 */
public class LogFactory {

  /**
   * 创建操作日志
   *
   * @author fengshuonan
   * @Date 2017/3/30 18:45
   */
  public static OperationLog createOperationLog(LogTypeEnum logType, Long userId, String bussinessName,
      String clazzName, String methodName, String msg, LogSucceedEnum succeed) {
    OperationLog operationLog = new OperationLog();
    operationLog.setLogtype(logType.getMessage());
    operationLog.setLogname(bussinessName);
    operationLog.setUserid(userId.intValue());
    operationLog.setClassname(clazzName);
    operationLog.setMethod(methodName);
    operationLog.setCreatetime(new Date());
    operationLog.setSucceed(succeed.getMessage());
    operationLog.setMessage(msg);
    return operationLog;
  }

  /**
   * 创建登录日志
   *
   * @author fengshuonan
   * @Date 2017/3/30 18:46
   */
  public static LoginLog createLoginLog(LogTypeEnum logType, Long userId, String msg, String ip) {
    LoginLog loginLog = new LoginLog();
    loginLog.setLogname(logType.getMessage());
    loginLog.setUserid(userId.intValue());
    loginLog.setCreatetime(new Date());
    loginLog.setSucceed(LogSucceedEnum.SUCCESS.getMessage());
    loginLog.setIp(ip);
    loginLog.setMessage(msg);
    return loginLog;
  }
}
