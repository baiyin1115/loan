package com.zsy.loan.service.factory;

import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayStatusEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.vo.RepayPlanStatusVo;
import java.util.HashMap;
import java.util.Map;

/**
 * 还款计划状态工厂
 *
 * @Author zhangxh
 * @Date 2019-02-01  10:10
 */
public class RepayPlanStatusFactory {

  /**
   * 借据状态工厂
   */
  public static Map<String, RepayPlanStatusVo> maps = new HashMap<String, RepayPlanStatusVo>();

  static {

    /**
     * 还款
     */
    maps.put(RepayStatusEnum.NOT_REPAY.getValue() + "_" + LoanBizTypeEnum.REPAY.getValue(),//待还-还款
        RepayPlanStatusVo.builder()
            .currentStatus(RepayStatusEnum.NOT_REPAY)
            .nextStatus(RepayStatusEnum.REPAID)
            .bizType(LoanBizTypeEnum.REPAY).build());
    maps.put(RepayStatusEnum.OVERDUE.getValue() + "_" + LoanBizTypeEnum.REPAY.getValue(),//已逾期-还款
        RepayPlanStatusVo.builder()
            .currentStatus(RepayStatusEnum.OVERDUE)
            .nextStatus(RepayStatusEnum.REPAID)
            .bizType(LoanBizTypeEnum.REPAY).build());
  }


  /**
   * 校验当前状态
   */
  public static boolean checkCurrentStatus(String key) {
    if (maps.get(key) == null) {
      throw new LoanException(BizExceptionEnum.STATUS_ERROR, "");
    } else {
      return true;
    }
  }

  /**
   * 取得下一个状态
   */
  public static RepayStatusEnum getNextStatus(String key) {
    RepayPlanStatusVo vo = maps.get(key);
    if (vo == null) {
      throw new LoanException(BizExceptionEnum.STATUS_ERROR, "");
    }

    return vo.getNextStatus();
  }

}
