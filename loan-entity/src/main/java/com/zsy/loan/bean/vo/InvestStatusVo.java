package com.zsy.loan.bean.vo;

import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 融资状态定义
 *
 * @Author zhangxh
 * @Date 2019-02-01  9:54
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestStatusVo {

  //当前状态
  private InvestStatusEnum currentStatus;
  //下一个状态
  private InvestStatusEnum nextStatus;
  //动作
  private LoanBizTypeEnum bizType;

}
