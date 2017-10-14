package com.jskingen.carlocation.dao;

import java.util.List;

/**
 * Created by ChneY on 2017/4/28.
 */

public interface RequestDaoListener<T> {
    void onChange(List<T> t);
}
