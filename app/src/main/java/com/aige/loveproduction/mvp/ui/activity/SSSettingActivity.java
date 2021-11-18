package com.aige.loveproduction.mvp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.util.ScreenUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;

public class SSSettingActivity extends AppCompatActivity {
    private RadioGroup radio;
    private int screenWidth,screenHeight;
    private Toolbar toolbar;
    private TextView toolbar_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sssetting);
        radio = findViewById(R.id.radio);
        screenWidth = ScreenUtils.getScreenWidth(this);
        screenHeight = ScreenUtils.getScreenHeight(this);
        initRadio(true);

        toolbar = findViewById(R.id.toolbar_title);
        toolbar_text = findViewById(R.id.toolbar_text);
        toolbar.setTitle("");
        toolbar.setBackground(ContextCompat.getDrawable(this,R.color.blue));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar_text.setText("异形工序设置");
        setSupportActionBar(toolbar);
    }
    private void initRadio(boolean isInit) {
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(screenWidth / 4 - 22,
                screenWidth / 11);
        layoutParams.setMargins(11, 11, 11, 11);
        RadioButton radioButton1 = setButton(0, "异形开料", 4);
        RadioButton radioButton2 = setButton(1, "异形封边", 4);
        radio.addView(radioButton1, layoutParams);
        radio.addView(radioButton2, layoutParams);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferencesUtils.detailSetting(SSSettingActivity.this,"sssetting");
                //RadioButton radioButton = (RadioButton) group.getChildAt(checkedId);
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);

                String s = radioButton.getText().toString();
                SharedPreferencesUtils.saveSetting(SSSettingActivity.this,"sssetting",s,true);
            }
        });
        int childCount = radio.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioButton radioButton = (RadioButton) radio.getChildAt(i);
            boolean workgroupSettings = SharedPreferencesUtils.getBoolean(SSSettingActivity.this, "sssetting", radioButton.getText().toString());
            radioButton.setChecked(workgroupSettings);
        }

    }
    private RadioButton setButton(int id, String text, int column) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setButtonDrawable(R.drawable.checkbox);
        radioButton.setId(id);
        radioButton.setText(text);
        radioButton.setTextColor(getColorStateList(R.color.checkbox_text_color));
        radioButton.setTextSize(16);
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setBackground(ContextCompat.getDrawable(this, R.drawable.checkbox));
        return radioButton;
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