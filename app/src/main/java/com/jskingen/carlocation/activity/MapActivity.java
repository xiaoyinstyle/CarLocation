package com.jskingen.carlocation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.jskingen.baselib.activity.base.TitleActivity;
import com.jskingen.baselib.network.ServiceManager;
import com.jskingen.baselib.network.callBack.OnResponseCallback;
import com.jskingen.baselib.network.exception.MyException;
import com.jskingen.baselib.utils.GsonUtils;
import com.jskingen.baselib.utils.LogUtils;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.BuildConfig;
import com.jskingen.carlocation.R;
import com.jskingen.carlocation.api.MapInfoService;
import com.jskingen.carlocation.common.Constant;
import com.jskingen.carlocation.common.utils.FileUtils;
import com.jskingen.carlocation.common.utils.TimeUtils;
import com.jskingen.carlocation.common.view.imagezoom.utils.BitmapUtils;
import com.jskingen.carlocation.dao.RealmHelper;
import com.jskingen.carlocation.model.LBean;
import com.jskingen.carlocation.model.LatLngInfo;
import com.jskingen.carlocation.model.LngLatBean;
import com.jskingen.carlocation.model.User;
import com.jskingen.carlocation.model.UserProject;
import com.jskingen.carlocation.utils.DialogFactory;
import com.jskingen.carlocation.utils.ImageUtil;
import com.jskingen.carlocation.utils.MathUtils;
import com.jskingen.carlocation.utils.SPCache;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmList;

