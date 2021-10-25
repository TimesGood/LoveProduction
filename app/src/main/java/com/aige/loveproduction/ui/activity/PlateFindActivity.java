package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import android.widget.Toast;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.PlateAdapter;
import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.contract.PlateFindContract;
import com.aige.loveproduction.customui.RecycleViewDivider;
import com.aige.loveproduction.presenter.PlateFindPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.util.SoundUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * 板件扫描界面
 */
public class PlateFindActivity extends BaseActivity<PlateFindPresenter,PlateFindContract.View> implements PlateFindContract.View,View.OnClickListener {
    private RelativeLayout loading_layout,recyclerview_title;
    private TextView barcode,plate_name,material,matname,size;
    private EditText find_edit;
    private ImageView image_camera,find_img;
    private LinearLayout linear_body;
    private RecyclerView plate_find_list;//列表组件
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
        plate_find_list = findViewById(R.id.plate_find_list);
        barcode = findViewById(R.id.barcode);
        plate_name = findViewById(R.id.plate_name);
        material = findViewById(R.id.material);
        matname = findViewById(R.id.matname);
        size = findViewById(R.id.size);
        loading_layout = findViewById(R.id.loading_layout);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        plate_item = findViewById(R.id.plate_item);
    }

    @Override
    public void initView() {
        bindViews();
        //nested_scrollview.setVisibility(View.GONE);
        plate_find_list.setOverScrollMode(View.OVER_SCROLL_NEVER);
        plate_find_list.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.grey)));
        find_img.setOnClickListener(this);
        //获取摄像头
        ZXingLibrary.initDisplayOpinion(this);
        image_camera.setOnClickListener(this);
        find_edit.requestFocus();//获得焦点
        find_edit.setSelection(find_edit.length());//光标置尾
        find_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER&&v.getText()!=null&& event.getAction() == KeyEvent.ACTION_DOWN){
                    temporary_find_edit = find_edit.getText().toString();
                    requestReady(temporary_find_edit);
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
        if(requestCode == 1) {
            //判断权限是否已经被允许，被允许实行相应业务
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(PlateFindActivity.this,CaptureActivity.class);
                startActivityForResult(intent,1);
            }else{
                soundUtils.playSound(1,0);
                showToast("请打开摄像机权限");
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
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    temporary_find_edit = data.getExtras().getString(CodeUtils.RESULT_STRING);
                    requestReady(temporary_find_edit);
                }
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
            plate_find_list.setLayoutManager(manager);
            plate_find_list.setAdapter(adapter);
            soundUtils.playSound(0,0);
        }else{
            hideTest();
            soundUtils.playSound(1,0);
            showToast(bean.getMsg());
        }
    }
    private void hideTest() {
        plate_find_list.setAdapter(null);
        plate_name.setText("");
        material.setText("");
        matname.setText("");
        size.setText("");
    }
    @Override
    public void showLoading() {
        recyclerview_title.setVisibility(View.GONE);
        plate_item.setVisibility(View.GONE);
        plate_find_list.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        recyclerview_title.setVisibility(View.VISIBLE);
        plate_item.setVisibility(View.VISIBLE);
        plate_find_list.setVisibility(View.VISIBLE);
        loading_layout.setVisibility(View.GONE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(linear_body,"scaleX",0,1f);
        animator.setDuration(200);
        animator.start();
    }

    @Override
    public void onError(String message) {
        plate_find_list.setAdapter(null);
        hideTest();
        soundUtils.playSound(1,0);
        showToast(message);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_camera) {
            //动态申请权限只在Android6.0（API22）加入
            if (Build.VERSION.SDK_INT > 22) {
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    //检查权限是否被授权，已经被授权，执行相应业务
                    Intent intent = new Intent(PlateFindActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    //当第一次申请该权限被拒绝后shouldShowRequestPermissionRationale返回true，可以在这里可以执行一些引导，告诉用户为什么要申请这个权限
                    requestPermissions(new String[]{Manifest.permission.CAMERA},1);
                } else {
                    //权限没被授权，在这进行授权申请
                    requestPermissions(new String[]{Manifest.permission.CAMERA},1);
                }
            }else{
                //系统版本在6.0之下，不需要动态申请权限，直接执行业务
                Intent intent = new Intent(PlateFindActivity.this, CaptureActivity.class);
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
        if(input.isEmpty()) {
            showToast("请输入板件编号");
            soundUtils.playSound(1,0);
        }else{
            mPresenter.getPlateListByPackageCode(input);
        }
    }
}