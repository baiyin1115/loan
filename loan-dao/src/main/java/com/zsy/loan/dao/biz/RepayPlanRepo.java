
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * 还款计划Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface RepayPlanRepo extends PagingAndSortingRepository<TBizRepayPlan, Long>
    , JpaRepository<TBizRepayPlan, Long>, JpaSpecificationExecutor<TBizRepayPlan> {

  @Modifying
  @Query(nativeQuery = true, value = "delete from t_biz_repay_plan where loan_no=?1")
  int deleteByLoanNo(Long loanNo);

  List<TBizRepayPlan> findByLoanNo(Long loanNo);

  @Query(nativeQuery = true, value = "select * from t_biz_repay_plan where loan_no=:loanNo and status in (:status) order by id asc ")
  List<TBizRepayPlan> findNotPayRecord(@Param("loanNo") Long loanNo,@Param("status") List<Long> status);

  /**
   * 开始时间、结束时间不能查询出来当前期，要不计算会特别复杂
   * @param loanNo
   * @param currentDate
   * @return
   */
  @Query(nativeQuery = true, value = "select * from t_biz_repay_plan where loan_no=:loanNo and begin_date <:currentDate and end_date>:currentDate "
      + "order by id asc ")
  List<TBizRepayPlan> findCurrentTermRecord(@Param("loanNo") Long loanNo,@Param("currentDate") Date currentDate);

  @Query(nativeQuery = true, value = "select * from t_biz_repay_plan where loan_no=:loanNo and status in (:status) and term_no < :currentTermNo order by id asc ")
  List<TBizRepayPlan> findNotPayRecord(@Param("loanNo") Long loanNo,@Param("status") List<Long> status,@Param("currentTermNo") Long currentTermNo);

  @Query(nativeQuery = true, value = "select * from t_biz_repay_plan where loan_no=:loanNo and term_no > :currentTermNo order by id asc ")
  List<TBizRepayPlan> findAfterPayRecord(@Param("loanNo") Long loanNo,@Param("currentTermNo") Long currentTermNo);

  @Query(nativeQuery = true, value = "SELECT * FROM t_biz_repay_plan WHERE id=?1 AND status=?2 FOR UPDATE ")
  TBizRepayPlan lockRecordByIdStatus(Long id, Long status);

}
