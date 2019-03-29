package com.zsy.loan.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2019/3/29/029.
 */
@FunctionalInterface
public interface ToBigDecimalFunction <T> {

  BigDecimal applyAsBigDecimal(T value);

}
