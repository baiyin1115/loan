package com.zsy.loan.service.factory;

import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.ServiceFeeTypeEnum;
import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * 根据还款方式、本金、利率、服务费收取方式、服务费比例、开始结束日期计算
 * 利息、服务费、放款金额、期数、应还本金、应还利息、应收服务费、还款计划信息
 *
 * @Author zhangxh
 * @Date 2019-01-30  9:59
 */
@Slf4j
public class TrialCalculateFactory {

  //向上取整用Math.ceil(double a)向下取整用Math.floor(double a)

  /**
   * 试算工厂
   */
  public static Map<RepayTypeEnum, Function<LoanCalculateVo, LoanCalculateVo>> maps
      = new HashMap<RepayTypeEnum, Function<LoanCalculateVo, LoanCalculateVo>>();

  static {
    //这里用到landa表达式，新特性。 其中 Cat，Dog 可以看成 if-else 中的条件
    maps.put(RepayTypeEnum.EQUAL_LOAN, bean -> equalLoan(bean));
    maps.put(RepayTypeEnum.EQUAL_PRINCIPAL, bean -> equalPrincipal(bean));
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL, bean -> bInterestAPrincipal(bean));
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL_2, bean -> bInterestAPrincipal2(bean));
    maps.put(RepayTypeEnum.A_DEBT_SERVICE_DUE, bean -> aDebtServiceDue(bean));
  }

  /**
   * 先息后本
   * @param data
   * @return
   */
  private static LoanCalculateVo bInterestAPrincipal(LoanCalculateVo data) {

    log.info("要试算的数据是："+data);
    LoanCalculateVo result =  LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data,result);

    if(data.getLoanBizType().equals(LoanBizTypeEnum.LOAN_CHECK_IN.getValue())){  //融资登记
      // 期数
      result.setTermNo((long)result.getMonth());

      // 应收利息 = 本金*月利息*期数
      result.setReceiveInterest(BigDecimalUtil.mul(data.getPrin(),data.getMonthRate(), BigDecimal.valueOf(data.getMonth())));

      // 服务费 = 本金 * 服务费比例
      result.setServiceFee(BigDecimalUtil.mul(data.getPrin(),data.getServiceFeeScale()));

      // 放款金额 首期直接扣减服务费
      if(data.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue())){
        result.setLendingAmt(BigDecimalUtil.sub(data.getPrin(),result.getServiceFee()));
      }else{
        result.setLendingAmt(data.getPrin());
      }

      // 应还本金
      result.setSchdPrin(data.getPrin());

      // 应还利息
      result.setSchdInterest(result.getReceiveInterest());

      // 应收服务费
      result.setSchdServFee(result.getServiceFee());
    }

    if(data.getLoanBizType().equals(LoanBizTypeEnum.PUT.getValue())) {  //放款
      Long termNo = data.getTermNo(); //期数
      BigDecimal schdPrin = data.getSchdPrin(); //应还本金
      BigDecimal schdInterest = data.getSchdInterest(); //应还利息
      BigDecimal schdServFee = data.getSchdServFee(); //应收服务费

      //当期
      BigDecimal ctdPrin = BigDecimalUtil.div(schdPrin,BigDecimal.valueOf(termNo));
      BigDecimal ctdInterest = BigDecimalUtil.div(schdInterest,BigDecimal.valueOf(termNo));
      BigDecimal ctdServFee = BigDecimalUtil.div(schdServFee,BigDecimal.valueOf(termNo));
      Date beginDate = data.getBeginDate();
      Date endDate = null;

      List<TBizRepayPlan> list = new ArrayList<>(termNo.intValue());

      for (int i = 0; i < termNo; i++) {
        TBizRepayPlan plan = TBizRepayPlan.builder().build();
        BeanKit.copyProperties(data,plan);

        plan.setId(null);
        plan.setTermNo(i+1l); //当前期
        plan.setBeginDate(beginDate); //开始时间
//        plan.setEndDate(DateUtil.getAfterDayDate(data.getProduct().getCycleInterval().intValue()));

      }


    }


    log.info("试算结果为："+result);
    return result;
  }

  /**
   * 4:先息后本[上交息],
   * @param data
   * @return
   */
  private static LoanCalculateVo bInterestAPrincipal2(LoanCalculateVo data) {

    return bInterestAPrincipal(data);
  }

  /**
   * 5:一次性还本付息
   * @param data
   * @return
   */
  private static LoanCalculateVo aDebtServiceDue(LoanCalculateVo data) {

    log.info("要试算的数据是："+data);
    LoanCalculateVo result =  LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data,result);

    // 期数 = 就是一期
    result.setTermNo(1l);

    // 应收利息 = 本金*日利息*天数
    result.setReceiveInterest(BigDecimalUtil.mul(data.getPrin(),data.getDayRate(), BigDecimal.valueOf(data.getDay())));

    // 服务费 = 本金 * 服务费比例
    result.setServiceFee(BigDecimalUtil.mul(data.getPrin(),data.getServiceFeeScale()));

    // 放款金额 首期直接扣减服务费
    if(data.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue())){
      result.setLendingAmt(BigDecimalUtil.sub(data.getPrin(),result.getServiceFee()));
    }else{
      result.setLendingAmt(data.getPrin());
    }

    // 应还本金
    result.setSchdPrin(data.getPrin());

    // 应还利息
    result.setSchdInterest(result.getReceiveInterest());

    // 应收服务费
    result.setSchdServFee(result.getServiceFee());

    log.info("试算结果为："+result);
    return result;

  }

  /**
   * 等额本息
   * @param data
   * @return
   */
  private static LoanCalculateVo equalLoan(LoanCalculateVo data) {

    //TODO
    return null;
  }

  /**
   * 等额本金
   * @param data
   * @return
   */
  private static LoanCalculateVo equalPrincipal(LoanCalculateVo data) {

    //TODO
    return null;
  }



}
