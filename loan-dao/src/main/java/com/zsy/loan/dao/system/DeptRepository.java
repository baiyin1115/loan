package com.zsy.loan.dao.system;


import com.zsy.loan.bean.entity.system.Dept;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface DeptRepository extends PagingAndSortingRepository<Dept, Integer> {

  List<Dept> findByPidsLike(String pid);

  @Query(nativeQuery = true, value = "SELECT id, pid AS pId, simplename AS NAME, ( CASE WHEN (pId = 0 OR pId IS NULL) THEN 'true' ELSE 'false' END ) AS isOpen FROM t_sys_dept")
  List tree();

  List<Dept> findBySimplenameLikeOrFullnameLike(String name, String name2);

  @Query(nativeQuery = true, value = "SELECT\n"
      + "\tr.id AS id,\n"
      + "\tpid AS pId,\n"
      + "\tsimplename AS NAME,\n"
      + "\t( CASE WHEN ( pId = 0 OR pId IS NULL ) THEN 'true' ELSE 'false' END ) \"open\",\n"
      + "\t( CASE WHEN ( r1.ID = 0 OR r1.ID IS NULL ) THEN 'false' ELSE 'true' END ) AS checked \n"
      + "FROM\n"
      + "\tt_sys_dept r\n"
      + "\tLEFT JOIN ( SELECT ID FROM t_sys_dept WHERE ID IN (?1) ) r1 ON r.ID = r1.ID \n"
      + "ORDER BY\n"
      + "\tpId,\n"
      + "\tnum ASC;")
  List deptTreeListByDeptId(Integer[] ids);
}
