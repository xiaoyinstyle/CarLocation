package com.jskingen.carlocation.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.jskingen.baselib.activity.base.TitleActivity;
import com.jskingen.baselib.utils.KeyboardUtils;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.baselib.view.MyViewPage;
import com.jskingen.carlocation.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RegisterActivity extends TitleActivity {
    @BindView(R.id.vp_regist)
    MyViewPage vpRegist;

    private MyAdapter adapter;
    protected List<View> lists = new ArrayList<>();

    @Override
    protected int getViewByXml() {
        return R.layout.activity_register;
    }

    @Override
    protected void setTitle() {
//        title.setText(R.string.title_activity_register);
        title.setText("请输入公司帐号");
        tv_right.setText("下一步");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vpRegist.getCurrentItem() == 0) {//下一步
                    vpRegist.setCurrentItem(1, true);
                } else {//完成
                    ToastUtils.show("完成");
                }
            }
        });

        hiddenBackButton();
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setText("取消");
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vpRegist.getCurrentItem() == 0) {//下一步
                    finish();
                } else {//完成
                    vpRegist.setCurrentItem(0, true);
                }
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        adapter = new MyAdapter(lists);
        vpRegist.setAdapter(adapter);
        vpRegist.setCanScroll(false);

        vpRegist.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tv_left.setText("取消");
                    tv_right.setText("下一步");
                    title.setText("请输入公司帐号");
                } else {
                    tv_left.setText("上一步");
                    tv_right.setText("完成");
                    title.setText("提交申请信息");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        vpRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.close(RegisterActivity.this, vpRegist);
            }
        });
    }

    @Override
    protected void initData() {
        lists.clear();
        View view1 = View.inflate(this, R.layout.fragment_register_1, null);
        lists.add(view1);

        View view2 = View.inflate(this, R.layout.fragment_register_2, null);
        lists.add(view2);
        adapter.notifyDataSetChanged();

    }

    class MyAdapter extends PagerAdapter {

        List<View> viewLists;

        public MyAdapter(List<View> lists) {
            viewLists = lists;
        }

        @Override
        public int getCount() {                                                                 //获得size
            // TODO Auto-generated method stub
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View view, int position, Object object) {
            ((ViewPager) view).removeView(viewLists.get(position));
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(viewLists.get(position), 0);
            return viewLists.get(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (vpRegist.getCurrentItem() == 0) {
                //下一步
                finish();
            } else {
                //完成
                vpRegist.setCurrentItem(0, true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
