
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizInvestPlan;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 回款计划Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface InvestPlanRepo extends PagingAndSortingRepository< TBizInvestPlan, Long>
    , JpaRepository< TBizInvestPlan, Long>, JpaSpecificationExecutor< TBizInvestPlan> {

  List<TBizInvestPlan> findByInvestNo(Long investNo);

  @Modifying
  @Query(nativeQuery = true, value = "delete from t_biz_invest_plan where invest_no=?1")
  int deleteByInvestNo(Long investNo);
}
