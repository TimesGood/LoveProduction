package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.util.FormatDateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 板件扫描适配器,多条目
 */
public class PlateAdapter extends AppAdapter<PlateBean> {
    private static final int item_type = -1;

    public PlateAdapter(Context context) {
        super(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == item_type) {
            return new HeaderHolder(R.layout.plate_find_head_item);
        }
        return new InnerHolder(R.layout.plate_item);
    }

    /**
     * 重写多条目配置
     */
    @Override
    public int getItemViewType(int position) {
        if(getItemData(position) == null) {
            return item_type;
        }
        return super.getItemViewType(position);
    }
    public class InnerHolder extends ViewHolder {
        public TextView opname,operation_date,operation_name,operation_group;
        public InnerHolder(int id) {
            super(id);
            opname = findViewById(R.id.opname);
            operation_date = findViewById(R.id.operation_date);
            operation_name = findViewById(R.id.operation_name);
            operation_group = findViewById(R.id.operation_group);
            View itemView = getItemView();
            itemView.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_left_in));
            itemView.setBackgroundColor(getContext().getColor(R.color.white));
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
    public class HeaderHolder extends ViewHolder {
        private final TextView plate_item_title,head_planNo_text,head_plan_data;
        public HeaderHolder(int id) {
            super(id);
            plate_item_title = findViewById(R.id.plate_item_title);
            head_planNo_text = findViewById(R.id.head_planNo_text);
            head_plan_data = findViewById(R.id.head_plan_data);
        }

        @Override
        public void onBindView(int position) {
            PlateBean itemData = getItemData(position + 1);
            plate_item_title.setText(itemData.getSolutionConfigName());
            String p = itemData.getPlanNo()+"-"+itemData.getApS_Code();
            head_planNo_text.setText(p);
            head_plan_data.setText(itemData.getPlanEndDate());
        }
    }
}
