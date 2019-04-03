package com.zsy.loan.bean.entity.biz;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
public class TBizAcctPopup implements Serializable {

  private static final long serialVersionUID = -6712496907168087009L;

  @Id
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


  @Column(name = "version")
  private Long version;

  @Column(name = "remark")
  private String remark;

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


  @Column(name = "cust_cert_no")
  private String custCertNo;


  @Column(name = "cust_cert_type")
  private Long custCertType;


  @Column(name = "cust_name")
  private String custName;


  @Column(name = "cust_sex")
  private Long custSex;


  @Column(name = "cust_mobile")
  private String custMobile;


  @Column(name = "cust_phone")
  private String custPhone;


  @Column(name = "cust_email")
  private String custEmail;

  @Column(name = "cust_type")
  private Long custType;

  @Column(name = "cust_status")
  private Long custStatus;

  @Transient
  private String acctTypeName;

  @Transient
  private String statusName;

}
