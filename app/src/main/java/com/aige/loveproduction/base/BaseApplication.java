package com.aige.loveproduction.base;

import android.app.Application;
import android.util.Log;

import com.aige.loveproduction.manager.ActivityManager;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.HashMap;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Activity栈管理初始化
        ActivityManager.getInstance().init(this);
        initQbSdk();

    }
    private void initQbSdk() {
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }
            @Override
            public void onViewInitFinished(boolean b) {
                //这里被回调，并且b=true说明内核初始化并可以使用
                //如果b=false,内核会尝试安装，你可以通过下面监听接口获知
                if(b) {
                    Log.d("X5内核加载*****************", "加载成功.........................................");
                }else{
                    Log.d("X5内核加载*****************", "加载失败.........................................");
                }
            }
        });
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                //tbs内核下载完成回调
                Log.d("X5内核下载*****************", "下载成功.........................................");
            }

            @Override
            public void onInstallFinish(int i) {
                //内核安装完成回调，
                Log.d("X5内核安装*****************", "安装成功.........................................");
            }

            @Override
            public void onDownloadProgress(int i) {
                //下载进度监听
                Log.d("X5内核下载进度***************", i+".........................................");
            }
        });

//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                // TODO Auto-generated method stub
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                if(arg0) {
//                    Log.d("X5内核加载*****************", "加载成功.........................................");
//                }else{
//                    Log.d("X5内核加载*****************", "加载失败.........................................");
//                }
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//                // TODO Auto-generated method stub
//            }
//        };
//        //x5内核初始化接口
//        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}
