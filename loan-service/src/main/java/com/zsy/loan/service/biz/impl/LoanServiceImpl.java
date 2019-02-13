package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.bean.convey.LoanPutVo;
import com.zsy.loan.bean.convey.LoanVo;
import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.biz.LoanInfoRepo;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.dao.biz.RepayPlanRepo;
import com.zsy.loan.service.factory.LoanTrialCalculateFactory;
import com.zsy.loan.service.shiro.ShiroKit;
import com.zsy.loan.service.system.ISystemService;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.JodaTimeUtil;
import com.zsy.loan.utils.StringUtils;
import com.zsy.loan.utils.factory.Page;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 贷款服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class LoanServiceImpl {

  @Autowired
  private LoanInfoRepo repository;

  @Autowired
  private RepayPlanRepo repayPlanRepo;

  @Autowired
  private ProductInfoRepo productRepo;

  @Autowired
  private ISystemService systemService;


  public Page<TBizLoanInfo> getTBLoanPages(Page<TBizLoanInfo> page, TBizLoanInfo condition) {

    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {

      List<Order> orders = new ArrayList<Order>();
      orders.add(Order.desc("status"));
      orders.add(Order.desc("id"));

      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), Sort.by(orders));
    }

    org.springframework.data.domain.Page<TBizLoanInfo> page1 = repository
        .findAll(new Specification<TBizLoanInfo>() {

          @Override
          public Predicate toPredicate(Root<TBizLoanInfo> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            if (!ObjectUtils.isEmpty(condition.getCustNo())) {
              list.add(criteriaBuilder.equal(root.get("custNo"), condition.getCustNo()));
            }
            if (StringUtils.isNotEmpty(condition.getContrNo())) {
              list.add(criteriaBuilder
                  .like(root.get("contrNo").as(String.class), condition.getContrNo().trim() + "%"));
            }

            if (!ShiroKit.isAdmin()) { //如果不是管理员，只能查询到自己有权限公司的数据信息
              List<Integer> orgList = ShiroKit.getDeptDataScope();
              CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("orgNo"));
              in.value(orgList);
              list.add(in);
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

  public Boolean save(LoanVo loan, boolean b) {

    /**
     * 判断账户状态
     */
    TBizLoanInfo loanInfo = repository.findById(loan.getId()).get();
    if (!loanInfo.getStatus().equals(LoanStatusEnum.CHECK_IN.getValue())) { //登记
      throw new LoanException(BizExceptionEnum.LOAN_NOT_CHECK_IN, String.valueOf(loan.getId()));
    }

    /**
     * 试算
     */
    LoanCalculateVo calculateRequest = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);
    calculateRequest.setLoanBizType(LoanBizTypeEnum.LOAN_CHECK_IN.getValue()); //设置业务类型
    LoanCalculateVo result = calculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!loan.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.LOAN_CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }
    if (loan.getReceiveInterest().compareTo(result.getReceiveInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.LOAN_CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
    }
    if (loan.getServiceFee().compareTo(result.getServiceFee()) != 0) {
      throw new LoanException(BizExceptionEnum.LOAN_CALCULATE_REQ_NOT_MATCH, "服务费不一致");
    }

    TBizLoanInfo info = TBizLoanInfo.builder().build();
    BeanKit.copyProperties(loan, info);

    /**
     * 赋值
     */
    info.setAcctDate(systemService.getSysAcctDate());
    info.setStatus(LoanStatusEnum.CHECK_IN.getValue()); //登记
    info.setSchdPrin(result.getPrin()); // 应还本金
    info.setSchdInterest(result.getReceiveInterest()); // 应还利息
    info.setSchdServFee(result.getServiceFee()); // 应收服务费

    info.setSchdPen(BigDecimal.valueOf(0.00)); // 逾期罚息累计
    info.setTotPaidPrin(BigDecimal.valueOf(0.00)); // 已还本金累计
    info.setTotPaidInterest(BigDecimal.valueOf(0.00)); // 已还利息累计
    info.setTotPaidServFee(BigDecimal.valueOf(0.00)); // 已收服务费累计
    info.setTotPaidPen(BigDecimal.valueOf(0.00)); // 已还罚息累计
    info.setTotWavAmt(BigDecimal.valueOf(0.00)); // 减免金额累计

    repository.save(info);

    return true;
  }

  @Transactional
  public Boolean delete(List<Long> loanIds) {

    for (long loanId : loanIds) {
      /**
       * 判断账户状态
       */
      TBizLoanInfo loanInfo = repository.findById(loanId).get();
      if (!loanInfo.getStatus().equals(LoanStatusEnum.CHECK_IN.getValue())) { //登记
        throw new LoanException(BizExceptionEnum.LOAN_NOT_CHECK_IN, String.valueOf(loanId));
      }

      repository.deleteById(loanId);

    }
    return true;

  }

  @Transactional
  public Boolean put(LoanPutVo loan, boolean b) {

    /**
     * 锁记录
     */
    TBizLoanInfo record = repository.lockRecordByIdStatus(loan.getId(), loan.getStatus());
    if (record == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING,
          loan.getId() + "_" + loan.getStatus());
    }

    /**
     * 试算
     */
    LoanCalculateVo calculateRequest = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);
    calculateRequest.setLoanBizType(LoanBizTypeEnum.PUT.getValue()); //设置业务类型
    LoanCalculateVo result = calculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!loan.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.LOAN_CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }
    if (loan.getReceiveInterest().compareTo(result.getReceiveInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.LOAN_CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
    }
    if (loan.getServiceFee().compareTo(result.getServiceFee()) != 0) {
      throw new LoanException(BizExceptionEnum.LOAN_CALCULATE_REQ_NOT_MATCH, "服务费不一致");
    }

    TBizLoanInfo info = TBizLoanInfo.builder().build();
    BeanKit.copyProperties(loan, info);

    /**
     * 修改凭证信息
     */
    info.setStatus(LoanStatusEnum.PUT.getValue()); //已放款
    info.setAcctDate(systemService.getSysAcctDate());
    repository.put(info.getId(), info.getStatus(), info.getRemark(), info.getLendingDate(),
        info.getDdDate(), info.getAcctDate());

    /**
     * 先删除后插保存还款计划信息
     */
    repayPlanRepo.deleteByLoanNo(info.getId());
    repayPlanRepo.saveAll(result.getRepayPlanList());

    /**
     * 调用账户模块记账
     */
    //TODO

    return true;

  }

  public void delay(LoanVo loan, boolean b) {
  }

  public void breach(LoanVo loan, boolean b) {
  }

  public void updateVoucher(LoanVo loan, boolean b) {
  }

  public Page<TBizRepayPlan> getPlanPages(Page<TBizRepayPlan> page, TBizRepayPlan condition) {
    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      List<Order> orders = new ArrayList<Order>();
      orders.add(Order.desc("status"));
      orders.add(Order.asc("ddDate"));
      orders.add(Order.desc("id"));

      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), Sort.by(orders));
    }

    org.springframework.data.domain.Page<TBizRepayPlan> page1 = repayPlanRepo
        .findAll(new Specification<TBizRepayPlan>() {


          @Override
          public Predicate toPredicate(Root<TBizRepayPlan> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            //设置借据编号
            if (!ObjectUtils.isEmpty(condition.getLoanNo())) {
              list.add(criteriaBuilder.equal(root.get("loanNo"), condition.getLoanNo()));
            }

            if (!ShiroKit.isAdmin()) { //如果不是管理员，只能查询到自己有权限公司的数据信息
              List<Integer> orgList = ShiroKit.getDeptDataScope();
              CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("orgNo"));
              in.value(orgList);
              list.add(in);
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

  public void repay(LoanVo loan, boolean b) {
  }

  private void setOrgList(Long orgNo, Path path, CriteriaBuilder criteriaBuilder,
      List<Predicate> list) {
    if (!ObjectUtils.isEmpty(orgNo)) {
      list.add(criteriaBuilder.equal(path, orgNo));
    } else {

      if (!ShiroKit.isAdmin()) {
        // 只能查当前登录用户拥有查询权限的
        List<Integer> orgList = ShiroKit.getDeptDataScope();

        CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
        in.in(orgList);
        list.add(in);
      }
    }
  }

  /**
   * 试算
   */
  public LoanCalculateVo calculate(LoanCalculateVo loan) {

    /**
     * 根据还款方式、本金、利率、服务费收取方式、服务费比例、开始结束日期计算
     * 利息、服务费、放款金额、期数、应还本金、应还利息、应收服务费、还款计划相关信息
     */
    loan.setDayRate(BigDecimalUtil
        .div(loan.getRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_UP)); //日利息
    loan.setMonthRate(BigDecimalUtil
        .div(loan.getRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_UP)); //月利息
    loan.setDay(JodaTimeUtil.daysBetween(loan.getBeginDate(), loan.getEndDate())); //相差天数
    loan.setMonth(JodaTimeUtil.getMonthFloor(loan.getBeginDate(), loan.getEndDate())); //相差月数
    loan.setProduct(productRepo.findById(loan.getProductNo()).get()); //产品信息

    LoanCalculateVo result = LoanTrialCalculateFactory.maps
        .get(RepayTypeEnum.getEnumByKey(loan.getRepayType())).apply(loan);

    return result;

  }
}
