package com.zsy.loan.service.warpper.biz;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.warpper.BaseControllerWarpper;
import com.zsy.loan.utils.ExactCompute;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * 客户列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class CustomerWarpper extends BaseControllerWarpper {

  public CustomerWarpper(Object list) {
    super(list);
  }

  @Override
  public void warpTheMap(Map<String, Object> map) {

    Optional<Object> sex = Optional.ofNullable(map.get("sex"));
    map.put("sexName", ConstantFactory.me().getSexName(Integer.valueOf(sex.orElse(-1L).toString())));
    map.put("typeName", ConstantFactory.me().getCustomerTypeName((Long) map.get("type")));
    map.put("statusName", ConstantFactory.me().getCustomerStatusName((Long) map.get("status")));
    map.put("certTypeName", ConstantFactory.me().getIdCardTypeName(map.get("certType").toString()));

    Optional<Object> createBy = Optional.ofNullable(map.get("createBy"));
    map.put("createByName", ConstantFactory.me().getUserNameById(Integer.valueOf(createBy.orElse(-1L).toString())));

    Optional<Object> modifiedBy = Optional.ofNullable(map.get("modifiedBy"));
    map.put("modifiedByName", ConstantFactory.me().getUserNameById(Integer.valueOf(modifiedBy.orElse(-1L).toString())));

  }

}
