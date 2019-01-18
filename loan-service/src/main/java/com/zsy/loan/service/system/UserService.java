package com.zsy.loan.service.system;

import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.utils.factory.Page;

import java.util.List;
import java.util.Map;

/**
 * Created  on 2018/3/23 0023.
 *
 * @author enilu
 */
public interface UserService {

  List<User> findAll(Map<String, Object> params);

  Page<User> findPage(Page<User> page, Map<String, Object> params);
}
