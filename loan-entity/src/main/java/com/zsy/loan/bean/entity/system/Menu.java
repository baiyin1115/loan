package com.zsy.loan.bean.entity.system;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity
@Table(name = "t_sys_menu")
public class Menu implements Serializable {

  private static final long serialVersionUID = -344155474895067320L;
  private Long id;
  private String code;
  private String pcode;
  private String pcodes;
  private String name;
  private String icon;
  private String url;
  private Integer num;
  private Integer levels;
  private Integer ismenu;
  private String tips;
  private Integer status;
  private Integer isopen;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  @Column(name = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  @Column(name = "pcode")
  public String getPcode() {
    return pcode;
  }

  public void setPcode(String pcode) {
    this.pcode = pcode;
  }


  @Column(name = "pcodes")
  public String getPcodes() {
    return pcodes;
  }

  public void setPcodes(String pcodes) {
    this.pcodes = pcodes;
  }


  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Column(name = "icon")
  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }


  @Column(name = "url")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  @Column(name = "num")
  public Integer getNum() {
    return num;
  }

  public void setNum(Integer num) {
    this.num = num;
  }


  @Column(name = "levels")
  public Integer getLevels() {
    return levels;
  }

  public void setLevels(Integer levels) {
    this.levels = levels;
  }


  @Column(name = "ismenu")
  public Integer getIsmenu() {
    return ismenu;
  }

  public void setIsmenu(Integer ismenu) {
    this.ismenu = ismenu;
  }


  @Column(name = "tips")
  public String getTips() {
    return tips;
  }

  public void setTips(String tips) {
    this.tips = tips;
  }


  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


  @Column(name = "isopen")
  public Integer getIsopen() {
    return isopen;
  }

  public void setIsopen(Integer isopen) {
    this.isopen = isopen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Menu tSysMenu = (Menu) o;

    if (id != null ? !id.equals(tSysMenu.id) : tSysMenu.id != null) {
      return false;
    }
    if (code != null ? !code.equals(tSysMenu.code) : tSysMenu.code != null) {
      return false;
    }
    if (pcode != null ? !pcode.equals(tSysMenu.pcode) : tSysMenu.pcode != null) {
      return false;
    }
    if (pcodes != null ? !pcodes.equals(tSysMenu.pcodes) : tSysMenu.pcodes != null) {
      return false;
    }
    if (name != null ? !name.equals(tSysMenu.name) : tSysMenu.name != null) {
      return false;
    }
    if (icon != null ? !icon.equals(tSysMenu.icon) : tSysMenu.icon != null) {
      return false;
    }
    if (url != null ? !url.equals(tSysMenu.url) : tSysMenu.url != null) {
      return false;
    }
    if (num != null ? !num.equals(tSysMenu.num) : tSysMenu.num != null) {
      return false;
    }
    if (levels != null ? !levels.equals(tSysMenu.levels) : tSysMenu.levels != null) {
      return false;
    }
    if (ismenu != null ? !ismenu.equals(tSysMenu.ismenu) : tSysMenu.ismenu != null) {
      return false;
    }
    if (tips != null ? !tips.equals(tSysMenu.tips) : tSysMenu.tips != null) {
      return false;
    }
    if (status != null ? !status.equals(tSysMenu.status) : tSysMenu.status != null) {
      return false;
    }
    if (isopen != null ? !isopen.equals(tSysMenu.isopen) : tSysMenu.isopen != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (code != null ? code.hashCode() : 0);
    result = 31 * result + (pcode != null ? pcode.hashCode() : 0);
    result = 31 * result + (pcodes != null ? pcodes.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (icon != null ? icon.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    result = 31 * result + (num != null ? num.hashCode() : 0);
    result = 31 * result + (levels != null ? levels.hashCode() : 0);
    result = 31 * result + (ismenu != null ? ismenu.hashCode() : 0);
    result = 31 * result + (tips != null ? tips.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (isopen != null ? isopen.hashCode() : 0);
    return result;
  }
}
