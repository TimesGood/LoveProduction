package com.aige.loveproduction.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.PlateWrapBean;
import com.aige.loveproduction.mvp.contract.PlateFindContract;
import com.aige.loveproduction.mvp.presenter.PlateFindPresenter;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.ui.adapter.PlateAdapter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.util.IntentUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 板件扫描界面
 */
public class PlateFindActivity extends BaseActivity<PlateFindPresenter,PlateFindContract.View>
        implements PlateFindContract.View,View.OnClickListener , StatusAction {
    private RelativeLayout recyclerview_title;
    private TextView barcode,plate_name,material,matname,size,operator,supplier;
    private EditText find_edit;
    private ImageView image_camera,find_img;
    private RecyclerView recyclerview_data;//列表组件
    private PlateAdapter adapter;
    private final StringBuilder builder = new StringBuilder();
    private String temporary_find_edit;


    @Override
    protected PlateFindPresenter createPresenter() {
        return new PlateFindPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plate_find;
    }

    @Override
    protected void initView() {
        find_edit = findViewById(R.id.find_edit);
        image_camera = findViewById(R.id.image_camera);
        find_img = findViewById(R.id.find_img);
        barcode = findViewById(R.id.barcode);
        plate_name = findViewById(R.id.plate_name);
        material = findViewById(R.id.material);
        matname = findViewById(R.id.matname);
        size = findViewById(R.id.size);
        operator = findViewById(R.id.operator);
        supplier = findViewById(R.id.supplier);
        recyclerview_title = findViewById(R.id.recyclerview_title);
        recyclerview_data = findViewById(R.id.recyclerview_data);
    }

    @Override
    protected void initData() {
        setCenterTitle("板件查询");
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
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,1,getColor(R.color.black)));
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
    public void onGetPlateListByPackageCodeSuccess(PlateWrapBean bean) {
        soundUtils.playSound(0,0);
        //List分组
//        Map<Object, List<PlateBean>> collect = bean.getPlateBeans().stream().collect(
//                Collectors.groupingBy(b -> b.getSolutionConfigName() + b.getPlanNo() + b.getApS_Code()));
//        //分组后循环渲染列表
//        collect.forEach((k,v) -> setRecyclerviewData(v));
        //他要按服务器传过来的数据顺序进行分组排列，罢了罢了，上面lambda函数不能用了，改吧
        StringBuilder newStr = new StringBuilder();
        List<PlateBean> plateBeans = bean.getPlateBeans();
        for(byte i = 0;i < bean.getPlateBeans().size();i++) {
            PlateBean plateBean = bean.getPlateBeans().get(i);
            newStr.delete(0,newStr.length());
            newStr.append(plateBean.getSolutionConfigName())
                    .append(plateBean.getPlanNo())
                    .append(plateBean.getApS_Code());
            if(!builder.toString().equals(newStr.toString())) {
                plateBeans.add(i,null);
            }
            builder.delete(0,builder.length());
            builder.append(plateBean.getSolutionConfigName())
                    .append(plateBean.getPlanNo())
                    .append(plateBean.getApS_Code());
        }
        adapter = new PlateAdapter(this);
        adapter.setData(bean.getPlateBeans());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
        PlateBean plateBean = bean.getPlateBeans().get(1);
        plate_name.setText(plateBean.getDetailName());
        material.setText(plateBean.getMatProducer());
        matname.setText(plateBean.getMatname());
        builder.delete(0,builder.length());
        builder.append(plateBean.getFleng()).append("*")
                .append(plateBean.getFwidth()).append("*")
                .append(plateBean.getThk());
        size.setText(builder.toString());
        operator.setText(bean.getNickName());
        supplier.setText(plateBean.getSupplier());
    }
    private void hideTest() {
        for(int i = 1;i < recyclerview_title.getChildCount();i++){
            if(i%2 == 0 && recyclerview_title.getChildAt(i) instanceof TextView) {
                ((TextView) recyclerview_title.getChildAt(i)).setText("");
            }
        }
    }
    @Override
    public void showLoading() {
        recyclerview_title.setVisibility(View.GONE);
        hideTest();
        showLoadings();
    }

    @Override
    public void hideLoading() {
        showComplete();
        recyclerview_title.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String message) {
        showEmpty();
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
        barcode.setText(input);
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