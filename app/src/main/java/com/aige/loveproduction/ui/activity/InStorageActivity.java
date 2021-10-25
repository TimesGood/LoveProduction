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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.StorageAdapter;
import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.contract.InStorageContract;
import com.aige.loveproduction.dialogin.MessageDialog;
import com.aige.loveproduction.presenter.InStoragePresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InStorageActivity extends BaseActivity<InStoragePresenter,InStorageContract.View> implements InStorageContract.View {
    private TextView storage_bit_name,barcode,not_in_storage;
    private EditText find_edit;
    private ImageView image_camera,find_img;
    private StorageAdapter adapter;
    private RecyclerView recyclerview_data;
    private LinearLayout linear_body,recyclerview_title,storage_item;
    private RelativeLayout loading_layout;

    private String hide_salesOrder = "";
    private String temporary_find_edit = "";


    @Override
    protected InStoragePresenter createPresenter() {
        return new InStoragePresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_in_storage;
    }

    private void bindViews() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        storage_bit_name = findViewById(R.id.storage_bit_name);
        barcode = findViewById(R.id.barcode);
        not_in_storage = findViewById(R.id.not_in_storage);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        linear_body = findViewById(R.id.linear_body);
        loading_layout = findViewById(R.id.loading_layout);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        storage_item = findViewById(R.id.storage_item);
    }

    @Override
    public void initView() {
        bindViews();
        find_edit.setHint("直接扫描或输入条码");
        setOnClickListener(find_img,image_camera);
        recyclerview_data.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ZXingLibrary.initDisplayOpinion(this);
        find_edit.requestFocus();//获得焦点
        find_edit.setSelection(find_edit.length());//光标置尾
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    temporary_find_edit = find_edit.getText().toString();
                    requestReady(temporary_find_edit);
                }
                return true;
            }
        });
        storage_item.setVisibility(View.GONE);
        recyclerview_title.setVisibility(View.GONE);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("入库扫描");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_camera) {
            //Android系统6.0之上需要动态获取权限
            if (Build.VERSION.SDK_INT > 22) {
                if (ContextCompat.checkSelfPermission(InStorageActivity.this,
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(InStorageActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, 1);
                } else {
                    Intent intent = new Intent(InStorageActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                }
            } else {
                //系统版本在6.0之下，不需要动态获取权限。
                Intent intent = new Intent(InStorageActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
        }else if(id == R.id.find_img) {
            temporary_find_edit = find_edit.getText().toString();
            requestReady(temporary_find_edit);
        }
    }

    public void requestReady(String input) {
        find_edit.setText("");
        hideKeyboard(find_edit);
        boolean isStock = SharedPreferencesUtils.getBoolean(this, "examineSettings", "inStock");
        String binCode = storage_bit_name.getText().toString();
        String userName = SharedPreferencesUtils.getValue(this, "loginInfo", "userName");
        if(input.isEmpty()){
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else if(input.length() < 11) {
            mPresenter.getBinFind(input);
        }else{
            if(binCode.isEmpty()) {
                showToast("请先扫描库位");
                soundUtils.playSound(1,0);
            }else if(isStock){
                String substring = input.substring(0, 11);
                if("".equals(hide_salesOrder) || substring.equals(hide_salesOrder)) {
                    mPresenter.getScanPackage(input,userName,"入库",binCode);
                }else{
                    soundUtils.playSound(1,0);
                    new MessageDialog.Builder(getActivity())
                            .setTitle("混单")
                            .setMessage("单号："+temporary_find_edit+"\n可能存在混单,如需继续入库，可在设置取消检验混单")
                            .setConfirm("确定")
                            .setCancel(null)
                            .setCanceledOnTouchOutside(false).show();
                            //.setListener(dialog -> mPresenter.getScanPackage(input,userName,"入库",binCode)).show();
                }
            }else{
                mPresenter.getScanPackage(input,userName,"入库",binCode);
            }
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
                showToast("请打开相机权限");
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
        recyclerview_data.setAdapter(null);
        soundUtils.playSound(1,0);
        showToast(message);
    }

    @Override
    public void onGetScanPackageSuccess(BaseBean<List<StorageBean>> bean) {
        if(bean.getCode() == 0) {
            List<StorageBean> data = bean.getData();
            setAdapterData(data);
            soundUtils.playSound(0,0);
        }else if(bean.getCode() == 1) {
            List<StorageBean> data = bean.getData();
            setAdapterData(data);
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        } else{
            recyclerview_title.setVisibility(View.GONE);
            storage_item.setVisibility(View.GONE);
            recyclerview_data.setAdapter(null);
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        }
    }
    //请求成功设置适配器展现列表
    public void setAdapterData(List<StorageBean> data) {
        StorageBean next = data.iterator().next();
        barcode.setText(temporary_find_edit);
        not_in_storage.setText(next.getNotIntoCode());
        for (StorageBean bean : data) {
            bean.setType("入库");
        }
        hide_salesOrder = temporary_find_edit.substring(0,11);
        adapter = new StorageAdapter(this,data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);

    }

    @Override
    public void onGetBinFindSuccess(BaseBean<BinFindBean> bean) {
        if(bean.getCode() == 0) {
            BinFindBean data = bean.getData();
            temporary_find_edit = "";
            hide_salesOrder = "";
            storage_bit_name.setText(data.getBinCode());
            showToast("已储存库位，请扫描入库包装码");
            soundUtils.playSound(0,0);
        }else{
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        }
    }

}