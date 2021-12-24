package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.StorageBean;


public class SendVerifyAdapter extends AppAdapter<StorageBean> {

    public SendVerifyAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(R.layout.send_verify_item);
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        public TextView solution_name, package_count,not_pack,not_in_storage,not_out_storage,not_send,bin_name;
        public LinearLayout work_scan_layout;
        public ViewHolder(int id) {
            super(id);
            solution_name = findViewById(R.id.solution_name);
            package_count = findViewById(R.id.package_count);
            not_pack = findViewById(R.id.not_pack);
            not_in_storage = findViewById(R.id.not_in_storage);
            not_out_storage = findViewById(R.id.not_out_storage);
            not_send = findViewById(R.id.not_send);
            bin_name = findViewById(R.id.bin_name);

        }

        @Override
        public void onBindView(int position) {
            solution_name.setText(getItemData(position).getSolutionName());
            package_count.setText(String.valueOf(getItemData(position).getTotalPackage()));
            not_pack.setText(String.valueOf(getItemData(position).getWeiBaoNumber()));
            not_in_storage.setText(String.valueOf(getItemData(position).getNotInPackage()));
            not_out_storage.setText(String.valueOf(getItemData(position).getNotOutPackage()));
            not_send.setText(String.valueOf(getItemData(position).getNotSendPackage()));
            bin_name.setText(getItemData(position).getBinCode());
        }
    }
}