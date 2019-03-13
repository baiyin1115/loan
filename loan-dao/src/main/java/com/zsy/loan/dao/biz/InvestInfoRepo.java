
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizInvestInfo;
import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 融资信息Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface InvestInfoRepo extends PagingAndSortingRepository<TBizInvestInfo, Long>
    , JpaRepository<TBizInvestInfo, Long>, JpaSpecificationExecutor<TBizInvestInfo> {

  @Modifying
  @Transactional
  @Query(nativeQuery = true, value = "delete t from t_biz_invest_info t where t.id in (?1) ")
  void deleteByIds(List<Long> investIds);

  @Query(nativeQuery = true, value = "SELECT * FROM t_biz_invest_info WHERE id=?1 AND status=?2 FOR UPDATE ")
  TBizInvestInfo lockRecordByIdStatus(Long id, Long status);

//  void confirm(Long id, Long status, String remark, Long ddDate, Date sysAcctDate, BigDecimal totSchdInterest);

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_invest_info t set status=:status,remark=:remark,dd_date=:ddDate,acct_date=:sysAcctDate,"
      + "tot_schd_interest=:totSchdInterest where t.id =:id ")
  int confirm(@Param("id") Long id, @Param("status") Long status, @Param("remark") String remark, @Param("ddDate") Long ddDate,
      @Param("sysAcctDate") Date sysAcctDate,
      @Param("totSchdInterest") BigDecimal totSchdInterest);
}
