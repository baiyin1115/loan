package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.convey.LoanBreachVo;
import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.bean.convey.LoanDelayVo;
import com.zsy.loan.bean.convey.LoanPrepayVo;
import com.zsy.loan.bean.convey.LoanPutVo;
import com.zsy.loan.bean.convey.LoanRepayPlanVo;
import com.zsy.loan.bean.convey.LoanVo;
import com.zsy.loan.bean.dictmap.biz.LoanDict;
import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.entity.biz.TBizLoanVoucherInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.dao.biz.LoanInfoRepo;
import com.zsy.loan.dao.biz.LoanVoucherInfoRepo;
import com.zsy.loan.dao.biz.RepayPlanRepo;
import com.zsy.loan.service.biz.impl.LoanServiceImpl;
import com.zsy.loan.service.factory.LoanStatusFactory;
import com.zsy.loan.service.factory.RepayPlanStatusFactory;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.system.impl.SystemServiceImpl;
import com.zsy.loan.service.warpper.biz.LoanWarpper;
import com.zsy.loan.service.warpper.biz.RepayPlanWarpper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateTimeKit;
import com.zsy.loan.utils.DateUtil;
import com.zsy.loan.utils.factory.Page;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 贷款控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/loan")
public class LoanController extends BaseController {

  private String PREFIX = "/biz/loan/";

  @Resource
  LoanInfoRepo loanInfoRepo;

  @Resource
  LoanServiceImpl loanService;

  @Resource
  LoanVoucherInfoRepo loanVoucherInfoRepo;

  @Resource
  RepayPlanRepo repayPlanRepo;

