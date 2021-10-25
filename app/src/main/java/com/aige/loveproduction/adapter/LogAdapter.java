package com.aige.loveproduction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.aige.loveproduction.R;
import com.aige.loveproduction.bean.HistoryLog;

import java.util.List;

/**
 * 日志管理适配器
 */
public class LogAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    public LogAdapter() {}

    public LogAdapter(Context context) {
        this.context = context;
    }
    public void setDate(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    //获取列表数量
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }
    //获取列表对象
    @Override
    public String getItem(int position) {
        return list == null ? null : list.get(position);
    }
    //获取列表对象Id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.historylog_item, null);
            vh.log_text = convertView.findViewById(R.id.log_text);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final String bean = getItem(position);
        if (bean != null) {
            vh.log_text.setText(bean);

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
    static class ViewHolder {
        public TextView log_text;
    }
}
