package com.aige.loveproduction.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aige.loveproduction.R;
import com.aige.loveproduction.bean.TransportBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TransportAdapter extends AppAdapter<TransportBean.TransportBeans>{
    public TransportAdapter(@NonNull @NotNull Context context) {
        super(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        private final TextView packageCode,packageDate;
        public ViewHolder() {
            super(R.layout.transfer_item);
            packageCode = findViewById(R.id.packageCode);
            packageDate = findViewById(R.id.packageDate);
        }


        @Override
        public void onBindView(int position) {
            packageCode.setText(getItem(position).getPackageCode());
            packageDate.setText(getItem(position).getTransportDate());

        }
    }
}
