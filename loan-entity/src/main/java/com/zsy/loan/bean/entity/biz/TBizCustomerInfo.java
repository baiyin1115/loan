package com.zsy.loan.bean.entity.biz;

import java.io.Serializable;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_biz_customer_info")
public class TBizCustomerInfo implements Serializable {

  private static final long serialVersionUID = -4126943838386471353L;

  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;


  @Column(name = "cert_no")
  private String certNo;


  @Column(name = "cert_type")
  private Long certType;


  @Column(name = "name")
  private String name;


  @Column(name = "sex")
  private Long sex;


  @Column(name = "mobile")
  private String mobile;


  @Column(name = "phone")
  private String phone;


  @Column(name = "email")
  private String email;

  @Column(name = "type")
  private Long type;

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
  private String typeName;

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

    TBizCustomerInfo that = (TBizCustomerInfo) o;

    if (id != that.id) {
      return false;
    }
    if (certType != that.certType) {
      return false;
    }
    if (sex != that.sex) {
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
    result = 31 * result + (int) (sex ^ (sex >>> 32));
    result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
    result = 31 * result + (phone != null ? phone.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
    result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
    return result;
  }
}
