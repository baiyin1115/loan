package com.zsy.loan.bean.entity.biz;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 *
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
@Table(name = "t_biz_invest_info")
public class TBizInvestInfo implements Serializable {

  private static final long serialVersionUID = 2406673866480723832L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;


  @Column(name = "org_no")
  private Integer orgNo;


  @Column(name = "invest_type")
  private Integer investType;


  @Column(name = "cust_no")
  private Long custNo;


  @Column(name = "in_acct_no")
  private Long inAcctNo;


  @Column(name = "external_acct")
  private String externalAcct;


  @Column(name = "prin")
  private BigDecimal prin;


  @Column(name = "acct_date")
  private Date acctDate;


  @Column(name = "begin_date")
  private Date beginDate;


  @Column(name = "end_date")
  private Date endDate;


  @Column(name = "rate")
  private BigDecimal rate;


  @Column(name = "term_no")
  private Long termNo;


  @Column(name = "cycle_interval")
  private Long cycleInterval;


  @Column(name = "status")
  private Long status;


  @Column(name = "dd_date")
  private Long ddDate;


  @Column(name = "extension_no")
  private Long extensionNo;


  @Column(name = "extension_rate")
  private BigDecimal extensionRate;


  @Column(name = "tot_schd_interest")
  private BigDecimal totSchdInterest;


  @Column(name = "tot_accrued_interest")
  private BigDecimal totAccruedInterest;


  @Column(name = "tot_paid_prin")
  private BigDecimal totPaidPrin;


  @Column(name = "tot_paid_interest")
  private BigDecimal totPaidInterest;


  @Column(name = "tot_wav_amt")
  private BigDecimal totWavAmt;

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

  @Transient
  private String statusName;

  @Transient
  private String orgName;

  @Transient
  private String custName;

  @Transient
  private String inAcctName;

  @Transient
  private String investTypeName;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TBizInvestInfo that = (TBizInvestInfo) o;

    if (id != that.id) {
      return false;
    }
    if (orgNo != that.orgNo) {
      return false;
    }
    if (custNo != that.custNo) {
      return false;
    }
    if (inAcctNo != that.inAcctNo) {
      return false;
    }
    if (cycleInterval != that.cycleInterval) {
      return false;
    }
    if (status != that.status) {
      return false;
    }

    if (externalAcct != null ? !externalAcct.equals(that.externalAcct)
        : that.externalAcct != null) {
      return false;
    }
    if (prin != null ? !prin.equals(that.prin) : that.prin != null) {
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
    if (rate != null ? !rate.equals(that.rate) : that.rate != null) {
      return false;
    }
    if (termNo != null ? !termNo.equals(that.termNo) : that.termNo != null) {
      return false;
    }
    if (ddDate != null ? !ddDate.equals(that.ddDate) : that.ddDate != null) {
      return false;
    }
    if (extensionNo != null ? !extensionNo.equals(that.extensionNo) : that.extensionNo != null) {
      return false;
    }
    if (extensionRate != null ? !extensionRate.equals(that.extensionRate)
        : that.extensionRate != null) {
      return false;
    }
    if (totSchdInterest != null ? !totSchdInterest.equals(that.totSchdInterest)
        : that.totSchdInterest != null) {
      return false;
    }
    if (totPaidPrin != null ? !totPaidPrin.equals(that.totPaidPrin) : that.totPaidPrin != null) {
      return false;
    }
    if (totPaidInterest != null ? !totPaidInterest.equals(that.totPaidInterest)
        : that.totPaidInterest != null) {
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
    result = 31 * result + (int) (custNo ^ (custNo >>> 32));
    result = 31 * result + (int) (inAcctNo ^ (inAcctNo >>> 32));
    result = 31 * result + (externalAcct != null ? externalAcct.hashCode() : 0);
    result = 31 * result + (prin != null ? prin.hashCode() : 0);
    result = 31 * result + (acctDate != null ? acctDate.hashCode() : 0);
    result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (rate != null ? rate.hashCode() : 0);
    result = 31 * result + (termNo != null ? termNo.hashCode() : 0);
    result = 31 * result + (int) (cycleInterval ^ (cycleInterval >>> 32));
    result = 31 * result + (int) (status ^ (status >>> 32));
    result = 31 * result + (ddDate != null ? ddDate.hashCode() : 0);
    result = 31 * result + (extensionNo != null ? extensionNo.hashCode() : 0);
    result = 31 * result + (extensionRate != null ? extensionRate.hashCode() : 0);
    result = 31 * result + (totSchdInterest != null ? totSchdInterest.hashCode() : 0);
    result = 31 * result + (totPaidPrin != null ? totPaidPrin.hashCode() : 0);
    result = 31 * result + (totPaidInterest != null ? totPaidInterest.hashCode() : 0);
    result = 31 * result + (totWavAmt != null ? totWavAmt.hashCode() : 0);
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
