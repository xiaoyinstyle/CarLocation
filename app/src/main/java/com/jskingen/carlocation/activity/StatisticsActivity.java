package com.jskingen.carlocation.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.jskingen.baselib.activity.base.TitleActivity;
import com.jskingen.baselib.network.ServiceManager;
import com.jskingen.baselib.network.callBack.OnResponseCallback;
import com.jskingen.baselib.network.exception.MyException;
import com.jskingen.baselib.network.model.HttpResult;
import com.jskingen.baselib.utils.ToastUtils;
import com.jskingen.carlocation.R;
import com.jskingen.carlocation.api.StatisticsService;
import com.jskingen.carlocation.common.Constant;
import com.jskingen.baselib.utils.LogUtils;
import com.jskingen.carlocation.common.utils.TimeUtils;
import com.jskingen.carlocation.common.view.AutofitTextView;
import com.jskingen.carlocation.model.FindMileageBean;
import com.jskingen.carlocation.model.User;
import com.jskingen.carlocation.utils.SPCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import yin.style.recyclerlib.adapter.BaseQuickAdapter;
import yin.style.recyclerlib.holder.BaseViewHolder;

/**
 * 里程统计
 */
public class StatisticsActivity extends TitleActivity {

    @BindView(R.id.tv_statistics_month)
    TextView tvStatisticsMonth;
    @BindView(R.id.tv_statistics_week)
    TextView tvStatisticsWeek;
    @BindView(R.id.tv_statistics_day)
    TextView tvStatisticsDay;
    @BindView(R.id.tv_statistics_value)
    TextView tvStatisticsValue;
    @BindView(R.id.bt_statistics_query)
    Button btStatisticsQuery;
    @BindView(R.id.rv_statistics_list)
    RecyclerView rvStatisticsList;
    @BindView(R.id.ll_statistics_list)
    LinearLayout llStatisticsList;
    @BindView(R.id.tv_statistics_start)
    RadioButton tvStatisticsStart;
    @BindView(R.id.tv_statistics_end)
    RadioButton tvStatisticsEnd;

    private TimePickerView pickerViewStart;
    private TimePickerView pickerViewEnd;
    private MileageAdapter adapter;
    private List<FindMileageBean.DataSelfBean.ProjectmileageBean> list = new ArrayList<>();

    private String timeStart;
    private String timeEnd;

    private StatisticsService service;
    private User user;

