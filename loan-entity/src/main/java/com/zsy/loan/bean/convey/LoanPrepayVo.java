package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提前还款请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanPrepayVo {

  private Long id;

//  @ApiModelProperty( value = "用户编号")
//  @NotNull(message = "[用户编号]不能为空！")
//  private Long userNo;
//
//  @ApiModelProperty( value = "账户名称")
//  @NotNull(message = "[账户名称]不能为空！")
//  private String name;
//
//  @ApiModelProperty( value = "可用余额")
//  private BigDecimal availableBalance;
//
//  @ApiModelProperty( value = "冻结余额")
//  private BigDecimal freezeBalance;
//
//  @ApiModelProperty( value = "账户类型")
//  @NotNull(message = "[账户类型]不能为空！")
//  private Long acctType;
//
//  @ApiModelProperty( value = "余额性质")
//  @NotNull(message = "[余额性质]不能为空！")
//  private Long balanceType;
//
//  @ApiModelProperty( value = "账户状态")
//  @NotNull(message = "[账户状态]不能为空！")
//  private Long status;

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
