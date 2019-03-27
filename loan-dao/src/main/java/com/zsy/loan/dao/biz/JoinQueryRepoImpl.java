package com.zsy.loan.dao.biz;

import com.zsy.loan.bean.convey.AcctVo;
import com.zsy.loan.bean.entity.biz.TBizAcctPopup;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 多表联合查询共同
 *
 * @Author zhangxh
 * @Date 2019-03-27  16:51
 */

@Component
public class JoinQueryRepoImpl {

  @Autowired
  private EntityManager entityManager;

//  /** 查询省份访问量数据 **/
//  private final static String SELECT_CHINA_MAP_SQL = ""
//      + "select "
//      + "		taa.num as num, map.province_name as provinceName "
//      + "from ( "
//      + "		select "
//      + "			sum(pv_count+visitor_count) as num, province_id	"
//      + "		from area "
//      + "		where "
//      + "			create_time BETWEEN :startTime and :endTime	"
//      + "		group by province_id	"
//      + ") as taa left join province as map on taa.province_id=map.province_id		";
//
//  public Map<String, Integer> selectChinaMapData(String startTime, String endTime) {
//
//    Query query = entityManager.createNativeQuery(SELECT_CHINA_MAP_SQL)
//        .setParameter("startTime", startTime)
//        .setParameter("endTime", endTime);
//
//    query.unwrap(SQLQuery.class)
//        .addScalar("num", IntegerType.INSTANCE)
//        .addScalar("provinceName", StringType.INSTANCE)
//        .setResultTransformer(Transformers.aliasToBean(ChinaMapDto.class));
//    List<ChinaMapDto> chinaMapDtoList = query.getResultList();
//
//    Map<String, Integer> map = new HashMap<>();
//    if(!CollectionUtils.isEmpty(chinaMapDtoList)) {
//      for (ChinaMapDto chinaMapDto : chinaMapDtoList) {
//        map.put(chinaMapDto.getProvinceName(), chinaMapDto.getNum());
//      }
//    }
//
//    return map;
//  }
//
//  /**
//   * 多表查询，原始查询的方式（native sql），并且按以前hibernate方式返回
//   * @return
//   */
//  public Page<DemoStudent> pageStudentClassSqlMapResult(Pageable pageable, String name, String age) {
//    StringBuilder countSelectSql = new StringBuilder();
//    countSelectSql.append("select count(1)  from demo_student s "+
//        " left join  demo_class c on s.class_guid=c.class_guid where 1=1 ");
//
//    StringBuilder selectSql = new StringBuilder();
//
//    selectSql.append("select {s.*},{c.*}  from demo_student s "+
//        " left join  demo_class c on s.class_guid=c.class_guid where 1=1 ");
//
//    Map<String,Object> params = new HashMap<>();
//    StringBuilder whereSql = new StringBuilder();
//    if(StringUtils.isNotBlank(name)){
//      whereSql.append(" and name=:name ");
//      params.put("name",name);
//    }
//    if(StringUtils.isNotBlank(age)){
//      whereSql.append(" and age=:age ");
//      params.put("age",age);
//    }
//
//    String countSql = new StringBuilder().append(countSelectSql).append(whereSql).toString();
//    Query countQuery = entityManager.createNativeQuery(countSql);
//
//
//    for(Map.Entry<String,Object> entry:params.entrySet()){
//      countQuery.setParameter(entry.getKey(),entry.getValue());
//    }
//    BigInteger totalCount = (BigInteger)countQuery.getSingleResult();
//
//    String querySql = new StringBuilder().append(selectSql).append(whereSql).toString();
//
//    // select s.*,c.* 这种，两个表有相同字段的，因为第二个表的对应字段会用第一个表的对应字段，数据信息不对。
//    //Query query = this.entityManager.createNativeQuery(querySql,"StudentResults");
//    Query query = this.entityManager.createNativeQuery(querySql);
//    for(Map.Entry<String,Object> entry:params.entrySet()){
//      query.setParameter(entry.getKey(),entry.getValue());
//    }
//    query.setFirstResult((int)pageable.getOffset());
//    query.setMaxResults(pageable.getPageSize());
//
//    //query.unwrap(SQLQuery.class).addEntity("s",DemoStudent.class).addEntity("c",DemoClass.class);
//    query.unwrap(NativeQuery.class).addEntity("s",DemoStudent.class).addEntity("c",DemoClass.class);
//    List result =query.getResultList();//是object[]数组，第一个元素是demo_student对象，第二个元素是demo_class对象
//    List<DemoStudent> lsStudent=new ArrayList <DemoStudent>();
//    for (Object row : result) {
//      DemoStudent demoStudent=(DemoStudent)((Object[])row)[0];
//      DemoClass demoClass=(DemoClass)((Object[])row)[1];
//      demoStudent.setDemoClass(demoClass);
//      lsStudent.add(demoStudent);
//    }
//    Page<DemoStudent> pageDemoStudent=new PageImpl < DemoStudent >(lsStudent,pageable,totalCount.longValue());
//    return pageDemoStudent;
//  }

//  @org.springframework.data.jpa.repository.Query(nativeQuery = true, value = "select * from goods where codeName like CONCAT('%',?1,'%') and order by  ?w#{#pageable}")
//  Page<TBizAcctPopup> findAcctPopup(@Param("custCertNo") String custCertNo, @Param("custName") String custName,
//      @Param("custMobile") String custMobile, @Param("acctType") Long acctType, Pageable pageable);

