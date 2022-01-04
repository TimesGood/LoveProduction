package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.BaseDialog;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.SpeciaBarAsk;
import com.aige.loveproduction.mvp.contract.SpecialShapedContract;
import com.aige.loveproduction.mvp.presenter.SpecialShapedPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecialShapedActivity extends BaseActivity<SpecialShapedPresenter, SpecialShapedContract.View>
        implements SpecialShapedContract.View , StatusAction {
    private GridLayout grid_data;
    private TextView find_edit;
    private RelativeLayout main_body;

    private String find_edit_data;

    @Override
    protected SpecialShapedPresenter createPresenter() {
        return new SpecialShapedPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_special_shaped;
    }

    @Override
    protected void initView() {
        grid_data = findViewById(R.id.grid_data);
        main_body = findViewById(R.id.main_body);
        find_edit = findViewById(R.id.find_edit);
    }

    @Override
    protected void initData() {
        setCenterTitle("异形板件扫描");
        setOnClickListener(R.id.image_camera,R.id.find_img,R.id.submit_button);
        main_body.setVisibility(View.GONE);
        find_edit.setHint("板件二维码");
        find_edit.requestFocus();//获得焦点
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

    @Override
    public void showLoading() {
        main_body.setVisibility(View.GONE);
        showLoadings();
    }

    @Override
    public void hideLoading() {
        main_body.setVisibility(View.VISIBLE);
        main_body.setAnimation(AnimationUtils.loadAnimation(this,R.anim.ios_in_window));
        showComplete();
    }

    @Override
    public void onError(String message) {
        showEmpty();
        main_body.setVisibility(View.GONE);
        showComplete();
        showToast(message);
        soundUtils.playSound(1,0);
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
                requestReady(result);
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
            String input = find_edit.getText().toString();
            requestReady(input);
        }else if(id == R.id.submit_button) {
            String sssetting = SharedPreferencesUtils.getRadioSetting(this, Sssetting);
            if(ask == null) {
                soundUtils.playSound(1,0);
                showToast("请先扫描板件获取信息");
                return;
            }else if("".equals(sssetting)) {
                soundUtils.playSound(1,0);
                showToast("请在用户界面选择异形工序");
                return;
            }
            mPresenter.getSpecialBar(ask);
        }
    }
    private void requestReady(String input) {
        find_edit_data = input;
        find_edit.setText("");
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
            soundUtils.playSound(1,0);
        }else{
            mPresenter.getPlateListByPackageCode(input);
        }
    }
    private SpeciaBarAsk ask;
    @Override
    public void onGetPlateListByPackageCodeSuccess(PlateBean bean) {
        String userName = SharedPreferencesUtils.getValue(this, LoginInfo, "userName");
        String sssetting = SharedPreferencesUtils.getRadioSetting(this, Sssetting);
        soundUtils.playSound(0,0);
        List<String> listBean = new ArrayList<>();
        listBean.add(0,find_edit_data);
        listBean.add(1,bean.getMatProducer());
        listBean.add(2,bean.getMatname());
        listBean.add(3,String.valueOf(bean.getThk()));
        listBean.add(4,String.valueOf(bean.getFleng()));
        listBean.add(5,String.valueOf(bean.getFwidth()));
        listBean.add(6,bean.getDetailName());
        listBean.add(7,bean.getInfo1());
        getGridView(listBean);

        ask = new SpeciaBarAsk();
        ask.setBarCode(find_edit_data);
        ask.setMat(bean.getMatProducer());
        ask.setColor(bean.getMatname());
        ask.setThk(String.valueOf(bean.getThk()));
        ask.setFleng(String.valueOf(bean.getFleng()));
        ask.setFwidth(String.valueOf(bean.getFwidth()));
        ask.setName(bean.getDetailName());
        ask.setRemark(bean.getInfo1());
        ask.setOperator(userName);
        ask.setStep(sssetting);
    }
    private MessageDialog.Builder messageDialog;
    @Override
    public void submitShowLoading() {
        messageDialog = new MessageDialog.Builder(this);
        messageDialog.setMessage("提交数据中...").setCancel(null).setConfirm("取消")
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {

                    }
                })
                .show();
    }

    @Override
    public void submitHideLoading() {
        messageDialog.dismiss();
        main_body.setVisibility(View.GONE);
    }

    private void getGridView(List<String> bean) {
        int childCount = grid_data.getChildCount();
        byte j = 0;
        for(byte i = 0 ;i < childCount;i++) {
            if(i%2 != 0 && grid_data.getChildAt(i) instanceof TextView) {
                ((TextView) grid_data.getChildAt(i)).setText(bean.get(j));
                j++;
            }
        }
    }


    @Override
    public void onGetSpecialBar(BaseBean bean) {
        showToast("成功提交");
        ask = null;
        soundUtils.playSound(0,0);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
}