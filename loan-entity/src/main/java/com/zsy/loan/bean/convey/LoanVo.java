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
 * 借据请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanVo {

  private Long id;

  @NotNull(message = "[公司编号]不能为空！")
  @ApiModelProperty(value = "公司编号 ")
  private Long orgNo;

  @NotNull(message = "[产品编号]不能为空！")
  @ApiModelProperty(value = "产品编号")
  private Long productNo;

  @NotNull(message = "[客户编号]不能为空！")
  @ApiModelProperty(value = "客户编号")
  private Long custNo;

  @ApiModelProperty(value = "原始合同编号")
  private String contrNo;

  @NotNull(message = "[贷款类型]不能为空！")
  @ApiModelProperty(value = "贷款类型")
  private Long loanType;

  @NotNull(message = "[业务日期]不能为空！")
  @ApiModelProperty(value = "业务日期")
  private Date acctDate;

  @NotNull(message = "[借款开始日期]不能为空！")
  @ApiModelProperty(value = "借款开始日期")
  private Date beginDate;

  @NotNull(message = "[借款结束日期]不能为空！")
  @ApiModelProperty(value = "借款结束日期")
  private Date endDate;

  @NotNull(message = "[本金]不能为空！")
  @ApiModelProperty(value = "本金")
  private BigDecimal prin;

  @NotNull(message = "[利率]不能为空！")
  @DecimalMax(value = "1.00", message = "[利率]不能超过1.00")
  @ApiModelProperty(value = "利率")
  private BigDecimal rate;

  @NotNull(message = "[应收利息]不能为空！")
  @ApiModelProperty(value = "应收利息")
  private BigDecimal receiveInterest;

  @NotNull(message = "[产品还款方式]不能为空！")
  @ApiModelProperty(value = "产品还款方式")
  private Long repayType;

  @NotNull(message = "[期数]不能为空！")
  @ApiModelProperty(value = "期数")
  private Long termNo;

  @ApiModelProperty(value = "放款日期")
  private Date lendingDate;


  @ApiModelProperty(value = "放款金额")
  private BigDecimal lendingAmt;

  @NotNull(message = "[放款账户]不能为空！")
  @ApiModelProperty(value = "放款账户")
  private Long lendingAcct;

  @ApiModelProperty(value = "收款账户")
  private String externalAcct;

  @ApiModelProperty( value = "服务费比例")
  @NotNull(message = "[服务费比例]不能为空！")
  @DecimalMax(value = "1.00", message = "[服务费比例]不能超过1.00")
  private BigDecimal serviceFeeScale;

  @NotNull(message = "[服务费]不能为空！")
  @ApiModelProperty(value = "服务费")
  private BigDecimal serviceFee;

  @NotNull(message = "[服务费收取方式]不能为空！")
  @ApiModelProperty(value = "服务费收取方式")
  private Long serviceFeeType;

  @ApiModelProperty(value = "约定还款日")
  private Long ddDate;

  @NotNull(message = "[是否罚息]不能为空！")
  @ApiModelProperty(value = "是否罚息")
  private Long isPen;

  @ApiModelProperty(value = "罚息利率")
  @NotNull(message = "[罚息利率]不能为空！")
  @DecimalMax(value = "1.00", message = "[罚息利率]不能超过1.00")
  private BigDecimal penRate;

  @NotNull(message = "[罚息基数]不能为空！")
  @ApiModelProperty(value = "罚息基数")
  private Long penNumber;

  @ApiModelProperty(value = "展期期数")
  private Long extensionNo;

  @ApiModelProperty(value = "展期利息")
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

}
