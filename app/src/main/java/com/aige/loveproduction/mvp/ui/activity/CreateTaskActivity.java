package com.aige.loveproduction.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.mvp.contract.CreateTaskContract;
import com.aige.loveproduction.mvp.presenter.CreateTaskPresenter;
import com.aige.loveproduction.mvp.ui.customui.StatusLayout;
import com.aige.loveproduction.mvp.ui.dialogin.MessageDialog;
import com.aige.loveproduction.premission.Permission;
import com.aige.loveproduction.util.IntentUtils;
import com.aige.loveproduction.util.SharedPreferencesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Arrays;

/**
 * 创建打印任务
 */
public class CreateTaskActivity extends BaseActivity<CreateTaskPresenter, CreateTaskContract.View>
        implements CreateTaskContract.View, StatusAction {

    private Spinner spinner;
    private EditText qr_edit,qr_type_edit,order_num_edit,find_edit;
    private Button submit_button;
    private ImageView image_camera,find_img;
    private RelativeLayout print_page;
    @Override
    protected CreateTaskPresenter createPresenter() {
        return new CreateTaskPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_task;
    }

    @Override
    public void initView() {
        print_page = findViewById(R.id.print_page);
        find_edit = findViewById(R.id.find_edit);
        qr_edit = findViewById(R.id.qr_edit);
        qr_type_edit = findViewById(R.id.qr_type_edit);
        order_num_edit = findViewById(R.id.order_num_edit);
        submit_button = findViewById(R.id.submit_button);
        image_camera = findViewById(R.id.image_camera);
        find_img = findViewById(R.id.find_img);
        spinner = findViewById(R.id.print_type_spinner);
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
        print_page.setVisibility(View.GONE);
        setOnClickListener(image_camera,find_img,submit_button);
    }
    @Override
    public void initToolbar() {
        super.initToolbar();
        setCenterTitle("无纸化打印");
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
        }else if(id == R.id.submit_button) {
            if(printBean == null) {
                showToast("请先获取打印数据");
                return;
            }
            System.out.println(getAsk());
            mPresenter.submitPrint(getAsk());
        }
    }
    private void requestReady(String input){
        find_edit.setText("");
        if(input.isEmpty()) {
            showToast("请输入条码");
        }else{
            mPresenter.getEntityByBarcode(input);
        }
    }
    @Override
    public void showLoading() {
        print_page.setVisibility(View.GONE);
        showLoadings();

    }

    @Override
    public void hideLoading() {
        print_page.setVisibility(View.VISIBLE);
        print_page.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_in_window));
        showComplete();
    }

    @Override
    public void onError(String message) {
        showToast(message);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }

    private PrintBean printBean;
    @Override
    public void onGetEntityByBarcodeSuccess(PrintBean bean) {
        qr_edit.setText(bean.getOrderId());
        qr_type_edit.setText(bean.getCodeType());
        printBean = bean;
    }
    private PrintAsk getAsk() {
        PrintAsk ask = new PrintAsk();
        ask.setBarCode(printBean.getBarcode());
        ask.setCodeType(printBean.getCodeType());
        ask.setNum(Integer.parseInt(order_num_edit.getText().toString()));
        TextView selectedView = (TextView)spinner.getSelectedView();
        ask.setPrintType(selectedView.getText().toString());
        ask.setArea(printBean.getAssemblyArea());
        String username = SharedPreferencesUtils.getValue(this, "loginInfo", "userName");
        ask.setCreator(username);
        ask.setStep("");
        ask.setPaper(1);
        return ask;
    }

    @Override
    public void onSubmitPrintSuccess() {

    }
}