package com.zsy.loan.bean.entity.biz;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.apache.commons.collections.functors.ConstantFactory;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_biz_acct")
public class TBizAcct implements Serializable {

  private static final long serialVersionUID = 1430225421250896758L;

  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;


  @Column(name = "cust_no", nullable = false)
  private Long custNo;

  @Column(name = "name", nullable = false)
  private String name;
  
  @Column(name = "available_balance", nullable = false, precision = 2)
  private BigDecimal availableBalance;

  @Column(name = "freeze_balance", nullable = false, precision = 2)
  private BigDecimal freezeBalance;


  @Column(name = "acct_type", nullable = false)
  private Long acctType;


  @Column(name = "balance_type")
  private Long balanceType;


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

  @Transient
  private String acctTypeName;

  @Transient
  private String statusName;

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
    if (custNo != tBizAcct.custNo) {
      return false;
    }
    if (acctType != tBizAcct.acctType) {
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
    result = 31 * result + (int) (custNo ^ (custNo >>> 32));
    result = 31 * result + (availableBalance != null ? availableBalance.hashCode() : 0);
    result = 31 * result + (freezeBalance != null ? freezeBalance.hashCode() : 0);
    result = 31 * result + (int) (acctType ^ (acctType >>> 32));
    result = 31 * result + (balanceType != null ? balanceType.hashCode() : 0);
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
