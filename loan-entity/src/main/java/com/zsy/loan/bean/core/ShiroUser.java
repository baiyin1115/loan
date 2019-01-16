package com.zsy.loan.bean.core;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 *
 * @author fengshuonan
 * @date 2016年12月5日 上午10:26:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiroUser implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;          // 主键ID
  private String account;      // 账号
  private String name;         // 姓名
//  private String deptId;      // 部门id
//private String deptName;        // 部门名称
  private List<Integer> deptList;
  private List<String> deptNameList;
  private List<Integer> roleList; // 角色集
  private List<String> roleNames; // 角色名称集
  private List<String> roleCodes;//角色编码
}
