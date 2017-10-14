package com.jskingen.carlocation.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.jskingen.baselib.activity.base.TitleActivity;
import com.jskingen.baselib.imageload.GlideUtil;
import com.jskingen.baselib.network.exception.MyException;
import com.jskingen.baselib.update.UpdateSoftUtils;
import com.jskingen.baselib.utils.NetUtils;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.R;
import com.jskingen.carlocation.common.Constant;
import com.jskingen.carlocation.popwindow.MainMenu;
import com.jskingen.carlocation.utils.SPCache;
import com.jskingen.carlocation.common.utils.FileUtils;
import com.jskingen.carlocation.common.utils.TimeUtils;
import com.jskingen.carlocation.dao.RealmHelper;
import com.jskingen.carlocation.utils.DialogFactory;
import com.jskingen.carlocation.utils.ImageShopUtils;
import com.jskingen.baselib.utils.LogUtils;
import com.jskingen.carlocation.utils.UploadProject;
import com.jskingen.carlocation.model.User;
import com.jskingen.carlocation.model.UserProject;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends TitleActivity {
    private final int TAKE_PHOTO_START = 9;
    private final int TAKE_PHOTO_END = 10;
    private final int ACTIVITY_MAP = 11;

    @BindView(R.id.tv_main_user_name)
    TextView tvMainUserName;
    @BindView(R.id.tv_main_project_name)
    TextView tvMainProjectName;
    @BindView(R.id.tv_main_project_mileage)
    TextView tvMainProjectMileage;
    @BindView(R.id.bt_main_start)
    Button btMainStart;
    @BindView(R.id.bt_main_photo_start)
    Button btMainPhotoStart;
    @BindView(R.id.bt_main_photo_end)
    Button btMainPhotoEnd;
    @BindView(R.id.bt_main_upload)
    Button btMainUpload;
    @BindView(R.id.iv_main_photo_start)
    ImageView ivMainPhotoStart;
    @BindView(R.id.iv_main_photo_end)
    ImageView ivMainPhotoEnd;
    @BindView(R.id.iv_main_photo_crop)
    ImageView ivMainPhotoCrop;
    @BindView(R.id.fbt_new_project)
    FloatingActionButton fbtNewProject;

    MainMenu mainMenu;

    private DialogFactory dialogFactory;
    private String projectName = "";
    private String projectMileage = "";//
    private User user;

    private File photoStart;
    private File photoEnd;

    private UserProject userProject;
    private ImageShopUtils lubanUtils;

    private String startTime = "";
    private boolean isNeedUpload = false;

    @Override
    protected int getViewByXml() {
        return R.layout.activity_main;
    }

    @Override
    protected void setTitle() {
        iv_left.setVisibility(View.GONE);
        title.setText(R.string.app_name);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.ic_main_more);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNeedUpload)
                    ToastUtils.show(R.string.main_need_upload);
                else
                    mainMenu.show(iv_right);
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mainMenu = new MainMenu(this);
        user = SPCache.getInstance().getUser();
        tvMainUserName.setText(user.getUsername());

        //图片处理
        lubanUtils = new ImageShopUtils(this);
        //
        dialogFactory = new DialogFactory(this);

        //检测更新
        if (getIntent().getBooleanExtra("isUpdata", false))
            new UpdateSoftUtils.Builder(this)
                    .showToast(false)
                    .downloadUrl(Constant.downloadUrl)
