//package com.aige.loveproduction.ui.activity;
//
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.animation.AnimationUtils;
//import android.widget.CheckBox;
//import android.widget.GridLayout;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//
//import androidx.core.content.ContextCompat;
//
//import com.aige.loveproduction.R;
//import com.aige.loveproduction.action.StatusAction;
//import com.aige.loveproduction.base.BaseDialog;
//import com.aige.loveproduction.base.BaseBean;
//import com.aige.loveproduction.bean.HandlerBean;
//import com.aige.loveproduction.bean.MachineBean;
//import com.aige.loveproduction.bean.WorkgroupBean;
//import com.aige.loveproduction.mvp.contract.FactorySettingContract;
//
//import com.aige.loveproduction.ui.customui.StatusLayout;
//import com.aige.loveproduction.ui.dialogin.MessageDialog;
//import com.aige.loveproduction.mvp.presenter.FactorySettingPresenter;
//import com.aige.loveproduction.base.BaseActivity;
//import com.aige.loveproduction.util.CommonUtils;
//import com.aige.loveproduction.util.SharedPreferencesUtils;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 工厂设置页面
// */
//public class FactorySettingsActivityDeprecated extends BaseActivity<FactorySettingPresenter, FactorySettingContract.View>
//        implements FactorySettingContract.View , StatusAction {
//    private RadioGroup machineRadio, workgroupRadio;
//    private GridLayout handlerRadio;
//    private int screenWidth, screenHeight;
//    private LinearLayout machine_lay, workgroup_lay, handler_lay;
//
//    private MenuItem item;
//
//    //临时储存一些数据
//    Map<String, Map<String, String>> temporary_data = new HashMap<>();
//    private String temporary_machineId = "";
//    private String temporary_workgroupId = "";
//
//    @Override
//    protected FactorySettingPresenter createPresenter() {
//        return new FactorySettingPresenter();
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_fsettings;
//    }
//
//    /**
//     * 绑定孔家、及初始化对象
//     */
//    private void bindViews() {
//        machineRadio = findViewById(R.id.machineRadio);
//        workgroupRadio = findViewById(R.id.workgroupRadio);
//        handlerRadio = findViewById(R.id.handlerRadio);
//        machine_lay = findViewById(R.id.machine_lay);
//        workgroup_lay = findViewById(R.id.workgroup_lay);
//        handler_lay = findViewById(R.id.handler_lay);
//
//    }
//
//    @Override
//    public void initView() {
//        bindViews();
//        machine_lay.setVisibility(View.GONE);
//        workgroup_lay.setVisibility(View.GONE);
//        handler_lay.setVisibility(View.GONE);
//        screenWidth = CommonUtils.getScreenWidth(this);
//        screenHeight = CommonUtils.getScreenHeight(this);
//        mPresenter.getMachine(true);
//
//    }
//
//    @Override
//    public void initToolbar() {
//        super.initToolbar();
//        setCenterTitle("工厂设置");
//    }
//
//    /**
//     * 创建菜单
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.fsettings_menu, menu);
//        item = menu.getItem(0);
//        item.setEnabled(false);
//        return true;
//    }
//
//    /**
//     * 监听菜单
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == android.R.id.home) {
//            onBackPressed();
//        } else if (itemId == R.id.confirm) {
//            //检查必须项是否已经被选中，不选中不能保存
//            View mRadio = findViewById(machineRadio.getCheckedRadioButtonId());
//            View wRadio = findViewById(workgroupRadio.getCheckedRadioButtonId());
//            CheckBox hRadio = null;
//            for (int i = 0; i < handlerRadio.getChildCount(); i++) {
//                CheckBox box = (CheckBox) handlerRadio.getChildAt(i);
//                if (box.isChecked()) {
//                    hRadio = box;
//                    break;
//                }
//            }
//            if (mRadio == null) {
//                showToast("请选择机器");
//                soundUtils.playSound(1, 0);
//                return true;
//            } else if (wRadio == null) {
//                showToast("请选择工作组");
//                soundUtils.playSound(1, 0);
//                return true;
//            } else if (hRadio == null) {
//                showToast("请选择操作人");
//                soundUtils.playSound(1, 0);
//                return true;
//            }
//            //清除旧设置
//            SharedPreferencesUtils.detailSetting(FactorySettingsActivityDeprecated.this, MachineSettings);
//            SharedPreferencesUtils.detailSetting(FactorySettingsActivityDeprecated.this, WorkgroupSettings);
//            SharedPreferencesUtils.detailSetting(FactorySettingsActivityDeprecated.this, HandlerSettings);
//            boolean machine_msg = saveSetting(machineRadio, MachineSettings, "机器");
//            boolean workgroup_msg = saveSetting(workgroupRadio, WorkgroupSettings, "工作组");
//            boolean handler_msg = saveSetting(handlerRadio, HandlerSettings, "操作人");
//            if (machine_msg && workgroup_msg && handler_msg) {
//                showToast("保存成功！");
//                soundUtils.playSound(0, 0);
//            } else {
//                soundUtils.playSound(1, 0);
//                getDialogBuilder("保存失败","刷新界面重新选择？","是","否")
//                        .setListener(dialog -> mPresenter.getMachine(false)).show();
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void showLoading() {
//    }
//
//    @Override
//    public void hideLoading() {
//    }
//
//    @Override
//    public void onError(String message) {
//
//    }
//
//    /**
//     * 请求数据开始后回调，用于一些加载动画
//     *
//     * @param method 请求方法名
//     */
//    @Override
//    public void showLoading(String method) {
//        switch (method) {
//            case "getMachine":
//                machine_lay.setVisibility(View.GONE);
//                break;
//            case "getWorkgroupByMachineId":
//                workgroup_lay.setVisibility(View.GONE);
//                break;
//            case "getHandlerByWorkgroupId":
//                handler_lay.setVisibility(View.GONE);
//                break;
//        }
//    }
//
//    /**
//     * 请求数据结束时回调，用于加载一些动画
//     *
//     * @param method 请求方法名
//     */
//    @Override
//    public void hideLoading(String method) {
//        switch (method) {
//            case "getMachine":
//                machine_lay.setVisibility(View.VISIBLE);
//                machine_lay.setAnimation(AnimationUtils.loadAnimation(this,R.anim.ios_in_window));
////                setAnimation(machine_lay);
//                break;
//            case "getWorkgroupByMachineId":
//                workgroup_lay.setVisibility(View.VISIBLE);
//                workgroup_lay.setAnimation(AnimationUtils.loadAnimation(this,R.anim.ios_in_window));
////                setAnimation(workgroup_lay);
//                break;
//            case "getHandlerByWorkgroupId":
//                handler_lay.setVisibility(View.VISIBLE);
//                handler_lay.setAnimation(AnimationUtils.loadAnimation(this,R.anim.ios_in_window));
////                setAnimation(handler_lay);
//                break;
//        }
//    }
//
//    private MessageDialog.Builder getDialogBuilder(String title,String message,String confirm,String cancel) {
//        return new MessageDialog.Builder(getActivity())
//                .setTitle(title)
//                .setMessage(message)
//                .setConfirm(confirm)
//                .setCancel(cancel)
//                .setCanceledOnTouchOutside(false);
//    }
//    @Override
//    public void onError(String method, String message) {
//        soundUtils.playSound(1, 0);
//        if ("请连接网络后重试".equals(message)) {
//            getDialogBuilder("网络错误",message,"确定",null)
//                    .setListener(dialog -> onBackPressed())
//                    .show();
//            return;
//        }
//        switch (method) {
//            case "getMachine":
//                machineRadio.removeAllViews();
//                getDialogBuilder(message,"是否重新加载？","是","否")
//                        .setListener(new MessageDialog.OnListener() {
//                            @Override
//                            public void onConfirm(BaseDialog dialog) {
//                                mPresenter.getMachine(true);
//                            }
//
//                            @Override
//                            public void onCancel(BaseDialog dialog) {
//                                onBackPressed();
//                            }
//                        }).show();
//                break;
//            case "getWorkgroupByMachineId":
//                workgroupRadio.removeAllViews();
//                getDialogBuilder(message,"是否重新加载？","是","否")
//                        .setListener(new MessageDialog.OnListener() {
//                            @Override
//                            public void onConfirm(BaseDialog dialog) {
//                                String substring = temporary_machineId.substring(0, 1);
//                                String machineId = temporary_machineId.substring(1);
//                                if ("*".equals(substring)) {
//                                    mPresenter.getWorkgroupByMachineId(machineId, true);
//                                } else {
//                                    mPresenter.getWorkgroupByMachineId(temporary_machineId, false);
//                                }
//                            }
//
//                            @Override
//                            public void onCancel(BaseDialog dialog) {
//                                onBackPressed();
//                            }
//                        }).show();
//                break;
//            case "getHandlerByWorkgroupId":
//                handlerRadio.removeAllViews();
//                getDialogBuilder(message,"是否重新加载？","是","否")
//                        .setListener(new MessageDialog.OnListener() {
//                            @Override
//                            public void onConfirm(BaseDialog dialog) {
//                                String substring = temporary_workgroupId.substring(0, 1);
//                                String workgroupId = temporary_workgroupId.substring(1);
//                                if ("*".equals(substring)) {
//                                    mPresenter.getHandlerByWorkgroupId(workgroupId, true);
//                                } else {
//                                    mPresenter.getHandlerByWorkgroupId(temporary_workgroupId, false);
//                                }
//                            }
//
//                            @Override
//                            public void onCancel(BaseDialog dialog) {
//                                onBackPressed();
//                            }
//                        }).show();
//                break;
//        }
//    }
//
//    @Override
//    public void onGetMachineSuccess(List<MachineBean> data, boolean isInit) {
//            machineRadio.removeAllViews();
//            machineRadio.setOnCheckedChangeListener(null);
//            machineRadio.clearCheck();
//            for (MachineBean machineBean : data) {
//                if (machineBean.getId() == null) continue;
//                RadioButton radioButton = setButton(machineBean.getId(), machineBean.getMdName(), 4);
//                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(screenWidth / 4 - 22,
//                        screenHeight / 20);
//                layoutParams.setMargins(11, 11, 11, 11);
//                machineRadio.addView(radioButton, layoutParams);
//                Map<String, String> map = new HashMap<>();
//                map.put("machineId", machineBean.getId());
//                map.put("opId", machineBean.getOperation_Id());
//                map.put("opType", machineBean.getTypeName());
//                temporary_data.put("machine-" + machineBean.getMdName(), map);
//            }
//            int childCount = machineRadio.getChildCount();
//            if (isInit) {
//                for (int i = 0; i < childCount; i++) {
//                    RadioButton radioButton = (RadioButton) machineRadio.getChildAt(i);
//                    boolean machineSettings = SharedPreferencesUtils.getBoolean(FactorySettingsActivityDeprecated.this, MachineSettings, radioButton.getText().toString());
//                    if (machineSettings) {
//                        radioButton.setChecked(machineSettings);
//                        mPresenter.getWorkgroupByMachineId(String.valueOf(radioButton.getId()), true);
//                        temporary_machineId = "*" + radioButton.getId();
//                    }
//                }
//            }
//            machineRadio.setOnCheckedChangeListener((group, checkedId) -> {
//                RadioButton button = (RadioButton) group.findViewById(checkedId);
//                Map<String, String> map = temporary_data.get("machine-" + button.getText());
//                if (map == null) return;
//                String machineId = map.get("machineId");
//                workgroup_lay.setVisibility(View.GONE);
//                handler_lay.setVisibility(View.GONE);
//                mPresenter.getWorkgroupByMachineId(machineId, false);
//                temporary_machineId = machineId;
//            });
//
//    }
//
//    @Override
//    public void onGetWorkgroupSuccess(List<WorkgroupBean> data, boolean isInit) {
//            workgroupRadio.removeAllViews();
//            workgroupRadio.setOnCheckedChangeListener(null);
//            workgroupRadio.clearCheck();
//            for (WorkgroupBean workgroupBean : data) {
//                RadioButton radioButton = setButton(workgroupBean.getId(), workgroupBean.getWgName(), 4);
//                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(screenWidth / 4 - 22,
//                        screenHeight / 20);
//                layoutParams.setMargins(11, 11, 11, 11);
//                workgroupRadio.addView(radioButton, layoutParams);
//                Map<String, String> map = new HashMap();
//                map.put("workgroupId", workgroupBean.getId());
//                temporary_data.put("workgroup-" + workgroupBean.getWgName(), map);
//            }
//            int childCount = workgroupRadio.getChildCount();
//            if (isInit) {
//                for (int i = 0; i < childCount; i++) {
//                    RadioButton radioButton = (RadioButton) workgroupRadio.getChildAt(i);
//                    boolean workgroupSettings = SharedPreferencesUtils.getBoolean(FactorySettingsActivityDeprecated.this, WorkgroupSettings, radioButton.getText().toString());
//                    if (workgroupSettings) {
//                        radioButton.setChecked(workgroupSettings);
//                        mPresenter.getHandlerByWorkgroupId(String.valueOf(radioButton.getId()), true);
//                        temporary_workgroupId = "*" + radioButton.getId();
//                    }
//                }
//            }
//            workgroupRadio.setOnCheckedChangeListener((group, checkedId) -> {
//                RadioButton button = (RadioButton) group.findViewById(checkedId);
//                button.setChecked(true);
//                handler_lay.setVisibility(View.GONE);
//                mPresenter.getHandlerByWorkgroupId(checkedId + "", false);
//                temporary_workgroupId = checkedId + "";
//            });
//    }
//
//    @Override
//    public void onGetHandlerSuccess(List<HandlerBean> data, boolean isInit) {
//            handlerRadio.removeAllViews();
//            item.setEnabled(true);
//            for (HandlerBean handlerBean : data) {
//                CheckBox checkBox = new CheckBox(FactorySettingsActivityDeprecated.this);
//                checkBox.setButtonDrawable(R.drawable.checkbox);
//                checkBox.setId(Integer.parseInt(handlerBean.getId()));
//                checkBox.setText(handlerBean.getEmployeeName());
//                checkBox.setTextColor(getColorStateList(R.color.checked_text_select_black_write));
//                checkBox.setTextSize(16);
//                checkBox.setGravity(Gravity.CENTER);
//                checkBox.setBackground(ContextCompat.getDrawable(this, R.drawable.checkbox));
//                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(screenWidth / 4 - 22, screenHeight / 20);
//                GridLayout.LayoutParams gl = new GridLayout.LayoutParams(ll);
//                gl.setMargins(11, 11, 11, 11);
//                handlerRadio.addView(checkBox, gl);
//                if (isInit) {
//                    boolean handleSettings = SharedPreferencesUtils.getBoolean(FactorySettingsActivityDeprecated.this, HandlerSettings, checkBox.getText().toString());
//                    checkBox.setChecked(handleSettings);
//                }
//                Map<String, String> map = new HashMap<>();
//                map.put("handlerId", handlerBean.getId());
//                map.put("userId", handlerBean.getUser_Id());
//                map.put("employeeName", handlerBean.getEmployeeName());
//                temporary_data.put("handler-" + handlerBean.getEmployeeName(), map);
//
//            }
//    }
//
//
//    /**
//     * 保存设置
//     *
//     * @param view     RadioGroup、GridLayout
//     * @param filename SharedPreferences文件名
//     * @param key      SharedPreferences key值
//     * @return
//     */
//    private boolean saveSetting(View view, String filename, String key) {
//        String fileType = filename.split("S")[0];
//        if (view instanceof RadioGroup) {
//            RadioGroup rg = (RadioGroup) view;
//            int childCount = rg.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                //获得子控件对象
//                View childline = rg.getChildAt(i);
//                //判断是不是RadioButton
//                if (childline instanceof RadioButton) {
//                    RadioButton rb = (RadioButton) childline;
//                    //查看是否被选中,把选中的设置保存到文件中
//                    if (rb.isChecked()) {
//                        SharedPreferencesUtils.saveSetting(this, filename, rb.getText() + "", true);
//                        SharedPreferencesUtils.saveSetting(this, filename, key, rb.getId() + "");
//                        Map<String, String> map = temporary_data.get(fileType + "-" + rb.getText().toString());
//                        if (map == null) return false;
//                        map.forEach((k,v) -> SharedPreferencesUtils.saveSetting(this, filename, k, v));
//                    } else {
//                        SharedPreferencesUtils.saveSetting(this, filename, rb.getText() + "", false);
//                    }
//                }
//            }
//        } else if (view instanceof GridLayout) {
//            GridLayout gl = (GridLayout) view;
//            int childCount = gl.getChildCount();
//            StringBuffer buffer = new StringBuffer();
//            for (int i = 0; i < childCount; i++) {
//                View childline = gl.getChildAt(i);
//                if (childline instanceof CheckBox) {
//                    CheckBox cb = (CheckBox) childline;
//                    if (cb.isChecked()) {
//                        SharedPreferencesUtils.saveSetting(this, filename, cb.getText() + "", true);
//                        SharedPreferencesUtils.saveSetting(this, filename, key, cb.getId() + "");
//                        Map<String, String> map = temporary_data.get(fileType + "-" + cb.getText().toString());
//                        if (map == null) return false;
//                        String handlerId = map.get("handlerId");
//                        buffer.append(handlerId).append(",");
//                    } else {
//                        SharedPreferencesUtils.saveSetting(this, filename, cb.getText() + "", false);
//                    }
//                }
//            }
//            SharedPreferencesUtils.saveSetting(this, filename, "employeeName", buffer.toString());
//        }
//        return true;
//    }
//
//    //动态创建的单选按钮
//    private RadioButton setButton(String id, String text, int column) {
//        RadioButton radioButton = new RadioButton(FactorySettingsActivityDeprecated.this);
//        radioButton.setButtonDrawable(R.drawable.checkbox);
//        radioButton.setId(Integer.parseInt(id));
//        radioButton.setText(text);
//        radioButton.setTextColor(getColorStateList(R.color.checked_text_select_black_write));
//        radioButton.setTextSize(16);
//        radioButton.setGravity(Gravity.CENTER);
//        radioButton.setBackground(ContextCompat.getDrawable(this, R.drawable.checkbox));
//        return radioButton;
//    }
//
//    @Override
//    public StatusLayout getStatusLayout() {
//        return findViewById(R.id.loading);
//    }
//}