    @Override
    protected int getViewByXml() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void setTitle() {
        title.setText("里程统计");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rvStatisticsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MileageAdapter(R.layout.item_statistics, list);
        rvStatisticsList.setAdapter(adapter);

        //选择开始时间
        pickerViewStart = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pickerViewStart.setTime(new Date());
        pickerViewStart.setCyclic(false);
        pickerViewStart.setTitle("请选择结束时间");
        pickerViewStart.setCancelable(true);
        //时间选择后回调
        pickerViewStart.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                timeStart = TimeUtils.getYMD(date);
                tvStatisticsStart.setText(timeStart);
            }
        });

        //选择结束时间
        pickerViewEnd = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pickerViewEnd.setTime(new Date());
        pickerViewEnd.setCyclic(false);
        pickerViewEnd.setTitle("请选择开始时间");
        pickerViewEnd.setCancelable(true);
        //时间选择后回调
        pickerViewEnd.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                timeEnd = TimeUtils.getYMD(date);
                tvStatisticsEnd.setText(timeEnd);
            }
        });
    }

    @Override
    protected void initData() {
        user = SPCache.getInstance().getUser();

        timeEnd = TimeUtils.getYMD();
        tvStatisticsEnd.setText(timeEnd);
        timeStart = TimeUtils.getYMD();
        tvStatisticsStart.setText(timeStart);

        findMileage();
    }

    @OnClick({R.id.tv_statistics_start, R.id.tv_statistics_end, R.id.bt_statistics_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_statistics_start:
                pickerViewStart.show();
                break;
            case R.id.tv_statistics_end:
                pickerViewEnd.show();
                break;
            case R.id.bt_statistics_query:
                if (checkTimeLegal()) {
                    findMileage();
                }
                break;
        }
    }

    /**
     * 检查 选择的时间是否正确
     */
    private boolean checkTimeLegal() {
        try {
            if (TextUtils.isEmpty(timeStart)) {
                ToastUtils.show("选择开始时间");
                return false;
            }
            long start = TimeUtils.getTimestamp(timeStart, "yyyy-MM-dd");
            long end = TimeUtils.getTimestamp(timeEnd, "yyyy-MM-dd");
            LogUtils.e("start_" + start);
            LogUtils.e("end  _" + end);
            if (start <= end)
                return true;
            else {
                ToastUtils.show("结束时间后必须在开始时间之前");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询
     */
    private void findMileage() {
        if (service == null)
            service = ServiceManager.create(StatisticsService.class);
        Map<String, String> maps = new HashMap();
        maps.put("userid", "" + user.getUserid());

        if (!TextUtils.isEmpty(timeStart) && !TextUtils.isEmpty(timeEnd)) {
            maps.put("starttime", "" + timeStart);
            maps.put("endtime", "" + timeEnd);
        }
        service.findMileage(maps).enqueue(new OnResponseCallback<HttpResult<FindMileageBean>>(true) {
            @Override
            public void onSuccess(HttpResult<FindMileageBean> result) {

                tvStatisticsMonth.setText(Html.fromHtml("<b>" + result.getData().getM_mileage() + "</b>"));
                tvStatisticsWeek.setText(Html.fromHtml("<b>" + result.getData().getW_mileage() + "</b>"));
                tvStatisticsDay.setText(Html.fromHtml("<b>" + result.getData().getD_mileage() + "</b>"));

                //查询的总里程
                String totalMileage = result.getData().getDataSelf().getTotalmileage();
                tvStatisticsValue.setText(Html.fromHtml("<b>" + (totalMileage == null ? "0.00" : totalMileage) + "</b>"));

                list.clear();
                list.addAll(result.getData().getDataSelf().getProjectmileage());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(MyException e) {
                if (e.getCode() != 0)
                    ToastUtils.show(e.getDetailMessage());
            }
        });
    }

    private class MileageAdapter extends BaseQuickAdapter<FindMileageBean.DataSelfBean.ProjectmileageBean> {
        public MileageAdapter(@LayoutRes int layoutResId, List mData) {
            super(layoutResId, mData);
        }

        @Override
        protected void setViewHolder(BaseViewHolder baseViewHolder, final FindMileageBean.DataSelfBean.ProjectmileageBean bean, int position) {
            baseViewHolder.setTextColor(R.id.tv_item_statistics_name, R.color.text_black);
            baseViewHolder.setTextColor(R.id.tv_item_statistics_start, R.color.text_black);
            baseViewHolder.setTextColor(R.id.tv_item_statistics_end, R.color.text_black);
            baseViewHolder.setTextColor(R.id.tv_item_statistics_mileage, R.color.text_black);
            baseViewHolder.setTextColor(R.id.tv_item_statistics_screenshot, R.color.text_black);

            baseViewHolder.setText(R.id.tv_item_statistics_name, bean.getName());
            baseViewHolder.setText(R.id.tv_item_statistics_start, bean.getStarttime().replace(" ", "\n"));
            baseViewHolder.setText(R.id.tv_item_statistics_end, " ");
            baseViewHolder.setText(R.id.tv_item_statistics_mileage, bean.getMileage() + "\n(手输:" + bean.getSmileage() + ")");
            baseViewHolder.setTextColor(R.id.tv_item_statistics_screenshot, Color.parseColor("#59b0fb"));
            ((AutofitTextView) baseViewHolder.getView(R.id.tv_item_statistics_screenshot)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

            final ArrayList<String> piclist = getPhotoList(bean.getPhoto());
            baseViewHolder.setText(R.id.tv_item_statistics_screenshot, "查看(" + piclist.size() + "张)");

            baseViewHolder.setOnClickListener(R.id.tv_item_statistics_screenshot, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (piclist.size() == 0)
                        ToastUtils.show("" + getString(R.string.statistics_no_picture));
                    else {
                        startActivity(new Intent(StatisticsActivity.this, ShowImageActivity.class)
                                .putExtra(ShowImageActivity.LIST_PHOTO, piclist));
                    }
                }
            });
        }
    }

    private ArrayList<String> getPhotoList(String string) {
        ArrayList<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(string)) {
            String[] s = string.split(",");
            for (int i = 0; i < s.length; i++) {
                list.add(Constant.baseUrl + s[i].split(":")[1]);
                LogUtils.e("List_" + list.get(i));
            }
        }
        return list;

    }
}
