package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.TestAdapter;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.contract.WorkScanContract;
import com.aige.loveproduction.presenter.WorkScanPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.aige.loveproduction.util.FileUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单扫描界面
 */
public class WorkScanActivity extends BaseActivity<WorkScanPresenter,WorkScanContract.View>
        implements WorkScanContract.View,View.OnClickListener {
    private ImageView camera,find_img;
    private EditText planNo_Edit;
    private RecyclerView lv_list;//列表组件
    //private WorkScanAdapter adapter;
    private TestAdapter adapter;
    private RelativeLayout loading_layout;
    private DrawerLayout drawer_layout;

    private LinearLayout find_layout;

    private FileUtil fileUtil;

    //添加权限
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };


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
        find_layout = findViewById(R.id.find_layout);
        drawer_layout = findViewById(R.id.drawer_layout);

    }
    @Override
    public void initView() {
        bindViews();
        fileUtil = new FileUtil(this);
        lv_list.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //获取摄像头
        ZXingLibrary.initDisplayOpinion(this);
        camera.setOnClickListener(this);
        planNo_Edit.requestFocus();//获得焦点
        planNo_Edit.setSelection(planNo_Edit.length());//光标置尾
        planNo_Edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER&&v.getText()!=null&& event.getAction() == KeyEvent.ACTION_DOWN){
                    requestReady(planNo_Edit.getText().toString());
                }
                return true;
            }
        });
        //点击扫描按钮
        find_img.setOnClickListener(this);
        //抽屉监听
        drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {
                //抽屉滑动
            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {
                //抽屉打开之后，给焦点给抽屉页面
                drawerView.setClickable(true);
                hideKeyboard(planNo_Edit);
                System.out.println("抽屉打开");
            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {
                //抽屉关闭
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //抽屉状态改变，应该是运动状态
            }
        });
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
            drawer_layout.openDrawer(GravityCompat.END);
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
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(WorkScanActivity.this, "请打开相机权限", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == 2) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this,HistoryLogActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(WorkScanActivity.this, "请打开文件读取权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    //获取到扫描的结果
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if(inspectSetting()) {
                        requestReady(result);
                    }else{
                        soundUtils.playSound(1,0);
                        showToast("工厂设置未设置完全！");
                    }


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
        loading_layout.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideLoading() {
        loading_layout.setVisibility(View.GONE);
        lv_list.setVisibility(View.VISIBLE);
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
            //Android系统6.0之上需要动态获取权限
            if (Build.VERSION.SDK_INT > 22) {
                if (ContextCompat.checkSelfPermission(WorkScanActivity.this,
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(WorkScanActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, 1);
                } else {
                    Intent intent = new Intent(WorkScanActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                }
            } else {
                //系统版本在6.0之下，不需要动态获取权限。
                Intent intent = new Intent(WorkScanActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
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
}