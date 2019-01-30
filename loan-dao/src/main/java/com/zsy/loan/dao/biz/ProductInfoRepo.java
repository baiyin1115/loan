
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizProductInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 产品代码Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface ProductInfoRepo extends PagingAndSortingRepository< TBizProductInfo, Long>
    , JpaRepository< TBizProductInfo, Long>, JpaSpecificationExecutor< TBizProductInfo> {

  List<TBizProductInfo> findByProductNameLike(String condition);

  @Modifying
  @Transactional
  @Query(nativeQuery = true, value="delete t from t_biz_product_info t where t.id in (?1) ")
  int deleteByIds(List<Long> ids);

  @Query(nativeQuery = true, value = "SELECT m1.id AS id,'0' AS pId,m1.product_name AS NAME,'false' AS isOpen FROM t_biz_product_info as m1 where org_no in (?1) order by id ASC")
  List getTreeList(List<Integer> orgNos);
}
