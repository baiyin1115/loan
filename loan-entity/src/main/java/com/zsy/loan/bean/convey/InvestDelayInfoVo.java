package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 融资延期请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestDelayInfoVo implements Serializable {


  private static final long serialVersionUID = -5536799045685150901L;
  @ApiModelProperty(value = "融资编号")
  private Long id;

  @NotNull(message = "[公司编号]不能为空！")
  @ApiModelProperty(value = "公司编号")
  private Integer orgNo;

  @NotNull(message = "[客户编号]不能为空！")
  @ApiModelProperty(value = "客户编号")
  private Long custNo;

  @NotNull(message = "[产品类型]不能为空！")
  @ApiModelProperty(value = "产品类型")
  private Integer investType;

  @NotNull(message = "[入账账户]不能为空！")
  @ApiModelProperty(value = "入账账户")
  private Long inAcctNo;


  @ApiModelProperty(value = "投资人出款账户")
  private String externalAcct;

  @NotNull(message = "[本金]不能为空！")
  @ApiModelProperty(value = "本金")
  private BigDecimal prin;


  @ApiModelProperty(value = "业务日期")
  private Date acctDate;

  @NotNull(message = "[起息开始日期]不能为空！")
  @ApiModelProperty(value = "起息开始日期")
  private Date beginDate;

  @NotNull(message = "[起息结束日期]不能为空！")
  @ApiModelProperty(value = "起息结束日期")
  private Date endDate;

  @NotNull(message = "[利率]不能为空！")
  @DecimalMax(value = "1.00", message = "[利率]不能超过1.00")
  @ApiModelProperty(value = "利率")
  private BigDecimal rate;

  @NotNull(message = "[期数]不能为空！")
  @ApiModelProperty(value = "期数")
  private Long termNo;

  @NotNull(message = "[周期间隔]不能为空！")
  @ApiModelProperty(value = "周期间隔")
  private Long cycleInterval;


  @ApiModelProperty(value = "状态")
  private Long status;

  @NotNull(message = "[计息日]不能为空！")
  @ApiModelProperty(value = "计息日")
  private Long ddDate;


  @ApiModelProperty(value = "延期期数")
  private Long extensionNo;


  @ApiModelProperty(value = "延期利率")
  private BigDecimal extensionRate;


  @ApiModelProperty(value = "应回利息累计")
  private BigDecimal totSchdInterest;


  @ApiModelProperty(value = "计提利息累计") //tot_accrued_interest
  private BigDecimal totAccruedInterest;


  @ApiModelProperty(value = "已提本金累计")
  private BigDecimal totPaidPrin;


  @ApiModelProperty(value = "已提利息累计")
  private BigDecimal totPaidInterest;


  @ApiModelProperty(value = "收益调整金额累计")
  private BigDecimal totWavAmt;


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


  @ApiModelProperty(value = "业务类型",hidden=true)
  private Long bizType;

}
