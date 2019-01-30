package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.request.LoanCalculateRequest;
import com.zsy.loan.bean.request.LoanRequest;
import com.zsy.loan.dao.biz.LoanInfoRepo;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.dao.biz.RepayPlanRepo;
import com.zsy.loan.service.factory.TrialCalculateFactory;
import com.zsy.loan.service.shiro.ShiroKit;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateUtil;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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


  public Page<TBizLoanInfo> getTBLoanPages(Page<TBizLoanInfo> page, TBizLoanInfo condition) {

    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {

      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC,
          "status", "id");
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

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

  public Object save(LoanRequest loan, boolean b) {

    /**
     * 试算
     */
    LoanCalculateRequest calculateRequest = LoanCalculateRequest.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);
    LoanCalculateRequest result = calculate(calculateRequest); //试算结果

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
    info.setStatus(LoanStatusEnum.CHECK_IN.getValue()); //登记

    return repository.save(info);
  }

  public void delete(List<Long> loanIds) {
  }

  public void put(LoanRequest loan, boolean b) {
  }

  public void delay(LoanRequest loan, boolean b) {
  }

  public void breach(LoanRequest loan, boolean b) {
  }

  public void updateVoucher(LoanRequest loan, boolean b) {
  }

  public Page<TBizRepayPlan> getPlanPages(Page<TBizRepayPlan> page, TBizRepayPlan condition) {
    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC,
          "status", "id");
    }

    org.springframework.data.domain.Page<TBizRepayPlan> page1 = repayPlanRepo
        .findAll(new Specification<TBizRepayPlan>() {


          @Override
          public Predicate toPredicate(Root<TBizRepayPlan> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            //设置借据编号
            if (!ObjectUtils.isEmpty(condition.getLoanNo())) {
              list.add(criteriaBuilder.equal(root.get("loanNo"), condition.getLoanNo()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

  public void repay(LoanRequest loan, boolean b) {
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
  public LoanCalculateRequest calculate(LoanCalculateRequest loan) {

    /**
     * 根据还款方式、本金、利率、服务费收取方式、服务费比例、开始结束日期计算
     * 利息、服务费、放款金额、期数、应还本金、应还利息、应收服务费
     */
    loan.setDayRate(BigDecimalUtil
        .div(loan.getRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_UP)); //日利息
    loan.setMonthRate(BigDecimalUtil
        .div(loan.getRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_UP)); //月利息
    loan.setDay(DateUtil.daysBetween(loan.getBeginDate(), loan.getEndDate())); //相差天数
    loan.setMonth(DateUtil.getMonthFloor(loan.getBeginDate(), loan.getEndDate())); //相差月数
    loan.setProduct(productRepo.findById(loan.getProductNo()).get()); //产品信息

    LoanCalculateRequest result = TrialCalculateFactory.maps
        .get(RepayTypeEnum.getEnumByKey(loan.getRepayType())).apply(loan);

    return result;

  }
}
