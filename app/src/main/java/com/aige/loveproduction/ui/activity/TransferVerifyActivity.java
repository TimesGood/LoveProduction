package com.aige.loveproduction.ui.activity;

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
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.ui.adapter.TransportAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.TransferVerifyContract;
import com.aige.loveproduction.mvp.presenter.TransferVerifyPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.permission.Permission;
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
    protected int getLayoutId() {
        return R.layout.activity_transfer_verify;
    }

    @Override
    protected void initView() {
        find_edit = findViewById(R.id.find_edit);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.item_line)));
        orderId_text = findViewById(R.id.orderId_text);
        plan_text = findViewById(R.id.plan_text);
        not_pack = findViewById(R.id.not_pack);
        not_transfer = findViewById(R.id.not_transfer);
        grid_item = findViewById(R.id.grid_item);
        status_text = findViewById(R.id.status_text);
    }

    @Override
    protected void initData() {
        setCenterTitle("????????????");
        setOnClickListener(R.id.image_camera,R.id.find_img);
        find_edit.setHint("?????????????????????????????????");
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
        showLoadings();
        hideKeyboard(find_edit);
        recyclerview_data.setVisibility(View.GONE);
        recyclerview_data.setAdapter(null);
        grid_item.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        showComplete();
        recyclerview_data.setVisibility(View.VISIBLE);
        grid_item.setVisibility(View.VISIBLE);
        mAnimation.alphaTran(recyclerview_data,300);
    }

    @Override
    public void onError(String message) {
        showToast(message);
        showEmpty();
        soundUtils.playSound(1,0);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivityCapture();
            }else{
                new MessageDialog.Builder(this)
                        .setTitle("????????????")
                        .setMessage("?????????????????????????????????????????????")
                        .setConfirm("?????????")
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
                //????????????
                showToast("?????????????????????");
            } else {
                String result = intentResult.getContents();//?????????
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
            showToast("??????????????????");
            return;
        }else if(input.length() < 17) {
            soundUtils.playSound(1,0);
            showToast("???????????????????????????");
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
        String packageCode = transportBeans.getPackageCode();
        if(packageCode.length() > 9) {
            orderId_text.setText(packageCode.substring(0,packageCode.length()-9));
        }else {
            orderId_text.setText(packageCode);
        }
        plan_text.setText(transportBeans.getType());
        not_pack.setText(String.valueOf(bean.getWeiBaoNumber()));

        if(confirmStatus(bean)) {
            status_text.setText("?????????");
        }else{
            status_text.setText("?????????");
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
        ((TextView)footerView.findViewById(R.id.packageDate)).setText("????????????");
        recyclerview_data.addHeaderView(footerView);
        if(bean.getNotTransport() == 0) {
            showToast("?????????????????????????????????");
            soundUtils.playSound(0,0);
        }else{
            showToast("????????????????????????????????????");
            soundUtils.playSound(1,0);
        }
    }
    private boolean confirmStatus(TransportBean bean) {
        boolean flag = false;
        for (TransportBean.TransportBeans beans : bean.getList()) {
            if(!(beans.getConfirmDate() == null || "".equals(beans.getConfirmDate()))) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}