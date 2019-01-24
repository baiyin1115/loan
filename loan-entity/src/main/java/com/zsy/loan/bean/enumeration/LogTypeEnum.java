package com.zsy.loan.bean.enumeration;

/**
 * 日志类型
 *
 * @author fengshuonan
 * @Date 2017年1月22日 下午12:14:59
 */
public enum LogTypeEnum {

  LOGIN("登录日志"),
  LOGIN_FAIL("登录失败日志"),
  EXIT("退出日志"),
  EXCEPTION("异常日志"),
  BUSSINESS("业务日志");

  String message;

  LogTypeEnum(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
