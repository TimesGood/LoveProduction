package com.aige.loveproduction.manager;

import android.content.Context;
import android.graphics.Color;

import com.aige.loveproduction.R;
import com.aige.loveproduction.ui.customui.view.LineChartMarkView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表绘制管理
 * 柱形图、折线图、混合图（柱形、折线）已经配置好，其他图形官网自个找
 * @author 张文科
 */
public class MPAndroidChartManager {
    private YAxis leftAxis;//左侧Y轴
    private YAxis rightAxis;//右侧Y轴
    private XAxis xAxis;//X轴
    private Legend legend;//图例
    private Context mContext;
    private BarData barData;
    private LineData lineData;
    //折线图是否填充
    private boolean drawFilled = true;

    public MPAndroidChartManager(Context context) {
        this.mContext = context;
    }
    //****************************************************柱形图绘制************************************************************
    /**
     * 绘制多组柱形图
     * @param barChart 柱形View对象
     * @param values x轴刻度描述
     */
    public void showBarChart(BarChart barChart,List<String> values) {
        initBarChart(barChart);
        BarData data = getBarData();
        xAxis.setLabelCount(values.size() - 1, false);//显示刻度数量，不设置的话，默认只显示6个
        xAxis.setAxisMinimum(0f);//最小刻度
        xAxis.setAxisMaximum(values.size());//最大刻度
        //将x坐标的值显示在中央
        xAxis.setCenterAxisLabels(true);
        //修改刻度值的显示
        xAxis.setValueFormatter(new IndexAxisValueFormatter(values));
        xAxis.setTextSize(1f);
        //(barWidth + barSpace)*barAmount+groupSpace=1这个公式必须成立
        int barAmount = data.getDataSets().size();
        float groupSpace = 0.3f;//组与组之间的间距
        float barSpace = 0f;//组内柱子之间的间距
        float barWidth = (1f-groupSpace) / barAmount - barSpace;//柱子宽度
        data.setBarWidth(barWidth);//柱子的宽度
        data.groupBars(0, groupSpace, barSpace);//分组
        barChart.setData(data);
        setMarkView(barChart);
    }

    /**
     * 获取柱形图绘制参数
     */
    public BarData getBarData(){
        if(this.barData == null) throw new NullPointerException("未设置BarData！");
        return this.barData;
    }

    /**
     * 设置单组BarData
     * @param xAxisValues x轴刻度值
     * @param yAxisValues y轴刻度值
     * @param label 柱形名称
     * @param color 柱形颜色
     */
    public void setBarData(List<Byte> xAxisValues,List<Integer> yAxisValues,String label,int color) {
        barData = new BarData();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < yAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(entries, label);
        initBarDataSet(barDataSet,color);
        barData.addDataSet(barDataSet);
    }

