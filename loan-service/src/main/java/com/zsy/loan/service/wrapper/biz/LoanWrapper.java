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
 * 借据列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class LoanWrapper extends BaseControllerWrapper {

  public LoanWrapper(Object list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {

    //获取名称
    map.put("orgName", ConstantFactory.me().getDeptName(((Integer) map.get("orgNo")).intValue()));
    map.put("productName", ConstantFactory.me().getProductName(((Long) map.get("productNo"))));
    map.put("custName", ConstantFactory.me().getCustomerName(((Long) map.get("custNo"))));
    map.put("lendingAcctName", ConstantFactory.me().getAcctName(((Long) map.get("lendingAcct"))));
    map.put("loanTypeName", ConstantFactory.me().getLoanTypeName((Long) map.get("loanType")));
    map.put("serviceFeeTypeName", ConstantFactory.me().getServiceFeeTypeName((Long) map.get("serviceFeeType")));
    map.put("repayTypeName", ConstantFactory.me().getRepayTypeName((Long) map.get("repayType")));
    map.put("isPenName", ConstantFactory.me().getIsPenName((Long) map.get("isPen")));
    map.put("penNumberName", ConstantFactory.me().getPenNumberName((Long) map.get("penNumber")));
    map.put("statusName", ConstantFactory.me().getLoanStatusName((Long) map.get("status")));

    //费率部分格式化
    map.put("rateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("rate")));
    map.put("serviceFeeScaleFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("serviceFeeScale")));
    map.put("penRateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("penRate")));
    map.put("extensionRateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("extensionRate")));

    //金额部分格式化
    map.put("prinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("prin")));
    map.put("serviceFeeFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("serviceFee")));
    map.put("receiveInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("receiveInterest")));
    map.put("lendingAmtFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("lendingAmt")));
    map.put("schdPrinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("schdPrin")));
    map.put("schdInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("schdInterest")));
    map.put("schdServFeeFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("schdServFee")));
    map.put("schdPenFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("schdPen")));
    map.put("totPaidPrinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totPaidPrin")));
    map.put("totPaidInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totPaidInterest")));
    map.put("totPaidServFeeFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totPaidServFee")));
    map.put("totPaidPenFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totPaidPen")));
    map.put("totWavAmtFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totWavAmt")));

    //时间格式化
    map.put("acctDateFormat", DateUtil.getDay((Date) map.get("acctDate")));
    map.put("beginDateFormat", DateUtil.getDay((Date) map.get("beginDate")));
    map.put("endDateFormat", DateUtil.getDay((Date) map.get("endDate")));
    map.put("lendingDateFormat", DateUtil.getDay((Date) map.get("lendingDate")));

    Optional<Object> createBy = Optional.ofNullable(map.get("createBy"));
    map.put("createByName", ConstantFactory.me().getUserNameById(Integer.valueOf(createBy.orElse(-1L).toString())));
    Optional<Object> modifiedBy = Optional.ofNullable(map.get("modifiedBy"));
    map.put("modifiedByName", ConstantFactory.me().getUserNameById(Integer.valueOf(modifiedBy.orElse(-1L).toString())));

  }

}
