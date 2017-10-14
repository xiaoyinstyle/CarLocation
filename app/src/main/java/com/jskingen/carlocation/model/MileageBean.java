package com.jskingen.carlocation.model;


import com.jskingen.baselib.network.model.HttpResult;

/**
 * Created by ChneY on 2017/4/8.
 */

public class MileageBean extends HttpResult {
    /**
     * mileage : 0.00
     */
    private String mileage;

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
}
