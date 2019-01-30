package com.zsy.loan.bean.entity.biz;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 借据表
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_biz_loan_info")
public class TBizLoanInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;


  @Column(name = "org_no")
  private Long orgNo;


  @Column(name = "product_no")
  private Long productNo;


  @Column(name = "cust_no")
  private Long custNo;


  @Column(name = "contr_no")
  private String contrNo;


  @Column(name = "loan_type")
  private Long loanType;


  @Column(name = "acct_date")
  private Date acctDate;


  @Column(name = "begin_date")
  private Date beginDate;


  @Column(name = "end_date")
  private Date endDate;


  @Column(name = "prin")
  private BigDecimal prin;


  @Column(name = "rate")
  private BigDecimal rate;


  @Column(name = "receive_interest")
  private BigDecimal receiveInterest;


  @Column(name = "repay_type")
  private Long repayType;


  @Column(name = "term_no")
  private Long termNo;


  @Column(name = "lending_date")
  private Date lendingDate;


  @Column(name = "lending_amt")
  private BigDecimal lendingAmt;


  @Column(name = "lending_acct")
  private Long lendingAcct;


  @Column(name = "external_acct")
  private String externalAcct;


  @Column(name = "service_fee")
  private BigDecimal serviceFee;


  @Column(name = "service_fee_type")
  private Long serviceFeeType;


  @Column(name = "dd_date")
  private Long ddDate;


  @Column(name = "is_pen")
  private Long isPen;


  @Column(name = "pen_rate")
  private BigDecimal penRate;


  @Column(name = "pen_number")
  private Long penNumber;


  @Column(name = "extension_no")
  private Long extensionNo;


  @Column(name = "extension_rate")
  private BigDecimal extensionRate;


  @Column(name = "schd_prin")
  private BigDecimal schdPrin;


  @Column(name = "schd_interest")
  private BigDecimal schdInterest;


  @Column(name = "schd_serv_fee")
  private BigDecimal schdServFee;


  @Column(name = "schd_pen")
  private BigDecimal schdPen;


  @Column(name = "tot_paid_prin")
  private BigDecimal totPaidPrin;


  @Column(name = "tot_paid_interest")
  private BigDecimal totPaidInterest;


  @Column(name = "tot_paid_serv_fee")
  private BigDecimal totPaidServFee;


  @Column(name = "tot_paid_pen")
  private BigDecimal totPaidPen;


  @Column(name = "tot_wav_amt")
  private BigDecimal totWavAmt;


  @Column(name = "status")
  private Long status;

  /**
   * 创建人
   */
  @Column(name = "create_by", updatable = false)
  @CreatedBy
  private Long createBy;

  /**
   * 修改人
   */
  @Column(name = "modified_by")
  @LastModifiedBy
  private Long modifiedBy;

  /**
   * 创建时间
   */
  @CreatedDate
  @Column(name = "create_at", updatable = false)
  private Timestamp createAt;

  /**
   * 修改时间
   */
  @LastModifiedDate
  @Column(name = "update_at")
  protected Timestamp updateAt;

  @Column(name = "remark")
  private String remark;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TBizLoanInfo that = (TBizLoanInfo) o;

    if (id != that.id) {
      return false;
    }
    if (orgNo != that.orgNo) {
      return false;
    }
    if (productNo != that.productNo) {
      return false;
    }
    if (custNo != that.custNo) {
      return false;
    }
    if (loanType != that.loanType) {
      return false;
    }
    if (repayType != that.repayType) {
      return false;
    }
    if (lendingAcct != that.lendingAcct) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    if (contrNo != null ? !contrNo.equals(that.contrNo) : that.contrNo != null) {
      return false;
    }
    if (acctDate != null ? !acctDate.equals(that.acctDate) : that.acctDate != null) {
      return false;
    }
    if (beginDate != null ? !beginDate.equals(that.beginDate) : that.beginDate != null) {
      return false;
    }
    if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) {
      return false;
    }
    if (prin != null ? !prin.equals(that.prin) : that.prin != null) {
      return false;
    }
    if (rate != null ? !rate.equals(that.rate) : that.rate != null) {
      return false;
    }
    if (receiveInterest != null ? !receiveInterest.equals(that.receiveInterest)
        : that.receiveInterest != null) {
      return false;
    }
    if (termNo != null ? !termNo.equals(that.termNo) : that.termNo != null) {
      return false;
    }
    if (lendingDate != null ? !lendingDate.equals(that.lendingDate) : that.lendingDate != null) {
      return false;
    }
    if (lendingAmt != null ? !lendingAmt.equals(that.lendingAmt) : that.lendingAmt != null) {
      return false;
    }
    if (externalAcct != null ? !externalAcct.equals(that.externalAcct)
        : that.externalAcct != null) {
      return false;
    }
    if (serviceFee != null ? !serviceFee.equals(that.serviceFee) : that.serviceFee != null) {
      return false;
    }
    if (serviceFeeType != null ? !serviceFeeType.equals(that.serviceFeeType)
        : that.serviceFeeType != null) {
      return false;
    }
    if (ddDate != null ? !ddDate.equals(that.ddDate) : that.ddDate != null) {
      return false;
    }
    if (isPen != null ? !isPen.equals(that.isPen) : that.isPen != null) {
      return false;
    }
    if (penRate != null ? !penRate.equals(that.penRate) : that.penRate != null) {
      return false;
    }
    if (penNumber != null ? !penNumber.equals(that.penNumber) : that.penNumber != null) {
      return false;
    }
    if (extensionNo != null ? !extensionNo.equals(that.extensionNo) : that.extensionNo != null) {
      return false;
    }
    if (extensionRate != null ? !extensionRate.equals(that.extensionRate)
        : that.extensionRate != null) {
      return false;
    }
    if (schdPrin != null ? !schdPrin.equals(that.schdPrin) : that.schdPrin != null) {
      return false;
    }
    if (schdInterest != null ? !schdInterest.equals(that.schdInterest) : that.schdInterest != null) {
      return false;
    }
    if (schdServFee != null ? !schdServFee.equals(that.schdServFee) : that.schdServFee != null) {
      return false;
    }
    if (schdPen != null ? !schdPen.equals(that.schdPen) : that.schdPen != null) {
      return false;
    }
    if (totPaidPrin != null ? !totPaidPrin.equals(that.totPaidPrin) : that.totPaidPrin != null) {
      return false;
    }
    if (totPaidInterest != null ? !totPaidInterest.equals(that.totPaidInterest)
        : that.totPaidInterest != null) {
      return false;
    }
    if (totPaidServFee != null ? !totPaidServFee.equals(that.totPaidServFee)
        : that.totPaidServFee != null) {
      return false;
    }
    if (totPaidPen != null ? !totPaidPen.equals(that.totPaidPen) : that.totPaidPen != null) {
      return false;
    }
    if (totWavAmt != null ? !totWavAmt.equals(that.totWavAmt) : that.totWavAmt != null) {
      return false;
    }
    if (createAt != null ? !createAt.equals(that.createAt) : that.createAt != null) {
      return false;
    }
    if (updateAt != null ? !updateAt.equals(that.updateAt) : that.updateAt != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (orgNo ^ (orgNo >>> 32));
    result = 31 * result + (int) (productNo ^ (productNo >>> 32));
    result = 31 * result + (int) (custNo ^ (custNo >>> 32));
    result = 31 * result + (contrNo != null ? contrNo.hashCode() : 0);
    result = 31 * result + (int) (loanType ^ (loanType >>> 32));
    result = 31 * result + (acctDate != null ? acctDate.hashCode() : 0);
    result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (prin != null ? prin.hashCode() : 0);
    result = 31 * result + (rate != null ? rate.hashCode() : 0);
    result = 31 * result + (receiveInterest != null ? receiveInterest.hashCode() : 0);
    result = 31 * result + (int) (repayType ^ (repayType >>> 32));
    result = 31 * result + (termNo != null ? termNo.hashCode() : 0);
    result = 31 * result + (lendingDate != null ? lendingDate.hashCode() : 0);
    result = 31 * result + (lendingAmt != null ? lendingAmt.hashCode() : 0);
    result = 31 * result + (int) (lendingAcct ^ (lendingAcct >>> 32));
    result = 31 * result + (externalAcct != null ? externalAcct.hashCode() : 0);
    result = 31 * result + (serviceFee != null ? serviceFee.hashCode() : 0);
    result = 31 * result + (serviceFeeType != null ? serviceFeeType.hashCode() : 0);
    result = 31 * result + (ddDate != null ? ddDate.hashCode() : 0);
    result = 31 * result + (isPen != null ? isPen.hashCode() : 0);
    result = 31 * result + (penRate != null ? penRate.hashCode() : 0);
    result = 31 * result + (penNumber != null ? penNumber.hashCode() : 0);
    result = 31 * result + (extensionNo != null ? extensionNo.hashCode() : 0);
    result = 31 * result + (extensionRate != null ? extensionRate.hashCode() : 0);
    result = 31 * result + (schdPrin != null ? schdPrin.hashCode() : 0);
    result = 31 * result + (schdInterest != null ? schdInterest.hashCode() : 0);
    result = 31 * result + (schdServFee != null ? schdServFee.hashCode() : 0);
    result = 31 * result + (schdPen != null ? schdPen.hashCode() : 0);
    result = 31 * result + (totPaidPrin != null ? totPaidPrin.hashCode() : 0);
    result = 31 * result + (totPaidInterest != null ? totPaidInterest.hashCode() : 0);
    result = 31 * result + (totPaidServFee != null ? totPaidServFee.hashCode() : 0);
    result = 31 * result + (totPaidPen != null ? totPaidPen.hashCode() : 0);
    result = 31 * result + (totWavAmt != null ? totWavAmt.hashCode() : 0);
    result = 31 * result + (int) (status ^ (status >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
