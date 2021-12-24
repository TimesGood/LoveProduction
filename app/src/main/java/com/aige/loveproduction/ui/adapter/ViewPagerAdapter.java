package com.aige.loveproduction.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *    PagerAdapter 封装
 *    当页卡是View时使用这个
 */
public final class ViewPagerAdapter<V extends View> extends PagerAdapter {

    /** Fragment 集合 */
    private final List<V> mViewSet = new ArrayList<>();
    /** Fragment 标题 */
    private final List<CharSequence> mViewTitle = new ArrayList<>();
    /**当前的ViewPager*/
    private ViewPager mViewPager;

    /**
     * 把页面放入ViewPage
     * @param container ViewPage的ViewGroup
     * @param position 要放View的索引
     */
    @NonNull
    @NotNull
    @Override
    public V instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        mViewPager = (ViewPager) container;
        container.addView(mViewSet.get(position));//把当前位置的页卡放进显示页
        return mViewSet.get(position);
    }

    /**
     * 删除指定索引的页面
     */
    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView(mViewSet.get(position));//删除当前位置的显示页
    }

    @Override
    public int getCount() {
        return mViewSet.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mViewTitle.get(position);
    }

    /**
     * 添加View页面
     */
    public void addView(V view){
        addView(view,null);
    }
    public void addView(V view,CharSequence title) {
        mViewSet.add(view);
        mViewTitle.add(title);
        if(mViewPager != null) notifyDataSetChanged();
    }

}