package com.zsy.loan.bean.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "t_sys_dict")
public class Dict implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Basic
  @Column(name = "num")
  private String num;

  @Basic
  @Column(name = "pid")
  private Integer pid;

  @Basic
  @Column(name = "name")
  private String name;

  @Basic
  @Column(name = "tips")
  private String tips;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Dict tSysDict = (Dict) o;

    if (id != null ? !id.equals(tSysDict.id) : tSysDict.id != null) {
      return false;
    }
    if (num != null ? !num.equals(tSysDict.num) : tSysDict.num != null) {
      return false;
    }
    if (pid != null ? !pid.equals(tSysDict.pid) : tSysDict.pid != null) {
      return false;
    }
    if (name != null ? !name.equals(tSysDict.name) : tSysDict.name != null) {
      return false;
    }
    if (tips != null ? !tips.equals(tSysDict.tips) : tSysDict.tips != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (num != null ? num.hashCode() : 0);
    result = 31 * result + (pid != null ? pid.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (tips != null ? tips.hashCode() : 0);
    return result;
  }
}
