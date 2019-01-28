package com.zsy.loan.bean.request;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原始凭证请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanVoucherRequest {

  private Long id;



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

}
