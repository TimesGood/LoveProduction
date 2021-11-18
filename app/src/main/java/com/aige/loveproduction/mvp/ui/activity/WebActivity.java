//package com.aige.loveproduction.mvp.ui.activity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.aige.loveproduction.R;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//
//
//public class WebActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private Toolbar toolbar_title;
//    private TextView toobar_text;
//    private String fileName="TBS测试.docx";
//    private String fileUrl="https://raw.githubusercontent.com/yangxch/Resources/master/test.docx";//远程文档地址
//    public static String FILE_DIR = "/sdcard/Downloads/test/";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web);
//        initView();
//        verifyStoragePermissions(this);
//    }
//    private  final int REQUEST_EXTERNAL_STORAGE = 1;
//    private  String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//    };
//    public  void verifyStoragePermissions(Activity activity) {
//        if (ContextCompat.checkSelfPermission(
//                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_GRANTED) {
//            // You can use the API that requires the permission.
//
//        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//        } else {
//            // You can directly ask for the permission.
//            ActivityCompat.requestPermissions(this,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        }
//    }
//    private void initView() {
//        Button browser_btn = findViewById(R.id.browser_btn);
//        Button file_selection_btn = findViewById(R.id.file_selection_btn);
//        toolbar_title = findViewById(R.id.toolbar_title);
//        toobar_text = findViewById(R.id.toolbar_text);
//
//        toolbar_title.setBackgroundColor(getColor(R.color.blue));
//        toolbar_title.setTitle("");
//        toolbar_title.setNavigationIcon(R.drawable.back);
//        toobar_text.setText("内置浏览器");
//        setSupportActionBar(toolbar_title);
//
//        browser_btn.setOnClickListener(this);
//        file_selection_btn.setOnClickListener(this);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == android.R.id.home) {
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //PermissionsDispatcherActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        Intent intent = null;
//        if (id == R.id.browser_btn) {
//            intent = new Intent(WebActivity.this, WebviewActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("urlKey", "https://cn.bing.com");
//            intent.putExtras(bundle);
//            startActivity(intent);
//        } else if (id == R.id.file_selection_btn) {
//
//            openFileWithTbs("/storage/emulated/0/TbsReaderTemp/A5521100005G01.xls");
//
//        }
//    }
//    //获取文件目录
//    @NonNull
//    private String getFilePath(String fileName) {
//        return new File(FILE_DIR + fileName).getAbsolutePath();
//    }
//
//    private void openFileWithTbs(String filePath) {
//        TBSFileViewActivity.viewFile(this, filePath);
//    }
//}