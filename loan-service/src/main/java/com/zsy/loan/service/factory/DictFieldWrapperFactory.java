package com.zsy.loan.service.factory;

import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;

import com.zsy.loan.service.system.IConstantFactory;
import com.zsy.loan.service.system.impl.ConstantFactory;
import java.lang.reflect.Method;

/**
 * 字段的包装创建工厂
 *
 * @author fengshuonan
 * @date 2017-05-06 15:12
 */
public class DictFieldWrapperFactory {

  public static Object createFieldWrapper(Object field, String methodName) {
    IConstantFactory me = ConstantFactory.me();
    try {
      Method method = IConstantFactory.class.getMethod(methodName, field.getClass());
      Object result = method.invoke(me, field);
      return result;
    } catch (Exception e) {
      try {
        Method method = IConstantFactory.class.getMethod(methodName, Integer.class);
        Object result = method.invoke(me, Integer.parseInt(field.toString()));
        return result;
      } catch (Exception e1) {
        throw new LoanException(BizExceptionEnum.ERROR_WRAPPER_FIELD);
      }
    }
  }

}
