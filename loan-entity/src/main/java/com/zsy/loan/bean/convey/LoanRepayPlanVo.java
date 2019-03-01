package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 还款计划请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRepayPlanVo {

  private Long id;

  @ApiModelProperty(value = "借据编号 ")
  private Long loanNo;


  @ApiModelProperty(value = "公司编号 ")
  private Long orgNo;


  @ApiModelProperty(value = "产品编号")
  private Long productNo;


  @ApiModelProperty(value = "客户编号")
  private Long custNo;


  @ApiModelProperty(value = "业务日期")
  private Date acctDate;


  @ApiModelProperty(value = "期数")
  private Long termNo;


  @ApiModelProperty(value = "利率")
  private BigDecimal rate;


  @ApiModelProperty(value = "本期开始日期")
  private Date beginDate;


  @ApiModelProperty(value = "本期结束日期")
  private Date endDate;


  @ApiModelProperty(value = "计息天数")
  private Long ddNum;


  @ApiModelProperty(value = "还款日期")
  private Date ddDate;


  @ApiModelProperty(value = "还款账户")
  private String externalAcct;


  @ApiModelProperty(value = "入账账户")
  private Long inAcctNo;


  @ApiModelProperty(value = "本期应还本金")
  private BigDecimal ctdPrin;


  @ApiModelProperty(value = "本期应还利息")
  private BigDecimal ctdInterest;


  @ApiModelProperty(value = "本期应收服务费")
  private BigDecimal ctdServFee;


  @ApiModelProperty(value = "本期应收罚息")
  private BigDecimal ctdPen;


  @ApiModelProperty(value = "本期已还本金")
  private BigDecimal paidPrin;


  @ApiModelProperty(value = "本期已还利息")
  private BigDecimal paidInterest;


  @ApiModelProperty(value = "本期已收服务费")
  private BigDecimal paidServFee;


  @ApiModelProperty(value = "本期已收罚息")
  private BigDecimal paidPen;


  @ApiModelProperty(value = "本期减免")
  private BigDecimal wavAmt;


  @ApiModelProperty(value = "还款状态")
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

  @ApiModelProperty(value = "本金")
  @NotNull(message = "[本金]不能为空！")
  private BigDecimal currentPrin;

  @ApiModelProperty(value = "利息")
  @NotNull(message = "[利息]不能为空！")
  private BigDecimal currentInterest;

  @ApiModelProperty(value = "罚息")
  @NotNull(message = "[罚息]不能为空！")
  private BigDecimal currentPen;

  @ApiModelProperty(value = "服务费")
  @NotNull(message = "[服务费]不能为空！")
  private BigDecimal currentServFee;

  @ApiModelProperty(value = "减免")
  @NotNull(message = "[减免]不能为空！")
  private BigDecimal currentWav;

}
