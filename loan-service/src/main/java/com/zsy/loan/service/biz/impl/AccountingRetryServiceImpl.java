package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.entity.biz.TBizAcct;
import com.zsy.loan.dao.biz.AcctRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2019/4/2/002.
 */
@Service
public class AccountingRetryServiceImpl {

  @Autowired
  private AcctRepo repository;

//  @Transactional//(propagation = Propagation.NESTED)
//  public int retryUpdate(TreeMap<Long, BigDecimal> upAcctMap) {
//
//    int upNum = 0;
//
//    Iterator<Entry<Long, BigDecimal>> up = upAcctMap.entrySet().iterator();
//    while (up.hasNext()) {
//      Map.Entry<Long, BigDecimal> entry = up.next();
//      upNum += updateBalance(entry.getKey(), entry.getValue());
//    }
//
//    return upNum;
//  }
//
//
//  public int updateBalance(Long acctId, BigDecimal upAmt) {
//
//    int upNum = 0;
//
//    TBizAcct acct = repository.findById(acctId).get();
//    if (BigDecimalUtil.add(upAmt, acct.getAvailableBalance()).compareTo(BigDecimal.valueOf(0.00)) < 0) {
//      throw new LoanException(BizExceptionEnum.ACCOUNT_NO_OVERDRAW, "余额不足" + acctId + "_" + acct.getAvailableBalance() + "_" + upAmt);
//    }
//
//    upNum = repository.upAvailableBalance(acctId, upAmt, acct.getVersion());
//    return upNum;
//  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public TBizAcct getNewestBizAcct(Long acctId) {

    TBizAcct acct = repository.findById(acctId).get();
    return acct;
  }

}
