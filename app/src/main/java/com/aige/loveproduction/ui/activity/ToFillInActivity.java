package com.aige.loveproduction.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.ui.adapter.ImageSelectAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.base.BaseDialog;
import com.aige.loveproduction.bean.ToFillInAsk;
import com.aige.loveproduction.bean.ToFillInBean;
import com.aige.loveproduction.mvp.contract.ToFillInContract;
import com.aige.loveproduction.mvp.presenter.ToFillInPresenter;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.dialogin.LoadingDialog;
import com.aige.loveproduction.ui.dialogin.MessageDialog;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.IOUtil;
import com.aige.loveproduction.util.IntentUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToFillInActivity extends BaseActivity<ToFillInPresenter, ToFillInContract.View>
        implements ToFillInContract.View, StatusAction, BaseAdapter.OnChildClickListener,
        ImageSelectActivity.OnPhotoSelectListener{
    private Spinner type,cause,post_responsibility;
    private EditText barcode,plate_name,materials,mat_name,f_leng,f_width,thk,area,price,amount,discoverer,responsible,find_edit,remark;
    private Button submit_button;
    private ImageView image_camera,find_img;
    private RelativeLayout data_body;
    private ToFillInAsk ask;
    private LoadingDialog.Builder loadingDialog;
    private WrapRecyclerView select_images;
    private ImageSelectAdapter image_adapter;

    @Override
    protected ToFillInPresenter createPresenter() {
        return new ToFillInPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_to_fill_in;
    }

    @Override
    protected void initView() {
        select_images = findViewById(R.id.select_images);
        plate_name = findViewById(R.id.plate_name);
        image_camera = findViewById(R.id.image_camera);
        find_img = findViewById(R.id.find_img);
        find_edit = findViewById(R.id.find_edit);
        remark = findViewById(R.id.remark);
        submit_button = findViewById(R.id.submit_button);
        barcode = findViewById(R.id.barcode);
        materials = findViewById(R.id.materials);
        mat_name = findViewById(R.id.mat_name);
        f_leng = findViewById(R.id.f_leng);
        f_width = findViewById(R.id.f_width);
        thk = findViewById(R.id.thk);
        area = findViewById(R.id.area);
        price = findViewById(R.id.price);
        amount = findViewById(R.id.amount);
        discoverer = findViewById(R.id.discoverer);
        responsible = findViewById(R.id.responsible);
        type = findViewById(R.id.type);
        cause = findViewById(R.id.cause);
        post_responsibility = findViewById(R.id.post_responsibility);
        data_body = findViewById(R.id.data_body);
    }

    @Override
    protected void initData() {
        setCenterTitle("内改补数据采集");
        data_body.setVisibility(View.GONE);
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
        setOnClickListener(image_camera,find_img,submit_button);
        setFooterView();
    }

    private void setFooterView(){
        GridLayoutManager manager = new GridLayoutManager(ToFillInActivity.this,3);
        select_images.setLayoutManager(manager);
        select_images.setAdapter(null);
        View view = select_images.addFooterView(R.layout.image_select_footer);
        setOnClickListener(view);
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
            }
            return;
        }else if(requestCode == 2) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageSelectActivity.start(ToFillInActivity.this, 1, this);
            }
            return;
        }
        new MessageDialog.Builder(this)
                .setTitle("温馨提醒")
                .setMessage("权限拒绝后某些功能将不能使用，为了使用完整功能请打开"+permission.getPermissionHint(Arrays.asList(permissions)))
                .setConfirm("去开启")
                .setListener(dialog -> IntentUtils.gotoPermission(this))
                .show();
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
            if(examineData(false)) {
                ToFillInAsk ask = getAsk();
                System.out.println(ask);
                mPresenter.submitData(ask);
            }else{
                showToast("请填写完整");
            }

        }else if(id == R.id.image_select_footer) {
            permission.applyPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new Permission.ApplyListener() {
                @Override
                public void apply(String[] permission) {
                    requestPermissions(permission,2);
                }

                @Override
                public void applySuccess() {
                    ImageSelectActivity.start(ToFillInActivity.this, 5, ToFillInActivity.this);
                }
            });
        }
    }
    //检查数据是否填写完整,并且是否清除文本框中的内容
    private boolean examineData(boolean isClear) {
        boolean flag = true;
        int childCount = data_body.getChildCount();
        for (int i = 0; i < childCount;i++) {
            View childAt = data_body.getChildAt(i);
            if(childAt instanceof EditText) {
                EditText text = ((EditText) childAt);
                if(isClear) text.setText("");
                if(text.getId() == R.id.responsible || text.getId() == R.id.remark) continue;
                if(text.getText().toString().isEmpty()) {
                    text.requestFocus();
                    flag = false;
                }

            }
        }
        return flag;
    }
    private void requestReady(String input){
        find_edit.setText("");
        hideKeyboard(find_edit);
        if(input.isEmpty()) {
            showToast("请输入条码");
        }else{
            mPresenter.getToFillInData(input);
        }
    }
    @Override
    public void showLoading() {
        examineData(true);
        showLoadings();
    }

    @Override
    public void showLoading(String method) {
        if("submitData".equals(method)) {
            loadingDialog = new LoadingDialog.Builder(this);
            loadingDialog.setTitle("正在提交数据...")
                    .setCancel(null)
                    .setConfirm("取消")
                    .setListener(new LoadingDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            mPresenter.dispose();
                        }
                    })
                    .setCanceledOnTouchOutside(false)
                    .show();
        }
    }

    @Override
    public void hideLoading() {
        data_body.setVisibility(View.VISIBLE);
        data_body.setAnimation(AnimationUtils.loadAnimation(this,R.anim.ios_in_window));
        showComplete();
    }

    @Override
    public void hideLoading(String method) {
        if("submitData".equals(method)) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onError(String message) {
        showToast(message);
        soundUtils.playSound(1,0);
    }

    @Override
    public void onError(String method, String message) {
        showToast(message);
        soundUtils.playSound(1,0);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }

    @Override
    public void onGetToFillInDataSuccess(ToFillInBean bean) {
        soundUtils.playSound(0,0);
        if(bean.getBarEntity() != null) {
            ToFillInBean.BarEntity barEntity = bean.getBarEntity();
            barcode.setText(barEntity.getBarcode());
            plate_name.setText(barEntity.getDetailName());
            materials.setText(barEntity.getMatProducer());
            mat_name.setText(barEntity.getMatname());
            f_leng.setText(String.valueOf(barEntity.getFleng()));
            f_width.setText(String.valueOf(barEntity.getFwidth()));
            thk.setText(String.valueOf(barEntity.getThk()));
            area.setText(String.valueOf(barEntity.getArea()));
            price.setText(String.valueOf(barEntity.getUnitPrice()));
            amount.setText(String.valueOf(barEntity.getTotal()));
        }
        SpinnerAdapter adapter = null;
        //原因
        adapter = new ArrayAdapter<>(this, R.layout.spinner_select_2,bean.getReasonListStr());
        cause.setAdapter(adapter);
        //改补类型
        adapter = new ArrayAdapter<>(this, R.layout.spinner_select_2,bean.getCategoryListStr());
        type.setAdapter(adapter);
        //责任岗位
        adapter = new ArrayAdapter<>(this, R.layout.spinner_select_2,bean.getDepartmentListStr());
        post_responsibility.setAdapter(adapter);
    }
    private ToFillInAsk getAsk(){
        ask = new ToFillInAsk();
        ask.setBarCode(barcode.getText().toString());
        ask.setMat(materials.getText().toString());
        ask.setName(mat_name.getText().toString());
        ask.setFleng(f_leng.getText().toString());
        ask.setFwidth(f_width.getText().toString());
        ask.setThk(thk.getText().toString());
        ask.setArea(area.getText().toString());
        ask.setUnitPrice(price.getText().toString());
        ask.setTotal(amount.getText().toString());
        //类型
        ask.setCategory(type.getSelectedItem().toString());
        //部门
        ask.setDepartment(post_responsibility.getSelectedItem().toString());
        //原因
        ask.setReason(cause.getSelectedItem().toString());
        ask.setFinder(discoverer.getText().toString());
        ask.setOperator(responsible.getText().toString());
        ask.setColor(mat_name.getText().toString());
        ask.setDetailName(plate_name.getText().toString());
        ask.setRemak(remark.getText().toString());
        ArrayList<String> list = new ArrayList<>();
        select_image_list.forEach(v ->{
            list.add(IOUtil.imageToBase64(v));
        });
        ask.setImgBase64(list);
        return ask;
    }

    @Override
    public void onSubmitDataSuccess() {
        showToast("数据已提交");
        data_body.setVisibility(View.GONE);
        soundUtils.playSound(0,0);
        select_images.setAdapter(null);
        select_image_list.clear();
    }

    private List<String> select_image_list = new ArrayList<>();
    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        if (childView.getId() == R.id.fl_image_select_check) {
            String path = image_adapter.getItemData(position);
            select_image_list.remove(path);
            image_adapter.setData(select_image_list);
        }
    }

    @Override
    public void onSelected(List<String> data) {
        select_image_list = data;
        image_adapter = new ImageSelectAdapter(ToFillInActivity.this,data);
        image_adapter.setChecked(false);
        image_adapter.setData(data);
        image_adapter.setOnChildClickListener(R.id.fl_image_select_check, ToFillInActivity.this);
        GridLayoutManager manager = new GridLayoutManager(ToFillInActivity.this,3);
        select_images.setLayoutManager(manager);
        select_images.setAdapter(image_adapter);
    }
}