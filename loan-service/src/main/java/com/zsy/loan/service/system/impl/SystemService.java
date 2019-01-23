package com.zsy.loan.service.system.impl;

import com.zsy.loan.bean.constant.cache.CacheConstantKey;
import com.zsy.loan.bean.vo.SpringContextHolder;
import com.zsy.loan.dao.cache.ConfigCache;
import com.zsy.loan.dao.system.StatusRepository;
import com.zsy.loan.service.system.ISystemService;
import com.zsy.loan.utils.DateUtil;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019/1/23/023.
 */
@Component
@DependsOn("springContextHolder")
@CacheConfig
public class SystemService implements ISystemService {

  @Autowired
  private StatusRepository repository;
  @Autowired
  private ConfigCache configCache;

  public static ISystemService me() {
    return SpringContextHolder.getBean("systemService");
  }

  @Override
  public Date getSysAcctDate() {

    Date acctDate = null;
    String key = CacheConstantKey.SYS + "acct_date";
    String val = configCache.get(key) == null ? null : (String) configCache.get(key);
    if (val != null) {
      acctDate = DateUtil.parseDate(val);
      return acctDate;
    }
    acctDate = repository.getSysAcctDate();
    configCache.set(key, DateUtil.getDay(acctDate));
    return acctDate;
  }
}
