package com.zsy.loan.service.business.system.impl;

import com.google.common.collect.Lists;
import com.zsy.loan.bean.core.ShiroUser;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.dao.cache.TokenCache;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.business.system.AccountService;
import com.zsy.loan.service.platform.log.LogManager;
import com.zsy.loan.service.platform.log.LogTaskFactory;
import com.zsy.loan.utils.Convert;
import com.zsy.loan.utils.HttpKit;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccountServiceImpl
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  private TokenCache tokenCache;
  @Autowired
  private UserRepository userRepository;

  @Override
  public String login(Long idUser) {
    String token = UUID.randomUUID().toString();
    tokenCache.put(token, idUser);
    LogManager.me().executeLog(
    LogTaskFactory.loginLog(idUser, HttpKit.getIp()));
    User user = userRepository.findOne(idUser.intValue());
    Integer[] roleArray = Convert.toIntArray(user.getRoleid());
    ShiroUser shiroUser = new ShiroUser();
    shiroUser.setAccount(user.getAccount());
    shiroUser.setId(idUser);
    shiroUser.setName(user.getName());
    shiroUser.setRoleList(Lists.newArrayList(roleArray));
    List<String> roleNames = Lists.newArrayList();
    List<String> roleCodes = Lists.newArrayList();
    for (int roleId : roleArray) {
      roleCodes.add(ConstantFactory.me().getSingleRoleTip(roleId));
      roleNames.add(ConstantFactory.me().getSingleRoleName(roleId));

    }
    shiroUser.setRoleNames(roleNames);
    shiroUser.setRoleCodes(roleCodes);

    /**
     * 部门集设置
     */
    //    shiroUser.setDeptId(user.getDeptid());
//    shiroUser.setDeptName(ConstantFactory.me().getDeptName(user.getDeptid()));
    Integer[] depoArray = Convert.toIntArray(user.getDeptid());
    shiroUser.setDeptList(Lists.newArrayList(depoArray));
    List<String> depoNames = Lists.newArrayList();
    for (int depoId : depoArray) {
      depoNames.add(ConstantFactory.me().getDeptName(depoId));
    }
    shiroUser.setDeptNameList(depoNames);

    tokenCache.setUser(token, shiroUser);
    return token;
  }

  @Override
  public void logout(String token) {
    tokenCache.remove(token);
  }
}
