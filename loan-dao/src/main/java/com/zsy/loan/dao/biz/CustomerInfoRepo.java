
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizCustomerInfo;
import com.zsy.loan.utils.factory.Page;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户信息Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface CustomerInfoRepo extends PagingAndSortingRepository< TBizCustomerInfo, Long>
    , JpaRepository< TBizCustomerInfo, Long>, JpaSpecificationExecutor< TBizCustomerInfo> {

  @Modifying
  @Transactional
  @Query(nativeQuery = true, value="update t_biz_customer_info t set status=?2 where t.id in (?1) ")
  int UpStatusByIds(List<Long> ids,Long status);

  @Modifying
  @Query(nativeQuery = true, value="update t_biz_customer_info t set status=?2 where t.id =?1 ")
  int UpStatusById(Long id,Long status);


}
