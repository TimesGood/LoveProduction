package com.aige.loveproduction.mvp.ui.customui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.aige.loveproduction.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 板件渲染
 * 自定义渲染图形
 * 支持拖拽、缩放
 */
public class DrawMprView extends View {
    private final Context mContext;
    //坐标
    private RectF mRectF;
    //画笔
    private Paint mPaint;
    //储存主图形的宽高
    private float mWidth = 0f,mHeight = 0f;
    //外面传递的数据
    private Map<String,List<Map<String,Float>>> oldMapData;
    private Map<String,List<Map<String,Float>>> newMapData;
    //缩放比例，当绘制的图形大于当前View的大小时，按比例缩放
    private float scale = 1f;
    //图形初始坐标
    private float centerX = 0f,centerY = 0f;
    private final PointF centerPoint = new PointF();
    //**********************************以下是实现图形移动、缩放功能的属性***********************************
    // 不同状态的表示：
    private static final int NONE = 0;//未接触
    private static final int DRAG = 1;//单指
    private static final int ZOOM = 2;//双指
    private int mode = NONE;
    // 定义第一个按下的点，两只接触点的重点，以及出事的两指按下的距离：
    //图形移动前的坐标
    private final PointF coordinate = new PointF();
    private final PointF startPoint = new PointF();
    private float oriDis = 1f;
    //*****************************************
    private final int MAX_LONG_PRESS_TIME=350;//双击最长等待时间
    private long oldTime;
    private long newTime = 0;

    public DrawMprView(Context context) {
        this(context,null);
    }

