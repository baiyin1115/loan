package com.zsy.loan.service.factory;

import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestStatusEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.vo.InvestStatusVo;
import java.util.HashMap;
import java.util.Map;

/**
 * 融资状态工厂
 *
 * @Author zhangxh
 * @Date 2019-02-01  10:10
 */
public class InvestStatusFactory {

  /**
   * 融资状态工厂
   */
  public static Map<String, InvestStatusVo> maps = new HashMap<String, InvestStatusVo>();

  static {

    /**
     * 起息
     */
    maps.put(InvestStatusEnum.CHECK_IN.getValue() + "_" + LoanBizTypeEnum.INVEST.getValue(),//登记-融资
        InvestStatusVo.builder()
            .currentStatus(InvestStatusEnum.CHECK_IN)
            .nextStatus(InvestStatusEnum.INTEREST_ING)
            .bizType(LoanBizTypeEnum.INVEST).build());

    /**
     * 延期
     */
    maps.put(InvestStatusEnum.INTEREST_ING.getValue() + "_" + LoanBizTypeEnum.DELAY.getValue(),//计息中-展期
        InvestStatusVo.builder()
            .currentStatus(InvestStatusEnum.INTEREST_ING)
            .nextStatus(InvestStatusEnum.DELAY)
            .bizType(LoanBizTypeEnum.DELAY).build());
    maps.put(InvestStatusEnum.DELAY.getValue() + "_" + LoanBizTypeEnum.DELAY.getValue(), //已延期-展期
        InvestStatusVo.builder()
            .currentStatus(InvestStatusEnum.DELAY)
            .nextStatus(InvestStatusEnum.DELAY)
            .bizType(LoanBizTypeEnum.DELAY).build());

    /**
     * 撤资
     */
    maps.put(InvestStatusEnum.INTEREST_ING.getValue() + "_" + LoanBizTypeEnum.DIVESTMENT.getValue(),//计息中-撤资
        InvestStatusVo.builder()
            .currentStatus(InvestStatusEnum.INTEREST_ING)
            .nextStatus(InvestStatusEnum.DIVESTMENT)
            .bizType(LoanBizTypeEnum.DIVESTMENT).build());
    maps.put(InvestStatusEnum.DELAY.getValue() + "_" + LoanBizTypeEnum.DIVESTMENT.getValue(), //已延期-撤资
        InvestStatusVo.builder()
            .currentStatus(InvestStatusEnum.DELAY)
            .nextStatus(InvestStatusEnum.DIVESTMENT)
            .bizType(LoanBizTypeEnum.DIVESTMENT).build());

    /**
     * 结转
     */
    maps.put(InvestStatusEnum.INTEREST_ING.getValue() + "_" + LoanBizTypeEnum.SETTLEMENT.getValue(),//计息中-撤资
        InvestStatusVo.builder()
            .currentStatus(InvestStatusEnum.INTEREST_ING)
            .nextStatus(InvestStatusEnum.SETTLEMENT)
            .bizType(LoanBizTypeEnum.SETTLEMENT).build());
    maps.put(InvestStatusEnum.DELAY.getValue() + "_" + LoanBizTypeEnum.SETTLEMENT.getValue(), //已延期-撤资
        InvestStatusVo.builder()
            .currentStatus(InvestStatusEnum.DELAY)
            .nextStatus(InvestStatusEnum.SETTLEMENT)
            .bizType(LoanBizTypeEnum.SETTLEMENT).build());

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
  public static InvestStatusEnum getNextStatus(String key) {
    InvestStatusVo vo = maps.get(key);
    if (vo == null) {
      throw new LoanException(BizExceptionEnum.LOAN_STATUS_ERROR, "");
    }

    return vo.getNextStatus();
  }

}
