
package com.zsy.loan.dao.system;

import com.zsy.loan.bean.entity.biz.TBizCustomerInfo;
import com.zsy.loan.bean.entity.biz.TSysStatus;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统状态Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface StatusRepository extends PagingAndSortingRepository< TSysStatus, Long>
    , JpaRepository<TSysStatus, Long>, JpaSpecificationExecutor< TSysStatus> {

  @Query(nativeQuery = true, value="select t.acct_date from tb_sys_status t where t.id = 2019 ")
  Date getSysAcctDate();

  @Query(nativeQuery = true, value="select nextval(?1) ")
  long getNextVal(String key);

  @Query(nativeQuery = true, value="select t.settlement_flag from tb_sys_status t where t.id = 2019 ")
  Integer getSettlementFlag();
}
