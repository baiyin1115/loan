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
 * 客户信息表
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_biz_customer_info", schema = "loan", catalog = "")
public class TBizCustomerInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Basic
  @Column(name = "cert_no")
  private String certNo;

  @Basic
  @Column(name = "cert_type")
  private long certType;

  @Basic
  @Column(name = "name")
  private String name;

  @Basic
  @Column(name = "gender")
  private long gender;

  @Basic
  @Column(name = "mobile")
  private String mobile;

  @Basic
  @Column(name = "phone")
  private String phone;

  @Basic
  @Column(name = "email")
  private String email;

  @Basic
  @Column(name = "status")
  private Long status;

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

    TBizCustomerInfo that = (TBizCustomerInfo) o;

    if (id != that.id) {
      return false;
    }
    if (certType != that.certType) {
      return false;
    }
    if (gender != that.gender) {
      return false;
    }
    if (operator != that.operator) {
      return false;
    }
    if (certNo != null ? !certNo.equals(that.certNo) : that.certNo != null) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (mobile != null ? !mobile.equals(that.mobile) : that.mobile != null) {
      return false;
    }
    if (phone != null ? !phone.equals(that.phone) : that.phone != null) {
      return false;
    }
    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    if (status != null ? !status.equals(that.status) : that.status != null) {
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
    result = 31 * result + (certNo != null ? certNo.hashCode() : 0);
    result = 31 * result + (int) (certType ^ (certType >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (int) (gender ^ (gender >>> 32));
    result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
    result = 31 * result + (phone != null ? phone.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (int) (operator ^ (operator >>> 32));
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
