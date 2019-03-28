package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记账bean
 *
 * @Author zhangxh
 * @Date 2019-03-28  16:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountingMainVo implements Serializable {

  private static final long serialVersionUID = 692866527534764544L;

  @ApiModelProperty(value = "公司编号")
  private Integer orgNo;

  @ApiModelProperty(value = "业务类型")
  private Long type;

  @ApiModelProperty(value = "业务日期")
  private Date acctDate;

  @ApiModelProperty(value = "组号")
  private Long groupNo;

  @ApiModelProperty(value = "凭证编号--借据、融资、转账、收支")
  private Long voucherNo;

  @ApiModelProperty(value = "凭证计划编号--还款、回款")
  private Long voucherPlanNo;

  @ApiModelProperty(value = "明细")
  private List<AccountingDetailVo> detail;

}
