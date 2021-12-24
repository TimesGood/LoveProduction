package com.aige.loveproduction.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.aige.loveproduction.R;
import com.aige.loveproduction.ui.adapter.ImagePreviewAdapter;
import com.aige.loveproduction.ui.adapter.RecyclerPagerAdapter;
import com.aige.loveproduction.base.AppActivity;
import com.aige.loveproduction.base.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 图片预览
 */
public final class ImagePreviewActivity extends AppActivity
        implements BaseAdapter.OnItemClickListener {

    private ViewPager mViewPager;
    private ImagePreviewAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.image_preview_activity;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_image_preview_pager);
    }

    @Override
    protected void initData() {
        ArrayList<String> images = getStringArrayList("image");
        if (images == null || images.isEmpty()) {
            finish();
            return;
        }
        mAdapter = new ImagePreviewAdapter(this);
        mAdapter.setData(images);
        mAdapter.setOnItemClickListener(this);
        mViewPager.setAdapter(new RecyclerPagerAdapter(mAdapter));
        //定位到要查看图片
        if (images.size() != 1) {
            int index = getInt("index");
            if (index < images.size()) {
                mViewPager.setCurrentItem(index);
            }
        }
    }

    /**
     * 单击图片退出预览
     */
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (!isFinishing()) {
            finish();
        }
    }

    /**
     * 跳转页面时，携带一些数据
     * @param context 上下文
     * @param urls 图片地址集合
     * @param index 要查看该集合中图片的索引
     */
    public static void start(Context context, List<String> urls, int index) {
        if (urls == null || urls.isEmpty()) return;
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        if (urls.size() > 2500) {
            // 请注意：如果传输的数据量过大，会抛出此异常，并且这种异常是不能被捕获的
            // 所以当图片数量过多的时候，我们应当只显示一张，这种一般是手机图片过多导致的
            // 经过测试，传入 3121 张图片集合的时候会抛出此异常，所以保险值应当是 2500
            // android.os.TransactionTooLargeException: data parcel size 521984 bytes
            urls = Collections.singletonList(urls.get(index));
        }

        if (urls instanceof ArrayList) {
            intent.putExtra("image", (ArrayList<String>) urls);
        } else {
            intent.putExtra("image", new ArrayList<>(urls));
        }
        intent.putExtra("index", index);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}