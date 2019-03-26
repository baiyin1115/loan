
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizTransferVoucherInfo;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * 转账凭证Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface TransferVoucherRepo extends PagingAndSortingRepository<TBizTransferVoucherInfo, Long>
    , JpaRepository<TBizTransferVoucherInfo, Long>, JpaSpecificationExecutor<TBizTransferVoucherInfo> {

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_transfer_voucher_info t set acct_date=:sysAcctDate,status=:newStatus where t.id =:id "
      + " and status=:oldStatus ")
  int updateStatus(@Param("id") Long id, @Param("newStatus") Long newStatus, @Param("oldStatus") Long oldStatus,
      @Param("sysAcctDate") Date sysAcctDate);
}
