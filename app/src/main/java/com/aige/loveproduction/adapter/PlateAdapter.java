package com.aige.loveproduction.adapter;

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
import com.aige.loveproduction.bean.PlateBean;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 板件扫描适配器
 */
public class PlateAdapter extends RecyclerView.Adapter<PlateAdapter.InnerHolder> {
    private Context context;
    private List<PlateBean> list;
    private Animation animation;
    public PlateAdapter(Context context,List<PlateBean> list) {
        this.context = context;
        this.list = list;
        animation = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
    }
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plate_item,parent,false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InnerHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public TextView opname,operation_date,operation_name,operation_group;
        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            opname = itemView.findViewById(R.id.opname);
            operation_date = itemView.findViewById(R.id.operation_date);
            operation_name = itemView.findViewById(R.id.operation_name);
            operation_group = itemView.findViewById(R.id.operation_group);
            animation.setStartOffset(100);
            itemView.startAnimation(animation);
        }
        public void setData(PlateBean bean) {
            opname.setText(bean.getOpName());
            String formats = null;
            if(bean.getModifyDate() != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                try {
                    Date parse = format.parse(bean.getModifyDate());
                    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                    formats = format1.format(parse);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            operation_date.setText(formats);
            operation_name.setText(bean.getHandler());
            operation_group.setText(bean.getOperator());
        }
    }
}
