package com.aige.loveproduction.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.aige.loveproduction.bean.UserCenterBean;

import org.jetbrains.annotations.NotNull;

public class UserCenterAdapter extends AppAdapter<UserCenterBean>{
    public UserCenterAdapter(@NonNull @NotNull Context context) {
        super(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }
    private class ViewHolder extends AppAdapter<?>.ViewHolder {
        private final RelativeLayout user_select;
        private final ImageView left_img,right_img;
        private final TextView select_text;
        ViewHolder() {
            super(R.layout.user_select_item);
            user_select = findViewById(R.id.user_select);
            left_img = findViewById(R.id.left_img);
            right_img = findViewById(R.id.right_img);
            select_text = findViewById(R.id.select_text);
        }
        @Override
        public void onBindView(int position) {
            user_select.setId(getItem(position).getId());
            left_img.setImageDrawable(ContextCompat.getDrawable(getContext(),getItem(position).getLeft_img_id()));
            right_img.setImageDrawable(ContextCompat.getDrawable(getContext(),getItem(position).getRight_img_id()));
            select_text.setText(getItem(position).getSelect_text());
        }
    }

}