  /**
   * 客户和账户级联查询
   */
  public Page<TBizAcctPopup> findAcctPopup(AcctVo condition, Pageable pageable) {

    StringBuilder countSelectSql = new StringBuilder();
    countSelectSql.append(" SELECT    count(1)                     ");
    countSelectSql.append(" FROM                                   ");
    countSelectSql.append(" 	t_biz_acct m,                        ");
    countSelectSql.append(" 	t_biz_customer_info n                ");
    countSelectSql.append(" WHERE                                  ");
    countSelectSql.append(" 	m.cust_no = n.id                     ");

    StringBuilder selectSql = new StringBuilder();
    selectSql.append(" SELECT                                 ");
    selectSql.append(" 	m.*,                                 ");
    selectSql.append(" 	n.cert_no AS cust_cert_no,           ");
    selectSql.append(" 	n.cert_type AS cust_cert_type,       ");
    selectSql.append(" 	n.NAME AS cust_name,                 ");
    selectSql.append(" 	n.sex AS cust_sex,                   ");
    selectSql.append(" 	n.mobile AS cust_mobile,             ");
    selectSql.append(" 	n.phone AS cust_phone,               ");
    selectSql.append(" 	n.email AS cust_email,               ");
    selectSql.append(" 	n.type AS cust_type,                 ");
    selectSql.append(" 	n.STATUS AS cust_status              ");
    selectSql.append(" FROM                                   ");
    selectSql.append(" 	t_biz_acct m,                        ");
    selectSql.append(" 	t_biz_customer_info n                ");
    selectSql.append(" WHERE                                  ");
    selectSql.append(" 	m.cust_no = n.id                     ");

    Map<String, Object> params = new HashMap<>();
    StringBuilder whereSql = new StringBuilder();
    if (!ObjectUtils.isEmpty(condition.getAcctType())) {
      whereSql.append(" AND m.acct_type = :acctType ");
      params.put("acctType", condition.getAcctType());
    }
    if (!StringUtils.isEmpty(condition.getCustCertNo())) {
      whereSql.append(" AND n.cert_no = :custCertNo ");
      params.put("custCertNo", condition.getCustCertNo());
    }
    if (!StringUtils.isEmpty(condition.getCustName())) {
      whereSql.append(" AND n.name like :custName ");
      params.put("custName", condition.getCustName() + "%");
    }
    if (!StringUtils.isEmpty(condition.getCustMobile())) {
      whereSql.append(" AND n.mobile = :custMobile ");
      params.put("custMobile", condition.getCustMobile());
    }
    if (!ObjectUtils.isEmpty(condition.getStatsList())) {
      whereSql.append(" AND n.status in (:status) ");
      params.put("status", condition.getStatsList());
    }

    String orderSql = "  order by m.id asc ";

    String countSql = new StringBuilder().append(countSelectSql).append(whereSql).toString();
    Query countQuery = entityManager.createNativeQuery(countSql);

    for (Map.Entry<String, Object> entry : params.entrySet()) {
      countQuery.setParameter(entry.getKey(), entry.getValue());
    }
    BigInteger totalCount = (BigInteger) countQuery.getSingleResult();

    String querySql = new StringBuilder().append(selectSql).append(whereSql).append(orderSql).toString();

    // select s.*,c.* 这种，两个表有相同字段的，因为第二个表的对应字段会用第一个表的对应字段，数据信息不对。
    //Query query = this.entityManager.createNativeQuery(querySql,"StudentResults");
    Query query = this.entityManager.createNativeQuery(querySql, TBizAcctPopup.class);
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    List<TBizAcctPopup> resultList = query.getResultList();
    Page<TBizAcctPopup> page = new PageImpl<>(resultList, pageable, totalCount.longValue());

    return page;
  }


}