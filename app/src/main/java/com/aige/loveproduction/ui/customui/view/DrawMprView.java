package com.aige.loveproduction.ui.customui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.ResourcesAction;
import com.aige.loveproduction.enums.MprPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 张文科
 * 板件渲染
 * 自定义图形图形渲染
 * 支持拖拽、缩放，双击图形置中
 */
public class DrawMprView extends View implements ResourcesAction {
    //坐标
    private final RectF mRectF;
    private final PointF mPointF = new PointF();
    //画笔
    private final Paint mPaint;
    //储存主图形的宽高
    private float masterX = 0f,masterY = 0f,finalMasterX,finalMasterY,viewWidth,viewHeight;
    //外面传递的图形参数
    private Map<String,List<Map<String,Float>>> oldMapData;
    private Map<String,List<Map<String,Float>>> newMapData;
    private Map<String,List<Map<String,Float>>> mapData;
    //临时存储的数据
    private Map<String,Float> filter = new HashMap<>();
    //缩放比例
    private float scale = 1f;
    //图形初始坐标
    private float centerX = 0f,centerY = 0f;
    private final PointF centerPoint = new PointF();
    //**********************************初始设置****************************************************
    private int rectangle_color = getColor(R.color.draw_brown);
    private int cutting_color = getColor(R.color.draw_green);
    private int bohrHoriz_color = getColor(R.color.white);
    private int bohrVert_color = getColor(R.color.white);
    private boolean showXY = false;
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
    public DrawMprView(Context context) {
        this(context,null);
    }

