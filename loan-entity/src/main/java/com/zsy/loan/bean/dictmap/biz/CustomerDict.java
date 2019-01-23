package com.zsy.loan.bean.dictmap.biz;

import com.zsy.loan.bean.dictmap.AbstractDictMap;

/**
 * 客户的映射
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class CustomerDict extends AbstractDictMap {

  @Override
  public void init() {
    put("id"," 客户编号");
    put("certNo","证件号码");
    put("certType","证件类型");
    put("name","客户姓名");
    put("sex","性别");
    put("mobile","手机号");
    put("phone","电话");
    put("email","电子邮箱");
    put("type","客户类型");
    put("status","客户状态");

  }

  @Override
  protected void initBeWrapped() {
    putFieldWrapperMethodName("certType", "getIdCardTypeName");
    putFieldWrapperMethodName("sex", "getSexName");
    putFieldWrapperMethodName("type", "getCustomerTypeName");
    putFieldWrapperMethodName("status", "getCustomerStatusName");
  }
}