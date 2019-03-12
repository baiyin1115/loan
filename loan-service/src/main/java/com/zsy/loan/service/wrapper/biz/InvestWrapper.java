package com.zsy.loan.service.wrapper.biz;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.BaseControllerWrapper;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
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
public class InvestWrapper extends BaseControllerWrapper {

  public InvestWrapper(Object list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {

    //获取名称
    map.put("orgName", ConstantFactory.me().getDeptName(((Integer) map.get("orgNo")).intValue()));
    map.put("custName", ConstantFactory.me().getCustomerName(((Long) map.get("custNo"))));
    map.put("inAcctName", ConstantFactory.me().getAcctName(((Long) map.get("inAcctNo"))));
    map.put("statusName", ConstantFactory.me().getInvestStatusName((Long) map.get("status")));
    map.put("investTypeName", ConstantFactory.me().getInvestTypeName((Integer) map.get("investType")));

    //费率部分格式化
    map.put("rateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("rate")));
    map.put("extensionRateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("extensionRate")));

    //金额部分格式化
    map.put("prinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("prin")));
    map.put("totSchdInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totSchdInterest")));
    map.put("totAccruedInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totAccruedInterest")));
    map.put("totPaidPrinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totPaidPrin")));
    map.put("totPaidInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totPaidInterest")));
    map.put("totWavAmtFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("totWavAmt")));

    //时间格式化
    map.put("acctDateFormat", DateUtil.getDay((Date) map.get("acctDate")));
    map.put("beginDateFormat", DateUtil.getDay((Date) map.get("beginDate")));
    map.put("endDateFormat", DateUtil.getDay((Date) map.get("endDate")));

    Optional<Object> createBy = Optional.ofNullable(map.get("createBy"));
    map.put("createByName", ConstantFactory.me().getUserNameById(Integer.valueOf(createBy.orElse(-1L).toString())));
    Optional<Object> modifiedBy = Optional.ofNullable(map.get("modifiedBy"));
    map.put("modifiedByName", ConstantFactory.me().getUserNameById(Integer.valueOf(modifiedBy.orElse(-1L).toString())));
  }

}
