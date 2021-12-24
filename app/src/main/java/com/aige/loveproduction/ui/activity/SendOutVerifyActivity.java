package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.mvp.contract.ReportContract;
import com.aige.loveproduction.mvp.contract.SendOutContract;
import com.aige.loveproduction.ui.adapter.SendVerifyAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.mvp.contract.SendOutVerifyContract;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.ui.customui.viewgroup.DampNestedScrollView;
import com.aige.loveproduction.mvp.presenter.SendOutVerifyPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class SendOutVerifyActivity extends BaseActivity<SendOutVerifyPresenter,SendOutVerifyContract.View>
        implements SendOutVerifyContract.View,View.OnClickListener, StatusAction {
    private TextView
            find_edit,storage_bit_name_to,
            storage_bit_name,not_in_storage_to,not_in_storage,
            barcode;
    private ImageView image_camera,find_img;
    private WrapRecyclerView recyclerview_data;
    private SendVerifyAdapter adapter2;
    private LinearLayout recyclerview_title,storage_item;
    private DampNestedScrollView damp_scrollview;


    private String temporary_find_edit;
    @Override
    protected SendOutVerifyPresenter createPresenter() {
        return new SendOutVerifyPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_out_verify;
    }

    @Override
    protected void initView() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        not_in_storage_to = findViewById(R.id.not_in_storage_to);
        not_in_storage = findViewById(R.id.not_in_storage);
        storage_bit_name_to = findViewById(R.id.storage_bit_name_to);
        storage_bit_name = findViewById(R.id.storage_bit_name);
        barcode = findViewById(R.id.barcode);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        storage_item = findViewById(R.id.storage_item);
        damp_scrollview = findViewById(R.id.damp_scrollview);
    }

    @Override
    protected void initData() {
        setCenterTitle("发货验证");
        find_edit.setHint("直接扫描或输入条码");
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.item_line)));
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && v.getText() != null && event.getAction() == KeyEvent.ACTION_UP) {
                    requestReady(find_edit.getText().toString());
                }
                if(event == null) {
                    requestReady(find_edit.getText().toString());
                }
                return true;
            }
        });
        setOnClickListener(find_img,image_camera);
        storage_item.setVisibility(View.GONE);
        recyclerview_title.setVisibility(View.GONE);
        not_in_storage_to.setVisibility(View.GONE);
        not_in_storage.setVisibility(View.GONE);
        storage_bit_name_to.setVisibility(View.GONE);
        storage_bit_name.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_camera) {
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

    private void requestReady(String input) {
        temporary_find_edit = input;
        find_edit.setText("");
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else{
            String userName = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
            mPresenter.getSendOutVerify(input,userName,"","");
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
                requestReady(intentResult.getContents());
            }

        }
    }

    @Override
    public void onGetSendOutVerifySuccess(BaseBean<List<StorageBean>> bean) {
        if(bean.getCode() == 0) {
            List<StorageBean> data = bean.getData();
            data.forEach(d -> d.setType("发货验证"));
            //adapter = new StorageAdapter(this,data);
            adapter2 = new SendVerifyAdapter(this);
            adapter2.setData(data);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerview_data.setLayoutManager(manager);
            recyclerview_data.setAdapter(adapter2);
            barcode.setText(temporary_find_edit);
            boolean flag = true;
            for(StorageBean storageBean : data) {
                if(storageBean.getNotOutPackage() > 0) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                soundUtils.playSound(0,0);
            }else{
                soundUtils.playSound(1,0);
            }
        }else{
            showEmpty();
            storage_item.setVisibility(View.GONE);
            damp_scrollview.setVisibility(View.GONE);
            recyclerview_title.setVisibility(View.GONE);
            recyclerview_data.setAdapter(null);
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        }
    }

    @Override
    public void showLoading() {
        storage_item.setVisibility(View.GONE);
        damp_scrollview.setVisibility(View.GONE);
        recyclerview_title.setVisibility(View.GONE);
        //loading_layout.setVisibility(View.VISIBLE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        storage_item.setVisibility(View.VISIBLE);
        damp_scrollview.setVisibility(View.VISIBLE);
        recyclerview_title.setVisibility(View.VISIBLE);
        //loading_layout.setVisibility(View.GONE);
        showComplete();
        ObjectAnimator animator = ObjectAnimator.ofFloat(damp_scrollview,"scaleX",0,1f);
        animator.setDuration(200);
        animator.start();

    }

    @Override
    public void onError(String message) {
        showEmpty();
        soundUtils.playSound(1,0);
        recyclerview_data.setAdapter(null);
        showToast(message);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}