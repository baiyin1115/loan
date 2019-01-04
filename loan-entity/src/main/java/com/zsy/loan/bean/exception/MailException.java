package com.zsy.loan.bean.exception;

public class MailException extends Exception {

  public MailException(String msg) {
    super(msg);
  }

  public MailException(String msg, Throwable e) {
    super(msg, e);
  }
}
