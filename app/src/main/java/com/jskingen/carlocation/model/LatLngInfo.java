package com.jskingen.carlocation.model;

/**
 * Created by ChneY on 2017/5/2.
 */

public class LatLngInfo {
    /**
     * status : 1
     * info : OK
     * infocode : 10000
     * regeocode : {"formatted_address":"北京市海淀区燕园街道北京大学","addressComponent":{"country":"中国","province":"北京市","city":[],"citycode":"010","district":"海淀区","adcode":"110108","township":"燕园街道","towncode":"110108015000","neighborhood":{"name":"北京大学","type":"科教文化服务;学校;高等院校"},"building":{"name":"北京大学","type":"科教文化服务;学校;高等院校"},"streetNumber":{"street":"颐和园路","number":"5号","location":"116.310454,39.9927339","direction":"东北","distance":"94.5489"},"businessAreas":[{"location":"116.29522008325625,39.99426090286774","name":"颐和园","id":"110108"},{"location":"116.31060892521111,39.99231773703259","name":"北京大学","id":"110108"},{"location":"116.32013920092481,39.97507461118122","name":"中关村","id":"110108"}]}}
     */

    private String status;
    private String info;
    private String infocode;
    private RegeocodeBean regeocode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public RegeocodeBean getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(RegeocodeBean regeocode) {
        this.regeocode = regeocode;
    }

    public static class RegeocodeBean {
        /**
         * formatted_address : 北京市海淀区燕园街道北京大学
         * addressComponent : {"country":"中国","province":"北京市","city":[],"citycode":"010","district":"海淀区","adcode":"110108","township":"燕园街道","towncode":"110108015000","neighborhood":{"name":"北京大学","type":"科教文化服务;学校;高等院校"},"building":{"name":"北京大学","type":"科教文化服务;学校;高等院校"},"streetNumber":{"street":"颐和园路","number":"5号","location":"116.310454,39.9927339","direction":"东北","distance":"94.5489"},"businessAreas":[{"location":"116.29522008325625,39.99426090286774","name":"颐和园","id":"110108"},{"location":"116.31060892521111,39.99231773703259","name":"北京大学","id":"110108"},{"location":"116.32013920092481,39.97507461118122","name":"中关村","id":"110108"}]}
         */

        private String formatted_address;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }
    }
}