    public DrawMprView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();
        mPaint = new Paint();
        initPaint();
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
     * @param map 第一层表图形类型，第二层表该类型的数量，第三层表每个图形的坐标属性
     */
    public void setData(Map<String, List<Map<String,Float>>> map) {
        if(map == null) throw new NullPointerException("图形数据为空！");
        oldMapData = map;
        setCustom();
        //深度克隆
        newMapData = cloneData(oldMapData,1f);
        mapData = cloneData(oldMapData,1f);
        //恢复初始状态
        initProperty();
        //刷新，重新绘制
        invalidate();
    }
    //恢复一些属性的初始状态
    private void initProperty() {
        scale = 1f;
        firstDraw = true;
        //图形显示坐标
        setCenter();
    }
    //设置图形的显示居中
    private void setCenter() {
        List<Map<String, Float>> master = newMapData.get("master");
        if(master == null) throw new NullPointerException("主视图不能为空!");
        master.get(0).forEach((k,v) -> {
            if("BSX".equals(k)) masterX = finalMasterX = v;
            if("BSY".equals(k)) masterY = finalMasterY = v;
        });
        centerX = viewWidth/2-masterX/2;
        centerY = viewHeight/2+masterY/2;
        centerPoint.set(centerX,centerY);
    }
    /**
     * 项目需求自定义一些属性
     */
    private void setCustom() {
        Map<String,Float> cusMap = new HashMap<>();
        cusMap.put("textSize",16f);
        cusMap.put("offsetX",50f);
        cusMap.put("offsetY",12f);
        cusMap.put("strokeWidth",2f);
        cusMap.put("masterLine",100f);
        cusMap.put("edge",20f);
        List<Map<String,Float>> list = new ArrayList<>();
        list.add(cusMap);
        oldMapData.put("customProperty",list);
    }
    /**
     * 深度克隆+数据调整缩放
     * @param data 克隆的数据
     * @param scale 缩放被克隆的数据的倍率
     * @return 克隆后全新的List集合
     */
    private Map<String,List<Map<String, Float>>> cloneData(Map<String,List<Map<String, Float>>> data,float scale) {
        List<Map<String, Float>> rectangle;
        Map<String,List<Map<String, Float>>> maps = new HashMap<>();
        Map<String,Float> map;
        for (Map.Entry<String, List<Map<String, Float>>> entry : data.entrySet()) {
            rectangle = new ArrayList<>();
            for (Map<String, Float> mapEntry : entry.getValue()) {
                map = new HashMap<>();
                for(Map.Entry<String, Float> entries : mapEntry.entrySet()) {
                    map.put(entries.getKey(),entries.getValue()*scale);
                }
                rectangle.add(map);
            }
            maps.put(entry.getKey(),rectangle);
        }
        return maps;
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
    //目前默认占满父布局
    //测量自己的大小
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//    {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int maxWidth;
//        int maxHeight;
//        float textWidth = mRectF.width();
//        maxWidth = (int) (getPaddingLeft() + textWidth + getPaddingRight());
//        float textHeight = mRectF.height();
//        maxHeight = (int) (getPaddingTop() + textHeight + getPaddingBottom());
//        setMeasuredDimension(
//                widthMode == MeasureSpec.EXACTLY ? widthSize : maxWidth,
//                heightMode == MeasureSpec.EXACTLY ? heightSize : maxHeight
//        );
//    }

    //开始绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        parseData(canvas);
    }
    //是否是第一次绘制
    private boolean firstDraw = true;
    //记录初始坐标值
    private final List<String> textList1 = new ArrayList<>();
    private final List<String> textList2 = new ArrayList<>();
    //解析整理数据，绘制要绘制的图形
    private void parseData(Canvas canvas) {
        if(newMapData == null) return;
        List<Map<String, Float>> master = newMapData.get("master");
        List<Map<String, Float>> bohrVert = newMapData.get("bohrVert");
        List<Map<String, Float>> bohrVertCross = newMapData.get("bohrVertCross");
        List<Map<String, Float>> bohrHoriz = newMapData.get("bohrHoriz");
        List<Map<String, Float>> cutting = newMapData.get("cutting");
        List<Map<String, Float>> property = newMapData.get("customProperty");
        Map<String, Float> propertyMap = property.get(0);
        //绘制主视图
        if(master != null) {
            master.get(0).forEach((k,v) -> {
                if("BSX".equals(k)) masterX = v;
                if("BSY".equals(k)) masterY = v;
            });
            centerX = viewWidth/2-masterX/2;
            centerY = viewHeight/2+masterY/2;
            initPaint();
            mPaint.setColor(getRectangle_color());
            drawRectangle(canvas,centerPoint.x, centerPoint.y, masterX, masterY);
        }
        //绘制切割线
        if(cutting != null) {
            byte pathCount = 1;
            for(Map<String, Float> map : cutting) {
                int size = map.size()/2;
                initPaint();
                mPaint.setColor(getCutting_color());
                mPaint.setStrokeWidth(propertyMap.get("strokeWidth") >= 3f ? 3f:propertyMap.get("strokeWidth"));
                mPaint.setStyle(Paint.Style.STROKE);
                if(size == 2) {
                    //当坐标只有两个时，那是弧形切割面
                    drawPathQuad(canvas,map);
                }else if(pathCount == cutting.size()){
                    //取最后一组切面，绘制最终切割描边
                    drawPath(canvas,map,propertyMap.get("textSize"),propertyMap.get("offsetX"),propertyMap.get("offsetY"),false,true);
                }
                pathCount++;
            }
        }
        //绘制侧面钉子
        if(bohrHoriz != null) {
            initPaint();
            mPaint.setColor(getBohrHoriz_color());
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(1f);
            for(Map<String, Float> map : bohrHoriz) {
                Float[] floats = parseMap(map);
                drawNail(canvas,floats[0],floats[1],floats[2],floats[3]);
            }
        }
        byte index;
        //绘制表面钉子
        if(bohrVert != null) {
            index = 0;
            for(Map<String, Float> map : bohrVert) {
                Float[] floats = parseMap(map);
                initPaint();
                if(firstDraw) {
                    textList1.add(index,printCoordinate(floats[0],floats[1]));
                }
                mPaint.setColor(getBohrVert_color());
                drawText(canvas, textList1.get(index), propertyMap.get("textSize"), floats[0], floats[1], 0f, floats[2],showXY);
                drawArc(canvas,0,360,floats[2],floats[0],floats[1]);
                index++;
            }
        }
        //绘制表面十字钉
        if(bohrVertCross != null) {
            index = 0;
            for(Map<String, Float> map : bohrVertCross) {
                Float[] floats = parseMap(map);
                initPaint();
                if(firstDraw) {
                    textList2.add(index,printCoordinate(floats[0],floats[1]));
                }
                mPaint.setColor(getBohrVert_color());
                drawText(canvas, textList2.get(index), propertyMap.get("textSize"), floats[0], floats[1], 0f, floats[2],showXY);
                drawArc(canvas,0,360,floats[2],floats[0],floats[1]);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1f);
                mPaint.setColor(getColor(R.color.black));
                drawArc(canvas,45,90,floats[2],floats[0],floats[1]);
                drawArc(canvas,135,90,floats[2],floats[0],floats[1]);
                drawArc(canvas,225,90,floats[2],floats[0],floats[1]);
                drawArc(canvas,315,90,floats[2],floats[0],floats[1]);
                index++;
            }
        }
        //绘制距离线
        initPaint();
        mPaint.setStrokeWidth(1f);
        drawDistanceMasterLine(canvas,propertyMap.get("textSize"),propertyMap.get("masterLine"),propertyMap.get("edge"));
        drawDistanceBohrLine(canvas,bohrHoriz,propertyMap.get("textSize"),propertyMap.get("masterLine"),propertyMap.get("edge"));
        drawDistanceCuttingLine(canvas,cutting.get(cutting.size()-1),propertyMap.get("textSize"),propertyMap.get("masterLine")/2,propertyMap.get("edge"));
        //已经绘制过一次，改变绘制状态
        firstDraw = false;
    }

    /**
     * 解析固定格式的Map集合
     * @param map Key为XA、YA、TI、DU的Map
     * @return Float数组，0：x轴，1：y轴，2：直径，3：深度
     */
    private Float[] parseMap(Map<String,Float> map) {
        Float[] floats = new Float[4];
        for(Map.Entry<String, Float> entry : map.entrySet()) {
            if("XA".equals(entry.getKey())) floats[0] = entry.getValue();
            if("YA".equals(entry.getKey())) floats[1] = entry.getValue();
            if("DU".equals(entry.getKey())) floats[2] = entry.getValue();
            if("TI".equals(entry.getKey())) floats[3] = entry.getValue();
        }
        return floats;
    }

    /**
     * 坐标输出坐标
     * @param fx x坐标
     * @param fy y坐标
     * @return x=？,y=?
     */
    private String printCoordinate(Float fx,Float fy) {
        StringBuilder builder = new StringBuilder();
        String x = String.valueOf(fx);
        String y = String.valueOf(fy);
        if(fx % 1.0 == 0) x = String.valueOf(fx.intValue());
        if(fy % 1.0 == 0) y = String.valueOf(fy.intValue());
        builder.append("x=").append(x).append(",")
                .append("y=").append(y);
        return builder.toString();
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
     * @param masterLine 主图形线
     * @param edge 线距中间缺口大小
     */
    private void drawDistanceMasterLine(Canvas canvas,float textSize,float masterLine,float edge){
        //master距线
        drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterY),textSize,0,0,0,masterY);
        drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterX),textSize,0,masterY,masterX,masterY);
        drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterY),textSize,masterX,0,masterX,masterY);
        drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterX),textSize,0,0,masterX,0);
    }

    /**
     *
     * @param data 距离线数据
     * @param textSize 字体大小
     * @param masterLine 主图形线距
     * @param edge 线距中间缺口大小
     */
    private void drawDistanceBohrLine(Canvas canvas,List<Map<String,Float>> data,float textSize,float masterLine,float edge) {

        List<Map<String, Float>> bohrHoriz = mapData.get("bohrHoriz");
        float ave = masterLine / 10;
        float step = ave;
        for(int i = 0;i < data.size();i++) {
            Float[] floats = parseMap(data.get(i));
            Float[] finalF = parseMap(bohrHoriz.get(i));
            MprPosition position = getPosition(floats[0], floats[1]);
            switch (position) {
                case LEFT:
                    drawLineWrap(canvas,step,edge,String.valueOf(finalF[1]),textSize,0,0,floats[0],floats[1]);
                    break;
                case TOP:
                    drawLineWrap(canvas,step,edge,String.valueOf(finalF[0]),textSize,0,masterY,floats[0],floats[1]);
                    break;
                case RIGHT:
                    drawLineWrap(canvas,step,edge,String.valueOf(finalF[1]),textSize,masterX,0,floats[0],floats[1]);
                    break;
                case BOTTOM:
                    drawLineWrap(canvas,step,edge,String.valueOf(finalF[0]),textSize,0,0,floats[0],floats[1]);
                    break;
            }
            step += ave;
            if(i < data.size()-1) {
                Float[] next = parseMap(data.get(i + 1));
                MprPosition nextPosition = getPosition(next[0], next[1]);
                if(nextPosition != position) {
                    step = masterLine / 10;
                }
            }
        }
    }
    private void drawDistanceCuttingLine(Canvas canvas,Map<String,Float> data,float textSize,float masterLine,float edge) {
        List<Map<String, Float>> cutting = mapData.get("cutting");
        Map<String, Float> finalCutting = cutting.get(cutting.size() - 1);
        float lastX = 0;
        float lastY = 0;
        byte index = 0;
        for(byte i = 0;i < data.size()/2;i++) {
            float x = data.get("X"+i);
            float y = data.get("Y"+i);
            MprPosition position = getPosition(x, y);
            switch (position) {
                case LEFT:
                case TOP:
                case RIGHT:
                case BOTTOM:
                    index++;
                    if(index % 2 == 0) {
                        MprPosition position1 = getPosition(lastX, lastY, x, y);
                        switch (position1) {
                            case LEFT_BOTTOM:
                                drawLineWrap(canvas,masterLine,edge,String.valueOf(finalCutting.get("Y"+(i-1))),textSize,0,0,lastX,lastY);
                                drawLineWrap(canvas,masterLine,edge,String.valueOf(finalCutting.get("X"+i)),textSize,0,0,x,y);
                                break;
                            case LEFT_TOP:
                                if(lastX == 0) {
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterY-finalCutting.get("Y"+(i-1))),textSize,0,masterY,lastX,lastY);
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalCutting.get("X"+i)),textSize,0,masterY,x,y);
                                }else{
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalCutting.get("X"+(i-1))),textSize,0,masterY,lastX,lastY);
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterY-finalCutting.get("Y"+i)),textSize,0,masterY,x,y);
                                }
                                break;
                            case RIGHT_BOTTOM:
                                if(lastY == 0) {
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterX-finalCutting.get("X"+(i-1))),textSize,masterX,0,lastX,lastY);
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalCutting.get("Y"+i)),textSize,masterX,0,x,y);
                                }else{
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalCutting.get("Y"+i)),textSize,masterX,0,lastX,lastY);
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterX-finalCutting.get("X"+(i-1))),textSize,masterX,0,x,y);
                                }
                                break;
                            case RIGHT_TOP:
                                if(lastY == masterY) {
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterX-finalCutting.get("X"+(i-1))),textSize,lastX,lastY,masterX,masterY);
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterY-finalCutting.get("Y"+i)),textSize,x,y,masterX,masterY);
                                }else{
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterY-finalCutting.get("Y"+(i-1))),textSize,lastX,lastY,masterX,masterY);
                                    drawLineWrap(canvas,masterLine,edge,String.valueOf(finalMasterX-finalCutting.get("X"+(i))),textSize,x,y,masterX,masterY);
                                }

                                break;
                        }
                    }
                    lastX = x;
                    lastY = y;
                    break;
            }
        }
    }


    /**
     * 绘制距离线
     * @param masterLine 中间空格
     * @param textSize 字体大小
     * @param startX 开始X坐标
     * @param startY 开始Y坐标
     * @param stopX 结束X坐标
     * @param stopY 结束Y坐标
     */
    private void drawLineWrap(Canvas canvas,float masterLine,float edge,String text,float textSize,float startX,float startY,float stopX,float stopY){
        float pointX;
        float pointY;
        switch (getPosition(startX, startY, stopX, stopY)) {
            case LEFT:
                startX -= masterLine;
                stopX -= masterLine;
                pointY = startY+(stopY-startY)/2;
                if(edge > pointY) edge = stopY/3;
                drawLine(canvas,startX,startY,stopX,pointY-edge);
                drawLine(canvas,stopX,stopY,startX,pointY+edge);
                drawLine(canvas,startX,startY,startX+masterLine,startY);
                drawLine(canvas,stopX,stopY,stopX+masterLine,stopY);
                drawTextOnPath(canvas,text,textSize,0,pointY,masterLine,MprPosition.LEFT,true);
                break;
            case TOP:
                startY += masterLine;
                stopY += masterLine;
                pointX = startX+(stopX-startX)/2;
                if(edge > pointX) edge = stopX/3;
                drawLine(canvas,startX,startY,pointX-edge,stopY);
                drawLine(canvas,stopX,stopY,pointX+edge,stopY);
                drawLine(canvas,startX,startY,startX,startY-masterLine);
                drawLine(canvas,stopX,stopY,stopX,stopY-masterLine);
                drawTextOnPath(canvas,text,textSize,pointX,startY,masterLine,MprPosition.TOP,true);
                break;
            case RIGHT:
                startX += masterLine;
                stopX += masterLine;
                pointY = startY+(stopY-startY)/2;
                if(edge > pointY) edge = stopY/3;
                drawLine(canvas,startX,startY,stopX,pointY-edge);
                drawLine(canvas,stopX,stopY,stopX,pointY+edge);
                drawLine(canvas,startX,startY,startX-masterLine,startY);
                drawLine(canvas,stopX,stopY,stopX-masterLine,stopY);
                drawTextOnPath(canvas,text,textSize,startX,pointY,masterLine,MprPosition.RIGHT,true);
                break;
            case BOTTOM:
                startY -= masterLine;
                stopY -= masterLine;
                pointX = startX+(stopX-startX)/2;
                if(edge > pointX) edge = stopX/3;
                drawLine(canvas,startX,startY,pointX-edge,stopY);
                drawLine(canvas,stopX,stopY,pointX+edge,stopY);
                drawLine(canvas,startX,startY,startX,startY+masterLine);
                drawLine(canvas,stopX,stopY,stopX,stopY+masterLine);
                drawTextOnPath(canvas,text,textSize,pointX,0,masterLine,MprPosition.BOTTOM,true);
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
    private void drawNail(Canvas canvas,float x,float y,float d,float depth) {
        PointF xy = getXY(x, y);
        d /= 2;
        switch (getPosition(x,y)) {
            case LEFT:
                mRectF.left = xy.x;
                mRectF.bottom = xy.y-d;
                mRectF.right = xy.x+depth;
                mRectF.top = xy.y+d;
                break;
            case TOP:
                mRectF.left = xy.x-d;
                mRectF.bottom = xy.y;
                mRectF.right = xy.x+d;
                mRectF.top = xy.y+depth;

                break;
            case RIGHT:
                mRectF.left = xy.x-depth;
                mRectF.bottom = xy.y-d;
                mRectF.right = xy.x;
                mRectF.top = xy.y+d;
                break;
            case BOTTOM:
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

    private final Path mPath = new Path();
    private final List<String> pathList = new ArrayList<>();
    /**
     *画路径
     * @param map 一组路径数值
     * @param offsetX 坐标偏移 负往左偏，反之右偏
     * @param offsetY 坐标偏移 负往下偏，反之上偏
     * @param isFill 是否填充所画路径
     * @param showXY 是否显示坐标
     */
    private void drawPath(Canvas canvas,Map<String,Float> map,float size,float offsetX,float offsetY,boolean isFill,boolean showXY) {
        mPath.reset();
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
            if(firstDraw) pathList.add(i,printCoordinate(x,y));
            //字体位置
            if(x == 0 && y != 0 || (x != 0 && y != 0)) {
                drawText(canvas,pathList.get(i),size,x,y,0,offsetY,showXY);
            } else if(x != 0 && y == 0 || (x == 0 && y == 0)) {
                drawText(canvas,pathList.get(i),size,x,y,0,-offsetY,showXY);
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
        switch (getPosition(x0,y0,x1,y1)) {
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
//        if((x0 == 0 && y0 != 0 && x1 != 0 && y1 == 0) || (x1 == 0 && y1 != 0 && x0 != 0 && y0 == 0)) {
//            xy = getXY(x0, y0-abs);
//        }else if((x0 != 0 && y0 == 0 && x1 != 0 && y1 != 0) || (x1 != 0 && y1 == 0 && x0 != 0 && y0 != 0)) {
//            xy = getXY(x0+abs, y0);
//        }else if((x0 != 0 && y0 != 0 && x1 != 0 && y1 != 0)) {
//            xy = getXY(x0, y0+abs);
//        }else {
//            xy = getXY(x0-abs, y0);
//        }
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
        if(oldMapData == null) return false;
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
                oldMapData.putAll(cloneData(oldMapData, scale));
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
                    //缩放，主要通过更改图形数值以及更改坐标实现
                    //获取新的俩手指距离
                    float newDist = distance(event);
                    //原手指距离与新手指距离比率为缩放倍率
                    scale = (newDist / oldDist);
                    newMapData.putAll(cloneData(oldMapData, scale));
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
     * 获取一个点在主图形边上的位置
     * @return 0：右边，1：上面，2：右边，3：下边
     */
    private MprPosition getPosition(float x,float y) {
        if(x == 0 && y != 0 && y != masterY) return MprPosition.LEFT;
        if(
                (x != 0 && y == masterY && x != masterX)
                ||(x != 0 && y != 0 && y == masterY && x != masterX)
        ) return MprPosition.TOP;
        if(
                (x == masterX && y != 0 && y != masterY)
                ||(x != 0 && y != 0 && x == masterX && y != masterY)

        ) return MprPosition.RIGHT;
        if(x != 0 && y == 0 && x != masterX) return MprPosition.BOTTOM;
        return MprPosition.NULL;
    }

    /**
     * 获取两点在主图形边上的位置
     * 只能获取位于主图形边上的点位
     * @return 0：右边，1：上面，2：右边，3：下边
     */
    private MprPosition getPosition(float startX,float startY,float stopX,float stopY) {
        MprPosition start = getPosition(startX, startY);
        MprPosition stop = getPosition(stopX, stopY);
        if(startX == stopX && stopX == 0) return MprPosition.LEFT;
        if(startY == stopY && stopY == masterY) return MprPosition.TOP;
        if(startX == stopX && stopX == masterX) return MprPosition.RIGHT;
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


    //*************************************暴露给外面***************************************
    public int getRectangle_color() {
        return rectangle_color;
    }

    public void setRectangle_color(@ColorRes int rectangle_color) {
        this.rectangle_color = getColor(rectangle_color);
    }

    public int getCutting_color() {
        return cutting_color;
    }

    public void setCutting_color(@ColorRes int cutting_color) {
        this.cutting_color = getColor(cutting_color);
    }

    public int getBohrHoriz_color() {
        return bohrHoriz_color;
    }

    public void setBohrHoriz_color(@ColorRes int bohrHoriz_color) {
        this.bohrHoriz_color = getColor(bohrHoriz_color);
    }

    public int getBohrVert_color() {
        return bohrVert_color;
    }

    public void setBohrVert_color(@ColorRes int bohrVert_color) {
        this.bohrVert_color = getColor(bohrVert_color);
    }
    /**是否显示坐标*/
    public void setShowXY(boolean param) {
        this.showXY = param;
    }
}
