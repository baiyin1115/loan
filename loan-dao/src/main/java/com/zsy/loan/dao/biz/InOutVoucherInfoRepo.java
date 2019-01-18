
package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.entity.biz. TBizInOutVoucherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 收支信息Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface InOutVoucherInfoRepo extends PagingAndSortingRepository< TBizInOutVoucherInfo, Long>
    , JpaRepository< TBizInOutVoucherInfo, Long>, JpaSpecificationExecutor< TBizInOutVoucherInfo> {

}
