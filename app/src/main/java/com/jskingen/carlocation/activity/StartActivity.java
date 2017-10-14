package com.jskingen.carlocation.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jskingen.baselib.activity.base.NormalAcitivity;
import com.jskingen.baselib.utils.PermissionUtil;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.App;
import com.jskingen.carlocation.R;

public class StartActivity extends NormalAcitivity {

    private String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected int getViewByXml() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        PermissionUtil.getInstance().getPermissions(this, PERMISSIONS, new PermissionUtil.onRequestPermissionsListener() {
            @Override
            public void result(boolean hasRequest, String[] permissions) {
                if (hasRequest) {
                    App.getInstance().init();
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                } else {
                    ToastUtils.show(StartActivity.this, "权限获取失败,程序无法正常运行");

                }
                StartActivity.super.finish();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限反馈 必须写
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initData() {


    }


}
