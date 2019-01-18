package com.zsy.loan.service.biz.impl;

import com.zsy.loan.dao.system.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 贷款服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class LoanServiceImpl {

  @Autowired
  private UserRepository userRepository;


}
