package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.TransportBean;

import org.jetbrains.annotations.NotNull;

public class TransportAdapter extends AppAdapter<TransportBean.TransportBeans> {
    private int Type = 0;
    public TransportAdapter(@NonNull @NotNull Context context) {
        super(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.transfer_item);
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        private final TextView packageCode,packageDate;
        public ViewHolder(int id) {
            super(id);
            packageCode = findViewById(R.id.packageCode);
            packageDate = findViewById(R.id.packageDate);
        }


        @Override
        public void onBindView(int position) {
            String packageCodeStr = getItemData(position).getPackageCode();
            packageCode.setText(packageCodeStr == null ? null : packageCodeStr.substring(packageCodeStr.length()-5));
            if(Type == 0) {
                packageDate.setText(getItemData(position).getTransportDate());
            }else if(Type == 1) {
                packageDate.setText(getItemData(position).getConfirmDate());
            }


        }
    }
    public void setType(int type) {
        Type = type;
    }
}
