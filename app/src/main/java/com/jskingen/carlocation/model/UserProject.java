package com.jskingen.carlocation.model;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ChneY on 2017/4/8.
 */

public class UserProject extends RealmObject {
    private String userid;
    private String project;
    private String createTime;//行程开始时间
    private String endTime;//行程开始时间
    private boolean flagJson; //json上传
    private boolean flagImage; //image上传

    private RealmList<LngLatBean> lnglatlist = new RealmList<>();//行驶轨迹

    private String projectMileage;//里程数

    @PrimaryKey
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean getFlagJson() {
        return flagJson;
    }

    public void setFlagJson(boolean flagJson) {
        this.flagJson = flagJson;
    }

    public boolean getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(boolean flagImage) {
        this.flagImage = flagImage;
    }

    public List<LngLatBean> getLngLatBeen() {
        return lnglatlist;
    }

    public void setLngLatBeen(RealmList<LngLatBean> lngLatlist) {
        this.lnglatlist = lngLatlist;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProjectMileage() {
        return projectMileage;
    }

    public void setProjectMileage(String projectMileage) {
        this.projectMileage = projectMileage;
    }


    //    private class LngLatBean extends RealmObject {
//        private String lng;
//        private String lat;
//        private String createtime;//yyyy-MM-dd HH:mm:ss
//
//        public String getLng() {
//            return lng;
//        }
//
//        public void setLng(String lng) {
//            this.lng = lng;
//        }
//
//        public String getLat() {
//            return lat;
//        }
//
//        public void setLat(String lat) {
//            this.lat = lat;
//        }
//
//        public String getCreatetime() {
//            return createtime;
//        }
//
//        public void setCreatetime(String createtime) {
//            this.createtime = createtime;
//        }
//    }
}
