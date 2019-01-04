package com.zsy.loan.admin.core.util;

import com.zsy.loan.bean.constant.Const;
import com.zsy.loan.admin.config.properties.LoanProperties;
import com.zsy.loan.bean.vo.SpringContextHolder;
import com.zsy.loan.bean.vo.node.MenuNode;

import java.util.ArrayList;
import java.util.List;

/**
 * api接口文档显示过滤
 *
 * @author fengshuonan
 * @date 2017-08-17 16:55
 */
public class ApiMenuFilter extends MenuNode {


  public static List<MenuNode> build(List<MenuNode> nodes) {

    //如果关闭了接口文档,则不显示接口文档菜单
    LoanProperties LoanProperties = SpringContextHolder.getBean(LoanProperties.class);
    if (!LoanProperties.getSwaggerOpen()) {
      List<MenuNode> menuNodesCopy = new ArrayList<>();
      for (MenuNode menuNode : nodes) {
        if (Const.API_MENU_NAME.equals(menuNode.getName())) {
          continue;
        } else {
          menuNodesCopy.add(menuNode);
        }
      }
      nodes = menuNodesCopy;
    }

    return nodes;
  }
}
