package com.zsy.loan.service.wrapper.biz;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.BaseControllerWrapper;
import com.zsy.loan.utils.BigDecimalUtil;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 产品列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class ProductWrapper extends BaseControllerWrapper {

  public ProductWrapper(Object list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {

    map.put("orgNoName", ConstantFactory.me().getDeptName(((Integer) map.get("orgNo")).intValue()));
    map.put("serviceFeeTypeName", ConstantFactory.me().getServiceFeeTypeName((Long) map.get("serviceFeeType")));
    map.put("isPenName", ConstantFactory.me().getIsPenName((Long) map.get("isPen")));
    map.put("penNumberName", ConstantFactory.me().getPenNumberName((Long) map.get("penNumber")));
    map.put("repayTypeName", ConstantFactory.me().getRepayTypeName((Long) map.get("repayType")));
    map.put("loanTypeName", ConstantFactory.me().getLoanTypeName((Long) map.get("loanType")));

    map.put("rateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("rate")));
    map.put("serviceFeeScaleFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("serviceFeeScale")));
    map.put("penRateFormat", BigDecimalUtil.formatHundred((BigDecimal) map.get("penRate")));
    map.put("cycleIntervalFormat", map.get("cycleInterval")+"天");


//    put("orgNo", "公司");
//    put("productName", "产品名称");
//    put("rate", "产品利率");
//    put("serviceFeeScale", "服务费比例");
//    put("serviceFeeType", "服务费收取方式");
//    put("penRate", "罚息利率");
//    put("isPen", "是否罚息");
//    put("penNumber", "罚息基数");
//    put("repayType", "还款方式");
//    put("loanType", "贷款类型");
//    put("cycleInterval", "周期间隔");

  }

}
