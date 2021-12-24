package com.aige.loveproduction.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;

/**
 * 检验混单页面
 */
public class ExamineActivity extends AppActivity {
    private SwitchCompat in_stock_but,out_stock_but;
    private final static String VAG = "ExamineActivity";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_examine;
    }


    @Override
    protected void initView() {
        in_stock_but = findViewById(R.id.in_stock_but);
        out_stock_but = findViewById(R.id.out_stock_but);
    }

    @Override
    protected void initData() {
        setCenterTitle("检验混单");
        initSetting();
        in_stock_but.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Log.d(VAG,"开");
                    SharedPreferencesUtils.saveSetting(ExamineActivity.this,"examineSettings","inStock",true);
                }else{
                    Log.d(VAG,"关");
                    SharedPreferencesUtils.saveSetting(ExamineActivity.this,"examineSettings","inStock",false);
                }
            }
        });
        out_stock_but.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Log.d(VAG,"开");
                    SharedPreferencesUtils.saveSetting(ExamineActivity.this,"examineSettings","outStock",true);
                }else{
                    Log.d(VAG,"关");
                    SharedPreferencesUtils.saveSetting(ExamineActivity.this,"examineSettings","outStock",false);
                }
            }
        });
    }

    //初始化设置
    private void initSetting() {
        boolean stock_in = SharedPreferencesUtils.getBoolean(ExamineActivity.this, "examineSettings", "inStock");
        boolean stock_out = SharedPreferencesUtils.getBoolean(ExamineActivity.this, "examineSettings", "outStock");
        in_stock_but.setChecked(stock_in);
        out_stock_but.setChecked(stock_out);
    }
}
