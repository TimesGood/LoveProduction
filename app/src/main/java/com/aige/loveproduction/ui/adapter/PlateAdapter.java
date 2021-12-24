package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.util.FormatDateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 板件扫描适配器
 */
public class PlateAdapter extends AppAdapter<PlateBean> {

    public PlateAdapter(Context context) {
        super(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new InnerHolder(R.layout.plate_item);
    }

    public class InnerHolder extends ViewHolder {
        public TextView opname,operation_date,operation_name,operation_group;
        public InnerHolder(int id) {
            super(id);
            opname = findViewById(R.id.opname);
            operation_date = findViewById(R.id.operation_date);
            operation_name = findViewById(R.id.operation_name);
            operation_group = findViewById(R.id.operation_group);
            getItemView().setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_left_in));
        }

        @Override
        public void onBindView(int position) {
            PlateBean itemData = getItemData(position);
            opname.setText(itemData.getOpName());
            operation_date.setText(FormatDateUtil.FormatDate(itemData.getModifyDate(),"yyyy-MM-dd hh:mm"));
            operation_name.setText(itemData.getHandler());
            operation_group.setText(itemData.getOperator());
        }
    }
}
