package com.zsy.loan.dao.cache;

/**
 * 顶级缓存接口
 */
public interface Cache {

  /**
   * 将数据库中的数据加载到缓存中
   */
  void cache();


  /**
   * 获取缓存数据
   */
  Object get(String key);


  /**
   * 设置缓存数据
   */
  void set(String key, Object val);


}
