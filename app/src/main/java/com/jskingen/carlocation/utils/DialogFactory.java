package com.jskingen.carlocation.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ChneY on 2017/4/11.
 */

public class DialogFactory {

    private ProgressDialog progressDialog;
    private AlertDialog inputDialog;//mainAcivity 输入 项目名称
    private AlertDialog inputNumbDialog;//mainAcivity 输入 项目名称
    private AlertDialog confirmDialog;// 返回确认
    private EditText etProject;
    private EditText etMileage;
    private Context context;

    public DialogFactory(Context context) {
        this.context = context;
    }

    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
        }
        if (!TextUtils.isEmpty(title))
            progressDialog.setTitle(title);
        if (!TextUtils.isEmpty(message))
            progressDialog.setMessage(message);
        progressDialog.show();
    }


    /**
     * 输入项目名称
     */
    public void showInputDialog(String inputText, final OnInputClick onInputClick) {
        if (inputDialog == null) {
            etProject = new EditText(context);
            etProject.setSingleLine();
            etProject.setHint(R.string.et_dialog_hint);
            inputDialog = new AlertDialog.Builder(context)
                    .setTitle("项目名称")
                    .setView(etProject, 50, 20, 50, 20)
                    .setCancelable(false)
                    .setPositiveButton("确认", null)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
        }
        etProject.setText(inputText);
        inputDialog.show();
        inputDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyNickname(etProject.getText().toString()))
                    ToastUtils.show(R.string.main_project_name_error);
                else {
                    onInputClick.onInputClick(etProject.getText().toString());
                    dismiss();
                }
            }
        });
    }

    /**
     * 输入项目 里程数
     */
    public void showInputNumbDialog(String inputText, final OnInputClick onInputClick) {
        if (inputNumbDialog == null) {
            etMileage = new EditText(context);
            etMileage.setSingleLine();
            etMileage.setHint(R.string.et_dialog_mileage_hint);
            //小数
            etMileage.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etMileage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

            inputNumbDialog = new AlertDialog.Builder(context)
                    .setTitle("里程数（单位：km）")
                    .setView(etMileage, 50, 20, 50, 20)
                    .setCancelable(false)
                    .setPositiveButton("确认", null)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
        }
        etMileage.setText(inputText);
        inputNumbDialog.show();
        inputNumbDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numb = verifyNumb(etMileage.getText().toString());
                if (numb.length() == 0)
                    ToastUtils.show(R.string.main_project_mileage_error);
                else {
                    onInputClick.onInputClick(numb);
                    dismiss();
                }
            }
        });
    }

    /**
     * 验证数字
     *
     * @param s
     * @return
     */
    private String verifyNumb(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        else if (s.equals("."))
            return "0.0";
        else
            return "" + Float.parseFloat(s);
    }

    public void dismiss() {
        if (inputDialog != null)
            inputDialog.dismiss();
        if (confirmDialog != null)
            confirmDialog.dismiss();
        if (progressDialog != null)
            progressDialog.dismiss();
        if (inputNumbDialog != null)
            inputNumbDialog.dismiss();
    }

    /**
     * 弹窗提示框，两个按钮
     *
     * @param messasge
     * @param onBackClick
     */
    public void showConfirmDialog(String messasge, final OnBackClick onBackClick) {
        if (confirmDialog == null) {
            confirmDialog = new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage(messasge)
//                    .setCancelable(false)
//                    .setNegativeButton("直接返回", null)
                    .setPositiveButton("确认", null)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmDialog.dismiss();
                        }
                    }).create();
        }
        confirmDialog.show();
        confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传数据
                onBackClick.onBacktClick(true);
            }
        });
    }

    /**
     * 弹窗提示框，两个按钮
     *
     * @param messasge
     * @param onBackClick
     */
    public void showConfirmDialogPro(String messasge, final OnBackClick onBackClick) {
        if (confirmDialog == null) {
            confirmDialog = new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage(messasge)
//                    .setCancelable(false)
                    .setNeutralButton("直接返回", null)
                    .setPositiveButton("确认", null)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmDialog.dismiss();
                        }
                    }).create();
        }
        confirmDialog.show();
        confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传数据
                onBackClick.onBacktClick(true);
            }
        });
        confirmDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //强制退出
                onBackClick.onBacktClick(false);
            }
        });
    }

    // 验证昵称
    private boolean verifyNickname(String nickname) {
        if (nickname == null || nickname.length() == 0) {
            return false;
        }
        if (nickname.length() < 2 || nickname.length() > 20) {
            return false;
        }
        String str = "[a-zA-Z0-9_\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(nickname);
        return m.matches();
    }

    public interface OnInputClick {
        void onInputClick(String inputText);
    }

    public interface OnBackClick {
        void onBacktClick(boolean b);
    }
}
