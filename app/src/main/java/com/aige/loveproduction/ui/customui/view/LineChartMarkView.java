package com.aige.loveproduction.ui.customui.view;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.math.BigDecimal;

public class LineChartMarkView extends MarkerView {
    private final TextView mark_label_to,mark_label,mark_x_to,mark_x,mark_y_to,mark_y;
    private Context mContext;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    public LineChartMarkView(Context context) {
        super(context, R.layout.layout_markview);
        mark_label_to = findViewById(R.id.mark_label_to);
        mark_label = findViewById(R.id.mark_label);
        mark_x_to = findViewById(R.id.mark_x_to);
        mark_x = findViewById(R.id.mark_x);
        mark_y_to = findViewById(R.id.mark_y_to);
        mark_y = findViewById(R.id.mark_y);
        mContext = context;
    }



    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //获取组名称描述
        String label = getChartView().getData().getDataSetForEntry(e).getLabel();
        mark_label.setText(label);
        //获取x轴上的刻度描述
        XAxis xAxis = getChartView().getXAxis();
        IndexAxisValueFormatter indexAxisValueFormatter = (IndexAxisValueFormatter) xAxis.getValueFormatter();
        String xLabel = indexAxisValueFormatter.getFormattedValue((int) e.getX());
        mark_x.setText(xLabel);
        //获取y轴上的值
        //把科学计数法转为正常数值
        BigDecimal bd = new BigDecimal(e.getY());
        mark_y.setText(bd.toPlainString());
        super.refreshContent(e, highlight);
    }

    //显示位置
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f),-getHeight());
    }

    /**
     * 设置字体颜色
     */
    public void setTextColor(int color){
        mark_label_to.setTextColor(mContext.getColor(color));
        mark_label.setTextColor(mContext.getColor(color));
        mark_x_to.setTextColor(mContext.getColor(color));
        mark_x.setTextColor(mContext.getColor(color));
        mark_y_to.setTextColor(mContext.getColor(color));
        mark_y.setTextColor(mContext.getColor(color));
    }
}
