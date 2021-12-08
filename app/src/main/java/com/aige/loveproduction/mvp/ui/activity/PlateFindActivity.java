package com.aige.loveproduction.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.adapter.PlateAdapter;
import com.aige.loveproduction.adapter.WrapRecyclerView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.mvp.contract.PlateFindContract;
import com.aige.loveproduction.mvp.ui.customui.StatusLayout;
import com.aige.loveproduction.mvp.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.mvp.presenter.PlateFindPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.mvp.ui.dialogin.MessageDialog;
import com.aige.loveproduction.premission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


/**
 * 板件扫描界面
 */
public class PlateFindActivity extends BaseActivity<PlateFindPresenter,PlateFindContract.View>
        implements PlateFindContract.View,View.OnClickListener , StatusAction {
    private RelativeLayout recyclerview_title;
    private TextView barcode,plate_name,material,matname,size;
    private EditText find_edit;
    private ImageView image_camera,find_img;
    private LinearLayout linear_body;
    private WrapRecyclerView recyclerview_data;//列表组件
    private PlateAdapter adapter;
    private LinearLayout plate_item;

    private String temporary_find_edit;


    @Override
    protected PlateFindPresenter createPresenter() {
        return new PlateFindPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_plate_find;
    }

    private void bindViews() {
        find_edit = findViewById(R.id.find_edit);
        image_camera = findViewById(R.id.image_camera);
        find_img = findViewById(R.id.find_img);
        linear_body = findViewById(R.id.linear_body);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        barcode = findViewById(R.id.barcode);
        plate_name = findViewById(R.id.plate_name);
        material = findViewById(R.id.material);
        matname = findViewById(R.id.matname);
        size = findViewById(R.id.size);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        plate_item = findViewById(R.id.plate_item);
    }

    @Override
    public void initView() {
        bindViews();
        recyclerview_data.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.item_line)));
        find_img.setOnClickListener(this);
        image_camera.setOnClickListener(this);
        find_edit.requestFocus();//获得焦点
        find_edit.setSelection(find_edit.length());//光标置尾
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER&&v.getText()!=null&& event.getAction() == KeyEvent.ACTION_DOWN){
                    requestReady(find_edit.getText().toString());
                }
                if(event == null) {
                    requestReady(find_edit.getText().toString());
                }
                return true;
            }
        });

        recyclerview_title.setVisibility(View.GONE);
        plate_item.setVisibility(View.GONE);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("板件查询");
    }

    /**
     * 获取权限后回调
     * @param requestCode 请求权限时携带的请求码
     * @param permissions 需要获取的权限
     * @param grantResults 权限获取结果
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
     * 从本界面startActivityForResult()跳转到另一个界面后setResult返回本界面时回调的函数
     * @param requestCode startActivityForResult()时携带的请求码
     * @param resultCode setResult时携带的返回码
     * @param data 上一页面返回的Intent对象
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
    public void onGetPlateListByPackageCodeSuccess(BaseBean<List<PlateBean>> bean) {
        if(bean.getCode() == 0) {
            List<PlateBean> data = bean.getData();
            PlateBean next = data.iterator().next();
            barcode.setText(temporary_find_edit);
            plate_name.setText(next.getDetailName());
            material.setText(next.getMatProducer());
            matname.setText(next.getMatname());
            StringBuffer buffer = new StringBuffer()
                    .append(next.getFleng()).append("*")
                    .append(next.getFwidth()).append("*")
                    .append(next.getThk());
            size.setText(buffer);
            adapter = new PlateAdapter(this,data);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerview_data.setLayoutManager(manager);
            recyclerview_data.setAdapter(adapter);
            soundUtils.playSound(0,0);
        }else{
            hideTest();
            showEmpty();
            soundUtils.playSound(1,0);
            recyclerview_data.setAdapter(null);
            showToast(bean.getMsg());
        }
    }
    private void hideTest() {
        recyclerview_data.setAdapter(null);
        plate_name.setText("");
        material.setText("");
        matname.setText("");
        size.setText("");
    }
    @Override
    public void showLoading() {
        recyclerview_title.setVisibility(View.GONE);
        plate_item.setVisibility(View.GONE);
        recyclerview_data.setVisibility(View.GONE);
        //loading_layout.setVisibility(View.VISIBLE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        recyclerview_title.setVisibility(View.VISIBLE);
        plate_item.setVisibility(View.VISIBLE);
        recyclerview_data.setVisibility(View.VISIBLE);
        //loading_layout.setVisibility(View.GONE);
        showComplete();
        mAnimation.alphaTran(linear_body,300);
    }

    @Override
    public void onError(String message) {
        showEmpty();
        recyclerview_data.setAdapter(null);
        hideTest();
        soundUtils.playSound(1,0);
        showToast(message);
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
        find_edit.setText("");
        temporary_find_edit = input;
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入板件编号");
            soundUtils.playSound(1,0);
        }else{
            mPresenter.getPlateListByPackageCode(input);
        }
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}