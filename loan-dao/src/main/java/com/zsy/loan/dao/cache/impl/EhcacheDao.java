package com.zsy.loan.dao.cache.impl;

import com.zsy.loan.bean.constant.cache.CacheName;
import com.zsy.loan.dao.cache.CacheDao;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * EhcacheDao
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@Component("ehcacheDao")
public class EhcacheDao implements CacheDao {

  @Resource
  private CacheManager cacheManager;

  @Override
  public void hset(Serializable key, Serializable k, Object val) {
    Cache cache = cacheManager.getCache(String.valueOf(key));
    cache.put(k, val);
  }

  @Override
  public Serializable hget(Serializable key, Serializable k) {
    Cache cache = cacheManager.getCache(String.valueOf(key));
    return cache.get(k, String.class);
  }

  @Override
  public <T> T hget(Serializable key, Serializable k, Class<T> klass) {
    Cache cache = cacheManager.getCache(String.valueOf(key));
    return cache.get(k, klass);
  }

  @Override
  public void set(Serializable key, Object val) {
    Cache cache = cacheManager.getCache(CacheName.CONSTANT);
    cache.put(key, val);

  }

  @Override
  public <T> T get(Serializable key, Class<T> klass) {
    return cacheManager.getCache(CacheName.CONSTANT).get(String.valueOf(key), klass);
  }

  @Override
  public String get(Serializable key) {
    return cacheManager.getCache(CacheName.CONSTANT).get(String.valueOf(key), String.class);
  }

  @Override
  public void del(Serializable key) {
    cacheManager.getCache(CacheName.CONSTANT).put(String.valueOf(key), null);
  }

  @Override
  public void hdel(Serializable key, Serializable k) {
    cacheManager.getCache(String.valueOf(key)).put(String.valueOf(k), null);
  }
}
