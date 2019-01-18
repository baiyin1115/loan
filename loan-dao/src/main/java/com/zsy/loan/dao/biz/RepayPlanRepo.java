
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizRepayPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 还款计划Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface RepayPlanRepo extends PagingAndSortingRepository< TBizRepayPlan, Long>
    , JpaRepository< TBizRepayPlan, Long>, JpaSpecificationExecutor< TBizRepayPlan> {

}
