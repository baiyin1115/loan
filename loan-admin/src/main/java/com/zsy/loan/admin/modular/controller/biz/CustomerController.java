package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.dictmap.biz.CustomerDict;
import com.zsy.loan.bean.entity.biz.TBizCustomerInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.bean.request.CustomerInfoRequest;
import com.zsy.loan.dao.biz.CustomerInfoRepo;
import com.zsy.loan.service.biz.impl.CustomerServiceImpl;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.warpper.biz.CustomerWarpper;
import com.zsy.loan.utils.BeanUtil;
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
 * 客户控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

  private String PREFIX = "/biz/customer/";

  @Resource
  CustomerInfoRepo customerInfoRepo;

  @Resource
  CustomerServiceImpl customerService;

  /**
   * 跳转到客户管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "customer.html";
  }

  /**
   * 跳转到添加客户
   */
  @RequestMapping("/customer_add")
  public String customerAdd() {
    return PREFIX + "customer_add.html";
  }

  /**
   * 跳转到修改客户
   */
  @Permission
  @RequestMapping("/customer_update/{customerId}")
  public String customerUpdate(@PathVariable Long customerId, Model model) {

    TBizCustomerInfo customer = customerInfoRepo.findById(customerId).get();

    customer.setRemark(customer.getRemark().trim());
    customer.setTypeName(ConstantFactory.me().getCustomerTypeName(customer.getType()));
    customer.setStatusName(ConstantFactory.me().getCustomerStatusName(customer.getStatus()));

    model.addAttribute("customer", customer);
    LogObjectHolder.me().set(customer);
    return PREFIX + "customer_edit.html";

  }

  /**
   * 新增客户
   */
  @BussinessLog(value = "添加客户", dict = CustomerDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  @ApiOperation(value = "新增客户", notes = "新增客户")
  public Object add(@Valid @RequestBody CustomerInfoRequest customer, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    return customerService.save(customer, false);
  }

  /**
   * 获取所有客户列表
   * 通过客户名、手机号、身份证号码 模糊查询
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "获取所有客户列表", notes = "获取所有客户列表")
  public Object list(TBizCustomerInfo condition) {

    Page<TBizCustomerInfo> page = new PageFactory<TBizCustomerInfo>().defaultPage();

    page = customerService.getTBizCustomers(page, condition);
    page.setRecords(
        (List<TBizCustomerInfo>) new CustomerWarpper(BeanUtil.objectsToMaps(page.getRecords()))
            .warp());
    return super.packForBT(page);
  }

  /**
   * 客户详情
   */
  @RequestMapping(value = "/detail/{customerId}")
  @Permission
  @ResponseBody
  @ApiOperation(value = "客户详情", notes = "客户详情")
  public Object detail(@PathVariable("customerId") Long customerId) {

    TBizCustomerInfo info = customerInfoRepo.findById(customerId).get();

    info.setTypeName(ConstantFactory.me().getCustomerTypeName(info.getType()));
    info.setStatusName(ConstantFactory.me().getCustomerStatusName(info.getStatus()));

    return info;
  }

  /**
   * 修改客户
   */
  @BussinessLog(value = "更新客户", dict = CustomerDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "修改客户", notes = "修改客户")
  public Object update(@Valid @RequestBody CustomerInfoRequest customer, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (customer.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    customerService.save(customer, true);
    return SUCCESS_TIP;
  }

  /**
   * 删除客户
   */
  @BussinessLog(value = "逻辑删除客户", dict = CustomerDict.class)
  @RequestMapping(value = "/logic_delete", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "逻辑删除客户", notes = "逻辑删除客户")
  public Object logicDelete(@RequestBody List<Long> customerIds) {

    if (customerIds == null || customerIds.size() == 0) {

      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    customerService.logicDelete(customerIds);
    return SUCCESS_TIP;
  }

  /**
   * 设置为黑名单用户
   */
  @BussinessLog(value = "设置为黑名单用户", dict = CustomerDict.class)
  @RequestMapping(value = "/set_black_list", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "设置为黑名单用户", notes = "设置为黑名单用户")
  public Object setBlackList(@RequestBody List<Long> customerIds) {

    if (customerIds == null || customerIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    customerService.setBlackList(customerIds);
    return SUCCESS_TIP;
  }

  /**
   * 取消黑名单用户
   */
  @BussinessLog(value = "取消黑名单用户", dict = CustomerDict.class)
  @RequestMapping(value = "/cancel_black_list", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "取消黑名单用户", notes = "取消黑名单用户")
  public Object cancelBlackList(@RequestBody List<Long> customerIds) {

    if (customerIds == null || customerIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    customerService.cancelBlackList(customerIds);
    return SUCCESS_TIP;
  }

}
