package com.aige.loveproduction.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseDialog;
import com.aige.loveproduction.bean.DownloadBean;
import com.aige.loveproduction.mpr.MprColor;
import com.aige.loveproduction.mvp.contract.MprContract;
import com.aige.loveproduction.mvp.presenter.MprPresenter;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.mpr.MprView;
import com.aige.loveproduction.mpr.MprDataWrap;
import com.aige.loveproduction.ui.adapter.MprSettingAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.ui.dialogin.LoadingDialog;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.util.CommonUtils;
import com.aige.loveproduction.util.FileViewerUtils;
import com.aige.loveproduction.util.IOUtil;
import com.aige.loveproduction.util.IntentUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class MprActivity extends BaseActivity<MprPresenter, MprContract.View> implements MprContract.View{
    private MprView apply_view;
    private EditText find_edit;
    private FrameLayout right_drawer;
    private DrawerLayout mpr_drawer;
    private LoadingDialog.Builder dialog;
    private WrapRecyclerView recyclerview_data;
    private final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected MprPresenter createPresenter() {
        return new MprPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mpr;
    }

    @Override
    protected void initView() {
        apply_view = findViewById(R.id.apply_view);
        right_drawer = findViewById(R.id.right_drawer);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        mpr_drawer = findViewById(R.id.mpr_drawer);
    }
    //沉浸式体验
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //判断是否有焦点
        if(hasFocus){
            //获取当前界面的装饰视图
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN//全屏
                            //让应用主体占用状态栏空间，这两个必须绑定一起使用
                            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            //让应用主体占中底部导航栏空间
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            //隐藏导航栏
                            |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //当在顶部下拉时，显示状态栏
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            //设置状态栏透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //设置底部导航透明
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }
    @Override
    protected void initData() {
        setData();
        setOnClickListener(R.id.image_camera);
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && v.getText() != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                requestReady(v.getText().toString());
            }
            if(event == null) {
                requestReady(v.getText().toString());
            }
            return true;
        });
        ViewGroup.LayoutParams layoutParams = right_drawer.getLayoutParams();
        layoutParams.width = CommonUtils.getScreenWidth(this)/3;

    }
    private void setData(){
        MprSettingAdapter adapter = new MprSettingAdapter(this);
        adapter.setMprView(apply_view);
        recyclerview_data.setAdapter(adapter);
        LinearLayout inflate = (LinearLayout)getLayoutInflater().inflate(R.layout.find_include, recyclerview_data,false);
        inflate.getChildAt(0).setVisibility(View.GONE);
        find_edit = (EditText) inflate.getChildAt(1);
        setOnClickListener(inflate.getChildAt(2));
        recyclerview_data.addHeaderView(inflate);
    }

    @Override
    public void showLoading() {
        dialog = new LoadingDialog.Builder(this);
        dialog.setCanceledOnTouchOutside(false)
                .setTitle("加载中...")
                .setBackgroundDimEnabled(false)
                .setCancel("")
                .setConfirm("取消")
                .setListener(new LoadingDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        mPresenter.dispose();
                    }
                })
                .show();
    }


    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    @Override
    public void onError(String message) {
        dialog.dismiss();
        showToast(message);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_camera){
            permission.applyPermission(PERMISSIONS_STORAGE, new Permission.ApplyListener() {
                @Override
                public void apply(String[] permission) {
                    requestPermissions(permission, 1);
                }

                @Override
                public void applySuccess() {
                    startActivityCapture();
                }
            });
        }else if(id == R.id.find_img) {
            String input = find_edit.getText().toString();
            requestReady(input);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            List<String> list = new ArrayList<>();
            for(int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    list.add(PERMISSIONS_STORAGE[i]);
                }
            }
            if(list.size() != 0) {
                new MessageDialog.Builder(this)
                        .setTitle("温馨提醒")
                        .setMessage("权限拒绝后某些功能将不能使用，为了使用完整功能请打开"+permission.getPermissionHint(list))
                        .setConfirm("去开启")
                        .setListener(dialog -> IntentUtils.gotoPermission(this))
                        .show();
            }else{
                startActivityCapture();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //扫码失败
                showToast("解析二维码失败");
            } else {
                String result = intentResult.getContents();//返回值
                requestReady(result);
            }

        }

    }
    //请求前的操作
    private void requestReady(String input) {
        mpr_drawer.closeDrawers();
        find_edit.setText("");
        hideKeyboard(find_edit);
        //每一次搜索请求清空之前下载的文件
        FileViewerUtils.deleteDir(new File(getExternalCacheDir() + "/mprFile"));
        if (input.isEmpty()) {
            showToast("请输入批次号");
        } else {
            mPresenter.getMPRByBatchNoV2(input);
        }
    }


    @Override
    public void onGetMPRByBatchNoSuccess(List<DownloadBean> beans) {
//        DownloadBean downloadBean = beans.get(0);
//        mFile = new File(getExternalCacheDir() + "/mprFile/"+downloadBean.getFileName());
    }

    @Override
    public void onGetFileSuccess(ResponseBody body) {
//        FileViewerUtils.createOrExistsDir(FileViewerUtils.getFilePath(mFile));
//        //下载监听
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IOUtil.writeResponseBody(body, mFile, new OnWriteFileListener() {
//                    @Override
//                    public void onSuccess(String path) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                parseFile(mFile);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFail(Throwable e) {
//                        showToast("文件下载过程异常");
//                    }
//                });
//            }
//        }).start();
    }

    private void parseFile(File file) {
//        Map<String, List<Map<String, Float>>> data = IOUtil.readMprFile(file);
//        if(data == null) {
//            showToast("文件解析错误");
//            return;
//        }
//        apply_view.setData(data);
//        apply_view.setRectangle_color(R.color.draw_sky_blue);
//        apply_view.setCutting_color(R.color.draw_gray);
//        apply_view.setBohrVert_color(R.color.draw_gray);
    }

    @Override
    public void onGetMPRByBatchNoV2Success(List<String> beans) {
        if(beans == null || "[]".equals(beans.toString())) {
            showToast("无板件数据");
            return;
        }

        MprDataWrap wrap = IOUtil.readMpr(beans);
        if(wrap == null) {
            showToast("数据解析异常");
            return;
        }
        apply_view.setData(wrap);
        MprColor mprColor = apply_view.getMprColor();
        mprColor.setBohrHoriz_distance_line(getColor(R.color.draw_sky_blue));
    }

}