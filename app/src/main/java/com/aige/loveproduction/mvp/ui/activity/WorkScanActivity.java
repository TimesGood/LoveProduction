package com.aige.loveproduction.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.adapter.TestAdapter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.WorkScanContract;
import com.aige.loveproduction.mvp.presenter.WorkScanPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.mvp.ui.customui.StatusLayout;
import com.aige.loveproduction.mvp.ui.dialogin.MessageDialog;
import com.aige.loveproduction.premission.Permission;
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
    private EditText planNo_Edit;
    private RecyclerView lv_list;//列表组件
    //private WorkScanAdapter adapter;
    private TestAdapter adapter;
    private RelativeLayout loading_layout;


    @Override
    protected WorkScanPresenter createPresenter() {
        return new WorkScanPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_work_scan;
    }
    private void bindViews() {
        camera = findViewById(R.id.image_camera);
        planNo_Edit = findViewById(R.id.find_edit);
        lv_list = findViewById(R.id.lv_list);
        find_img = findViewById(R.id.find_img);
        loading_layout = findViewById(R.id.loading_layout);
    }
    @Override
    public void initView() {
        bindViews();
        lv_list.setOverScrollMode(View.OVER_SCROLL_NEVER);
        camera.setOnClickListener(this);
        planNo_Edit.requestFocus();//获得焦点
        planNo_Edit.setSelection(planNo_Edit.length());//光标置尾
        planNo_Edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER&&v.getText()!=null&& event.getAction() == KeyEvent.ACTION_DOWN){
                    requestReady(planNo_Edit.getText().toString());
                }
                if(event == null) {
                    requestReady(planNo_Edit.getText().toString());
                }
                return true;
            }
        });
        //点击扫描按钮
        find_img.setOnClickListener(this);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("工单扫描");
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
//        else if(itemId == R.id.work_scan_record) {
//            //动态申请读取文件的权限
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(PERMISSIONS, 2);
//                }else{
//                    startActivity(HistoryLogActivity.class);
//                }
//            }
//        }
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
        String machineId = SharedPreferencesUtils.getValue(this, "machineSettings", "machineId");
        String opId = SharedPreferencesUtils.getValue(this, "machineSettings", "opId");
        String opType = SharedPreferencesUtils.getValue(this, "machineSettings", "opType");
        String workgroupId = SharedPreferencesUtils.getValue(this,"workgroupSettings","workgroupId");
        String employeeName = SharedPreferencesUtils.getValue(this, "handlerSettings", "employeeName");
        String username = SharedPreferencesUtils.getValue(this, "loginInfo", "userName");
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
    public void onGetMessageByWonoSuccess(BaseBean<PlanNoMessageBean> bean) {
        if(bean.getCode() == 0) {
            PlanNoMessageBean data = bean.getData();
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
    private void setAdapterData(PlanNoMessageBean data) {
        ScanCodeBean scanCodeBean = new ScanCodeBean();
        scanCodeBean.setMessage(data.getMsg());
        scanCodeBean.setPlanNo(data.getPlanNo());
        scanCodeBean.setOrderId(data.getOrderId());
        scanCodeBean.setTotalArea(String.valueOf(data.getTotalArea()));
        scanCodeBean.setSaoMiaoCount(String.valueOf(data.getTotalCnt()));
        scanCodeBean.setWono(data.getWono());
        scanCodeBean.setWeiSaoCount("0");

        List<ScanCodeBean> list = new ArrayList<>();
        list.add(scanCodeBean);
        adapter = new TestAdapter(this);
        adapter.setData(list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_list.setLayoutManager(manager);
        lv_list.setAdapter(adapter);

    }

    @Override
    public void showLoading() {
        lv_list.setVisibility(View.GONE);
        lv_list.setAdapter(null);
        //loading_layout.setVisibility(View.VISIBLE);
        showLoadings();
    }
    @Override
    public void hideLoading() {
        //loading_layout.setVisibility(View.GONE);
        showComplete();
        lv_list.setVisibility(View.VISIBLE);
        mAnimation.alphaTran(lv_list,300);
    }

    @Override
    public void onError(String message) {
        showToast(message);
        lv_list.setAdapter(null);
        soundUtils.playSound(1,0);
    }

    //扫描获取消息错误
    @Override
    public void onScanMessageError(Throwable throwable,int index) {
    }

    //检查工厂设置
    public boolean inspectSetting() {
        String machineId = SharedPreferencesUtils.getValue(this, "machineSettings", "machineId");
        String workgroupId = SharedPreferencesUtils.getValue(this,"workgroupSettings","workgroupId");
        String employeeName = SharedPreferencesUtils.getValue(this, "handleSettings", "employeeName");
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
            String plan = planNo_Edit.getText()+"";
            requestReady(plan);

        }
    }
    private void requestReady(String input) {
        planNo_Edit.setText("");
        hideKeyboard(planNo_Edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else{
            String opId = SharedPreferencesUtils.getValue(this, "machineSettings", "opId");
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