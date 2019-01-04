package com.zsy.loan.dao.cache;

import java.io.Serializable;

/**
 * CacheDao
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
public interface CacheDao {


  /**
   * 设置hash key值
   */
  void hset(Serializable key, Serializable k, Object val);

  /**
   * 获取hash key值
   */
  Serializable hget(Serializable key, Serializable k);

  /**
   * 获取hash key值
   */
  <T> T hget(Serializable key, Serializable k, Class<T> klass);

  /**
   * 设置key值，超时失效
   */
  void set(Serializable key, Object val);


  /**
   * 获取key值
   */
  <T> T get(Serializable key, Class<T> klass);

  String get(Serializable key);


  void del(Serializable key);

  void hdel(Serializable key, Serializable k);
}
