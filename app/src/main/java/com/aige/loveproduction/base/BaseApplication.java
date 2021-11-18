package com.aige.loveproduction.base;

import android.app.Application;
import android.util.Log;

import com.aige.loveproduction.manager.ActivityManager;

import java.util.HashMap;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Activity栈管理初始化
        ActivityManager.getInstance().init(this);
    }

}
