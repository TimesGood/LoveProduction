package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 图片选择适配器
 */
public final class ImageSelectAdapter extends AppAdapter<String> {

    private final List<String> mSelectImages;

    public ImageSelectAdapter(Context context, List<String> images) {
        super(context);
        mSelectImages = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.image_select_item);
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final ImageView mImageView;
        private final CheckBox mCheckBox;

        private ViewHolder(int id) {
            super(id);
            mImageView = findViewById(R.id.iv_image_select_image);
            mCheckBox = findViewById(R.id.iv_image_select_check);
        }

        @Override
        public void onBindView(int position) {
            String imagePath = getItemData(position);
            Glide.with(getContext()).load(imagePath).into(mImageView);
//            if(!flag) mCheckBox.setBackground();
            mCheckBox.setChecked(mSelectImages.contains(imagePath));


        }
    }
    private boolean flag = true;
    public void setChecked(boolean flag){
        this.flag = flag;
    }

    //如果没有设置布局管理器，设置默认的布局管理
    @Override
    protected RecyclerView.LayoutManager generateDefaultLayoutManager(Context context) {
        return new GridLayoutManager(context, 3);
    }
}