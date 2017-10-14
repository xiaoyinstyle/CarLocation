package com.jskingen.carlocation.model;

import java.util.List;

/**
 * Created by Chne on 2017/8/21.
 */

public class LBean {
    /**
     * adCode : 321003
     * address : 江苏省扬州市邗江区维扬路靠近顾家小桥
     * aoiName :
     * buildingId :
     * city : 扬州市
     * cityCode : 0514
     * country : 中国
     * desc :
     * district : 邗江区
     * street :
     * errorInfo : success
     * floor :
     * road : 维扬路
     * province : 江苏省
     * locationDetail : -5 #csid:82e3ae719e694b9d9b1474b7b8c8820c
     * poiName :
     * number :
     * errorCode : 0
     * longitude : 119.416021
     * latitude : 32.360557
     * isOffset : true
     * satellites : 0
     * signalIntensity : -1
     * locationType : 4
     * mResults : [0,0]
     * mProvider : lbs
     * mExtras : {"mParcelledData":null,"mClassLoader":{"packages":{},"parent":null},"mMap":{"errorCode":0,"errorInfo":"success","locationType":4},"mHasFds":false,"mFdsKnown":true,"mAllowFds":true}
     * mDistance : 0.0
     * mTime : 1503280432061
     * mAltitude : 0.0
     * mLongitude : 0.0
     * mLon2 : 0.0
     * mLon1 : 0.0
     * mLatitude : 0.0
     * mLat1 : 0.0
     * mLat2 : 0.0
     * mInitialBearing : 0.0
     * mHasSpeed : true
     * mHasBearing : true
     * mHasAltitude : true
     * mHasAccuracy : true
     * mAccuracy : 150.0
     * mSpeed : 0.0
     * mBearing : 0.0
     */

