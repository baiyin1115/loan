package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
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
 * 借据违约
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanBreachVo {

  private Long id;

  @ApiModelProperty(value = "公司编号 ")
  private Integer orgNo;

  @ApiModelProperty(value = "产品编号")
  private Long productNo;

  @ApiModelProperty(value = "客户编号")
  private Long custNo;

  @ApiModelProperty(value = "原始合同编号")
  private String contrNo;

  @ApiModelProperty(value = "贷款类型")
  private Long loanType;

  @ApiModelProperty(value = "业务日期")
  private Date acctDate;

  @ApiModelProperty(value = "借款开始日期")
  private Date beginDate;

  @ApiModelProperty(value = "借款结束日期")
  private Date endDate;

  @ApiModelProperty(value = "本金")
  private BigDecimal prin;

  @ApiModelProperty(value = "利率")
  private BigDecimal rate;

  @ApiModelProperty(value = "应收利息")
  private BigDecimal receiveInterest;

  @ApiModelProperty(value = "产品还款方式")
  private Long repayType;

  @ApiModelProperty(value = "期数")
  private Long termNo;

  @ApiModelProperty(value = "放款日期")
  private Date lendingDate;


  @ApiModelProperty(value = "放款金额")
  private BigDecimal lendingAmt;

  @ApiModelProperty(value = "放款账户")
  private Long lendingAcct;

  @ApiModelProperty(value = "收款账户")
  private String externalAcct;

  @ApiModelProperty( value = "服务费比例")
  private BigDecimal serviceFeeScale;

  @ApiModelProperty(value = "服务费")
  private BigDecimal serviceFee;

  @ApiModelProperty(value = "服务费收取方式")
  private Long serviceFeeType;

  @ApiModelProperty(value = "约定还款日")
  private Long ddDate;

  @ApiModelProperty(value = "是否罚息")
  private Long isPen;

  @ApiModelProperty(value = "罚息利率")
  private BigDecimal penRate;

  @ApiModelProperty(value = "罚息基数")
  private Long penNumber;

  @ApiModelProperty(value = "展期期数")
  private Long extensionNo;

  @ApiModelProperty(value = "展期利率")
  private BigDecimal extensionRate;

  @ApiModelProperty(value = "应还本金")
  private BigDecimal schdPrin;

  @ApiModelProperty(value = "应还利息")
  private BigDecimal schdInterest;

  @ApiModelProperty(value = "应收服务费")
  private BigDecimal schdServFee;

  @ApiModelProperty(value = "逾期罚息累计")
  private BigDecimal schdPen;

  @ApiModelProperty(value = "已还本金累计")
  private BigDecimal totPaidPrin;

  @ApiModelProperty(value = "已还利息累计")
  private BigDecimal totPaidInterest;

  @ApiModelProperty(value = "已收服务费累计")
  private BigDecimal totPaidServFee;

  @ApiModelProperty(value = "已还罚息累计")
  private BigDecimal totPaidPen;

  @ApiModelProperty(value = "减免金额累计")
  private BigDecimal totWavAmt;

  @ApiModelProperty(value = "借据状态")
  private Long status;

  @ApiModelProperty(value = "创建人")
  private Long createBy;

  @ApiModelProperty(value = "修改人")
  private Long modifiedBy;

  @ApiModelProperty(value = "创建时间")
  private Timestamp createAt;

  @ApiModelProperty(value = "修改时间")
  private Timestamp upDateAt;

  @ApiModelProperty(value = "备注")
  private String remark;

  @NotNull(message = "[当前用户录入代偿本金]不能为空！")
  @ApiModelProperty(value = "当前用户录入代偿本金")
  private BigDecimal currentBreachPrin;

  @NotNull(message = "[当前用户录入代偿费用]不能为空！")
  @ApiModelProperty(value = "当前用户录入代偿费用=利息+服务费+罚息")
  private BigDecimal currentBreachFee;

  @NotNull(message = "[代偿账户]不能为空！")
  @ApiModelProperty(value = "代偿账户")
  private String compensationAcct;

}
