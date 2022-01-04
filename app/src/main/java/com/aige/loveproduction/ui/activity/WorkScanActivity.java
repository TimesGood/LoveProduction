package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.ui.adapter.WorkScanAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.WorkScanContract;
import com.aige.loveproduction.mvp.presenter.WorkScanPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 工单扫描界面
 */
public class WorkScanActivity extends BaseActivity<WorkScanPresenter,WorkScanContract.View>
        implements WorkScanContract.View,View.OnClickListener , StatusAction {
    private ImageView camera,find_img;
    private EditText find_edit;
    private WrapRecyclerView recyclerview_data;//列表组件
    private WorkScanAdapter adapter;


    @Override
    protected WorkScanPresenter createPresenter() {
        return new WorkScanPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_scan;
    }

    @Override
    protected void initView() {
        camera = findViewById(R.id.image_camera);
        find_edit = findViewById(R.id.find_edit);
        recyclerview_data = findViewById(R.id.recyclerview_data);
        find_img = findViewById(R.id.find_img);
    }

    @Override
    protected void initData() {
        setCenterTitle("工单扫描");
        recyclerview_data.setOverScrollMode(View.OVER_SCROLL_NEVER);
        setOnClickListener(camera,find_img);
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
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.work_scan_menu,menu);
        //现在还没用到侧滑菜单，先隐藏掉
        MenuItem item = menu.getItem(0);
        item.setVisible(false);
        return true;
    }
    //菜单监听
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
        }
        else if(itemId == R.id.work_scan_event_more) {

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @param requestCode 请求码
     * @param permissions 权限名称
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                if(inspectSetting()) {
                    requestReady(result);
                }else{
                    soundUtils.playSound(1,0);
                    showToast("工厂设置未设置完全！");
                }
            }

        }
    }
    public WonoAsk getAsk() {
        String machineId = SharedPreferencesUtils.getValue(this, MachineSettings, "machineId");
        String opId = SharedPreferencesUtils.getValue(this, MachineSettings, "opId");
        String opType = SharedPreferencesUtils.getValue(this, MachineSettings, "opType");
        String workgroupId = SharedPreferencesUtils.getValue(this,WorkgroupSettings,"workgroupId");
        String employeeName = SharedPreferencesUtils.getValue(this, HandlerSettings, "employeeName");
        String username = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
        WonoAsk ask = new WonoAsk();
        ask.setUserName(username);
        ask.setEmployeeId(employeeName);
        ask.setMachineId(machineId);
        ask.setWorkGroupId(workgroupId);
        ask.setOperationId(opId);
        ask.setOperationType(opType);
        return ask;
    }
    @Override
    public void onGetMessageByWonoSuccess(BaseBean<ScanCodeBean> bean) {
        if(bean.getCode() == 0) {
            ScanCodeBean data = bean.getData();
            if(data.getCode() == 0) {
                data.setMsg("扫描成功");
                setAdapterData(data);
                soundUtils.playSound(0,0);
            }else{
                setAdapterData(data);
                soundUtils.playSound(1,0);
            }

        }else{
            showToast(bean.getMsg());
        }
    }
    private void setAdapterData(ScanCodeBean data) {
        List<ScanCodeBean> list = new ArrayList<>();
        list.add(data);
        adapter = new WorkScanAdapter(this);
        adapter.setData(list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);

    }

    @Override
    public void showLoading() {
        showLoadings();
        recyclerview_data.setVisibility(View.GONE);
        recyclerview_data.setAdapter(null);
    }
    @Override
    public void hideLoading() {
        showComplete();
        recyclerview_data.setVisibility(View.VISIBLE);
        mAnimation.alphaTran(recyclerview_data,300);
    }

    @Override
    public void onError(String message) {
        showEmpty();
        showToast(message);
        recyclerview_data.setAdapter(null);
        soundUtils.playSound(1,0);
    }

    //扫描获取消息错误
    @Override
    public void onScanMessageError(Throwable throwable,int index) {
    }

    //检查工厂设置
    public boolean inspectSetting() {
        String machineId = SharedPreferencesUtils.getValue(this, MachineSettings, "machineId");
        String workgroupId = SharedPreferencesUtils.getValue(this,WorkgroupSettings,"workgroupId");
        String employeeName = SharedPreferencesUtils.getValue(this, HandlerSettings, "employeeName");
        return !machineId.isEmpty() || !workgroupId.isEmpty() || !employeeName.isEmpty();
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
            String plan = find_edit.getText()+"";
            requestReady(plan);

        }
    }
    private void requestReady(String input) {
        find_edit.setText("");
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else{
            String opId = SharedPreferencesUtils.getValue(this, MachineSettings, "opId");
            if(opId.isEmpty()) {
                showToast("未设置工厂设置");
                soundUtils.playSound(1,0);
                return;
            }
            WonoAsk ask = getAsk();
            ask.setScanCode(input);
            mPresenter.getMessageByWono(ask);
        }
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}