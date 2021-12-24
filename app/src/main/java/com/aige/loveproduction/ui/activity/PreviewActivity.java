package com.aige.loveproduction.ui.activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppActivity;
import com.aige.loveproduction.test.BaseAdapter;
import com.aige.loveproduction.test.WrapRecyclerView;
import com.aige.loveproduction.test.TestAdapter;
import com.aige.loveproduction.ui.adapter.HomeAdapter;
import com.aige.loveproduction.ui.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppActivity {
    private ViewPager view_page;
    private ViewPagerAdapter<View> adapter;
    private TabLayout tab_layout;
    private WrapRecyclerView recyclerview_data;
    private TestAdapter adapter1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    protected void initView() {
        recyclerview_data = findViewById(R.id.recyclerview_data);
//        view_page = findViewById(R.id.view_page);
//        tab_layout = findViewById(R.id.tab_layout);
    }

    @Override
    protected void initData() {
        adapter1 = new TestAdapter(this);
        List<String> list = new ArrayList<>();
        for(int i = 0;i < 100;i++) {
            list.add(String.valueOf(i));
        }
        adapter1.setData(list);
        recyclerview_data.setAdapter(adapter1);
//        setCenterTitle("功能预览");
//        adapter = new ViewPagerAdapter<>();
//        View inflate = LayoutInflater.from(this).inflate(R.layout.plate_item, view_page, false);
//        View inflate1 = LayoutInflater.from(this).inflate(R.layout.transfer_item, view_page, false);
//        adapter.addView(inflate,"测试");
//        adapter.addView(inflate1,"测试2");
//        view_page.setAdapter(adapter);
//        tab_layout.setupWithViewPager(view_page);
    }
}