package com.zsy.loan.bean.entity.system;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_sys_status")
public class Status implements Serializable {

  private static final long serialVersionUID = -1381332258502811524L;
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "acct_date", nullable = false)
  private Date acctDate;

}
