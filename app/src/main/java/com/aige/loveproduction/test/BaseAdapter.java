package com.aige.loveproduction.test;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * 封装Adapter
 * 这里主要对Adapter和RecyclerView进行封装
 * @param <VH>
 */
public abstract class BaseAdapter<VH extends BaseAdapter<?>.ViewHolder> extends RecyclerView.Adapter<VH> {
    private Context mContext;
    private RecyclerView mRecyclerView;
    //***************设置一些监听****************
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    //子View不止一个，需要集合来装载监听对象
    private SparseArray<OnChildClickListener> mChildClickListener;
    private SparseArray<OnChildLongClickListener> mChildLongClickListener;
    /** ViewHolder 位置偏移值 */
    protected int mPositionOffset = 0;//当装饰适配器时，位置会有偏差
    public BaseAdapter(Context context) {
        mContext = context;
    }

    /**
     * 绑定holder，设置数据
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        //一般情况下position雨getLayoutPosition是相同的
        //当添加了头部和尾部后，他们之间就会有偏差
        mPositionOffset = position - holder.getLayoutPosition();
        holder.onBindView(position);
    }
    public Context getContext(){
        return mContext;
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,View.OnLongClickListener{
        //由原onCreateViewHolder方法创建的Item对象给ViewHolder创建，
        //onCreateViewHolder方法只需要new ViewHolder传入Item的Id即可
        public ViewHolder(@LayoutRes int id) {
            this(LayoutInflater.from(getContext()).inflate(id, mRecyclerView, false));
        }
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //拿到Item对象之后，设置一些监听
            if(mItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            if(mItemLongClickListener != null) {
                itemView.setOnLongClickListener(this);
            }
            if(mChildClickListener != null) {
                for (int i = 0;i < mChildClickListener.size();i++) {
                    //拿到需要监听的子View，设置点击事件
                    View child = findViewById(mChildClickListener.keyAt(i));
                    child.setOnClickListener(this);
                }

            }
            if(mChildLongClickListener != null) {
                for (int i = 0;i < mChildLongClickListener.size();i++) {
                    View child = findViewById(mChildLongClickListener.keyAt(i));
                    child.setOnLongClickListener(this);
                }
            }
        }

        /**
         * 数据绑定
         */
        public abstract void onBindView(int position);

        /**
         * 获取当前的ItemView
         */
        public final View getItemView() {
            return itemView;
        }

        /**
         * 封装findViewById
         */
        public final <V extends View> V findViewById(@IdRes int id) {
            return getItemView().findViewById(id);
        }


        /**
         * 获取当前位置
         */
        protected int getViewHolderPosition() {
            return getLayoutPosition() + mPositionOffset;
        }
        /**
         * 点击事件
         */
        @Override
        public void onClick(View v) {
            int position = getViewHolderPosition();
            if(position < 0 || position >= getItemCount()) return;
            //把监听事件移交给自定义的监听事件
            if(mItemClickListener != null) mItemClickListener.onItemClick(mRecyclerView,v,position);

            if(mChildClickListener != null) {
                OnChildClickListener listener = mChildClickListener.get(v.getId());
                if(listener != null) listener.onChildClick(mRecyclerView,v,position);
            }
        }

        /**
         * 长按事件
         */
        @Override
        public boolean onLongClick(View v) {
            int position = getViewHolderPosition();
            if(position < 0 || position >= getItemCount()) return false;
            //把监听事件移交给自定义的监听事件
            if(mItemLongClickListener != null) mItemLongClickListener.onItemLongClick(mRecyclerView,v,position);

            if(mChildLongClickListener != null) {
                OnChildLongClickListener listener = mChildLongClickListener.get(v.getId());
                if(listener != null) listener.onChildLongClick(mRecyclerView,v,position);

            }
            return false;
        }
    }
    /**
     * 绑定RecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        //如果布局管理器没有设置，设置默认布局管理器
        if(mRecyclerView.getLayoutManager() == null) {
            RecyclerView.LayoutManager layoutManager = generateDefaultLayoutManager(mContext);
            if(layoutManager != null) {
                mRecyclerView.setLayoutManager(layoutManager);
            }
        }
    }
    /**
     * 生成默认的布局摆放器
     */
    protected RecyclerView.LayoutManager generateDefaultLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    /**
     * 解绑RecyclerView
     */
    @Override
    public void onDetachedFromRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        mRecyclerView = null;
    }
    //***************************************设置监听****************************************
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }
    public void setChildClickListener(@IdRes int id, OnChildClickListener listener) {
        if(mChildClickListener == null) mChildClickListener = new SparseArray<>();
        mChildClickListener.put(id,listener);
    }
    public void setOnChildLongClickListener(@IdRes int id, OnChildLongClickListener listener) {
        if(mChildLongClickListener == null) mChildLongClickListener = new SparseArray<>();
        mChildLongClickListener.put(id,listener);
    }

    //****************************************一些监听接口***************************************
    /**
     * 条目点击监听
     */
    public interface OnItemClickListener {

        void onItemClick(RecyclerView recyclerView, View v, int position);
    }

    /**
     * 条目长按监听
     */
    public interface OnItemLongClickListener{
        void onItemLongClick(RecyclerView recyclerView, View v, int position);
    }

    /**
     * 条目子View的点击监听
     */
    public interface OnChildClickListener {
        void onChildClick(RecyclerView recyclerView, View v, int position);
    }

    /**
     * 条目子View的长按监听
     */
    public interface OnChildLongClickListener {
        void onChildLongClick();

        void onChildLongClick(RecyclerView recyclerView, View v, int position);
    }

}
