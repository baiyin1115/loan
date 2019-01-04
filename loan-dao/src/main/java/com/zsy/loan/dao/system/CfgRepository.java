
package com.zsy.loan.dao.system;

import com.zsy.loan.bean.entity.system.Cfg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CfgRepository extends PagingAndSortingRepository<Cfg, Long>
    , JpaRepository<Cfg, Long>, JpaSpecificationExecutor<Cfg> {

  Cfg findByCfgName(String cfgName);
}
