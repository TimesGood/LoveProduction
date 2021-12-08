package com.aige.loveproduction.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;


import com.aige.loveproduction.R;
import com.aige.loveproduction.animation.BaseAnimation;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动欢迎页面
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //刘海屏适配
//        WindowManager.LayoutParams attributes = getWindow().getAttributes();
//        attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        init();
    }
    private void init() {
        ImageView button_text = findViewById(R.id.button_text);
        BaseAnimation animation = new BaseAnimation();
        animation.alphaTran(button_text,1000);
        Timer timer = new Timer();//延迟类
        //延迟目标
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(SharedPreferencesUtils.getBoolean(SplashActivity.this,"loginInfo","isLogin")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        };
        timer.schedule(task,1500);//task延迟3秒
    }
}
