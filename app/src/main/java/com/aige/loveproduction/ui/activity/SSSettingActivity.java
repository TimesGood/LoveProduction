package com.aige.loveproduction.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.AppActivity;
import com.aige.loveproduction.util.CommonUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;

public class SSSettingActivity extends AppActivity {
    private RadioGroup radio;
    private int screenWidth;
    private int screenHeight;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sssetting;
    }

    @Override
    protected void initView() {
        radio = findViewById(R.id.radio);
    }

    @Override
    protected void initData() {
        setCenterTitle("异形工序设置");
        screenWidth = CommonUtils.getScreenWidth(this);
        screenHeight = CommonUtils.getScreenHeight(this);
        initRadio();
    }

    private void initRadio() {
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(screenWidth / 4 - 22,
                screenHeight / 22);
        layoutParams.setMargins(11, 11, 11, 11);
        RadioButton radioButton1 = setButton(0, "异形开料", 4);
        RadioButton radioButton2 = setButton(1, "异形封边", 4);
        radio.addView(radioButton1, layoutParams);
        radio.addView(radioButton2, layoutParams);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferencesUtils.detailSetting(SSSettingActivity.this,Sssetting);
                //RadioButton radioButton = (RadioButton) group.getChildAt(checkedId);
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);

                String s = radioButton.getText().toString();
                SharedPreferencesUtils.saveSetting(SSSettingActivity.this,Sssetting,s,true);
            }
        });
        int childCount = radio.getChildCount();
        for (byte i = 0; i < childCount; i++) {
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
        radioButton.setTextColor(getColorStateList(R.color.checked_text_select_black_write));
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