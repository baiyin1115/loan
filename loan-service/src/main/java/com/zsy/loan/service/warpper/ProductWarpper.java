package com.zsy.loan.service.warpper;

import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.utils.ToolUtil;
import java.util.Map;

/**
 * 产品列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class ProductWarpper extends BaseControllerWarpper {

  public ProductWarpper(Object list) {
    super(list);
  }

  @Override
  public void warpTheMap(Map<String, Object> map) {

    Integer id = (Integer) map.get("id");


  }

}
