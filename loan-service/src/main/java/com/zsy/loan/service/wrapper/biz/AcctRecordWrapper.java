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
 * 交易记录列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class AcctRecordWrapper extends BaseControllerWrapper {

  public AcctRecordWrapper(Object list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {

    map.put("orgName", ConstantFactory.me().getDeptName(((Integer) map.get("orgNo")).intValue()));
    map.put("acctName", ConstantFactory.me().getAcctName(((Long) map.get("acctNo"))));
    map.put("typeName", ConstantFactory.me().getLoanBizTypeName(((Long) map.get("type"))));
    map.put("amtTypeName", ConstantFactory.me().getAmtTypeName(((Long) map.get("amtType"))));
    map.put("statusName", ConstantFactory.me().getProcessStatusName((Long) map.get("status")));

    //时间格式化
    map.put("acctDateFormat", DateUtil.getDay((Date) map.get("acctDate")));

    //金额部分格式化
    map.put("amtFormat", BigDecimalUtil.formatAmt((BigDecimal) map.get("amt")));

    Optional<Object> createBy = Optional.ofNullable(map.get("createBy"));
    map.put("createByName", ConstantFactory.me().getUserNameById(Integer.valueOf(createBy.orElse(-1L).toString())));
    Optional<Object> modifiedBy = Optional.ofNullable(map.get("modifiedBy"));
    map.put("modifiedByName", ConstantFactory.me().getUserNameById(Integer.valueOf(modifiedBy.orElse(-1L).toString())));

  }

}
