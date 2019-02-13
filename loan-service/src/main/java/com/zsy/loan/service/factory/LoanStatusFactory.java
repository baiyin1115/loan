package com.zsy.loan.service.factory;

import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanStatusEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.vo.LoanStatusVo;
import java.util.HashMap;
import java.util.Map;

/**
 * 借据状态工厂
 *
 * @Author zhangxh
 * @Date 2019-02-01  10:10
 */
public class LoanStatusFactory {

  /**
   * 借据状态工厂
   */
  public static Map<String, LoanStatusVo> maps = new HashMap<String, LoanStatusVo>();

  static {

    /**
     * 放款
     */
    maps.put(LoanStatusEnum.CHECK_IN.getValue() + "_" + LoanBizTypeEnum.PUT.getValue(),//登记-放款
        LoanStatusVo.builder()
            .currentStatus(LoanStatusEnum.CHECK_IN)
            .nextStatus(LoanStatusEnum.PUT)
            .bizType(LoanBizTypeEnum.PUT).build());

    /**
     * 展期
     */
    maps.put(LoanStatusEnum.PUT.getValue() + "_" + LoanBizTypeEnum.DELAY.getValue(),//已放款-展期
        LoanStatusVo.builder()
            .currentStatus(LoanStatusEnum.PUT)
            .nextStatus(LoanStatusEnum.DELAY)
            .bizType(LoanBizTypeEnum.DELAY).build());
    maps.put(LoanStatusEnum.REPAY_IND.getValue() + "_" + LoanBizTypeEnum.DELAY.getValue(), //还款中-展期
        LoanStatusVo.builder()
            .currentStatus(LoanStatusEnum.REPAY_IND)
            .nextStatus(LoanStatusEnum.DELAY)
            .bizType(LoanBizTypeEnum.DELAY).build());
    maps.put(LoanStatusEnum.OVERDUE.getValue() + "_" + LoanBizTypeEnum.DELAY.getValue(),//已逾期-展期
        LoanStatusVo.builder()
            .currentStatus(LoanStatusEnum.OVERDUE)
            .nextStatus(LoanStatusEnum.DELAY)
            .bizType(LoanBizTypeEnum.DELAY).build());
    maps.put(LoanStatusEnum.DELAY.getValue() + "_" + LoanBizTypeEnum.DELAY.getValue(),//已展期-展期
        LoanStatusVo.builder()
            .currentStatus(LoanStatusEnum.DELAY)
            .nextStatus(LoanStatusEnum.DELAY)
            .bizType(LoanBizTypeEnum.DELAY).build());
  }

  /**
   * 校验当前状态
   */
  public static boolean checkCurrentStatus(String key) {
    if (maps.get(key) == null) {
      throw new LoanException(BizExceptionEnum.LOAN_STATUS_ERROR, "");
    } else {
      return true;
    }
  }

  /**
   * 取得下一个状态
   */
  public static LoanStatusEnum getNextStatus(String key) {
    LoanStatusVo vo = maps.get(key);
    if (vo == null) {
      throw new LoanException(BizExceptionEnum.LOAN_STATUS_ERROR, "");
    }

    return vo.getNextStatus();
  }

}
