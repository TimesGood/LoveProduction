package com.aige.loveproduction.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.TransferVerifyContract;
import com.aige.loveproduction.mvp.presenter.TransferVerifyPresenter;
import com.aige.loveproduction.mvp.ui.customui.StatusLayout;
import com.aige.loveproduction.mvp.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.mvp.ui.dialogin.MessageDialog;
import com.aige.loveproduction.premission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

public class TransferVerifyActivity extends BaseActivity<TransferVerifyPresenter, TransferVerifyContract.View>
        implements TransferVerifyContract.View , StatusAction {

    private TextView find_edit;
    private WrapRecyclerView recyclerview_data;
    private GridLayout grid_item;
    private TransportAdapter adapter;
    private TextView orderId_text,plan_text,not_pack,not_transfer,status_text;
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
        find_edit = findViewById(R.id.find_edit);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.item_line)));
        orderId_text = findViewById(R.id.orderId_text);
        plan_text = findViewById(R.id.plan_text);
        not_pack = findViewById(R.id.not_pack);
        not_transfer = findViewById(R.id.not_transfer);
        grid_item = findViewById(R.id.grid_item);
        status_text = findViewById(R.id.status_text);
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
        showEmpty();
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

    private int count = 0;
    @Override
    public void onGetTransport(TransportBean bean) {
        if(bean == null) return;
        TransportBean.TransportBeans transportBeans = bean.getList().get(0);
        String packageCode = transportBeans.getPackageCode();
        if(packageCode.length() > 9) {
            orderId_text.setText(packageCode.substring(0,packageCode.length()-9));
        }else {
            orderId_text.setText(packageCode);
        }
        plan_text.setText(transportBeans.getType());
        not_pack.setText(String.valueOf(bean.getWeiBaoNumber()));

        if(confirmStatus(bean)) {
            status_text.setText("已提交");
        }else{
            status_text.setText("未提交");
        }
        not_transfer.setText(String.valueOf(bean.getList().size()-bean.getNotTransport()));
        adapter = new TransportAdapter(this);
        adapter.setType(1);
        adapter.setData(bean.getList());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
        LinearLayout footerView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.transfer_item, recyclerview_data, false);
        ((TextView)footerView.findViewById(R.id.packageDate)).setText("提交日期");
        recyclerview_data.addHeaderView(footerView);
        if(bean.getNotTransport() == 0) {
            showToast("包装已全部扫描，可转运");
            soundUtils.playSound(0,0);
        }else{
            showToast("有未扫描的包装，不可转运");
            soundUtils.playSound(1,0);
        }
    }
    private boolean confirmStatus(TransportBean bean) {
        boolean flag = false;
        count = 0;
        for (TransportBean.TransportBeans beans : bean.getList()) {
            if(!(beans.getConfirmDate() == null || "".equals(beans.getConfirmDate()))) {
                flag = true;
                count++;
            }
        }
        return flag;
    }
}