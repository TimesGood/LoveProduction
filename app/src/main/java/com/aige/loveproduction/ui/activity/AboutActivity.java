package com.aige.loveproduction.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.ui.adapter.UserCenterAdapter;
import com.aige.loveproduction.ui.customui.viewgroup.WrapRecyclerView;
import com.aige.loveproduction.base.AppActivity;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.bean.UserCenterBean;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppActivity implements BaseAdapter.OnItemClickListener {
    private TextView versions;
    private WrapRecyclerView recyclerview_data;
    private UserCenterAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    protected void initView() {

        versions = findViewById(R.id.versions);
        recyclerview_data = findViewById(R.id.recyclerview_data);

    }
    @Override
    protected void initData() {
        setCenterTitle("关于我们");
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),0);//获取程序包信息，获取程序包有时获取不到，所以要异常处理，
            versions.setText("Version "+info.versionName);//设置控件显示的文本，versionName是版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versions.setText("V");//获取不到程序包信息时，直接显示V
        }
        setSelectItem();
    }

    //id与资源id二维数组
    private final int[][] ids = new int[][]{
            {
                    0
            },
            {
                    R.drawable.problem
            },
    };
    private final String[] text = new String[]{
            "遇到问题"
    };
    private void setSelectItem() {
        adapter = new UserCenterAdapter(this);
        List<UserCenterBean> list = new ArrayList<>();
        for (int i = 0;i < ids[0].length;i++) {
            UserCenterBean bean = new UserCenterBean();
            bean.setId(ids[0][i]);
            bean.setLeft_img_id(ids[1][i]);
            bean.setRight_img_id(R.drawable.right_arrow);
            bean.setSelect_text(text[i]);
            list.add(bean);
        }
        adapter.setOnItemClickListener(this);
        adapter.setData(list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        Intent intent = null;
        if(itemView.getId() == 0) {
            intent = new Intent(this,ProblemActivity.class);
        }
        if(intent != null ) startActivity(intent);
    }
}