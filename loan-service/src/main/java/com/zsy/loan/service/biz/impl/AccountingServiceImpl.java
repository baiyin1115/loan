package com.zsy.loan.service.biz.impl;

import com.zsy.loan.dao.biz.AcctRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账务处理
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class AccountingServiceImpl {

  @Autowired
  private AcctRepo repository;

}
