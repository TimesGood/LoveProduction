package com.aige.loveproduction.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.aige.loveproduction.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * 自定义二维码扫描界面
 */
public class QrCodeActivity extends AppCompatActivity implements  DecoratedBarcodeView.TorchListener {
 
    private DecoratedBarcodeView decoratedBarcodeView;
    private CaptureManager captureManager;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
 
        decoratedBarcodeView=this.findViewById(R.id.decoratedBarcodeView);
        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, decoratedBarcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
        decoratedBarcodeView.setTorchListener(this);
    }
 
    @Override
    public void onTorchOn() {
 
    }
 
    @Override
    public void onTorchOff() {
 
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    //手电筒开关
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return decoratedBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}