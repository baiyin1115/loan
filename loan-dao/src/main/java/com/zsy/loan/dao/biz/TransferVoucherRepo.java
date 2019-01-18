
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizTransferVoucherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 转账凭证Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface TransferVoucherRepo extends PagingAndSortingRepository<  TBizTransferVoucherInfo, Long>
    , JpaRepository<  TBizTransferVoucherInfo, Long>, JpaSpecificationExecutor<  TBizTransferVoucherInfo> {

}
