package com.jskingen.carlocation.model;

import java.util.List;

/**
 * Created by ChneY on 2017/5/26.
 */

public class FindMileageBean {

    /**
     * w_mileage : 76.95
     * searchstime : 2017-05-01
     * searchetime : 2017-05-30
     * DataSelf : {"projectmileage":[{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-12","username":"admin"},{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-16","username":"admin"},{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-17","username":"admin"},{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-18","username":"admin"},{"mileage":"0.00","name":"ffghg","photo":null,"starttime":"2017-05-20 17:54:14","username":"admin"},{"mileage":"2.56","name":"hhh","photo":null,"starttime":"2017-05-20 18:02:46","username":"admin"},{"mileage":"24.14","name":"hghgh","photo":null,"starttime":"2017-05-24 10:29:28","username":"admin"},{"mileage":"52.81","name":"ee","photo":null,"starttime":"2017-05-24 17:10:51","username":"admin"}],"totalmileage":null,"userid":1,"username":"admin"}
     * d_mileage : 0.00
     * m_mileage : 88.90
     */

    private String w_mileage;
    private String searchstime;
    private String searchetime;
    private DataSelfBean DataSelf;
    private String d_mileage;
    private String m_mileage;

    public String getW_mileage() {
        return w_mileage;
    }

    public void setW_mileage(String w_mileage) {
        this.w_mileage = w_mileage;
    }

    public String getSearchstime() {
        return searchstime;
    }

    public void setSearchstime(String searchstime) {
        this.searchstime = searchstime;
    }

    public String getSearchetime() {
        return searchetime;
    }

    public void setSearchetime(String searchetime) {
        this.searchetime = searchetime;
    }

    public DataSelfBean getDataSelf() {
        return DataSelf;
    }

    public void setDataSelf(DataSelfBean DataSelf) {
        this.DataSelf = DataSelf;
    }

    public String getD_mileage() {
        return d_mileage;
    }

    public void setD_mileage(String d_mileage) {
        this.d_mileage = d_mileage;
    }

    public String getM_mileage() {
        return m_mileage;
    }

    public void setM_mileage(String m_mileage) {
        this.m_mileage = m_mileage;
    }

    public static class DataSelfBean {
        /**
         * projectmileage : [{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-12","username":"admin"},{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-16","username":"admin"},{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-17","username":"admin"},{"mileage":"2.35","name":"测试1","photo":null,"starttime":"2017-05-18","username":"admin"},{"mileage":"0.00","name":"ffghg","photo":null,"starttime":"2017-05-20 17:54:14","username":"admin"},{"mileage":"2.56","name":"hhh","photo":null,"starttime":"2017-05-20 18:02:46","username":"admin"},{"mileage":"24.14","name":"hghgh","photo":null,"starttime":"2017-05-24 10:29:28","username":"admin"},{"mileage":"52.81","name":"ee","photo":null,"starttime":"2017-05-24 17:10:51","username":"admin"}]
         * totalmileage : null
         * userid : 1
         * username : admin
         */

        private String totalmileage;
        private int userid;
        private String username;
        private List<ProjectmileageBean> projectmileage;

        public String getTotalmileage() {
            return totalmileage;
        }

        public void setTotalmileage(String totalmileage) {
            this.totalmileage = totalmileage;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<ProjectmileageBean> getProjectmileage() {
            return projectmileage;
        }

        public void setProjectmileage(List<ProjectmileageBean> projectmileage) {
            this.projectmileage = projectmileage;
        }

        public static class ProjectmileageBean {
            /**
             * mileage : 2.35
             * name : 测试1
             * photo : null
             * starttime : 2017-05-12
             * username : admin
             */

            private String mileage;
            private String name;
            private String photo;
            private String starttime;
            private String username;
            private String smileage;

            public String getSmileage() {
                return smileage;
            }

            public void setSmileage(String smileage) {
                this.smileage = smileage;
            }

            public String getMileage() {
                return mileage;
            }

            public void setMileage(String mileage) {
                this.mileage = mileage;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
