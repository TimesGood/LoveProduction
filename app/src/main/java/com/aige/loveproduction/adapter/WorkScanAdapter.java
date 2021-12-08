package com.aige.loveproduction.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.bean.ScanCodeBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkScanAdapter extends RecyclerView.Adapter<WorkScanAdapter.InnerHolder> implements View.OnClickListener{
    private Context context;
    private List<ScanCodeBean> mData;
    private Animation animation;
    public WorkScanAdapter(Context context, List<ScanCodeBean> data) {
        this.context = context;
        mData = data;

    }

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_item,parent,false);
        animation = AnimationUtils.loadAnimation(context,R.anim.scale_anim);
        view.startAnimation(animation);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InnerHolder holder, int position) {
        holder.setData(mData.get(position));
        holder.work_scan_layout.setTag(position);
    }

    @Override
    public int getItemCount() {
        if(mData != null) {
            return mData.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public TextView work_order_text, orderId_text,yesScan_text,planNo_text,noScan_text,count_text,area_text,massage_text;
        public LinearLayout work_scan_layout;

        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            work_order_text = itemView.findViewById(R.id.work_order_text);
            orderId_text = itemView.findViewById(R.id.orderId_text);
            yesScan_text = itemView.findViewById(R.id.yesScan_text);
            planNo_text = itemView.findViewById(R.id.planNo_text);
            noScan_text = itemView.findViewById(R.id.noScan_text);
            count_text = itemView.findViewById(R.id.count_text);
            area_text = itemView.findViewById(R.id.area_text);
            massage_text = itemView.findViewById(R.id.massage_text);
            work_scan_layout = itemView.findViewById(R.id.work_scan_layout);
            work_scan_layout.setOnClickListener(WorkScanAdapter.this);
        }
        public void setData(ScanCodeBean bean) {
            work_order_text.setText(bean.getWono());
            orderId_text.setText(bean.getOrderId());
            yesScan_text.setText(bean.getSaoMiaoCount());
            planNo_text.setText(bean.getPlanNo());
            noScan_text.setText(bean.getWeiSaoCount());
            count_text.setText(bean.getTotalCnt());
            area_text.setText(bean.getTotalArea());
            massage_text.setText(bean.getMessage());
        }
    }
    //更新某条目message
    public void update(int position,String message) {
        ScanCodeBean scanCodeBean = mData.get(position);
        scanCodeBean.setMessage(message);
        notifyItemChanged(position);
    }
    //自定义一个回调接口来实现Click事件
    public interface OnItemClickListener  {
        void onItemClick(View v,ScanCodeBean bean, int position);
    }
    private OnItemClickListener mOnItemClickListener;//声明自定义的接口
    //定义方法并暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }
    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(); //getTag()获取数据
        ScanCodeBean scanCodeBean = mData.get(position);
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.work_scan_layout:
                    mOnItemClickListener.onItemClick(v,scanCodeBean,position);
                    if("错误".equals(scanCodeBean.getMessage()) || "超时".equals(scanCodeBean.getMessage())) {
                        notifyItemChanged(position);
                    }
                    break;
            }
        }
    }
}