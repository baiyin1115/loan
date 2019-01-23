package com.zsy.loan.service.warpper.system;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.warpper.BaseControllerWarpper;
import java.util.List;
import java.util.Map;

/**
 * 角色列表的包装类
 *
 * @author fengshuonan
 * @date 2017年2月19日10:59:02
 */
public class RoleWarpper extends BaseControllerWarpper {

  public RoleWarpper(List<Map<String, Object>> list) {
    super(list);
  }

  @Override
  public void warpTheMap(Map<String, Object> map) {
    map.put("pName", ConstantFactory.me().getSingleRoleName((Integer) map.get("pid")));
//    map.putToken("deptName", ConstantFactory.me().getDeptName((Integer) map.getToken("deptid")));
  }

}
