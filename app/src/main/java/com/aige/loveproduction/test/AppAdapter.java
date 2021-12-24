package com.aige.loveproduction.test;

import android.content.Context;

import androidx.annotation.IntRange;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 再次对Adapter封装
 * 主要对数据进行处理
 * @param <T>
 */
public abstract class AppAdapter<T> extends BaseAdapter<BaseAdapter<?>.ViewHolder> {
    private List<T> mData;
    public AppAdapter(Context context) {
        super(context);
    }
    @Override
    public int getItemCount() {
        return getCount();
    }
    /**
     * 获取数据总数
     */
    public int getCount() {
        if (mData != null) return mData.size();
        return 0;
    }

    /**
     * 设置数据
     */
    public void setData(List<T> data){
        mData = data;
        //重新刷新数据
        notifyDataSetChanged();
    }

    /**
     * 获取当前设置的数据
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 追加数据
     */
    public void addData(List<T> data) {
        if(data == null || data.size() == 0) return;
        if(mData == null || mData.size() == 0) {
            setData(data);
            return;
        }
        mData.addAll(data);
        //从追加点开始刷新多少条数据
        notifyItemRangeChanged(mData.size() - data.size(),data.size());
    }

    /**
     * 清除所有数据
     */
    public void clearData() {
        if(mData == null || mData.size() == 0) return;
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取某个位置的数据
     */
    public T getItemData(@IntRange(from = 0) int position) {
        if(mData == null || !(position < mData.size())) return null;
        return mData.get(position);
    }

    /**
     * 更新某个位置的数据
     */
    public void setItemData(@IntRange(from = 0) int position, @NotNull T data) {
        if(mData == null || position >= mData.size()) return;
        mData.set(position,data);
        notifyItemChanged(position);
    }

    /**
     * 追加单条数据
     */
    public void addItemData(T item){
        addItemData(mData.size(),item);
    }

    /**
     * 向某个位置添加单条数据
     */
    public void addItemData(@IntRange(from = 0) int position,T item) {
        if(mData == null) mData = new ArrayList<>();
        if(position < mData.size()) {
            mData.add(mData.size(),item);
        }else {
            mData.add(item);
            position = mData.size() - 1;
        }
        notifyItemChanged(position);
    }

    /**
     * 删除单条数据
     */
    public void removeItemData(T item) {
        if(mData == null) return;
        int index = mData.indexOf(item);
        if(index != -1) removeItemData(index);
    }

    /**
     * 删除某个位置的数据
     */
    public void removeItemData(@IntRange(from = 0) int position) {
        if(mData == null || !(position < mData.size())) return;
        mData.remove(position);
        notifyItemChanged(position);
    }


}
