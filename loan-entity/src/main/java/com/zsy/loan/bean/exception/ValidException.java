package com.zsy.loan.bean.exception;

public class ValidException extends RuntimeException {

  public ValidException(String msg) {
    super(msg);
  }

  public ValidException(String msg, Throwable e) {
    super(msg, e);
  }
}
