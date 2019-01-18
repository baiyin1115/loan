
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizCustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 客户信息Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface CustomerInfoRepo extends PagingAndSortingRepository< TBizCustomerInfo, Long>
    , JpaRepository< TBizCustomerInfo, Long>, JpaSpecificationExecutor< TBizCustomerInfo> {

}
