package com.zsy.loan.admin.core.beetl;

import com.zsy.loan.admin.core.util.KaptchaUtil;
import com.zsy.loan.service.business.system.impl.ConstantFactory;
import com.zsy.loan.utils.ToolUtil;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

  @Override
  public void initOther() {

    groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
    groupTemplate.registerFunctionPackage("tool", new ToolUtil());
    groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
    groupTemplate.registerFunctionPackage("constant", ConstantFactory.me());

  }

}
