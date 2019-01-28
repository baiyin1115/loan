package com.zsy.loan.service.system;

import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.bean.entity.system.Dict;
import java.util.List;

/**
 * 当前登录用户所拥有的信息
 *
 * @Author zhangxh
 * @Date 2019-01-21  13:19
 */
public interface IUserOwnFactory {

  /**
   * 获取当前用户所属的公司
   */
  public List<Dict> getOrgsByUser(Long userId);

  public List<Dict> getOrgsByToken(String token);

  public List<Dict> getOrgList();

}
