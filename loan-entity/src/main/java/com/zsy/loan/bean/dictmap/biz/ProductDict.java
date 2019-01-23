package com.zsy.loan.bean.dictmap.biz;

import com.zsy.loan.bean.dictmap.AbstractDictMap;

/**
 * 产品的映射
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class ProductDict extends AbstractDictMap {

  @Override
  public void init() {
    put("orgNo", "公司");
    put("productName", "产品名称");
    put("rate", "产品利率");
    put("serviceFeeScale", "服务费比例");
    put("serviceFeeType", "服务费收取方式");
    put("penRate", "罚息利率");
    put("isPen", "是否罚息");
    put("penNumber", "罚息基数");
    put("repayType", "还款方式");
    put("loanType", "贷款类型");
    put("cycleInterval", "周期间隔");

  }

  @Override
  protected void initBeWrapped() {
    putFieldWrapperMethodName("orgNo", "getOrgNoName");
    putFieldWrapperMethodName("serviceFeeType", "getServiceFeeTypeName");
    putFieldWrapperMethodName("isPen", "getIsPenName");
    putFieldWrapperMethodName("penNumber", "getPenNumberName");
    putFieldWrapperMethodName("repayType", "getRepayTypeName");
    putFieldWrapperMethodName("loanType", "getLoanTypeName");
  }
}
