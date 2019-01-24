package com.zsy.loan.service.system.impl;

import com.zsy.loan.bean.constant.Const;
import com.zsy.loan.bean.constant.cache.CacheConstantKey;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.CustomerTypeEnum;
import com.zsy.loan.bean.vo.SpringContextHolder;
import com.zsy.loan.dao.cache.ConfigCache;
import com.zsy.loan.dao.system.StatusRepository;
import com.zsy.loan.service.system.ISystemService;
import com.zsy.loan.utils.DateUtil;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019/1/23/023.
 */
@Component
@DependsOn("springContextHolder")
@CacheConfig
public class SystemServiceImpl implements ISystemService {

  @Autowired
  private StatusRepository repository;
  @Autowired
  private ConfigCache configCache;

  public static ISystemService me() {
    return SpringContextHolder.getBean("systemServiceImpl");
  }

  @Override
  public Date getSysAcctDate() {

    Date acctDate = null;
    String key = CacheConstantKey.SYS + "acct_date";
    String val = configCache.get(key) == null ? null : (String) configCache.get(key);
    if (val != null) {
      acctDate = DateUtil.parseDate(val);
      return acctDate;
    }
    acctDate = repository.getSysAcctDate();
    configCache.set(key, DateUtil.getDay(acctDate));
    return acctDate;
  }

  /**
   * 账户号起始 融资账户171开头,借款账户271开头,公司卡账户471开头, 暂收371，暂付372，代偿370
   */
  @Override
  public long getNextAcctNO(AcctTypeEnum type) {

    //下一个值
    long next = repository.getNextVal(Const.ACCT_KEY);

    switch (type) {
      case LOAN: {
        return Const.ACCT_NO_BEGIN_LOAN + next;
      }
      case COMPANY: {
        return Const.ACCT_NO_BEGIN_COMPANY + next;
      }
      case REPLACE: {
        return Const.ACCT_NO_BEGIN_REPLACE + next;
      }
      case INTERIM_IN: {
        return Const.ACCT_NO_BEGIN_INTERIOR_IN + next;
      }
      case INTERIM_OUT: {
        return Const.ACCT_NO_BEGIN_INTERIOR_OUT + next;
      }
      case INVEST: {
        return Const.ACCT_NO_BEGIN_INVEST + next;
      }
      default: {
        return -1;
      }
    }
  }

  /**
   * 客户号起始 融资账户151开头,借款账户251开头
   */
  @Override
  public long getNextCustomerNO(CustomerTypeEnum type) {
    //下一个值
    long next = repository.getNextVal(Const.CUSTOMER_KEY);

    switch (type) {
      case INVEST: {
        return Const.CUSTOMER_NO_BEGIN_INVEST + next;
      }
      case LOAN: {
        return Const.CUSTOMER_NO_BEGIN_LOAN + next;
      }
      default: {
        return -1;
      }
    }
  }
}
