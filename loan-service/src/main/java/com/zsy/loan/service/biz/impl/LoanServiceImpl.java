package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.LoanBreachVo;
import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.bean.convey.LoanDelayVo;
import com.zsy.loan.bean.convey.LoanPrepayVo;
import com.zsy.loan.bean.convey.LoanPutVo;
import com.zsy.loan.bean.convey.LoanRepayPlanVo;
import com.zsy.loan.bean.convey.LoanVo;
import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.biz.LoanInfoRepo;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.dao.biz.RepayPlanRepo;
import com.zsy.loan.service.factory.LoanTrialCalculateFactory;
import com.zsy.loan.service.sequence.IdentifyGenerated;
import com.zsy.loan.service.system.ISystemService;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateUtil;
import com.zsy.loan.utils.JodaTimeUtil;
import com.zsy.loan.utils.StringUtils;
import com.zsy.loan.utils.factory.Page;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 贷款服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class LoanServiceImpl extends BaseServiceImpl {

  @Autowired
  private LoanInfoRepo repository;

  @Autowired
  private RepayPlanRepo repayPlanRepo;

  @Autowired
  private ProductInfoRepo productRepo;

  @Autowired
  private ISystemService systemService;

  public Boolean save(LoanVo loan, boolean b) {

    /**
     * 判断状态
     */
    if (b) { //修改
      TBizLoanInfo loanInfo = repository.findById(loan.getId()).get();
      if (!loanInfo.getStatus().equals(LoanStatusEnum.CHECK_IN.getValue())) { //登记
        throw new LoanException(BizExceptionEnum.LOAN_NOT_CHECK_IN, String.valueOf(loan.getId()));
      }
    }

    /**
     * 试算
     */
    LoanCalculateVo calculateRequest = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.LOAN_CHECK_IN.getValue()); //设置业务类型
    LoanCalculateVo result = calculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!loan.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }
    if (loan.getReceiveInterest().compareTo(result.getReceiveInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
    }
    if (loan.getServiceFee().compareTo(result.getServiceFee()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "服务费不一致");
    }

    TBizLoanInfo info = TBizLoanInfo.builder().build();
    BeanKit.copyProperties(loan, info);

    /**
     * 赋值
     */
    info.setExtensionNo(0l);
    info.setExtensionRate(BigDecimal.valueOf(0.00));
    info.setAcctDate(systemService.getSysAcctDate());
    info.setStatus(LoanStatusEnum.CHECK_IN.getValue()); //登记
    info.setSchdPrin(result.getPrin()); // 应还本金
    info.setSchdInterest(result.getReceiveInterest()); // 应还利息
    info.setSchdServFee(result.getServiceFee()); // 应收服务费

    info.setSchdPen(BigDecimal.valueOf(0.00)); // 逾期罚息累计
    info.setTotPaidPrin(BigDecimal.valueOf(0.00)); // 已还本金累计
    info.setTotPaidInterest(BigDecimal.valueOf(0.00)); // 已还利息累计
    info.setTotPaidServFee(BigDecimal.valueOf(0.00)); // 已收服务费累计
    info.setTotPaidPen(BigDecimal.valueOf(0.00)); // 已还罚息累计
    info.setTotWavAmt(BigDecimal.valueOf(0.00)); // 减免金额累计

    if(!b){
      info.setId(IdentifyGenerated.INSTANCE.getNextId()); //修改为统一的凭证编号规则
    }

    repository.save(info);

    return true;
  }

  @Transactional
  public Boolean delete(List<Long> loanIds) {

    for (long loanId : loanIds) {
      /**
       * 判断账户状态
       */
      TBizLoanInfo loanInfo = repository.findById(loanId).get();
      if (!loanInfo.getStatus().equals(LoanStatusEnum.CHECK_IN.getValue())) { //登记
        throw new LoanException(BizExceptionEnum.LOAN_NOT_CHECK_IN, String.valueOf(loanId));
      }

      repository.deleteById(loanId);

    }
    return true;

  }

  /**
   * 放款
   */
  @Transactional
  public Boolean put(LoanPutVo loan, boolean b) {

    /**
     * 锁记录
     */
    TBizLoanInfo old = repository.lockRecordByIdStatus(loan.getId(), loan.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING,
          loan.getId() + "_" + loan.getStatus());
    }

    /**
     * 试算
     */
    LoanCalculateVo calculateRequest = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.PUT.getValue()); //设置业务类型
    LoanCalculateVo result = calculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!loan.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }
    if (loan.getReceiveInterest().compareTo(result.getReceiveInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
    }
    if (loan.getServiceFee().compareTo(result.getServiceFee()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "服务费不一致");
    }

    TBizLoanInfo info = TBizLoanInfo.builder().build();
    BeanKit.copyProperties(loan, info);

    /**
     * 修改凭证信息
     */
    info.setStatus(LoanStatusEnum.PUT.getValue()); //已放款
    info.setAcctDate(systemService.getSysAcctDate());
    repository
        .put(info.getId(), LoanStatusEnum.PUT.getValue(), info.getRemark(), info.getLendingDate(), result.getDdDate(), systemService.getSysAcctDate(),
            result.getTotPaidServFee());

    /**
     * 先删除后插保存还款计划信息
     */
    repayPlanRepo.deleteByLoanNo(info.getId());
    repayPlanRepo.saveAll(result.getRepayPlanList());

    /**
     * 调用账户模块记账
     */
    String key =  LoanBizTypeEnum.PUT + "_" + "";
    executeAccounting(key,result);

    return true;

  }

  /**
   * 展期
   */
  @Transactional
  public Boolean delay(LoanDelayVo loan, boolean b) {
    /**
     * 锁记录
     */
    TBizLoanInfo old = repository.lockRecordByIdStatus(loan.getId(), loan.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING,
          loan.getId() + "_" + loan.getStatus());
    }

    /**
     * 试算
     */
    LoanCalculateVo calculateRequest = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.DELAY.getValue()); //设置业务类型
    LoanCalculateVo result = calculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!loan.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }
    if (loan.getReceiveInterest().compareTo(result.getReceiveInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
    }
    if (loan.getServiceFee().compareTo(result.getServiceFee()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "服务费不一致");
    }

    /**
     * 调整还款计划
     */
    //更新最后一期
    int termNo = result.getTermNo().intValue() + old.getExtensionNo().intValue();
    TBizRepayPlan endPlan = result.getRepayPlanList().get(termNo - 1);
    repayPlanRepo.save(endPlan);

    //插入展期的还款计划
    List<TBizRepayPlan> subList = result.getRepayPlanList()
        .subList(result.getRepayPlanList().size() - result.getCurrentExtensionNo().intValue(),
            result.getRepayPlanList().size());
    repayPlanRepo.saveAll(subList);

    /**
     * 修改凭证信息
     */
    repository.delay(old.getId(), LoanStatusEnum.DELAY.getValue(), loan.getRemark().trim(),
        result.getEndDate(),
        result.getSchdInterest(), systemService.getSysAcctDate(),
        result.getCurrentExtensionNo() + old.getExtensionNo(), //展期期数等于上次和本次的和
        result.getExtensionRate());

    return true;
  }

  /**
   * 提前还款
   */
  @Transactional
  public void prepay(@Valid LoanPrepayVo loan, boolean b) {

    /**
     * 锁记录
     */
    TBizLoanInfo old = repository.lockRecordByIdStatus(loan.getId(), loan.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING,
          loan.getId() + "_" + loan.getStatus());
    }

    /**
     * 试算
     */
    LoanCalculateVo calculateRequest = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);

    //借据状态校验
    if (loan.getCurrentRepayPrin()
        .compareTo(BigDecimalUtil.sub(loan.getRepayAmt(), loan.getRepayInterest(), loan.getRepayPen(), loan.getRepayServFee())) == 0) { //提前结清
      calculateRequest.setBizType(LoanBizTypeEnum.PREPAYMENT.getValue()); //设置业务类型
    } else {
      calculateRequest.setBizType(LoanBizTypeEnum.PART_REPAYMENT.getValue()); //设置业务类型
    }

    LoanCalculateVo result = prepayCalculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!loan.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }

    /**
     * 调整还款计划
     */
    //更新当前期
    List<TBizRepayPlan> currentRepayPlans = result.getCurrentRepayPlans();
    repayPlanRepo.saveAll(currentRepayPlans);

    //更新当前期后续还款还款计划
    List<TBizRepayPlan> subList = result.getAfterPayRecords();
    repayPlanRepo.saveAll(subList);

    /**
     * 修改凭证信息
     */
    repository.prepay(old.getId(), result.getStatus(), loan.getRemark().trim(),
        systemService.getSysAcctDate(), result.getTotPaidPrin(), result.getTotPaidInterest(), result.getTotPaidPen(), result.getTotPaidServFee(),
        result.getTotWavAmt(), result.getSchdInterest());

    /**
     * 调用账户模块记账
     */
    String key =  calculateRequest.getBizType() + "_" + "";
    executeAccounting(key,result);

  }

  /**
   * 提前还款试算
   */
  public LoanCalculateVo prepayCalculate(LoanCalculateVo loan) {
    /**
     *设置共同数据信息
     */
    setCalculateCommon(loan);

    //当前期还款计划
    List<TBizRepayPlan> currentRepayPlans = repayPlanRepo.findCurrentTermRecord(loan.getId(), systemService.getSysAcctDate());
    if (currentRepayPlans == null || currentRepayPlans.size() == 0) {
      throw new LoanException(BizExceptionEnum.NOT_FOUND, "未查询到当前期还款计划");
    }
    loan.setCurrentRepayPlans(currentRepayPlans);

    //当前期以前未还的还款计划 5:待还，4:已逾期
    Long currentTermNo = currentRepayPlans.get(0).getTermNo();
    List<Long> status = new ArrayList<>(2);
    status.add(RepayStatusEnum.NOT_REPAY.getValue());
    status.add(RepayStatusEnum.OVERDUE.getValue());
    List<TBizRepayPlan> notPayRecords = repayPlanRepo.findNotPayRecord(loan.getId(), status, currentTermNo);
    if (notPayRecords != null && notPayRecords.size() != 0) {
      throw new LoanException(BizExceptionEnum.STATUS_ERROR, "有未还的还款计划，不能提前还款");
    }

    List<TBizRepayPlan> afterPayRecords = repayPlanRepo.findAfterPayRecord(loan.getId(), currentTermNo);
    loan.setAfterPayRecords(afterPayRecords);

    return executeCalculate(loan);

  }

  /**
   * 取得提前还款数据信息
   */
  public LoanCalculateVo getPrepayInfo(Long loanId) {

    //借据
    TBizLoanInfo loan = repository.findById(loanId).get();

    /**
     * 试算
     */
    LoanCalculateVo calculateRequest = LoanCalculateVo.builder().build();
    BeanKit.copyProperties(loan, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.PREPAYMENT.getValue()); //设置业务类型
    LoanCalculateVo result = prepayCalculate(calculateRequest); //试算结果

    return result;
  }

  /**
   * 还款
   */
  @Transactional
  public void repay(LoanRepayPlanVo plan, boolean b) {

    /**
     * 锁记录
     */
    TBizRepayPlan old = repayPlanRepo.lockRecordByIdStatus(plan.getId(), plan.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING,
          plan.getId() + "_" + plan.getStatus());
    }

    BigDecimal currentPrin = plan.getCurrentPrin();        // 录入本金
    BigDecimal currentInterest = plan.getCurrentInterest();    // 录入利息
    BigDecimal currentPen = plan.getCurrentPen();         // 录入罚息
    BigDecimal currentWav = plan.getCurrentWav();         // 录入减免
    BigDecimal currentServFee = plan.getCurrentServFee();         // 录入服务费

    BigDecimal ctdPrin = old.getCtdPrin();                // 本期应还本金
    BigDecimal ctdInterest = old.getCtdInterest();        // 本期应还利息
    BigDecimal ctdServFee = old.getCtdServFee();          // 本期应收服务费
    BigDecimal ctdPen = old.getCtdPen();                  // 本期应收罚息
    BigDecimal paidPrin = old.getPaidPrin();              // 本期已还本金
    BigDecimal paidInterest = old.getPaidInterest();      // 本期已还利息
    BigDecimal paidServFee = old.getPaidServFee();        // 本期已收服务费
    BigDecimal paidPen = old.getPaidPen();                // 本期已收罚息
    BigDecimal wavAmt = old.getWavAmt();                  // 本期减免

    BigDecimal current = BigDecimalUtil.add(currentInterest, currentServFee, currentPen); //费用
    BigDecimal schdPrin = BigDecimalUtil.sub(ctdPrin, paidPrin); //应还本金=本期应还本金-本期已还本金
    BigDecimal sctdInterest = BigDecimalUtil.sub(ctdInterest, paidInterest); //应还利息=本期应还利息-本期已还利息
    BigDecimal sctdServFee = BigDecimalUtil.sub(ctdServFee, paidServFee); //应收服务费=本期应收服务费-本期已收服务费
    BigDecimal sctdPen = BigDecimalUtil.sub(ctdPen, paidPen); //应收罚息=本期应收罚息-本期已收罚息

    if (currentPrin.compareTo(schdPrin) != 0) {
      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "当前用户录入本金需要等于待还的本金");
    }

    if (current.compareTo(BigDecimalUtil.add(sctdInterest, sctdServFee, sctdPen)) < 0) {
      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "费用>=应还相关项的和");
    }
    if (currentWav.compareTo(BigDecimalUtil.add(sctdInterest, sctdServFee, sctdPen)) > 0) {
      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "减免<=应还相关项的和");
    }

    /**
     * 调用记账接口记账
     */
    String key =  LoanBizTypeEnum.REPAY + "_" + "";
    executeAccounting(key,plan);

    /**
     * 更新还款计划
     */
    TBizRepayPlan upInfo = TBizRepayPlan.builder().build();
    BeanKit.copyProperties(old, upInfo);

    upInfo.setPaidPrin(BigDecimalUtil.add(paidPrin, currentPrin));
    upInfo.setPaidInterest(BigDecimalUtil.add(paidInterest, currentInterest));
    upInfo.setPaidServFee(BigDecimalUtil.add(paidServFee, currentServFee));
    upInfo.setPaidPen(BigDecimalUtil.add(paidPen, currentPen));
    upInfo.setWavAmt(BigDecimalUtil.add(wavAmt, currentWav));
    upInfo.setAcctDate(systemService.getSysAcctDate());
    upInfo.setStatus(RepayStatusEnum.REPAID.getValue());

    repayPlanRepo.save(upInfo);

    /**
     * 更新凭证信息
     */
    TBizLoanInfo loan = repository.getOne(old.getLoanNo());
    Long status;
    if (loan.getExtensionNo() + loan.getTermNo() == old.getTermNo()) { //最后一期
      status = LoanStatusEnum.SETTLE.getValue();
    } else {
      status = LoanStatusEnum.REPAY_IND.getValue();
    }
    repository.repay(plan.getLoanNo(), systemService.getSysAcctDate(), status, currentPrin, currentInterest, currentPen, currentServFee,
        currentWav, plan.getRemark());

  }

  /**
   * 违约还款
   */
  @Transactional
  public void breach(LoanBreachVo loan, boolean b) {

    /**
     * 锁记录
     */
    TBizLoanInfo old = repository.lockRecordByIdStatus(loan.getId(), loan.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING,
          loan.getId() + "_" + loan.getStatus());
    }

    BigDecimal currentBreachPrin = loan.getCurrentBreachPrin();        // 录入本金
    BigDecimal currentBreachFee = loan.getCurrentBreachFee();    // 录入费用
    Long compensationAcct = loan.getCompensationAcct();         // 代偿账户

    BigDecimal schdPrin = BigDecimalUtil.sub(old.getSchdPrin(), old.getTotPaidPrin());

    if (currentBreachPrin.compareTo(schdPrin) > 0) {
      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "当前用户录入本金不能大于待还的本金");
    }

    /**
     * 调用记账接口记账
     */
    String key =  LoanBizTypeEnum.COMPENSATION + "_" + "";
    executeAccounting(key,loan);

    /**
     * 更新还款计划
     */
    //当前期以前未还的还款计划 5:待还，4:已逾期
    List<Long> status = new ArrayList<>(2);
    status.add(RepayStatusEnum.NOT_REPAY.getValue());
    status.add(RepayStatusEnum.OVERDUE.getValue());
    List<TBizRepayPlan> notPayRecords = repayPlanRepo.findNotPayRecord(loan.getId(), status);
    if (notPayRecords == null || notPayRecords.size() == 0) {
      throw new LoanException(BizExceptionEnum.NOT_FOUND, "未查询到还款计划");
    }

    for (int i = 0; i < notPayRecords.size(); i++) {
      TBizRepayPlan upInfo = notPayRecords.get(i);
      if (i == 0) {
        upInfo.setPaidPrin(BigDecimalUtil.add(upInfo.getPaidPrin(), currentBreachPrin));
        upInfo.setPaidPen(BigDecimalUtil.add(upInfo.getPaidPen(), currentBreachFee));
        upInfo.setExternalAcct(String.valueOf(compensationAcct)); //设置为代偿账户
      }
      upInfo.setAcctDate(systemService.getSysAcctDate());
      upInfo.setStatus(RepayStatusEnum.COMPENSATION.getValue());
    }

    repayPlanRepo.saveAll(notPayRecords);

    /**
     * 更新凭证信息
     */
    repository.repay(old.getId(), systemService.getSysAcctDate(), LoanStatusEnum.COMPENSATION.getValue(), currentBreachPrin, BigDecimal.valueOf(0.00),
        currentBreachFee,
        BigDecimal.valueOf(0.00),
        BigDecimal.valueOf(0.00), loan.getRemark());
  }

  public void updateVoucher(LoanVo loan, boolean b) {
  }

  /**
   * 试算--登记、放款、展期试算
   */
  public LoanCalculateVo calculate(LoanCalculateVo loan) {

    /**
     * 根据还款方式、本金、利率、服务费收取方式、服务费比例、开始结束日期计算
     * 利息、服务费、放款金额、期数、应还本金、应还利息、应收服务费、还款计划相关信息
     */
    //设置共同信息
    setCalculateCommon(loan);

    /**
     * 查询下还款计划
     */
    List<TBizRepayPlan> repayPlans = repayPlanRepo.findByLoanNo(loan.getId());
    loan.setRepayPlanList(repayPlans);

    return executeCalculate(loan);

  }

  /**
   * 设置试算的共同数据信息
   */
  private void setCalculateCommon(LoanCalculateVo loan) {
    loan.setDayRate(BigDecimalUtil
        .div(loan.getRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN)); //日利息
    loan.setMonthRate(BigDecimalUtil
        .div(loan.getRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN)); //月利息
    loan.setDay(JodaTimeUtil.daysBetween(loan.getBeginDate(), loan.getEndDate())); //相差天数
    loan.setMonth(JodaTimeUtil.getMonthFloor(loan.getBeginDate(), loan.getEndDate())); //相差月数
    loan.setProduct(productRepo.findById(loan.getProductNo()).get()); //产品信息

    if (loan.getExtensionRate() != null) {
      loan.setDelayDayRate(BigDecimalUtil
          .div(loan.getExtensionRate(), BigDecimal.valueOf(360), 6,
              BigDecimal.ROUND_HALF_EVEN)); //展期日利息
      loan.setDelayMonthRate(BigDecimalUtil
          .div(loan.getExtensionRate(), BigDecimal.valueOf(12), 6,
              BigDecimal.ROUND_HALF_EVEN)); //展期月利息
    }

    loan.setAcctDate(systemService.getSysAcctDate()); //设置当前账务日期

    if (loan.getPenRate() != null) {
      loan.setPenDayRate(BigDecimalUtil
          .div(loan.getPenRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN)); //罚息日利息
      loan.setPenMonthRate(BigDecimalUtil
          .div(loan.getPenRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN)); //罚息月利息
    }
  }


  public LoanCalculateVo executeCalculate(LoanCalculateVo loan) {

    LoanCalculateVo result = LoanTrialCalculateFactory.maps
        .get(RepayTypeEnum.getEnumByKey(loan.getRepayType()) + "_" + LoanBizTypeEnum
            .getEnumByKey(loan.getBizType())).apply(loan);

    return result;
  }


  /**
   * 查询借据list
   */
  public Page<TBizLoanInfo> getTBLoanPages(Page<TBizLoanInfo> page, LoanVo condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.desc("status"));
    orders.add(Order.desc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizLoanInfo> page1 = repository
        .findAll(new Specification<TBizLoanInfo>() {

          @Override
          public Predicate toPredicate(Root<TBizLoanInfo> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            if (!ObjectUtils.isEmpty(condition.getCustNo())) {
              list.add(criteriaBuilder.equal(root.get("custNo"), condition.getCustNo()));
            }
            if (StringUtils.isNotEmpty(condition.getContrNo())) {
              list.add(criteriaBuilder
                  .like(root.get("contrNo").as(String.class), condition.getContrNo().trim() + "%"));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryBeginDate())) {
              list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryBeginDate())));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryEndDate())) {
              list.add(criteriaBuilder.lessThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryEndDate())));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }


  /**
   * 查询还款计划list
   */
  public Page<TBizRepayPlan> getPlanPages(Page<TBizRepayPlan> page, TBizRepayPlan condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.desc("status"));
    orders.add(Order.asc("ddDate"));
    orders.add(Order.asc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizRepayPlan> page1 = repayPlanRepo
        .findAll(new Specification<TBizRepayPlan>() {


          @Override
          public Predicate toPredicate(Root<TBizRepayPlan> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            //设置借据编号
            if (!ObjectUtils.isEmpty(condition.getLoanNo())) {
              list.add(criteriaBuilder.equal(root.get("loanNo"), condition.getLoanNo()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }
}
