package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回款计划请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestPlanVo implements Serializable {


  private static final long serialVersionUID = -5512425609387061774L;
  @ApiModelProperty(value = "计划编号")
  private Long id;


  @ApiModelProperty(value = "融资编号")
  private Long investNo;


  @ApiModelProperty(value = "公司编号")
  private Integer orgNo;


  @ApiModelProperty(value = "客户编号")
  private Long userNo;


  @ApiModelProperty(value = "期数")
  private Long termNo;


  @ApiModelProperty(value = "计息日期")
  private Date ddDate;


  @ApiModelProperty(value = "利率")
  private BigDecimal rate;


  @ApiModelProperty(value = "本期开始日期")
  private Date beginDate;


  @ApiModelProperty(value = "本期结束日期")
  private Date endDate;


  @ApiModelProperty(value = "计息天数")
  private Long ddNum;


  @ApiModelProperty(value = "本期计息本金")
  private BigDecimal ddPrin;


  @ApiModelProperty(value = "本期应收利息")
  private BigDecimal chdInterest;


  @ApiModelProperty(value = "本期已回利息")
  private BigDecimal paidInterest;


  @ApiModelProperty(value = "回款状态")
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


}
