package com.aige.loveproduction.mpr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.ResourcesAction;
import com.aige.loveproduction.enums.MprPosition;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 张文科
 * 板件渲染
 * 自定义图形图形渲染
 * 支持拖拽、缩放，双击图形置中
 */
public class MprView extends View implements ResourcesAction {
    //坐标
    private final RectF mRectF;
    private final PointF mPointF = new PointF();
    //画笔
    private final Paint mPaint;
    //路径
    private final Path mPath = new Path();
    //储存主图形的宽高
    private float viewWidth,viewHeight;
    //外面传递的图形参数
    private MprDataWrap data;
    //缩放比例
    private float scale = 1f;
    //图形初始坐标
    private float centerX = 0f,centerY = 0f;
    private final PointF centerPoint = new PointF();
    //**********************************初始设置****************************************************
    private final MprColor mprColor;
    private boolean mpr_bohrcoord_describe = true;
    private boolean mpr_distance_line = true;
    private boolean mpr_cuttingcoord_describe = true;
    //*********************************************************************************************
    //**********************************以下是实现图形移动、缩放功能的属性***********************************
    //触摸的不同状态的表示：
    private static final int NONE = 0;//未接触
    private static final int DRAG = 1;//单指
    private static final int ZOOM = 2;//双指
    private int mode = NONE;
    //定义第一个按下的点，两只接触点的重点，以及出事的两指按下的距离：
    //图形移动、缩放前的坐标
    private final PointF coordinate = new PointF();
    //手指按下时的位置
    private final PointF startPoint = new PointF();
    //两指按下时，两指之间的距离
    private static float oldDist = 1f;
    //双击最长等待时间
    private static final int MAX_LONG_PRESS_TIME=350;
    private long oldTime;
    private long newTime = 0;
    //*************************************************************************************************
    public MprView(Context context) {
        this(context,null);
    }

