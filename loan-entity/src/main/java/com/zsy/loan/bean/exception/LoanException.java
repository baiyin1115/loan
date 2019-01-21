package com.zsy.loan.bean.exception;

/**
 * 封装loan的异常
 *
 * @author fengshuonan
 * @Date 2017/12/28 下午10:32
 */
public class LoanException extends RuntimeException {

  private Integer code;

  private String message;

  private String data;

  public LoanException(ServiceExceptionEnum serviceExceptionEnum) {
    this.code = serviceExceptionEnum.getCode();
    this.message = serviceExceptionEnum.getMessage();
  }

  public LoanException(ServiceExceptionEnum serviceExceptionEnum, String data) {
    this.code = serviceExceptionEnum.getCode();
    this.message = serviceExceptionEnum.getMessage();
    this.data = data;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
