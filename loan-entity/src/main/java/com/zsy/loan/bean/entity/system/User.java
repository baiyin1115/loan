package com.zsy.loan.bean.entity.system;

import java.io.Serializable;
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
public class User implements Serializable {

  private static final long serialVersionUID = 8184666206226593170L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;


  @Column(name = "avatar")
  private String avatar;


  @Column(name = "account")
  private String account;


  @Column(name = "password")
  private String password;


  @Column(name = "salt")
  private String salt;


  @Column(name = "name")
  private String name;


  @Column(name = "birthday")
  private Date birthday;


  @Column(name = "sex")
  private Integer sex;


  @Column(name = "email")
  private String email;


  @Column(name = "phone")
  private String phone;


  @Column(name = "roleid")
  private String roleid;


  @Column(name = "deptid")
  private String deptid;


  @Column(name = "status")
  private Integer status;


  @Column(name = "create_at")
  private Date createtime;


  @Column(name = "update_at")
  private Date updateAt;


  @Column(name = "remark")
  private Integer remark;


  @Column(name = "operator")
  private Integer operator;


  @Column(name = "version")
  private Integer version;

}
