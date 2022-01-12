package com.aige.loveproduction.mpr;

import android.graphics.PointF;

import com.aige.loveproduction.enums.MprPosition;

/**
 * @author 张文科
 * 主图形数据
 */
public class MprMaster implements MprData{
    private float master_x;
    private float master_y;
    private float finalMasterX;
    private float finalMasterY;
    private static final PointF mPointF = new PointF();

    //*******************获取master四个点的坐标*********************
    public PointF getMasterLeftBottom() {
        mPointF.x = 0;
        mPointF.y = 0;
        return mPointF;
    }
    public PointF getMasterLeftTop(){
        mPointF.x = 0;
        mPointF.y = master_y;
        return mPointF;
    }
    public PointF getMasterRightTop(){
        mPointF.x = master_x;
        mPointF.y = master_y;
        return mPointF;
    }
    public PointF getMasterRightBottom(){
        mPointF.x = master_x;
        mPointF.y = 0;
        return mPointF;
    }

    /**
     * 获取根据位置获取主图形的坐标
     */
    public PointF getMasterXY(MprPosition position) {
        switch (position) {
            case LEFT_BOTTOM:
                return getMasterLeftBottom();
            case LEFT_TOP:
                return getMasterLeftTop();
            case RIGHT_BOTTOM:
                return getMasterRightBottom();
            case RIGHT_TOP:
                return getMasterRightTop();
            default:
                return null;
        }
    }
    /**
     * 计算某个点相对于主图形某个角的距离
     */
    public float getRelativeDistance(float x,float y,MprPosition position) {
        switch (position) {
            case LEFT_BOTTOM:
                return getDistance(x,y,0,0);
            case LEFT_TOP:
                return getDistance(x,y,0,getFinalMasterY());
            case RIGHT_BOTTOM:
                return getDistance(x,y,getFinalMasterX(),0);
            case RIGHT_TOP:
                return getDistance(x,y,getFinalMasterX(),getFinalMasterY());
            default:
                return 0;
        }
    }
    /**
     * 获取两点之间的距离
     * @param startX 点1X坐标
     * @param startY 点1Y坐标
     * @param stopX 点2X坐标
     * @param stopY 点2Y坐标
     */
    public float getDistance(float startX,float startY,float stopX,float stopY) {
        return (float) Math.sqrt(Math.pow(startX-stopX,2)+Math.pow(startY-stopY,2));
    }
    /**
     * 获取某个点在图形四个角中的位置
     * 左下角、左上角、右下角、右上角
     */
    public MprPosition getXYPosition(float x,float y){
        if(getXPosition(x) == MprPosition.LEFT && getYPosition(y) == MprPosition.BOTTOM) {
            return MprPosition.LEFT_BOTTOM;
        } else if(getXPosition(x) == MprPosition.LEFT && getYPosition(y) == MprPosition.TOP) {
            return MprPosition.LEFT_TOP;
        } else if(getXPosition(x) == MprPosition.RIGHT && getYPosition(y) == MprPosition.BOTTOM) {
            return MprPosition.RIGHT_BOTTOM;
        } else if(getXPosition(x) == MprPosition.RIGHT && getYPosition(y) == MprPosition.TOP) {
            return MprPosition.RIGHT_TOP;
        }else{
            return MprPosition.NULL;
        }
    }
    /**
     * 根据x坐标确定该点在哪个位置
     * 左、右
     * */
    private MprPosition getXPosition(float x) {
        float mx = master_x/2;
        if((x == 0 || (x < mx))) return MprPosition.LEFT;
        if((x == mx || (x > mx))) return MprPosition.RIGHT;
        return MprPosition.NULL;
    }
    /**
     * 根据y坐标确定该点在哪个位置
     * 上、下
     * */
    private MprPosition getYPosition(float y) {
        float my = master_y/2;
        if((y == 0 || (y < my))) return MprPosition.BOTTOM;
        if((y == my || (y > my))) return MprPosition.TOP;
        return MprPosition.NULL;
    }
    /**
     * 获取一个点在主图形边界上的位置
     * @return 0：左边，1：上面，2：右边，3：下边
     */
    public MprPosition getPosition(float x, float y) {
        if(x == 0 && y != 0 && y != master_y) return MprPosition.LEFT;
        if(
                (x != 0 && y == master_y && x != master_x)
                        ||(x != 0 && y != 0 && y == master_y && x != master_x)
        ) return MprPosition.TOP;
        if(
                (x == master_x && y != 0 && y != master_y)
                        ||(x != 0 && y != 0 && x == master_x && y != master_y)

        ) return MprPosition.RIGHT;
        if(x != 0 && y == 0 && x != master_x) return MprPosition.BOTTOM;
        return MprPosition.NULL;
    }

    /**
     * 获取两点在主图形边界上的位置
     * 只能获取位于主图形边上的点位
     * @return 0：右边，1：上面，2：右边，3：下边
     */
    public MprPosition getPosition(float startX,float startY,float stopX,float stopY) {
        MprPosition start = getPosition(startX, startY);
        MprPosition stop = getPosition(stopX, stopY);
        if(startX == stopX && stopX == 0) return MprPosition.LEFT;
        if(startY == stopY && stopY == master_y) return MprPosition.TOP;
        if(startX == stopX && stopX == master_x) return MprPosition.RIGHT;
        if(startY == stopY && stopY == 0) return MprPosition.BOTTOM;
        if(
                (start == MprPosition.LEFT && stop == MprPosition.BOTTOM)
                        ||(start == MprPosition.BOTTOM && stop == MprPosition.LEFT)
        ) return MprPosition.LEFT_BOTTOM;
        if(
                (start == MprPosition.LEFT && stop == MprPosition.TOP)
                        ||(start == MprPosition.TOP && stop == MprPosition.LEFT)
        ) return MprPosition.LEFT_TOP;
        if(
                (start == MprPosition.TOP && stop == MprPosition.RIGHT)
                        ||(start == MprPosition.RIGHT && stop == MprPosition.TOP)
        ) return MprPosition.RIGHT_TOP;
        if(
                (start == MprPosition.RIGHT && stop == MprPosition.BOTTOM)
                        ||(start == MprPosition.BOTTOM && stop == MprPosition.RIGHT)
        ) return MprPosition.RIGHT_BOTTOM;

        return MprPosition.NULL;
    }
    //********************************************************


    public float getMaster_x() {
        return master_x;
    }

    public void setMaster_x(float master_x) {
        this.master_x = master_x;
        setFinalMasterX(master_x);
    }

    public float getMaster_y() {
        return master_y;
    }

    public void setMaster_y(float master_y) {
        this.master_y = master_y;
        setFinalMasterY(master_y);
    }

    public float getFinalMasterX() {
        return finalMasterX;
    }
    private void setFinalMasterX(float finalMasterX) {
        this.finalMasterX = finalMasterX;
    }
    public float getFinalMasterY() {
        return finalMasterY;
    }
    private void setFinalMasterY(float finalMasterY) {
        this.finalMasterY = finalMasterY;
    }

    @Override
    public void setScale(float scale) {
        this.master_x *= scale;
        this.master_y *= scale;
    }

    @Override
    public MprData cloneData(MprMaster master) {
        MprMaster newMaster = new MprMaster();
        newMaster.setMaster_x(this.master_x);
        newMaster.setMaster_y(this.master_y);
        return newMaster;
    }

    @Override
    public void initData() {

    }

    @Override
    public String toString() {
        return "MprMaster{" +
                "master_x=" + master_x +
                ", master_y=" + master_y +
                '}';
    }
}
