package com.aige.loveproduction.mvp.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aige.loveproduction.R;
import com.aige.loveproduction.manager.ActivityManager;
import com.aige.loveproduction.mvp.ui.activity.FactorySettingsActivity;
import com.aige.loveproduction.mvp.ui.activity.ExamineActivity;
import com.aige.loveproduction.mvp.ui.activity.LoginActivity;
import com.aige.loveproduction.mvp.ui.activity.SSSettingActivity;
import com.aige.loveproduction.mvp.ui.activity.TransfersActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

/**
 * 个人中心界面
 */
public class UserFragment extends Fragment implements View.OnClickListener{
    private View view;

    private Activity activity;
    private RelativeLayout settings,logout,layout_test,about_us,examine_layout,special_shaped_layout;
    private TextView textUser,tv_version;
    private ImageView image_user;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = requireActivity();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.userfragment,container,false);
        initView();
        return view;
    }
    //初始化
    private void initView() {
        textUser = view.findViewById(R.id.textUser);
        settings = view.findViewById(R.id.settings);//工厂设置
        logout = view.findViewById(R.id.logout);
        layout_test = view.findViewById(R.id.layout_test);
        image_user = view.findViewById(R.id.image_user);
        tv_version = view.findViewById(R.id.tv_version);
        about_us = view.findViewById(R.id.about_us);
        examine_layout = view.findViewById(R.id.examine_layout);
        special_shaped_layout = view.findViewById(R.id.special_shaped_layout);

        setLoginParams(readLoginStatus());//设置登录用户名
        view.setVisibility(View.VISIBLE);
        //工厂设置
        settings.setOnClickListener(this);
        //检验混单
        examine_layout.setOnClickListener(this);
        //测试界面
        layout_test.setOnClickListener(this);
        //layout_test.setVisibility(View.GONE);
        //退出登录
        logout.setOnClickListener(this);
        image_user.setOnClickListener(this);
        special_shaped_layout.setOnClickListener(this);

        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(),0);//获取程序包信息，获取程序包有时获取不到，所以要异常处理，
            tv_version.setText("V"+info.versionName);//设置控件显示的文本，versionName是版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tv_version.setText("V");//获取不到程序包信息时，直接显示V
        }

        about_us.setOnClickListener(this);
    }
    public void setLoginParams(boolean isLogin) {
        if(isLogin) {
            textUser.setText(SharedPreferencesUtils.readLoginUserName(activity));
        }else {
            textUser.setText("点击头像登录");
        }
    }

    //查看登录状态
    private boolean readLoginStatus() {
        SharedPreferences sp = activity.getSharedPreferences("loginInfo", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);//查到则返回true否则false
        return isLogin;
    }
    //修改登录状态
    private void updateStatus() {
        SharedPreferences sp = activity.getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isLogin",false);
        edit.commit();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.settings:
                //查看登录状态
                if(readLoginStatus()) {
                    intent = new Intent(activity, FactorySettingsActivity.class);
                    activity.startActivityForResult(intent,1);
                }else {
                    intent = new Intent(activity,LoginActivity.class);
                    activity.startActivityForResult(intent,1);
                    activity.finish();
                    Toast.makeText(activity,"您还未登录，请先登录！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.examine_layout:
                if(readLoginStatus()) {
                    intent = new Intent(activity, ExamineActivity.class);
                    activity.startActivity(intent);
                }else {
                    intent = new Intent(activity,LoginActivity.class);
                    activity.startActivityForResult(intent,1);
                    activity.finish();
                    Toast.makeText(activity,"您还未登录，请先登录！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.logout:
                if(readLoginStatus()) {
                    updateStatus();
                    intent = new Intent(activity,LoginActivity.class);
                    activity.startActivityForResult(intent,1);
                    ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
                } else {
                    Toast.makeText(activity,"您还未登录！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_user:
                if(SharedPreferencesUtils.getBoolean(activity,"loginInfo","isLogin")) {
                    Toast.makeText(activity,"切换账号请先退出当前账号",Toast.LENGTH_SHORT).show();
                }else{
                    intent = new Intent(activity,LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                break;
            case R.id.about_us:
                break;
            case R.id.special_shaped_layout:
                intent = new Intent(activity, SSSettingActivity.class);
                activity.startActivity(intent);
                break;
                //测试界面
            case R.id.layout_test:
                intent = new Intent(activity, TransfersActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}
