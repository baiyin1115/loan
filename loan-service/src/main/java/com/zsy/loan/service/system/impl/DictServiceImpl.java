package com.zsy.loan.service.system.impl;

import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.dao.cache.DictCache;
import com.zsy.loan.dao.system.DictRepository;
import com.zsy.loan.service.system.DictService;
import com.zsy.loan.utils.factory.MutiStrFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DictServiceImpl implements DictService {

  private Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);


  @Resource
  DictRepository dictRepository;
  @Autowired
  private DictCache dictCache;

  @Override
  public void addDict(String dictName, String dictValues) {
    //判断有没有该字典
    List<Dict> dicts = dictRepository.findByNameAndPid(dictName, 0);
    if (dicts != null && dicts.size() > 0) {
      return;
    }

    //解析dictValues
    List<Map<String, String>> items = MutiStrFactory.parseKeyValue(dictValues);

    //添加字典
    Dict dict = new Dict();
    dict.setName(dictName);
    dict.setNum("0");
    dict.setPid(0);
    this.dictRepository.save(dict);

    //添加字典条目
    for (Map<String, String> item : items) {
      String num = item.get(MutiStrFactory.MUTI_STR_KEY);
      String name = item.get(MutiStrFactory.MUTI_STR_VALUE);
      Dict itemDict = new Dict();
      itemDict.setPid(dict.getId());
      itemDict.setName(name);
      try {
        itemDict.setNum(num);
      } catch (NumberFormatException e) {
        logger.error(e.getMessage(), e);
      }
      this.dictRepository.save(itemDict);
    }
    dictCache.cache();
  }

  @Override
  public void editDict(Integer dictId, String dictName, String dicts) {
    //删除之前的字典
    this.delteDict(dictId);

    //重新添加新的字典
    this.addDict(dictName, dicts);

    dictCache.cache();
  }

  @Override
  public void delteDict(Integer dictId) {
    //删除这个字典的子词典
    List<Dict> subList = dictRepository.findByPid(dictId);
    dictRepository.delete(subList);
    //删除这个词典
    dictRepository.delete(dictId);

    dictCache.cache();
  }
}
