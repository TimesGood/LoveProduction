package com.aige.loveproduction.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WrapRecyclerView extends RecyclerView {
    private Adapter mAdapter;//原来的Adapter
    private final WrapAdapter mWrapAdapter = new WrapAdapter();
    public WrapRecyclerView(@NonNull @NotNull Context context) {
        super(context);
    }

    public WrapRecyclerView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写setAdapter，替换成我们包装过的Adapter
     */
    @Override
    public void setAdapter(@Nullable @org.jetbrains.annotations.Nullable Adapter adapter) {
        mAdapter = adapter;
        mWrapAdapter.setReplaceAdapter(adapter);
        super.setAdapter(mWrapAdapter);
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    /**添加头部*/
    public void addHeaderView(@LayoutRes int resource) {
        View inflate = LayoutInflater.from(getContext()).inflate(resource, this,false);
        addHeaderView(inflate);
    }
    public void addHeaderView(View view) {
        mWrapAdapter.addHeaderView(view);
    }
    /**添加尾部*/
    public void addFooterView(@LayoutRes int resource) {
        View inflate = LayoutInflater.from(getContext()).inflate(resource, this,false);
        addFooterView(inflate);
    }
    public void addFooterView(View view) {
        mWrapAdapter.addFooterView(view);
    }
    /**
     * 设置在 GridLayoutManager 模式下头部和尾部都是独占一行的效果
     */
    public void adjustSpanSize() {
        final LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    return (position < mWrapAdapter.getHeaderViewCount()
                            || position >= mWrapAdapter.getHeaderViewCount() + (mAdapter == null ? 0 : mAdapter.getItemCount()))
                            ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
                }
            });
        }
    }
    /**
     * 装饰Adapter
     */
    private static final class WrapAdapter extends Adapter<ViewHolder>{
        //头部、尾部类型，>>双目移位运算符，将要移位的数转为二进制再移位
        private static final int HEADER_VIEW_TYPE = Integer.MIN_VALUE >> 1;
        private static final int FOOTER_VIEW_TYPE = Integer.MAX_VALUE >> 1;
        /**原来的适配器**/
        private Adapter mAdapter;
        //头部、尾部数量集合
        private final List<View> mHeaderViews = new ArrayList<>();
        private final List<View> mFooterViews = new ArrayList<>();
        private RecyclerView mRecyclerView;
        private WrapAdapterDataObserver mObserver;
        private int mPosition;
        /**
         * 替换成我们装饰的适配器
         */
        private void setReplaceAdapter(Adapter adapter) {
            //如果新设置的适配器没有被装饰，或者不是之前已经装饰过的适配器，
            //需要为旧的适配器解绑监视器，并把旧的适配器替换掉
            if(mAdapter != adapter) {
                if(mAdapter != null && mObserver != null) {
                    mAdapter.unregisterAdapterDataObserver(mObserver);
                }
                //替换旧的适配器
                mAdapter = adapter;
                if(mAdapter != null) {
                    if(mObserver == null) mObserver = new WrapAdapterDataObserver(this);
                    mAdapter.registerAdapterDataObserver(mObserver);
                    //当适配器不是第一次绑定时，需要自己通知刷新，（首次绑定会自行通知刷新）
                    //第一次绑定时会在setReplaceAdapter之后调用onAttachedToRecyclerView
                    //所以通过mRecyclerView是否为空判断是不是第一次绑定
                    if(mRecyclerView != null) notifyDataSetChanged();
                }
            }
        }
        /**
         * 在这里通过条目类型不同，而创建不同的条目
         * @param viewType 这个值就是我们在getItemViewType()中自定义的类型判断逻辑
         */
        @SuppressWarnings("all")
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            if(viewType == HEADER_VIEW_TYPE) {
                return new WrapViewHolder(mHeaderViews.get(getPosition()));
            }else if(viewType == FOOTER_VIEW_TYPE) {
                return new WrapViewHolder(mFooterViews.get(getPosition() - getHeaderViewCount() - (mAdapter == null ? 0 : mAdapter.getItemCount())));
            }
            return mAdapter == null ? null : mAdapter.onCreateViewHolder(parent,viewType);
        }

        /**
         * 这里就是根据索引来给添加条目类型的判断逻辑了
         * @param position 当前调用的Item位置
         * @return 当前位置的Item的类型
         */
        @Override
        public int getItemViewType(int position) {
            mPosition = position;
            int headerViewCount = getHeaderViewCount();//获取头部数量
            int itemCount = mAdapter == null ? 0 : mAdapter.getItemCount();//获取原适配器的Item数量
            if(position < headerViewCount) {
                return HEADER_VIEW_TYPE;
            }else if(position >= headerViewCount + itemCount) {
                return FOOTER_VIEW_TYPE;
            }
            int adapterIndex = position - headerViewCount;//当前调用位置减去头部的数量，就是原适配器的索引了
            //返回原适配器所定义的类型
            return mAdapter.getItemViewType(adapterIndex);
        }

        /**
         * 绑定Holder
         */
        @SuppressWarnings("all")
        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            int itemViewType = getItemViewType(position);
            WrapViewHolder wrapViewHolder = null;
            if(holder instanceof WrapViewHolder) {
                wrapViewHolder = (WrapViewHolder) holder;
            }
            switch (itemViewType) {
                case HEADER_VIEW_TYPE:
                    wrapViewHolder.onBindView(position);
                    break;
                case FOOTER_VIEW_TYPE:
                    wrapViewHolder.onBindView(position);
                    break;
                default:
                    if(mAdapter != null) mAdapter.onBindViewHolder(holder,position - getHeaderViewCount());
            }
        }

        /**获取通过装饰后的总数量*/
        @Override
        public int getItemCount() {
            if(mAdapter != null) return getHeaderViewCount()+mAdapter.getItemCount()+getFooterViewCount();
            return getHeaderViewCount()+getFooterViewCount();
        }

        @Override
        public long getItemId(int position) {
            if(mAdapter != null && position >= getHeaderViewCount() && position < getHeaderViewCount() + mAdapter.getItemCount()) {
                return mAdapter.getItemId(position - getHeaderViewCount());
            }
            return super.getItemId(position);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onViewRecycled(@NonNull @NotNull ViewHolder holder) {
            //防止头部和尾部被复用
            if(holder instanceof WrapViewHolder) {
                holder.setIsRecyclable(false);
                return;
            }
            if(mAdapter != null) mAdapter.onViewRecycled(holder);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean onFailedToRecycleView(@NonNull @NotNull ViewHolder holder) {
            if(mAdapter != null) mAdapter.onFailedToRecycleView(holder);
            return super.onFailedToRecycleView(holder);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onViewAttachedToWindow(@NonNull @NotNull ViewHolder holder) {
            if(mAdapter != null) mAdapter.onViewAttachedToWindow(holder);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onViewDetachedFromWindow(@NonNull @NotNull ViewHolder holder) {
            if(mAdapter != null) mAdapter.onViewDetachedFromWindow(holder);
            super.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            if(mAdapter != null) mAdapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
            mRecyclerView = null;
            if(mAdapter != null) mAdapter.onDetachedFromRecyclerView(recyclerView);
        }

        /**获取头部数量*/
        private int getHeaderViewCount() {
            return mHeaderViews.size();
        }
        /**获取尾部数量*/
        private int getFooterViewCount() {
            return mFooterViews.size();
        }

        /**获取当前调用的位置*/
        private int getPosition(){
            return mPosition;
        }

        /**添加头部*/
        private void addHeaderView(View view) {
            //防止添加重复的头部
            for (View v : mHeaderViews) {
                if(v.getId() == view.getId()) return;
            }
            mHeaderViews.add(view);
        }

        /**添加尾部*/
        private void addFooterView(View view) {
            //防止添加重复的尾部
            for (View v : mFooterViews) {
                if(v.getId() == view.getId()) return;
            }
            mFooterViews.add(view);
        }

        private static class WrapViewHolder extends ViewHolder {

            public WrapViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
            }
            //设置头尾的数据
            private void onBindView(int position) {
                switch (getItemViewType()) {
                    case HEADER_VIEW_TYPE:
                        break;
                    case FOOTER_VIEW_TYPE:
                        break;
                }
            }
        }

    }
    /**
     * 数据改变监听器，当要数据要刷新时，刷新的实质是原来的适配器
     * 我们需要把刷新的动作交由我们包装过的适配器
     * 参照源码
     */
    private static final class WrapAdapterDataObserver extends AdapterDataObserver {

        private final WrapAdapter mWrapAdapter;

        private WrapAdapterDataObserver(WrapAdapter adapter) {
            mWrapAdapter = adapter;
        }

        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        /**
         * 从指定位置开始刷新数据
         * @param positionStart 开始索引
         * @param itemCount 数量
         */
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onItemRangeChanged(positionStart,itemCount,null);
        }

        /**
         * 从指定索引开始刷新数据
         * @param positionStart 指定索引
         * @param itemCount 刷新的数量
         * @param payload
         */
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(mWrapAdapter.getHeaderViewCount() + positionStart, itemCount,payload);
        }

        /**
         * 从指定索引开始插入数据
         * @param positionStart 指定索引
         * @param itemCount 插入的数量
         */
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(mWrapAdapter.getHeaderViewCount() + positionStart, itemCount);
        }

        /**
         * 从指定索引开始删除数据
         * @param positionStart 指定索引
         * @param itemCount 删除的数量
         */
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(mWrapAdapter.getHeaderViewCount() + positionStart, itemCount);
        }

        /**
         * 移动某条数据到某位置
         * @param fromPosition 要移动数据的索引
         * @param toPosition 要移动到的位置
         * @param itemCount 要移动的数据的数量
         */
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(mWrapAdapter.getHeaderViewCount() + fromPosition, toPosition);
        }
    }
}
