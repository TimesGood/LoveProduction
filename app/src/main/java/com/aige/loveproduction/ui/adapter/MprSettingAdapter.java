package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.HomeBean;
import com.aige.loveproduction.mpr.MprView;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;


public class MprSettingAdapter extends AppAdapter<String>{
    private MprView mprView;
    public MprSettingAdapter(Context context) {
        super(context);
        List<String> data = new ArrayList<>();
        data.add("钉子坐标描述");
        data.add("距离线");
        data.add("切割线坐标描述");
        setData(data);
    }
    public void setMprView(MprView mprView){
        this.mprView = mprView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.open_inclue);
    }


    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        private final TextView open_describe;
        private final SwitchCompat open_switch;
        public ViewHolder(int id) {
            super(id);
            open_describe = findViewById(R.id.open_describe);
            open_switch = findViewById(R.id.open_switch);
            open_switch.setOnClickListener(this);
        }


        @Override
        public void onBindView(int position) {
            open_describe.setText(getItemData(position));
            open_switch.setId(position);
            boolean ischeck = false;
            if(position == 0) {
                ischeck = SharedPreferencesUtils.getBoolean(getContext(), "mprSettings", "mpr_bohrcoord_describe");
            }else if(position == 1) {
                ischeck = SharedPreferencesUtils.getBoolean(getContext(),"mprSettings","mpr_distance_line");
            }else if(position == 2) {
                ischeck = SharedPreferencesUtils.getBoolean(getContext(),"mprSettings","mpr_cuttingcoord_describe");
            }
            open_switch.setChecked(ischeck);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            int id = v.getId();
            if(v instanceof SwitchCompat) {
                SwitchCompat switchView = (SwitchCompat) v;
                boolean checked = switchView.isChecked();
                if(id == 0) {
                    SharedPreferencesUtils.saveSetting(getContext(),"mprSettings","mpr_bohrcoord_describe",checked);
                }else if(id == 1) {
                    SharedPreferencesUtils.saveSetting(getContext(),"mprSettings","mpr_distance_line",checked);
                }else if(id == 2) {
                    SharedPreferencesUtils.saveSetting(getContext(),"mprSettings","mpr_cuttingcoord_describe",checked);
                }
                mprView.initSetting();
            }

        }
    }

}