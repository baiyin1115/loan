package com.zsy.loan.service.biz.impl;

import com.google.common.collect.Lists;
import com.zsy.loan.bean.core.ShiroUser;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.dao.cache.TokenCache;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.platform.log.LogManager;
import com.zsy.loan.service.platform.log.LogTaskFactory;
import com.zsy.loan.service.system.LogInService;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.utils.Convert;
import com.zsy.loan.utils.HttpKit;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账户服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class AcctServiceImpl {

  @Autowired
  private UserRepository userRepository;


}
