package com.zsy.loan.service.system.impl;

import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.bean.vo.node.DeptNode;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.dao.system.DeptRepository;
import com.zsy.loan.service.system.DeptService;
import com.zsy.loan.utils.ToolUtil;
import com.google.common.base.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
@Service
public class DeptServiceImpl implements DeptService {

  @Autowired
  private DeptRepository deptRepository;

  @Override
  public List<ZTreeNode> tree() {
    List<Object[]> list = deptRepository.tree();
    List<ZTreeNode> nodes = new ArrayList<>();
    for (Object[] obj : list) {
      ZTreeNode node = transfer(obj);
      nodes.add(node);
    }
    return nodes;
  }

  @Override
  public List<ZTreeNode> deptTreeListByDeptId(Integer[] ids) {
    List list = deptRepository.deptTreeListByDeptId(ids);
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

  private ZTreeNode transfer(Object[] obj) {
    ZTreeNode node = new ZTreeNode();
    node.setId(Long.valueOf(obj[0].toString()));
    node.setpId(Long.valueOf(obj[1].toString()));
    node.setName(obj[2].toString());
    node.setIsOpen(Boolean.valueOf(obj[3].toString()));
    return node;
  }

  @Override
  public List<Dept> query(String condition) {
    List<Dept> list = new ArrayList<>();
    if (Strings.isNullOrEmpty(condition)) {
      list = (List<Dept>) deptRepository.findAll();
    } else {
      condition = "%" + condition + "%";
      list = deptRepository.findBySimplenameLikeOrFullnameLike(condition, condition);
    }
    return list;
  }

  @Override
  public void deleteDept(Integer deptId) {
    Dept dept = deptRepository.findById(deptId).get();

    List<Dept> subDepts = deptRepository.findByPidsLike("%[" + dept.getId() + "]%");
    deptRepository.deleteAll(subDepts);
    deptRepository.delete(dept);
  }

  @Override
  public List<DeptNode> queryAll() {
    List<Dept> list = (List<Dept>) deptRepository.findAll();

    return generateTree(list);
  }

  @Override
  public void deptSetPids(Dept dept) {
    if (ToolUtil.isEmpty(dept.getPid()) || dept.getPid().equals(0)) {
      dept.setPid(0);
      dept.setPids("[0],");
    } else {
      int pid = dept.getPid();
      Dept temp = deptRepository.findById(pid).get();
      String pids = temp.getPids();
      dept.setPid(pid);
      dept.setPids(pids + "[" + pid + "],");
    }
  }

  private List<DeptNode> generateTree(List<Dept> list) {

    List<DeptNode> nodes = new ArrayList<>(20);
    for (Dept dept : list) {
      DeptNode deptNode = new DeptNode();
      BeanUtils.copyProperties(dept, deptNode);
      nodes.add(deptNode);
    }
    for (DeptNode deptNode : nodes) {
      for (DeptNode child : nodes) {
        if (child.getPid().intValue() == deptNode.getId().intValue()) {
          deptNode.getChildren().add(child);
        }
      }
    }
    List<DeptNode> result = new ArrayList<>(20);
    for (DeptNode node : nodes) {
      if (node.getPid().intValue() == 0) {
        result.add(node);
      }
    }
    return result;


  }


}
