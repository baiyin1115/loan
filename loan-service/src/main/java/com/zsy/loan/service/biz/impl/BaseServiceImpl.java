package com.zsy.loan.service.biz.impl;

import com.zsy.loan.service.shiro.ShiroKit;
import com.zsy.loan.utils.factory.Page;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.ObjectUtils;

/**
 * Created by Administrator on 2019/3/7/007.
 */
public class BaseServiceImpl {

  protected void setOrgList(Integer orgNo, Path path, CriteriaBuilder criteriaBuilder,
      List<Predicate> list) {
    if (!ObjectUtils.isEmpty(orgNo)) {
      list.add(criteriaBuilder.equal(path, orgNo)); //指定机构
    } else {

      if (!ShiroKit.isAdmin()) {
        // 只能查当前登录用户拥有查询权限的
        List orgList = ShiroKit.getDeptDataScope();

        CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
        in.value(orgList);
        list.add(in);
      }
    }
  }

  protected <T> Pageable getPageable(Page<T> page, List<Order> orders) {
    Pageable pageable;
    if (page.isOpenSort()) {
      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), Sort.by(orders));
    }
    return pageable;
  }

//
//  /**
//   * 设置排序
//   */
//  protected abstract <T> List<Order> setOrders(T condition);
//
//  /**
//   * 设置查询条件
//   */
//  protected abstract <T> List<Predicate> setWhere(Root<T> root, T condition, CriteriaBuilder criteriaBuilder);
//
//  /**
//   * 查询page
//   */
//  protected abstract <T> org.springframework.data.domain.Page<T> findPage(Specification<T> cation, Pageable pageable);
//
//  public <T> Page<T> getPlanPages(Page<T> page, T condition) {
//    Pageable pageable = null;
//    if (page.isOpenSort()) {
//      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(),
//          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
//    } else {
//
//      List<Order> orders = setOrders(condition);
//      pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), Sort.by(orders));
//    }
//
//    Specification<T> cation = new Specification<T>() {
//      @Override
//      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//        List<Predicate> list = setWhere(root, condition, criteriaBuilder);
//        Predicate[] p = new Predicate[list.size()];
//        return criteriaBuilder.and(list.toArray(p));
//      }
//    };
//
//    org.springframework.data.domain.Page<T> page1 = findPage(cation, pageable);
//
//    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
//    page.setRecords(page1.getContent());
//    return page;
//  }


}
