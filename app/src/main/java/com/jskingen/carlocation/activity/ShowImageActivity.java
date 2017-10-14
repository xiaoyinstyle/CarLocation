package com.jskingen.carlocation.activity;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jskingen.carlocation.common.Constant;
import com.jskingen.carlocation.common.activity.ImageBaseActivity;
import com.jskingen.carlocation.common.utils.FileUtils;

import java.io.File;
import java.util.List;


public class ShowImageActivity extends ImageBaseActivity {
    public static final String LIST_PHOTO = "LIST_PHOTO";

    @Override
    protected void setOnclick(TextView tvRight) {

    }

    @Override
    protected void addData() {
        List<String> list = getIntent().getStringArrayListExtra(LIST_PHOTO);

        String projectName = getIntent().getStringExtra("projectName");

        if (list != null && list.size() != 0) {
            for (String url:list) {
                addImage(getView(url));
            }
        } else if (!TextUtils.isEmpty(projectName)) {
            //出去 文件的特殊字符
            projectName = Constant.removeString(projectName);
            File fileStart = FileUtils.getImageFile(this, projectName + "S.png");
            File file = FileUtils.getImageFile(this, projectName + "C.png");
            File fileEnd = FileUtils.getImageFile(this, projectName + "E.png");

            if (fileStart.exists()) {
                addImage(getView(fileStart));
            }
            if (fileEnd.exists()) {
                addImage(getView(fileEnd));
            }
            if (file.exists()) {
                addImage(getView(file));
            }
        }
    }

    @Override
    public void setViewPage(ViewPager viewPage) {
        int sort = getIntent().getIntExtra("sort", 0);
        if (sort != 0)
            viewPage.setCurrentItem(sort);
    }
}
