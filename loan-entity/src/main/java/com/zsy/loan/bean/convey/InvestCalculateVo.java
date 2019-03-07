package com.zsy.loan.bean.convey;

import com.zsy.loan.bean.entity.biz.TBizInvestPlan;
import com.zsy.loan.bean.entity.biz.TBizProductInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.functors.ConstantFactory;

/**
 * 融资请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestCalculateVo {


  @ApiModelProperty(value = "融资编号")
  private Long id;

  @NotNull(message = "[公司编号]不能为空！")
  @ApiModelProperty(value = "公司编号")
  private Integer orgNo;

  @NotNull(message = "[客户编号]不能为空！")
  @ApiModelProperty(value = "客户编号")
  private Long userNo;

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


  @ApiModelProperty(value = "应收利息累计")
  private BigDecimal totSchdInterest;


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

  @ApiModelProperty(value = "日利息",hidden=true)
  private BigDecimal dayRate;
  @ApiModelProperty(value = "月利息",hidden=true)
  private BigDecimal monthRate;

  @ApiModelProperty(value = "展期日利息",hidden=true)
  private BigDecimal delayDayRate;
  @ApiModelProperty(value = "展期月利息",hidden=true)
  private BigDecimal delayMonthRate;

  @ApiModelProperty(value = "相差天数",hidden=true)
  private int day;
  @ApiModelProperty(value = "相差月数",hidden=true)
  private int month;

  @ApiModelProperty(value = "还款计划",hidden=true)
  private List<TBizInvestPlan> planList;

  @ApiModelProperty(value = "计算详情",hidden=true)
  private String resultMsg;

  private BigDecimal penMonthRate;
  @ApiModelProperty(value = "当前展期期数")
  private Long currentExtensionNo;

  private String orgName;

  private String productName;

  private String custName;

  private String inAcctName;

  private String statusName;

}
