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
 * 交易流水请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcctRecordVo implements Serializable {


  private static final long serialVersionUID = 4274965384268027384L;

  @ApiModelProperty(value = "流水编号")
  private Long id;

  @ApiModelProperty(value = "组号")
  private Long groupNo;

  @ApiModelProperty(value = "公司编号")
  private Integer orgNo;

  @ApiModelProperty(value = "凭证编号")
  private Integer voucherNo;

  @ApiModelProperty(value = "账户")
  private Long acctNo;

  @ApiModelProperty(value = "业务类型")
  private Long type;

  @ApiModelProperty(value = "资金类型")
  private Long amtType;

  @ApiModelProperty(value = "业务日期")
  private Date acctDate;

  @ApiModelProperty(value = "发生金额")
  private BigDecimal amt;

  @ApiModelProperty(value = "发生方向")
  private Long balDir;

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

}
