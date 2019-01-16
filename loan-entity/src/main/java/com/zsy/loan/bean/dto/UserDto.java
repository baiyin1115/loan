package com.zsy.loan.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户传输bean
 *
 * @author stylefeng
 * @Date 2017/5/5 22:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private Integer id;
  private String account;
  private String password;
  private String salt;
  private String name;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthday;
  private Integer sex;
  private String email;
  private String phone;
  private String roleid;
  private String deptid;
  private Long eduorgid;
  private Integer status;
  private Date createtime;
  private Integer version;
  private String avatar;
}
