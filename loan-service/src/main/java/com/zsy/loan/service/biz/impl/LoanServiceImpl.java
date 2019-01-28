package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.request.LoanRequest;
import com.zsy.loan.dao.biz.LoanInfoRepo;
import com.zsy.loan.dao.biz.RepayPlanRepo;
import com.zsy.loan.service.shiro.ShiroKit;
import com.zsy.loan.utils.StringUtils;
import com.zsy.loan.utils.factory.Page;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
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

  public void repay(@Valid LoanRequest loan, boolean b) {
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
}
