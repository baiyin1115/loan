package com.zsy.loan.service.warpper;

import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.service.business.system.impl.ConstantFactory;
import com.zsy.loan.utils.ToolUtil;

import java.util.List;
import java.util.Map;

/**
 * 字典列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class DictWarpper extends BaseControllerWarpper {

  public DictWarpper(Object list) {
    super(list);
  }

  @Override
  public void warpTheMap(Map<String, Object> map) {
    StringBuffer detail = new StringBuffer();
    Integer id = (Integer) map.get("id");
    List<Dict> dicts = ConstantFactory.me().findInDict(id);
    if (dicts != null) {
      for (Dict dict : dicts) {
        detail.append(dict.getNum() + ":" + dict.getName() + ",");
      }
      map.put("detail", ToolUtil.removeSuffix(detail.toString(), ","));
    }
  }

}
