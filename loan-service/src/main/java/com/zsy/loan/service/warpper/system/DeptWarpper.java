package com.zsy.loan.service.warpper.system;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.warpper.BaseControllerWarpper;
import com.zsy.loan.utils.ToolUtil;
import java.util.Map;

/**
 * 部门列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class DeptWarpper extends BaseControllerWarpper {

  public DeptWarpper(Object list) {
    super(list);
  }

  @Override
  public void warpTheMap(Map<String, Object> map) {

    Integer pid = (Integer) map.get("pid");

    if (ToolUtil.isEmpty(pid) || pid.equals(0)) {
      map.put("pName", "--");
    } else {
      map.put("pName", ConstantFactory.me().getDeptName(pid));
    }
  }

}
