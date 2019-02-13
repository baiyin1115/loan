
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 借据Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface LoanInfoRepo extends PagingAndSortingRepository<TBizLoanInfo, Long>
    , JpaRepository<TBizLoanInfo, Long>, JpaSpecificationExecutor<TBizLoanInfo> {

  @Query(nativeQuery = true, value = "SELECT * FROM t_biz_loan_info WHERE id=?1 AND status=?2 FOR UPDATE ")
  TBizLoanInfo lockRecordByIdStatus(Long id, Long status);

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_loan_info t set status=?2,remark=?3,lending_date=?4,dd_date=?5,acct_date=?6 where t.id =?1 ")
  int put(Long id, Long status, String remark, Date lendingDate, Long ddDate,Date acctDate);

}
