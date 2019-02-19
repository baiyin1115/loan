package com.zsy.loan.service.factory;

import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.ServiceFeeTypeEnum;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.JodaTimeUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 * 根据还款方式、本金、利率、服务费收取方式、服务费比例、开始结束日期计算
 * 利息、服务费、放款金额、期数、应还本金、应还利息、应收服务费、还款计划信息
 *
 * @Author zhangxh
 * @Date 2019-01-30  9:59
 */
@Slf4j
public class LoanTrialCalculateFactory {

  //向上取整用Math.ceil(double a)向下取整用Math.floor(double a)

  /**
   * 试算工厂
   */
  public static Map<String, Function<LoanCalculateVo, LoanCalculateVo>> maps = new HashMap<>();

  static {

    /**
     * 登记试算
     */
    maps.put(RepayTypeEnum.EQUAL_LOAN + "_" + LoanBizTypeEnum.LOAN_CHECK_IN,
        LoanTrialCalculateFactory::equalLoan_check_in);
    maps.put(RepayTypeEnum.EQUAL_PRINCIPAL + "_" + LoanBizTypeEnum.LOAN_CHECK_IN,
        LoanTrialCalculateFactory::equalPrincipal_check_in);
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL + "_" + LoanBizTypeEnum.LOAN_CHECK_IN,
        LoanTrialCalculateFactory::bInterestAPrincipalCheckIn);
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL_2 + "_" + LoanBizTypeEnum.LOAN_CHECK_IN,
        LoanTrialCalculateFactory::bInterestAPrincipal2_check_in);
    maps.put(RepayTypeEnum.A_DEBT_SERVICE_DUE + "_" + LoanBizTypeEnum.LOAN_CHECK_IN,
        LoanTrialCalculateFactory::aDebtServiceDue_check_in);

    /**
     * 放款试算
     */
    maps.put(RepayTypeEnum.EQUAL_LOAN + "_" + LoanBizTypeEnum.PUT,
        LoanTrialCalculateFactory::equalLoan_put);
    maps.put(RepayTypeEnum.EQUAL_PRINCIPAL + "_" + LoanBizTypeEnum.PUT,
        LoanTrialCalculateFactory::equalPrincipal_put);
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL + "_" + LoanBizTypeEnum.PUT,
        LoanTrialCalculateFactory::bInterestAPrincipal_put);
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL_2 + "_" + LoanBizTypeEnum.PUT,
        LoanTrialCalculateFactory::bInterestAPrincipal2_put);
    maps.put(RepayTypeEnum.A_DEBT_SERVICE_DUE + "_" + LoanBizTypeEnum.PUT,
        LoanTrialCalculateFactory::aDebtServiceDue_put);

    /**
     * 展期试算
     */
    maps.put(RepayTypeEnum.EQUAL_LOAN + "_" + LoanBizTypeEnum.DELAY,
        LoanTrialCalculateFactory::equalLoan_delay);
    maps.put(RepayTypeEnum.EQUAL_PRINCIPAL + "_" + LoanBizTypeEnum.DELAY,
        LoanTrialCalculateFactory::equalPrincipal_delay);
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL + "_" + LoanBizTypeEnum.DELAY,
        LoanTrialCalculateFactory::bInterestAPrincipal_delay);
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL_2 + "_" + LoanBizTypeEnum.DELAY,
        LoanTrialCalculateFactory::bInterestAPrincipal2_delay);
    maps.put(RepayTypeEnum.A_DEBT_SERVICE_DUE + "_" + LoanBizTypeEnum.DELAY,
        LoanTrialCalculateFactory::aDebtServiceDue_delay);
  }

  /**
   * 登记试算--先息后本
   */
  private static LoanCalculateVo bInterestAPrincipalCheckIn(LoanCalculateVo data) {

    log.info("登记试算--先息后本--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    // 期数
    result.setTermNo((long) result.getMonth());

    // 应收利息 = 本金*月利息*期数
    result.setReceiveInterest(BigDecimalUtil
        .mul(data.getPrin(), data.getMonthRate(), BigDecimal.valueOf(data.getMonth())));

    // 服务费 = 本金 * 服务费比例
    result.setServiceFee(BigDecimalUtil.mul(data.getPrin(), data.getServiceFeeScale()));

    // 放款金额 首期直接扣减服务费
    if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue())) {
      result.setLendingAmt(BigDecimalUtil.sub(data.getPrin(), result.getServiceFee()));
    } else {
      result.setLendingAmt(data.getPrin());
    }

    // 应还本金
    result.setSchdPrin(data.getPrin());

    // 应还利息
    result.setSchdInterest(result.getReceiveInterest());

    // 应收服务费
    result.setSchdServFee(result.getServiceFee());

    log.info("登记试算--先息后本--试算结果为：" + result);
    return result;
  }

  /**
   * 登记试算--先息后本[上交息],
   */
  private static LoanCalculateVo bInterestAPrincipal2_check_in(LoanCalculateVo data) {

    log.info("登记试算--先息后本[上交息]--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    // 期数
    result.setTermNo((long) result.getMonth() + 1);

    // 应收利息 = 本金*月利息*期数
    result.setReceiveInterest(BigDecimalUtil
        .mul(data.getPrin(), data.getMonthRate(), BigDecimal.valueOf(data.getMonth())));

    // 服务费 = 本金 * 服务费比例
    result.setServiceFee(BigDecimalUtil.mul(data.getPrin(), data.getServiceFeeScale()));

    // 放款金额 首期直接扣减服务费
    if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue())) {
      result.setLendingAmt(BigDecimalUtil.sub(data.getPrin(), result.getServiceFee()));
    } else {
      result.setLendingAmt(data.getPrin());
    }

    // 应还本金
    result.setSchdPrin(data.getPrin());

    // 应还利息
    result.setSchdInterest(result.getReceiveInterest());

    // 应收服务费
    result.setSchdServFee(result.getServiceFee());

    log.info("登记试算--先息后本[上交息]--试算结果为：" + result);
    return result;
  }

  /**
   * 登记试算--一次性还本付息
   */
  private static LoanCalculateVo aDebtServiceDue_check_in(LoanCalculateVo data) {

    log.info("登记试算--一次性还本付息--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    // 期数 = 就是一期
    result.setTermNo(1l);

    // 应收利息 = 本金*日利息*天数
    result.setReceiveInterest(
        BigDecimalUtil.mul(data.getPrin(), data.getDayRate(), BigDecimal.valueOf(data.getDay())));

    // 服务费 = 本金 * 服务费比例
    result.setServiceFee(BigDecimalUtil.mul(data.getPrin(), data.getServiceFeeScale()));

    // 放款金额 首期直接扣减服务费
    if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue())) {
      result.setLendingAmt(BigDecimalUtil.sub(data.getPrin(), result.getServiceFee()));
    } else {
      result.setLendingAmt(data.getPrin());
    }

    // 应还本金
    result.setSchdPrin(data.getPrin());

    // 应还利息
    result.setSchdInterest(result.getReceiveInterest());

    // 应收服务费
    result.setSchdServFee(result.getServiceFee());

    log.info("登记试算--一次性还本付息--试算结果为：" + result);
    return result;

  }

  /**
   * 登记试算--等额本息
   */
  private static LoanCalculateVo equalLoan_check_in(LoanCalculateVo data) {

    //TODO
    return null;
  }

  /**
   * 登记试算--等额本金
   */
  private static LoanCalculateVo equalPrincipal_check_in(LoanCalculateVo data) {

    //TODO
    return null;
  }

  /**
   * 放款试算--先息后本
   */
  private static LoanCalculateVo bInterestAPrincipal_put(LoanCalculateVo data) {

    log.info("放款试算--先息后本--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    Long termNo = data.getTermNo(); //期数
    BigDecimal schdPrin = data.getSchdPrin(); //应还本金
    BigDecimal schdInterest = data.getSchdInterest(); //应还利息
    BigDecimal schdServFee = data.getSchdServFee(); //应收服务费

    //当期
    //BigDecimal ctdPrin = BigDecimalUtil.div(schdPrin, BigDecimal.valueOf(termNo));
    BigDecimal ctdInterest = BigDecimalUtil.div(schdInterest, BigDecimal.valueOf(termNo));
    BigDecimal ctdServFee = BigDecimalUtil.div(schdServFee, BigDecimal.valueOf(termNo));
    Date beginDate = data.getBeginDate();
    Date endDate = beginDate;
    Date lendingDate = data.getLendingDate(); //首期是放款日期
    Long dd = data.getDdDate(); //约定还款日

    List<TBizRepayPlan> list = new ArrayList<>(termNo.intValue());

    BigDecimal serviceFee = BigDecimal.valueOf(0.00);
    BigDecimal interest = BigDecimal.valueOf(0.00);
    for (int i = 0; i < termNo; i++) {
      TBizRepayPlan plan = TBizRepayPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      plan.setId(null);
      plan.setLoanNo(data.getId());
      plan.setTermNo(i + 1l); //当前期
      plan.setBeginDate(beginDate); //开始日期

      plan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
      if (i == termNo - 1) {
        plan.setEndDate(data.getEndDate()); //结束日期
        plan.setDdDate(data.getEndDate()); //还款日期
        plan.setCtdPrin(schdPrin); //本期应还本金
        plan.setCtdInterest(BigDecimalUtil.sub(schdInterest, interest)); //本期应还利息
        // 按期扣减服务费
        if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.EACH.getValue())) {
          plan.setCtdServFee(BigDecimalUtil.sub(schdServFee, serviceFee)); //本期应收服务费
        }

      } else {
        endDate = JodaTimeUtil.getAfterDayMonth(data.getBeginDate(), i + 1);
        interest = BigDecimalUtil.add(interest, ctdInterest);
        serviceFee = BigDecimalUtil.add(serviceFee, ctdServFee);

        plan.setEndDate(endDate); //结束日期
        plan.setDdDate(endDate); //还款日期
        plan.setCtdPrin(BigDecimal.valueOf(0.00)); //本期应还本金
        plan.setCtdInterest(ctdInterest); //本期应还利息
        // 按期扣减服务费
        if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.EACH.getValue())) {
          plan.setCtdServFee(ctdServFee); //本期应收服务费
        }
      }

      plan.setInAcctNo(data.getLendingAcct()); //入账账户
      plan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
      plan.setPaidPrin(BigDecimal.valueOf(0.00));
      plan.setPaidInterest(BigDecimal.valueOf(0.00));
      plan.setPaidServFee(BigDecimal.valueOf(0.00));
      plan.setPaidPen(BigDecimal.valueOf(0.00));
      plan.setWavAmt(BigDecimal.valueOf(0.00));
      plan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
      plan.setDdNum(data.getProduct().getCycleInterval()); //计息天数

      /**
       * 还款日期 如果约定还款日有值取当期约定还款日，没有就是当期结束日期，最后一期还款日是借据结束日期
       */
      if (i == data.getTermNo() - 1) {
        plan.setDdDate(data.getEndDate()); //还款日期
      } else {
        if (dd != null && dd.intValue() != 0) {
          plan.setDdDate(JodaTimeUtil.getDdDate(beginDate, endDate, dd.intValue()));
        } else {
          plan.setDdDate(endDate); //还款日期
        }
      }

      list.add(plan);
      beginDate = plan.getEndDate();//修改开始日期，为下一次循环准备
    }
    result.setRepayPlanList(list);

    log.info("放款试算--先息后本--试算结果为：" + result);
    return result;
  }

  /**
   * 放款试算--先息后本[上交息],
   */
  private static LoanCalculateVo bInterestAPrincipal2_put(LoanCalculateVo data) {

    log.info("放款试算--先息后本[上交息]--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    Long termNo = data.getTermNo() - 1; //期数 最后一期只还本金  不能计算在内
    BigDecimal schdPrin = data.getSchdPrin(); //应还本金
    BigDecimal schdInterest = data.getSchdInterest(); //应还利息
    BigDecimal schdServFee = data.getSchdServFee(); //应收服务费

    //当期
    //BigDecimal ctdPrin = BigDecimalUtil.div(schdPrin, BigDecimal.valueOf(termNo));
    BigDecimal ctdInterest = BigDecimalUtil.div(schdInterest, BigDecimal.valueOf(termNo));
    BigDecimal ctdServFee = BigDecimalUtil.div(schdServFee, BigDecimal.valueOf(termNo));
    Date beginDate = data.getBeginDate();
    Date endDate = beginDate;

    Date lendingDate = data.getLendingDate(); //首期是放款日期
    Long dd = data.getDdDate(); //约定还款日

    List<TBizRepayPlan> list = new ArrayList<>(data.getTermNo().intValue());

    BigDecimal serviceFee = BigDecimal.valueOf(0.00);
    BigDecimal interest = BigDecimal.valueOf(0.00);
    for (int i = 0; i < termNo; i++) {
      TBizRepayPlan plan = TBizRepayPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      plan.setId(null);
      plan.setLoanNo(data.getId());
      plan.setTermNo(i + 1l); //当前期
      plan.setBeginDate(beginDate); //开始日期

      endDate = JodaTimeUtil.getAfterDayMonth(data.getBeginDate(), i + 1);
      plan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
      plan.setCtdPrin(BigDecimal.valueOf(0.00)); //本期应还本金
      if (i == termNo - 1) {
        plan.setCtdInterest(BigDecimalUtil.sub(schdInterest, interest)); //本期应还利息
        // 按期扣减服务费
        if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.EACH.getValue())) {
          plan.setCtdServFee(BigDecimalUtil.sub(schdServFee, serviceFee)); //本期应收服务费
        }
        plan.setEndDate(data.getEndDate()); //结束日期
      } else {
        interest = BigDecimalUtil.add(interest, ctdInterest);
        serviceFee = BigDecimalUtil.add(serviceFee, ctdServFee);
        plan.setCtdInterest(ctdInterest); //本期应还利息
        // 按期扣减服务费
        if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.EACH.getValue())) {
          plan.setCtdServFee(ctdServFee); //本期应收服务费
        }
        plan.setEndDate(endDate); //结束日期
      }

      plan.setInAcctNo(data.getLendingAcct()); //入账账户
      plan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
      plan.setPaidPrin(BigDecimal.valueOf(0.00));
      plan.setPaidInterest(BigDecimal.valueOf(0.00));
      plan.setPaidServFee(BigDecimal.valueOf(0.00));
      plan.setPaidPen(BigDecimal.valueOf(0.00));
      plan.setWavAmt(BigDecimal.valueOf(0.00));
      plan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
      plan.setDdNum(data.getProduct().getCycleInterval()); //计息天数

      /**
       * 还款日期 --上扣息 首期是放款日期  如果约定还款日有值取当期约定还款日，没有就是当期开始日期，最后一期还款日是借据结束日期
       */
      if (i == 0) {
        plan.setDdDate(lendingDate); //还款日期
      } else {
        if (dd != null && dd.intValue() != 0) {
          plan.setDdDate(JodaTimeUtil.getDdDate(beginDate, endDate, dd.intValue()));
        } else {
          plan.setDdDate(beginDate); //还款日期
        }
      }

      list.add(plan);
      beginDate = plan.getEndDate();//修改开始日期，为下一次循环准备
    }

    TBizRepayPlan endPlan = TBizRepayPlan.builder().build();
    BeanKit.copyProperties(data, endPlan);
    endPlan.setId(null);
    endPlan.setLoanNo(data.getId());
    endPlan.setTermNo(data.getTermNo()); //当前期
    endPlan.setBeginDate(list.get(termNo.intValue() - 1).getBeginDate()); //开始日期
    endPlan.setEndDate(list.get(termNo.intValue() - 1).getEndDate()); //结束日期
    endPlan.setDdDate(list.get(termNo.intValue() - 1).getEndDate()); //还款日期
    endPlan.setCtdPrin(schdPrin); //本期应还本金
    endPlan.setCtdInterest(BigDecimal.valueOf(0.00)); //本期应还利息
    endPlan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
    endPlan.setInAcctNo(data.getLendingAcct()); //入账账户
    endPlan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
    endPlan.setPaidPrin(BigDecimal.valueOf(0.00));
    endPlan.setPaidInterest(BigDecimal.valueOf(0.00));
    endPlan.setPaidServFee(BigDecimal.valueOf(0.00));
    endPlan.setPaidPen(BigDecimal.valueOf(0.00));
    endPlan.setWavAmt(BigDecimal.valueOf(0.00));
    endPlan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
    endPlan.setDdNum(0l); //计息天数

    list.add(endPlan); //最后一期只还本金

    result.setRepayPlanList(list);

    log.info("放款试算--先息后本[上交息]--试算结果为：" + result);
    return result;
  }

  /**
   * 放款试算--一次性还本付息
   */
  private static LoanCalculateVo aDebtServiceDue_put(LoanCalculateVo data) {

    log.info("放款试算--一次性还本付息--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    Long termNo = data.getTermNo(); //期数
    BigDecimal schdPrin = data.getSchdPrin(); //应还本金
    BigDecimal schdInterest = data.getSchdInterest(); //应还利息
    BigDecimal schdServFee = data.getSchdServFee(); //应收服务费

    List<TBizRepayPlan> list = new ArrayList<>(data.getTermNo().intValue());

    TBizRepayPlan plan = TBizRepayPlan.builder().build();
    BeanKit.copyProperties(data, plan);

    plan.setId(null);
    plan.setLoanNo(data.getId());
    plan.setTermNo(1l); //当前期
    plan.setBeginDate(data.getBeginDate()); //开始日期
    plan.setEndDate(data.getEndDate()); //结束日期
    plan.setDdDate(data.getEndDate()); //还款日期
    plan.setCtdPrin(schdPrin); //本期应还本金
    plan.setCtdInterest(schdInterest); //本期应还利息
    // 按期扣减服务费
    if (data.getServiceFeeType().equals(ServiceFeeTypeEnum.EACH.getValue())) {
      plan.setCtdServFee(schdServFee); //本期应收服务费
    } else {
      plan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
    }
    plan.setInAcctNo(data.getLendingAcct()); //入账账户
    plan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
    plan.setPaidPrin(BigDecimal.valueOf(0.00));
    plan.setPaidInterest(BigDecimal.valueOf(0.00));
    plan.setPaidServFee(BigDecimal.valueOf(0.00));
    plan.setPaidPen(BigDecimal.valueOf(0.00));
    plan.setWavAmt(BigDecimal.valueOf(0.00));
    plan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
//    plan.setDdNum(data.getProduct().getCycleInterval()); //计息天数
    plan.setDdNum((long) JodaTimeUtil.daysBetween(data.getBeginDate(), data.getEndDate())); //计息天数

    list.add(plan);
    result.setRepayPlanList(list);

    log.info("放款试算--一次性还本付息--试算结果为：" + result);
    return result;

  }

  /**
   * 放款试算--等额本息
   */
  private static LoanCalculateVo equalLoan_put(LoanCalculateVo data) {

    //TODO
    return null;
  }

  /**
   * 放款试算--等额本金
   */
  private static LoanCalculateVo equalPrincipal_put(LoanCalculateVo data) {

    //TODO
    return null;
  }


  /**
   * 展期试算--一次性还本付息
   */
  private static LoanCalculateVo aDebtServiceDue_delay(LoanCalculateVo data) {

    log.info("展期试算--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    Long currentNo = data.getCurrentExtensionNo(); //当前展期期数
    BigDecimal schdPrin = BigDecimalUtil
        .sub(data.getSchdPrin(), data.getTotPaidPrin()); //应还本金=应还本金-已还本金累计
    BigDecimal schdInterest = BigDecimalUtil.mul(schdPrin, data.getDelayDayRate(),
        BigDecimal.valueOf(data.getProduct().getCycleInterval())); //应还利息

    Date beginDate = data.getEndDate();
    Date endDate = JodaTimeUtil.getAfterDayDate(data.getEndDate(),
        data.getProduct().getCycleInterval().intValue()); //按照产品周期计算

    List<TBizRepayPlan> list = data.getRepayPlanList();

    /**
     * 设置最后一期信息
     */
    TBizRepayPlan last = list
        .get(data.getTermNo().intValue() + data.getExtensionNo().intValue() - 1); //最后一期
    last.setCtdPrin(BigDecimal.valueOf(0.00));

    /**
     * 新增展期还款计划
     */
    TBizRepayPlan plan = TBizRepayPlan.builder().build();
    BeanKit.copyProperties(data, plan);

    plan.setRate(data.getExtensionRate()); //展期利率
    plan.setId(null);
    plan.setLoanNo(data.getId());
    plan.setTermNo(data.getTermNo() + data.getExtensionNo() + currentNo); //当前期
    plan.setBeginDate(beginDate); //开始日期
    plan.setEndDate(endDate); //结束日期
    plan.setDdDate(endDate); //还款日期
    plan.setCtdPrin(schdPrin); //本期应还本金
    plan.setCtdInterest(schdInterest); //本期应还利息
    plan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
    plan.setInAcctNo(data.getLendingAcct()); //入账账户
    plan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
    plan.setPaidPrin(BigDecimal.valueOf(0.00));
    plan.setPaidInterest(BigDecimal.valueOf(0.00));
    plan.setPaidServFee(BigDecimal.valueOf(0.00));
    plan.setPaidPen(BigDecimal.valueOf(0.00));
    plan.setWavAmt(BigDecimal.valueOf(0.00));
    plan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
    plan.setDdNum(data.getProduct().getCycleInterval()); //计息天数

    list.add(plan);
    result.setRepayPlanList(list);

    /**
     * 设置借据信息
     */
    result.setSchdInterest(BigDecimalUtil.add(data.getSchdInterest(), schdInterest)); //应还利息
    result.setEndDate(endDate); //借款结束日期

    log.info("试算结果为：" + result);
    return result;

  }

  /**
   * 展期试算--先息后本
   */
  private static LoanCalculateVo bInterestAPrincipal_delay(LoanCalculateVo data) {

    log.info("展期试算--先息后本--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    Long currentNo = data.getCurrentExtensionNo(); //当前展期期数
    BigDecimal schdPrin = BigDecimalUtil
        .sub(data.getSchdPrin(), data.getTotPaidPrin()); //应还本金=应还本金-已还本金累计
    BigDecimal schdInterest = BigDecimalUtil
        .mul(schdPrin, data.getDelayMonthRate(), BigDecimal.valueOf(currentNo)); //应还利息

    //当期
    BigDecimal ctdInterest = BigDecimalUtil.div(schdInterest, BigDecimal.valueOf(currentNo));
    Date beginDate = data.getEndDate();
    Date endDate = beginDate;
    Long dd = data.getDdDate(); //约定还款日

    List<TBizRepayPlan> list = data.getRepayPlanList();

    /**
     * 设置最后一期信息
     */
    TBizRepayPlan last = list
        .get(data.getTermNo().intValue() + data.getExtensionNo().intValue() - 1); //最后一期
    last.setCtdPrin(BigDecimal.valueOf(0.00));

    /**
     * 新增展期还款计划
     */
    BigDecimal interest = BigDecimal.valueOf(0.00);
    for (int i = 0; i < currentNo; i++) {
      TBizRepayPlan plan = TBizRepayPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      plan.setId(null);
      plan.setLoanNo(data.getId());
      plan.setTermNo(data.getTermNo() + data.getExtensionNo() + i + 1l); //当前期
      plan.setBeginDate(beginDate); //开始日期

      plan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
      endDate = JodaTimeUtil.getAfterDayMonth(beginDate, 1);
      plan.setEndDate(endDate); //结束日期
      if (i == currentNo - 1) {
        plan.setCtdPrin(schdPrin); //本期应还本金
        plan.setCtdInterest(BigDecimalUtil.sub(schdInterest, interest)); //本期应还利息

      } else {
        interest = BigDecimalUtil.add(interest, ctdInterest);
        plan.setCtdPrin(BigDecimal.valueOf(0.00)); //本期应还本金
        plan.setCtdInterest(ctdInterest); //本期应还利息
      }

      plan.setRate(data.getExtensionRate()); //展期利率
      plan.setInAcctNo(data.getLendingAcct()); //入账账户
      plan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
      plan.setPaidPrin(BigDecimal.valueOf(0.00));
      plan.setPaidInterest(BigDecimal.valueOf(0.00));
      plan.setPaidServFee(BigDecimal.valueOf(0.00));
      plan.setPaidPen(BigDecimal.valueOf(0.00));
      plan.setWavAmt(BigDecimal.valueOf(0.00));
      plan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
      plan.setDdNum(data.getProduct().getCycleInterval()); //计息天数

      /**
       * 还款日期 如果约定还款日有值取当期约定还款日，没有就是当期结束日期，最后一期还款日是借据结束日期
       */
      if (i == currentNo - 1) {
        plan.setDdDate(endDate); //还款日期
      } else {
        if (dd != null && dd.intValue() != 0) {
          plan.setDdDate(JodaTimeUtil.getDdDate(beginDate, endDate, dd.intValue()));
        } else {
          plan.setDdDate(endDate); //还款日期
        }
      }

      list.add(plan);
      beginDate = plan.getEndDate();//修改开始日期，为下一次循环准备
    }
    result.setRepayPlanList(list);

    /**
     * 设置借据信息
     */
    result.setSchdInterest(BigDecimalUtil.add(result.getSchdInterest(), schdInterest)); //应还利息
    result.setEndDate(endDate); //借款结束日期

    log.info("展期试算--先息后本--试算结果为：" + result);
    return result;
  }

  /**
   * 展期试算--先息后本[上交息],
   */
  private static LoanCalculateVo bInterestAPrincipal2_delay(LoanCalculateVo data) {

    log.info("展期试算--先息后本[上交息]--要试算的数据是：" + data);
    LoanCalculateVo result = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    int currentNo = data.getCurrentExtensionNo().intValue(); //展期期数 上收息自动增加一期
    BigDecimal schdPrin = BigDecimalUtil
        .sub(data.getSchdPrin(), data.getTotPaidPrin()); //应还本金=应还本金-已还本金累计
    BigDecimal schdInterest = BigDecimalUtil
        .mul(schdPrin, data.getDelayMonthRate(), BigDecimal.valueOf(currentNo)); //应还利息

    //当期
    BigDecimal ctdInterest = BigDecimalUtil.div(schdInterest, BigDecimal.valueOf(currentNo));
    Date beginDate = data.getEndDate(); //展期开始日期是上次结束日期
    Date endDate = beginDate;

    Long dd = data.getDdDate(); //约定还款日

    List<TBizRepayPlan> list = data.getRepayPlanList();

    /**
     * 设置最后一期信息
     */
    int termNo = data.getTermNo().intValue() + data.getExtensionNo().intValue();
    TBizRepayPlan last = list.get(termNo - 1); //最后一期
    last.setCtdPrin(BigDecimal.valueOf(0.00));

    /**
     * 新增展期还款计划
     */
    BigDecimal interest = BigDecimal.valueOf(0.00);
    for (int i = 0; i < currentNo; i++) {
      TBizRepayPlan plan = TBizRepayPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      plan.setId(null);
      plan.setLoanNo(data.getId());
      plan.setTermNo(data.getTermNo() + data.getExtensionNo() + i + 1l); //当前期
      plan.setBeginDate(beginDate); //开始日期

      endDate = JodaTimeUtil.getAfterDayMonth(beginDate, 1);
      plan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
      plan.setEndDate(endDate); //结束日期
      plan.setCtdPrin(BigDecimal.valueOf(0.00)); //本期应还本金
      if (i == currentNo - 1) {
        plan.setCtdInterest(BigDecimalUtil.sub(schdInterest, interest)); //本期应还利息
      } else {
        interest = BigDecimalUtil.add(interest, ctdInterest);
        plan.setCtdInterest(ctdInterest); //本期应还利息
      }

      plan.setRate(data.getExtensionRate()); //展期利率
      plan.setInAcctNo(data.getLendingAcct()); //入账账户
      plan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
      plan.setPaidPrin(BigDecimal.valueOf(0.00));
      plan.setPaidInterest(BigDecimal.valueOf(0.00));
      plan.setPaidServFee(BigDecimal.valueOf(0.00));
      plan.setPaidPen(BigDecimal.valueOf(0.00));
      plan.setWavAmt(BigDecimal.valueOf(0.00));
      plan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
      plan.setDdNum(data.getProduct().getCycleInterval()); //计息天数

      /**
       * 还款日期 --上扣息 首期是放款日期  如果约定还款日有值取当期约定还款日，没有就是当期开始日期，最后一期还款日是借据结束日期
       */
      if (dd != null && dd.intValue() != 0) {
        plan.setDdDate(JodaTimeUtil.getDdDate(beginDate, endDate, dd.intValue()));
      } else {
        plan.setDdDate(beginDate); //还款日期
      }

      list.add(plan);
      beginDate = plan.getEndDate();//修改开始日期，为下一次循环准备

    }

    TBizRepayPlan endPlan = TBizRepayPlan.builder().build();
    BeanKit.copyProperties(data, endPlan);
    endPlan.setId(null);
    endPlan.setLoanNo(data.getId());
    endPlan.setTermNo((long) termNo + currentNo + 1); //当前期
    endPlan.setBeginDate(list.get(termNo + currentNo - 1).getBeginDate()); //开始日期
    endPlan.setEndDate(list.get(termNo + currentNo - 1).getEndDate()); //结束日期
    endPlan.setDdDate(list.get(termNo + currentNo - 1).getEndDate()); //还款日期
    endPlan.setCtdPrin(schdPrin); //本期应还本金
    endPlan.setCtdInterest(BigDecimal.valueOf(0.00)); //本期应还利息
    endPlan.setCtdServFee(BigDecimal.valueOf(0.00)); //本期应收服务费
    endPlan.setInAcctNo(data.getLendingAcct()); //入账账户
    endPlan.setCtdPen(BigDecimal.valueOf(0.00)); //本期应收罚息
    endPlan.setPaidPrin(BigDecimal.valueOf(0.00));
    endPlan.setPaidInterest(BigDecimal.valueOf(0.00));
    endPlan.setPaidServFee(BigDecimal.valueOf(0.00));
    endPlan.setPaidPen(BigDecimal.valueOf(0.00));
    endPlan.setWavAmt(BigDecimal.valueOf(0.00));
    endPlan.setStatus(RepayStatusEnum.NOT_REPAY.getValue()); //还款状态
    endPlan.setDdNum(0l); //计息天数

    list.add(endPlan); //最后一期只还本金

    result.setRepayPlanList(list);

    /**
     * 设置借据信息
     */
    result.setCurrentExtensionNo(currentNo + 1l); // 加一期
    result.setSchdInterest(BigDecimalUtil.add(result.getSchdInterest(), schdInterest)); //应还利息
    result.setEndDate(endDate); //借款结束日期

    log.info("展期试算--先息后本[上交息]--试算结果为：" + result);
    return result;

  }

  /**
   * 展期试算--等额本金
   */
  private static LoanCalculateVo equalPrincipal_delay(LoanCalculateVo loanCalculateVo) {
    //TODO
    return null;
  }

  /**
   * 展期试算--等额本息
   */
  private static LoanCalculateVo equalLoan_delay(LoanCalculateVo loanCalculateVo) {
    //TODO
    return null;
  }


}
