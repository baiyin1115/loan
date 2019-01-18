package com.zsy.loan.service.system;

import com.zsy.loan.bean.vo.node.ZTreeNode;


import java.util.List;

/**
 * Created  on 2018/3/25 0025.
 *
 * @author enilu
 */
public interface RoleService {

  List<ZTreeNode> roleTreeList();

  List<ZTreeNode> roleTreeListByRoleId(Integer[] ids);

  void setAuthority(Integer roleId, String ids);

  void delRoleById(Integer roleId);

}
