package com.zsy.loan.bean.request;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 放款请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanPutRequest {

  private Long id;

//  @ApiModelProperty(required = true, value = "用户编号")
//  @NotNull(message = "[用户编号]不能为空！")
//  private Long userNo;
//
//  @ApiModelProperty(required = true, value = "账户名称")
//  @NotNull(message = "[账户名称]不能为空！")
//  private String name;
//
//  @ApiModelProperty(required = true, value = "可用余额")
//  private BigDecimal availableBalance;
//
//  @ApiModelProperty(required = true, value = "冻结余额")
//  private BigDecimal freezeBalance;
//
//  @ApiModelProperty(required = true, value = "账户类型")
//  @NotNull(message = "[账户类型]不能为空！")
//  private Long acctType;
//
//  @ApiModelProperty(required = true, value = "余额性质")
//  @NotNull(message = "[余额性质]不能为空！")
//  private Long balanceType;
//
//  @ApiModelProperty(required = true, value = "账户状态")
//  @NotNull(message = "[账户状态]不能为空！")
//  private Long status;

  @ApiModelProperty(required = true, value = "创建人")
  private Long createBy;

  @ApiModelProperty(required = true, value = "修改人")
  private Long modifiedBy;

  @ApiModelProperty(required = true, value = "创建时间")
  private Timestamp createAt;

  @ApiModelProperty(required = true, value = "修改时间")
  private Timestamp updateAt;

  @ApiModelProperty(required = true, value = "备注")
  private String remark;

}
