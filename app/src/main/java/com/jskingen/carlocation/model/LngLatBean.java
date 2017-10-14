package com.jskingen.carlocation.model;

import io.realm.RealmObject;

/**
 * Created by ChneY on 2017/4/10.
 */

public class LngLatBean extends RealmObject {
    private double lng;
    private double lat;
    private String createtime;//yyyy-MM-dd HH:mm:ss

//    @PrimaryKey
//    private String id;
//
//    public LngLatBean(String lat, String lng, String createtime) {
//        this.lat = lat;
//        this.lng = lng;
//        this.createtime = createtime;
//    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
