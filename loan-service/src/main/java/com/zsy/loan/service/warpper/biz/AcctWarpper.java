package com.zsy.loan.service.warpper.biz;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.warpper.BaseControllerWarpper;
import com.zsy.loan.utils.BigDecimalUtil;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * 账户列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class AcctWarpper extends BaseControllerWarpper {

  public AcctWarpper(Object list) {
    super(list);
  }

  @Override
  public void warpTheMap(Map<String, Object> map) {

    map.put("acctTypeName", ConstantFactory.me().getAcctTypeName((Long) map.get("acctType")));
    map.put("balanceTypeName", ConstantFactory.me().getBalanceTypeName((Long) map.get("balanceType")));
    map.put("statusName", ConstantFactory.me().getAcctStatusName((Long)map.get("status")));

    map.put("freezeBalance", BigDecimalUtil.formatAmt((BigDecimal)map.get("freezeBalance")));
    map.put("availableBalance", BigDecimalUtil.formatAmt((BigDecimal)map.get("availableBalance")));


    Optional<Object> createBy = Optional.ofNullable(map.get("createBy"));
    map.put("createByName", ConstantFactory.me().getUserNameById(Integer.valueOf(createBy.orElse(-1L).toString())));

    Optional<Object> modifiedBy = Optional.ofNullable(map.get("modifiedBy"));
    map.put("modifiedByName", ConstantFactory.me().getUserNameById(Integer.valueOf(modifiedBy.orElse(-1L).toString())));

  }

}
