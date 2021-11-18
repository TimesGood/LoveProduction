package com.aige.loveproduction.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.adapter.TransportAdapter;
import com.aige.loveproduction.adapter.WrapRecyclerView;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseAnimation;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.TransferVerifyContract;
import com.aige.loveproduction.mvp.presenter.TransferVerifyPresenter;
import com.aige.loveproduction.mvp.ui.customui.StatusLayout;
import com.aige.loveproduction.mvp.ui.dialogin.MessageDialog;
import com.aige.loveproduction.premission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TransferVerifyActivity extends BaseActivity<TransferVerifyPresenter, TransferVerifyContract.View>
        implements TransferVerifyContract.View , StatusAction {

    private TextView find_edit;
    private RelativeLayout loading_layout;
    private WrapRecyclerView recyclerview_data;
    private GridLayout grid_item;
    private TransportAdapter adapter;
    private TextView orderId_text,plan_text,not_pack,not_transfer;
    @Override
    protected TransferVerifyPresenter createPresenter() {
        return new TransferVerifyPresenter();
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("转运验证");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transfer_verify;
    }

    @Override
    public void initView() {
        setOnClickListener(R.id.image_camera,R.id.find_img);
        loading_layout = findViewById(R.id.loading_layout);
        find_edit = findViewById(R.id.find_edit);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        orderId_text = findViewById(R.id.orderId_text);
        plan_text = findViewById(R.id.plan_text);
        not_pack = findViewById(R.id.not_pack);
        not_transfer = findViewById(R.id.not_transfer);
        grid_item = findViewById(R.id.grid_item);

        find_edit.setHint("直接扫描、或输入包装码");
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener((v, actionId, event) -> {
            if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && v.getText() != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                requestReady(find_edit.getText().toString());
            }
            if(event == null) {
                requestReady(find_edit.getText().toString());
            }
            return true;

        });
        grid_item.setVisibility(View.GONE);
    }


    @Override
    public void showLoading() {
        grid_item.setVisibility(View.GONE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        grid_item.setVisibility(View.VISIBLE);
        showComplete();
        mAnimation.alphaTran(recyclerview_data,300);
    }

    @Override
    public void onError(String message) {
        soundUtils.playSound(1,0);
        showToast(message);
        recyclerview_data.setAdapter(null);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivityCapture();
            }else{
                new MessageDialog.Builder(this)
                        .setTitle("开启权限")
                        .setMessage("权限未开启，请手动授予相机权限")
                        .setConfirm("去开启")
                        .setListener(dialog -> IntentUtils.gotoPermission(this))
                        .show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_camera) {
            permission.applyPermission( new String[]{Manifest.permission.CAMERA}, new Permission.ApplyListener() {
                @Override
                public void apply(String[] permission) {
                    requestPermissions(permission,1);
                }

                @Override
                public void applySuccess() {
                    startActivityCapture();
                }
            });
        }else if(id == R.id.find_img) {
            requestReady(find_edit.getText().toString());
        }
    }
    private void requestReady(String input) {
        find_edit.setText("");
        if(input.isEmpty()) {
            soundUtils.playSound(1,0);
            showToast("请输入包装码");
            return;
        }else if(input.length() < 17) {
            soundUtils.playSound(1,0);
            showToast("请输入正确的包装码");
            return;
        }
        mPresenter.getTransportVerification(input);
    }
    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }

    @Override
    public void onGetTransport(TransportBean bean) {
        if(bean == null) return;
        TransportBean.TransportBeans transportBeans = bean.getList().get(0);
        orderId_text.setText(transportBeans.getPackageCode().substring(0,13));
        plan_text.setText(transportBeans.getType());
        not_pack.setText(bean.getWeiBaoNumber());
        not_transfer.setText(bean.getNotTransport());
        adapter = new TransportAdapter(this);
        adapter.setData(bean.getList());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
        recyclerview_data.addHeaderView(R.layout.transfer_item);
        if(bean.getNotTransport().equals("0")) {
            showToast("包装已全部扫描，可转运");
            soundUtils.playSound(0,0);
        }else{
            showToast("有未扫描的包装，不可转运");
            soundUtils.playSound(1,0);
        }

    }
}