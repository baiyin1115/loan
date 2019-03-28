package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferInfoVo implements Serializable {


  private static final long serialVersionUID = 4274965384268027384L;

  @ApiModelProperty(value = "转账编号")
  private Long id;

  @NotNull(message = "[公司编号]不能为空！")
  @ApiModelProperty(value = "公司编号")
  private Integer orgNo;

  @ApiModelProperty(value = "入账账户")
  private Long inAcctNo;

  @ApiModelProperty(value = "出账账户")
  private Long outAcctNo;

  @NotNull(message = "[金额]不能为空！")
  @ApiModelProperty(value = "金额")
  private BigDecimal amt;

  @ApiModelProperty(value = "业务日期")
  private Date acctDate;

  @NotNull(message = "[用途]不能为空！")
  @ApiModelProperty(value = "用途")
  private Long type;

  @ApiModelProperty(value = "状态")
  private Long status;

  @ApiModelProperty(value = "操作员")
  private Long createBy;

  @ApiModelProperty(value = "修改操作员")
  private Long modifiedBy;

  @ApiModelProperty(value = "创建时间")
  private Timestamp createAt;

  @ApiModelProperty(value = "更新时间")
  private Timestamp updateAt;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "查询开始日期")
  private String queryBeginDate;

  @ApiModelProperty(value = "查询结束日期")
  private String queryEndDate;

  @ApiModelProperty(value = "入账账户名称", hidden = true)
  private String inAcctName;

  @ApiModelProperty(value = "出账账户名称", hidden = true)
  private String outAcctName;


  @ApiModelProperty(value = "入账账户类型", hidden = true)
  private Long inAcctType;

  @ApiModelProperty(value = "出账账户类型", hidden = true)
  private Long outAcctType;


}
