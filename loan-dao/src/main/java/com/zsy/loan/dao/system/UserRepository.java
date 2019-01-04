package com.zsy.loan.dao.system;


import com.zsy.loan.bean.entity.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface UserRepository extends JpaRepository<User, Integer>,
    JpaSpecificationExecutor<User> {

  User findByAccount(String account);
}
