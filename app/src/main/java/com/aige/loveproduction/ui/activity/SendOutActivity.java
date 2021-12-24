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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.mvp.contract.ReportContract;
import com.aige.loveproduction.ui.adapter.StorageAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.mvp.contract.SendOutContract;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.mvp.presenter.SendOutPresenter;
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

public class SendOutActivity extends BaseActivity<SendOutPresenter,SendOutContract.View>
        implements SendOutContract.View,View.OnClickListener, StatusAction {

    private TextView find_edit,barcode,storage_bit_name_to,
            storage_bit_name,not_in_storage_to,not_in_storage,button_text;
    private ImageView image_camera,find_img;
    private RelativeLayout re_layout_body;
    private LinearLayout recyclerview_title,storage_item;

    private StorageAdapter adapter;
    private WrapRecyclerView recyclerview_data;

    private String temporary_barcode = "";


    @Override
    protected SendOutPresenter createPresenter() {
        return new SendOutPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_out;
    }
    @Override
    protected void initView() {
        image_camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        find_img = findViewById(R.id.find_img);
        barcode = findViewById(R.id.barcode);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        not_in_storage_to = findViewById(R.id.not_in_storage_to);
        not_in_storage = findViewById(R.id.not_in_storage);
        storage_bit_name_to = findViewById(R.id.storage_bit_name_to);
        storage_bit_name = findViewById(R.id.storage_bit_name);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        storage_item = findViewById(R.id.storage_item);
        button_text = findViewById(R.id.button_text);
        re_layout_body = findViewById(R.id.re_layout_body);
    }

    @Override
    protected void initData() {
        setCenterTitle("发货扫描");
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.item_line)));
        find_edit.setHint("直接扫描或输入条码");
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
        ((TextView)storage_item.getChildAt(2)).setText("已发");
        ((TextView)storage_item.getChildAt(3)).setText("未发");
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
    public void requestReady(String input) {
        temporary_barcode = input;
        find_edit.setText("");
        button_text.setVisibility(View.GONE);
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else{
            String userName = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
            mPresenter.getScanPackage(input,userName,"发货",input);
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
        storage_item.setVisibility(View.GONE);
        re_layout_body.setVisibility(View.GONE);
        recyclerview_title.setVisibility(View.GONE);
        //loading_layout.setVisibility(View.VISIBLE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        storage_item.setVisibility(View.VISIBLE);
        re_layout_body.setVisibility(View.VISIBLE);
        recyclerview_title.setVisibility(View.VISIBLE);
        //loading_layout.setVisibility(View.GONE);
        showComplete();
        mAnimation.alphaTran(re_layout_body,300);
    }

    @Override
    public void onError(String message) {
        showEmpty();
        soundUtils.playSound(1,0);
        recyclerview_data.setAdapter(null);
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
        }else{
            showEmpty();
            recyclerview_title.setVisibility(View.GONE);
            storage_item.setVisibility(View.GONE);
            recyclerview_data.setAdapter(null);
            showToast(bean.getMsg());
            soundUtils.playSound(1,0);
        }
    }
    private void setAdapterData(List<StorageBean> data) {
        data.forEach(d -> d.setType("发货"));
        button_text.setVisibility(View.VISIBLE);
        String notIntoCode = data.get(0).getNotIntoCode();
        StringBuffer buffer = new StringBuffer();
        if(notIntoCode != null) {
            if(notIntoCode.split(",").length > 15) {
                String substring = notIntoCode.substring(0,(15 * 6)-1);
                buffer.append(substring).append("...");
            }else{
                buffer.append(notIntoCode.substring(0,notIntoCode.length()-1));
            }
        }
        button_text.setText(buffer);
        barcode.setText(temporary_barcode);
        adapter = new StorageAdapter(this);
        adapter.setData(data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}