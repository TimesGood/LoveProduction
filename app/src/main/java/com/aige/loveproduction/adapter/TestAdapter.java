package com.aige.loveproduction.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aige.loveproduction.R;
import com.aige.loveproduction.bean.ScanCodeBean;


public class TestAdapter extends AppAdapter<ScanCodeBean> {

    public TestAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        public TextView work_order_text, orderId_text,yesScan_text,planNo_text,noScan_text,count_text,area_text,massage_text;
        public LinearLayout work_scan_layout;
        ViewHolder() {
            super(R.layout.work_item);
            work_order_text = findViewById(R.id.work_order_text);
            orderId_text = findViewById(R.id.orderId_text);
            yesScan_text = findViewById(R.id.yesScan_text);
            planNo_text = findViewById(R.id.planNo_text);
            noScan_text = findViewById(R.id.noScan_text);
            count_text = findViewById(R.id.count_text);
            area_text = findViewById(R.id.area_text);
            massage_text = findViewById(R.id.massage_text);
            work_scan_layout = findViewById(R.id.work_scan_layout);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_anim);
            work_scan_layout.startAnimation(animation);

        }

        @Override
        public void onBindView(int position) {
            work_order_text.setText(getItem(position).getWono());
            orderId_text.setText(getItem(position).getOrderId());
            yesScan_text.setText(getItem(position).getSaoMiaoCount());
            planNo_text.setText(getItem(position).getPlanNo());
            noScan_text.setText(getItem(position).getWeiSaoCount());
            count_text.setText(getItem(position).getTotalCnt());
            area_text.setText(getItem(position).getTotalArea());
            massage_text.setText(getItem(position).getMessage());
        }
    }
}