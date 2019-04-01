package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.AcctVo;
import com.zsy.loan.bean.convey.CustomerInfoVo;
import com.zsy.loan.bean.entity.biz.TBizAcct;
import com.zsy.loan.bean.entity.biz.TBizCustomerInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctBalanceTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.CustomerStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.CustomerTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanStatusEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.biz.AcctRepo;
import com.zsy.loan.dao.biz.CustomerInfoRepo;
import com.zsy.loan.dao.biz.InvestInfoRepo;
import com.zsy.loan.dao.biz.LoanInfoRepo;
import com.zsy.loan.service.system.impl.SystemServiceImpl;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.StringUtils;
import com.zsy.loan.utils.factory.Page;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 客户服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class CustomerServiceImpl {

  @Autowired
  private CustomerInfoRepo repository;

  @Autowired
  private AcctRepo acctRepository;

  @Autowired
  private SystemServiceImpl systemService;

  @Autowired
  private AcctServiceImpl acctService;

  @Autowired
  private InvestInfoRepo investInfoRepo;

  @Autowired
  private LoanInfoRepo loanInfoRepo;

  /**
   * 通过客户名、手机号、身份证号码 模糊查询
   */
  public Page<TBizCustomerInfo> getTBizCustomers(Page<TBizCustomerInfo> page,
      TBizCustomerInfo condition) {

    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC, "id");
    }

    org.springframework.data.domain.Page<TBizCustomerInfo> page1 = repository
        .findAll(new Specification<TBizCustomerInfo>() {


          @Override
          public Predicate toPredicate(Root<TBizCustomerInfo> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();
            if (StringUtils.isNotEmpty(condition.getName())) {
              list.add(criteriaBuilder
                  .like(root.get("name").as(String.class), condition.getName().trim() + "%"));
            }
            if (StringUtils.isNotEmpty(condition.getCertNo())) {
              list.add(criteriaBuilder
                  .like(root.get("certNo").as(String.class), condition.getCertNo().trim() + "%"));
            }
            if (StringUtils.isNotEmpty(condition.getMobile())) {
              list.add(criteriaBuilder
                  .like(root.get("mobile").as(String.class), condition.getMobile().trim() + "%"));
            }
            if (condition.getType() != null) {
              list.add(criteriaBuilder
                  .equal(root.get("type").as(Long.class), condition.getType()));
            }

            CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("status"));
            in.value(CustomerStatusEnum.NORMAL.getValue());
            in.value(CustomerStatusEnum.BLACKLIST.getValue());
            list.add(in);

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;

  }

  @Transactional
  public Object save(CustomerInfoVo customer, boolean isUp) {

    TBizCustomerInfo info = TBizCustomerInfo.builder().build();
    BeanUtils.copyProperties(customer, info);

    if (ObjectUtils.isEmpty(info.getId())) {
      info.setId(systemService.getNextCustomerNO(CustomerTypeEnum.getEnumByKey(info.getType())));
    }

    repository.save(info);

    /**
     * 创建账户信息
     */
    if (!isUp) {
      AcctVo account = null;
      if (info.getType().equals(CustomerTypeEnum.INVEST.getValue())) { //投资人
        account = AcctVo.builder().acctType(AcctTypeEnum.INVEST.getValue())
            .availableBalance(BigDecimal.ZERO).balanceType(AcctBalanceTypeEnum.NO_OVERDRAW.getValue())
            .freezeBalance(BigDecimal.ZERO).remark("系统自动建立").status(AcctStatusEnum.VALID.getValue())
            .custNo(info.getId()).name(info.getName()).build();
      } else { //借款人
        account = AcctVo.builder().acctType(AcctTypeEnum.LOAN.getValue())
            .availableBalance(BigDecimal.ZERO).balanceType(AcctBalanceTypeEnum.NO_OVERDRAW.getValue())
            .freezeBalance(BigDecimal.ZERO).remark("系统自动建立").status(AcctStatusEnum.VALID.getValue())
            .custNo(info.getId()).name(info.getName()).build();
      }
      acctService.save(account, false);
    }

    return true;
  }

  /**
   * 逻辑上删除客户
   */
  @Transactional
  public Boolean logicDelete(List<Long> ids) {

    for (long id : ids) {

      /**
       * 判断客户状态
       */
      TBizCustomerInfo customer = repository.findById(id).get();
      if (customer.getStatus().equals(CustomerStatusEnum.DELETE.getValue())) { //已经删除
        continue;
      } else if (customer.getStatus().equals(CustomerStatusEnum.BLACKLIST.getValue())) { //账户处于黑名单
        throw new LoanException(BizExceptionEnum.CUSTOMER_FREEZED, String.valueOf(id));
      }

      /**
       * 校验账户是否有余额
       */
      TBizAcct bizAcct = acctRepository.findByCustNo(id).get();
      BigDecimal balance = BigDecimalUtil
          .add(bizAcct.getAvailableBalance(), bizAcct.getFreezeBalance());
      if (balance.compareTo(BigDecimal.ZERO) > 0) {
        throw new LoanException(BizExceptionEnum.ACCOUNT_BALANCE_NOT_ZERO, String.valueOf(id));
      }

      /**
       * 校验是否有进行中的项目
       */
      if (customer.getType().equals(CustomerTypeEnum.INVEST.getValue())) { //投资人
        List<Long> status = new ArrayList<>(3);
        status.add(InvestStatusEnum.CHECK_IN.getValue());
        status.add(InvestStatusEnum.INTEREST_ING.getValue());
        status.add(InvestStatusEnum.DELAY.getValue());
        if (investInfoRepo.getCountByCustNo(id, status) != 0) {
          throw new LoanException(BizExceptionEnum.CUSTOMER_BIZ_ING, String.valueOf(id));
        }
      } else {

        List<Long> status = new ArrayList<>(5);
        status.add(LoanStatusEnum.CHECK_IN.getValue());
        status.add(LoanStatusEnum.PUT.getValue());
        status.add(LoanStatusEnum.REPAY_IND.getValue());
        status.add(LoanStatusEnum.OVERDUE.getValue());
        status.add(LoanStatusEnum.DELAY.getValue());
        if (loanInfoRepo.getCountByCustNo(id, status) != 0) {
          throw new LoanException(BizExceptionEnum.CUSTOMER_BIZ_ING, String.valueOf(id));
        }
      }

      repository.UpStatusById(id, CustomerStatusEnum.DELETE.getValue());
      /**
       * 删除账户
       */
      acctRepository.UpStatusById(bizAcct.getId(), AcctStatusEnum.INVALID.getValue());

    }

    return true;
  }

  /**
   * 设置黑名单
   */
  @Transactional
  public Boolean setBlackList(List<Long> ids) {

    for (long id : ids) {
      /**
       * 判断客户状态
       */
      TBizCustomerInfo customer = repository.findById(id).get();
      if (customer.getStatus().equals(CustomerStatusEnum.BLACKLIST.getValue())) { //已经处理
        continue;
      } else if (customer.getStatus().equals(CustomerStatusEnum.DELETE.getValue())) { //已经删除
        throw new LoanException(BizExceptionEnum.NO_THIS_CUSTOMER, String.valueOf(id));
      }

      repository.UpStatusById(id, CustomerStatusEnum.BLACKLIST.getValue());
      /**
       * 冻结账户信息
       */
      TBizAcct bizAcct = acctRepository.findByCustNo(id).get();
      if (bizAcct != null) {
        acctRepository.UpStatusById(bizAcct.getId(), AcctStatusEnum.FREEZE.getValue());
      }

    }

    return true;
  }

  /**
   * 取消黑名单
   */
  @Transactional
  public Boolean cancelBlackList(List<Long> ids) {

    for (long id : ids) {

      /**
       * 判断客户状态
       */
      TBizCustomerInfo customer = repository.findById(id).get();
      if (customer.getStatus().equals(CustomerStatusEnum.NORMAL.getValue())) { //已经处理
        continue;
      } else if (customer.getStatus().equals(CustomerStatusEnum.DELETE.getValue())) { //已经删除
        throw new LoanException(BizExceptionEnum.NO_THIS_CUSTOMER, String.valueOf(id));
      }

      repository.UpStatusById(id, CustomerStatusEnum.NORMAL.getValue());

      /**
       * 取消冻结账户信息
       */
      TBizAcct bizAcct = acctRepository.findByCustNo(id).get();
      if (bizAcct != null) {
        acctRepository.UpStatusById(bizAcct.getId(), AcctStatusEnum.VALID.getValue());
      }

    }

    return true;
  }
}
