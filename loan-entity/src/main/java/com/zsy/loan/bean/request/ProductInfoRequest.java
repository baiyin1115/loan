package com.zsy.loan.bean.request;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 产品请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoRequest {

  private Long id;

  @ApiModelProperty(required = true, value = "公司编号")
  @NotNull(message = "[公司编号]不能为空！")
  private Long orgNo;

  @ApiModelProperty(required = true, value = "产品名称")
  @NotNull(message = "[产品名称]不能为空！")
  private String productName;

  @ApiModelProperty(required = true, value = "产品利率")
  @NotNull(message = "[产品利率]不能为空！")
  @DecimalMax(value = "1.00", message = "[产品利率]不能超过1.00")
  private BigDecimal rate;

  @ApiModelProperty(required = true, value = "服务费比例")
  @NotNull(message = "[服务费比例]不能为空！")
  @DecimalMax(value = "1.00", message = "[服务费比例]不能超过1.00")
  private BigDecimal serviceFeeScale;

  @ApiModelProperty(required = true, value = "服务费收取方式")
  @NotNull(message = "[服务费收取方式]不能为空！")
  private Long serviceFeeType;

  @ApiModelProperty(required = true, value = "罚息利率")
  @NotNull(message = "[罚息利率]不能为空！")
  @DecimalMax(value = "1.00", message = "[服务费比例]不能超过1.00")
  private BigDecimal penRate;

  @ApiModelProperty(required = true, value = "是否罚息")
  @NotNull(message = "[是否罚息]不能为空！")
  private Long isPen;

  @ApiModelProperty(required = true, value = "罚息基数")
  @NotNull(message = "[罚息基数]不能为空！")
  private Long penNumber;

  @ApiModelProperty(required = true, value = "产品还款方式")
  @NotNull(message = "[产品还款方式]不能为空！")
  private Long repayType;

  @ApiModelProperty(required = true, value = "贷款类型")
  @NotNull(message = "[贷款类型]不能为空！")
  private Long loanType;

  @ApiModelProperty(required = true, value = "周期间隔")
  @NotNull(message = "[周期间隔]不能为空！")
  @Max(value = 30, message = "[周期间隔]不能超过30天")
  private Long cycleInterval;

  @ApiModelProperty(required = true, value = "创建人")
  private Long createBy;

  @ApiModelProperty(required = true, value = "修改人")
  private Long modifiedBy;

  @ApiModelProperty(required = true, value = "创建时间")
  private Timestamp createAt;

  @ApiModelProperty(required = true, value = "修改时间")
  private Timestamp updateAt;

  @ApiModelProperty(required = true, value = "备注")
  private String remark;

}
