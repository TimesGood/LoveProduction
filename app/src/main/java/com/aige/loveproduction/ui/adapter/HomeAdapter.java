package com.aige.loveproduction.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppAdapter;
import com.aige.loveproduction.bean.HomeBean;


public class HomeAdapter extends AppAdapter<HomeBean> {

    public HomeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.image_text_layout);
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        private RelativeLayout image_text_layout;
        private ImageView img_top;
        private TextView text_below;
        public ViewHolder(int id) {
            super(id);
            image_text_layout = findViewById(R.id.image_text_layout);
            img_top = findViewById(R.id.img_top);
            text_below = findViewById(R.id.text_below);
        }


        @Override
        public void onBindView(int position) {
            image_text_layout.setId(getItemData(position).getId());
            img_top.setImageDrawable(ContextCompat.getDrawable(getContext(),getItemData(position).getImg_id()));
            text_below.setText(getItemData(position).getText());
        }
    }
}