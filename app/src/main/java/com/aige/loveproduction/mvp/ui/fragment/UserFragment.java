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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.UserCenterAdapter;
import com.aige.loveproduction.adapter.WrapRecyclerView;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.bean.HomeBean;
import com.aige.loveproduction.bean.UserCenterBean;
import com.aige.loveproduction.manager.ActivityManager;
import com.aige.loveproduction.mvp.ui.activity.AboutActivity;
import com.aige.loveproduction.mvp.ui.activity.FactorySettingsActivity;
import com.aige.loveproduction.mvp.ui.activity.ExamineActivity;
import com.aige.loveproduction.mvp.ui.activity.LoginActivity;
import com.aige.loveproduction.mvp.ui.activity.SSSettingActivity;
import com.aige.loveproduction.mvp.ui.activity.TransfersActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * 个人中心界面
 */
public class UserFragment extends Fragment implements View.OnClickListener, BaseAdapter.OnItemClickListener {
    private View view;
    private Activity activity;
    private RelativeLayout logout;
    private TextView textUser;
    private ImageView image_user;
    private WrapRecyclerView recyclerview_data;
    private UserCenterAdapter adapter;

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
    //id与资源id二维数组
    private final int[][] ids = new int[][]{
            {
                0,1,2,3,
            },
            {
                R.drawable.setting_image,R.drawable.examine_image,R.drawable.examine_image,R.drawable.about_image
            },
    };
    private final String[] text = new String[]{
            "工厂设置","检验混单","异形工序设置","关于我们",
    };
    //初始化
    private void initView() {
        textUser = view.findViewById(R.id.textUser);
        logout = view.findViewById(R.id.logout);
        image_user = view.findViewById(R.id.image_user);
        recyclerview_data = view.findViewById(R.id.recyclerview_data);

        logout.setOnClickListener(this);
        image_user.setOnClickListener(this);
        textUser.setText(SharedPreferencesUtils.getValue(activity,"loginInfo","userName"));
        setSelectItem();
    }
    //设置选项列表
    private void setSelectItem() {
        adapter = new UserCenterAdapter(activity);
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
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
    }
    //查看登录状态
    private boolean getLoginStatus() {
        return SharedPreferencesUtils.getBoolean(activity,"loginInfo","isLogin");
    }
    //点击事件
    @Override
    public void onClick(View v) {
        Intent intent;
        int id = v.getId();
        if(id == R.id.logout) {
            SharedPreferencesUtils.saveSetting(activity,"loginInfo","isLogin",false);
            intent = new Intent(activity,LoginActivity.class);
            activity.startActivityForResult(intent,1);
            ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
        }else if(id == R.id.image_user) {
            if(getLoginStatus()) {
                Toast.makeText(activity,"切换账号请先退出当前账号",Toast.LENGTH_SHORT).show();
            }else{
                intent = new Intent(activity,LoginActivity.class);
                activity.startActivityForResult(intent,1);
                ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
            }
        }
    }
    //列表点击事件
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (!getLoginStatus()) {
            Toast.makeText(activity,"请登录账号进行操作",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent;
        switch (itemView.getId()) {
            case 0:
                intent = new Intent(activity, FactorySettingsActivity.class);
                activity.startActivity(intent);
                break;
            case 1:
                intent = new Intent(activity, ExamineActivity.class);
                activity.startActivity(intent);
                break;
            case 2:
                intent = new Intent(activity, SSSettingActivity.class);
                activity.startActivity(intent);
                break;
            case 3:
                intent = new Intent(activity, AboutActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}
