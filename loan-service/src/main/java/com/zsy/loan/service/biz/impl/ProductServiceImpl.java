package com.zsy.loan.service.biz.impl;

import com.google.common.base.Strings;
import com.zsy.loan.bean.entity.biz.TBizProductInfo;
import com.zsy.loan.bean.request.ProductInfoRequest;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.utils.DateUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
      condition = "%" + condition + "%";
      list = productRepo.findByProductNameLike(condition);
    }
    return list;
  }

  public Object save(ProductInfoRequest product) {

    TBizProductInfo info = TBizProductInfo.builder().build();
    BeanUtils.copyProperties(product,info);
    info.setCreateAt(DateUtil.getTimestamp());

    return productRepo.save(info);

  }
}
