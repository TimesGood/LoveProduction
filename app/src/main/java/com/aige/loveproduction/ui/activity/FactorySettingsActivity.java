package com.aige.loveproduction.ui.activity;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.BaseDialog;
import com.aige.loveproduction.bean.HandlerBean;
import com.aige.loveproduction.bean.MachineBean;
import com.aige.loveproduction.bean.WorkgroupBean;
import com.aige.loveproduction.mvp.contract.FactorySettingContract;
import com.aige.loveproduction.mvp.presenter.FactorySettingPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.util.CommonUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工厂设置页面
 */
public class FactorySettingsActivity extends BaseActivity<FactorySettingPresenter, FactorySettingContract.View>
        implements FactorySettingContract.View, StatusAction {
    private RadioGroup machineRadio, workgroupRadio;
    private GridLayout handlerRadio;
    private int screenWidth, screenHeight;
    private LinearLayout machine_lay, workgroup_lay, handler_lay;
    private MenuItem item;
    private String temporary_machineId = "";
    private String temporary_workgroupId = "";

    @Override
    protected FactorySettingPresenter createPresenter() {
        return new FactorySettingPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fsettings;
    }


    @Override
    protected void initView() {
        machineRadio = findViewById(R.id.machineRadio);
        workgroupRadio = findViewById(R.id.workgroupRadio);
        handlerRadio = findViewById(R.id.handlerRadio);
        machine_lay = findViewById(R.id.machine_lay);
        workgroup_lay = findViewById(R.id.workgroup_lay);
        handler_lay = findViewById(R.id.handler_lay);
    }

    @Override
    protected void initData() {
        setCenterTitle("工厂设置");
        machine_lay.setVisibility(View.GONE);
        workgroup_lay.setVisibility(View.GONE);
        handler_lay.setVisibility(View.GONE);
        screenWidth = CommonUtils.getScreenWidth(this);
        screenHeight = CommonUtils.getScreenHeight(this);
        mPresenter.getMachine();
    }

    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fsettings_menu, menu);
        item = menu.getItem(0);
        item.setEnabled(false);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onError(String message) {

    }

    /**
     * 请求数据开始后回调，用于一些加载动画
     *
     * @param method 请求方法名
     */
    @Override
    public void showLoading(String method) {
        switch (method) {
            case "getMachine":
                machine_lay.setVisibility(View.GONE);
                break;
            case "getWorkgroupByMachineId":
                workgroup_lay.setVisibility(View.GONE);
                break;
            case "getHandlerByWorkgroupId":
                handler_lay.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 请求数据结束时回调，用于加载一些动画
     *
     * @param method 请求方法名
     */
    @Override
    public void hideLoading(String method) {
        switch (method) {
            case "getMachine":
                machine_lay.setVisibility(View.VISIBLE);
                machine_lay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ios_in_window));
//                setAnimation(machine_lay);
                break;
            case "getWorkgroupByMachineId":
                workgroup_lay.setVisibility(View.VISIBLE);
                workgroup_lay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ios_in_window));
//                setAnimation(workgroup_lay);
                break;
            case "getHandlerByWorkgroupId":
                handler_lay.setVisibility(View.VISIBLE);
                handler_lay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ios_in_window));