  /**
   * 跳转到贷款管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "loan.html";
  }

  /**
   * 获取所有借据列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "获取所有借据列表", notes = "获取所有借据列表")
  public Object list(TBizLoanInfo condition) {

    Page<TBizLoanInfo> page = new PageFactory<TBizLoanInfo>().defaultPage();

    page = loanService.getTBLoanPages(page, condition);
    page.setRecords(
        (List<TBizLoanInfo>) new LoanWarpper(BeanUtil.objectsToMaps(page.getRecords()))
            .warp());
    return super.packForBT(page);
  }

  /**
   * 获取还款计划列表
   */
  @RequestMapping(value = "/loan_repay_plan_list")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "获取还款计划列表", notes = "获取还款计划列表")
  public Object repayPlanList(TBizRepayPlan condition) {

    Page<TBizRepayPlan> page = new PageFactory<TBizRepayPlan>().defaultPage();

    page = loanService.getPlanPages(page, condition);
    page.setRecords(
        (List<TBizRepayPlan>) new RepayPlanWarpper(BeanUtil.objectsToMaps(page.getRecords()))
            .warp());
    return super.packForBT(page);
  }

  /**
   * 借据详情
   */
  @RequestMapping(value = "/detail/{loanId}")
  @Permission
  @ResponseBody
  @ApiOperation(value = "借据详情", notes = "借据详情")
  public Object detail(@PathVariable("loanId") Long loanId) {
    return loanInfoRepo.findById(loanId).get();
  }

  /**
   * 跳转到添加借据
   */
  @RequestMapping("/loan_add")
  public String loanAdd() {
    return PREFIX + "loan_add.html";
  }

  /**
   * 登记借据
   */
  @BussinessLog(value = "登记借据", dict = LoanDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  @ApiOperation(value = "登记借据", notes = "登记借据")
  public Object add(@Valid @RequestBody LoanVo loan, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    return loanService.save(loan, false);
  }


  /**
   * 登记试算
   */
  @BussinessLog(value = "登记试算", dict = LoanDict.class)
  @RequestMapping(value = "/calculate")
  @Permission
  @ResponseBody
  @ApiOperation(value = "登记试算", notes = "登记试算")
  public LoanCalculateVo calculate(@Valid @RequestBody LoanCalculateVo loan,
      BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    loan.setLoanBizType(LoanBizTypeEnum.LOAN_CHECK_IN.getValue()); //设置业务类型
    return loanService.calculate(loan);
  }

  /**
   * 跳转到修改借据
   */
  @Permission
  @RequestMapping("/to_loan_update/{loanId}")
  public String loanUpdate(@PathVariable Long loanId, Model model) {
    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

    loan.setRemark(loan.getRemark() == null ? "" : loan.getRemark().trim());
    loan.setProductName(ConstantFactory.me().getProductName(loan.getProductNo()));
    loan.setCustName(ConstantFactory.me().getCustomerName(loan.getCustNo()));
    loan.setLendingAcctName(ConstantFactory.me().getAcctName(loan.getLendingAcct()));

    model.addAttribute("loan", loan);
    LogObjectHolder.me().set(loan);
    return PREFIX + "loan_edit.html";

  }

  /**
   * 修改借据
   */
  @BussinessLog(value = "更新借据", dict = LoanDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "修改借据", notes = "修改借据")
  public Object update(@Valid @RequestBody LoanVo loan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    /**
     * 校验
     */
    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    loanService.save(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 删除借据
   */
  @BussinessLog(value = "删除借据", dict = LoanDict.class)
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "删除借据", notes = "删除借据")
  public Object logicDelete(@RequestBody List<Long> loanIds) {

    if (loanIds == null || loanIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    loanService.delete(loanIds);
    return SUCCESS_TIP;
  }


  /**
   * 跳转到放款
   */
  @Permission
  @RequestMapping("/to_loan_put/{loanId}")
  public String loanPut(@PathVariable Long loanId, Model model) {

    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PUT.getValue());

    //设置中文信息
    setLoanNameMsg(loan);

    model.addAttribute("loan", loan);
    LogObjectHolder.me().set(loan);
    return PREFIX + "loan_put.html";

  }

  /**
   * 放款
   */
  @BussinessLog(value = "放款", dict = LoanDict.class)
  @RequestMapping(value = "/put")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "放款", notes = "放款")
  public Object put(@Valid @RequestBody LoanPutVo loan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    /**
     * 校验
     */
    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    //放款日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getLendingDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_LENDING_DATE, "");
    }

    //借据状态校验
    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PUT.getValue());

    loanService.put(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 放款试算
   */
  @BussinessLog(value = "放款试算", dict = LoanDict.class)
  @RequestMapping(value = "/put_calculate")
  @ResponseBody
  @ApiOperation(value = "放款试算", notes = "放款试算")
  public LoanCalculateVo putCalculate(@Valid @RequestBody LoanCalculateVo loan,
      BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    //放款日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getLendingDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_LENDING_DATE, "");
    }

    loan.setLoanBizType(LoanBizTypeEnum.PUT.getValue()); //设置业务类型

    //借据状态校验
    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PUT.getValue());

    LoanCalculateVo calculateVo = loanService.calculate(loan);

    StringBuffer tmp = new StringBuffer();
    tmp.append("本金：" + BigDecimalUtil.formatAmt(calculateVo.getSchdPrin()));
    tmp.append(",利息：" + BigDecimalUtil.formatAmt(calculateVo.getSchdInterest()));
    tmp.append(",服务费：" + BigDecimalUtil.formatAmt(calculateVo.getSchdServFee()));
    tmp.append(",放款金额：" + BigDecimalUtil.formatAmt(calculateVo.getLendingAmt()));
    tmp.append(",放款日期：" + DateTimeKit.formatDate(calculateVo.getLendingDate()));
    tmp.append("<BR>");

    for (TBizRepayPlan plan : calculateVo.getRepayPlanList()) {
      tmp.append("--------------------------------------------------------------<BR>");
      tmp.append("第：" + plan.getTermNo() + "期");
      tmp.append(",还款日期：" + DateTimeKit.formatDate(plan.getDdDate()));
      tmp.append(",本期应还本金：" + BigDecimalUtil.formatAmt(plan.getCtdPrin()));
      tmp.append(",本期应还利息：" + BigDecimalUtil.formatAmt(plan.getCtdInterest()));
      tmp.append(",本期应收服务费：" + BigDecimalUtil.formatAmt(plan.getCtdServFee()));
      tmp.append("<BR>");
    }
    calculateVo.setResultMsg(tmp.toString());

    return calculateVo;
  }

  /**
   * 跳转到展期
   */
  @Permission
  @RequestMapping("/to_loan_delay/{loanId}")
  public String loanDelay(@PathVariable Long loanId, Model model) {

    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.DELAY.getValue());

    //设置中文信息
    setLoanNameMsg(loan);

    model.addAttribute("loan", loan);
    LogObjectHolder.me().set(loan);

    return PREFIX + "loan_delay.html";

  }

  /**
   * 展期
   */
  @BussinessLog(value = "展期", dict = LoanDict.class)
  @RequestMapping(value = "/delay")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "展期", notes = "展期")
  public Object delay(@Valid @RequestBody LoanDelayVo loan, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    //放款日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getLendingDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_LENDING_DATE, "");
    }

    if (RepayTypeEnum.A_DEBT_SERVICE_DUE.getValue() == loan.getRepayType()
        && loan.getCurrentExtensionNo() > 1) {
      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, " 一次性还本付息展期数不能超过1");
    }

    //借据状态校验
    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.DELAY.getValue());

    loanService.delay(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 展期试算
   */
  @BussinessLog(value = "展期试算", dict = LoanDict.class)
  @RequestMapping(value = "/delay_calculate")
  @ResponseBody
  @ApiOperation(value = "展期试算", notes = "展期试算")
  public LoanCalculateVo delayCalculate(@Valid @RequestBody LoanCalculateVo loan,
      BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    //放款日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getLendingDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_LENDING_DATE, "");
    }

    loan.setLoanBizType(LoanBizTypeEnum.DELAY.getValue()); //设置业务类型

    //借据状态校验
    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.DELAY.getValue());

    LoanCalculateVo calculateVo = loanService.calculate(loan);

    StringBuffer tmp = new StringBuffer();
    tmp.append("应还本金：" + BigDecimalUtil.formatAmt(calculateVo.getSchdPrin()));
    tmp.append(",应还利息：" + BigDecimalUtil.formatAmt(calculateVo.getSchdInterest()));
    tmp.append(",应收服务费：" + BigDecimalUtil.formatAmt(calculateVo.getSchdServFee()));
    tmp.append(",应收罚息：" + BigDecimalUtil.formatAmt(calculateVo.getSchdPen()));
    tmp.append("<BR>");

    tmp.append("已还本金累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidPrin()));
    tmp.append(",已还利息累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidInterest()));
    tmp.append(",已收服务费累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidServFee()));
    tmp.append(",已还罚息累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidPen()));
    tmp.append(",减免金额累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotWavAmt()));
    tmp.append("<BR>");

    for (TBizRepayPlan plan : calculateVo.getRepayPlanList()) {
      tmp.append("--------------------------------------------------------------<BR>");
      tmp.append("第：" + plan.getTermNo() + "期");
      tmp.append(",还款日期：" + DateTimeKit.formatDate(plan.getDdDate()));
      tmp.append(",本期应还本金：" + BigDecimalUtil.formatAmt(plan.getCtdPrin()));
      tmp.append(",本期应还利息：" + BigDecimalUtil.formatAmt(plan.getCtdInterest()));
      tmp.append(",本期应收服务费：" + BigDecimalUtil.formatAmt(plan.getCtdServFee()));
      tmp.append(",本期应收罚息：" + BigDecimalUtil.formatAmt(plan.getCtdPen()));
      tmp.append(",状态：" + ConstantFactory.me().getRepayStatusName(plan.getStatus()));
      tmp.append("<BR>");
    }
    calculateVo.setResultMsg(tmp.toString());

    return calculateVo;
  }

  /**
   * 跳转到提前还款
   */
  @Permission
  @RequestMapping("/to_loan_prepay/{loanId}")
  public String loanPrepay(@PathVariable Long loanId, Model model) {

    LoanCalculateVo loan = loanService.getPrepayInfo(loanId);

    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PREPAYMENT.getValue());

    //设置中文信息
    setLoanNameMsg(loan);

    model.addAttribute("loan", loan);

    return PREFIX + "loan_prepay.html";

  }

  /**
   * 提前还款
   */
  @BussinessLog(value = "提前还款", dict = LoanDict.class)
  @RequestMapping(value = "/prepay")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "提前还款", notes = "提前还款")
  public Object prepay(@Valid @RequestBody LoanPrepayVo loan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    //借据状态校验
    if (loan.getCurrentRepayPrin()
        .compareTo(BigDecimalUtil.sub(loan.getRepayAmt(), loan.getRepayInterest(), loan.getRepayPen(), loan.getRepayServFee())) == 0) { //提前结清
      LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PREPAYMENT.getValue());
    } else {
      LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PART_REPAYMENT.getValue());
    }

    loanService.prepay(loan, true);

    return SUCCESS_TIP;
  }

  /**
   * 提前还款试算
   */
  @BussinessLog(value = "提前还款试算", dict = LoanDict.class)
  @RequestMapping(value = "/prepay_calculate")
  @ResponseBody
  @ApiOperation(value = "提前还款试算", notes = "提前还款试算")
  public LoanCalculateVo prepayCalculate(@Valid @RequestBody LoanCalculateVo loan,
      BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(loan.getBeginDate(), loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    //借据状态校验
    if (loan.getCurrentRepayPrin()
        .compareTo(BigDecimalUtil.sub(loan.getRepayAmt(), loan.getRepayInterest(), loan.getRepayPen(), loan.getRepayServFee())) == 0) { //提前结清
      LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PREPAYMENT.getValue());
    } else {
      LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.PART_REPAYMENT.getValue());
    }

    loan.setLoanBizType(LoanBizTypeEnum.PREPAYMENT.getValue()); //设置业务类型

    LoanCalculateVo calculateVo = loanService.prepayCalculate(loan);

    StringBuffer tmp = new StringBuffer();
    tmp.append("应还本金：" + BigDecimalUtil.formatAmt(calculateVo.getSchdPrin()));
    tmp.append(",应还利息：" + BigDecimalUtil.formatAmt(calculateVo.getSchdInterest()));
    tmp.append(",应收服务费：" + BigDecimalUtil.formatAmt(calculateVo.getSchdServFee()));
    tmp.append(",应收罚息：" + BigDecimalUtil.formatAmt(calculateVo.getSchdPen()));
    tmp.append(",退回金额：" + BigDecimalUtil.formatAmt(calculateVo.getBackAmt()));
    tmp.append("<BR>");

    tmp.append("已还本金累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidPrin()));
    tmp.append(",已还利息累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidInterest()));
    tmp.append(",已收服务费累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidServFee()));
    tmp.append(",已还罚息累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotPaidPen()));
    tmp.append(",减免金额累计：" + BigDecimalUtil.formatAmt(calculateVo.getTotWavAmt()));
    tmp.append(",借据状态：" + ConstantFactory.me().getLoanStatusName(calculateVo.getStatus()));
    tmp.append("<BR>");

    for (TBizRepayPlan plan : calculateVo.getCurrentRepayPlans()) {
      tmp.append("--------------------------------------------------------------<BR>");
      tmp.append("第：" + plan.getTermNo() + "期");
      tmp.append(",还款日期：" + DateTimeKit.formatDate(plan.getDdDate()));
      tmp.append(",本期应还本金：" + BigDecimalUtil.formatAmt(plan.getCtdPrin()));
      tmp.append(",本期应还利息：" + BigDecimalUtil.formatAmt(plan.getCtdInterest()));
      tmp.append(",本期应收服务费：" + BigDecimalUtil.formatAmt(plan.getCtdServFee()));
      tmp.append(",本期应收罚息：" + BigDecimalUtil.formatAmt(plan.getCtdPen()));
      tmp.append(",本期已还本金：" + BigDecimalUtil.formatAmt(plan.getPaidPrin()));
      tmp.append(",本期已还利息：" + BigDecimalUtil.formatAmt(plan.getPaidInterest()));
      tmp.append(",本期已收服务费：" + BigDecimalUtil.formatAmt(plan.getPaidServFee()));
      tmp.append(",本期已收罚息：" + BigDecimalUtil.formatAmt(plan.getPaidPen()));
      tmp.append(",本期减免：" + BigDecimalUtil.formatAmt(plan.getWavAmt()));
      tmp.append(",计息天数：" + plan.getDdNum());
      tmp.append(",状态：" + ConstantFactory.me().getRepayStatusName(plan.getStatus()));
      tmp.append("<BR>");
    }
    for (TBizRepayPlan plan : calculateVo.getAfterPayRecords()) {
      tmp.append("--------------------------------------------------------------<BR>");
      tmp.append("第：" + plan.getTermNo() + "期");
      tmp.append(",还款日期：" + DateTimeKit.formatDate(plan.getDdDate()));
      tmp.append(",本期应还本金：" + BigDecimalUtil.formatAmt(plan.getCtdPrin()));
      tmp.append(",本期应还利息：" + BigDecimalUtil.formatAmt(plan.getCtdInterest()));
      tmp.append(",本期应收服务费：" + BigDecimalUtil.formatAmt(plan.getCtdServFee()));
      tmp.append(",本期应收罚息：" + BigDecimalUtil.formatAmt(plan.getCtdPen()));
      tmp.append(",本期已还本金：" + BigDecimalUtil.formatAmt(plan.getPaidPrin()));
      tmp.append(",本期已还利息：" + BigDecimalUtil.formatAmt(plan.getPaidInterest()));
      tmp.append(",本期已收服务费：" + BigDecimalUtil.formatAmt(plan.getPaidServFee()));
      tmp.append(",本期已收罚息：" + BigDecimalUtil.formatAmt(plan.getPaidPen()));
      tmp.append(",本期减免：" + BigDecimalUtil.formatAmt(plan.getWavAmt()));
      tmp.append(",计息天数：" + plan.getDdNum());
      tmp.append(",状态：" + ConstantFactory.me().getRepayStatusName(plan.getStatus()));
      tmp.append("<BR>");
    }
    calculateVo.setResultMsg(tmp.toString());

    return calculateVo;
  }

  /**
   * 跳转到违约还款
   */
  @Permission
  @RequestMapping("/to_loan_breach/{loanId}")
  public String loanBreach(@PathVariable Long loanId, Model model) {

    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

//    LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.COMPENSATION.getValue());

    //设置中文信息
    setLoanNameMsg(loan);

    model.addAttribute("loan", loan);

    return PREFIX + "loan_breach.html";

  }

  /**
   * 违约还款
   */
  @BussinessLog(value = "违约还款", dict = LoanDict.class)
  @RequestMapping(value = "/breach")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "违约还款", notes = "违约还款")
  public Object breach(@Valid @RequestBody LoanBreachVo loan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    /**
     * 修改校验
     */
    //LoanStatusFactory.checkCurrentStatus(loan.getStatus() + "_" + LoanBizTypeEnum.COMPENSATION.getValue());

    loanService.breach(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到原始凭证管理
   */
  @Permission
  @RequestMapping("/to_loan_update_voucher/{loanId}")
  public String toUpdateVoucher(@PathVariable Long loanId, Model model) {
    List<TBizLoanVoucherInfo> vouchers = loanVoucherInfoRepo.findByLoanNo(loanId).get();

//    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

    model.addAttribute("vouchers", vouchers);
    return PREFIX + "loan_update_voucher.html";

  }

  /**
   * 原始凭证管理
   */
  @BussinessLog(value = "原始凭证管理", dict = LoanDict.class)
  @RequestMapping(value = "/loan_update_voucher")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "原始凭证管理", notes = "原始凭证管理")
  public Object UpdateVoucher(@Valid @RequestBody LoanVo loan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    /**
     * 修改校验
     */

    loanService.updateVoucher(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到还款
   */
  @Permission
  @RequestMapping("/to_loan_repay/{loanId}")
  public String toLoanRepay(@PathVariable Long loanId, Model model) {
    TBizRepayPlan plan = repayPlanRepo.findById(loanId).get();

    RepayPlanStatusFactory.checkCurrentStatus(plan.getStatus() + "_" + LoanBizTypeEnum.REPAY.getValue());

    plan.setRemark(plan.getRemark().trim());
    plan.setOrgName(ConstantFactory.me().getDeptName(plan.getOrgNo().intValue()));
    plan.setStatusName(ConstantFactory.me().getRepayStatusName(plan.getStatus()));
    plan.setCustName(ConstantFactory.me().getCustomerName(plan.getCustNo()));
    plan.setInAcctName(ConstantFactory.me().getAcctName(plan.getInAcctNo()));

    model.addAttribute("plan", plan);
    return PREFIX + "loan_repay.html";

  }

  /**
   * 还款
   */
  @BussinessLog(value = "还款", dict = LoanDict.class)
  @RequestMapping(value = "/repay")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "还款", notes = "还款")
  public Object repay(@Valid @RequestBody LoanRepayPlanVo plan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (plan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //还款日期必须在当前账务日期之前
    if (!DateUtil.compareDate(plan.getDdDate(), SystemServiceImpl.me().getSysAcctDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DD_DATE, "");
    }

    RepayPlanStatusFactory.checkCurrentStatus(plan.getStatus() + "_" + LoanBizTypeEnum.REPAY.getValue());

//    if(1==1) {
//      return SUCCESS_TIP;
//    }

    /**
     * 修改校验
     */

    loanService.repay(plan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到打印
   */
  @Permission
  @RequestMapping("/to_loan_print/{loanId}")
  public String toLoanPrint(@PathVariable Long loanId, Model model) {
    List<TBizLoanVoucherInfo> vouchers = loanVoucherInfoRepo.findByLoanNo(loanId).get();

//    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

    model.addAttribute("vouchers", vouchers);
    return PREFIX + "loan_repay.html";

  }

  /**
   * 打印
   */
  @BussinessLog(value = "打印", dict = LoanDict.class)
  @RequestMapping(value = "/print")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "打印", notes = "打印")
  public Object print(@Valid @RequestBody LoanVo loan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    /**
     * 修改校验
     */

//    loanService.repay(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到客户选择页面
   */
  @RequestMapping("/loan_cust_list")
  public String loanCustList() {
    return PREFIX + "loan_customer.html";
  }

  private void setLoanNameMsg(TBizLoanInfo loan) {

    //获取名称
    loan.setRemark(loan.getRemark() == null ? "" : loan.getRemark().trim());
    loan.setProductName(ConstantFactory.me().getProductName(loan.getProductNo()));
    loan.setCustName(ConstantFactory.me().getCustomerName(loan.getCustNo()));
    loan.setLendingAcctName(ConstantFactory.me().getAcctName(loan.getLendingAcct()));
    loan.setOrgName(ConstantFactory.me().getDeptName(loan.getOrgNo().intValue()));
    loan.setProductName(ConstantFactory.me().getProductName(loan.getProductNo()));
    loan.setCustName(ConstantFactory.me().getCustomerName(loan.getCustNo()));
    loan.setLendingAcctName(ConstantFactory.me().getAcctName(loan.getLendingAcct()));
    loan.setLoanTypeName(ConstantFactory.me().getLoanTypeName(loan.getLoanType()));
    loan.setServiceFeeTypeName(
        ConstantFactory.me().getServiceFeeTypeName(loan.getServiceFeeType()));
    loan.setRepayTypeName(ConstantFactory.me().getRepayTypeName(loan.getRepayType()));
    loan.setIsPenName(ConstantFactory.me().getIsPenName(loan.getIsPen()));
    loan.setPenNumberName(ConstantFactory.me().getPenNumberName(loan.getPenNumber()));
    loan.setStatusName(ConstantFactory.me().getLoanStatusName(loan.getStatus()));
  }

  private void setLoanNameMsg(LoanCalculateVo calculateVo) {
    //获取名称
    calculateVo.setRemark(calculateVo.getRemark() == null ? "" : calculateVo.getRemark().trim());
    calculateVo.setProductName(ConstantFactory.me().getProductName(calculateVo.getProductNo()));
    calculateVo.setCustName(ConstantFactory.me().getCustomerName(calculateVo.getCustNo()));
    calculateVo.setLendingAcctName(ConstantFactory.me().getAcctName(calculateVo.getLendingAcct()));
    calculateVo.setOrgName(ConstantFactory.me().getDeptName(calculateVo.getOrgNo().intValue()));
    calculateVo.setProductName(ConstantFactory.me().getProductName(calculateVo.getProductNo()));
    calculateVo.setCustName(ConstantFactory.me().getCustomerName(calculateVo.getCustNo()));
    calculateVo.setLendingAcctName(ConstantFactory.me().getAcctName(calculateVo.getLendingAcct()));
    calculateVo.setLoanTypeName(ConstantFactory.me().getLoanTypeName(calculateVo.getLoanType()));
    calculateVo.setServiceFeeTypeName(
        ConstantFactory.me().getServiceFeeTypeName(calculateVo.getServiceFeeType()));
    calculateVo.setRepayTypeName(ConstantFactory.me().getRepayTypeName(calculateVo.getRepayType()));
    calculateVo.setIsPenName(ConstantFactory.me().getIsPenName(calculateVo.getIsPen()));
    calculateVo.setPenNumberName(ConstantFactory.me().getPenNumberName(calculateVo.getPenNumber()));
    calculateVo.setStatusName(ConstantFactory.me().getLoanStatusName(calculateVo.getStatus()));
  }
}
