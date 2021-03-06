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
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.StorageBean;

import com.aige.loveproduction.mvp.contract.MoveStorageContract;
import com.aige.loveproduction.mvp.presenter.MoveStoragePresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.ui.customui.viewgroup.DampNestedScrollView;
import com.aige.loveproduction.ui.dialogin.MessageDialog;

import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MoveStorageActivity extends BaseActivity<MoveStoragePresenter,MoveStorageContract.View>
        implements MoveStorageContract.View,View.OnClickListener, StatusAction {
    private TextView barcode,storage_bit_name,not_in_storage_to,not_in_storage;
    private EditText find_edit;
    private ImageView image_camera,find_img;
    private StorageAdapter adapter;
    private WrapRecyclerView recyclerview_data;
    private LinearLayout recyclerview_title,storage_item;
    private DampNestedScrollView damp_scrollview;

    private String hide_salesOrder = "";
    private String temporary_find_edit = "";


    @Override
    protected MoveStoragePresenter createPresenter() {
        return new MoveStoragePresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_move_storage;
    }

    @Override
    protected void initView() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        storage_bit_name = findViewById(R.id.storage_bit_name);
        barcode = findViewById(R.id.barcode);
        not_in_storage_to = findViewById(R.id.not_in_storage_to);
        not_in_storage = findViewById(R.id.not_in_storage);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        storage_item = findViewById(R.id.storage_item);
        damp_scrollview = findViewById(R.id.damp_scrollview);
    }

    @Override
    protected void initData() {
        setCenterTitle("????????????");
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,1,getColor(R.color.item_line)));
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    requestReady(find_edit.getText().toString());
                    return true;
                }
                if(event == null) {
                    requestReady(find_edit.getText().toString());
                }

                return true;
            }
        });
        setOnClickListener(find_img,image_camera);

        not_in_storage_to.setVisibility(View.GONE);
        not_in_storage.setVisibility(View.GONE);
        ((TextView)storage_item.getChildAt(2)).setText("??????");
        ((TextView)storage_item.getChildAt(3)).setText("??????");
        storage_item.setVisibility(View.GONE);
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
    public void requestReady(String input) {
        temporary_find_edit = input;
        find_edit.setText("");
        hideKeyboard(find_edit);
        boolean outStock = SharedPreferencesUtils.getBoolean(this, ExamineSettings, "outStock");
        String binCode = storage_bit_name.getText().toString();
        String userName = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
        if(input.isEmpty()){
            showToast("???????????????");
            soundUtils.playSound(1,0);
        }else if(input.length() < 11) {
            mPresenter.getBinFind(input);
        }else{
            if(binCode.isEmpty()) {
                showToast("??????????????????");
                soundUtils.playSound(1,0);
            }else if(outStock){
                String substring = input.substring(0, 11);
                if("".equals(hide_salesOrder) || substring.equals(hide_salesOrder)) {
                    mPresenter.getScanPackage(input,userName,"??????",binCode);
                }else{
                    soundUtils.playSound(1,0);
                    new MessageDialog.Builder(getActivity())
                            .setTitle("??????")
                            .setMessage("?????????"+temporary_find_edit+"\n??????????????????,???????????????")
                            .setConfirm("??????")
                            .setCancel("??????")
                            .setCanceledOnTouchOutside(false)
                            .setListener(dialog -> mPresenter.getScanPackage(input,userName,"??????",binCode)).show();
                }
            }else{
                mPresenter.getScanPackage(input,userName,"??????",binCode);
            }
        }
    }
    @Override
    public void onGetScanPackageSuccess(BaseBean<List<StorageBean>> bean) {
        if(bean.getCode() == 0) {
            List<StorageBean> data = bean.getData();
            setAdapterData(data);
            soundUtils.playSound(0,0);
        }else if(bean.getCode() == 1 || bean.getCode() == 2) {
            List<StorageBean> data = bean.getData();
            setAdapterData(data);
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        }else{
            showEmpty();
            showToast(bean.getMsg());
            recyclerview_title.setVisibility(View.GONE);
            storage_item.setVisibility(View.GONE);
            recyclerview_data.setAdapter(null);
            soundUtils.playSound(1,0);
        }
    }
    public void setAdapterData(List<StorageBean> data) {
        data.forEach(d -> d.setType("??????"));
        adapter = new StorageAdapter(this);
        adapter.setData(data);
        barcode.setText(temporary_find_edit);
        hide_salesOrder = temporary_find_edit.substring(0,11);
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
            showToast("??????????????????????????????????????????");
            soundUtils.playSound(0,0);
        }else{
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        }
    }

    @Override
    public void showLoading() {
        storage_item.setVisibility(View.GONE);
        damp_scrollview.setVisibility(View.GONE);
        recyclerview_title.setVisibility(View.GONE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        storage_item.setVisibility(View.VISIBLE);
        damp_scrollview.setVisibility(View.VISIBLE);
        recyclerview_title.setVisibility(View.VISIBLE);
        showComplete();
        mAnimation.alphaTran(damp_scrollview,300);
    }

    @Override
    public void onError(String message) {
        showEmpty();
        soundUtils.playSound(1,0);
        recyclerview_data.setAdapter(null);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}