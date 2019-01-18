package com.zsy.loan.bean.entity.biz;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "t_biz_invest_plan", schema = "loan", catalog = "")
public class TBizInvestPlan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Basic
  @Column(name = "invest_no")
  private long investNo;

  @Basic
  @Column(name = "org_no")
  private long orgNo;

  @Basic
  @Column(name = "user_no")
  private long userNo;

  @Basic
  @Column(name = "term_no")
  private long termNo;

  @Basic
  @Column(name = "dd_date")
  private Date ddDate;

  @Basic
  @Column(name = "rate")
  private BigDecimal rate;

  @Basic
  @Column(name = "begin_date")
  private Date beginDate;

  @Basic
  @Column(name = "end_date")
  private Date endDate;

  @Basic
  @Column(name = "dd_num")
  private long ddNum;

  @Basic
  @Column(name = "dd_prin")
  private BigDecimal ddPrin;

  @Basic
  @Column(name = "chd_bigint")
  private BigDecimal chdBigint;

  @Basic
  @Column(name = "paid_bigint")
  private BigDecimal paidBigint;

  @Basic
  @Column(name = "status")
  private long status;

  @Basic
  @Column(name = "operator")
  private long operator;

  @Basic
  @Column(name = "create_at")
  private Timestamp createAt;

  @Basic
  @Column(name = "update_at")
  private Timestamp updateAt;


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
    if (userNo != that.userNo) {
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
    if (operator != that.operator) {
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
    if (chdBigint != null ? !chdBigint.equals(that.chdBigint) : that.chdBigint != null) {
      return false;
    }
    if (paidBigint != null ? !paidBigint.equals(that.paidBigint) : that.paidBigint != null) {
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
    result = 31 * result + (int) (userNo ^ (userNo >>> 32));
    result = 31 * result + (int) (termNo ^ (termNo >>> 32));
    result = 31 * result + (ddDate != null ? ddDate.hashCode() : 0);
    result = 31 * result + (rate != null ? rate.hashCode() : 0);
    result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (int) (ddNum ^ (ddNum >>> 32));
    result = 31 * result + (ddPrin != null ? ddPrin.hashCode() : 0);
    result = 31 * result + (chdBigint != null ? chdBigint.hashCode() : 0);
    result = 31 * result + (paidBigint != null ? paidBigint.hashCode() : 0);
    result = 31 * result + (int) (status ^ (status >>> 32));
    result = 31 * result + (int) (operator ^ (operator >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}