//                    .forceUpdata(true)
                    .updataUrl(Constant.checkUpload)
                    .start();

        OfflineMapManager amapManager = new OfflineMapManager(this, new OfflineMapManager.OfflineMapDownloadListener() {
            @Override
            public void onDownload(int i, int i1, String s) {
                LogUtils.e("onDownload: " + i + "_" + "_" + i1 + "_" + s);
            }

            @Override
            public void onCheckUpdate(boolean b, String s) {
                LogUtils.e("onCheckUpdate: " + b + "_" + s);
            }

            @Override
            public void onRemove(boolean b, String s, String s1) {
                LogUtils.e("onRemove: " + b + "_" + "_" + s + "_" + s);
            }
        });

        try { //下载地图缓存
            if (NetUtils.isConnected(this) && NetUtils.isWifi(this))
                amapManager.downloadByCityCode("0514");
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        btMainUpload.setEnabled(false);
        btMainPhotoEnd.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_MAP && RESULT_OK == resultCode) {

            userProject = RealmHelper.getInstance().findProject(userProject.getProject());
            btMainPhotoStart.setEnabled(false);
            btMainStart.setEnabled(false);
            btMainPhotoEnd.setEnabled(true);
            btMainUpload.setEnabled(true);
            isNeedUpload = true;

            File f = FileUtils.getImageFile(this, Constant.removeString(projectName) + "C.png");
            if (f.exists())
                GlideUtil.getInstance(MainActivity.this).setView(ivMainPhotoCrop, f);
        } else if (TAKE_PHOTO_START == requestCode && RESULT_OK == resultCode) {
            //开始时，拍照
            if (photoStart == null || !photoStart.exists()) {
                return;
            }
            lubanUtils.addText(photoStart, getStartText(), new ImageShopUtils.OnFileListener() {
                @Override
                public void success(File file) {
                    if (file != null) {
                        btMainPhotoStart.setText(R.string.main_take_photo_again);
                        GlideUtil.getInstance(MainActivity.this).setView(ivMainPhotoStart, photoStart);
                    }
                }
            });

        } else if (TAKE_PHOTO_END == requestCode && RESULT_OK == resultCode) {
            //结束时，拍照
            if (photoEnd == null || !photoEnd.exists()) {
                return;
            }
            lubanUtils.addText(photoEnd, getStartText(), new ImageShopUtils.OnFileListener() {
                @Override
                public void success(File file) {
                    if (file != null) {
                        btMainPhotoEnd.setText(R.string.main_take_photo_again);
                        GlideUtil.getInstance(MainActivity.this).setView(ivMainPhotoEnd, photoEnd);
                    }

                }
            });
        }
    }

    @OnClick({R.id.tv_main_project_name, R.id.tv_main_project_mileage, R.id.bt_main_start, R.id.bt_main_photo_start, R.id.bt_main_photo_end
            , R.id.bt_main_upload, R.id.fbt_new_project, R.id.iv_main_photo_end, R.id.iv_main_photo_start, R.id.iv_main_photo_crop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_main_project_name://项目名 textView
                //显示 输入 项目名 Dialog
                showInputDialog();
                break;
            case R.id.tv_main_project_mileage://里程数 textView
                if (!isNeedUpload) {
                    ToastUtils.show(getString(R.string.main_need_start));
                } else
                    dialogFactory.showInputNumbDialog(projectMileage, new DialogFactory.OnInputClick() {
                        @Override
                        public void onInputClick(String inputText) {
                            tvMainProjectMileage.setText(inputText);
                            projectMileage = inputText;
                            RealmHelper.getInstance().updataMileage(userProject, projectMileage);
                        }
                    });
                break;
            case R.id.bt_main_start://开始 button
                if (TextUtils.isEmpty(projectName)) {
                    showInputDialog();
                } else if (photoStart == null || !photoStart.exists()) {
                    ToastUtils.show(R.string.main_project_photo_start_null);
                } else
                    enterMapAct(); //进入Map activity
                break;
            case R.id.bt_main_photo_start: //开始拍照 button
                if (TextUtils.isEmpty(projectName)) {
                    showInputDialog();
                    ToastUtils.show(R.string.main_project_name_null);
                } else {
                    photoStart = FileUtils.getImageFile(this, Constant.removeString(projectName) + "S.png");
                    takePhoto(photoStart, TAKE_PHOTO_START);
                }
                break;
            case R.id.bt_main_photo_end://结束拍照 button
                photoEnd = FileUtils.getImageFile(this, Constant.removeString(projectName) + "E.png");
                takePhoto(photoEnd, TAKE_PHOTO_END);
                break;
            case R.id.bt_main_upload://上传
                if (TextUtils.isEmpty(projectMileage)) {
                    ToastUtils.show(R.string.main_project_mileage_null);
                    tvMainProjectMileage.performClick();
                } else if (photoEnd == null || !photoEnd.exists()) {
                    ToastUtils.show(R.string.main_project_photo_end_null);
                } else
                    uploadAll();
                break;
            case R.id.fbt_new_project://新建 项目行程
                if (TextUtils.isEmpty(projectName))
                    break;
                dialogFactory.showConfirmDialog(getString(R.string.main_create_project), new DialogFactory.OnBackClick() {
                    @Override
                    public void onBacktClick(boolean b) {
                        dialogFactory.dismiss();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                    }
                });
                break;
            case R.id.iv_main_photo_start://第一张照片
                startActivity(new Intent(MainActivity.this, ShowImageActivity.class)
                        .putExtra("projectName", projectName));
                overridePendingTransition(0, 0);
                break;
            case R.id.iv_main_photo_end://最后一张照片
                startActivity(new Intent(MainActivity.this, ShowImageActivity.class)
                        .putExtra("projectName", projectName)
                        .putExtra("sort", 1));
                overridePendingTransition(0, 0);
                break;
            case R.id.iv_main_photo_crop://最后一张照片
                startActivity(new Intent(MainActivity.this, ShowImageActivity.class)
                        .putExtra("projectName", projectName)
                        .putExtra("sort", 2));
                overridePendingTransition(0, 0);
                break;
        }
    }

    /**
     * 上传 图片与 路径
     */
    private void uploadAll() {
        new UploadProject(this, userProject, new UploadProject.UploadListener() {
            @Override
            public void success() {
                ToastUtils.show(R.string.map_uplaod_file_success);
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void error(int flag, MyException e) {
                if (flag == 2)
                    ToastUtils.show(R.string.map_uplaod_error_image);
                else if (flag == 1)
                    ToastUtils.show(R.string.map_uplaod_error_location);
                else
                    ToastUtils.show(R.string.map_uplaod_error);

                MobclickAgent.reportError(MainActivity.this, e);
            }
        });
    }

    /**
     * 显示 输入 项目名 Dialog
     */
    private void showInputDialog() {
        dialogFactory.showInputDialog(projectName, new DialogFactory.OnInputClick() {
            @Override
            public void onInputClick(String inputText) {
                tvMainProjectName.setEnabled(false);
                //开始拍照时间
                startTime = TimeUtils.currentTime();
                projectName = inputText;
                projectName = projectName + "_" + user.getUsername() + "_" + startTime;
                tvMainProjectName.setText(projectName.split("_")[0]);
            }
        });
    }

    /**
     * 拍照 android7.0
     */
    public void takePhoto(File tempFile, int requestCode) {
        // 判断存储卡是否可以用，可用进行存储
        if (FileUtils.hasSdcard()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri uri = FileUtils.getUri2File(this, tempFile);
            if (Build.VERSION.SDK_INT >= 24) //24 android 7.0
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, requestCode);
        } else {
            ToastUtils.show("SD卡不可用！");
        }
    }

    /**
     * 开始拍照 文字
     */
    public String getStartText() {
        String info = "";
        info += "用户名：" + user.getUsername() + "\n";
        info += "项目名：" + projectName + "\n";
        info += "时间：" + TimeUtils.currentTime();
        return info;
    }


    /**
     * 进入Map activity
     */
    private void enterMapAct() {
        if (userProject == null)
            //新建数据 项目
            userProject = new UserProject();
        userProject.setUserid(user.getUserid() + "");
        userProject.setProject(projectName);
        userProject.setCreateTime(startTime);
        RealmHelper.getInstance().savaTempLocation(userProject);
        startActivityForResult(new Intent(MainActivity.this, MapActivity.class)
                .putExtra("projectName", projectName), ACTIVITY_MAP);
    }

    /**
     * 双击返回键退出程序
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.show(this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                RealmHelper.close();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
