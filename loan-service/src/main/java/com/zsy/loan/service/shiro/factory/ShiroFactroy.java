package com.zsy.loan.service.shiro.factory;

import com.google.common.collect.Lists;
import com.zsy.loan.bean.vo.SpringContextHolder;
import com.zsy.loan.bean.constant.state.ManagerStatus;
import com.zsy.loan.bean.core.ShiroUser;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.dao.system.MenuRepository;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.utils.Convert;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class ShiroFactroy implements IShiro {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MenuRepository menuRepository;

  public static IShiro me() {
    return SpringContextHolder.getBean(IShiro.class);
  }

  @Override
  public User user(String account) {

    User user = userRepository.findByAccount(account);

    // 账号不存在
    if (null == user) {
      throw new CredentialsException();
    }
    // 账号被冻结
    if (user.getStatus() != ManagerStatus.OK.getCode()) {
      throw new LockedAccountException();
    }
    return user;
  }

  @Override
  public ShiroUser shiroUser(User user) {
    ShiroUser shiroUser = new ShiroUser();

    shiroUser.setId(Long.valueOf(user.getId()));            // 账号id
    shiroUser.setAccount(user.getAccount());// 账号
//    shiroUser.setDeptId(user.getDeptid());    // 部门id
//    shiroUser.setDeptName(ConstantFactory.me().getDeptName(user.getDeptid()));// 部门名称

    /**
     * 部门集设置
     */
    Integer[] depoArray = Convert.toIntArray(user.getDeptid());
    shiroUser.setDeptList(Lists.newArrayList(depoArray));
    List<String> depoNames = Lists.newArrayList();
    for (int depoId : depoArray) {
      depoNames.add(ConstantFactory.me().getDeptName(depoId));
    }
    shiroUser.setDeptNameList(depoNames);

    shiroUser.setName(user.getName());        // 用户名称

    Integer[] roleArray = Convert.toIntArray(user.getRoleid());// 角色集合
    List<Integer> roleList = new ArrayList<Integer>();
    List<String> roleNameList = new ArrayList<String>();
    for (int roleId : roleArray) {
      roleList.add(roleId);
      roleNameList.add(ConstantFactory.me().getSingleRoleName(roleId));
    }
    shiroUser.setRoleList(roleList);
    shiroUser.setRoleNames(roleNameList);

    return shiroUser;
  }

  @Override
  public List<String> findPermissionsByRoleId(Integer roleId) {
    List<String> resUrls = menuRepository.getResUrlsByRoleId(roleId);
    return resUrls;
  }

  @Override
  public String findRoleNameByRoleId(Integer roleId) {
    return ConstantFactory.me().getSingleRoleTip(roleId);
  }

  @Override
  public SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName) {
    String credentials = user.getPassword();
    // 密码加盐处理
    String source = user.getSalt();
    ByteSource credentialsSalt = new Md5Hash(source);
    return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
  }

}
