package com.zsy.loan.service.system;

import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.CustomerTypeEnum;
import java.util.Date;

/**
 * Created by Administrator on 2019/1/23/023.
 */
public interface ISystemService {

  /**
   * 取得系统时间
   * @return
   */
  Date getSysAcctDate();

  /**
   * 取得结转状态
   * @return
   */
  Integer getSettlementFlag();

  /**
   * 取得下一个账户号
   * @return
   */
  long getNextAcctNO(AcctTypeEnum type);

  /**
   * 取得下一个客户号
   * @return
   */
  long getNextCustomerNO(CustomerTypeEnum type);

}
