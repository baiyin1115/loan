package com.zsy.loan.service.system;

import com.zsy.loan.bean.vo.node.DeptNode;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.bean.entity.system.Dept;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface DeptService {

  List<ZTreeNode> tree();

  List<Dept> query(String condition);

  void deleteDept(Integer deptId);

  List<DeptNode> queryAll();

  void deptSetPids(Dept dept);

  List<ZTreeNode> deptTreeListByDeptId(Integer[] deptIds);
}
