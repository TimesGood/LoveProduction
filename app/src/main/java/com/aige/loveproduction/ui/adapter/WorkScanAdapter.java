package com.aige.loveproduction.ui.adapter;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.ScanCodeBean;

import org.jetbrains.annotations.NotNull;

public class WorkScanAdapter extends AppAdapter<ScanCodeBean> {

    public WorkScanAdapter(Context context) {
        super(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new InnerHolder(R.layout.work_item);
    }


    public class InnerHolder extends ViewHolder {
        public TextView work_order_text, orderId_text,yesScan_text,planNo_text,noScan_text,count_text,area_text,massage_text;
        public LinearLayout work_scan_layout;

        public InnerHolder(int id) {
            super(id);
            work_order_text = findViewById(R.id.work_order_text);
            orderId_text = findViewById(R.id.orderId_text);
            yesScan_text = findViewById(R.id.yesScan_text);
            planNo_text = findViewById(R.id.planNo_text);
            noScan_text = findViewById(R.id.noScan_text);
            count_text = findViewById(R.id.count_text);
            area_text = findViewById(R.id.area_text);
            massage_text = findViewById(R.id.massage_text);
            work_scan_layout = findViewById(R.id.work_scan_layout);
        }


        @Override
        public void onBindView(int position) {
            ScanCodeBean itemData = getItemData(position);
            work_order_text.setText(itemData.getWono());
            orderId_text.setText(itemData.getOrderId());
            yesScan_text.setText(itemData.getSaoMiaoCount());
            planNo_text.setText(itemData.getPlanNo());
            noScan_text.setText(itemData.getWeiSaoCount());
            count_text.setText(itemData.getTotalCnt());
            area_text.setText(itemData.getTotalArea());
            massage_text.setText(itemData.getMessage());
        }
    }
}