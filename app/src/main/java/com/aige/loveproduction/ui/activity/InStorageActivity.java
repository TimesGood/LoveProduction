package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.ui.adapter.StorageAdapter;
import com.aige.loveproduction.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.mvp.contract.InStorageContract;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.customui.viewgroup.DampNestedScrollView;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.mvp.presenter.InStoragePresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class InStorageActivity extends BaseActivity<InStoragePresenter,InStorageContract.View>
        implements InStorageContract.View, StatusAction {
    private TextView storage_bit_name,barcode,not_in_storage;
    private EditText find_edit;
    private ImageView image_camera,find_img;
    private StorageAdapter adapter;
    private WrapRecyclerView recyclerview_data;
    private LinearLayout recyclerview_title;
    private DampNestedScrollView damp_scrollview;
    private String hide_salesOrder = "";
    private String temporary_find_edit = "";


    @Override
    protected InStoragePresenter createPresenter() {
        return new InStoragePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_in_storage;
    }

    @Override
    protected void initView() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        storage_bit_name = findViewById(R.id.storage_bit_name);
        barcode = findViewById(R.id.barcode);
        not_in_storage = findViewById(R.id.not_in_storage);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        damp_scrollview = findViewById(R.id.damp_scrollview);
    }

    @Override
    protected void initData() {
        setCenterTitle("入库扫描");
        find_edit.setHint("直接扫描或输入条码");
        setOnClickListener(find_img,image_camera);
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,R.color.black));
        find_edit.requestFocus();//获得焦点
        find_edit.setSelection(find_edit.length());//光标置尾
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    requestReady(find_edit.getText().toString());
                }
                if(event == null) {
                    requestReady(find_edit.getText().toString());
                }
                return true;
            }
        });
        recyclerview_title.setVisibility(View.GONE);
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

    public void requestReady(String input) {
        temporary_find_edit = input;
        find_edit.setText("");
        hideKeyboard(find_edit);
        boolean isStock = SharedPreferencesUtils.getBoolean(this, ExamineSettings, "inStock");
        String binCode = storage_bit_name.getText().toString();
        String userName = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
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
    public void showLoading() {
        damp_scrollview.setVisibility(View.GONE);
        recyclerview_title.setVisibility(View.GONE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        showComplete();
        recyclerview_title.setVisibility(View.VISIBLE);
        damp_scrollview.setVisibility(View.VISIBLE);
        mAnimation.alphaTran(damp_scrollview,300);
    }

    @Override
    public void onError(String message) {
        showEmpty();
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
            showEmpty();
            recyclerview_title.setVisibility(View.GONE);
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
        data.forEach(v -> v.setType("入库"));
        hide_salesOrder = temporary_find_edit.substring(0,11);
        adapter = new StorageAdapter(this);
        adapter.setData(data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
        recyclerview_data.addHeaderView(R.layout.storage_item);

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

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}