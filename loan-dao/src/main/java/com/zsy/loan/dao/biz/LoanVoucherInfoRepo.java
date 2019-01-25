
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizLoanVoucherInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 借据凭证Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface LoanVoucherInfoRepo extends PagingAndSortingRepository< TBizLoanVoucherInfo, Long>
    , JpaRepository< TBizLoanVoucherInfo, Long>, JpaSpecificationExecutor< TBizLoanVoucherInfo> {

  Optional<List<TBizLoanVoucherInfo>> findByLoanNo(Long loanId);
}
