package com.aige.loveproduction.ui.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.aige.loveproduction.R;
import com.aige.loveproduction.ui.adapter.LogAdapter;
import com.aige.loveproduction.bean.HistoryLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史记录页面
 */
public class HistoryLogActivity extends AppCompatActivity {
    private LogAdapter adapter;
    private List<HistoryLog> ebl;
    private ListView log_listview;
    private TextView toolbar_text;
    private Toolbar toolbar_title;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historylog);
        bindViews();
        initUI();
    }
    private void bindViews() {
        log_listview = findViewById(R.id.log_listview);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_text = findViewById(R.id.toolbar_text);
    }
    private void initUI() {
        toolbar_title.setTitle("");
        toolbar_title.setBackground(ContextCompat.getDrawable(this,R.color.blue));
        toolbar_text.setText("历史记录");
        toolbar_title.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar_title);
        toolbar_title.setNavigationOnClickListener(v -> HistoryLogActivity.this.finish());
        adapter = new LogAdapter(this);
        ebl = new ArrayList<>();
    }
}
