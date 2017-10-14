package com.jskingen.carlocation.utils;

import java.math.BigDecimal;

/**
 * Created by ChneY on 2017/5/25.
 */

public class MathUtils {
    //保留两位小数
    public static double point2(float point, int newScale) {
        BigDecimal bg = new BigDecimal(point);
        return bg.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
