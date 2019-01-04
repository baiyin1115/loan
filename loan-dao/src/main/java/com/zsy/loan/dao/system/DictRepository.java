
package com.zsy.loan.dao.system;


import com.zsy.loan.bean.entity.system.Dict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DictRepository extends JpaRepository<Dict, Integer> {

  List<Dict> findByPid(Integer pid);

  List<Dict> findByNameAndPid(String name, Integer pid);

  Dict findByName(String name);

  List<Dict> findByNameLike(String name);
}
