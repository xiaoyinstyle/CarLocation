package com.jskingen.carlocation.api;

import com.jskingen.baselib.network.MyCall;
import com.jskingen.baselib.network.model.HttpResult;
import com.jskingen.carlocation.model.FindMileageBean;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by ChneY on 2017/4/6.
 */

public interface StatisticsService {
//http://192.168.0.234:8081/meters/gpsdata/piecewisemileage?userid=1&starttime=2017-05-01&endtime=2017-05-30
    @POST("gpsdata/piecewisemileage")
    MyCall<HttpResult<FindMileageBean>> findMileage(@QueryMap Map<String, String> map);
}
