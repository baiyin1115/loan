package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.entity.biz.TBizAcct;
import com.zsy.loan.bean.entity.biz.TBizCustomerInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.CustomerStatusEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.request.AcctRequest;
import com.zsy.loan.dao.biz.AcctRepo;
import com.zsy.loan.dao.biz.CustomerInfoRepo;
import com.zsy.loan.service.system.impl.SystemServiceImpl;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.factory.Page;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
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
 * 账户服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class AcctServiceImpl {

  @Autowired
  private AcctRepo repository;

  @Autowired
  private CustomerInfoRepo customerInfoRepo;

  @Autowired
  private SystemServiceImpl systemService;


  @Transactional
  public Object save(@Valid AcctRequest account,boolean isUp) {

    TBizAcct info = TBizAcct.builder().build();
    BeanUtils.copyProperties(account, info);

    if (ObjectUtils.isEmpty(info.getId())) {
      info.setId(systemService.getNextAcctNO(AcctTypeEnum.getEnumByKey(info.getAcctType())));
    }

    return repository.save(info);

  }

  public Page<TBizAcct> getTBizAccounts(Page<TBizAcct> page, TBizAcct condition) {

    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC, "id");
    }

    org.springframework.data.domain.Page<TBizAcct> page1 = repository
        .findAll(new Specification<TBizAcct>() {


          @Override
          public Predicate toPredicate(Root<TBizAcct> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();
            if (!ObjectUtils.isEmpty(condition.getUserNo())) {
              list.add(criteriaBuilder.equal(root.get("userNo"), condition.getUserNo()));
            }
            if (!ObjectUtils.isEmpty(condition.getAcctType())) {
              list.add(criteriaBuilder.equal(root.get("acctType"), condition.getAcctType()));
            }
            if (!ObjectUtils.isEmpty(condition.getName())) {
              list.add(criteriaBuilder
                  .like(root.get("name").as(String.class), condition.getName() + "%"));
            }

            CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("status"));
            in.value(AcctStatusEnum.FREEZE.getValue());
            in.value(AcctStatusEnum.VALID.getValue());
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
  public Boolean logicDelete(List<Long> accountIds) {

    for (long acct : accountIds) {
      /**
       * 判断账户状态
       */
      TBizAcct bizAcct = repository.findById(acct).get();
      if (bizAcct.getStatus().equals(AcctStatusEnum.INVALID.getValue())) { //已经失效
        continue;
      } else if (bizAcct.getStatus().equals(AcctStatusEnum.FREEZE.getValue())) { //账户处于冻结状态
        throw new LoanException(BizExceptionEnum.ACCOUNT_FREEZED, String.valueOf(acct));
      }

      BigDecimal balance = BigDecimalUtil
          .add(bizAcct.getAvailableBalance(), bizAcct.getFreezeBalance());
      if (balance.compareTo(BigDecimal.ZERO) > 0) {
        throw new LoanException(BizExceptionEnum.ACCOUNT_BALANCE_NOT_ZERO, String.valueOf(acct));
      }

      /**
       * 判断用户状态
       */
      if (bizAcct.getAcctType().equals(AcctTypeEnum.INVEST.getValue()) || bizAcct.getAcctType()
          .equals(AcctTypeEnum.LOAN.getValue())) {
        TBizCustomerInfo customer = customerInfoRepo.findById(bizAcct.getUserNo()).get();
        if (customer.getStatus().equals(CustomerStatusEnum.NORMAL.getValue()) || customer
            .getStatus()
            .equals(CustomerStatusEnum.BLACKLIST.getValue())) { //客户处于有效状态
          throw new LoanException(BizExceptionEnum.ACCOUNT_CUSTOMER_FREEZED, String.valueOf(acct));
        }
      }

      repository.UpStatusById(acct, AcctStatusEnum.INVALID.getValue());

    }

    return true;
  }

  @Transactional
  public Boolean freeze(List<Long> accountIds) {

    for (long acct : accountIds) {
      /**
       * 判断账户状态
       */
      TBizAcct bizAcct = repository.findById(acct).get();
      if (bizAcct.getStatus().equals(AcctStatusEnum.FREEZE.getValue())) { //已经冻结
        continue;
      } else if (bizAcct.getStatus().equals(AcctStatusEnum.INVALID.getValue())) { //账户处于止用状态
        throw new LoanException(BizExceptionEnum.NO_THIS_ACCOUNT, String.valueOf(acct));
      }

      repository.UpStatusById(acct, AcctStatusEnum.FREEZE.getValue());

    }

    return true;
  }

  @Transactional
  public Boolean unfreeze(List<Long> accountIds) {

    for (long acct : accountIds) {
      /**
       * 判断账户状态
       */
      TBizAcct bizAcct = repository.findById(acct).get();
      if (bizAcct.getStatus().equals(AcctStatusEnum.VALID.getValue())) { //已经有效
        continue;
      } else if (bizAcct.getStatus().equals(AcctStatusEnum.INVALID.getValue())) { //账户处于止用状态
        throw new LoanException(BizExceptionEnum.NO_THIS_ACCOUNT, String.valueOf(acct));
      }

      repository.UpStatusById(acct, AcctStatusEnum.VALID.getValue());

    }

    return true;
  }
}
