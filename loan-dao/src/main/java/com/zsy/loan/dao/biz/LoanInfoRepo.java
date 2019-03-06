
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

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
  @Query(nativeQuery = true, value = "update t_biz_loan_info t set status=?2,remark=?3,lending_date=?4,dd_date=?5,acct_date=?6,tot_paid_serv_fee=?7 "
      + "  where t.id =?1 ")
  int put(Long id, Long status, String remark, Date lendingDate, Long ddDate, Date acctDate, BigDecimal totPaidServFee);

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_loan_info t set status=:status,remark=:remark,acct_date=:acctDate,"
      + "schd_interest=:schdInterest,end_date=:endDate,extension_no=:extensionNo,extension_rate=:extensionRate where t.id =:id ")
  void delay(@Param("id") Long id, @Param("status") Long status, @Param("remark") String remark,
      @Param("endDate") Date endDate, @Param("schdInterest") BigDecimal schdInterest,
      @Param("acctDate") Date acctDate, @Param("extensionNo") Long extensionNo,
      @Param("extensionRate") BigDecimal extensionRate);

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_loan_info t set status=:status,remark=:remark,acct_date=:acctDate,"
      + "schd_interest=:schdInterest,tot_paid_prin=:totPaidPrin,tot_paid_interest=:totPaidInterest,tot_paid_serv_fee=:totPaidServFee,"
      + "tot_paid_pen=:totPaidPen,tot_wav_amt=:totWavAmt where t.id =:id ")
  void prepay(@Param("id") Long id, @Param("status") Long status, @Param("remark") String remark, @Param("acctDate") Date acctDate,
      @Param("totPaidPrin") BigDecimal totPaidPrin, @Param("totPaidInterest") BigDecimal totPaidInterest, @Param("totPaidPen") BigDecimal
      totPaidPen, @Param("totPaidServFee") BigDecimal totPaidServFee, @Param("totWavAmt") BigDecimal totWavAmt,
      @Param("schdInterest") BigDecimal schdInterest);

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_loan_info t set status=:status,acct_date=:acctDate,"
      + "tot_paid_prin = tot_paid_prin+:paidPrin,tot_paid_interest= tot_paid_interest+:paidInterest,"
      + "tot_paid_serv_fee = tot_paid_serv_fee+:paidServFee,tot_paid_pen=tot_paid_pen+:paidPen,"
      + "tot_wav_amt=tot_wav_amt+:wavAmt,remark=:remark where t.id =:id ")
  void repay(@Param("id") Long id, @Param("acctDate") Date acctDate,@Param("status") Long status,
      @Param("paidPrin") BigDecimal paidPrin, @Param("paidInterest") BigDecimal paidInterest, @Param("paidPen") BigDecimal
      paidPen, @Param("paidServFee") BigDecimal paidServFee, @Param("wavAmt") BigDecimal wavAmt, @Param("remark") String remark);
}
