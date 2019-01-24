package com.zsy.loan.bean.constant;

/**
 * 系统常量
 *
 * @author fengshuonan
 * @date 2017年2月12日 下午9:42:53
 */
public interface Const {

  /**
   * 系统默认的管理员密码
   */
  String DEFAULT_PWD = "111111";

  /**
   * 管理员角色的名字
   */
  String ADMIN_NAME = "administrator";

  /**
   * 管理员id
   */
  Integer ADMIN_ID = 1;

  /**
   * 超级管理员角色id
   */
  Integer ADMIN_ROLE_ID = 1;

  /**
   * 接口文档的菜单名
   */
  String API_MENU_NAME = "接口文档";

  /**
   * 客户号起始 融资151开头,借款251开头
   */
  //融资人
  long CUSTOMER_NO_BEGIN_INVEST = 15100000000L;
  //借款人
  long CUSTOMER_NO_BEGIN_LOAN = 25100000000L;
  //公司账户统一应用
  long CUSTOMER_NO_BEGIN_COMPANY = 15000000001L;

  /**
   * 账户号起始 融资账户171开头,借款账户271开头,公司卡账户471开头, 暂收371，暂付372，代偿370
   */
  //融资人
  long ACCT_NO_BEGIN_INVEST = 17100000000L;
  //借款人
  long ACCT_NO_BEGIN_LOAN = 27100000000L;
  //公司账户
  long ACCT_NO_BEGIN_COMPANY = 37100000000L;
  //代偿账户
  long ACCT_NO_BEGIN_REPLACE = 37000000000L;
  //往来户-暂收
  long ACCT_NO_BEGIN_INTERIOR_IN = 37100000000L;
  //往来户-暂付
  long ACCT_NO_BEGIN_INTERIOR_OUT = 37200000000L;

  /**
   * sequence 关键字
   */
  String ACCT_KEY = "ACCT";
  String CUSTOMER_KEY = "CUSTOMER";


}
