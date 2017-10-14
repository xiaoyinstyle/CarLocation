package com.jskingen.carlocation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jskingen.baselib.activity.base.TitleActivity;
import com.jskingen.baselib.imageload.GlideUtil;
import com.jskingen.carlocation.R;
import com.jskingen.carlocation.utils.DialogFactory;

import butterknife.BindView;

public class PreviewActivity extends TitleActivity {

    @BindView(R.id.iamgeView)
    ImageView iamgeView;

    @Override
    protected int getViewByXml() {
        return R.layout.activity_preview;
    }

    @Override
    protected void setTitle() {
        title.setText("行程预览");

        tv_right.setText(R.string.map_title_back);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFactory(PreviewActivity.this).showConfirmDialog(getString(R.string.map_dialog_back), new DialogFactory.OnBackClick() {
                    @Override
                    public void onBacktClick(boolean b) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String path = getIntent().getStringExtra("path");
//        Bitmap b = BitmapUtils.getImageCompress(path);
//        iamgeView.setImageBitmap(b);
        GlideUtil.Cache.clearImageAllCache(this);
        GlideUtil.getInstance(this).setView( iamgeView, path);
    }

    @Override
    protected void initData() {

    }

}
