package com.aige.loveproduction.ui.activity;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.bean.ReportBean;
import com.aige.loveproduction.manager.MPAndroidChartManager;
import com.aige.loveproduction.mvp.contract.ReportContract;
import com.aige.loveproduction.mvp.presenter.ReportPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

public class ReportOrderDayActivity extends BaseActivity<ReportPresenter, ReportContract.View>
        implements ReportContract.View , StatusAction {
    private MPAndroidChartManager chartManager;
    private LineChart lineChart;

    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report_order_day;
    }

    @Override
    protected void initView() {

        lineChart = findViewById(R.id.lineChart);

    }

    @Override
    protected void initData() {
        setCenterTitle("日下单统计");
        chartManager = new MPAndroidChartManager(this);
        String userName = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
        mPresenter.getReportOrderDay(userName);
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
    public void onReportOrderDaySuccess(List<ReportBean> beans) {
        System.out.println(beans.toString());
        List<Integer> direct = new ArrayList<>();
        List<Integer> channel_one = new ArrayList<>();
        List<Integer> channel_tow = new ArrayList<>();
        List<Integer> scale = new ArrayList<>();
        int i = 0;
        for (ReportBean bean : beans) {
            if("直营".equals(bean.getProjectName())) direct.add(bean.getDayCount());
            if("渠道1部".equals(bean.getProjectName())) channel_one.add(bean.getDayCount());
            if("渠道2部".equals(bean.getProjectName())) {
                channel_tow.add(bean.getDayCount());
                scale.add(i);
                i++;
            }
        }
        List<List<Integer>> broken_line = new ArrayList<>();
        broken_line.add(direct);
        broken_line.add(channel_one);
        broken_line.add(channel_tow);
        List<String> labels = new ArrayList<>();
        labels.add("直营");
        labels.add("一部");
        labels.add("二部");
        List<Integer> colors = new ArrayList<>();
        colors.add(R.color.blue);
        colors.add(R.color.red);
        colors.add(R.color.yellow);
        List<String> xlabel = new ArrayList<>();
        for(int j = 1;j <= scale.size();j++) {
            xlabel.add(j+"日");
        }
        chartManager.setLineData(scale,broken_line,labels,colors,YAxis.AxisDependency.LEFT);
        chartManager.showLineChart(lineChart,xlabel);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}