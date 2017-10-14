package com.jskingen.carlocation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.jskingen.baselib.activity.base.TitleActivity;
import com.jskingen.baselib.network.exception.MyException;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.baselib.view.LoadingDialog;
import com.jskingen.carlocation.R;
import com.jskingen.carlocation.common.Constant;
import com.jskingen.carlocation.common.utils.FileUtils;
import com.jskingen.carlocation.dao.RealmHelper;
import com.jskingen.carlocation.dao.RequestDaoListener;
import com.jskingen.carlocation.utils.DialogFactory;
import com.jskingen.carlocation.utils.UploadProject;
import com.jskingen.carlocation.model.UserProject;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import yin.style.recyclerlib.adapter.BaseQuickAdapter;
import yin.style.recyclerlib.holder.BaseViewHolder;
import yin.style.recyclerlib.inter.OnItemClickListener;
import yin.style.recyclerlib.inter.OnItemClickLongListener;

public class RecordActivity extends TitleActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<UserProject> list = new ArrayList<>();
    private BaseQuickAdapter adapter;

    private LoadingDialog loadingDialog;
    DialogFactory dialogFactory;
    private boolean dataReadError = false;

    @Override
    protected int getViewByXml() {
        return R.layout.activity_record;
    }

    @Override
    protected void setTitle() {
        title.setText(R.string.title_record);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        dialogFactory = new DialogFactory(this);

        adapter = new BaseQuickAdapter<UserProject>(R.layout.item_record, list) {
            @Override
            protected void setViewHolder(final BaseViewHolder baseViewHolder,  UserProject record, final int i) {
                baseViewHolder.setText(R.id.tv_item_record_project_name, record.getProject().split("_")[0]);
                baseViewHolder.setText(R.id.tv_item_record_project_time, record.getCreateTime());
                baseViewHolder.setText(R.id.tv_item_record_project_mileage, TextUtils.isEmpty(record.getProjectMileage()) ? "0" : record.getProjectMileage());

                //是否可以点击
                final boolean b = (record.getFlagImage() && record.getFlagJson()) ? true : false;
                baseViewHolder.getView(R.id.bt_item_record_upload).setEnabled(!b);

                final UserProject userProject = RealmHelper.getInstance().findProject(record.getProject());
                baseViewHolder.setOnClickListener(R.id.bt_item_record_upload, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!b) {    //上传
                            uploadProject(userProject, baseViewHolder.getView(R.id.bt_item_record_upload));
                        }
                    }
                });
            }
        };
        adapter.setEmptyView(new View(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (list.get(i).getFlagImage() && list.get(i).getFlagJson()
                        && list.get(i).getLngLatBeen() != null && list.get(i).getLngLatBeen().size() != 0)
                    RealmHelper.getInstance().relase(list.get(i).getProject());

                startActivity(new Intent(RecordActivity.this, ShowImageActivity.class)
                        .putExtra("projectName", list.get(i).getProject()));
            }
        });

        //长按删除
        adapter.setOnItemClickLongListener(new OnItemClickLongListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                dialogFactory.showConfirmDialog("需要删除本条记录吗？", new DialogFactory.OnBackClick() {
                    @Override
                    public void onBacktClick(boolean b) {
                        RealmHelper.getInstance().delete(list.get(position));
                        dialogFactory.dismiss();
//                        list.remove(position);
//                        adapter.notifyDataSetChanged();
                        adapter.remove(position);
                    }
                });
            }
        });

        loadingDialog = new LoadingDialog.Builder(this)
                .setMessage("加载数据中..").create();
//        .setCancelable(false);
        loadingDialog.show();
    }

    @Override
    protected void initData() {
        dataReadError = true;
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dataReadError) {
                    loadingDialog.dismiss();
                    ToastUtils.show("数据加载失败！");
                }
            }
        }, 30 * 1000);

        list.clear();
        RealmHelper.getInstance().findProjectsAll(new RequestDaoListener<UserProject>() {
            @Override
            public void onChange(final List<UserProject> userProjects) {
                dataReadError = false;

                list.addAll(userProjects);
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
//                clearPictureCache();
            }
        });

    }

    /**
     * 上传Json
     *
     * @param record
     * @param view
     */
    private void uploadProject(final UserProject record, final View view) {
        new UploadProject(this, record, new UploadProject.UploadListener() {
            @Override
            public void success() {
                view.setEnabled(false);
                ToastUtils.show(R.string.map_uplaod_file_success);
            }

            @Override
            public void error(int flag, MyException e) {
                if (flag == 2)
                    ToastUtils.show(R.string.map_uplaod_error_image);
                else if (flag == 1)
                    ToastUtils.show(R.string.map_uplaod_error_location);
                else
                    ToastUtils.show(R.string.map_uplaod_error);
                MobclickAgent.reportError(RecordActivity.this, e);
            }
        });
    }

    /**
     * 清除已经不存在的图片文件
     */
    private void clearPictureCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (list.size() == 0)
                        return;
                    boolean b;
                    File[] files = FileUtils.getImageFile(RecordActivity.this.getApplicationContext()).listFiles();// 列出所有文件
                    for (File file : files) {
                        b = false;
                        for (UserProject bean : list) {
                            String projectName = Constant.removeString(bean.getProject());
                            if (file.getName().contains(projectName)) {
                                b = true;
                                break;
                            }
                        }
                        if (!b)
                            file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    private void test(int pos) {
//        UserProject userProject = list.get(pos);
//        LngLatBean lngLatBean = new LngLatBean();
//        lngLatBean.setCreatetime(TimeUtils.currentTime());
//        lngLatBean.setLat(30.0000);
//        lngLatBean.setLng(130.0000);
//
//        for (int i = 0; i < 10000; i++) {
//            userProject.getLngLatBeen().add(lngLatBean);
//        }
//        RealmHelper.getInstance().chageUploadFlag(userProject);
//
//
//        for (int i = 0; i <10; i++) {
//            userProject.setCreateTime(TimeUtils.currentTime());
//            RealmHelper.getInstance().savaTempLocation(userProject);
//        }
//
//    }
}
