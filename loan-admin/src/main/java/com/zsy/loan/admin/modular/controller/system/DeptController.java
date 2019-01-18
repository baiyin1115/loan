package com.zsy.loan.admin.modular.controller.system;

import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.dictmap.DeptDict;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.system.DeptRepository;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.system.DeptService;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.warpper.DeptWarpper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.utils.Convert;
import com.zsy.loan.utils.ToolUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门控制器
 *
 * @author fengshuonan
 * @Date 2017年2月17日20:27:22
 */
@Controller
@RequestMapping("/dept")
public class DeptController extends BaseController {

  private String PREFIX = "/system/dept/";

  @Resource
  DeptService deptService;

  @Resource
  DeptRepository deptRepository;

  @Resource
  UserRepository userRepository;


  /**
   * 跳转到部门管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "dept.html";
  }

  /**
   * 跳转到添加部门
   */
  @RequestMapping("/dept_add")
  public String deptAdd() {
    return PREFIX + "dept_add.html";
  }

  /**
   * 跳转到修改部门
   */
  @Permission
  @RequestMapping("/dept_update/{deptId}")
  public String deptUpdate(@PathVariable Integer deptId, Model model) {
    Dept dept = deptRepository.findOne(deptId);
    model.addAttribute(dept);
    model.addAttribute("pName", ConstantFactory.me().getDeptName(dept.getPid()));
    LogObjectHolder.me().set(dept);
    return PREFIX + "dept_edit.html";
  }

  /**
   * 获取部门的tree列表
   */
  @RequestMapping(value = "/tree")
  @ResponseBody
  public List<ZTreeNode> tree() {
    List<ZTreeNode> tree = this.deptService.tree();
    tree.add(ZTreeNode.createParent());
    return tree;
  }

  /**
   * 新增部门
   */
  @BussinessLog(value = "添加部门", key = "simplename", dict = DeptDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  public Object add(Dept dept) {
    if (ToolUtil.isOneEmpty(dept, dept.getSimplename())) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    //完善pids,根据pid拿到pid的pids
    deptService.deptSetPids(dept);
    return this.deptRepository.save(dept);
  }

  /**
   * 获取所有部门列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  public Object list(String condition) {
    List<Dept> list = this.deptService.query(condition);
    return super.warpObject(new DeptWarpper(BeanUtil.objectsToMaps(list)));
  }

  /**
   * 部门详情
   */
  @RequestMapping(value = "/detail/{deptId}")
  @Permission
  @ResponseBody
  public Object detail(@PathVariable("deptId") Integer deptId) {
    return deptRepository.findOne(deptId);
  }

  /**
   * 修改部门
   */
  @BussinessLog(value = "修改部门", key = "simplename", dict = DeptDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  public Object update(Dept dept) {
    if (ToolUtil.isEmpty(dept) || dept.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    deptService.deptSetPids(dept);
    deptRepository.save(dept);
    return SUCCESS_TIP;
  }

  /**
   * 删除部门
   */
  @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
  @RequestMapping(value = "/delete")
  @Permission
  @ResponseBody
  public Object delete(@RequestParam Integer deptId) {
    //缓存被删除的部门名称
    LogObjectHolder.me().set(ConstantFactory.me().getDeptName(deptId));
    deptService.deleteDept(deptId);
    return SUCCESS_TIP;
  }

  /**
   * 获取角色列表
   */
  @RequestMapping(value = "/deptTreeListByUserId/{userId}")
  @ResponseBody
  public List<ZTreeNode> deptTreeListByUserId(@PathVariable Integer userId) {
    User theUser = this.userRepository.findOne(userId);
    String deptId = theUser.getDeptid();
    if (ToolUtil.isEmpty(deptId)) {
      List<ZTreeNode> tree = this.deptService.tree();
      tree.add(ZTreeNode.createParent());
      return tree;
    } else {
      Integer[] deptIds = Convert.toIntArray(",", deptId);
      List<ZTreeNode> treeListByUserId = this.deptService.deptTreeListByDeptId(deptIds);
      return treeListByUserId;
    }
  }

}
