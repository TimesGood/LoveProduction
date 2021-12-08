package com.aige.loveproduction.mvp.ui.activity;

import android.Manifest;
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
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.adapter.WorkScanAdapter;
import com.aige.loveproduction.adapter.WrapRecyclerView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.PlanNoScanContract;
import com.aige.loveproduction.mvp.presenter.PlanNoScanPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.mvp.ui.customui.StatusLayout;
import com.aige.loveproduction.mvp.ui.dialogin.MessageDialog;
import com.aige.loveproduction.premission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PlanNoScanActivity extends BaseActivity<PlanNoScanPresenter,PlanNoScanContract.View>
        implements PlanNoScanContract.View,View.OnClickListener, StatusAction {
    private TextView find_edit;
    private ImageView image_camera,find_img;
    private WorkScanAdapter adapter;
    private WrapRecyclerView recyclerview_data;
    private String temporary_barcode = "";

    @Override
    protected PlanNoScanPresenter createPresenter() {
        return new PlanNoScanPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_plan_no_scan;
    }

    private void bindViews() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        recyclerview_data = findViewById(R.id.recyclerview_data);
    }

    @Override
    public void initView() {
        bindViews();
        find_edit.setHint("直接扫描、或输入批次号");
        image_camera.setOnClickListener(this);
        find_img.setOnClickListener(this);
        recyclerview_data.setOverScrollMode(View.OVER_SCROLL_NEVER);
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && v.getText() != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                    temporary_barcode = find_edit.getText().toString();
                    requestReady(temporary_barcode);
                }
                if(event == null) {
                    temporary_barcode = find_edit.getText().toString();
                    requestReady(temporary_barcode);
                }
                return true;
            }
        });
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
            permission.applyPermission(new String[]{Manifest.permission.CAMERA}, new Permission.ApplyListener() {
                @Override
                public void apply(String[] permission) {
                    requestPermissions(permission, 1);
                }

                @Override
                public void applySuccess() {
                    startActivityCapture();
                }
            });
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
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityCapture();
            } else {
                new MessageDialog.Builder(this)
                        .setTitle("温馨提醒")
                        .setMessage("权限拒绝后某些功能将不能使用，为了使用完整功能请打开"+permission.getPermissionHint(Arrays.asList(permissions)))
                        .setConfirm("去开启")
                        .setListener(dialog -> IntentUtils.gotoPermission(this))
                        .show();
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
            recyclerview_data.setAdapter(null);
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
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
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
        recyclerview_data.setVisibility(View.GONE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        recyclerview_data.setVisibility(View.VISIBLE);
        showComplete();
        mAnimation.alphaTran(recyclerview_data,300);
    }

    @Override
    public void onError(String message) {
        showEmpty();
        soundUtils.playSound(1,0);
        showToast(message);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}
