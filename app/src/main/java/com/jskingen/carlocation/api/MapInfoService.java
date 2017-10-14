package com.jskingen.carlocation.api;

import com.jskingen.baselib.network.MyCall;
import com.jskingen.carlocation.model.LatLngInfo;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by ChneY on 2017/5/2.
 */

public interface MapInfoService {
    //逆地理编码
//    @GET("http://restapi.amap.com/v3/geocode/regeo?key=ce6a245bad6971fc8d07bc2ddb71f0c6&radius=1000&output=json&location=119.416113,32.360461")
    @GET("http://restapi.amap.com/v3/geocode/regeo")
    MyCall<LatLngInfo> getInfo(@QueryMap Map<String, String> map);
}
