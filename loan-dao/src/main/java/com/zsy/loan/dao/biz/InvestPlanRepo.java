
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizInvestPlan;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * 回款计划Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface InvestPlanRepo extends PagingAndSortingRepository<TBizInvestPlan, Long>
    , JpaRepository<TBizInvestPlan, Long>, JpaSpecificationExecutor<TBizInvestPlan> {

  List<TBizInvestPlan> findByInvestNo(Long investNo);

  @Modifying
  @Query(nativeQuery = true, value = "delete from t_biz_invest_plan where invest_no=?1")
  int deleteByInvestNo(Long investNo);

  @Query(nativeQuery = true, value = "select * from t_biz_invest_plan where invest_no=:investNo and begin_date <:currentDate and "
      + " end_date>=:currentDate order by id asc ")
  List<TBizInvestPlan> findCurrentTermRecord(@Param("investNo") Long investNo, @Param("currentDate") Date currentDate);

  @Query(nativeQuery = true, value = "select * from t_biz_invest_plan where invest_no=:investNo and status in (:status) and term_no <:currentTermNo order by id asc ")
  List<TBizInvestPlan> findBeforeRecord(@Param("investNo") Long investNo, @Param("status") List<Long> status, @Param("currentTermNo") Long
      currentTermNo);

  @Query(nativeQuery = true, value = "select * from t_biz_invest_plan where invest_no=:investNo and term_no >:currentTermNo order by id asc ")
  List<TBizInvestPlan> findAfterRecord(@Param("investNo") Long investNo, @Param("currentTermNo") Long currentTermNo);

  @Query(nativeQuery = true, value =
      "select count(*) as num,sum(chd_interest) interest from t_biz_invest_plan where invest_no=:investNo and status in "
          + "(:status) AND end_date<=:settleDate ")
  Map<String, Object> findSettlement(@Param("investNo") Long investNo, @Param("status") List<Long> status, @Param("settleDate") Date settleDate);

  @Query(nativeQuery = true, value = "select * from t_biz_invest_plan where invest_no=:investNo and status in "
      + "(:status) AND end_date<=:settleDate order by id asc  ")
  List<TBizInvestPlan> findSettlementRecord(@Param("investNo") Long investNo, @Param("status") List<Long> status, @Param("settleDate") Date
      settleDate);

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_invest_plan set status=:newStatus,acct_date=:sysAcctDate where invest_no=:investNo and "
      + "status=:oldStatus and dd_date<=:settleDate ")
  int updateSettlement(@Param("investNo") Long investNo,@Param("oldStatus") Long oldStatus,@Param("newStatus") Long newStatus,@Param("settleDate") Date
      settleDate,@Param("sysAcctDate") Date sysAcctDate);

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_invest_plan set status=:newStatus,acct_date=:sysAcctDate where invest_no=:investNo and "
      + "status=:oldStatus and dd_date>:settleDate ")
  int updateSettlementAfter(@Param("investNo") Long investNo,@Param("oldStatus") Long oldStatus,@Param("newStatus") Long newStatus,@Param("settleDate") Date
      settleDate,@Param("sysAcctDate") Date sysAcctDate);

}
