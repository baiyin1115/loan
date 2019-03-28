package com.zsy.loan.service.factory;

import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.TransferTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import java.util.HashMap;
import java.util.Map;

/**
 * 转账业务权限工厂
 *
 * @Author zhangxh
 * @Date 2019-02-01  10:10
 */
public class TransferPermissionFactory {

  /**
   * 转账业务权限工厂
   */
  public static Map<String, Boolean> maps = new HashMap<>();

  //出账账户类型+业务类型+入账账户类型
  static {

    /**
     * 资金登记
     */
    maps.put(null + "_" + TransferTypeEnum.REGISTER + "_" + AcctTypeEnum.INTERIM_IN.getValue(), true);

    /**
     * 资金提出
     */
    maps.put(AcctTypeEnum.INTERIM_OUT.getValue() + "_" + TransferTypeEnum.WITHDRAW + "_" + null, true);

    /**
     * 资金分拨
     */
    maps.put(AcctTypeEnum.INTERIM_IN.getValue() + "_" + TransferTypeEnum.DISTRIBUTION + "_" + AcctTypeEnum.INVEST.getValue(), true);
    maps.put(AcctTypeEnum.INTERIM_IN.getValue() + "_" + TransferTypeEnum.DISTRIBUTION + "_" + AcctTypeEnum.LOAN.getValue(), true);
    maps.put(AcctTypeEnum.INTERIM_IN.getValue() + "_" + TransferTypeEnum.DISTRIBUTION + "_" + AcctTypeEnum.REPLACE.getValue(), true);

    /**
     * 补偿
     */
    maps.put(AcctTypeEnum.COMPANY.getValue() + "_" + TransferTypeEnum.SUPPLEMENT + "_" + AcctTypeEnum.LOAN.getValue(), true);

    /**
     * 贴息
     */
    maps.put(AcctTypeEnum.COMPANY.getValue() + "_" + TransferTypeEnum.DISCOUNT + "_" + AcctTypeEnum.INVEST.getValue(), true);

    /**
     * 其他
     */

  }

  /**
   * 校验动作权限
   */
  public static boolean checkPermission(String key) {

    if (maps.get(key) == null) {

      if (key.split("_")[1].equals(TransferTypeEnum.OTHER.toString())) { //其他不控制
        return true;
      }

      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "业务类型和账户不匹配");
    } else {
      return true;
    }
  }


}
