package com.jskingen.carlocation.utils;

import android.app.Activity;

import com.jskingen.baselib.network.ServiceManager;
import com.jskingen.baselib.network.callBack.OnResponseCallback;
import com.jskingen.baselib.network.exception.MyException;
import com.jskingen.baselib.network.model.HttpResult;
import com.jskingen.baselib.network.utils.RequestMultipart;
import com.jskingen.baselib.utils.GsonUtils;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.common.Constant;
import com.jskingen.carlocation.common.utils.FileUtils;
import com.jskingen.baselib.utils.LogUtils;
import com.jskingen.carlocation.dao.RealmHelper;
import com.jskingen.carlocation.model.UserProject;
import com.jskingen.carlocation.api.UploadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by ChneY on 2017/4/11.
 */

public class UploadProject {
    private UploadService service;
    private Activity context;
    private UserProject userProject;

    public UploadProject(Activity activity, UserProject userProject, UploadListener uploadListener) {
        this.context = activity;
        this.userProject = userProject;
        this.uploadListener = uploadListener;
        uploadProject();
    }

    /**
     * 上传json
     */
    private void uploadProject() {
        //如果json已经上传，则跳过 直接上传 图片
        if (userProject.getFlagJson()) {
            uploadFile();
            return;
        }

        if (service == null)
            service = ServiceManager.create(UploadService.class);
        List<UserProject> t = new ArrayList<>();
        t.add(userProject);
        LogUtils.e(GsonUtils.toJson(t));

//        @Query("gpsinfo")
        RequestMultipart multipart = new RequestMultipart();
        RequestBody body = multipart.add("gpsinfo", GsonUtils.toJson(t))
                .add("userid", userProject.getUserid() + "")
                .build();
        service.uploadProject(body).enqueue(new OnResponseCallback<HttpResult>(true) {
            @Override
            public void onSuccess(HttpResult result) {
                userProject.setFlagJson(true);
                RealmHelper.getInstance().chageUploadFlag(userProject);
                uploadFile();
            }

            @Override
            public void onError(MyException e) {
                if (e.getCode() == 0)
                    uploadListener.error(1, e);
                else
                    ToastUtils.show(e.getDetailMessage() + "");
            }
        });
    }

    /**
     * 上传图片
     */
    private void uploadFile() {
        //如果图片已经上传，则跳过 直接弹框
        if (userProject.getFlagImage()) {
            if (uploadListener != null) {
                uploadListener.success();
                RealmHelper.getInstance().relase(userProject.getProject());
            }
            return;
        }
        //图片地址需要要 除去 一些特殊字符
        String projectName = Constant.removeString(userProject.getProject());
        File file = FileUtils.getImageFile(context, projectName + "C.png");
        File fileStart = FileUtils.getImageFile(context, projectName + "S.png");
        File fileEnd = FileUtils.getImageFile(context, projectName + "E.png");

        if (service == null)
            service = ServiceManager.create(UploadService.class);

        //图片
        RequestMultipart multipart = new RequestMultipart();
        if (fileStart.exists())
            multipart.add("startPic", fileStart);
        if (file.exists())
            multipart.add("trackPic", file);
        if (fileEnd.exists())
            multipart.add("endPic", fileEnd);

        RequestBody body = multipart.add("project", userProject.getProject())
                .add("userid", userProject.getUserid() + "")
                .add("smileage", userProject.getProjectMileage() + "")   //手输里程数
                .build();

        service.uploadImage(body).enqueue(new OnResponseCallback<HttpResult>(true) {
            @Override
            public void onSuccess(HttpResult result) {
                userProject.setFlagImage(true);
                RealmHelper.getInstance().chageUploadFlag(userProject);
                if (uploadListener != null) {
                    uploadListener.success();
                    RealmHelper.getInstance().relase(userProject.getProject());
                }
            }

            @Override
            public void onError(MyException e) {
                if (e.getCode() == 0)
                    uploadListener.error(2, e);
                else
                    ToastUtils.show(e.getDetailMessage() + "");
            }
        });
    }


    private UploadListener uploadListener;

    public interface UploadListener {
        void success();

        void error(int flag, MyException e);
    }
}
