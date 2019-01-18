package com.zsy.loan.bean.entity.biz;

import java.math.BigDecimal;
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
 * 产品代码表
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_biz_product_info", schema = "loan", catalog = "")
public class TBizProductInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Basic
  @Column(name = "org_no")
  private long orgNo;

  @Basic
  @Column(name = "product_name")
  private String productName;

  @Basic
  @Column(name = "rate")
  private BigDecimal rate;

  @Basic
  @Column(name = "service_fee_scale")
  private BigDecimal serviceFeeScale;

  @Basic
  @Column(name = "service_fee_type")
  private Long serviceFeeType;

  @Basic
  @Column(name = "pen_rate")
  private BigDecimal penRate;

  @Basic
  @Column(name = "is_pen")
  private Long isPen;

  @Basic
  @Column(name = "pen_number")
  private Long penNumber;

  @Basic
  @Column(name = "repay_type")
  private long repayType;

  @Basic
  @Column(name = "loan_type")
  private long loanType;

  @Basic
  @Column(name = "cycle_interval")
  private long cycleInterval;

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

    TBizProductInfo that = (TBizProductInfo) o;

    if (id != that.id) {
      return false;
    }
    if (orgNo != that.orgNo) {
      return false;
    }
    if (repayType != that.repayType) {
      return false;
    }
    if (loanType != that.loanType) {
      return false;
    }
    if (cycleInterval != that.cycleInterval) {
      return false;
    }
    if (operator != that.operator) {
      return false;
    }
    if (productName != null ? !productName.equals(that.productName) : that.productName != null) {
      return false;
    }
    if (rate != null ? !rate.equals(that.rate) : that.rate != null) {
      return false;
    }
    if (serviceFeeScale != null ? !serviceFeeScale.equals(that.serviceFeeScale)
        : that.serviceFeeScale != null) {
      return false;
    }
    if (serviceFeeType != null ? !serviceFeeType.equals(that.serviceFeeType)
        : that.serviceFeeType != null) {
      return false;
    }
    if (penRate != null ? !penRate.equals(that.penRate) : that.penRate != null) {
      return false;
    }
    if (isPen != null ? !isPen.equals(that.isPen) : that.isPen != null) {
      return false;
    }
    if (penNumber != null ? !penNumber.equals(that.penNumber) : that.penNumber != null) {
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
    result = 31 * result + (productName != null ? productName.hashCode() : 0);
    result = 31 * result + (rate != null ? rate.hashCode() : 0);
    result = 31 * result + (serviceFeeScale != null ? serviceFeeScale.hashCode() : 0);
    result = 31 * result + (serviceFeeType != null ? serviceFeeType.hashCode() : 0);
    result = 31 * result + (penRate != null ? penRate.hashCode() : 0);
    result = 31 * result + (isPen != null ? isPen.hashCode() : 0);
    result = 31 * result + (penNumber != null ? penNumber.hashCode() : 0);
    result = 31 * result + (int) (repayType ^ (repayType >>> 32));
    result = 31 * result + (int) (loanType ^ (loanType >>> 32));
    result = 31 * result + (int) (cycleInterval ^ (cycleInterval >>> 32));
    result = 31 * result + (int) (operator ^ (operator >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
