package com.aige.loveproduction.mvp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.ResourcesAction;

public class ProblemActivity extends AppCompatActivity{
    private Toolbar toolbar_title;
    private TextView toolbar_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        initView();
    }
    private void initView() {
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_title.setTitle("");
        toolbar_title.setBackgroundColor(getColor(R.color.blue));
        toolbar_title.setNavigationIcon(R.drawable.back);
        toolbar_text.setText("使用问题");
        setSupportActionBar(toolbar_title);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}