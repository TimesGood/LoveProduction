//package com.aige.loveproduction.ui.activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.aige.loveproduction.R;
//import com.aige.loveproduction.base.BaseActivity;
//import com.aige.loveproduction.base.BaseDialog;
//import com.aige.loveproduction.bean.DownloadBean;
//import com.aige.loveproduction.mvp.contract.MprContract;
//import com.aige.loveproduction.mvp.presenter.MprPresenter;
//import com.aige.loveproduction.mpr.MprDataWrap;
//import com.aige.loveproduction.ui.customui.view.DrawMprView;
//import com.aige.loveproduction.ui.dialogin.LoadingDialog;
//import com.aige.loveproduction.ui.dialogin.MessageDialog;
//import com.aige.loveproduction.permission.Permission;
//import com.aige.loveproduction.util.IOUtil;
//import com.aige.loveproduction.util.FileViewerUtils;
//import com.aige.loveproduction.util.IntentUtils;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.ResponseBody;
//
//public class ApplyActivity extends BaseActivity<MprPresenter, MprContract.View> implements MprContract.View {
//    private FrameLayout main_body;
//    private DrawMprView apply_view;
//    private EditText find_edit;
//    private LoadingDialog.Builder dialog;
//    //打开文件
//    private File mFile;
//    private final String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
//    @Override
//    protected MprPresenter createPresenter() {
//        return new MprPresenter();
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_apply;
//    }
//
//    @Override
//    protected void initView() {
//        main_body = findViewById(R.id.main_body);
//        apply_view = findViewById(R.id.apply_view);
//        find_edit = findViewById(R.id.find_edit);
//    }
//
//    @Override
//    protected void initData() {
//        setOnClickListener(R.id.image_camera,R.id.find_img);
//        find_edit.requestFocus();
//        find_edit.setOnEditorActionListener((v, actionId, event) -> {
//            if (event != null && v.getText() != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                requestReady(v.getText().toString());
//            }
//            if(event == null) {
//                requestReady(v.getText().toString());
//            }
//            return true;
//        });
//    }
//
//    @Override
//    public void showLoading() {
//        dialog = new LoadingDialog.Builder(this);
//        dialog.setCanceledOnTouchOutside(false)
//                .setTitle("加载中...")
//                .setBackgroundDimEnabled(false)
//                .setCancel("")
//                .setConfirm("取消")
//                .setListener(new LoadingDialog.OnListener() {
//                    @Override
//                    public void onConfirm(BaseDialog dialog) {
//                        mPresenter.dispose();
//                    }
//                })
//                .show();
//    }
//
//
//    @Override
//    public void hideLoading() {
//        dialog.dismiss();
//    }
//
//    @Override
//    public void onError(String message) {
//        dialog.dismiss();
//        showToast(message);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if(id == R.id.image_camera){
//            permission.applyPermission(PERMISSIONS_STORAGE, new Permission.ApplyListener() {
//                @Override
//                public void apply(String[] permission) {
//                    requestPermissions(permission, 1);
//                }
//
//                @Override
//                public void applySuccess() {
//                    startActivityCapture();
//                }
//            });
//        }else if(id == R.id.find_img) {
//            String input = find_edit.getText().toString();
//            requestReady(input);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 1) {
//            List<String> list = new ArrayList<>();
//            for(int i = 0; i < grantResults.length; i++) {
//                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    list.add(PERMISSIONS_STORAGE[i]);
//                }
//            }
//            if(list.size() != 0) {
//                new MessageDialog.Builder(this)
//                        .setTitle("温馨提醒")
//                        .setMessage("权限拒绝后某些功能将不能使用，为了使用完整功能请打开"+permission.getPermissionHint(list))
//                        .setConfirm("去开启")
//                        .setListener(dialog -> IntentUtils.gotoPermission(this))
//                        .show();
//            }else{
//                startActivityCapture();
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (intentResult != null) {
//            if (intentResult.getContents() == null) {
//                //扫码失败
//                showToast("解析二维码失败");
//            } else {
//                String result = intentResult.getContents();//返回值
//                requestReady(result);
//            }
//
//        }
//
//    }
//    //请求前的操作
//    private void requestReady(String input) {
//        find_edit.setText("");
//        hideKeyboard(find_edit);
//        //每一次搜索请求清空之前下载的文件
//        FileViewerUtils.deleteDir(new File(getExternalCacheDir() + "/mprFile"));
//        if (input.isEmpty()) {
//            showToast("请输入批次号");
//        } else {
//            mPresenter.getMPRByBatchNoV2(input);
//        }
//    }
//
//
//    @Override
//    public void onGetMPRByBatchNoSuccess(List<DownloadBean> beans) {
////        DownloadBean downloadBean = beans.get(0);
////        mFile = new File(getExternalCacheDir() + "/mprFile/"+downloadBean.getFileName());
//    }
//
//    @Override
//    public void onGetFileSuccess(ResponseBody body) {
////        FileViewerUtils.createOrExistsDir(FileViewerUtils.getFilePath(mFile));
////        //下载监听
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                IOUtil.writeResponseBody(body, mFile, new OnWriteFileListener() {
////                    @Override
////                    public void onSuccess(String path) {
////                        runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                parseFile(mFile);
////                            }
////                        });
////                    }
////
////                    @Override
////                    public void onFail(Throwable e) {
////                        showToast("文件下载过程异常");
////                    }
////                });
////            }
////        }).start();
//    }
//
//    private void parseFile(File file) {
////        Map<String, List<Map<String, Float>>> data = IOUtil.readMprFile(file);
////        if(data == null) {
////            showToast("文件解析错误");
////            return;
////        }
////        apply_view.setData(data);
////        apply_view.setRectangle_color(R.color.draw_sky_blue);
////        apply_view.setCutting_color(R.color.draw_gray);
////        apply_view.setBohrVert_color(R.color.draw_gray);
//    }
//
//    @Override
//    public void onGetMPRByBatchNoV2Success(List<String> beans) {
//        if(beans == null || "[]".equals(beans.toString())) {
//            showToast("无板件数据");
//            return;
//        }
//        Map<String, List<Map<String, Float>>> stringListMap = IOUtil.readMpr(beans);
//        apply_view.setData(stringListMap);
//        apply_view.setShowXY(true);
//    }
//
//
//}