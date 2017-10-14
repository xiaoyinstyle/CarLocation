package com.jskingen.carlocation.common;

import com.jskingen.carlocation.BuildConfig;

/**
 * Created by ChneY on 2017/4/6.
 */

public class Constant {
    //    public static String baseUrl = "http://172.17.79.1:8080/";
//    public final static String baseUrl = "http://192.168.0.234:12138/meters/";
    public final static String baseUrl = "http://112.124.9.97:8889/meters/";

    //更新下载地址
    public final static String downloadUrl = "http://112.124.9.97:8889/meters/down/meters_android.apk";

    //检查更新
    public final static String checkUpload = "http://112.124.9.97:8889/meters/down/meters_android_version.txt";

    //去除图片文件的特殊字符
    public final static String removeString(String s) {
        s = s.replaceAll("_| |:|-", "");
        return s;
    }
}
