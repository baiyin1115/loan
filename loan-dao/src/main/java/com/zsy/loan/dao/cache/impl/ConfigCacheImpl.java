package com.zsy.loan.dao.cache.impl;

import com.zsy.loan.bean.constant.cache.CacheName;
import com.zsy.loan.bean.entity.system.Cfg;
import com.zsy.loan.dao.cache.CacheDao;
import com.zsy.loan.dao.cache.ConfigCache;
import com.zsy.loan.dao.system.CfgRepository;
import com.zsy.loan.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 全局参数缓存实现类
 *
 * @author enilu
 * @version 2018/12/20 0020
 */
@Slf4j
@Service
public class ConfigCacheImpl implements ConfigCache {

  @Autowired
  private CfgRepository cfgRepository;
  @Autowired@Qualifier("ehcacheDao")
  private CacheDao cacheDao;

  @Override
  public Object get(String key) {
    return (String) cacheDao.hget(CacheName.CONSTANT, key);
  }

  @Override
  public String get(String key, boolean local) {
    String ret = null;
    if (local) {
      ret = (String) get(key);
    } else {
      ret = cfgRepository.findByCfgName(key).getCfgValue();
      set(key, ret);
    }
    return ret;
  }

  @Override
  public String get(String key, String def) {
    String ret = (String) get(key);
    if (StringUtils.isEmpty(ret)) {
      return ret;
    }
    return ret;
  }


  @Override
  public void set(String key, Object val) {
    log.info("加载缓存："+ CacheName.CONSTANT+":"+ key+":"+val);
    cacheDao.hset(CacheName.CONSTANT, key, val);
  }

  @Override
  public void del(String key, String val) {
    cacheDao.hdel(CacheName.CONSTANT, val);
  }

  @Override
  public void cache() {
    log.info("reset config cache");
    List<Cfg> list = cfgRepository.findAll();
    if (list != null && !list.isEmpty()) {
      for (Cfg cfg : list) {
        if (StringUtils.isNotEmpty(cfg.getCfgName()) && StringUtils.isNotEmpty(cfg.getCfgValue())) {
          set(cfg.getCfgName(), cfg.getCfgValue());
        }
      }
    }
  }
}
