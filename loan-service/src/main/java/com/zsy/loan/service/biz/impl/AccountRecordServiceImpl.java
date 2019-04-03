package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.AcctRecordVo;
import com.zsy.loan.bean.entity.biz.TBizAcctRecord;
import com.zsy.loan.dao.biz.AcctRecordRepo;
import com.zsy.loan.utils.DateUtil;
import com.zsy.loan.utils.factory.Page;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 转账服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class AccountRecordServiceImpl extends BaseServiceImpl {

  @Resource
  AcctRecordRepo repository;


  public Page<TBizAcctRecord> getTBizAcctRecords(Page<TBizAcctRecord> page, AcctRecordVo condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.desc("groupNo"));
    orders.add(Order.desc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizAcctRecord> page1 = repository
        .findAll(new Specification<TBizAcctRecord>() {

          @Override
          public Predicate toPredicate(Root<TBizAcctRecord> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            if (!ObjectUtils.isEmpty(condition.getOrgNo())) {
              list.add(criteriaBuilder.equal(root.get("orgNo"), condition.getOrgNo()));
            }

            if (!ObjectUtils.isEmpty(condition.getType())) {
              list.add(criteriaBuilder.equal(root.get("type"), condition.getType()));
            }

            if (!ObjectUtils.isEmpty(condition.getAmtType())) {
              list.add(criteriaBuilder.equal(root.get("amtType"), condition.getAmtType()));
            }

            if (!ObjectUtils.isEmpty(condition.getAcctNo())) {
              list.add(criteriaBuilder.equal(root.get("acctNo"), condition.getAcctNo()));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryBeginDate())) {
              list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryBeginDate())));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryEndDate())) {
              list.add(criteriaBuilder.lessThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryEndDate())));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

}