public class MapActivity extends TitleActivity {
    private final int FLAG_PREVIEW = 8;
    private final int FLAG_FINISH = 10;
    private final int FLAG_NORMAL = 9;
    private final int CODE_PREVIEW = 11;
    private final int mapZoom = 16;//地图缩放等级

    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.view)
    LinearLayout view;
    @BindView(R.id.tv_map_distance)
    TextView tvMapDistance;
    @BindView(R.id.tv_map_time)
    TextView tvMapTime;
    @BindView(R.id.tv_map_speed)
    TextView tvMapSpeed;
    @BindView(R.id.tv_map_speed_title)
    TextView tvMapSpeedTitle;
    @BindView(R.id.tv_map_info)
    TextView tvMapInfo;
    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.bt_map_correct)
    Button btMapCorrect;

    private MapInfoService service;

    private AMap aMap;
    private User user;
    private LatLngBounds.Builder builder;

    private List<LatLng> latLngs = new ArrayList<>();

    private UserProject userProject;

    private DialogFactory dialogFactory;
    private String addressStrat = "";
    private String addressEnd = "";

    private long startTime = 0;//时间戳
    private long currentTime = 0;//时间戳
    private float distance = 0;//单位 （米）
    private float speed = 0;//单位（km/h）

    private int skip = 1;//前两次 定位的 点忽略
    private Location tempLocation = null;//临时定位的点，用于跟速度比较的
    private boolean isFinish = false;

    private Timer timer;//定时器

    @Override
    protected int getViewByXml() {
        return R.layout.activity_map;
    }

    @Override
    protected void setTitle() {
        tv_right.setText(R.string.map_title_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFinish = false;
                view.setClickable(true);
                cross(FLAG_PREVIEW);
                showAllLocation(FLAG_PREVIEW);

                dialogFactory.showProgressDialog("", getString(R.string.map_dialog_deal));
                mapScreenShot(FLAG_PREVIEW);
            }
        });
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        user = SPCache.getInstance().getUser();
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mMapView.getMap();
        initMap();

        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTask(), 0, 1000);
    }

    private void initMap() {

        aMap.getUiSettings().setZoomControlsEnabled(false); //缩放按钮不显示
        aMap.getUiSettings().setScaleControlsEnabled(true);//比例尺控件
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);// Logo位置（地图右下角）

        setLocationIcon(true);

        //自动缩放，最小控制
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LogUtils.e("aMap_Change" + cameraPosition.target.latitude + "_" + cameraPosition.target.longitude);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LogUtils.e("aMap_" + aMap.getScalePerPixel() + "_" + latLngs.size());
                if (aMap.getScalePerPixel() < 1)
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(mapZoom));
            }
        });

        //定位监听
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                try {
                    mapInfo(location);//打印数据

                    //只保存  GPS 定位
                    if (location.getLatitude() < 1) {
                        autoMoveCamrea();
                    }
                    tempLocation = location;
                    tvMapSpeed.setText("0.0km/h");
                    if (location.getProvider().equals("gps"))
                        tvPrompt.setVisibility(View.GONE);
                    else
                        tvPrompt.setVisibility(View.VISIBLE);

                    if (!location.getProvider().equals("gps") || isFinish || location.getSpeed() == 0)
                        return;

                    //速度》》单位是 m/s 需要 1米/秒(m/s)=3.6千米/时(km/h)
                    speed = location.getSpeed();
                    tvMapSpeed.setText(MathUtils.point2(3.6f * speed, 2) + "km/h");

                    //移动Camera
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(mapZoom));
                    if (skip > 0) {
                        skip--;
                    } else {
                        setMapInfo(location);  //保存
                        addTraceList(location);//添加轨迹
                        cross(FLAG_NORMAL);    //画轨迹


                        try {
                            String l = GsonUtils.toJson(location);
                            LBean lBean = GsonUtils.getObject(l, LBean.class);
                            //保存地址
                            if (TextUtils.isEmpty(addressStrat))
                                addressStrat =lBean.getAddress();

                            if (TextUtils.isEmpty(addressEnd))
                                addressEnd =lBean.getAddress();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        aMap.animateCamera(CameraUpdateFactory.newLatLng(latLngs.get(latLngs.size() - 1)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 设置中心蓝点样式(显示或者隐藏)
     */
    private void setLocationIcon(boolean showIcon) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。

        myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。

        if (showIcon) {
            aMap.setMyLocationEnabled(true);//定位蓝点
        } else {
            aMap.setMyLocationEnabled(false);//定位蓝点
        }
    }

    @Override
    protected void initData() {
        dialogFactory = new DialogFactory(this);
        String projectName = getIntent().getStringExtra("projectName");
        //数据库 获取数据
        if (!TextUtils.isEmpty(projectName)) {
            userProject = RealmHelper.getInstance().findProject(projectName);
            String t[] = projectName.split("_");
            title.setText(t[0]);
        }

        autoMoveCamrea();

        btMapCorrect.setVisibility(View.GONE);
        if (!BuildConfig.DEBUG) {
            tvMapInfo.setVisibility(View.GONE);
        }
    }

    /**
     * 处理数据
     */
    private void setMapInfo(Location location) {
        //如果两次定位在相距小于1米，则跳过
        if (isSkipThis(location) < 20 && latLngs.size() >= 2)
            return;

        latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));

        //更新数据库
        uploadProject(location.getLatitude(), location.getLongitude(), TimeUtils.currentTime(location.getTime()));

        setAllMileageView();//设置路程 Textview
    }

    /**
     * 更新数据库
     */
    private void uploadProject(double latitude, double longitude, String time) {
        LngLatBean lngLatBean = new LngLatBean();
        lngLatBean.setLat(latitude);
        lngLatBean.setLng(longitude);
        lngLatBean.setCreatetime(time);
        if (userProject.getLngLatBeen() == null || userProject.getLngLatBeen().size() == 0)
            userProject.setLngLatBeen(new RealmList<LngLatBean>());
        userProject.getLngLatBeen().add(lngLatBean);
        //保存临时 定位 数据库
        RealmHelper.getInstance().updateTempLocation(userProject);
    }

    /**
     * 行驶轨迹
     */
    private void cross(int flag) {
        if (flag == FLAG_PREVIEW) {
            aMap.clear();
            addMarker(true);
        } else if (flag == FLAG_FINISH) {
            aMap.clear();
            addMarker(true);
            addMarker(false);
        }
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
    }

    /**
     * moveCamera
     */
    private void autoMoveCamrea() {
        aMap.animateCamera(CameraUpdateFactory.zoomTo(mapZoom));
        if (latLngs == null || latLngs.size() == 0)
            aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(32.36079, 119.416227)));//智谷
        else if (tempLocation == null)
            aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(tempLocation.getLatitude(), tempLocation.getLongitude())));
        else
            aMap.animateCamera(CameraUpdateFactory.newLatLng(latLngs.get(latLngs.size() - 1)));
    }

    /**
     * 显示所有路程
     */
    private void showAllLocation(int flag) {
        if (null == latLngs || latLngs.size() == 0) {
            aMap.animateCamera(CameraUpdateFactory.zoomTo(mapZoom));
            return;
        }

        //结束定位
        if (builder == null) {
            builder = new LatLngBounds.Builder();

            builder.include(latLngs.get(0));
            builder.include(new LatLng(latLngs.get(0).latitude + 0.000010, latLngs.get(0).longitude + 0.000010));
        } else {
            builder.include(latLngs.get(latLngs.size() - 1));
        }

        if (flag == FLAG_NORMAL) {
            aMap.animateCamera(CameraUpdateFactory.newLatLng(latLngs.get(latLngs.size() - 1)));
        } else if (flag == FLAG_PREVIEW || flag == FLAG_FINISH) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        }
    }

    /**
     * 保存 并处理 Bitmap
     */
    private File saveImage2Bitmap(Bitmap bitmap) {
        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
        bitmap = BitmapUtils.getViewBitmap(view);
        bitmap = ImageUtil.drawView(MapActivity.this, bitmap, getTextContent(1), getTextContent(2));

        File file = FileUtils.getImageFile(MapActivity.this, Constant.removeString(userProject.getProject()) + "C.png");
        BitmapUtils.saveBitmap(bitmap, file.getPath());
        return file;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destryAct = false;
        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        view.setClickable(false);
        showDistance();

        if (isFinish) {
            dialogFactory.showProgressDialog("", "正在保存轨迹...");
            btMapCorrect.performClick();
            return;
        }
        if (view != null)
            view.setBackgroundResource(0);

        if (latLngs.size() != 0) {
            cross(FLAG_NORMAL);
        } else {
            autoMoveCamrea();
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(mapZoom));
        setLocationIcon(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            setLocationIcon(true);
            mMapView.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }

    /**
     * 对地图进行截屏
     */
    private void mapScreenShot(final int flag) {
        dialogFactory.showProgressDialog("", "正在保存图片..");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
                    @Override
                    public void onMapScreenShot(Bitmap bitmap) {
                        if (flag == FLAG_PREVIEW) {//图片预览
                            File file = saveImage2Bitmap(bitmap);
                            startActivityForResult(new Intent(MapActivity.this, PreviewActivity.class)
                                    .putExtra("path", file.getPath()), CODE_PREVIEW);
                        } else if (flag == FLAG_FINISH) {//结束
                            if (null != bitmap) {
                                if (!saveImage2Bitmap(bitmap).exists()) {
                                    ToastUtils.show(R.string.map_save_error);
                                }
                            }
                            MapActivity.super.finish();
                        }
                        if (dialogFactory != null)
                            dialogFactory.dismiss();
                    }

                    @Override
                    public void onMapScreenShot(Bitmap bitmap, int status) {
                    }
                });
            }
        }, 600);
    }


    /**
     * 拼接文字
     */
    public String getTextContent(int flag) {
        String info = "";
        if (flag == 1) {
            info += "用户名：" + (TextUtils.isEmpty(user.getUsername()) ? "" : user.getUsername()) + "\n";
            info += "项目名：" + (TextUtils.isEmpty(userProject.getProject()) ? "" : userProject.getProject()) + "\n";
        } else if (flag == 2) {
            info += "开始地址：" + addressStrat + "\n";
            info += "结束地址：" + addressEnd + "\n";
        }
        return info;
    }

    private boolean destryAct = false;

    @Override
    public void finish() {
        if (dialogFactory == null)
            super.finish();
        else if (dialogFactory != null)
            dialogFactory.showConfirmDialog(getString(R.string.map_dialog_back), new DialogFactory.OnBackClick() {
                @Override
                public void onBacktClick(boolean b) {

                    isFinish = true;
                    if (latLngs.size() > 1) {
                        dialogFactory.showProgressDialog("", "正在保存轨迹...");
                        btMapCorrect.performClick();
                    } else {
                        dialogFactory.showProgressDialog("", getString(R.string.map_dialog_deal));
                        myFinish();
                    }
                }
            });
    }

    private void myFinish() {
        try {

            dialogFactory.showProgressDialog("", getString(R.string.map_dialog_deal));

            view.setClickable(true);
            setResult(RESULT_OK);
            //结束时间
            userProject.setEndTime(TimeUtils.currentTime());
            RealmHelper.getInstance().updateTempLocation(userProject);

            setLocationIcon(false);
            addMarker(false);
            timer.cancel();
            showAllLocation(FLAG_FINISH);
            mapScreenShot(FLAG_FINISH);

            destryAct = true;
            tvMapTime.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("AAA_LOG", "FINISH");
                    if (destryAct) {
                        showAllLocation(FLAG_FINISH);
                        mapScreenShot(FLAG_FINISH);
                    }
                }
            }, 20000);
        } catch (Exception c) {
            c.printStackTrace();
        }
    }

    /**
     * 添加标记
     */
    private void addMarker(boolean start) {
        if (null == latLngs || latLngs.size() == 0)
            return;
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.draggable(false);
        if (start) {
            markerOption.position(latLngs.get(0));
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.ic_map_start)));
        } else {
            markerOption.position(latLngs.get(latLngs.size() - 1));
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.ic_map_stop)));
        }
        aMap.addMarker(markerOption);
    }

    //距离
    private double isSkipThis(Location aMapLocation) {
        if (latLngs.size() < 2)
            return 0;
        else {
            LatLng LA = latLngs.get(latLngs.size() - 1);
            LatLng LB = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            return AMapUtils.calculateLineDistance(LA, LB);
        }
    }

    //遍历所有点的里程总数
    private void showDistance() {
        float tempDistance = 0;
        for (int i = 1; i < latLngs.size(); i++) {
            LatLng LA = latLngs.get(i);
            LatLng LB = latLngs.get(i - 1);
            float temp = AMapUtils.calculateLineDistance(LA, LB);//单位米
            tempDistance += temp;
        }

        distance = tempDistance;
        if (distance < 10)
            tvMapDistance.setText("0.00");
        else {//两个小数点
            tvMapDistance.setText(MathUtils.point2(distance / 1000f, 2) + "");
        }

    }


    /**
     * 计算并显示 总里程数
     */
    private void setAllMileageView() {
        if (latLngs.size() < 2)
            return;

        LatLng LA = latLngs.get(latLngs.size() - 1);
        LatLng LB = latLngs.get(latLngs.size() - 2);
        float temp = AMapUtils.calculateLineDistance(LA, LB);//单位米
        distance += temp;

        if (distance < 10)
            tvMapDistance.setText("0.00");
        else {//两个小数点
            tvMapDistance.setText(MathUtils.point2(distance / 1000f, 2) + "");
        }
    }

    //保存
    private void addTraceList(Location location) {
        //集合所有的点在一起
        if (builder == null) {
            builder = new LatLngBounds.Builder();
            builder.include(latLngs.get(0));
            builder.include(new LatLng(latLngs.get(0).latitude + 0.000010, latLngs.get(0).longitude + 0.000010));
        } else {
            builder.include(latLngs.get(latLngs.size() - 1));
        }

    }

    @OnClick({R.id.bt_map_correct, R.id.view})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.view:
                break;
            case R.id.bt_map_correct:
                cross(FLAG_FINISH);
                tvMapDistance.setText(MathUtils.point2(distance / 1000f, 2) + "");

                if (isFinish) {
                    myFinish();
                }
                break;
        }
    }

    /**
     * 定时器
     */
    private class MyTask extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            currentTime = System.currentTimeMillis();
            long temp = currentTime - startTime;//毫秒
            tvMapTime.setText(TimeUtils.getHHmmss(temp));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_PREVIEW && RESULT_OK == resultCode) {

            isFinish = true;
            if (latLngs.size() > 1) {
                dialogFactory.showProgressDialog("", "正在进行轨迹保存");
                btMapCorrect.performClick();
            } else {
                dialogFactory.showProgressDialog("", getString(R.string.map_dialog_deal));
                myFinish();
            }
        }
    }

    //显示信息
    private String mapInfo(Location amapLocation) {
        String info = "";
        if (amapLocation != null) {
            info = " 来源:" + amapLocation.getProvider() +
                    ",\n 获取时间:" + TimeUtils.currentTime(amapLocation.getTime()) +
                    ",\n 获取方向:" + amapLocation.getBearing() +
                    ",\n 获取经度:" + amapLocation.getLatitude() +
                    ",\n 获取经度:" + amapLocation.getLongitude() +
                    ",\n " + GsonUtils.toJson(amapLocation);
            Log.e("AAA", "Location:" + GsonUtils.toJson(amapLocation));
        }
        tvMapInfo.setText(info);
        return info;
    }

}