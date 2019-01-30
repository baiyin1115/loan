package com.zsy.loan.service.biz.impl;

import com.google.common.base.Strings;
import com.zsy.loan.bean.entity.biz.TBizProductInfo;
import com.zsy.loan.bean.request.ProductInfoRequest;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.shiro.ShiroKit;
import com.zsy.loan.utils.StringUtils;
import com.zsy.loan.utils.factory.Page;
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

/**
 * 产品服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class ProductServiceImpl {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductInfoRepo productRepo;

  public List<TBizProductInfo> query(String condition) {
    List<TBizProductInfo> list = new ArrayList<>();
    if (Strings.isNullOrEmpty(condition)) {
      list = (List<TBizProductInfo>) productRepo.findAll();
    } else {
      condition = condition.trim() + "%";
      list = productRepo.findByProductNameLike(condition);
    }
    return list;
  }

  public Page getTBizProducts(Page<TBizProductInfo> page, String condition) {
    Pageable pageable = null;
    if (page.isOpenSort()) {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(),
          page.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderByField());
    } else {
      pageable = new PageRequest(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC, "id");
    }

    org.springframework.data.domain.Page<TBizProductInfo> page1 = productRepo
        .findAll(new Specification<TBizProductInfo>() {


          @Override
          public Predicate toPredicate(Root<TBizProductInfo> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {
            List<Predicate> list = new ArrayList<Predicate>();
            if (StringUtils.isNotEmpty(condition)) {
              list.add(
                  criteriaBuilder
                      .like(root.get("productName").as(String.class), condition.trim() + "%"));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);
    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

  public Object save(ProductInfoRequest product) {

    TBizProductInfo info = TBizProductInfo.builder().build();
    BeanUtils.copyProperties(product, info);

    return productRepo.save(info);

  }

  public String getProductName(Long productId) {
    TBizProductInfo info = productRepo.findById(productId).get();

    if (info == null) {
      return "";
    } else {
      return info.getProductName();
    }
  }

  public List<TBizProductInfo> getProductNames(List<Long> productIds) {
    List<TBizProductInfo> infos = productRepo.findAllById(productIds);

    if (infos == null) {
      return null;
    } else {
      return infos;
    }
  }

  public List<ZTreeNode> getTreeList() {

    // 只能查当前登录用户拥有查询权限的
    List<Integer> orgList = ShiroKit.getDeptDataScope();

    List list = productRepo.getTreeList(orgList);

    List<ZTreeNode> nodes = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      Object[] source = (Object[]) list.get(i);
      ZTreeNode node = new ZTreeNode();
      node.setId(Long.valueOf(source[0].toString()));
      node.setpId(Long.valueOf(source[1].toString()));
      node.setName(source[2].toString());
      node.setIsOpen(Boolean.valueOf(source[3].toString()));
      nodes.add(node);
    }
    return nodes;
  }
}
