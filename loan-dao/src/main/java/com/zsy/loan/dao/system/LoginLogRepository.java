package com.zsy.loan.dao.system;

import com.zsy.loan.bean.entity.system.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface LoginLogRepository extends PagingAndSortingRepository<LoginLog, Integer>
    , JpaRepository<LoginLog, Integer>, JpaSpecificationExecutor<LoginLog> {

  @Modifying
  @Transactional
  @Query(nativeQuery = true, value = "delete from t_sys_login_log")
  int clear();
}
