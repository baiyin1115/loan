package com.zsy.loan.dao.system;

import com.zsy.loan.bean.entity.system.Cfg;
import com.zsy.loan.dao.BaseApplicationStartTest;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试
 *
 * @author enilu
 * @Date 2017/5/21 12:06
 */

public class CfgRepositoryTest extends BaseApplicationStartTest {

  @Autowired
  CfgRepository cfgRepository;

  @Test
  public void findByCfgName() {
    Cfg cfg = cfgRepository.findByCfgName("JS_API_TICKET");
    System.out.println(JSON.toJSON(cfg));
    Cfg cfg2 = cfgRepository.findByCfgName("JS_API_TICKET");
    System.out.println(JSON.toJSON(cfg2));
  }
}
