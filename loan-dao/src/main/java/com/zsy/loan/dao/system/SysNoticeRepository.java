package com.zsy.loan.dao.system;


import com.zsy.loan.bean.entity.system.Notice;
import com.zsy.loan.bean.entity.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface SysNoticeRepository extends PagingAndSortingRepository<Notice, Integer>
    , JpaRepository<Notice, Integer>, JpaSpecificationExecutor<Role> {

  List<Notice> findByTitleLike(String name);
}
