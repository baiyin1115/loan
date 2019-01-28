package com.zsy.loan.service.system.impl;

import com.zsy.loan.bean.constant.cache.CacheConstantKey;
import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.bean.vo.SpringContextHolder;
import com.zsy.loan.dao.cache.impl.SessionCache;
import com.zsy.loan.dao.system.DeptRepository;
import com.zsy.loan.service.shiro.ShiroKit;
import com.zsy.loan.service.system.IUserOwnFactory;
import com.zsy.loan.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


/**
 * 当前登录用户拥有的数据信息
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
@CacheConfig
public class UserOwnFactory implements IUserOwnFactory {

  private DeptRepository deptRepository = SpringContextHolder.getBean(DeptRepository.class);
  private SessionCache cache = SpringContextHolder.getBean(SessionCache.class);

  public static IUserOwnFactory me() {
    return SpringContextHolder.getBean("userOwnFactory");
  }

  public String get(String key) {
    return cache.get(key);
  }

  public <T> T get(String key,Class<T> klass) {
    return cache.get(key,klass);
  }

  public void set(String key, Object val) {
    cache.set(key, val);
  }


  @Override
  public List<Dict> getOrgsByUser(Long userId) {
    return null;
  }

  @Override
  public List<Dict> getOrgsByToken(String token) {
    return null;
  }

  @Override
  public List<Dict> getOrgList() {

    Long id = ShiroKit.getUser().getId();
    List<Dict> val = get(id+"_"+CacheConstantKey.DICT_ORG_ALL,List.class);

    if (!ObjectUtils.isEmpty(val)) {
      return val;
    }

    List<Integer> deptIds = ShiroKit.getDeptDataScope();
    List<Dept> deptList = deptRepository.findAllById(deptIds);

    List<Dict> orgList = new ArrayList<>(deptList.size());
    for (Dept dept : deptList) {
      orgList.add(
          Dict.builder().id(dept.getId()).num(String.valueOf(dept.getId())).name(dept.getFullname())
              .build());
    }
    set(id+"_"+CacheConstantKey.DICT_ORG_ALL, orgList);

    return orgList;
  }
}
