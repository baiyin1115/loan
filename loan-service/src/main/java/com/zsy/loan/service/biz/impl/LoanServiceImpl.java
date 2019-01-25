package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.request.LoanRequest;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.utils.factory.Page;
import java.util.List;
import javax.validation.Valid;
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


  public Page<TBizLoanInfo> getTBLoanInfos(Page<TBizLoanInfo> page, TBizLoanInfo condition) {

    return null;
  }

  public Object save(@Valid LoanRequest loan, boolean b) {
    return null;
  }

  public void delete(List<Long> loanIds) {
  }

  public void put(@Valid LoanRequest loan, boolean b) {
  }

  public void delay(@Valid LoanRequest loan, boolean b) {
  }

  public void breach(@Valid LoanRequest loan, boolean b) {
  }

  public void updateVoucher(@Valid LoanRequest loan, boolean b) {
  }

  public Page<TBizLoanInfo> getPlanList(Page<TBizLoanInfo> page, TBizLoanInfo condition) {
    return null;
  }

  public void repay(@Valid LoanRequest loan, boolean b) {
  }
}
