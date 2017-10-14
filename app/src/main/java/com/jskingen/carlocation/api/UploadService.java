package com.jskingen.carlocation.api;

import com.jskingen.baselib.network.MyCall;
import com.jskingen.baselib.network.model.HttpResult;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ChneY on 2017/4/8.
 */

public interface UploadService {
//    // 上传定位   http://192.168.0.234:8081/meters/gpsdata/syncgps?userid=3&project=1&lng=119.395661&lat=32.374378
//    @POST("gpsdata/syncgps")
//    DjCall<HttpResult> uploadLocation(@QueryMap Map<String, String> map);
//
//    //获取总里程数 http://192.168.0.234:8081/meters/gpsdata/projectmileage?userid=2&project=1
//    @POST("gpsdata/projectmileage")
//    DjCall<MileageBean> getDistance(@QueryMap Map<String, String> map);

    //图片上传 http://192.168.0.234:8081/meters/gpsdata/uploadphoto?userid=3&project=1&photo=
    @POST("gpsdata/uploadphoto")
    MyCall<HttpResult> uploadImage(@Body RequestBody body);

    //上传一个完整 路劲
    @POST("gpsdata/syncprojectgps")
    MyCall<HttpResult> uploadProject(@Body RequestBody body);
}
