package com.zsy.loan.service.warpper;

import com.zsy.loan.service.system.impl.ConstantFactory;
import java.util.Map;

/**
 * 部门列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class NoticeWrapper extends BaseControllerWarpper {

  public NoticeWrapper(Object list) {
    super(list);
  }

  @Override
  public void warpTheMap(Map<String, Object> map) {
    Integer creater = (Integer) map.get("creater");
    map.put("createrName", ConstantFactory.me().getUserNameById(creater));
  }

}
