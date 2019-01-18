
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizInvestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 融资信息Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface InvestInfoRepo extends PagingAndSortingRepository< TBizInvestInfo, Long>
    , JpaRepository< TBizInvestInfo, Long>, JpaSpecificationExecutor< TBizInvestInfo> {

}
