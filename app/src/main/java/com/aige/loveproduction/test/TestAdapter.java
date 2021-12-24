package com.aige.loveproduction.test;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.aige.loveproduction.R;

import org.jetbrains.annotations.NotNull;

public class TestAdapter extends AppAdapter<String> {
    public TestAdapter(Context context) {
        super(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.user_select_item);
    }

    public final class ViewHolder extends BaseAdapter<?>.ViewHolder {
        private TextView select_text;
        public ViewHolder(int id) {
            super(id);
            select_text = findViewById(R.id.select_text);
        }
        @Override
        public void onBindView(int position) {
            select_text.setText(getItemData(position));
        }
    }
}
