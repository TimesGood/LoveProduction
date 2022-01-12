package com.aige.loveproduction.mpr;

import android.content.Context;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.ResourcesAction;

/**
 * @author 张文科
 * Mpr图形颜色
 */
public class MprColor implements ResourcesAction {
    private final Context mContext;
    private int master;//主图形
    private int cutting;//切割线
    private int cutting_curve;//贝塞尔曲线
    private int bohrHoriz;//水平钉子
    private int bohrVert;//垂直钉子
    private int bohrVertCross;//十字钉

    private int master_distance_line;//主图形距离线
    private int bohrHoriz_distance_line;//水平钉子距离线
    private int cutting_distance_line;//切割缺角距离线

    public MprColor(Context context) {
        this.mContext = context;
        this.master = getColor(R.color.draw_brown);
        this.cutting = getColor(R.color.draw_green);
        this.cutting_curve = getColor(R.color.white);
        this.bohrHoriz = getColor(R.color.white);
        this.bohrVert = getColor(R.color.white);
        this.bohrVertCross = getColor(R.color.black);
        this.master_distance_line = getColor(R.color.white);
        this.bohrHoriz_distance_line = getColor(R.color.white);
        this.cutting_distance_line = getColor(R.color.white);
    }

    public int getMaster() {
        return master;
    }
    public void setMaster(int master) {
        this.master = master;
    }
    public int getCutting() {
        return cutting;
    }
    public void setCutting(int cutting) {
        this.cutting = cutting;
    }
    public int getCutting_curve() {
        return cutting_curve;
    }
    public void setCutting_curve(int cutting_curve) {
        this.cutting_curve = cutting_curve;
    }
    public int getBohrHoriz() {
        return bohrHoriz;
    }
    public void setBohrHoriz(int bohrHoriz) {
        this.bohrHoriz = bohrHoriz;
    }
    public int getBohrVert() {
        return bohrVert;
    }
    public void setBohrVert(int bohrVert) {
        this.bohrVert = bohrVert;
    }
    public int getMaster_distance_line() {
        return master_distance_line;
    }
    public void setMaster_distance_line(int master_distance_line) {
        this.master_distance_line = master_distance_line;
    }
    public int getBohrHoriz_distance_line() {
        return bohrHoriz_distance_line;
    }
    public void setBohrHoriz_distance_line(int bohrHoriz_distance_line) {
        this.bohrHoriz_distance_line = bohrHoriz_distance_line;
    }
    public int getCutting_distance_line() {
        return cutting_distance_line;
    }
    public void setCutting_distance_line(int cutting_distance_line) {
        this.cutting_distance_line = cutting_distance_line;
    }

    public int getBohrVertCross() {
        return bohrVertCross;
    }

    public void setBohrVertCross(int bohrVertCorss) {
        this.bohrVertCross = bohrVertCorss;
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
