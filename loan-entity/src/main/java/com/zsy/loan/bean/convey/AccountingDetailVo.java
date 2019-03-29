package com.zsy.loan.bean.convey;

import com.zsy.loan.bean.enumeration.BizTypeEnum.BalDirEnum;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记账明细bean
 *
 * @Author zhangxh
 * @Date 2019-03-28  16:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountingDetailVo implements Serializable{


  private static final long serialVersionUID = 6226733035919673733L;

  @ApiModelProperty(value = "资金类型")
  private Long amtType;

  @ApiModelProperty(value = "账户")
  private Long acctNo;

  @ApiModelProperty(value = "账户类型")
  private Long acctType;

  @ApiModelProperty(value = "客户")
  private Long custNo;

  @ApiModelProperty(value = "发生方向")
  private String balDir;

  @ApiModelProperty(value = "金额")
  private BigDecimal amt;

  /**
   * 根据资金方向取得金额
   */
  public double getAmtByBalDirToDouble() {
    return this.getBalDir().equals(BalDirEnum.ADD.getValue()) ? this.getAmt().doubleValue() : this.getAmt().negate().doubleValue();
  }

  public BigDecimal getAmtByBalDir() {
    return this.getBalDir().equals(BalDirEnum.ADD.getValue()) ? this.getAmt() : this.getAmt().negate();
  }

}