    public DrawMprView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRectF();
        initPaint();
        mContext = context;
    }

    /**
     * 图形坐标参数
     * @param map 第一层表图形类型，第二层表该类型的数量，第三层表每个图形的坐标属性
     */
    public void setData(Map<String, List<Map<String,Float>>> map) {
        if(map == null) throw new NullPointerException("图形数据为空！");
        oldMapData = map;
        //加入一些自定义的属性
        Map<String,Float> cusMap = new HashMap<>();
        cusMap.put("textSize",16f);
        cusMap.put("offsetX",50f);
        cusMap.put("offsetY",12f);
        setCustom("text",cusMap);
        //深度克隆
        newMapData = new HashMap<>();
        newMapData = cloneData(oldMapData,1f);
        //恢复原始状态
        initProperty();
        //刷新
        invalidate();
    }
    //恢复一些属性的初始状态
    private void initProperty() {
        scale = 1f;
        firstDraw = true;
    }
    /**
     * 项目需求自定义一些属性
     * @param key 自定义的key
     * @param map 数据
     */
    private void setCustom(String key,Map<String,Float> map) {
        List<Map<String,Float>> list = new ArrayList<>();
        list.add(map);
        oldMapData.put(key,list);
    }
    /**
     * 深度克隆+数据调整缩放
     * @param data 克隆的数据
     * @param scale 缩放被克隆的数据的倍率
     * @return 克隆新集合
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
    private void initRectF() {
        if(mRectF == null) mRectF = new RectF();
    }
    /**
     * 初始画笔样式
     */
    private void initPaint() {
        if(mPaint == null) mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f); //设置画笔宽度
        mPaint.setXfermode(null);

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
        //坐标点
        centerX = w/10f;
        centerY = h/1.2f;
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
    //第一次绘制
    private boolean firstDraw = true;
    //记录初始坐标值
    private final List<String> textList1 = new ArrayList<>();
    private final List<String> textList2 = new ArrayList<>();
    //解析整理数据，绘制要绘制的图形
    private void parseData(Canvas canvas) {
        if(newMapData == null) return;
        List<Map<String, Float>> rectangle = newMapData.get("rectangle");
        List<Map<String, Float>> cutting1 = newMapData.get("Cutting1");
        List<Map<String, Float>> bohrVert1 = newMapData.get("BohrVert1");
        List<Map<String, Float>> bohrVert2 = newMapData.get("BohrVert2");
        List<Map<String, Float>> bohrHoriz1 = newMapData.get("BohrHoriz1");
        List<Map<String, Float>> text = newMapData.get("text");
        Map<String, Float> textMap = text.get(0);
        if(rectangle != null) {
            for(Map<String, Float> map : rectangle) {
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext, R.color.draw_brown));
                mWidth = map.get("BSX");
                mHeight = map.get("BSY");
                drawRectangle(canvas,centerPoint.x, centerPoint.y, mWidth, mHeight);
            }
        }
        if(cutting1 != null) {
            int pathCount = 1;
            for(Map<String, Float> map : cutting1) {
                int size = map.size()/2;
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext, R.color.draw_green));
                mPaint.setStrokeWidth(3f);
                mPaint.setStyle(Paint.Style.STROKE);
                if(size == 2) {
                    drawPathQuad(canvas,map);
                }else if(pathCount == cutting1.size()){
                    drawPath(canvas,map,textMap.get("textSize"),textMap.get("offsetX"),textMap.get("offsetY"),false,true);
                }
                pathCount++;
            }
        }
        if(bohrHoriz1 != null) {
            for(Map<String, Float> map : bohrHoriz1) {
                Float xa = map.get("XA");
                Float ya = map.get("YA");
                Float ti = map.get("TI");
                Float du = map.get("DU");
                if(xa == null || ya == null || ti == null || du == null) break;
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1f);
                drawNail(canvas,xa,ya,du,ti);
            }
        }
        int index;
        if(bohrVert1 != null) {
            index = 0;
            for(Map<String, Float> map : bohrVert1) {
                Float xa = map.get("XA");
                Float ya = map.get("YA");
                Float ti = map.get("TI");
                Float du = map.get("DU");
                if(xa == null || ya == null || ti == null || du == null) break;
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.grey));
                drawArc(canvas,0,360,du,xa,ya);
                if(firstDraw) {
                    textList1.add(index,printInt(xa,ya));
                }
                drawText(canvas, textList1.get(index), textMap.get("textSize"), xa, ya, 0f, textMap.get("offsetY"));
                index++;
            }
        }
        if(bohrVert2 != null) {
            index = 0;
            for(Map<String, Float> map : bohrVert2) {
                Float ti = map.get("TI");
                Float xa = map.get("XA");
                Float ya = map.get("YA");
                if(ti == null || xa == null || ya == null) break;
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.grey));
                drawArc(canvas,0,360,ti,xa,ya);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1f);
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.black));
                drawArc(canvas,45,90,ti,xa,ya);
                drawArc(canvas,135,90,ti,xa,ya);
                drawArc(canvas,225,90,ti,xa,ya);
                drawArc(canvas,315,90,ti,xa,ya);
                if(firstDraw) {
                    textList2.add(index,printInt(xa,ya));
                }
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.grey));
                drawText(canvas, textList2.get(index), textMap.get("textSize"), xa, ya, 0f, textMap.get("offsetY"));
                index++;
            }
        }
        //改变绘制状态
        firstDraw = false;
    }

    /**
     * 坐标输出
     * @param fx x坐标
     * @param fy y坐标
     * @return x=？,y=?
     */
    private String printInt(Float fx,Float fy) {
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
     * 绘制侧钉子
     * @param x 钉子打入点x轴
     * @param y 钉子打入点y轴
     * @param d 钉子直径
     * @param depth 钉子深度
     */
    private void drawNail(Canvas canvas,float x,float y,float d,float depth) {
        PointF xy = getXY(x, y);
        d /= 2;
        if(x==0) {
            //当钉子在左
            mRectF.left = xy.x;
            mRectF.bottom = xy.y-d;
            mRectF.right = xy.x+depth;
            mRectF.top = xy.y+d;
        }else if(y == 0) {
            //当钉子在下
            mRectF.left = xy.x-d;
            mRectF.bottom = xy.y-depth;
            mRectF.right = xy.x+d;
            mRectF.top = xy.y;
        }else if(x == mWidth){
            //当钉子在右
            mRectF.left = xy.x-depth;
            mRectF.bottom = xy.y-d;
            mRectF.right = xy.x;
            mRectF.top = xy.y+d;
        }else if(y == mHeight) {
            //当钉子在上
            mRectF.left = xy.x-d;
            mRectF.bottom = xy.y;
            mRectF.right = xy.x+d;
            mRectF.top = xy.y+depth;
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
    private void drawText(Canvas canvas,String text,float size,float x,float y,float offsetX,float offsetY) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(size);//字体大小
        mPaint.setTextAlign(Paint.Align.CENTER);
        //获取文字的高度
        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        PointF xy = getXY(x, y);
        canvas.drawText(text,xy.x+offsetX,xy.y+distance-offsetY,mPaint);
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
        PointF stopXY = getXY(stopX, stopY);
        canvas.drawLine(startXY.x,startXY.y,stopXY.x,stopXY.y,mPaint);
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
            if(firstDraw) pathList.add(i,printInt(x,y));
            //是否显示坐标
            if (!showXY) continue;
            //字体位置
            if(x == 0 && y != 0 || (x != 0 && y != 0)) {
                drawText(canvas,pathList.get(i),size,x,y,0,offsetY);
            } else if(x != 0 && y == 0 || (x == 0 && y == 0)) {
                drawText(canvas,pathList.get(i),size,x,y,0,-offsetY);
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
        PointF xy0 = getXY(x0, y0);
        PointF xy1 = getXY(x1, y1);
        PointF xy = null;
        if((x0 == 0 && y0 != 0 && x1 != 0 && y1 == 0) || (x1 == 0 && y1 != 0 && x0 != 0 && y0 == 0)) {
            xy = getXY(x0, y0-abs);//
        }else if((x0 != 0 && y0 == 0 && x1 != 0 && y1 != 0) || (x1 != 0 && y1 == 0 && x0 != 0 && y0 != 0)) {
            xy = getXY(x0+abs, y0);
        }else if((x0 != 0 && y0 != 0 && x1 != 0 && y1 != 0)) {
            xy = getXY(x0, y0+abs);
        }else {
            xy = getXY(x0-abs, y0);
        }
        mPath.moveTo(xy0.x, xy0.y);
        // 二次贝塞尔曲线
        mPath.quadTo(xy.x, xy.y, xy1.x, xy1.y);
        canvas.drawPath(mPath,mPaint);
    }
    /**
     * 根据传递过来的xy轴坐标，转换为当前画布实际坐标
     */
    private PointF getXY(float x,float y) {
        return new PointF(centerPoint.x+x,centerPoint.y-y);
    }
    @Override
    public boolean performClick() {
        return super.performClick();
    }
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
                oldTime = newTime;
                newTime = System.currentTimeMillis();
                mode = DRAG;
                break;
            // 双指按下
            case MotionEvent.ACTION_POINTER_DOWN:
                //获取两手指之间的距离
                oriDis = distance(event);
                if (oriDis > 10f) {
                    //获取两手指之间的x，y
                    mode = ZOOM;
                }
                //设置前一次缩放之后的原始数据
                oldMapData.putAll(cloneData(oldMapData, scale));
                break;
            //手指放开
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                performClick();
                if(newTime - oldTime < MAX_LONG_PRESS_TIME) {
                    //双击恢复坐标点
                    centerPoint.set(centerX,centerY);
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                oldTime = 0;
                if(mode == DRAG) {
                    //更改图形坐标
                    centerPoint.x = coordinate.x+(event.getX()-startPoint.x);
                    centerPoint.y = coordinate.y+(event.getY()-startPoint.y);
                }else if(mode == ZOOM) {
                    //获取新的俩手指距离
                    float newDist = distance(event);
                    if (newDist > 10f) {
                        scale = (newDist / oriDis);
                        newMapData.putAll(cloneData(oldMapData, scale));
                    }
                }
                break;
        }
        invalidate();
        return true;
    }
    // 计算两个触摸点之间的距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
    // 计算两个触摸点的中点
    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }
}
