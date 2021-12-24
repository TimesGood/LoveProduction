package com.aige.loveproduction.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppFragment;
import com.aige.loveproduction.ui.activity.MainActivity;
import com.aige.loveproduction.ui.adapter.UserCenterAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.bean.UserCenterBean;
import com.aige.loveproduction.ui.activity.ReportChannelOneActivity;
import com.aige.loveproduction.ui.activity.ReportRefundMonthActivity;
import com.aige.loveproduction.ui.activity.ReportChannelTwoActivity;
import com.aige.loveproduction.ui.activity.ReportDirectlyMonthActivity;
import com.aige.loveproduction.ui.activity.ReportOrderDayActivity;
import com.aige.loveproduction.ui.activity.ReportOrderMonthActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面
 */
public class ReportFragment extends AppFragment<MainActivity> implements BaseAdapter.OnItemClickListener{
    private WrapRecyclerView recyclerview_data;
    private UserCenterAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.homefragment;
    }

    //id数组
    private final int[] ids = new int[]{

                    0,1,2,3,4,
                    5

    };
    private static final String[] text = new String[]{
            "月回款统计","直营单值月统计","渠道一部单值月统计","渠道二部单值月统计","月下单统计","日下单统计"
    };
    @Override
    public void initView() {
        recyclerview_data = findViewById(R.id.recyclerview_data);
        setSelectItem();
    }

    //设置选项列表
    private void setSelectItem() {
        adapter = new UserCenterAdapter(getAttachActivity());
        List<UserCenterBean> list = new ArrayList<>();
        for(int i = 0;i < ids.length;i++) {
            UserCenterBean bean = new UserCenterBean();
            bean.setId(ids[i]);
            bean.setRight_img_id(R.drawable.right_arrow);
            bean.setSelect_text(text[i]);
            list.add(bean);
        }
        adapter.setOnItemClickListener(this);
        adapter.setData(list);
        LinearLayoutManager manager = new LinearLayoutManager(getAttachActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
    }
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        switch (itemView.getId()) {
            case 0:
                startActivity(ReportRefundMonthActivity.class);
                break;
            case 1:
                startActivity(ReportDirectlyMonthActivity.class);
                break;
            case 2:
                startActivity(ReportChannelOneActivity.class);
                break;
            case 3:
                startActivity(ReportChannelTwoActivity.class);
                break;
            case 4:
                startActivity(ReportOrderMonthActivity.class);
                break;
            case 5:
                startActivity(ReportOrderDayActivity.class);
                break;
        }
    }
}
