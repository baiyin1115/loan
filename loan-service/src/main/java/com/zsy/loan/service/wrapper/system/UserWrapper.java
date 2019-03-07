package com.zsy.loan.service.wrapper.system;


import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.BaseControllerWrapper;
import com.zsy.loan.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:47:03
 */
public class UserWrapper extends BaseControllerWrapper {

  public UserWrapper(List<Map<String, Object>> list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {
    map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
    if (StringUtils.isNotNullOrEmpty(map.get("roleid"))) {
      map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleid")));
    }
    if(StringUtils.isNotNullOrEmpty(map.get("roleName")) && map.get("roleName").toString().contains("超级管理员")){
      map.put("deptName","ALL");
    }else{
      if (StringUtils.isNotNullOrEmpty(map.get("deptid"))) {
        map.put("deptName", ConstantFactory.me().getDeptName((String) map.get("deptid")));
      }
    }

    map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));

  }

}
