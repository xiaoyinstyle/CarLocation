package com.jskingen.carlocation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jskingen.baselib.utils.GsonUtils;
import com.jskingen.carlocation.App;
import com.jskingen.carlocation.dao.RealmHelper;
import com.jskingen.carlocation.model.User;
import com.jskingen.carlocation.model.UserProject;

import java.util.List;
import java.util.Map;

/**
 * Created by ChneY on 2017/4/7.
 */

public class SPCache {

    private final static String CACHENAME = "APP_CACHE";
    private static SPCache cache;
    private static Context mContext;

    private static SharedPreferences sharedPreferences;

    public static SPCache getInstance() {
        if (mContext == null)
            mContext = App.getInstance().getApplicationContext();

        if (cache == null)
            cache = new SPCache();

        if (sharedPreferences == null) {
            sharedPreferences = mContext.getSharedPreferences(CACHENAME, Activity.MODE_PRIVATE);
        }
        return cache;
    }

    public void clearData() {
        sharedPreferences.edit().clear().commit();
    }

    private String getUserID() {
        User user = SPCache.getInstance().getUser();
        return user == null ? user.getUserid() + "" : "";
    }

    public void setUserName(String[] s) {
        if (s == null || s.length == 0)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userName", s[0]);
        if (s.length >= 2)
            editor.putString("userPS", s[1]);
        editor.commit();
    }

    public String[] getUserName() {
        return new String[]{sharedPreferences.getString("userName", "")
                , sharedPreferences.getString("userPS", "")};
    }

    /**
     * 用户信息
     */
    public void setUser(User u) {
        if (u == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", GsonUtils.toJson(u));
        editor.commit();
    }

    public User getUser() {
        String s = sharedPreferences.getString("user", "");
        return TextUtils.isEmpty(s) ? null : GsonUtils.getObject(s, User.class);
    }

    /**
     * 记住密码
     */
    public void setRecordPassword(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("password", b);
        editor.commit();
    }

    /**
     * 记住密码
     */
    public boolean getRecordPassword() {
        return sharedPreferences.getBoolean("password", true);
    }

//
//    /**
//     * 保存 所有记录
//     */
//    public void setDaoList(List<RealmHelper.Bean> list) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("daoList", GsonUtils.toJson(list));
//        editor.commit();
//    }
//
//    public List<RealmHelper.Bean> getDaoList() {
//        return GsonUtils.getObjectList(
//                sharedPreferences.getString("daoList", "")
//                , RealmHelper.Bean.class);
//    }
//
//    /**
//     * 保存单个记录
//     */
//    public void setDaoItem(UserProject project) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(project.getProject() + "", GsonUtils.toJson(project));
//        editor.commit();
//    }
//
//    public List<RealmHelper.Bean> getDaoItem(String projectName) {
//        return GsonUtils.getObjectList(
//                sharedPreferences.getString(projectName, "")
//                , RealmHelper.Bean.class);
//    }
}
