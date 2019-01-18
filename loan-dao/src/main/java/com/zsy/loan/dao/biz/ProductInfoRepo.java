
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizProductInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 产品代码Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface ProductInfoRepo extends PagingAndSortingRepository< TBizProductInfo, Long>
    , JpaRepository< TBizProductInfo, Long>, JpaSpecificationExecutor< TBizProductInfo> {

  List<TBizProductInfo> findByProductNameLike(String condition);
}
