package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.base.tips.SuccessTip;
import com.zsy.loan.admin.core.base.tips.Tip;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.convey.InOutInfoVo;
import com.zsy.loan.bean.dictmap.biz.InOutDict;
import com.zsy.loan.bean.entity.biz.TBizInOutVoucherInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.dao.biz.InOutVoucherInfoRepo;
import com.zsy.loan.service.biz.impl.InOutServiceImpl;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.biz.InOutWrapper;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.factory.Page;
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
 * 收支控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/inout")
public class InOutController extends BaseController {

  private String PREFIX = "/biz/inout/";

  @Resource
  InOutVoucherInfoRepo inOutInfoRepo;

  @Resource
  InOutServiceImpl inOutService;


  /**
   * 跳转到收支管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "inout.html";
  }

  /**
   * 跳转到添加收支
   */
  @RequestMapping("/inout_add")
  public String inOutAdd() {
    return PREFIX + "inout_add.html";
  }

  /**
   * 跳转到修改收支
   */
  @Permission
  @RequestMapping("/to_inout_update/{inoutId}")
  public String inOutUpdate(@PathVariable Long inoutId, Model model) {
    InOutInfoVo inout = InOutInfoVo.builder().build();
    BeanKit.copyProperties(inOutInfoRepo.findById(inoutId).get(), inout);

    inout.setAcctName(ConstantFactory.me().getAcctName(inout.getAcctNo()));
    inout.setRemark(inout.getRemark() == null ? "" : inout.getRemark().trim());

    model.addAttribute("inout", inout);
    LogObjectHolder.me().set(inout);
    return PREFIX + "inout_edit.html";

  }

  /**
   * 新增收支
   */
  @BussinessLog(value = "添加收支", dict = InOutDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  public Object add(@Valid @RequestBody InOutInfoVo inOut, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    return inOutService.save(inOut, false);
  }

  /**
   * 获取所有收支列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  public Object list(InOutInfoVo condition) {

    Page<TBizInOutVoucherInfo> page = new PageFactory<TBizInOutVoucherInfo>().defaultPage();

    page = inOutService.getTBizInOutVouchers(page, condition);
    page.setRecords(
        (List<TBizInOutVoucherInfo>) new InOutWrapper(BeanUtil.objectsToMaps(page.getRecords()))
            .wrap());
    return super.packForBT(page);
  }

  /**
   * 收支详情
   */
  @RequestMapping(value = "/detail/{inoutId}")
  @Permission
  @ResponseBody
  public Object detail(@PathVariable("inoutId") Long inOutId) {
    return inOutInfoRepo.findById(inOutId).get();
  }

  /**
   * 修改收支
   */
  @BussinessLog(value = "更新收支", dict = InOutDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  public Object update(@Valid @RequestBody InOutInfoVo inOut, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (inOut.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    inOutService.save(inOut, true);
    return SUCCESS_TIP;
  }

  /**
   * 删除收支
   */
  @BussinessLog(value = "删除收支", dict = InOutDict.class)
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object delete(@RequestBody List<Long> inOutIds) {

    if (inOutIds == null || inOutIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    inOutService.delete(inOutIds);
    return SUCCESS_TIP;

  }

  /**
   * 确认检查
   */
  @BussinessLog(value = "确认检查", dict = InOutDict.class)
  @RequestMapping(value = "/to_confirm", method = RequestMethod.POST)
  @ResponseBody
  @OpLog
  public Object toConfirm(@RequestBody List<Long> inOutIds) {
    String resultMsg = inOutService.toConfirm(inOutIds);

    Tip tip = new SuccessTip();
    tip.setCode(200);
    tip.setMessage(resultMsg);

    return tip;

  }

  /**
   * 确认收支
   */
  @BussinessLog(value = "确认收支", dict = InOutDict.class)
  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object confirm(@RequestBody List<Long> inOutIds) {

    if (inOutIds == null || inOutIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    inOutService.confirm(inOutIds);
    return SUCCESS_TIP;

  }

  /**
   * 撤销收支
   */
  @BussinessLog(value = "撤销收支", dict = InOutDict.class)
  @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object cancel(@RequestBody List<Long> inOutIds) {

    if (inOutIds == null || inOutIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    inOutService.cancel(inOutIds);
    return SUCCESS_TIP;

  }

}
