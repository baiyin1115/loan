package com.zsy.loan.bean.entity.biz;

import com.zsy.loan.bean.convey.IBizToAcct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
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
 * 回款计划表
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
@Table(name = "t_biz_invest_plan")
public class TBizInvestPlan implements Serializable{

  private static final long serialVersionUID = 946438418079009769L;

  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;


  @Column(name = "invest_no")
  private Long investNo;


  @Column(name = "org_no")
  private Integer orgNo;


  @Column(name = "cust_no")
  private Long custNo;


  @Column(name = "term_no")
  private Long termNo;

  @Column(name = "acct_date")
  private Date acctDate;

  @Column(name = "dd_date")
  private Date ddDate;


  @Column(name = "rate")
  private BigDecimal rate;


  @Column(name = "begin_date")
  private Date beginDate;


  @Column(name = "end_date")
  private Date endDate;


  @Column(name = "dd_num")
  private Long ddNum;


  @Column(name = "dd_prin")
  private BigDecimal ddPrin;


  @Column(name = "chd_interest")
  private BigDecimal chdInterest;


  @Column(name = "paid_interest")
  private BigDecimal paidInterest;


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

    TBizInvestPlan that = (TBizInvestPlan) o;

    if (id != that.id) {
      return false;
    }
    if (investNo != that.investNo) {
      return false;
    }
    if (orgNo != that.orgNo) {
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
    if (status != that.status) {
      return false;
    }
    if (ddDate != null ? !ddDate.equals(that.ddDate) : that.ddDate != null) {
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
    if (ddPrin != null ? !ddPrin.equals(that.ddPrin) : that.ddPrin != null) {
      return false;
    }
    if (chdInterest != null ? !chdInterest.equals(that.chdInterest) : that.chdInterest != null) {
      return false;
    }
    if (paidInterest != null ? !paidInterest.equals(that.paidInterest) : that.paidInterest != null) {
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
    result = 31 * result + (int) (investNo ^ (investNo >>> 32));
    result = 31 * result + (int) (orgNo ^ (orgNo >>> 32));
    result = 31 * result + (int) (custNo ^ (custNo >>> 32));
    result = 31 * result + (int) (termNo ^ (termNo >>> 32));
    result = 31 * result + (ddDate != null ? ddDate.hashCode() : 0);
    result = 31 * result + (rate != null ? rate.hashCode() : 0);
    result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (int) (ddNum ^ (ddNum >>> 32));
    result = 31 * result + (ddPrin != null ? ddPrin.hashCode() : 0);
    result = 31 * result + (chdInterest != null ? chdInterest.hashCode() : 0);
    result = 31 * result + (paidInterest != null ? paidInterest.hashCode() : 0);
    result = 31 * result + (int) (status ^ (status >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
