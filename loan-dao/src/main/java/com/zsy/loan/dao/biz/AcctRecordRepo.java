
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizAcctRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 账户资金流水Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface AcctRecordRepo extends PagingAndSortingRepository< TBizAcctRecord, Long>
    , JpaRepository< TBizAcctRecord, Long>, JpaSpecificationExecutor< TBizAcctRecord> {

}
