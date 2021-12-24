package com.aige.loveproduction.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.SharePreferencesAction;
import com.aige.loveproduction.base.AppFragment;
import com.aige.loveproduction.ui.activity.MainActivity;
import com.aige.loveproduction.ui.adapter.UserCenterAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.bean.UserCenterBean;
import com.aige.loveproduction.manager.ActivityManager;
import com.aige.loveproduction.ui.activity.AboutActivity;
import com.aige.loveproduction.ui.activity.ExamineActivity;
import com.aige.loveproduction.ui.activity.FactorySettingsActivity;
import com.aige.loveproduction.ui.activity.LoginActivity;
import com.aige.loveproduction.ui.activity.SSSettingActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心界面
 */
public class UserFragment extends AppFragment<MainActivity> implements View.OnClickListener, BaseAdapter.OnItemClickListener {
    private RelativeLayout logout;
    private TextView textUser;
    private ImageView image_user;
    private WrapRecyclerView recyclerview_data;
    private UserCenterAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.userfragment;
    }

    //id与资源id二维数组
    private final int[][] ids = new int[][]{
            {
                0,1,2,3,
            },
            {
                R.drawable.setting_image,R.drawable.examine_image,R.drawable.examine_image,R.drawable.about_image
            },
    };
    private static final String[] text = new String[]{
            "工厂设置","检验混单","异形工序设置","关于我们",
    };
    //初始化
    public void initView() {
        textUser = findViewById(R.id.textUser);
        logout = findViewById(R.id.logout);
        image_user = findViewById(R.id.image_user);
        recyclerview_data = findViewById(R.id.recyclerview_data);

        logout.setOnClickListener(this);
        image_user.setOnClickListener(this);
        textUser.setText(SharedPreferencesUtils.getValue(getAttachActivity(), SharePreferencesAction.LoginInfo,"userName"));
        setSelectItem();
    }
    //设置选项列表
    private void setSelectItem() {
        adapter = new UserCenterAdapter(getAttachActivity());
        List<UserCenterBean> list = new ArrayList<>();
        for(int i = 0;i < ids[0].length;i++) {
            UserCenterBean bean = new UserCenterBean();
            bean.setId(ids[0][i]);
            bean.setLeft_img_id(ids[1][i]);
            bean.setRight_img_id(R.drawable.right_arrow);
            bean.setSelect_text(text[i]);
            list.add(bean);
        }
        adapter.setOnItemClickListener(this);
        adapter.setData(list);
        LinearLayoutManager manager = new LinearLayoutManager(getAttachActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
    }
    //查看登录状态
    private boolean getLoginStatus() {
        return SharedPreferencesUtils.getBoolean(getAttachActivity(),SharePreferencesAction.LoginInfo,"isLogin");
    }
    //点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.logout) {
            SharedPreferencesUtils.saveSetting(getAttachActivity(),SharePreferencesAction.LoginInfo,"isLogin",false);
            startActivity(LoginActivity.class);
            ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
        }else if(id == R.id.image_user) {
            if(getLoginStatus()) {
                showToast("切换账号请先退出当前账号");
            }else{
                startActivity(LoginActivity.class);
                ActivityManager.getInstance().finishAllActivities(LoginActivity.class);

            }
        }
    }
    //列表点击事件
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (!getLoginStatus()) {
            showToast("请登录账号进行操作");
            return;
        }
        switch (itemView.getId()) {
            case 0:
                startActivity(FactorySettingsActivity.class);
                break;
            case 1:
                startActivity(ExamineActivity.class);
                break;
            case 2:
                startActivity(SSSettingActivity.class);
                break;
            case 3:
                startActivity(AboutActivity.class);
                break;
        }
    }
}
