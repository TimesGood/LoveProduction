package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.ui.adapter.WorkScanAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.MixedLotContract;
import com.aige.loveproduction.mvp.presenter.MixedLotPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class MixedLotActivity extends BaseActivity<MixedLotPresenter, MixedLotContract.View>
        implements MixedLotContract.View , StatusAction {
    private Spinner spinner;
    private TextView find_edit;
    private String spinner_text;
    private WorkScanAdapter adapter;
    private WrapRecyclerView recyclerview_data;
    @Override
    protected MixedLotPresenter createPresenter() {
        return new MixedLotPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mixed_lot;
    }

    @Override
    protected void initView() {
        find_edit = findViewById(R.id.find_edit);
        recyclerview_data = findViewById(R.id.recyclerview_data);
    }

    @Override
    protected void initData() {
        setCenterTitle("????????????");
        setSpinner();
        setOnClickListener(R.id.image_camera,R.id.find_img);
        find_edit.setHint("????????????");
        find_edit.requestFocus();//????????????
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

    /**
     * ??????????????????
     */
    private void setSpinner() {
        final String[] type = {"?????????","?????????","?????????"};
        spinner = findViewById(R.id.spinner);
        //????????????
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, R.layout.spinner_select,type);
        //????????????
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_text = type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //????????????
                showToast("?????????????????????");
            } else {
                String result = intentResult.getContents();//?????????
                requestReady(result);
            }

        }

    }

    private void requestReady(String input) {
        find_edit.setText("");
        String opId = SharedPreferencesUtils.getValue(this, MachineSettings, "opId");
        if(input.isEmpty()) {
            showToast("???????????????");
            soundUtils.playSound(1, 0);
        }else if("".equals(opId) || opId == null){
            showToast("?????????????????????");
            soundUtils.playSound(1, 0);
        }else if("?????????".equals(spinner_text)) {
            showToast("???????????????");
            soundUtils.playSound(1, 0);
        }else{
            mPresenter.getHunPiByBatchNo(input,opId,spinner_text,getAsk());
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
            String input = find_edit.getText().toString();
            requestReady(input);
        }
    }

    @Override
    public void showLoading() {
        recyclerview_data.setVisibility(View.GONE);
        //loading_layout.setVisibility(View.VISIBLE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        recyclerview_data.setVisibility(View.VISIBLE);
        showComplete();
        mAnimation.alphaTran(recyclerview_data,300);
        //loading_layout.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        showEmpty();
        recyclerview_data.setAdapter(null);
        showToast(message);
        soundUtils.playSound(1,0);
    }

    @Override
    public void onGetHunPiByBatchNoSuccess(List<ScanCodeBean> bean) {
        soundUtils.playSound(0,0);
        spinner.setSelection(0);
        adapter = new WorkScanAdapter(this);
        adapter.setData(bean);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
    }

    @Override
    public void onGetMessageByWonoSuccess(BaseBean<List<ScanCodeBean>> bean) {
        List<ScanCodeBean> data = bean.getData();
        adapter = new WorkScanAdapter(this);
        adapter.setData(data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
        for(ScanCodeBean scanBean : data) {
            if(!"????????????".equals(scanBean.getMessage())) {
                soundUtils.playSound(1,0);
                return;
            }
        }
        soundUtils.playSound(0,0);
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
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}