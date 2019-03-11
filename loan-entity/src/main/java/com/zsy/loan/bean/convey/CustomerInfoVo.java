package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoVo implements Serializable {

  private static final long serialVersionUID = -2725687370299941691L;
  private Long id;

  @ApiModelProperty( value = "证件号码")
  @NotNull(message = "[证件号码]不能为空！")
  private String certNo;

  @ApiModelProperty( value = "证件类型")
  @NotNull(message = "[证件类型]不能为空！")
  private long certType;

  @ApiModelProperty( value = "客户姓名")
  @NotNull(message = "[客户姓名]不能为空！")
  private String name;

  @ApiModelProperty( value = "性别")
  @NotNull(message = "[性别]不能为空！")
  private long sex;

  @ApiModelProperty( value = "手机号")
  private String mobile;

  @ApiModelProperty( value = "电话")
  private String phone;

  @ApiModelProperty( value = "电子邮箱")
  @NotNull(message = "[电子邮箱]不能为空！")
  private String email;

  @ApiModelProperty( value = "客户类型")
  @NotNull(message = "[客户类型]不能为空！")
  private Long type;

  @ApiModelProperty( value = "客户状态")
  @NotNull(message = "[客户状态]不能为空！")
  private Long status;

  @ApiModelProperty( value = "创建人")
  private Long createBy;

  @ApiModelProperty( value = "修改人")
  private Long modifiedBy;

  @ApiModelProperty( value = "创建时间")
  private Timestamp createAt;

  @ApiModelProperty( value = "修改时间")
  private Timestamp updateAt;

  @ApiModelProperty( value = "备注")
  private String remark;

}
