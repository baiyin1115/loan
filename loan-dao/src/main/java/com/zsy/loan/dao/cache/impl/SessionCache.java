package com.zsy.loan.dao.cache.impl;

import com.zsy.loan.bean.constant.cache.CacheName;
import com.zsy.loan.bean.core.ShiroUser;
import com.zsy.loan.dao.cache.CacheDao;
import java.io.Serializable;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * 用户登录时，生成的Token与用户ID的对应关系
 * 用户登录时 记录其他信息
 */
@Service("sessionCache")
public class SessionCache implements CacheDao {
  
  @Autowired
  private EhcacheDao ehcacheDao;

  @Resource
  private CacheManager cacheManager;

  public void putToken(String token, Long idUser) {
    hset(CacheName.SESSION, token, idUser);
  }

  public Long getToken(String token) {
    return hget(CacheName.SESSION, token, Long.class);
  }

  public void removeToken(String token) {
    hdel(CacheName.SESSION, token);
  }

  public void setUserToken(String token, ShiroUser shiroUser) {
    hset(CacheName.SESSION, token + "user", shiroUser);
  }

  public ShiroUser getUserToken(String token) {
    return hget(CacheName.SESSION, token + "user", ShiroUser.class);
  }

  @Override
  public void hset(Serializable key, Serializable k, Object val) {
    ehcacheDao.hset(key,k,val);
  }

  @Override
  public Serializable hget(Serializable key, Serializable k) {
    return ehcacheDao.hget(key,k);
  }

  @Override
  public <T> T hget(Serializable key, Serializable k, Class<T> klass) {
    return ehcacheDao.hget(key,k,klass);
  }

  @Override
  public void
  set(Serializable key, Object val) {
    Cache cache = cacheManager.getCache(CacheName.SESSION);
    cache.put(key, val);

  }

  @Override
  public <T> T get(Serializable key, Class<T> klass) {
    return cacheManager.getCache(CacheName.SESSION).get(String.valueOf(key), klass);
  }

  @Override
  public String get(Serializable key) {
    return cacheManager.getCache(CacheName.SESSION).get(String.valueOf(key), String.class);
  }

  @Override
  public void del(Serializable key) {
    cacheManager.getCache(CacheName.SESSION).put(String.valueOf(key), null);
  }

  @Override
  public void hdel(Serializable key, Serializable k) {
    ehcacheDao.hdel(key,k);
  }
}
