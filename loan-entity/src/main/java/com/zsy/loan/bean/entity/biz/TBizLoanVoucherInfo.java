package com.zsy.loan.bean.entity.biz;

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
 * 借据凭证信息
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_biz_loan_voucher_info", schema = "loan", catalog = "")
public class TBizLoanVoucherInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Basic
  @Column(name = "loan_no")
  private long loanNo;

  @Basic
  @Column(name = "type")
  private long type;

  @Basic
  @Column(name = "path")
  private String path;

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

    TBizLoanVoucherInfo that = (TBizLoanVoucherInfo) o;

    if (id != that.id) {
      return false;
    }
    if (loanNo != that.loanNo) {
      return false;
    }
    if (type != that.type) {
      return false;
    }
    if (operator != that.operator) {
      return false;
    }
    if (path != null ? !path.equals(that.path) : that.path != null) {
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
    result = 31 * result + (int) (type ^ (type >>> 32));
    result = 31 * result + (path != null ? path.hashCode() : 0);
    result = 31 * result + (int) (operator ^ (operator >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
