package com.zsy.loan.service.system.impl;

import com.zsy.loan.bean.constant.cache.CacheConstantKey;
import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.bean.vo.SpringContextHolder;
import com.zsy.loan.dao.cache.impl.SessionCache;
import com.zsy.loan.dao.system.DeptRepository;
import com.zsy.loan.service.system.IUserOwnFactory;
import com.zsy.loan.utils.StringUtils;
import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


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

  public void set(String key, String val) {
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
}
