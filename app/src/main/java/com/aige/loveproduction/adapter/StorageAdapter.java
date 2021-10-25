package com.aige.loveproduction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.bean.StorageBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.InnerHolder> {
    private static List<StorageBean> mData;
    private Context context;

    public StorageAdapter() {
    }
    public StorageAdapter(Context context,List<StorageBean> beanList) {
        this.context = context;
        mData = beanList;
        StringBuffer buffer;
        for (int i = 0;i < mData.size();i++) {
            buffer = new StringBuffer();
            StorageBean storageBean = mData.get(i);
            if(storageBean.getBinCode() == null) {
                continue;
            }
            String[] split = storageBean.getBinCode().split(",");
            if(split.length == 0) continue;
            for(int j = 0;j < split.length;j++) {
                if(j%2 == 0 && j != 0) {
                    buffer.append("\n");
                }
                buffer.append(split[j]).append(",");
                System.out.println(buffer.toString());

            }
            mData.get(i).setBinCode(buffer.toString().substring(0,buffer.toString().length()-1));
        }
    }

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.storage_item,parent,false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InnerHolder holder, int position) {
        holder.setDate(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if(mData != null) return mData.size();
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView solution_name,package_count,storage_ok,storage_no,bin_name;
        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            solution_name = itemView.findViewById(R.id.solution_name);
            package_count = itemView.findViewById(R.id.package_count);
            storage_ok = itemView.findViewById(R.id.storage_ok);
            storage_no = itemView.findViewById(R.id.storage_no);
            bin_name  = itemView.findViewById(R.id.bin_name);
        }

        public void setDate(StorageBean bean) {
            solution_name.setText(bean.getSolutionName());
            package_count.setText(String.valueOf(bean.getPackageTotal()));
            if("库位查询".equals(bean.getType())){
                storage_ok.setText(String.valueOf(bean.getInPackage()));
                storage_no.setText(String.valueOf(bean.getNotIntoPackage()));
            }else if("入库".equals(bean.getType())) {
                storage_ok.setText(String.valueOf(bean.getInPackage()));
                storage_no.setText(String.valueOf(bean.getNotIntoPackage()));
            }else if("出库".equals(bean.getType())){
                storage_ok.setText(String.valueOf(bean.getOutPackage()));
                storage_no.setText(String.valueOf(bean.getInPackage()));
            }else if("发货".equals(bean.getType())) {
                storage_ok.setText(String.valueOf(bean.getSendPackage()));
                storage_no.setText(String.valueOf(bean.getOutPackage()));
            }else if("发货验证".equals(bean.getType())) {
                package_count.setText(String.valueOf(bean.getTotalPackage()));
                storage_ok.setText(String.valueOf(bean.getSendPackage()));
                storage_no.setText(String.valueOf(bean.getNotSendPackage()));
            }else if("移库".equals(bean.getType())) {
                storage_ok.setText(String.valueOf(bean.getInPackage()));
                storage_no.setText(String.valueOf(bean.getNotIntoPackage()));
            }
            bin_name.setText(bean.getBinCode());

        }
    }
}
