//package com.aige.loveproduction.mvp.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.WindowManager;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.aige.loveproduction.R;
//import com.uuzuche.lib_zxing.activity.CaptureFragment;
//import com.uuzuche.lib_zxing.activity.CodeUtils;
//
//public class CustomCameraActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED, WindowManager.LayoutParams.ALPHA_CHANGED);
//        setContentView(R.layout.activity_camera);//
//        CaptureFragment captureFragment = new CaptureFragment();
//        CodeUtils.setFragmentArgs(captureFragment,R.layout.camera);  //设置自定义扫码界面
//        captureFragment.setAnalyzeCallback(analyzeCallback);
//        //R.id.fl_zxing_container  对应   setContentView 布局中的  Fragment
//        getSupportFragmentManager().beginTransaction().replace(R.id.myCamera, captureFragment).commit();  // 替换setContenView设置的布局中的  ID为myCamera
//
//    }
//    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
//        @Override
//        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
//            resultIntent.putExtras(bundle);
//            CustomCameraActivity.this.setResult(RESULT_OK, resultIntent);
//            CustomCameraActivity.this.finish();
//        }
//
//        @Override
//        public void onAnalyzeFailed() {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
//            bundle.putString(CodeUtils.RESULT_STRING, "");
//            resultIntent.putExtras(bundle);
//            CustomCameraActivity.this.setResult(RESULT_OK, resultIntent);
//            CustomCameraActivity.this.finish();
//        }
//    };
//}