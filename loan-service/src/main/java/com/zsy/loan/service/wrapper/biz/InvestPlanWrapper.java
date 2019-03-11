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
public class InvestPlanWrapper extends BaseControllerWrapper {

  public InvestPlanWrapper(Object list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {

    //获取名称
    map.put("orgName", ConstantFactory.me().getDeptName(((Integer) map.get("orgNo")).intValue()));
    map.put("custName", ConstantFactory.me().getCustomerName(((Long) map.get("custNo"))));
    map.put("statusName", ConstantFactory.me().getInvestPlanStatusName((Long) map.get("status")));

    //费率部分格式化
    map.put("rateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("rate")));

    //金额部分格式化
    map.put("ddPrinFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("ddPrin")));
    map.put("chdInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("chdInterest")));
    map.put("paidInterestFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("paidInterest")));

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
