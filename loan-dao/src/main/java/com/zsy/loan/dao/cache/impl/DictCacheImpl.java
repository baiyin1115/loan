package com.zsy.loan.dao.cache.impl;

import com.zsy.loan.bean.constant.cache.CacheKey;
import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.dao.cache.CacheDao;
import com.zsy.loan.dao.cache.DictCache;
import com.zsy.loan.dao.system.DictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DictCacheImpl
 *
 * @author zt
 * @version 2018/12/23 0023
 */
@Slf4j
@Component
public class DictCacheImpl implements DictCache {

  @Autowired
  private DictRepository dictRepository;
  @Autowired
  private CacheDao cacheDao;

  @Override
  public List<Dict> getDictsByPname(String dictName) {
    return (List<Dict>) cacheDao.hget(EhcacheDao.CONSTANT, CacheKey.DICT + dictName, List.class);
  }

  @Override
  public String getDict(Integer dictId) {
    return (String) get(CacheKey.DICT_NAME + String.valueOf(dictId));
  }

  @Override
  public void cache() {
    List<Dict> list = dictRepository.findByPid(0);
    for (Dict dict : list) {
      List<Dict> children = dictRepository.findByPid(dict.getId());
      if (children.isEmpty()) {
        continue;
      }
      set(String.valueOf(dict.getId()), children);
      set(dict.getName(), children);
      for (Dict child : children) {
        set(CacheKey.DICT_NAME + String.valueOf(child.getId()), child.getName());
      }

    }

  }

  @Override
  public Object get(String key) {
    return cacheDao.hget(EhcacheDao.CONSTANT, CacheKey.DICT + key);
  }

  @Override
  public void set(String key, Object val) {
    log.info("加载缓存："+EhcacheDao.CONSTANT+":"+CacheKey.DICT + key+":"+val);
    cacheDao.hset(EhcacheDao.CONSTANT, CacheKey.DICT + key, val);

  }
}
