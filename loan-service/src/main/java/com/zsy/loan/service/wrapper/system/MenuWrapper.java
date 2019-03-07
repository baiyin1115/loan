package com.zsy.loan.service.wrapper.system;

import com.zsy.loan.bean.vo.node.IsMenu;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.BaseControllerWrapper;
import java.util.List;
import java.util.Map;

/**
 * 菜单列表的包装类
 *
 * @author fengshuonan
 * @date 2017年2月19日15:07:29
 */
public class MenuWrapper extends BaseControllerWrapper {

  public MenuWrapper(List<Map<String, Object>> list) {
    super(list);
  }

  @Override
  public void wrapTheMap(Map<String, Object> map) {
    map.put("statusName", ConstantFactory.me().getMenuStatusName((Integer) map.get("status")));
    map.put("isMenuName", IsMenu.valueOf((Integer) map.get("ismenu")));
  }

}
