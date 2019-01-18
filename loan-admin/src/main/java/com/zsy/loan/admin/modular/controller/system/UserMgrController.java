package com.zsy.loan.admin.modular.controller.system;

import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.constant.Const;
import com.zsy.loan.bean.dictmap.UserDict;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.admin.config.properties.LoanProperties;
import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.base.tips.Tip;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.core.ShiroUser;
import com.zsy.loan.bean.dto.UserDto;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.UserService;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.factory.UserFactory;
import com.zsy.loan.service.shiro.ShiroKit;
import com.zsy.loan.service.warpper.UserWarpper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.bean.constant.state.ManagerStatus;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.utils.Convert;
import com.zsy.loan.utils.MD5;
import com.zsy.loan.utils.ToolUtil;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * 系统管理员控制器
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午1:08:17
 */
@Controller
@RequestMapping("/mgr")
public class UserMgrController extends BaseController {

  private static String PREFIX = "/system/user/";

  @Resource
  private LoanProperties LoanProperties;
  @Autowired

  @Resource
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  /**
   * 跳转到查看管理员列表的页面
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "user.html";
  }

  /**
   * 跳转到查看管理员列表的页面
   */
  @RequestMapping("/user_add")
  public String addView() {
    return PREFIX + "user_add.html";
  }

