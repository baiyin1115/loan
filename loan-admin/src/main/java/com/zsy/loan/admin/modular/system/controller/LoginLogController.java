package com.zsy.loan.admin.modular.system.controller;

import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.constant.Const;
import com.zsy.loan.bean.constant.factory.PageFactory;
import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.service.business.system.LoginLogService;
import com.zsy.loan.service.warpper.LogWarpper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.bean.entity.system.LoginLog;
import com.zsy.loan.dao.system.LoginLogRepository;
import com.zsy.loan.utils.factory.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志管理的控制器
 *
 * @author fengshuonan
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

  private static String PREFIX = "/system/log/";

  @Resource
  private LoginLogRepository loginLogRepository;
  @Autowired
  private LoginLogService loginlogService;

  /**
   * 跳转到日志管理的首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "login_log.html";
  }

  /**
   * 查询登录日志列表
   */
  @RequestMapping("/list")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object list(@RequestParam(required = false) String beginTime,
      @RequestParam(required = false) String endTime,
      @RequestParam(required = false) String logName) {
    Page<LoginLog> page = new PageFactory<LoginLog>().defaultPage();

    page = loginlogService.getLoginLogs(page, beginTime, endTime, logName);
    page.setRecords(
        (List<LoginLog>) new LogWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp());
    return super.packForBT(page);

  }

  /**
   * 清空日志
   */
  @BussinessLog("清空登录日志")
  @RequestMapping("/delLoginLog")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object delLog() {
    loginLogRepository.clear();
    return SUCCESS_TIP;
  }
}
