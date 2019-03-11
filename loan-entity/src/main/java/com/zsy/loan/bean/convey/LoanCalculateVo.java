package com.zsy.loan.bean.convey;

import com.zsy.loan.bean.entity.biz.TBizProductInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试算
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculateVo implements Serializable {

  private static final long serialVersionUID = -1166079815427120087L;
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
  @DecimalMax(value = "1.00", message = "[利率]不能超过1.00")
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

  @ApiModelProperty(value = "服务费")
  private BigDecimal serviceFee;

  @ApiModelProperty(value = "服务费比例")
  @DecimalMax(value = "1.00", message = "[服务费比例]不能超过1.00")
  private BigDecimal serviceFeeScale;

  @ApiModelProperty(value = "服务费收取方式")
  private Long serviceFeeType;

  @Max(value = 27, message = "[约定还款日]不能超过27")
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
  @DecimalMax(value = "1.00", message = "[展期利率]不能超过1.00")
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

  @ApiModelProperty(value = "日利息", hidden = true)
  private BigDecimal dayRate;
  @ApiModelProperty(value = "月利息", hidden = true)
  private BigDecimal monthRate;
  @ApiModelProperty(value = "相差天数", hidden = true)
  private int day;
  @ApiModelProperty(value = "相差月数", hidden = true)
  private int month;
  @ApiModelProperty(value = "产品信息", hidden = true)
  private TBizProductInfo product;
  @ApiModelProperty(value = "还款计划", hidden = true)
  private List<TBizRepayPlan> repayPlanList;

  @ApiModelProperty(value = "业务类型", hidden = true)
  private Long bizType;

  @ApiModelProperty(value = "计算详情", hidden = true)
  private String resultMsg;

  @ApiModelProperty(value = "展期日利息", hidden = true)
  private BigDecimal delayDayRate;
  @ApiModelProperty(value = "展期月利息", hidden = true)
  private BigDecimal delayMonthRate;

  @ApiModelProperty(value = "罚息日利息", hidden = true)
  private BigDecimal penDayRate;
  @ApiModelProperty(value = "罚息月利息", hidden = true)
  private BigDecimal penMonthRate;
  @ApiModelProperty(value = "当前展期期数")
  private Long currentExtensionNo;

  @ApiModelProperty(value = "当前期还款计划", hidden = true)
  List<TBizRepayPlan> currentRepayPlans;

  @ApiModelProperty(value = "当前期以前的未还还款计划", hidden = true)
  List<TBizRepayPlan> notPayRecords;

  @ApiModelProperty(value = "当前期以后的还款计划", hidden = true)
  List<TBizRepayPlan> afterPayRecords;

  private String orgName;

  private String productName;

  private String custName;

  private String lendingAcctName;

  private String loanTypeName;

  private String serviceFeeTypeName;

  private String repayTypeName;

  private String isPenName;

  private String penNumberName;

  private String statusName;

  @ApiModelProperty(value = "当前用户录入还款本金")
  private BigDecimal currentRepayPrin;

  @ApiModelProperty(value = "当前用户录入费用=利息+服务费+罚息")
  private BigDecimal currentRepayFee;

  @ApiModelProperty(value = "当前用户录入减免")
  private BigDecimal currentRepayWav;

  @ApiModelProperty(value = "计算提前还清金额")
  private BigDecimal repayAmt;

  @ApiModelProperty(value = "计算提前还款利息")
  private BigDecimal repayInterest;

  @ApiModelProperty(value = "计算提前还款罚息")
  private BigDecimal repayPen;

  @ApiModelProperty(value = "计算提前还款服务费")
  private BigDecimal repayServFee;

  @ApiModelProperty(value = "退回金额")
  private BigDecimal backAmt;

}
