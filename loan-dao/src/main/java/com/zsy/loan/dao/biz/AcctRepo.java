
package com.zsy.loan.dao.biz;

    import com.zsy.loan.bean.entity.biz.TBizAcct;
    import java.math.BigDecimal;
    import java.util.List;
    import java.util.Optional;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.PagingAndSortingRepository;
    import org.springframework.data.repository.query.Param;

/**
 * 账户Repo
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:35
 */
public interface AcctRepo extends PagingAndSortingRepository<TBizAcct, Long>
    , JpaRepository<TBizAcct, Long>, JpaSpecificationExecutor<TBizAcct> {

  @Modifying
  @Query(nativeQuery = true, value = "update t_biz_acct t set status=?2 where t.id =?1 ")
  int UpStatusById(Long id, Long status);

  Optional<TBizAcct> findByCustNo(long custNo);

  Optional<List<TBizAcct>> findByAcctType(long acctType);

  TBizAcct getByCustNo(long custNo);

  @Query(nativeQuery = true, value = "SELECT m1.id AS id,'0' AS pId,concat(m1.id,'_',m1.name) AS NAME,'false' AS isOpen FROM t_biz_acct as m1 "
      + "where acct_type in (?1) order by id ASC")
  List getTreeList(List<Long> acctTypes);

  @Modifying
  @Query(nativeQuery = true, value =
      "update t_biz_acct t set available_balance=available_balance+:upAmt, version = version+1 where t.id =:acctId AND "
          + " version = :version ")
  int upAvailableBalance(@Param("acctId") Long acctId, @Param("upAmt") BigDecimal upAmt, @Param("version") Long version);
}
