package com.jskingen.carlocation.api;

import com.jskingen.baselib.network.MyCall;
import com.jskingen.baselib.network.model.HttpResult;
import com.jskingen.carlocation.model.User;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by ChneY on 2017/4/6.
 */

public interface LoginService {
    //http://192.168.0.234:8081/meters/gpsdata/login?username=admin&password=123456
    @POST("gpsdata/login")
    MyCall<HttpResult<User>> login(@QueryMap Map<String, String> map);

    //测试
    @POST("http://172.16.10.1:8080/upload.do")
    MyCall<HttpResult> uploadfie(@Body RequestBody body);

    //测试
    @GET("http://172.16.10.1:8080/")
    MyCall<HttpResult> index();
}
