package com.jskingen.carlocation.common.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jskingen.baselib.activity.base.NormalAcitivity;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public abstract class ImageBaseActivity extends NormalAcitivity {

    private ViewPager viewPager;
    private TextView textView;

    private TextView tvRight;

    private MyAdapter adapter;
    protected List<View> lists = new ArrayList<>();
    private int totalNumb = 0;

    @Override
    protected int getViewByXml() {
        return R.layout.activity_image_base;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvRight = (TextView) findViewById(R.id.tv_right);
        textView = (TextView) findViewById(R.id.textView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        adapter = new MyAdapter(lists);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                textView.setText((position + 1) + "/" + totalNumb);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setOnclick(tvRight);
    }

    protected abstract void setOnclick(TextView tvRight);

    @Override
    protected void initData() {
        //添加数据
        addData();

        textView.setText(1 + "/" + totalNumb);
        adapter.notifyDataSetChanged();
        if (totalNumb == 0) {
            ToastUtils.show("没有图片");
            finish();
        }
        //设置ViewPage
        setViewPage(viewPager);
    }

    protected void addImage(View view) {
        lists.add(view);
    }

    protected abstract void addData();

    public void setViewPage(ViewPager viewPage) {

    }

    protected View getView(Object file) {
        final ImageView imageView = new ImageView(this);
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);

        Glide.with(this)
                .load(file)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<Bitmap>(480, 800) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        mAttacher.update();
                    }
                });
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });

        totalNumb++;
        return imageView;
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
}
