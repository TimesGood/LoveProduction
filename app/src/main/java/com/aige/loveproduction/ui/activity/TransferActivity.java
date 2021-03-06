package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.ui.adapter.WorkScanAdapter;
import com.aige.loveproduction.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.TransferContract;
import com.aige.loveproduction.mvp.presenter.TransferPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TransferActivity extends BaseActivity<TransferPresenter, TransferContract.View>
        implements TransferContract.View, View.OnClickListener, StatusAction {
    private TextView find_edit;
    private ImageView image_camera,find_img;
    private WorkScanAdapter adapter;
    private WrapRecyclerView recyclerview_data;

    private String temporary_barcode = "";

    @Override
    protected TransferPresenter createPresenter() {
        return new TransferPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void initView() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        recyclerview_data = findViewById(R.id.recyclerview_data);
    }

    @Override
    protected void initData() {
        setCenterTitle("????????????");
        find_edit.setHint("?????????????????????????????????");
        image_camera.setOnClickListener(this);
        find_img.setOnClickListener(this);
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,R.color.black));
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
    }

    private void requestReady(String input) {
        temporary_barcode = input;
        find_edit.setText("");
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("???????????????");
            soundUtils.playSound(1,0);
        }else{
            String opId = SharedPreferencesUtils.getValue(this, MachineSettings, "opId");
            if(opId.isEmpty()) {
                showToast("?????????????????????");
                soundUtils.playSound(1,0);
                return;
            }
            recyclerview_data.setAdapter(null);
            mPresenter.getWonoByPackageCode(input,opId,getAsk());
        }
    }


    @Override
    public void onGetWonoByPackageCodeSuccess(BaseBean<List<TransferBean>> bean) {
        if(bean.getCode() == 0) {
//            System.out.println("-----------------------");
//            System.out.println(bean);
        }else{
            recyclerview_data.setAdapter(null);
            soundUtils.playSound(1,0);
            showToast(bean.getMsg());
        }

    }

    @Override
    public void onGetMessageByWonoSuccess(List<ScanCodeBean> bean) {
        adapter = new WorkScanAdapter(this);
        adapter.setData(bean);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
        for(ScanCodeBean scanBean : bean) {
            if(!"????????????".equals(scanBean.getMessage())) {
                soundUtils.playSound(1,0);
                return;
            }
        }
        soundUtils.playSound(0,0);
    }

    @Override
    public void showLoading() {
        showLoadings();
        hideKeyboard(find_edit);
        recyclerview_data.setVisibility(View.GONE);
        recyclerview_data.setAdapter(null);
    }

    @Override
    public void hideLoading() {
        showComplete();
        recyclerview_data.setVisibility(View.VISIBLE);
        mAnimation.alphaTran(recyclerview_data,300);
    }

    @Override
    public void onError(String message) {
        showEmpty();
        showToast(message);
        soundUtils.playSound(1,0);
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
        }else if(id == R.id.find_img) {
            requestReady(find_edit.getText().toString());
        }

    }
    /**
     * ????????????????????????
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
                        .setTitle("????????????")
                        .setMessage("??????????????????????????????????????????????????????????????????????????????"+permission.getPermissionHint(Arrays.asList(permissions)))
                        .setConfirm("?????????")
                        .setListener(dialog -> IntentUtils.gotoPermission(this))
                        .show();
            }
        }
    }

    /**
     * ????????????????????????????????????
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
                //????????????
                showToast("?????????????????????");
            } else {
                requestReady(intentResult.getContents());
            }

        }
    }
    public WonoAsk getAsk() {
        String machineId = SharedPreferencesUtils.getValue(this, MachineSettings, "machineId");
        String opId = SharedPreferencesUtils.getValue(this, MachineSettings, "opId");
        String opType = SharedPreferencesUtils.getValue(this, MachineSettings, "opType");
        String workgroupId = SharedPreferencesUtils.getValue(this,WorkgroupSettings,"workgroupId");
        String employeeName = SharedPreferencesUtils.getValue(this, HandlerSettings, "employeeName");
        String username = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
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
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}