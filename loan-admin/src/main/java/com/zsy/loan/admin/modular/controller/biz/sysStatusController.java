package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.dictmap.biz.InOutDict;
import com.zsy.loan.bean.entity.biz.TSysStatus;
import com.zsy.loan.bean.entity.system.Status;
import com.zsy.loan.dao.system.StatusRepository;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统状态控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/status")
public class sysStatusController extends BaseController {

  private String PREFIX = "/biz/status/";

  @Resource
  StatusRepository repo;


  /**
   * 跳转到系统状态管理首页
   */
  @RequestMapping("")
  public String index(Model model) {

    TSysStatus sysStatus = repo.findById(2019l).get();
    model.addAttribute("sysStatus", sysStatus);

    return PREFIX + "status.html";
  }

  /**
   * 修改系统状态
   */
  @BussinessLog(value = "修改系统状态", dict = InOutDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  public Object update(@Valid @RequestBody TSysStatus sysStatus, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    TSysStatus newStatus = repo.save(sysStatus);

    return newStatus;
  }

}
