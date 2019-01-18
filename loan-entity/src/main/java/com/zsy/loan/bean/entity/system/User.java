package com.zsy.loan.bean.entity.system;

import javax.persistence.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_sys_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Basic
  @Column(name = "avatar")
  private String avatar;

  @Basic
  @Column(name = "account")
  private String account;

  @Basic
  @Column(name = "password")
  private String password;

  @Basic
  @Column(name = "salt")
  private String salt;

  @Basic
  @Column(name = "name")
  private String name;

  @Basic
  @Column(name = "birthday")
  private Date birthday;

  @Basic
  @Column(name = "sex")
  private Integer sex;

  @Basic
  @Column(name = "email")
  private String email;

  @Basic
  @Column(name = "phone")
  private String phone;

  @Basic
  @Column(name = "roleid")
  private String roleid;

  @Basic
  @Column(name = "deptid")
  private String deptid;

  @Basic
  @Column(name = "status")
  private Integer status;

  @Basic
  @Column(name = "create_at")
  private Date createtime;

  @Basic
  @Column(name = "update_at")
  private Date updateAt;

  @Basic
  @Column(name = "remark")
  private Integer remark;

  @Basic
  @Column(name = "operator")
  private Integer operator;

  @Basic
  @Column(name = "version")
  private Integer version;

}
