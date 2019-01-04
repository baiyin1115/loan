package com.zsy.loan.service.business.system.impl;

import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.bean.entity.system.Relation;
import com.zsy.loan.dao.system.RelationRepository;
import com.zsy.loan.dao.system.RoleRepository;
import com.zsy.loan.service.business.system.RoleService;
import com.zsy.loan.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2018/3/25 0025.
 *
 * @author enilu
 */
@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private RelationRepository relationRepository;

  @Override
  public List<ZTreeNode> roleTreeList() {
    List list = roleRepository.roleTreeList();
    List<ZTreeNode> treeNodes = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      Object[] arr = (Object[]) list.get(i);
      ZTreeNode node = new ZTreeNode();
      node.setId(Long.valueOf(arr[0].toString()));
      node.setpId(Long.valueOf(arr[1].toString()));
      node.setName(arr[2].toString());
      node.setOpen(Boolean.valueOf(arr[3].toString()));
      treeNodes.add(node);
    }
    return treeNodes;
  }

  @Override
  public List<ZTreeNode> roleTreeListByRoleId(Integer[] ids) {
    List list = roleRepository.roleTreeListByRoleId(ids);
    List<ZTreeNode> treeNodes = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      Object[] arr = (Object[]) list.get(i);
      ZTreeNode node = new ZTreeNode();
      node.setId(Long.valueOf(arr[0].toString()));
      node.setpId(Long.valueOf(arr[1].toString()));
      node.setName(arr[2].toString());
      node.setOpen(Boolean.valueOf(arr[3].toString()));
      node.setChecked(Boolean.valueOf(arr[4].toString()));
      treeNodes.add(node);
    }
    return treeNodes;
  }

  @Override
  public void setAuthority(Integer roleId, String ids) {
    // 删除该角色所有的权限
    relationRepository.deleteByRoleId(roleId);

    // 添加新的权限
    for (Long id : Convert.toLongArray(true, Convert.toStrArray(",", ids))) {
      Relation relation = new Relation();
      relation.setRoleid(roleId);
      relation.setMenuid(id);
      relationRepository.save(relation);
    }
  }

  @Override
  public void delRoleById(Integer roleId) {
    //删除角色
    roleRepository.delete(roleId);

    // 删除该角色所有的权限
    relationRepository.deleteByRoleId(roleId);
  }
}