    /**
     * 设置多组BarData数据
     * @param xAxisValues x轴刻度值
     * @param yAxisValues y轴柱形组数
     * @param labels 柱形名称
     * @param colours 每组颜色值
     * @return
     */
    public void setBarData(List<Byte> xAxisValues,List<List<Integer>> yAxisValues,List<String> labels,List<Integer> colours){
        barData = new BarData();
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            if(xAxisValues.size() != yAxisValues.get(i).size()) throw new IllegalStateException("多组柱形图数量不一致");
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {
                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i).get(j)));

            }
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            initBarDataSet(barDataSet,colours.get(i));
            barData.addDataSet(barDataSet);
        }
    }

    /**初始化柱形图设置**/
    private void initBarChart(BarChart barChart) {
        /*图表设置*/
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //不显示边框
        barChart.setDrawBorders(false);
        //这是动画效果
        barChart.animateY(1000, Easing.Linear);
        barChart.animateX(1000,Easing.Linear);
        //不显示右下角描述内容
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        //XY轴的设置
        //X轴设置显示位置在底部
        setXAxis(barChart);
        setLRAxis(barChart);
        setLegend(barChart);
    }
    /**
     * 初始化BarDataSet设置
     * @param color 数值颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color){
        barDataSet.setColor(mContext.getColor(color));
        barDataSet.setColor(mContext.getColor(color));
        barDataSet.setValueTextColor(mContext.getColor(color));//数值颜色
        barDataSet.setValueTextSize(10f);//数值大小
        barDataSet.setDrawValues(false);//是否绘制数值
    }
    //*****************************************************绘制折线图***********************************************************

    /**
     * 绘制多条折线图
     * @param lineChart 折线View对象
     * @param values x轴刻度描述
     */
    public void showLineChart(LineChart lineChart,List<String> values) {
        initLineChart(lineChart);
        LineData data = getLineData();
        xAxis.setLabelCount(data.getDataSets().get(0).getEntryCount() - 1, false);//显示刻度数量，不设置的话，默认只显示6个
        xAxis.setAxisMinimum(-1f);//最小刻度
        xAxis.setAxisMaximum(data.getDataSets().get(0).getEntryCount());//最大刻度
        //将x坐标的值显示在中央
//        xAxis.setCenterAxisLabels(true);
        //修改刻度值的显示
        IndexAxisValueFormatter indexAxisValueFormatter = new IndexAxisValueFormatter(values);
        xAxis.setValueFormatter(indexAxisValueFormatter);
        xAxis.setTextSize(1f);
        xAxis.setDrawGridLines(false);//去掉网格显示
        rightAxis.setEnabled(false);//右边坐标不显示
        leftAxis.setLabelCount(7,false);
        lineChart.setData(data);
        //点击坐标时显示相应数值
        setMarkView(lineChart);
    }

    /**
     * 获取折线图绘制参数
     */
    public LineData getLineData(){
        if(this.lineData == null) throw new NullPointerException("未设置LineData！");
        return this.lineData;
    }

    /**
     * 设置单组LineData
     * @param xAxisValues x轴刻度值
     * @param yAxisValues y轴刻度值
     * @param label 柱形名称
     * @param color 柱形颜色
     */
    public void setLineData(List<Byte> xAxisValues, List<Integer> yAxisValues, String label, int color, YAxis.AxisDependency axisDependency) {
        lineData = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < yAxisValues.size(); i++) {
            entries.add(new Entry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, label);
        initLineDataSet(lineDataSet,color, axisDependency,LineDataSet.Mode.LINEAR);
        lineData.addDataSet(lineDataSet);
    }

    /**
     * 获取LineData数据
     * @param xAxisValues x轴刻度值
     * @param yAxisValues y轴柱形组数
     * @param labels 柱形名称
     * @param colours 每组颜色值
     */
    public void setLineData(List<Byte> xAxisValues, List<List<Integer>> yAxisValues,
                                 List<String> labels, List<Integer> colours,YAxis.AxisDependency axisDependency){
        lineData = new LineData();
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<Entry> entries = new ArrayList<>();
            if(xAxisValues.size() != yAxisValues.get(i).size()) throw new IllegalStateException("多组折线图数量不统一");
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {
                entries.add(new Entry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }
            LineDataSet barDataSet = new LineDataSet(entries, labels.get(i));
            initLineDataSet(barDataSet,colours.get(i),axisDependency, LineDataSet.Mode.LINEAR);
            lineData.addDataSet(barDataSet);
        }
    }

    /**
     * 初始化折线图设置
     */
    private void initLineChart(LineChart lineChart) {
        /*图表设置*/
        //背景颜色
        lineChart.setBackgroundColor(Color.WHITE);
        //不显示网格
        lineChart.setDrawGridBackground(false);
        //不显示边框
        lineChart.setDrawBorders(false);
        //这是动画效果
        lineChart.animateY(1000, Easing.Linear);
        lineChart.animateX(1000,Easing.Linear);
        //不显示右下角描述内容
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
        //XY轴的设置
        //X轴设置显示位置在底部
        setXAxis(lineChart);
        setLRAxis(lineChart);
        setLegend(lineChart);
    }

    /**
     * 初始化LineDataSet设置
     * @param color 数值颜色
     * @param axisDependency 参照坐标
     * @param mode 折线显示模式
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color,YAxis.AxisDependency axisDependency, LineDataSet.Mode mode){
        lineDataSet.setColor(mContext.getColor(color));
        lineDataSet.setValueTextColor(mContext.getColor(color));//数值颜色
        lineDataSet.setValueTextSize(10f);//数值大小
        lineDataSet.setDrawValues(false);//是否绘制数值
        //这是折线图填充
        lineDataSet.setDrawFilled(drawFilled);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15f);

        //以哪边Y坐标为准
        if(axisDependency == null) {
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        }else{
            lineDataSet.setAxisDependency(axisDependency);
        }

        if(mode == null) {
            //默认显示折线
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        }else{
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 设置折线图是否填充
     */
    public void setDrawFilled(boolean flag){
        this.drawFilled = flag;
    }

    //*****************************************************绘制混合图表*******************************************************

    /**
     * 绘制混合报表
     * @param combinedChart 混合对象
     * @param xLabels X刻度表示
     */
    public void showCombinedChart(CombinedChart combinedChart,List<String> xLabels) {
        initCombinedChart(combinedChart);
        CombinedData data = new CombinedData();
        BarData barData = getBarData();
        barData.setBarWidth(0.5f);//柱形宽度调整一下
        data.setData(barData);
        data.setData(getLineData());
        IndexAxisValueFormatter indexAxisValueFormatter = new IndexAxisValueFormatter(xLabels);
        xAxis.setValueFormatter(indexAxisValueFormatter);
        xAxis.setAxisMinimum(-0.5f);//最小刻度左边留白一点
        xAxis.setAxisMaximum((float) (xLabels.size()-0.5));//右边留白一点
        xAxis.setLabelCount(xLabels.size() - 1, false);//显示刻度数
        xAxis.setDrawGridLines(false);//取消网格显示
        rightAxis.setDrawGridLines(false);
        setMarkView(combinedChart);
        combinedChart.setData(data);
    }

    /**
     * 初始化混合图设置
     */
    private void initCombinedChart(CombinedChart combinedChart){
        /*图表设置*/
        //背景颜色
        combinedChart.setBackgroundColor(Color.WHITE);
        //不显示网格
        combinedChart.setDrawGridBackground(false);
        //背景阴影
        combinedChart.setDrawBarShadow(false);
        combinedChart.setHighlightFullBarEnabled(false);
        //不显示边框
        combinedChart.setDrawBorders(false);
        //这是动画效果
        combinedChart.animateY(1000, Easing.Linear);
        combinedChart.animateX(1000,Easing.Linear);
        //不显示右下角描述内容
        Description description = new Description();
        description.setEnabled(false);
        combinedChart.setDescription(description);
        //XY轴的设置
        //X轴设置显示位置在底部
        setXAxis(combinedChart);
        setLRAxis(combinedChart);
        setLegend(combinedChart);
    }

    //***************************************************通用设置********************************************
    /**
     * X轴刻度设置
     */
    private void setXAxis(BarLineChartBase chart){
        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
    }

    /**
     * 左右刻度设置
     */
    private void setLRAxis(BarLineChartBase chart){
        leftAxis = chart.getAxisLeft();
        rightAxis = chart.getAxisRight();
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setGranularityEnabled(true);
        leftAxis.setGranularityEnabled(true);
    }

    /**
     * 图例设置
     */
    private void setLegend(BarLineChartBase chart){
        //折线图例 标签 设置
        legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }
    /**
     * 设置点击图例时的显示数值
     */
    private void setMarkView(Chart chart) {
        //设置点击坐标后，显示自定义数值
        LineChartMarkView markView = new LineChartMarkView(mContext);
        markView.setTextColor(R.color.black);
        markView.setChartView(chart);
        chart.setMarker(markView);
    }
}
