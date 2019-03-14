package com.zsy.loan.bean.enumeration;

import com.zsy.loan.bean.exception.ServiceExceptionEnum;

/**
 * @author fengshuonan
 * @Description 所有业务异常的枚举
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum implements ServiceExceptionEnum {

  NOT_EXISTED_ING(401, "当前业务不存在或者正在处理中"),
  PARAMETER_ERROR(401, "数据错误"),
  NOT_FOUND(404, "未查询到符合条件的业务"),
  CALCULATE_REQ_NOT_MATCH(401, "请求与上次试算结果不一致"),
  STATUS_ERROR(401, "当前状态不支持这个业务"),
  DATE_COMPARE_ERROR(401, "日期比较错误"),

  /**
   * 字典
   */
  DICT_EXISTED(400, "字典已经存在"),
  ERROR_CREATE_DICT(500, "创建字典失败"),
  ERROR_WRAPPER_FIELD(500, "包装字典属性失败"),


  /**
   * 文件上传
   */
  FILE_READING_ERROR(400, "FILE_READING_ERROR!"),
  FILE_NOT_FOUND(400, "FILE_NOT_FOUND!"),
  UPLOAD_ERROR(500, "上传图片出错"),

  /**
   * 权限和数据问题
   */
  DB_RESOURCE_NULL(400, "数据库中没有该资源"),
  NO_PERMITION(405, "权限异常"),
  REQUEST_INVALIDATE(400, "请求数据格式不正确"),
  INVALID_KAPTCHA(400, "验证码不正确"),
  CANT_DELETE_ADMIN(600, "不能删除超级管理员"),
  CANT_FREEZE_ADMIN(600, "不能冻结超级管理员"),
  CANT_CHANGE_ADMIN(600, "不能修改超级管理员"),

  /**
   * 用户问题
   */
  USER_ALREADY_REG(401, "该用户已经注册"),
  NO_THIS_USER(400, "没有此用户"),
  USER_NOT_EXISTED(400, "没有此用户"),
  OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
  TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),

  /**
   * 账户问题
   */
  NO_THIS_ACCOUNT(400, "没有此账户"),
  ACCOUNT_FREEZED(401, "账号被冻结"),
  ACCOUNT_CUSTOMER_FREEZED(401, "客户有效"),
  ACCOUNT_BALANCE_NOT_ZERO(401, "账户有余额"),
  ACCOUNT_NO_ADD(401, "账户不能开立"),
  ACCOUNT_NO_OVERDRAW(401, "账户不能透支"),

  /**
   * 贷款问题
   */
  LOAN_DATE(401, "结束日期必须在开始日期之后"),
  LOAN_LENDING_DATE(401, "放款日期必须在开始日期之后"),
  LOAN_NOT_CHECK_IN(401, "不是登记状态"),
  LOAN_DD_DATE(401, "还款日期必须在当前账务日期之前"),

  /**
   * 融资问题
   */
  INVEST_AMT(401, "金额错误"),

  /**
   * 客户问题
   */
  NO_THIS_CUSTOMER(400, "没有此客户"),
  CUSTOMER_ACCOUNT_INCONSISTENT(401, "客户状态和用户状态不一致"),
  CUSTOMER_FREEZED(401, "客户被冻结"),

  /**
   * 错误的请求
   */
  MENU_PCODE_COINCIDENCE(400, "菜单编号和副编号不能一致"),
  EXISTED_THE_MENU(400, "菜单编号重复，不能添加"),
  DICT_MUST_BE_NUMBER(400, "字典的值必须为数字"),
  REQUEST_NULL(400, "请求有错误"),
  SESSION_TIMEOUT(400, "会话超时"),
  SERVER_ERROR(500, "服务器异常");

  BizExceptionEnum(int code, String message) {
    this.code = code;
    this.message = message;
  }

  private Integer code;

  private String message;

  @Override
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
}
