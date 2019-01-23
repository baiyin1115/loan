package com.zsy.loan.bean.entity.biz;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
 * 账户资金流水表
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
@Table(name = "t_biz_acct_record")
public class TBizAcctRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;


  @Column(name = "group_no")
  private long groupNo;


  @Column(name = "org_no")
  private long orgNo;


  @Column(name = "voucher_no")
  private long voucherNo;


  @Column(name = "acct_no")
  private long acctNo;


  @Column(name = "type")
  private long type;


  @Column(name = "amt_type")
  private long amtType;


  @Column(name = "acct_date")
  private Date acctDate;


  @Column(name = "amt")
  private BigDecimal amt;


  @Column(name = "bal_dir")
  private String balDir;


  @Column(name = "status")
  private long status;

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

    TBizAcctRecord that = (TBizAcctRecord) o;

    if (id != that.id) {
      return false;
    }
    if (groupNo != that.groupNo) {
      return false;
    }
    if (orgNo != that.orgNo) {
      return false;
    }
    if (voucherNo != that.voucherNo) {
      return false;
    }
    if (acctNo != that.acctNo) {
      return false;
    }
    if (type != that.type) {
      return false;
    }
    if (amtType != that.amtType) {
      return false;
    }
    if (status != that.status) {
      return false;
    }

    if (acctDate != null ? !acctDate.equals(that.acctDate) : that.acctDate != null) {
      return false;
    }
    if (amt != null ? !amt.equals(that.amt) : that.amt != null) {
      return false;
    }
    if (balDir != null ? !balDir.equals(that.balDir) : that.balDir != null) {
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
    result = 31 * result + (int) (groupNo ^ (groupNo >>> 32));
    result = 31 * result + (int) (orgNo ^ (orgNo >>> 32));
    result = 31 * result + (int) (voucherNo ^ (voucherNo >>> 32));
    result = 31 * result + (int) (acctNo ^ (acctNo >>> 32));
    result = 31 * result + (int) (type ^ (type >>> 32));
    result = 31 * result + (int) (amtType ^ (amtType >>> 32));
    result = 31 * result + (acctDate != null ? acctDate.hashCode() : 0);
    result = 31 * result + (amt != null ? amt.hashCode() : 0);
    result = 31 * result + (balDir != null ? balDir.hashCode() : 0);
    result = 31 * result + (int) (status ^ (status >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
