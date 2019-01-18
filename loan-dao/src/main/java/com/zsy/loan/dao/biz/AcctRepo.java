
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz.TBizAcct;
import com.zsy.loan.bean.entity.system.Cfg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 账户Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface AcctRepo extends PagingAndSortingRepository<TBizAcct, Long>
    , JpaRepository<TBizAcct, Long>, JpaSpecificationExecutor<TBizAcct> {

}