  /**
   * 跳转到角色分配页面
   */
  //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
  @Permission
  @RequestMapping("/role_assign/{userId}")
  public String roleAssign(@PathVariable Integer userId, Model model) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    User user = userRepository.findOne(userId);
    model.addAttribute("userId", userId);
    model.addAttribute("userAccount", user.getAccount());
    return PREFIX + "user_roleassign.html";
  }

  /**
   * 跳转到部门分配页面
   */
  //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
  @Permission
  @RequestMapping("/depo_assign/{userId}")
  public String depoAssign(@PathVariable Integer userId, Model model) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    User user = userRepository.findOne(userId);
    model.addAttribute("userId", userId);
    model.addAttribute("userAccount", user.getAccount());
    return PREFIX + "user_depoassign.html";
  }

  /**
   * 跳转到编辑管理员页面
   */
  @Permission
  @RequestMapping("/user_edit/{userId}")
  public String userEdit(@PathVariable Integer userId, Model model) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    assertAuth(userId);
    User user = this.userRepository.findOne(userId);
    model.addAttribute(user);
    model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
    model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));

    LogObjectHolder.me().set(user);
    return PREFIX + "user_edit.html";
  }

  /**
   * 跳转到查看用户详情页面
   */
  @RequestMapping("/user_info")
  public String userInfo(Model model) {
    Long userId = ShiroKit.getUser().getId();
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    User user = userRepository.findOne(userId.intValue());
    model.addAttribute(user);
    model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
    model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
    LogObjectHolder.me().set(user);
    return PREFIX + "user_view.html";
  }

  /**
   * 跳转到修改密码界面
   */
  @RequestMapping("/user_chpwd")
  public String chPwd() {
    return PREFIX + "user_chpwd.html";
  }

  /**
   * 修改当前用户的密码
   */
  @RequestMapping("/changePwd")
  @ResponseBody
  public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd,
      @RequestParam String rePwd) {
    if (!newPwd.equals(rePwd)) {
      throw new LoanException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
    }
    Long userId = ShiroKit.getUser().getId();
    User user = userRepository.findOne(userId.intValue());
    String oldMd5 = MD5.md5(oldPwd, user.getSalt());
    if (user.getPassword().equals(oldMd5)) {
      String newMd5 = MD5.md5(newPwd, user.getSalt());
      user.setPassword(newMd5);
      userRepository.save(user);
      return SUCCESS_TIP;
    } else {
      throw new LoanException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
    }
  }

  /**
   * 查询管理员列表
   */
  @RequestMapping("/list")
  @Permission
  @ResponseBody
  public Object list(@RequestParam(required = false) String name,
      @RequestParam(required = false) String beginTime,
      @RequestParam(required = false) String endTime,
      @RequestParam(required = false) Integer deptid) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name);
    params.put("beginTime", beginTime);
    params.put("endTime", endTime);
    if (ShiroKit.isAdmin()) {
      User user = new User();
      if (!Strings.isNullOrEmpty(name)) {
        user.setName(name);
        user.setAccount(name);
      }
      if (deptid != null && deptid != 0) {
        params.put("deptid", deptid);
      }

      List<User> users = userService.findAll(params);
      return new UserWarpper(BeanUtil.objectsToMaps(users)).warp();
    } else {
      params.put("deptid", deptid);
      List<User> users = userService.findAll(params);

      return new UserWarpper(BeanUtil.objectsToMaps(users)).warp();
    }
  }

  /**
   * 添加管理员
   */
  @RequestMapping("/add")
  @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Tip add(@Valid UserDto user, BindingResult result) {
    if (result.hasErrors()) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    // 判断账号是否重复
    User theUser = userRepository.findByAccount(user.getAccount());
    if (theUser != null) {
      throw new LoanException(BizExceptionEnum.USER_ALREADY_REG);
    }

    // 完善账号信息
    user.setSalt(ToolUtil.getRandomString(5));
    user.setPassword(MD5.md5(user.getPassword(), user.getSalt()));
    user.setStatus(ManagerStatus.OK.getCode());
    user.setCreatetime(new Date());

    this.userRepository.save(UserFactory.createUser(user, new User()));
    return SUCCESS_TIP;
  }

  /**
   * 修改管理员
   */
  @RequestMapping("/edit")
  @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
  @ResponseBody
  public Tip edit(@Valid UserDto user, BindingResult result) throws NoPermissionException {
    if (result.hasErrors()) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    User oldUser = userRepository.findOne(user.getId());
    if (ShiroKit.hasRole(Const.ADMIN_NAME)) {

      this.userRepository.save(UserFactory.updateUser(user, oldUser));
      return SUCCESS_TIP;
    } else {
      assertAuth(user.getId());
      ShiroUser shiroUser = ShiroKit.getUser();
      if (shiroUser.getId().equals(user.getId())) {
        this.userRepository.save(UserFactory.updateUser(user, oldUser));
        return SUCCESS_TIP;
      } else {
        throw new LoanException(BizExceptionEnum.NO_PERMITION);
      }
    }
  }

  /**
   * 删除管理员（逻辑删除）
   */
  @RequestMapping("/delete")
  @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
  @Permission
  @ResponseBody
  public Tip delete(@RequestParam Integer userId) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    //不能删除超级管理员
    if (userId.equals(Const.ADMIN_ID)) {
      throw new LoanException(BizExceptionEnum.CANT_DELETE_ADMIN);
    }
    assertAuth(userId);
    User user = userRepository.findOne(userId);
    user.setStatus(ManagerStatus.DELETED.getCode());
    userRepository.save(user);
    return SUCCESS_TIP;
  }

  /**
   * 查看管理员详情
   */
  @RequestMapping("/view/{userId}")
  @ResponseBody
  public User view(@PathVariable Integer userId) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    assertAuth(userId);
    return this.userRepository.findOne(userId);
  }

  /**
   * 重置管理员的密码
   */
  @RequestMapping("/reset")
  @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Tip reset(@RequestParam Integer userId) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    assertAuth(userId);
    User user = this.userRepository.findOne(userId);
    user.setSalt(ToolUtil.getRandomString(5));
    user.setPassword(MD5.md5(Const.DEFAULT_PWD, user.getSalt()));
    this.userRepository.save(user);
    return SUCCESS_TIP;
  }

  /**
   * 冻结用户
   */
  @RequestMapping("/freeze")
  @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Tip freeze(@RequestParam Integer userId) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    //不能冻结超级管理员
    if (userId.equals(Const.ADMIN_ID)) {
      throw new LoanException(BizExceptionEnum.CANT_FREEZE_ADMIN);
    }
    assertAuth(userId);
    User user = userRepository.findOne(userId);
    user.setStatus(ManagerStatus.FREEZED.getCode());
    userRepository.save(user);
    return SUCCESS_TIP;
  }

  /**
   * 解除冻结用户
   */
  @RequestMapping("/unfreeze")
  @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Tip unfreeze(@RequestParam Integer userId) {
    if (ToolUtil.isEmpty(userId)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    assertAuth(userId);
    User user = userRepository.findOne(userId);
    user.setStatus(ManagerStatus.OK.getCode());
    userRepository.save(user);
    return SUCCESS_TIP;
  }

  /**
   * 分配角色
   */
  @RequestMapping("/setRole")
  @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Tip setRole(@RequestParam("userId") Integer userId,
      @RequestParam("roleIds") String roleIds) {
    if (ToolUtil.isOneEmpty(userId, roleIds)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    //不能修改超级管理员
    if (userId.equals(Const.ADMIN_ID)) {
      throw new LoanException(BizExceptionEnum.CANT_CHANGE_ADMIN);
    }
    assertAuth(userId);
    User user = userRepository.findOne(userId);
    user.setRoleid(roleIds);
    userRepository.save(user);
    return SUCCESS_TIP;
  }

  /**
   * 分配部门
   */
  @RequestMapping("/setDept")
  @BussinessLog(value = "分配角色", key = "userId,deptIds", dict = UserDict.class)
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Tip setDept(@RequestParam("userId") Integer userId,
      @RequestParam("deptIds") String deptIds) {
    if (ToolUtil.isOneEmpty(userId, deptIds)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    //不能修改超级管理员
    if (userId.equals(Const.ADMIN_ID)) {
      throw new LoanException(BizExceptionEnum.CANT_CHANGE_ADMIN);
    }
    assertAuth(userId);
    User user = userRepository.findOne(userId);
    user.setDeptid(deptIds);
    userRepository.save(user);
    return SUCCESS_TIP;
  }

  /**
   * 上传图片(上传到项目的webapp/static/img)
   */
  @RequestMapping(method = RequestMethod.POST, path = "/upload")
  @ResponseBody
  public String upload(@RequestPart("file") MultipartFile picture) {
    String pictureName = UUID.randomUUID().toString() + ".jpg";
    try {
      String fileSavePath = LoanProperties.getFileUploadPath();
      picture.transferTo(new File(fileSavePath + pictureName));
    } catch (Exception e) {
      throw new LoanException(BizExceptionEnum.UPLOAD_ERROR);
    }
    return pictureName;
  }

  /**
   * 判断当前登录的用户是否有操作这个用户的权限
   */
  private void assertAuth(Integer userId) {
    if (ShiroKit.isAdmin()) {
      return;
    }
    List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
    User user = this.userRepository.findOne(userId);
    String deptid = user.getDeptid();
    Integer[] deptids = Convert.toIntArray(",", deptid);
    if (deptDataScope.contains(deptids)) {
      return;
    } else {
      throw new LoanException(BizExceptionEnum.NO_PERMITION);
    }

  }
}