//                setAnimation(handler_lay);
                break;
        }
    }

    private MessageDialog.Builder getDialogBuilder(String title, String message, String confirm, String cancel) {
        return new MessageDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setConfirm(confirm)
                .setCancel(cancel)
                .setCanceledOnTouchOutside(false);
    }

    @Override
    public void onError(String method, String message) {
        System.out.println("*******************"+message);
        soundUtils.playSound(1, 0);
        if ("请连接网络后重试".equals(message)) {
            getDialogBuilder("网络错误", message, "确定", null)
                    .setListener(dialog -> onBackPressed())
                    .show();
            return;
        }
        switch (method) {
            case "getMachine":
                machineRadio.removeAllViews();
                getDialogBuilder(message, "是否重新加载？", "是", "否")
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                mPresenter.getMachine();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                onBackPressed();
                            }
                        }).show();
                break;
            case "getWorkgroupByMachineId":
                workgroupRadio.removeAllViews();
                getDialogBuilder(message, "是否重新加载？", "是", "否")
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                String substring = temporary_machineId.substring(0, 1);
                                String machineId = temporary_machineId.substring(1);
                                if ("*".equals(substring)) {
                                    mPresenter.getWorkgroupByMachineId(machineId);
                                } else {
                                    mPresenter.getWorkgroupByMachineId(temporary_machineId);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                onBackPressed();
                            }
                        }).show();
                break;
            case "getHandlerByWorkgroupId":
                handlerRadio.removeAllViews();
                getDialogBuilder(message, "是否重新加载？", "是", "否")
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                String substring = temporary_workgroupId.substring(0, 1);
                                String workgroupId = temporary_workgroupId.substring(1);
                                if ("*".equals(substring)) {
                                    mPresenter.getHandlerByWorkgroupId(workgroupId);
                                } else {
                                    mPresenter.getHandlerByWorkgroupId(temporary_workgroupId);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                onBackPressed();
                            }
                        }).show();
                break;
        }
    }

    //*********************************请求成功回调*****************************************
    private List<MachineBean> machineDatas;

    @Override
    public void onGetMachineSuccess(List<MachineBean> data) {
            machineRadio.removeAllViews();
            machineRadio.setOnCheckedChangeListener(null);
            machineRadio.clearCheck();
            for (MachineBean machineBean : data) {
                if (machineBean.getId() == null) continue;
                RadioButton radioButton = setButton(machineBean.getId(), machineBean.getMdName());
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(screenWidth / 4 - 22,
                        screenHeight / 20);
                layoutParams.setMargins(11, 11, 11, 11);
                machineRadio.addView(radioButton, layoutParams);
            }
            //设置监听
            machineRadio.setOnCheckedChangeListener((group, checkedId) -> {
                mPresenter.getWorkgroupByMachineId(String.valueOf(checkedId));
                workgroup_lay.setVisibility(View.GONE);
                handler_lay.setVisibility(View.GONE);
                machineDatas = data;
                temporary_machineId = String.valueOf(checkedId);
            });

            String radioSetting = SharedPreferencesUtils.getRadioSetting(FactorySettingsActivity.this, MachineSettings);
            //如果有选中的
            if (!radioSetting.isEmpty()) {
                int childCount = machineRadio.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    RadioButton radioButton = (RadioButton) machineRadio.getChildAt(i);
                    if (radioButton.getText().toString().equals(radioSetting)) {
                        radioButton.setChecked(true);
                    }
                }
            }
    }

    private List<WorkgroupBean> workgroupDatas;
    private boolean isWorkgroupInit = false;

    @Override
    public void onGetWorkgroupSuccess(List<WorkgroupBean> data) {
            workgroupRadio.removeAllViews();
            workgroupRadio.setOnCheckedChangeListener(null);
            workgroupRadio.clearCheck();
            for (WorkgroupBean workgroupBean : data) {
                RadioButton radioButton = setButton(workgroupBean.getId(), workgroupBean.getWgName());
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(screenWidth / 4 - 22,
                        screenHeight / 20);
                layoutParams.setMargins(11, 11, 11, 11);
                workgroupRadio.addView(radioButton, layoutParams);
            }
            //设置监听
            workgroupRadio.setOnCheckedChangeListener((group, checkedId) -> {
                mPresenter.getHandlerByWorkgroupId(String.valueOf(checkedId));
                handler_lay.setVisibility(View.GONE);
                workgroupDatas = data;
                temporary_workgroupId = String.valueOf(checkedId);
            });
            if (!isWorkgroupInit) {
                String radioSetting = SharedPreferencesUtils.getRadioSetting(FactorySettingsActivity.this, WorkgroupSettings);
                if (!radioSetting.isEmpty()) {
                    int childCount = workgroupRadio.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        RadioButton radioButton = (RadioButton) workgroupRadio.getChildAt(i);
                        if (radioButton.getText().toString().equals(radioSetting)) {
                            radioButton.setChecked(true);

                        }
                    }
                }
            }
        isWorkgroupInit = true;
    }

    private List<HandlerBean> handlerData;
    private boolean isHandlerInit = false;

    @Override
    public void onGetHandlerSuccess(List<HandlerBean> data) {
            handlerData = data;
            item.setEnabled(true);
            handlerRadio.removeAllViews();
            for (HandlerBean handlerBean : data) {
                CheckBox checkBox = new CheckBox(FactorySettingsActivity.this);
                checkBox.setButtonDrawable(R.drawable.checkbox);
                checkBox.setId(Integer.parseInt(handlerBean.getId()));
                checkBox.setText(handlerBean.getEmployeeName());
                checkBox.setTextColor(getColorStateList(R.color.checked_text_select_black_write));
                checkBox.setTextSize(16);
                checkBox.setGravity(Gravity.CENTER);
                checkBox.setBackground(ContextCompat.getDrawable(this, R.drawable.checkbox));
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(screenWidth / 4 - 22, screenHeight / 20);
                GridLayout.LayoutParams gl = new GridLayout.LayoutParams(ll);
                gl.setMargins(11, 11, 11, 11);
                handlerRadio.addView(checkBox, gl);
                if (!isHandlerInit) {
                    boolean handleSettings = SharedPreferencesUtils.getBoolean(FactorySettingsActivity.this, HandlerSettings, checkBox.getText().toString());
                    checkBox.setChecked(handleSettings);
                }
            }
        isHandlerInit = true;
    }
    //*****************************************************************************************

    /**
     * 保存设置
     */
    private boolean saveSettings() {
        if (machineDatas == null || workgroupDatas == null || handlerData == null) return false;
        //*****************机器*****************
        int childCount = machineRadio.getChildCount();
        for (byte i = 0; i < childCount; i++) {
            RadioButton rb = (RadioButton) machineRadio.getChildAt(i);
            if (rb.isChecked()) {
                machineDatas.forEach(v -> {
                    if (v.getMdName().equals(rb.getText().toString())) {
                        SharedPreferencesUtils.saveSetting(this, MachineSettings, "machineId", v.getId());
                        SharedPreferencesUtils.saveSetting(this, MachineSettings, "opId", v.getOperation_Id());
                        SharedPreferencesUtils.saveSetting(this, MachineSettings, "opType", v.getTypeName());
                        SharedPreferencesUtils.saveSetting(this, MachineSettings, v.getMdName(), true);
                    }
                });
                break;
            }
        }

        //******************工作组******************
        int workgroupCount = workgroupRadio.getChildCount();
        for (byte i = 0; i < workgroupCount; i++) {
            RadioButton rb = (RadioButton) workgroupRadio.getChildAt(i);
            if (rb.isChecked()) {
                workgroupDatas.forEach(v -> {
                    if (v.getWgName().equals(rb.getText().toString())) {
                        SharedPreferencesUtils.saveSetting(this, WorkgroupSettings, "workgroupId", v.getId());
                        SharedPreferencesUtils.saveSetting(this, WorkgroupSettings, v.getWgName(), true);
                    }
                });
                break;
            }
        }
        //*******************操作人********************
        int handlerCount = handlerRadio.getChildCount();
        final StringBuffer buffer = new StringBuffer();
        for (byte i = 0; i < handlerCount; i++) {
            View childline = handlerRadio.getChildAt(i);
            if (childline instanceof CheckBox) {
                CheckBox cb = (CheckBox) childline;
                if (cb.isChecked()) {
                    SharedPreferencesUtils.saveSetting(this, HandlerSettings, cb.getText().toString(), true);
                    for (int j = 0; j < handlerData.size(); j++) {
                        if (handlerData.get(j).getEmployeeName().equals(cb.getText().toString())) {
                            buffer.append(handlerData.get(i).getId()).append(",");
                            break;
                        }
                    }
                } else {
                    SharedPreferencesUtils.saveSetting(this, HandlerSettings, cb.getText() + "", false);
                }
            }
        }
        SharedPreferencesUtils.saveSetting(this, HandlerSettings, "employeeName", buffer.toString());

        return true;
    }

    /**
     * 动态创建RadioButton样式定制
     *
     * @param id   id
     * @param text 字体
     */
    private RadioButton setButton(String id, String text) {
        RadioButton radioButton = new RadioButton(FactorySettingsActivity.this);
        radioButton.setButtonDrawable(R.drawable.checkbox);
        radioButton.setId(Integer.parseInt(id));
        radioButton.setText(text);
        radioButton.setTextColor(getColorStateList(R.color.checked_text_select_black_write));
        radioButton.setTextSize(16);
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setBackground(ContextCompat.getDrawable(this, R.drawable.checkbox));
        return radioButton;
    }

    /**
     * 监听菜单，保存
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.confirm) {
            //检查必须项是否已经被选中，不选中不能保存
            View mRadio = findViewById(machineRadio.getCheckedRadioButtonId());
            View wRadio = findViewById(workgroupRadio.getCheckedRadioButtonId());
            CheckBox hRadio = null;
            for (byte i = 0; i < handlerRadio.getChildCount(); i++) {
                CheckBox box = (CheckBox) handlerRadio.getChildAt(i);
                if (box.isChecked()) {
                    hRadio = box;
                    break;
                }
            }
            if (mRadio == null) {
                showToast("请选择机器");
                soundUtils.playSound(1, 0);
                return true;
            } else if (wRadio == null) {
                showToast("请选择工作组");
                soundUtils.playSound(1, 0);
                return true;
            } else if (hRadio == null) {
                showToast("请选择操作人");
                soundUtils.playSound(1, 0);
                return true;
            }
            //清除旧设置
            SharedPreferencesUtils.detailSetting(FactorySettingsActivity.this, MachineSettings);
            SharedPreferencesUtils.detailSetting(FactorySettingsActivity.this, WorkgroupSettings);
            SharedPreferencesUtils.detailSetting(FactorySettingsActivity.this, HandlerSettings);
            saveSettings();
            soundUtils.playSound(0,0);
            showToast("保存成功");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}
