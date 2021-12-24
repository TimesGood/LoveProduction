package com.aige.loveproduction.ui.customui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * 自定义RecycleView分割线
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = 1;//分割线高度，默认为1px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    public RecycleViewDivider(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.INVALID_OFFSET) {
            throw new IllegalArgumentException("请输入正确的水平方向！");
        }
        mOrientation = orientation;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public RecycleViewDivider(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewDivider(Context context, int orientation, int dividerHeight, int dividerColor) {
        this(context, orientation);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }
    //设置item四周的边距，围绕边距绘画分割线
    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
//        for(int i = 0;i < parent.getChildCount();i++) {
//            ViewGroup child = (ViewGroup)parent.getChildAt(i);
//            for(int j = 0;j < child.getChildCount();j++) {
//                View childAt = child.getChildAt(j);
//                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
//                if(i == 0) {
//                    params.leftMargin = mDividerHeight;
//                    params.rightMargin = mDividerHeight;
//                    childAt.setLayoutParams(params);
//                    continue;
//                }
//                params.rightMargin = mDividerHeight;
//            }
//
//        }

        //只给第一行的top加上分割线
        if(childAdapterPosition == 0) {
            outRect.top = mDividerHeight;
        }
        outRect.bottom = mDividerHeight;
    }

    //绘制分割线
    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else if(mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontal(c, parent);
        } else{
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        }
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        //获取分割线距左边距离
        final int left = parent.getPaddingLeft();
        //获取分割线距右边距离
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        //Item数量
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            //获取分割线顶部位置
            final int top = child.getBottom() + layoutParams.bottomMargin;
            //获取分割线底部位置
            final int bottom = top + mDividerHeight;
            //绘制矩形，相当于是线了
            if (mPaint != null) {
                if(i == 0) {
                    final int top1 = child.getTop() + layoutParams.topMargin;
                    final int bottom1 = top1 - mDividerHeight;
                    canvas.drawRect(left, top1, right, bottom1, mPaint);
                }
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
//            **************************************************************
//            final ViewGroup child = (ViewGroup) parent.getChildAt(i);
//            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
//            int childCount = child.getChildCount();
//            for (int j = 0; j < childCount; j++) {
//                System.out.println("****************"+childCount);
//                View childAt = child.getChildAt(j);
//                final int left = childAt.getRight() + layoutParams.rightMargin;
//                final int right = left + mDividerHeight;
//                if (mDivider != null) {
//                    mDivider.setBounds(left, top, right, bottom);
//                    mDivider.draw(canvas);
//                }
//                if (mPaint != null) {
//                    canvas.drawRect(left, top, right, bottom, mPaint);
//                }
//            }
        }
    }
}