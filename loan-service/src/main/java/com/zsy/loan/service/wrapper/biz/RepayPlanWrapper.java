package com.zsy.loan.service.wrapper.biz;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.BaseControllerWrapper;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 还款计划列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class RepayPlanWrapper extends BaseControllerWrapper {

  public RepayPlanWrapper(Object list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {

    //获取名称
    map.put("orgName", ConstantFactory.me().getDeptName(((Integer) map.get("orgNo")).intValue()));
    map.put("productName", ConstantFactory.me().getProductName(((Long) map.get("productNo"))));
    map.put("custName", ConstantFactory.me().getCustomerName(((Long) map.get("custNo"))));
    map.put("inAcctName", ConstantFactory.me().getAcctName(((Long) map.get("inAcctNo"))));
    map.put("statusName", ConstantFactory.me().getRepayStatusName((Long) map.get("status")));

    //费率部分格式化
    map.put("rateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("rate")));

    //金额部分格式化
    map.put("ctdPrinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("ctdPrin")));
    map.put("ctdInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("ctdInterest")));
    map.put("ctdServFeeFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("ctdServFee")));
    map.put("ctdPenFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("ctdPen")));
    map.put("paidPrinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("paidPrin")));
    map.put("paidInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("paidInterest")));
    map.put("paidServFeeFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("paidServFee")));
    map.put("paidPenFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("paidPen")));
    map.put("wavAmtFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("wavAmt")));

    //时间格式化
    map.put("acctDateFormat", DateUtil.getDay((Date) map.get("acctDate")));
    map.put("beginDateFormat", DateUtil.getDay((Date) map.get("beginDate")));
    map.put("endDateFormat", DateUtil.getDay((Date) map.get("endDate")));
    map.put("ddDateFormat", DateUtil.getDay((Date) map.get("ddDate")));

    Optional<Object> createBy = Optional.ofNullable(map.get("createBy"));
    map.put("createByName", ConstantFactory.me().getUserNameById(Integer.valueOf(createBy.orElse(-1L).toString())));
    Optional<Object> modifiedBy = Optional.ofNullable(map.get("modifiedBy"));
    map.put("modifiedByName", ConstantFactory.me().getUserNameById(Integer.valueOf(modifiedBy.orElse(-1L).toString())));

  }

}
