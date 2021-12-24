package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.StorageBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StorageAdapter extends AppAdapter<StorageBean>{

    public StorageAdapter(Context context) {
        super(context);
    }
    @Override
    public void setData(List<StorageBean> data) {
        StringBuffer buffer;
        for (int i = 0;i < data.size();i++) {
            buffer = new StringBuffer();
            StorageBean storageBean = data.get(i);
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

            }
            data.get(i).setBinCode(buffer.toString().substring(0,buffer.toString().length()-1));
        }
        super.setData(data);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new InnerHolder(R.layout.storage_item);
    }

    public class InnerHolder extends ViewHolder {
        TextView solution_name,package_count,storage_ok,storage_no,bin_name;
        public InnerHolder(int id) {
            super(id);
            solution_name = itemView.findViewById(R.id.solution_name);
            package_count = itemView.findViewById(R.id.package_count);
            storage_ok = itemView.findViewById(R.id.storage_ok);
            storage_no = itemView.findViewById(R.id.storage_no);
            bin_name  = itemView.findViewById(R.id.bin_name);
        }

        @Override
        public void onBindView(int position) {
            StorageBean itemData = getItemData(position);
            solution_name.setText(itemData.getSolutionName());
            package_count.setText(String.valueOf(itemData.getPackageTotal()));
            if("库位查询".equals(itemData.getType())){
                storage_ok.setText(String.valueOf(itemData.getInPackage()));
                storage_no.setText(String.valueOf(itemData.getNotIntoPackage()));
            }else if("入库".equals(itemData.getType())) {
                storage_ok.setText(String.valueOf(itemData.getInPackage()));
                storage_no.setText(String.valueOf(itemData.getNotIntoPackage()));
            }else if("出库".equals(itemData.getType())){
                storage_ok.setText(String.valueOf(itemData.getOutPackage()));
                storage_no.setText(String.valueOf(itemData.getInPackage()));
            }else if("发货".equals(itemData.getType())) {
                storage_ok.setText(String.valueOf(itemData.getSendPackage()));
                storage_no.setText(String.valueOf(itemData.getOutPackage()));
            }else if("发货验证".equals(itemData.getType())) {
                package_count.setText(String.valueOf(itemData.getTotalPackage()));
                storage_ok.setText(String.valueOf(itemData.getSendPackage()));
                storage_no.setText(String.valueOf(itemData.getNotSendPackage()));
            }else if("移库".equals(itemData.getType())) {
                storage_ok.setText(String.valueOf(itemData.getInPackage()));
                storage_no.setText(String.valueOf(itemData.getNotIntoPackage()));
            }
            bin_name.setText(itemData.getBinCode());
        }
    }
}
