package com.zsy.loan.dao.cache.impl;

import com.zsy.loan.bean.constant.cache.CacheConstantKey;
import com.zsy.loan.bean.constant.cache.CacheName;
import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.dao.cache.CacheDao;
import com.zsy.loan.dao.cache.DictCache;
import com.zsy.loan.dao.system.DeptRepository;
import com.zsy.loan.dao.system.DictRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * DictCacheImpl
 *
 * @author zt
 * @version 2018/12/23 0023
 */
@Slf4j
@Component("dictCacheImpl")
public class DictCacheImpl implements DictCache {

  @Autowired
  private DictRepository dictRepository;

  @Autowired
  private DeptRepository deptRepository;

  @Autowired()
  @Qualifier("ehcacheDao")
  private CacheDao cacheDao;

  @Override
  public List<Dict> getDictsByPname(String dictName) {
    return (List<Dict>) cacheDao
        .hget(CacheName.CONSTANT, CacheConstantKey.DICT + dictName, List.class);
  }

  @Override
  public String getDict(Integer dictId) {
    return (String) get(CacheConstantKey.DICT_NAME + String.valueOf(dictId));
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
        set(CacheConstantKey.DICT_NAME + String.valueOf(child.getId()), child.getName());
      }

    }

    /**
     * 加载公司信息
     */
    List<Dept> orgs = deptRepository.findAll();
    List<Dict> orgList = new ArrayList<>(orgs.size());
    for (Dept dept : orgs) {
      orgList.add(
          Dict.builder().id(dept.getId()).num(String.valueOf(dept.getId())).name(dept.getFullname())
              .build());
    }
    set(CacheConstantKey.DICT_ORG_ALL, orgList);

  }

  @Override
  public Object get(String key) {
    return cacheDao.hget(CacheName.CONSTANT, CacheConstantKey.DICT + key);
  }

  @Override
  public void set(String key, Object val) {
    log.info("加载缓存：" + CacheName.CONSTANT + ":" + CacheConstantKey.DICT + key + ":" + val);
    cacheDao.hset(CacheName.CONSTANT, CacheConstantKey.DICT + key, val);

  }
}
