package com.aige.loveproduction.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.WorkScanAdapter;
import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.contract.PlanNoScanContract;
import com.aige.loveproduction.customui.viewgroup.SuperSwipeRefreshLayout;
import com.aige.loveproduction.presenter.PlanNoScanPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlanNoScanActivity extends BaseActivity<PlanNoScanPresenter,PlanNoScanContract.View> implements PlanNoScanContract.View,View.OnClickListener{
    private TextView find_edit;
    private ImageView image_camera,find_img;
    private WorkScanAdapter adapter;
    private RecyclerView plan_no_recyclerview;

    private SuperSwipeRefreshLayout super_swipe;

    private RelativeLayout loading_layout;

    private String temporary_barcode = "";

    @Override
    protected PlanNoScanPresenter createPresenter() {
        return new PlanNoScanPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_plan_no_scan;
    }

    @Override
    public void initView() {
        bindViews();
        find_edit.setHint("直接扫描、或输入批次号");
        image_camera.setOnClickListener(this);
        find_img.setOnClickListener(this);
        ZXingLibrary.initDisplayOpinion(this);
        plan_no_recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener((v, actionId, event) -> {
            if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && v.getText() != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                temporary_barcode = find_edit.getText().toString();
                requestReady(temporary_barcode);
            }
            return true;

        });
        super_swipe.setTargetScrollWithLayout(false);
        super_swipe.setEnabled(false);
        super_swipe.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                requestReady(temporary_barcode);
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });
    }

    private void bindViews() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        plan_no_recyclerview = findViewById(R.id.plan_no_recyclerview);
        loading_layout = findViewById(R.id.loading_layout);
        super_swipe = findViewById(R.id.super_swipe);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("批次扫描");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_camera){
            //Android系统6.0之上需要动态获取权限
            if (Build.VERSION.SDK_INT > 22) {
                if (ContextCompat.checkSelfPermission(PlanNoScanActivity.this,
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(PlanNoScanActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, 1);
                } else {
                    Intent intent = new Intent(PlanNoScanActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                }
            } else {
                //系统版本在6.0之下，不需要动态获取权限。
                Intent intent = new Intent(PlanNoScanActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
        }else if(id == R.id.find_img){
            temporary_barcode = find_edit.getText().toString();
            requestReady(temporary_barcode);
        }
    }

    /**
     * 处理权限申请回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            } else {
                soundUtils.playSound(1,0);
                showToast("请打开摄像机权限");
            }
        }
    }

    /**
     * 处理界面跳转回传数据回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    //获取到扫描的结果
                    temporary_barcode = bundle.getString(CodeUtils.RESULT_STRING);
                    requestReady(temporary_barcode);
                }
            }
        }
    }
    private void requestReady(String input) {
        find_edit.setText("");
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else{
            String opId = SharedPreferencesUtils.getValue(this, "machineSettings", "opId");
            if(opId.isEmpty()) {
                showToast("未设置工厂设置");
                soundUtils.playSound(1,0);
                return;
            }

            mPresenter.getWonoByBatchNo(input,opId,getAsk());
        }
    }
    @Override
    public void onGetWonoByBatchNoSuccess(BaseBean<List<TransferBean>> bean) {

        if(bean.getCode() == 0) {
//            System.out.println("-----------------------");
//            System.out.println(bean);
        }else{
            plan_no_recyclerview.setAdapter(null);
            soundUtils.playSound(1,0);
            showToast(bean.getMsg());
        }

    }
    public WonoAsk getAsk() {
        String machineId = SharedPreferencesUtils.getValue(this, "machineSettings", "machineId");
        String opId = SharedPreferencesUtils.getValue(this, "machineSettings", "opId");
        String opType = SharedPreferencesUtils.getValue(this, "machineSettings", "opType");
        String workgroupId = SharedPreferencesUtils.getValue(this,"workgroupSettings","workgroupId");
        String employeeName = SharedPreferencesUtils.getValue(this, "handlerSettings", "employeeName");
        String username = SharedPreferencesUtils.getValue(this, "loginInfo", "userName");
        WonoAsk ask = new WonoAsk();
        ask.setUserName(username);
        ask.setEmployeeId(employeeName);
        ask.setMachineId(machineId);
        ask.setWorkGroupId(workgroupId);
        ask.setOperationId(opId);
        ask.setOperationType(opType);
        return ask;
    }
    @Override
    public void onGetMessageByWonoSuccess(BaseBean<List<ScanCodeBean>> bean) {
        List<ScanCodeBean> data = bean.getData();
        adapter = new WorkScanAdapter(this,data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        plan_no_recyclerview.setLayoutManager(manager);
        plan_no_recyclerview.setAdapter(adapter);
        for(ScanCodeBean scanBean : data) {
            if(!"扫描成功".equals(scanBean.getMessage())) {
                soundUtils.playSound(1,0);
                return;
            }
        }
        soundUtils.playSound(0,0);
    }

    @Override
    public void showLoading() {
        super_swipe.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        loading_layout.setVisibility(View.GONE);
        super_swipe.setRefreshing(false);
        super_swipe.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String message) {
        soundUtils.playSound(1,0);
        showToast(message);
    }
}
