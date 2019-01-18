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
 * 账户表
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_biz_acct", schema = "loan", catalog = "")
public class TBizAcct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Basic
  @Column(name = "user_no", nullable = false)
  private long userNo;

  @Basic
  @Column(name = "available_balance", nullable = false, precision = 2)
  private BigDecimal availableBalance;

  @Basic
  @Column(name = "freeze_balance", nullable = false, precision = 2)
  private BigDecimal freezeBalance;

  @Basic
  @Column(name = "acct_type", nullable = false)
  private long acctType;

  @Basic
  @Column(name = "balance_type", nullable = true)
  private Long balanceType;

  @Basic
  @Column(name = "operator", nullable = false)
  private long operator;

  @Basic
  @Column(name = "create_at", nullable = true)
  private Timestamp createAt;

  @Basic
  @Column(name = "update_at", nullable = true)
  private Timestamp updateAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TBizAcct tBizAcct = (TBizAcct) o;

    if (id != tBizAcct.id) {
      return false;
    }
    if (userNo != tBizAcct.userNo) {
      return false;
    }
    if (acctType != tBizAcct.acctType) {
      return false;
    }
    if (operator != tBizAcct.operator) {
      return false;
    }
    if (availableBalance != null ? !availableBalance.equals(tBizAcct.availableBalance)
        : tBizAcct.availableBalance != null) {
      return false;
    }
    if (freezeBalance != null ? !freezeBalance.equals(tBizAcct.freezeBalance)
        : tBizAcct.freezeBalance != null) {
      return false;
    }
    if (balanceType != null ? !balanceType.equals(tBizAcct.balanceType)
        : tBizAcct.balanceType != null) {
      return false;
    }
    if (createAt != null ? !createAt.equals(tBizAcct.createAt) : tBizAcct.createAt != null) {
      return false;
    }
    if (updateAt != null ? !updateAt.equals(tBizAcct.updateAt) : tBizAcct.updateAt != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (userNo ^ (userNo >>> 32));
    result = 31 * result + (availableBalance != null ? availableBalance.hashCode() : 0);
    result = 31 * result + (freezeBalance != null ? freezeBalance.hashCode() : 0);
    result = 31 * result + (int) (acctType ^ (acctType >>> 32));
    result = 31 * result + (balanceType != null ? balanceType.hashCode() : 0);
    result = 31 * result + (int) (operator ^ (operator >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
