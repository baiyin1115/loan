
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizLoanInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 借据Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface LoanInfoRepo extends PagingAndSortingRepository< TBizLoanInfo, Long>
    , JpaRepository< TBizLoanInfo, Long>, JpaSpecificationExecutor< TBizLoanInfo> {

}
