//package com.aige.loveproduction.mvp.ui.webview;
//
//import android.os.Bundle;
//import android.os.Environment;
//import android.text.TextUtils;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.aige.loveproduction.R;
//
//import com.tencent.smtt.sdk.TbsReaderView;
//
//import java.io.File;
//
//
//public class FileDisplayActivity extends AppCompatActivity {
//
//    SuperFileView mSuperFileView;
//    String filePathUrl;
//    private TextView tv_progress;
//    private FrameLayout frame_layout;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_filedisplay);
//        //QbSdk.initX5Environment(this,null);
//        filePathUrl = getIntent().getStringExtra("filePath");
//        frame_layout = findViewById(R.id.frame_layout);
//        openFile(filePathUrl);
//
//
//    }
//    private void openFile(String path) {
//        //通过bundle把文件传给x5,打开的事情交由x5处理
//        Bundle bundle = new Bundle();
//        //传递文件路径
//        bundle.putString("filePath", path);
//        //临时的路径
//        bundle.putString("tempPath", Environment.getExternalStorageDirectory() + File.separator + "temp");
//        TbsReaderView readerView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
//            @Override
//            public void onCallBackAction(Integer integer, Object o, Object o1) {
//
//            }
//        });
//        //加载文件前的初始化工作,加载支持不同格式的插件
//        boolean b = readerView.preOpen(getFileType(path), false);
//        if (b) {
//            readerView.openFile(bundle);
//        }
//        // 往容器里添加TbsReaderView控件
//        frame_layout.addView(readerView);
//    }
//    private String getFileType(String path) {
//        String str = "";
//
//        if (TextUtils.isEmpty(path)) {
//            return str;
//        }
//        int i = path.lastIndexOf('.');
//        if (i <= -1) {
//            return str;
//        }
//        str = path.substring(i + 1);
//        return str;
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // 一定要调用此方法，才能选择下一个文件预览
//        // 否则显示loading而不展示
//        // 适当的位置调用此方法
//        //readerView.onStop();
//    }
//}
