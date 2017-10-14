package com.jskingen.carlocation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.jskingen.baselib.activity.base.TitleActivity;
import com.jskingen.baselib.network.ServiceManager;
import com.jskingen.baselib.network.callBack.OnResponseCallback;
import com.jskingen.baselib.network.exception.MyException;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.BuildConfig;
import com.jskingen.carlocation.R;
import com.jskingen.carlocation.api.LoginService;
import com.jskingen.baselib.network.model.HttpResult;
import com.jskingen.carlocation.common.utils.FileUtils;
import com.jskingen.carlocation.model.User;
import com.jskingen.carlocation.utils.FolderUtils;
import com.jskingen.carlocation.utils.SPCache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

public class LoginActivity extends TitleActivity {

    @BindView(R.id.et_login_user)
    EditText etLoginUser;
    @BindView(R.id.et_login_pw)
    EditText etLoginPw;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.cb_record_password)
    CheckBox cbPassword;
    @BindView(R.id.cb_show_password)
    CheckBox cbShowPassword;
    @BindView(R.id.tv_login_codename)
    TextView tvLoginCodename;

    private String user;
    private String password;

    private LoginService service;

    @Override
    protected int getViewByXml() {
        return R.layout.activity_login;
    }

    @Override
    protected void setTitle() {
        iv_left.setVisibility(View.GONE);
        title.setText(R.string.activity_login_title);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        //是否记住密码
        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPCache.getInstance().setRecordPassword(isChecked);
            }
        });

        //是否显示密码
        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowPassword(isChecked);
            }
        });

        tvLoginCodename.setText("v" + BuildConfig.VERSION_NAME);

//        //关闭GpsService
//        if (GpsService.isServiceWork(this))
//            stopService(new Intent(this, GpsService.class));

        String s = Realm.getDefaultInstance().getPath();
        FolderUtils.readFolder(new File(s).getParentFile().getAbsolutePath());
        Log.e("AAA", "__" + s);
//        File file = new File(FileUtils.getRootPath(this), "/files/default.realm");
//        File file = new File(FileUtils.getRootPath(this), "/files/back.realm");
//        if (file.exists())
//            return;
//        if (!file.getParentFile().exists())
//            file.getParentFile().mkdirs();
//        FolderUtils.copyFile(s, file.getPath());
////        FolderUtils.copyFile(file.getPath(), s);

    }

    @Override
    protected void initData() {
        String[] userName = SPCache.getInstance().getUserName();
        etLoginUser.setText(userName[0]);

        cbPassword.setChecked(SPCache.getInstance().getRecordPassword());
        if (cbPassword.isChecked())
            etLoginPw.setText(userName[1]);
    }

    public void login() {
        user = etLoginUser.getText().toString();
        password = etLoginPw.getText().toString();

        if (service == null)
            service = ServiceManager.create(LoginService.class);

        if (check()) {
            Map<String, String> maps = new HashMap();
            maps.put("username", user);
            maps.put("password", password);
            service.login(maps).enqueue(new OnResponseCallback<HttpResult<User>>(true) {
                @Override
                public void onSuccess(HttpResult<User> httpResult) {
                    //保存相关信息
                    SPCache.getInstance().setUserName(new String[]{user, cbPassword.isChecked() ? password : ""});
                    SPCache.getInstance().setUser(httpResult.getData());

                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                            .putExtra("isUpdata", true));
                    finish();
                }

                @Override
                public void onError(MyException e) {
                    if (e.getCode() == 500)
                        ToastUtils.show(R.string.login_password_error);
                    else
                        ToastUtils.show(e.getDetailMessage());
                }
            });
        }
    }

    /**
     * 检测账户或者密码是否合法
     *
     * @return
     */
    private boolean check() {
        if (TextUtils.isEmpty(user)) {
            ToastUtils.show(R.string.login_user_null);
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(R.string.login_password_null);
            return false;
        }
        return true;
    }

    /**
     * 是否记住密码
     */
    private void isShowPassword(boolean isChecked) {
        if (isChecked) {
            //如果选中，显示密码
            etLoginPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //否则隐藏密码
            etLoginPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @OnClick({R.id.bt_login, R.id.ll_login, R.id.bt_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_regist:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.ll_login:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                break;
        }
    }
}
