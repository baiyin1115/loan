package com.zsy.loan.bean.entity.biz;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 还款计划表
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
@DynamicUpdate
@Table(name = "t_biz_repay_plan")
public class TBizRepayPlan implements Serializable, IBizInfo {


  private static final long serialVersionUID = 1028676638418093831L;

  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;


  @Column(name = "loan_no")
  private Long loanNo;


  @Column(name = "org_no")
  private Integer orgNo;


  @Column(name = "product_no")
  private Long productNo;


  @Column(name = "cust_no")
  private Long custNo;


  @Column(name = "acct_date")
  private Date acctDate;


  @Column(name = "term_no")
  private Long termNo;


  @Column(name = "rate")
  private BigDecimal rate;


  @Column(name = "begin_date")
  private Date beginDate;


  @Column(name = "end_date")
  private Date endDate;


  @Column(name = "dd_num")
  private Long ddNum;


  @Column(name = "dd_date")
  private Date ddDate;


  @Column(name = "external_acct")
  private String externalAcct;


  @Column(name = "in_acct_no")
  private Long inAcctNo;


  @Column(name = "ctd_prin")
  private BigDecimal ctdPrin;


  @Column(name = "ctd_interest")
  private BigDecimal ctdInterest;


  @Column(name = "ctd_serv_fee")
  private BigDecimal ctdServFee;


  @Column(name = "ctd_pen")
  private BigDecimal ctdPen;


  @Column(name = "paid_prin")
  private BigDecimal paidPrin;


  @Column(name = "paid_interest")
  private BigDecimal paidInterest;


  @Column(name = "paid_serv_fee")
  private BigDecimal paidServFee;


  @Column(name = "paid_pen")
  private BigDecimal paidPen;


  @Column(name = "wav_amt")
  private BigDecimal wavAmt;


  @Column(name = "status")
  private Long status;

  @Transient
  private String orgName;

  @Transient
  private String productName;

  @Transient
  private String custName;

  @Transient
  private String inAcctName;

  @Transient
  private String statusName;

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

    TBizRepayPlan that = (TBizRepayPlan) o;

    if (id != that.id) {
      return false;
    }
    if (loanNo != that.loanNo) {
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
    if (termNo != that.termNo) {
      return false;
    }
    if (ddNum != that.ddNum) {
      return false;
    }
    if (inAcctNo != that.inAcctNo) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    if (acctDate != null ? !acctDate.equals(that.acctDate) : that.acctDate != null) {
      return false;
    }
    if (rate != null ? !rate.equals(that.rate) : that.rate != null) {
      return false;
    }
    if (beginDate != null ? !beginDate.equals(that.beginDate) : that.beginDate != null) {
      return false;
    }
    if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) {
      return false;
    }
    if (ddDate != null ? !ddDate.equals(that.ddDate) : that.ddDate != null) {
      return false;
    }
    if (externalAcct != null ? !externalAcct.equals(that.externalAcct)
        : that.externalAcct != null) {
      return false;
    }
    if (ctdPrin != null ? !ctdPrin.equals(that.ctdPrin) : that.ctdPrin != null) {
      return false;
    }
    if (ctdInterest != null ? !ctdInterest.equals(that.ctdInterest) : that.ctdInterest != null) {
      return false;
    }
    if (ctdServFee != null ? !ctdServFee.equals(that.ctdServFee) : that.ctdServFee != null) {
      return false;
    }
    if (ctdPen != null ? !ctdPen.equals(that.ctdPen) : that.ctdPen != null) {
      return false;
    }
    if (paidPrin != null ? !paidPrin.equals(that.paidPrin) : that.paidPrin != null) {
      return false;
    }
    if (paidInterest != null ? !paidInterest.equals(that.paidInterest) : that.paidInterest != null) {
      return false;
    }
    if (paidServFee != null ? !paidServFee.equals(that.paidServFee) : that.paidServFee != null) {
      return false;
    }
    if (paidPen != null ? !paidPen.equals(that.paidPen) : that.paidPen != null) {
      return false;
    }
    if (wavAmt != null ? !wavAmt.equals(that.wavAmt) : that.wavAmt != null) {
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
    result = 31 * result + (int) (loanNo ^ (loanNo >>> 32));
    result = 31 * result + (int) (orgNo ^ (orgNo >>> 32));
    result = 31 * result + (int) (productNo ^ (productNo >>> 32));
    result = 31 * result + (int) (custNo ^ (custNo >>> 32));
    result = 31 * result + (acctDate != null ? acctDate.hashCode() : 0);
    result = 31 * result + (int) (termNo ^ (termNo >>> 32));
    result = 31 * result + (rate != null ? rate.hashCode() : 0);
    result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (int) (ddNum ^ (ddNum >>> 32));
    result = 31 * result + (ddDate != null ? ddDate.hashCode() : 0);
    result = 31 * result + (externalAcct != null ? externalAcct.hashCode() : 0);
    result = 31 * result + (int) (inAcctNo ^ (inAcctNo >>> 32));
    result = 31 * result + (ctdPrin != null ? ctdPrin.hashCode() : 0);
    result = 31 * result + (ctdInterest != null ? ctdInterest.hashCode() : 0);
    result = 31 * result + (ctdServFee != null ? ctdServFee.hashCode() : 0);
    result = 31 * result + (ctdPen != null ? ctdPen.hashCode() : 0);
    result = 31 * result + (paidPrin != null ? paidPrin.hashCode() : 0);
    result = 31 * result + (paidInterest != null ? paidInterest.hashCode() : 0);
    result = 31 * result + (paidServFee != null ? paidServFee.hashCode() : 0);
    result = 31 * result + (paidPen != null ? paidPen.hashCode() : 0);
    result = 31 * result + (wavAmt != null ? wavAmt.hashCode() : 0);
    result = 31 * result + (int) (status ^ (status >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
