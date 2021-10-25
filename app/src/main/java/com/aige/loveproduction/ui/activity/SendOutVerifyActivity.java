package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.SendVerifyAdapter;
import com.aige.loveproduction.adapter.StorageAdapter;
import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.contract.SendOutVerifyContract;
import com.aige.loveproduction.customui.RecycleViewDivider;
import com.aige.loveproduction.presenter.SendOutVerifyPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SendOutVerifyActivity extends BaseActivity<SendOutVerifyPresenter,SendOutVerifyContract.View> implements SendOutVerifyContract.View,View.OnClickListener{
    private RelativeLayout title_bar,tv_back;
    private TextView
            find_edit,storage_bit_name_to,
            storage_bit_name,not_in_storage_to,not_in_storage,
            barcode;
    private ImageView image_camera,find_img;
    private RecyclerView recyclerview_data;
    private StorageAdapter adapter;
    private SendVerifyAdapter adapter2;

    private RelativeLayout loading_layout;
    private LinearLayout linear_body,recyclerview_title,storage_item;


    private String temporary_find_edit;
    @Override
    protected SendOutVerifyPresenter createPresenter() {
        return new SendOutVerifyPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_out_verify;
    }

    @Override
    public void initView() {
        bindViews();
        find_edit.setHint("直接扫描或输入条码");
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.grey)));
        recyclerview_data.setOverScrollMode(View.OVER_SCROLL_NEVER);
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && v.getText() != null && event.getAction() == KeyEvent.ACTION_UP) {
                    temporary_find_edit = find_edit.getText().toString();
                    requestReady(temporary_find_edit);
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
        ZXingLibrary.initDisplayOpinion(this);
    }

    private void bindViews() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        loading_layout = findViewById(R.id.loading_layout);
        linear_body = findViewById(R.id.linear_body);
        not_in_storage_to = findViewById(R.id.not_in_storage_to);
        not_in_storage = findViewById(R.id.not_in_storage);
        storage_bit_name_to = findViewById(R.id.storage_bit_name_to);
        storage_bit_name = findViewById(R.id.storage_bit_name);
        barcode = findViewById(R.id.barcode);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        storage_item = findViewById(R.id.storage_item);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("发货验证");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.image_camera:
                //Android系统6.0之上需要动态获取权限
                if (Build.VERSION.SDK_INT > 22) {
                    if (ContextCompat.checkSelfPermission(SendOutVerifyActivity.this,
                            android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //判断有没有权限 ，没有就在这里进行权限的申请
                        ActivityCompat.requestPermissions(SendOutVerifyActivity.this,
                                new String[]{android.Manifest.permission.CAMERA}, 1);
                    } else {
                        Intent intent = new Intent(SendOutVerifyActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    //系统版本在6.0之下，不需要动态获取权限。
                    Intent intent = new Intent(SendOutVerifyActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.find_img:
                temporary_find_edit = find_edit.getText().toString();
                requestReady(temporary_find_edit);
                break;
        }
    }

    private void requestReady(String input) {
        find_edit.setText("");
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else{
            String userName = SharedPreferencesUtils.getValue(this, "loginInfo", "userName");
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
                    temporary_find_edit = bundle.getString(CodeUtils.RESULT_STRING);
                    requestReady(temporary_find_edit);
                }
            }
        }
    }

    @Override
    public void onGetSendOutVerifySuccess(BaseBean<List<StorageBean>> bean) {
        if(bean.getCode() == 0) {
            List<StorageBean> data = bean.getData();
            for(StorageBean storageBean : data) {
                storageBean.setType("发货验证");
            }
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
            storage_item.setVisibility(View.GONE);
            linear_body.setVisibility(View.GONE);
            recyclerview_title.setVisibility(View.GONE);
            recyclerview_data.setAdapter(null);
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        }
    }

    @Override
    public void showLoading() {
        storage_item.setVisibility(View.GONE);
        linear_body.setVisibility(View.GONE);
        recyclerview_title.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        storage_item.setVisibility(View.VISIBLE);
        linear_body.setVisibility(View.VISIBLE);
        recyclerview_title.setVisibility(View.VISIBLE);
        loading_layout.setVisibility(View.GONE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(linear_body,"scaleX",0,1f);
        animator.setDuration(200);
        animator.start();

    }

    @Override
    public void onError(String message) {
        soundUtils.playSound(1,0);
        recyclerview_data.setAdapter(null);
        showToast(message);
    }

}