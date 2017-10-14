package com.jskingen.carlocation.popwindow;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.jskingen.carlocation.R;
import com.jskingen.carlocation.activity.LoginActivity;
import com.jskingen.carlocation.activity.RecordActivity;
import com.jskingen.carlocation.activity.StatisticsActivity;

import java.lang.reflect.Field;

/**
 * Created by ChneY on 2017/5/24.
 */

public class MainMenu {

    private Activity activity;
    private PopupWindow popupMenu;//选择菜单
    private int statusBarHeight = 0;

    public MainMenu(Activity activity) {
        this.activity = activity;
        initPopUpMenu();
    }

    /**
     * 显示 Menu 界面
     *
     * @param view
     */
    public void show(View view) {
        if (statusBarHeight == 0)
            statusBarHeight = getStatusBarHeight();
//        setAlpha(0.7f);
        //Popwindow在7.0上有变化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            popupMenu.showAtLocation(view, Gravity.NO_GRAVITY, 0, statusBarHeight + view.getHeight());
        else
            popupMenu.showAsDropDown(view, 0, 0);
    }


    /**
     * 初始化popwindow
     */
    private void initPopUpMenu() {
        View view = activity.getLayoutInflater().inflate(R.layout.popupwindow_main_menu, null);
        Button pop_bt1 = (Button) view.findViewById(R.id.pop_bt1);
        Button pop_bt2 = (Button) view.findViewById(R.id.pop_bt2);
        Button pop_bt3 = (Button) view.findViewById(R.id.pop_bt3);
        Button pop_bt4 = (Button) view.findViewById(R.id.pop_bt4);
        PopupWindowClick popupWindowClick = new PopupWindowClick();
        pop_bt1.setOnClickListener(popupWindowClick);
        pop_bt2.setOnClickListener(popupWindowClick);
        pop_bt3.setOnClickListener(popupWindowClick);
        pop_bt4.setOnClickListener(popupWindowClick);

        popupMenu = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 必须设置
        popupMenu.setBackgroundDrawable(new BitmapDrawable());
        popupMenu.setAnimationStyle(R.style.AnimMenu_sizeChange);
        // 点击其他地方消失
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupMenu != null && popupMenu.isShowing()) {
                    popupMenu.dismiss();
                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
            }
        });
    }

    // 设置背景透明度
    public void setAlpha(float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 获取状态栏的高度
     */
    protected int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = activity.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * PopupWindow点击事件
     */
    private class PopupWindowClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_bt1:
                    //里程统计
                    activity.startActivity(new Intent(activity, StatisticsActivity.class));
                    break;
                case R.id.pop_bt2:
                    //历史记录
                    activity.startActivity(new Intent(activity, RecordActivity.class));
                    break;
                case R.id.pop_bt3:
                    //注销
                    new AlertDialog.Builder(activity)
                            .setTitle("提示")
                            .setMessage("确定要注销此帐号?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    activity.finish();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    break;
            }
            if (popupMenu != null && popupMenu.isShowing()) {
                popupMenu.dismiss();
            }
        }
    }
}
