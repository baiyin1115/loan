package com.zsy.loan.bean.dictmap.biz;

import com.zsy.loan.bean.dictmap.AbstractDictMap;

/**
 * 账户的映射
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class LoanDict extends AbstractDictMap {

  @Override
  public void init() {
//    put("id ","账户");
//    put("userNo","用户编号");
//    put("availableBalance","可用余额");
//    put("freezeBalance","冻结余额");
//    put("acctType","账户类型");
//    put("balanceType","余额性质");
//    put("status","账户状态");
//    put("remark","备注");
  }

  @Override
  protected void initBeWrapped() {
//    putFieldWrapperMethodName("acctType", "getAcctTypeName");
//    putFieldWrapperMethodName("balanceType", "getBalanceTypeName");
//    putFieldWrapperMethodName("status", "getAcctStatusName");
  }
}