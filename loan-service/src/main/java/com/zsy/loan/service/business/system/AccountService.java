package com.zsy.loan.service.business.system;

/**
 * AccountService
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
public interface AccountService {

  String login(Long idUser);

  void logout(String token);

}
