package com.zsy.loan.bean.vo;

import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 借据状态定义
 *
 * @Author zhangxh
 * @Date 2019-02-01  9:54
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepayPlanStatusVo {

  //当前状态
  private RepayStatusEnum currentStatus;
  //下一个状态
  private RepayStatusEnum nextStatus;
  //动作
  private LoanBizTypeEnum bizType;

}
