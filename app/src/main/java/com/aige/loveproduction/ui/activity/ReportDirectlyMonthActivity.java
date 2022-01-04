package com.aige.loveproduction.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.bean.ReportBean;
import com.aige.loveproduction.manager.MPAndroidChartManager;
import com.aige.loveproduction.mvp.contract.ReportContract;
import com.aige.loveproduction.mvp.presenter.ReportPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.customui.view.LineChartMarkView;
import com.aige.loveproduction.util.FormatDateUtil;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportDirectlyMonthActivity extends BaseActivity<ReportPresenter, ReportContract.View>
        implements ReportContract.View , StatusAction {
    private MPAndroidChartManager chartManager;
    private CombinedChart combinedChart;
    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report_directly_month;
    }


    @Override
    protected void initView() {
        combinedChart = findViewById(R.id.combinedChart);
    }

    @Override
    protected void initData() {
        setCenterTitle("直营单值月统计");
        chartManager = new MPAndroidChartManager(this);
        String userName = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
        mPresenter.getReportDirectlyMonth(userName);
    }
    @Override
    public void showLoading() {
        showLoadings();
    }

    @Override
    public void hideLoading() {
        showComplete();
    }

    @Override
    public void onError(String message) {
        showToast(message);
        showEmpty();
    }

    @Override
    public void onReportDirectlyMonthSuccess(List<ReportBean> beans) {
        List<Integer> sign = new ArrayList<>();
        List<Integer> avg = new ArrayList<>();
        List<Integer> total = new ArrayList<>();
        List<Byte> scale = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();
        byte i = 0;
        for (ReportBean bean : beans) {
            sign.add(bean.getMonthCount());
            avg.add((int) bean.getAvgNumber());
            total.add((int) bean.getMonthTotal());
            scale.add(i);
            i++;
            Calendar calendar = FormatDateUtil.FormatDate("yyyy-MM", bean.getMonthValue(), "yyyy-MM-dd");
            xLabels.add(calendar.get(Calendar.MONTH)+1+"月");
        }
        chartManager.setBarData(scale,total,"总额",R.color.blue);
        List<List<Integer>> broken_line = new ArrayList<>();
        broken_line.add(sign);
        broken_line.add(avg);
        List<String> labels = new ArrayList<>();
        labels.add("单量");
        labels.add("均值");
        List<Integer> colors = new ArrayList<>();
        colors.add(R.color.red);
        colors.add(R.color.yellow);
        chartManager.setDrawFilled(false);
        chartManager.setLineData(scale,broken_line,labels,colors,YAxis.AxisDependency.RIGHT);
        chartManager.showCombinedChart(combinedChart,xLabels);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}