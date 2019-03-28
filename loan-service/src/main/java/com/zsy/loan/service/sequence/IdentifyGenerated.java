package com.zsy.loan.service.sequence;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2018/6/11/011.
 */
public enum IdentifyGenerated {
  INSTANCE;

  private Sequence sequence;

  @Autowired
  SequenceConfig sequenceConfig;

  IdentifyGenerated() {
    this.sequence = new Sequence();
  }

  public Long getNextId() {
    return sequence.nextId();
  }
}
