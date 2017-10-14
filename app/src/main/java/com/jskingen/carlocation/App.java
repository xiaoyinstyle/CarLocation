package com.jskingen.carlocation;

import android.support.multidex.MultiDexApplication;

import com.amap.api.maps.MapsInitializer;
import com.facebook.stetho.Stetho;
import com.jskingen.baselib.BaseHelp;
import com.jskingen.baselib.Configuration;
import com.jskingen.baselib.utils.LogUtils;
import com.jskingen.carlocation.common.Constant;
import com.jskingen.carlocation.common.utils.FileUtils;
import com.jskingen.carlocation.dao.MyMigration;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by ChneY on 2017/4/6.
 */

public class App extends MultiDexApplication {
    private static App applicationContext;

    public static App getInstance() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        LogUtils.Debug = BuildConfig.DEBUG;

    }

    public void init() {
        initRealm();
        //设置高德地图缓存路径
        MapsInitializer.sdcardDir = FileUtils.getMapFile(this).getPath();
        //友盟
        //MobclickAgent.startWithConfigure(UMAnalyticsConfig config);

        //Stetho初始化
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build()
        );

        //框架初始化
        Configuration configuration = new Configuration.Builder(this)
                .baseUrl(Constant.baseUrl)
                .fileName("CarLocation")
                .debug(BuildConfig.DEBUG)
                .timeout(30*1000)
                .build();
        BaseHelp.getInstance().init(configuration);
    }

    /**
     * 数据库 初始化
     */
    private void initRealm() {
        Realm.init(this.getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
//                .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库。
//                .name(RealmHelper.DB_NAME) //文件名
                .schemaVersion(1)
                .migration(new MyMigration())//数据库升级
                .build();
        Realm.setDefaultConfiguration(config);
    }
}