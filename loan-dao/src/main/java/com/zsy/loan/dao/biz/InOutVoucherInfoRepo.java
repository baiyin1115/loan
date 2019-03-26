
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizInOutVoucherInfo;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * 收支信息Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface InOutVoucherInfoRepo extends PagingAndSortingRepository<TBizInOutVoucherInfo, Long>
    , JpaRepository<TBizInOutVoucherInfo, Long>, JpaSpecificationExecutor<TBizInOutVoucherInfo> {


  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_in_out_voucher_info t set acct_date=:sysAcctDate,status=:newStatus where t.id =:id ")
  int updateStatus(@Param("id") Long id, @Param("newStatus") Long newStatus, @Param("sysAcctDate") Date sysAcctDate);
}
