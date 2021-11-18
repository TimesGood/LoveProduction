package com.aige.loveproduction.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.AnimAction;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.adapter.TransportAdapter;
import com.aige.loveproduction.adapter.WrapRecyclerView;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.TransfersContract;
import com.aige.loveproduction.mvp.presenter.TransfersPresenter;
import com.aige.loveproduction.mvp.ui.customui.StatusLayout;
import com.aige.loveproduction.mvp.ui.customui.view.RecycleViewDivider;
import com.aige.loveproduction.mvp.ui.dialogin.MessageDialog;
import com.aige.loveproduction.premission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class TransfersActivity extends BaseActivity<TransfersPresenter, TransfersContract.View> implements TransfersContract.View , StatusAction , AnimAction {
    private TextView find_edit;
    private RelativeLayout loading_layout;
    //private RecyclerView recyclerview_data;
    private WrapRecyclerView recyclerview_data;
    private GridLayout grid_item;
    private TransportAdapter adapter;
    private String transport_temp = "";
    private String input_temp;
    private TextView orderId_text,plan_text,not_pack,not_transfer;


    @Override
    protected TransfersPresenter createPresenter() {
        return new TransfersPresenter();
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("转运扫描");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transfers;
    }
    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fsettings_menu, menu);
        menu.getItem(0).setTitle("提交");
        return true;
    }

    /**
     * 菜单监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.confirm) {
            requestReady(1,transport_temp);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        setOnClickListener(R.id.image_camera,R.id.find_img);
        loading_layout = findViewById(R.id.loading_layout);
        find_edit = findViewById(R.id.find_edit);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        grid_item = findViewById(R.id.grid_item);
        orderId_text = findViewById(R.id.orderId_text);
        plan_text = findViewById(R.id.plan_text);
        not_pack = findViewById(R.id.not_pack);
        not_transfer = findViewById(R.id.not_transfer);
        find_edit.setHint("直接扫描、或输入包装码");
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener((v, actionId, event) -> {
            if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && v.getText() != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                requestReady(0,find_edit.getText().toString());
            }
            if(event == null) {
                requestReady(0,find_edit.getText().toString());
            }
            return true;

        });
        grid_item.setVisibility(View.GONE);
        recyclerview_data.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerview_data.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1,getColor(R.color.grey)));
    }

    @Override
    public void showLoading() {
        grid_item.setVisibility(View.GONE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        grid_item.setVisibility(View.VISIBLE);
        showComplete();
        mAnimation.alphaTran(recyclerview_data,300);
    }

    @Override
    public void onError(String method, String message) {
        soundUtils.playSound(1,0);
        if(method.equals("getTransportVerification")) {
            soundUtils.playSound(1,0);
            showToast(message);
            recyclerview_data.setAdapter(null);
            transport_temp = "";
        }else if(method.equals("transportScan")) {
            showToast(message);
        }else if(method.equals("transportSubmit")) {
            showToast(message);
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
                        .setTitle("温馨提醒")
                        .setMessage("权限拒绝后某些功能将不能使用，为了使用完整功能请打开"+permission.getPermissionHint(Arrays.asList(permissions)))
                        .setConfirm("去开启")
                        .setListener(dialog -> IntentUtils.gotoPermission(this))
                        .show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //扫码失败
                showToast("解析二维码失败");
            } else {
                String result = intentResult.getContents();//返回值
                requestReady(0,result);
            }
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_camera) {
            permission.applyPermission( new String[]{Manifest.permission.CAMERA}, new Permission.ApplyListener() {
                @Override
                public void apply(String[] permission) {
                    requestPermissions(permission,1);
                }
                @Override
                public void applySuccess() {
                    startActivityCapture();
                }
            });

        }else if(id == R.id.find_img) {
            requestReady(0,find_edit.getText().toString());
        }
    }

    private void requestReady(int request,String input) {
        find_edit.setText("");
        if(request == 0) {
            if(input.isEmpty()) {
                showToast("请输入包装码");
                soundUtils.playSound(1,0);
                return;
            }else if(input.length() < 17) {
                showToast("请输入正确的包装码");
                soundUtils.playSound(1,0);
                return;
            }
            input_temp = input;
            if(transport_temp.length() > 16
                    && transport_temp.substring(0,transport_temp.length()-5)
                    .equals(input.substring(0,input.length()-5))) {
                String userName = SharedPreferencesUtils.getValue(this, "loginInfo", "userName");
                mPresenter.transportScan(input,userName);
            }else {
                mPresenter.getTransportVerification(input);
            }

        }else if(request == 1) {
            mPresenter.transportSubmit(input);
        }
    }
    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.status_layout);
    }

    private TransportBean beanList;
    @Override
    public void onGetTransport(TransportBean bean) {
        if (bean == null) return;
        beanList = bean;
        TransportBean.TransportBeans transportBeans = bean.getList().get(0);
        transport_temp = transportBeans.getPackageCode();
        orderId_text.setText(transport_temp.substring(0,13));
        plan_text.setText(transportBeans.getType());
        not_pack.setText(bean.getWeiBaoNumber());
        not_transfer.setText(bean.getNotTransport());
        adapter = new TransportAdapter(this);
        adapter.setData(bean.getList());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);

        recyclerview_data.addHeaderView(R.layout.transfer_item);
        requestReady(0,input_temp);
    }

    @Override
    public void onScanSuccess(BaseBean bean) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format1 = format.format(new Date().getTime());
        TransportBean.TransportBeans beans = new TransportBean.TransportBeans();
        beans.setPackageCode(input_temp);
        beans.setTransportDate(format1);
        int position = getPosition(input_temp);
        if(position == -1) return;
        adapter.setItem(position,beans);
        soundUtils.playSound(0,0);
    }
    private int getPosition(String packageCode){
        for(int i = 0; i < beanList.getList().size(); i++) {
            if(packageCode.equals(beanList.getList().get(i).getPackageCode())) return i;
        }
        return -1;
    }
    @Override
    public void onSubmitSuccess(BaseBean bean) {
        soundUtils.playSound(0,0);
        showToast("提交成功");
    }
}