    private String adCode;
    private String address;
    private String aoiName;
    private String buildingId;
    private String city;
    private String cityCode;
    private String country;
    private String desc;
    private String district;
    private String street;
    private String errorInfo;
    private String floor;
    private String road;
    private String province;
    private String locationDetail;
    private String poiName;
    private String number;
    private int errorCode;
    private double longitude;
    private double latitude;
    private boolean isOffset;
    private int satellites;
    private int signalIntensity;
    private int locationType;
    private String mProvider;
    private MExtrasBean mExtras;
    private double mDistance;
    private long mTime;
    private double mAltitude;
    private double mLongitude;
    private double mLon2;
    private double mLon1;
    private double mLatitude;
    private double mLat1;
    private double mLat2;
    private double mInitialBearing;
    private boolean mHasSpeed;
    private boolean mHasBearing;
    private boolean mHasAltitude;
    private boolean mHasAccuracy;
    private double mAccuracy;
    private double mSpeed;
    private double mBearing;
    private List<Double> mResults;

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAoiName() {
        return aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isIsOffset() {
        return isOffset;
    }

    public void setIsOffset(boolean isOffset) {
        this.isOffset = isOffset;
    }

    public int getSatellites() {
        return satellites;
    }

    public void setSatellites(int satellites) {
        this.satellites = satellites;
    }

    public int getSignalIntensity() {
        return signalIntensity;
    }

    public void setSignalIntensity(int signalIntensity) {
        this.signalIntensity = signalIntensity;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getMProvider() {
        return mProvider;
    }

    public void setMProvider(String mProvider) {
        this.mProvider = mProvider;
    }

    public MExtrasBean getMExtras() {
        return mExtras;
    }

    public void setMExtras(MExtrasBean mExtras) {
        this.mExtras = mExtras;
    }

    public double getMDistance() {
        return mDistance;
    }

    public void setMDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public long getMTime() {
        return mTime;
    }

    public void setMTime(long mTime) {
        this.mTime = mTime;
    }

    public double getMAltitude() {
        return mAltitude;
    }

    public void setMAltitude(double mAltitude) {
        this.mAltitude = mAltitude;
    }

    public double getMLongitude() {
        return mLongitude;
    }

    public void setMLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getMLon2() {
        return mLon2;
    }

    public void setMLon2(double mLon2) {
        this.mLon2 = mLon2;
    }

    public double getMLon1() {
        return mLon1;
    }

    public void setMLon1(double mLon1) {
        this.mLon1 = mLon1;
    }

    public double getMLatitude() {
        return mLatitude;
    }

    public void setMLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getMLat1() {
        return mLat1;
    }

    public void setMLat1(double mLat1) {
        this.mLat1 = mLat1;
    }

    public double getMLat2() {
        return mLat2;
    }

    public void setMLat2(double mLat2) {
        this.mLat2 = mLat2;
    }

    public double getMInitialBearing() {
        return mInitialBearing;
    }

    public void setMInitialBearing(double mInitialBearing) {
        this.mInitialBearing = mInitialBearing;
    }

    public boolean isMHasSpeed() {
        return mHasSpeed;
    }

    public void setMHasSpeed(boolean mHasSpeed) {
        this.mHasSpeed = mHasSpeed;
    }

    public boolean isMHasBearing() {
        return mHasBearing;
    }

    public void setMHasBearing(boolean mHasBearing) {
        this.mHasBearing = mHasBearing;
    }

    public boolean isMHasAltitude() {
        return mHasAltitude;
    }

    public void setMHasAltitude(boolean mHasAltitude) {
        this.mHasAltitude = mHasAltitude;
    }

    public boolean isMHasAccuracy() {
        return mHasAccuracy;
    }

    public void setMHasAccuracy(boolean mHasAccuracy) {
        this.mHasAccuracy = mHasAccuracy;
    }

    public double getMAccuracy() {
        return mAccuracy;
    }

    public void setMAccuracy(double mAccuracy) {
        this.mAccuracy = mAccuracy;
    }

    public double getMSpeed() {
        return mSpeed;
    }

    public void setMSpeed(double mSpeed) {
        this.mSpeed = mSpeed;
    }

    public double getMBearing() {
        return mBearing;
    }

    public void setMBearing(double mBearing) {
        this.mBearing = mBearing;
    }

    public List<Double> getMResults() {
        return mResults;
    }

    public void setMResults(List<Double> mResults) {
        this.mResults = mResults;
    }

    public static class MExtrasBean {
        /**
         * mParcelledData : null
         * mClassLoader : {"packages":{},"parent":null}
         * mMap : {"errorCode":0,"errorInfo":"success","locationType":4}
         * mHasFds : false
         * mFdsKnown : true
         * mAllowFds : true
         */

        private Object mParcelledData;
        private MClassLoaderBean mClassLoader;
        private MMapBean mMap;
        private boolean mHasFds;
        private boolean mFdsKnown;
        private boolean mAllowFds;

        public Object getMParcelledData() {
            return mParcelledData;
        }

        public void setMParcelledData(Object mParcelledData) {
            this.mParcelledData = mParcelledData;
        }

        public MClassLoaderBean getMClassLoader() {
            return mClassLoader;
        }

        public void setMClassLoader(MClassLoaderBean mClassLoader) {
            this.mClassLoader = mClassLoader;
        }

        public MMapBean getMMap() {
            return mMap;
        }

        public void setMMap(MMapBean mMap) {
            this.mMap = mMap;
        }

        public boolean isMHasFds() {
            return mHasFds;
        }

        public void setMHasFds(boolean mHasFds) {
            this.mHasFds = mHasFds;
        }

        public boolean isMFdsKnown() {
            return mFdsKnown;
        }

        public void setMFdsKnown(boolean mFdsKnown) {
            this.mFdsKnown = mFdsKnown;
        }

        public boolean isMAllowFds() {
            return mAllowFds;
        }

        public void setMAllowFds(boolean mAllowFds) {
            this.mAllowFds = mAllowFds;
        }

        public static class MClassLoaderBean {
            /**
             * packages : {}
             * parent : null
             */

            private PackagesBean packages;
            private Object parent;

            public PackagesBean getPackages() {
                return packages;
            }

            public void setPackages(PackagesBean packages) {
                this.packages = packages;
            }

            public Object getParent() {
                return parent;
            }

            public void setParent(Object parent) {
                this.parent = parent;
            }

            public static class PackagesBean {
            }
        }

        public static class MMapBean {
            /**
             * errorCode : 0
             * errorInfo : success
             * locationType : 4
             */

            private int errorCode;
            private String errorInfo;
            private int locationType;

            public int getErrorCode() {
                return errorCode;
            }

            public void setErrorCode(int errorCode) {
                this.errorCode = errorCode;
            }

            public String getErrorInfo() {
                return errorInfo;
            }

            public void setErrorInfo(String errorInfo) {
                this.errorInfo = errorInfo;
            }

            public int getLocationType() {
                return locationType;
            }

            public void setLocationType(int locationType) {
                this.locationType = locationType;
            }
        }
    }
}