    public MprView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();
        mPaint = new Paint();
        mprColor = new MprColor(context);
        initPaint();
    }
    public void initSetting(){
        mpr_bohrcoord_describe = SharedPreferencesUtils.getBoolean(getContext(),"mprSettings","mpr_bohrcoord_describe");
        mpr_distance_line = SharedPreferencesUtils.getBoolean(getContext(),"mprSettings","mpr_distance_line");
        mpr_cuttingcoord_describe = SharedPreferencesUtils.getBoolean(getContext(),"mprSettings","mpr_cuttingcoord_describe");
        invalidate();
    }
    /**
     * 初始画笔样式
     */
    private void initPaint() {
        mPaint.setColor(getColor(R.color.white));//画笔颜色
        mPaint.setStyle(Paint.Style.FILL);//画笔填充样式
        mPaint.setStrokeWidth(10f);//画笔粗细
        mPaint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setXfermode(null);//清除图像混合
    }
    /**
     * 图形坐标参数
     */
    public void setData(MprDataWrap data) {
        if(data.getMaster() == null) throw new NullPointerException("图形数据为空！");
        this.data = data;
        //恢复初始状态
        scale = 1f;
        MprMaster master = data.getMaster();
        centerX = viewWidth/2-master.getMaster_x()/2;
        centerY = viewHeight/2+master.getMaster_y()/2;
        centerPoint.set(centerX,centerY);
        initSetting();
    }

    /**
     * 当控件大小发生改变时调用，所以在初始化时会调用一次
     * @param w 新宽
     * @param h 新高
     * @param oldw 旧宽
     * @param oldh 旧高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        //初始坐标点
        centerX = viewWidth/2f;
        centerY = viewHeight/2f;
        centerPoint.set(centerX,centerY);
    }
    //开始绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        parseData(canvas);
    }
    //解析整理数据，绘制要绘制的图形
    private void parseData(Canvas canvas) {
        if(data == null) return;
        MprExtraData extraData = data.getExtraModul();
        MprMaster master = data.getMaster();
        centerX = viewWidth/2-master.getMaster_x()/2;
        centerY = viewHeight/2+master.getMaster_y()/2;
        initPaint();
        mPaint.setColor(mprColor.getMaster());
        drawRectangle(canvas,centerPoint.x, centerPoint.y, master.getMaster_x(), master.getMaster_y());

        //切割线
        if(data.getCuttingSize() != 0) {
            initPaint();
            mPaint.setColor(mprColor.getCutting());
            mPaint.setStrokeWidth(extraData.getStrokeWidth());
            mPaint.setStyle(Paint.Style.STROKE);
            MprCuttingData cuttingCurve = data.getCuttingCurve();
            if(cuttingCurve != null) drawPathQuad(canvas,cuttingCurve.getMap());
            MprCuttingData mprCuttingData = data.getCuttingEncircle();
            if(mprCuttingData != null) drawPath(canvas,mprCuttingData,extraData.getTextSize(),extraData.getOffsetX(),extraData.getOffsetY(),false,mpr_cuttingcoord_describe);
        }
        //绘制侧面钉子

        if(data.getBohrHorizSize() != 0) {
            initPaint();
            mPaint.setColor(mprColor.getBohrHoriz());
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(1f);
            for(byte i = 0;i < data.getBohrHorizSize();i++) {
                MprBohrData mprData = data.getMprBohrHorizData(i);
                drawNail(canvas,mprData.getXA(),mprData.getYA(),mprData.getDU(),mprData.getTI(),mprData.getPosition2());
            }
        }

        //绘制表面钉子
        if(data.getBohrVertSize() != 0) {
            for(byte i = 0;i < data.getBohrVertSize();i++) {
                MprBohrData mprData = data.getMprBohrVertData(i);
                initPaint();
                mPaint.setColor(mprColor.getBohrVert());
                drawText(canvas, mprData.getCoordText(), extraData.getTextSize(), mprData.getXA(), mprData.getYA(), 0f, -mprData.getDU(),mpr_bohrcoord_describe);
                drawArc(canvas,0,360,mprData.getDU(),mprData.getXA(), mprData.getYA());
            }
        }
        //绘制表面十字钉
        if(data.getBohrVertCrossSize() != 0) {
            for(byte i = 0;i < data.getBohrVertCrossSize();i++) {
                MprBohrData mprData = data.getMprBohrVertCrossData(i);
                initPaint();
                mPaint.setColor(mprColor.getBohrVert());
                drawText(canvas, mprData.getCoordText(), extraData.getTextSize(), mprData.getXA(), mprData.getYA(), 0f, -mprData.getDU(),mpr_bohrcoord_describe);
                drawArc(canvas,0,360,mprData.getDU(),mprData.getXA(), mprData.getYA());
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1f);
                mPaint.setColor(mprColor.getBohrVertCross());
                drawArc(canvas,45,90,mprData.getDU(),mprData.getXA(), mprData.getYA());
                drawArc(canvas,135,90,mprData.getDU(),mprData.getXA(), mprData.getYA());
                drawArc(canvas,225,90,mprData.getDU(),mprData.getXA(), mprData.getYA());
                drawArc(canvas,315,90,mprData.getDU(),mprData.getXA(), mprData.getYA());
            }
        }
        //绘制距离线
        if(mpr_distance_line) {
            initPaint();
            mPaint.setStrokeWidth(1f);
            mPaint.setColor(mprColor.getMaster_distance_line());
            drawDistanceMasterLine(canvas,master,extraData.getTextSize(),extraData.getMasterDistance(),extraData.getTextDistance());
            mPaint.setColor(mprColor.getBohrHoriz_distance_line());
            drawDistanceBohrLine(canvas,extraData.getTextSize(),extraData.getMasterDistance(),extraData.getTextDistance());
            mPaint.setColor(mprColor.getCutting_distance_line());
            drawDistanceCuttingLine(canvas,data.getCuttingEncircle(),extraData.getTextSize(),extraData.getMasterDistance()/2,extraData.getTextDistance());
        }

    }
    /**
     * 画矩形
     * @param x 矩形左下角x坐标
     * @param y 矩形左下角y坐标
     * @param width 矩形宽高
     * @param height 矩形宽高
     */
    private void drawRectangle(Canvas canvas,float x,float y,float width,float height) {
        PointF xy = getXY(width/2, height/2);
        mRectF.left = x;
        mRectF.bottom = y;
        mRectF.right = xy.x+width/2;
        mRectF.top = xy.y-height/2;
        canvas.drawRect(mRectF,mPaint);
    }
    /**
     * 绘制主图形距离线
     * @param textSize 文字大小
     * @param masterDistance 主图形线
     * @param textDistance 线距中间缺口大小
     */
    private void drawDistanceMasterLine(Canvas canvas,MprMaster master,float textSize,float masterDistance,float textDistance){
        //master距线
        drawLineWrap(canvas,masterDistance,textDistance,String.valueOf(master.getFinalMasterY()),textSize,0,0,0, master.getMaster_y());
        drawLineWrap(canvas,masterDistance,textDistance,String.valueOf(master.getFinalMasterX()),textSize,0, master.getMaster_y(), master.getMaster_x(), master.getMaster_y());
        drawLineWrap(canvas,masterDistance,textDistance,String.valueOf(master.getFinalMasterY()),textSize, master.getMaster_x(),0, master.getMaster_x(), master.getMaster_y());
        drawLineWrap(canvas,masterDistance,textDistance,String.valueOf(master.getFinalMasterX()),textSize,0,0, master.getMaster_x(),0);
    }
    /**
     * @param textSize 字体大小
     * @param masterDistance 主图形线距
     * @param textDistance 线距中间缺口大小
     */
    private void drawDistanceBohrLine(Canvas canvas,float textSize,float masterDistance,float textDistance) {
        float ave = masterDistance / 10;
        float step = ave;
        for(int i = 0;i < data.getBohrHorizSize();i++) {
            MprBohrData bohrData = data.getMprBohrHorizData(i);
            MprPosition position = bohrData.getPosition2();
            switch (position) {
                case LEFT:
                    drawLineWrap(canvas,step,textDistance,String.valueOf(bohrData.getFinalY()),textSize,0,0,bohrData.getXA(),bohrData.getYA());
                    break;
                case TOP:
                    drawLineWrap(canvas,step,textDistance,String.valueOf(bohrData.getFinalX()),textSize,0,data.getMaster().getMaster_y(),bohrData.getXA(),bohrData.getYA());
                    break;
                case RIGHT:
                    drawLineWrap(canvas,step,textDistance,String.valueOf(bohrData.getFinalY()),textSize,data.getMaster().getMaster_x(),0,bohrData.getXA(),bohrData.getYA());
                    break;
                case BOTTOM:
                    drawLineWrap(canvas,step,textDistance,String.valueOf(bohrData.getFinalX()),textSize,0,0,bohrData.getXA(),bohrData.getYA());
                    break;
            }
            step += ave;
            if(i < data.getBohrHorizSize()-1) {
                MprBohrData next = data.getMprBohrHorizData(i+1);
                MprPosition nextPosition = next.getPosition2();
                if(nextPosition != position) {
                    step = masterDistance / 10;
                }
            }
        }
    }

    /**
     * 绘制缺口的距离线
     * @param cuttingData 切割线数组中最后一组数据
     * @param textSize 字体大小
     * @param masterDistance 与主图形相距距离
     * @param textDistance
     */
    private void drawDistanceCuttingLine(Canvas canvas, MprCuttingData cuttingData,float textSize,float masterDistance,float textDistance) {

        List<float[]> point = cuttingData.getPoint(0);
        List<float[]> point1 = cuttingData.getPoint(1);
        if(point == null) return;
        for (int i = 0;i < point.size();i++) {
            if(i % 2 != 0) {
                float[] last = point.get(i - 1);
                float[] floats = point.get(i);
                MprPosition position = data.getMaster().getPosition(last[0], last[1], floats[0], floats[1]);
                PointF masterXY = data.getMaster().getMasterXY(position);
                float lastDistance = data.getMaster().getRelativeDistance(point1.get(i - 1)[0], point1.get(i - 1)[1], position);
                float relativeDistance = data.getMaster().getRelativeDistance(point1.get(i)[0], point1.get(i)[1], position);
                drawLineWrap(canvas,masterDistance,textDistance,String.valueOf(lastDistance),textSize,masterXY.x,masterXY.y,last[0],last[1]);
                drawLineWrap(canvas,masterDistance,textDistance,String.valueOf(relativeDistance),textSize,masterXY.x,masterXY.y,floats[0],floats[1]);
            }
        }
    }

    /**
     * 绘制距离线
     * @param masterDistance 与主图形之间的间隔
     * @param textSize 字体大小
     * @param startX 开始X坐标
     * @param startY 开始Y坐标
     * @param stopX 结束X坐标
     * @param stopY 结束Y坐标
     */
    private void drawLineWrap(Canvas canvas,float masterDistance,float textDistance,String text,float textSize,float startX,float startY,float stopX,float stopY){
        float[] xyMiddle = data.getXYMiddle(startX, startY, stopX, stopY);//中点坐标
        float distance = data.getDistance(startX, startY, stopX, stopY);//两点之间的距离
        if(textDistance > distance/2) textDistance = distance/3;
        switch (data.getMaster().getPosition(startX,startY,stopX,stopY)) {
            case LEFT:
                //调整与主图形的间距
                startX -= masterDistance;
                stopX -= masterDistance;

                if(xyMiddle[1] > startY) {
                    drawLine(canvas,startX,startY,stopX,xyMiddle[1]-textDistance);
                    drawLine(canvas,stopX,stopY,startX,xyMiddle[1]+textDistance);
                }else{
                    drawLine(canvas,startX,startY,stopX,xyMiddle[1]+textDistance);
                    drawLine(canvas,stopX,stopY,startX,xyMiddle[1]-textDistance);
                }
                drawLine(canvas,startX,startY,startX+masterDistance,startY);
                drawLine(canvas,stopX,stopY,stopX+masterDistance,stopY);
                drawTextOnPath(canvas,text,textSize,0,xyMiddle[1],masterDistance,MprPosition.LEFT,true);
                break;
            case TOP:
                startY += masterDistance;
                stopY += masterDistance;
                if(xyMiddle[0] > startX) {
                    drawLine(canvas,startX,startY,xyMiddle[0]-textDistance,stopY);
                    drawLine(canvas,stopX,stopY,xyMiddle[0]+textDistance,stopY);
                }else{
                    drawLine(canvas,startX,startY,xyMiddle[0]+textDistance,stopY);
                    drawLine(canvas,stopX,stopY,xyMiddle[0]-textDistance,stopY);
                }
                drawLine(canvas,startX,startY,startX,startY-masterDistance);
                drawLine(canvas,stopX,stopY,stopX,stopY-masterDistance);
                drawTextOnPath(canvas,text,textSize,xyMiddle[0],startY,masterDistance,MprPosition.TOP,true);
                break;
            case RIGHT:
                startX += masterDistance;
                stopX += masterDistance;
                if(xyMiddle[1] > startY) {
                    drawLine(canvas,startX,startY,stopX,xyMiddle[1]-textDistance);
                    drawLine(canvas,stopX,stopY,stopX,xyMiddle[1]+textDistance);
                }else{
                    drawLine(canvas,startX,startY,stopX,xyMiddle[1]+textDistance);
                    drawLine(canvas,stopX,stopY,stopX,xyMiddle[1]-textDistance);
                }
                drawLine(canvas,startX,startY,startX-masterDistance,startY);
                drawLine(canvas,stopX,stopY,stopX-masterDistance,stopY);
                drawTextOnPath(canvas,text,textSize,startX,xyMiddle[1],masterDistance,MprPosition.RIGHT,true);
                break;
            case BOTTOM:
                startY -= masterDistance;
                stopY -= masterDistance;
                if(xyMiddle[0] > startX) {
                    drawLine(canvas,startX,startY,xyMiddle[0]-textDistance,stopY);
                    drawLine(canvas,stopX,stopY,xyMiddle[0]+textDistance,stopY);
                }else{
                    drawLine(canvas,startX,startY,xyMiddle[0]+textDistance,stopY);
                    drawLine(canvas,stopX,stopY,xyMiddle[0]-textDistance,stopY);
                }
                drawLine(canvas,startX,startY,startX,startY+masterDistance);
                drawLine(canvas,stopX,stopY,stopX,stopY+masterDistance);
                drawTextOnPath(canvas,text,textSize,xyMiddle[0],0,masterDistance,MprPosition.BOTTOM,true);
                break;
        }
    }

    /**
     * 绘制侧钉子
     * @param x 钉子打入点x轴
     * @param y 钉子打入点y轴
     * @param d 钉子直径
     * @param depth 钉子深度
     */
    private void drawNail(Canvas canvas,float x,float y,float d,float depth,MprPosition position) {
        PointF xy = getXY(x, y);
        d /= 2;
        switch (position) {
            case LEFT:
                //当钉子在左
                mRectF.left = xy.x;
                mRectF.bottom = xy.y-d;
                mRectF.right = xy.x+depth;
                mRectF.top = xy.y+d;
                break;
            case TOP:
                //当钉子在上
                mRectF.left = xy.x-d;
                mRectF.bottom = xy.y;
                mRectF.right = xy.x+d;
                mRectF.top = xy.y+depth;
                break;
            case RIGHT:
                //当钉子在右
                mRectF.left = xy.x-depth;
                mRectF.bottom = xy.y-d;
                mRectF.right = xy.x;
                mRectF.top = xy.y+d;
                break;
            case BOTTOM:
                //当钉子在下
                mRectF.left = xy.x-d;
                mRectF.bottom = xy.y-depth;
                mRectF.right = xy.x+d;
                mRectF.top = xy.y;
                break;

        }
        canvas.drawRect(mRectF,mPaint);
    }

    /**
     * 画弧，正钉子
     * @param startAngle 开始角度
     * @param sweepAngle 画多少度
     * @param d 原点直径
     * @param x 圆点x坐标
     * @param y 圆点y坐标
     */
    private void drawArc(Canvas canvas,float startAngle,float sweepAngle,float d,float x,float y) {
        d /= 2;
        PointF xy = getXY(x, y);
        mRectF.left = xy.x-d;
        mRectF.bottom = xy.y+d;
        mRectF.right = xy.x+d;
        mRectF.top = xy.y-d;
        canvas.drawArc(mRectF,startAngle,sweepAngle,true,mPaint);
    }

    /**
     * 绘制文字
     * @param text 文字
     * @param size 字体大小
     * @param x 文字x轴
     * @param y 文字y轴
     * @param offsetX X轴偏移 正向右偏移，负向左偏移
     * @param offsetY Y轴偏移 正向上偏移，负向下偏移
     */
    private void drawText(Canvas canvas,String text,float size,float x,float y,float offsetX,float offsetY,boolean isShow) {
        if(!isShow) return;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(size);//字体大小
        mPaint.setTextAlign(Paint.Align.CENTER);
        //获取文字垂直中心点
        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        PointF xy = getXY(x, y);
        canvas.drawText(text,xy.x+offsetX,xy.y+distance-offsetY,mPaint);
    }
    private final Path pathText = new Path();
    /**
     * 根据路径绘制文字
     * @param text 文字
     * @param size 文字大小
     * @param x 文字x轴
     * @param y 文字y轴
     * @param textWidth 文字
     * @param position 位置
     * @param isShow 是否显示
     */
    private void drawTextOnPath(Canvas canvas,String text,float size,float x,float y,float textWidth,MprPosition position,boolean isShow) {
        pathText.reset();
        if(!isShow) return;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(size);//字体大小
        mPaint.setTextAlign(Paint.Align.CENTER);
        PointF xy = getXY(x, y);
        switch (position) {
            case LEFT:
                pathText.moveTo(xy.x-textWidth, xy.y+textWidth);
                pathText.lineTo(xy.x-textWidth, xy.y-textWidth);
                break;
            case TOP:
                pathText.moveTo(xy.x-textWidth, xy.y);
                pathText.lineTo(xy.x+textWidth, xy.y);
                break;
            case RIGHT:
                pathText.moveTo(xy.x, xy.y-textWidth);
                pathText.lineTo(xy.x, xy.y+textWidth);
                break;
            case BOTTOM:
                pathText.moveTo(xy.x+textWidth, xy.y+textWidth);
                pathText.lineTo(xy.x-textWidth, xy.y+textWidth);
                break;
        }
        canvas.drawTextOnPath(text, pathText, 0, 0, mPaint);
    }

    /**
     * 画直线
     * @param startX 开始X轴
     * @param startY 开始Y轴
     * @param stopX 结束X轴
     * @param stopY 结束Y轴
     */
    private void drawLine(Canvas canvas,float startX,float startY,float stopX,float stopY) {
        PointF startXY = getXY(startX, startY);
        float sX = startXY.x;
        float sY = startXY.y;
        PointF stopXY = getXY(stopX, stopY);
        canvas.drawLine(sX,sY,stopXY.x,stopXY.y,mPaint);
    }

    /**
     *画路径
     * @param mprData 一组路径数值
     * @param offsetX 坐标偏移 负往左偏，反之右偏
     * @param offsetY 坐标偏移 负往下偏，反之上偏
     * @param isFill 是否填充所画路径
     * @param showXY 是否显示坐标
     */
    private void drawPath(Canvas canvas,MprCuttingData mprData,float size,float offsetX,float offsetY,boolean isFill,boolean showXY) {
        mPath.reset();
        Map<String, Float> map = mprData.getMap();
        for(int i = 0 ; i < map.size()/2;i++) {
            Float x = map.get("X"+i);
            Float y = map.get("Y"+i);
            if(x == null||y == null) return;
            PointF xy = getXY(x, y);
            if(i == 0) {
                mPath.moveTo(xy.x,xy.y);
            }else{
                mPath.lineTo(xy.x,xy.y);
            }
            //字体位置
            if(x == 0 && y != 0 || (x != 0 && y != 0)) {
                drawText(canvas,mprData.getCoordText(i),size,x,y,0,offsetY,showXY);
            } else if(x != 0 && y == 0 || (x == 0 && y == 0)) {
                drawText(canvas,mprData.getCoordText(i),size,x,y,0,-offsetY,showXY);
            }

        }
        //是否填充所画路径
        if(isFill) {
            mPaint.setStyle(Paint.Style.FILL);
        }else{
            mPaint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制二阶贝塞尔曲线
     * 二阶贝塞尔曲线需要两个坐标参数
     * @param map 二阶贝塞尔数据组
     */
    private void drawPathQuad(Canvas canvas,Map<String,Float> map) {
        mPath.reset();
        Float x0 = map.get("X0");
        Float y0 = map.get("Y0");
        Float x1 = map.get("X1");
        Float y1 = map.get("Y1");
        if(x0 == null||y0 == null||x1 == null||y1 == null) return;
        float abs = Math.abs(x0 - x1);
        PointF xy = null;
        switch (data.getMaster().getPosition(x0,y0,x1,y1)) {
            case LEFT_BOTTOM:
                xy = getXY(x0, y0-abs);
                break;
            case LEFT_TOP:
                xy = getXY(x0-abs, y0);
                break;
            case RIGHT_BOTTOM:
                xy = getXY(x0+abs, y0);
                break;
            case RIGHT_TOP:
                xy = getXY(x0, y0+abs);
                break;
            default:
                xy = getXY(x0, y0-abs);
                break;
        }
        PointF xy0 = getNewXY(x0, y0);
        PointF xy1 = getNewXY(x1, y1);
        mPath.moveTo(xy0.x, xy0.y);
        // 二次贝塞尔曲线
        mPath.quadTo(xy.x, xy.y, xy1.x, xy1.y);
        canvas.drawPath(mPath,mPaint);
    }
    /**
     * 根据传递过来的xy轴坐标，转换为当前画布实际坐标
     */
    private PointF getXY(float x,float y) {
        mPointF.x = centerPoint.x+x;
        mPointF.y = centerPoint.y-y;
        return mPointF;
    }

    /**
     * 这个有新对象产生还是少用吧
     */
    private PointF getNewXY(float x,float y) {
        return new PointF(centerPoint.x+x,centerPoint.y-y);
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }
    private PointF distance;
    /**
     * 手指触摸移动、缩放
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(data == null) return false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //单指按下
            case MotionEvent.ACTION_DOWN:
                //获取手指按下的位置
                startPoint.set(event.getX(),event.getY());
                //获取当前图形的坐标
                coordinate.set(centerPoint.x,centerPoint.y);
                //双击储存上一次点击时间戳
                oldTime = newTime;
                //双击储存获取当前点击时间戳
                newTime = System.currentTimeMillis();
                //手指数量：单指
                mode = DRAG;
                break;
            // 多指按下
            case MotionEvent.ACTION_POINTER_DOWN:
                //获取当前图形的坐标
                coordinate.set(centerPoint.x,centerPoint.y);
                //如果之前有缩放的话，需要把旧数据也缩放到最新的缩放程度
//                oldMapData.putAll(cloneData(oldMapData, scale));
//                oldData.scale(scale);
                scale = 1f;
                //获取两手指之间的距离
                oldDist = distance(event);
                //获取两指坐标
                distance = middle(event);
                //取得初始坐标与两指之间的相距的坐标
                distance.set(-(distance.x - coordinate.x),-(distance.y - coordinate.y));
                mode = ZOOM;
                break;
            //单、多指放开
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                performClick();
                if(newTime - oldTime < MAX_LONG_PRESS_TIME) {
                    //双击恢复坐标点
                    centerPoint.set(centerX,centerY);
                }
                mode = NONE;
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                oldTime = 0;
                //根据屏幕上手指数量执行不同的事件
                if(mode == DRAG) {
                    //图形移动，实现主要通过更改坐标
                    centerPoint.x = coordinate.x+(event.getX()-startPoint.x);
                    centerPoint.y = coordinate.y+(event.getY()-startPoint.y);
                }else if(mode == ZOOM) {
                    data.scale(1/scale);
                    //缩放，主要通过更改图形数值以及更改坐标实现
                    //获取新的俩手指距离
                    float newDist = distance(event);
                    //原手指距离与新手指距离比率为缩放倍率
                    scale = (newDist / oldDist);
                    data.scale(scale);
                    //坐标的实际移动距离
                    float actualX = distance.x*scale-distance.x;
                    float actualY = distance.y*scale-distance.y;
                    //把原坐标加上缩放后坐标的实际移动距离
                    centerPoint.set(coordinate.x+actualX,coordinate.y+actualY);
                }
                break;
        }
        invalidate();
        return true;
    }
    //*****************************************计算一些东西***********************************************
    /**
     * 计算两个触摸点之间的距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }
    /**
     * 计算两个触摸点的中点
     */
    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }

    /**
     * 改变颜色
     */
    public MprColor getMprColor(){
        return this.mprColor;
    }
}
