package com.aige.loveproduction.mvp.ui.customui.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义RadioGroup流式布局
 */
public class LineWrapRadioGroup extends RadioGroup {
    public LineWrapRadioGroup(Context context) {
        super(context);
    }

    public LineWrapRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //计算自己的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取父的参考值以及父布局模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //调用ViewGroup的方法，测量子view
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //当前行最大宽高
        int maxLineWidth = 0;
        int maxLineHeight = 0;
        //最后的宽高
        int maxWidth = 0;
        int maxHeight = 0;

        int childCount = getChildCount();
        for(int i = 0;i < childCount;i++) {
            //获取当前子View
            View childAt = getChildAt(i);
            //获取当前子View的外边距参数
            MarginLayoutParams params = (MarginLayoutParams)childAt.getLayoutParams();
            //计算这一个控件所占大小（自己大小+左右间距）
            int deltaX = childAt.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int deltaY = childAt.getMeasuredHeight()+params.topMargin + params.bottomMargin;
            //当累加的宽大于父给予的参考大小时，进行换行处理
            if(maxLineWidth + deltaX + getPaddingLeft() + getPaddingRight() > widthSize) {
                //把之前记录的最宽与当前行最宽比较，取最大值为最宽
                maxWidth = Math.max(maxLineWidth,maxWidth);
                maxLineWidth = deltaX;//下一行最宽
                //累加高度
                maxHeight += maxLineHeight;
                maxLineHeight = deltaY;//下一行最高
            }else{
                //累加行宽
                maxLineWidth += deltaX;
                //找到最高宽
                maxLineHeight = Math.max(maxLineHeight,deltaY);
            }
            //前面我们只在换行时才取得最终的宽高，而当最后一行时并没有加上这一行的宽高
            if (i == childCount - 1) {
                maxWidth = Math.max(maxLineHeight,maxWidth);
                maxHeight += maxLineHeight;
            }
        }
        //最终加上父的内边距
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        //根据父布局模式，判断是取自己计算的高度，还是取父给予的参考值
        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : maxWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : maxHeight
        );
    }

    //确定子View的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        //布局起点
        int startLeft = getPaddingLeft();
        int startTop = getPaddingTop();
        //记录这一行的最大高度
        int maxHeight = 0;
        for(int i = 0;i < childCount; i++) {
            View childAt = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams)childAt.getLayoutParams();
            if(startLeft + params.leftMargin + params.rightMargin + getPaddingRight() > (r - l)) {
                //换行重置左起点
                startLeft = getPaddingLeft();
                //换行累加高起点
                startTop += maxHeight;
            }else{
                //不换行，取这一行的最大高度
                maxHeight = Math.max(maxHeight, childAt.getMeasuredHeight() + params.topMargin + params.bottomMargin);
            }
            //子View位置
            int left = startLeft + params.leftMargin;
            int top = startTop + params.topMargin;
            int right = left + childAt.getMeasuredWidth();
            int bottom = top + childAt.getMeasuredHeight();
            //为子view布局
            childAt.layout(left, top, right, bottom);
            //布局后，定位下一个子View起点
            startLeft += childAt.getMeasuredWidth() + params.leftMargin + params.rightMargin;
        }
    }
}