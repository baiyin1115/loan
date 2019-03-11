package com.zsy.loan.bean.entity.biz;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * 转账凭证表
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
@Table(name = "t_biz_transfer_voucher_info")
public class TBizTransferVoucherInfo implements Serializable {

  private static final long serialVersionUID = 7099828336472811968L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "org_no")
  private Integer orgNo;

  @Column(name = "in_acct_no")
  private long inAcctNo;


  @Column(name = "out_acct_no")
  private long outAcctNo;


  @Column(name = "amt")
  private BigDecimal amt;


  @Column(name = "type")
  private long type;


  @Column(name = "status")
  private long status;


  @Column(name = "operator")
  private long operator;

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

    TBizTransferVoucherInfo that = (TBizTransferVoucherInfo) o;

    if (id != that.id) {
      return false;
    }
    if (inAcctNo != that.inAcctNo) {
      return false;
    }
    if (outAcctNo != that.outAcctNo) {
      return false;
    }
    if (type != that.type) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    if (amt != null ? !amt.equals(that.amt) : that.amt != null) {
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
    result = 31 * result + (int) (inAcctNo ^ (inAcctNo >>> 32));
    result = 31 * result + (int) (outAcctNo ^ (outAcctNo >>> 32));
    result = 31 * result + (amt != null ? amt.hashCode() : 0);
    result = 31 * result + (int) (type ^ (type >>> 32));
    result = 31 * result + (int) (status ^ (status >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
