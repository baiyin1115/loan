package com.zsy.loan.service.business.system.impl;

import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.business.system.UserService;
import com.zsy.loan.utils.DateUtil;
import com.zsy.loan.utils.factory.Page;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created  on 2018/4/7 0007.
 *
 * @author enilu
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public List<User> findAll(final Map<String, Object> params) {
    return userRepository.findAll(new Specification<User>() {
      @Override
      public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder criteriaBuilder) {
        List<Predicate> list = new ArrayList<Predicate>();
        if (params.get("deptid") != null && !Strings
            .isNullOrEmpty(params.get("deptid").toString())) {
          list.add(criteriaBuilder
              .equal(root.get("deptid").as(String.class), params.get("deptid").toString()));
        }
        if (params.get("name") != null && !Strings.isNullOrEmpty(params.get("name").toString())) {
          list.add(criteriaBuilder
              .equal(root.get("name").as(String.class), params.get("name").toString()));
        }
        if (params.get("beginTime") != null && !Strings
            .isNullOrEmpty(params.get("beginTime").toString())) {
          list.add(criteriaBuilder.greaterThan(root.get("createtime").as(Date.class),
              DateUtil.parseDate(params.get("beginTime").toString())));
        }
        if (params.get("endTime") != null && !Strings
            .isNullOrEmpty(params.get("endTime").toString())) {
          list.add(criteriaBuilder.lessThan(root.get("createtime").as(Date.class),
              DateUtil.parseDate(params.get("endTime").toString())));
        }

        Predicate[] p = new Predicate[list.size()];
        return criteriaBuilder.and(list.toArray(p));
      }
    });

  }

  @Override
  public Page<User> findPage(Page<User> page, Map<String, Object> params) {
    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize());
    }

    org.springframework.data.domain.Page<User> pageResult = userRepository
        .findAll(new Specification<User>() {


          @Override
          public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {
            List<Predicate> list = new ArrayList<Predicate>();
//                if(!Strings.isNullOrEmpty(beginTime)){
//                    list.add(criteriaBuilder.greaterThan(root.get("createtime").as(Date.class), DateUtil.parseDate(beginTime)));
//                }
//                if(!Strings.isNullOrEmpty(endTime)){
//                    list.add(criteriaBuilder.lessThan(root.get("createtime").as(Date.class), DateUtil.parseDate(endTime)));
//                }
//                if(!Strings.isNullOrEmpty(logName)){
//                    list.add(criteriaBuilder.like(root.get("logname").as(String.class),logName));
//                }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);
    page.setTotal(Integer.valueOf(pageResult.getTotalElements() + ""));
    page.setRecords(pageResult.getContent());
    return page;
  }
}
