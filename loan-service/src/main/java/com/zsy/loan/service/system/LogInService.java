package com.zsy.loan.service.system;

/**
 * LogInService
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
public interface LogInService {

  String login(Long idUser);

  void logout(String token);

}
