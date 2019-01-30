package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.dictmap.biz.LoanDict;
import com.zsy.loan.bean.entity.biz.TBizLoanInfo;
import com.zsy.loan.bean.entity.biz.TBizLoanVoucherInfo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.bean.request.LoanCalculateRequest;
import com.zsy.loan.bean.request.LoanRequest;
import com.zsy.loan.dao.biz.LoanInfoRepo;
import com.zsy.loan.dao.biz.LoanVoucherInfoRepo;
import com.zsy.loan.service.biz.impl.LoanServiceImpl;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.warpper.biz.LoanWarpper;
import com.zsy.loan.utils.BeanUtil;
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

  /**
   * 跳转到贷款管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "loan.html";
  }

  /**
   * 获取所有借据列表
   *
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
   *
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
        (List<TBizRepayPlan>) new LoanWarpper(BeanUtil.objectsToMaps(page.getRecords()))
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
  public Object add(@Valid @RequestBody LoanRequest loan, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    //借款结束日期必须在开始日期之前
    if (!DateUtil.compareDate(loan.getBeginDate(),loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    return loanService.save(loan, false);
  }

  /**
   * 跳转到修改借据
   */
  @Permission
  @RequestMapping("/to_loan_update/{loanId}")
  public String loanUpdate(@PathVariable Long loanId, Model model) {
    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

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
  public Object update(@Valid @RequestBody LoanRequest loan, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (loan.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

//    /**
//     * 修改校验
//     */
//    //公司、代偿贷款不能透支
//    if (!(loan.getAcctType().equals(AcctTypeEnum.INTERIM_IN.getValue())
//        || loan.getAcctType().equals(AcctTypeEnum.INTERIM_OUT.getValue()))
//        && loan.getBalanceType()
//        .equals(AcctBalanceTypeEnum.OVERDRAW.getValue())) {
//      throw new LoanException(BizExceptionEnum.ACCOUNT_NO_OVERDRAW,
//          String.valueOf(loan.getUserNo()));
//    }

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

//    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

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
  public Object put(@Valid @RequestBody LoanRequest loan, BindingResult error) {
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

    loanService.put(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到展期
   */
  @Permission
  @RequestMapping("/to_loan_delay/{loanId}")
  public String loanDelay(@PathVariable Long loanId, Model model) {
    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

//    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

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
  public Object delay(@Valid @RequestBody LoanRequest loan, BindingResult error) {
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

    loanService.delay(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到违约还款
   */
  @Permission
  @RequestMapping("/to_loan_breach/{loanId}")
  public String loanBreach(@PathVariable Long loanId, Model model) {
    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

//    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

    model.addAttribute("loan", loan);
    LogObjectHolder.me().set(loan);
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
  public Object breach(@Valid @RequestBody LoanRequest loan, BindingResult error) {
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

    loanService.breach(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到提前还款
   */
  @Permission
  @RequestMapping("/to_loan_prepay/{loanId}")
  public String loanPrepay(@PathVariable Long loanId, Model model) {
    TBizLoanInfo loan = loanInfoRepo.findById(loanId).get();

//    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

    model.addAttribute("loan", loan);
    LogObjectHolder.me().set(loan);
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
  public Object prepay(@Valid @RequestBody LoanRequest loan, BindingResult error) {
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
  public Object UpdateVoucher(@Valid @RequestBody LoanRequest loan, BindingResult error) {
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
    List<TBizLoanVoucherInfo> vouchers = loanVoucherInfoRepo.findByLoanNo(loanId).get();

//    loan.setRemark(loan.getRemark().trim());
//    loan.setAcctTypeName(ConstantFactory.me().getAcctTypeName(loan.getAcctType()));
//    loan.setStatusName(ConstantFactory.me().getAcctStatusName(loan.getStatus()));

    model.addAttribute("vouchers", vouchers);
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
  public Object repay(@Valid @RequestBody LoanRequest loan, BindingResult error) {
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

    loanService.repay(loan, true);
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
  public Object print(@Valid @RequestBody LoanRequest loan, BindingResult error) {
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

    loanService.repay(loan, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到客户管理首页
   */
  @RequestMapping("/loan_cust_list")
  public String loanCustList() {
    return PREFIX + "loan_customer.html";
  }


  /**
   * 试算
   */
  @BussinessLog(value = "试算", dict = LoanDict.class)
  @RequestMapping(value = "/calculate")
  @Permission
  @ResponseBody
  @ApiOperation(value = "试算", notes = "试算")
  public LoanCalculateRequest calculate(@Valid @RequestBody LoanCalculateRequest loan, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    //借款结束日期必须在开始日期之前
    if (!DateUtil.compareDate(loan.getBeginDate(),loan.getEndDate())) {
      throw new LoanException(BizExceptionEnum.LOAN_DATE, "");
    }

    return loanService.calculate(loan);
  }



}
