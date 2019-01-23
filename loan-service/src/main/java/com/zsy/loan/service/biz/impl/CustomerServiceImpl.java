package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.entity.biz.TBizCustomerInfo;
import com.zsy.loan.bean.enumeration.BizTypeEnum.CustomerStatusEnum;
import com.zsy.loan.bean.request.CustomerInfoRequest;
import com.zsy.loan.dao.biz.CustomerInfoRepo;
import com.zsy.loan.utils.StringUtils;
import com.zsy.loan.utils.factory.Page;
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

/**
 * 客户服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class CustomerServiceImpl {

  @Autowired
  private CustomerInfoRepo customerInfoRepo;


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

    org.springframework.data.domain.Page<TBizCustomerInfo> page1 = customerInfoRepo
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

  public Object save(CustomerInfoRequest customer) {

    TBizCustomerInfo info = TBizCustomerInfo.builder().build();
    BeanUtils.copyProperties(customer, info);

    return customerInfoRepo.save(info);
  }

  /**
   * 逻辑上删除用户
   * @param ids
   * @return
   */
  public Object logicDelete(List<Long> ids) {

    /**
     * 校验账户是否有余额
     */
    //TODO
    /**
     * 校验是否有进行中的项目
     */
    //TODO

    return customerInfoRepo.UpStatusByIds(ids, CustomerStatusEnum.DELETE.getValue());
  }

  /**
   * 设置黑名单
   * @param ids
   * @return
   */
  public Object setBlackList(List<Long> ids) {
    return customerInfoRepo.UpStatusByIds(ids, CustomerStatusEnum.BLACKLIST.getValue());
  }

  /**
   * 取消黑名单
   * @param ids
   * @return
   */
  public Object cancelBlackList(List<Long> ids) {
    return customerInfoRepo.UpStatusByIds(ids, CustomerStatusEnum.NORMAL.getValue());
  }
}
