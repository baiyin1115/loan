package com.zsy.loan.bean.convey;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收支请求
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InOutInfoVo implements Serializable {

  private static final long serialVersionUID = 5914197759400321092L;

  @ApiModelProperty(value = "收支编号")
  private Long id;

  @NotNull(message = "[公司编号]不能为空！")
  @ApiModelProperty(value = "公司编号")
  private Integer orgNo;

  @NotNull(message = "[账户]不能为空！")
  @ApiModelProperty(value = "账户")
  private Long acctNo;

  @ApiModelProperty(value = "外部账户")
  private String externalAcct;

  @ApiModelProperty(value = "业务日期")
  private Date acctDate;

  @NotNull(message = "[金额]不能为空！")
  @ApiModelProperty(value = "金额")
  private BigDecimal amt;

  @NotNull(message = "[用途]不能为空！")
  @ApiModelProperty(value = "用途")
  private Long type;

  @ApiModelProperty(value = "状态")
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

  @ApiModelProperty(value = "查询开始日期")
  private String queryBeginDate;

  @ApiModelProperty(value = "查询结束日期")
  private String queryEndDate;

  @ApiModelProperty(value = "账户名称", hidden = true)
  private String acctName;


}
