package com.aige.loveproduction.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppActivity;

public class ProblemActivity extends AppActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_problem;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void initData() {
        setCenterTitle("使用问题");
    }
}