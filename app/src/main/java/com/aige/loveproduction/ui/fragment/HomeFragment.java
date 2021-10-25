package com.aige.loveproduction.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aige.loveproduction.R;
import com.aige.loveproduction.ui.activity.InStorageActivity;
import com.aige.loveproduction.ui.activity.MoveStorageActivity;
import com.aige.loveproduction.ui.activity.OutStorageActivity;
import com.aige.loveproduction.ui.activity.PlanNoScanActivity;
import com.aige.loveproduction.ui.activity.PlateFindActivity;
import com.aige.loveproduction.ui.activity.SendOutActivity;
import com.aige.loveproduction.ui.activity.SendOutVerifyActivity;
import com.aige.loveproduction.ui.activity.StorageFindActivity;
import com.aige.loveproduction.ui.activity.TransferActivity;
import com.aige.loveproduction.ui.activity.WorkScanActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Activity activity;
    private RelativeLayout workScan,plate_find,planNO_scan,storage_find,
            in_storage,out_storage,send_out,send_out_verify,move_storage,transfer;

    private GridLayout grid_home;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = requireActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment, container, false);
        initView();
        return view;
    }

    public void initView() {
        workScan = view.findViewById(R.id.workScan);
        plate_find = view.findViewById(R.id.plate_find);
        planNO_scan = view.findViewById(R.id.planNo_scan);
        storage_find = view.findViewById(R.id.storage_find);//库位查询
        in_storage = view.findViewById(R.id.in_storage);//入库扫描
        out_storage = view.findViewById(R.id.out_storage);//出库扫描
        send_out = view.findViewById(R.id.send_out);//发货扫描
        send_out_verify = view.findViewById(R.id.send_out_verify);//发货验证
        move_storage = view.findViewById(R.id.move_storage);//移库扫描
        grid_home = view.findViewById(R.id.grid_home);
        transfer = view.findViewById(R.id.transfer);//转运扫描

        workScan.setOnClickListener(this);
        plate_find.setOnClickListener(this);
        planNO_scan.setOnClickListener(this);
        storage_find.setOnClickListener(this);
        in_storage.setOnClickListener(this);
        out_storage.setOnClickListener(this);
        send_out.setOnClickListener(this);
        send_out_verify.setOnClickListener(this);
        move_storage.setOnClickListener(this);
        transfer.setOnClickListener(this);
        showRole();

    }

    /**
     * 根据角色显示对应的界面
     */
    private void showRole() {
        List<View> viewList = new ArrayList<>();
        int childCount = grid_home.getChildCount();
        for(int i = 0;i < childCount;i++) {
            viewList.add(grid_home.getChildAt(i));
        }
        String roleName = SharedPreferencesUtils.getValue(activity, "loginInfo", "roleName");
        if("Administrator".equals(roleName)) {

        }else if("经销商".equals(roleName)) {
            grid_home.removeAllViews();
        }else if("车间".equals(roleName)) {
            grid_home.removeAllViews();
            for (int i = 0;i < 3;i++) {
                grid_home.addView(viewList.get(i));
            }
            grid_home.addView(viewList.get(viewList.size()-1));

        }else if("入库出库发货".equals(roleName)) {
            grid_home.removeAllViews();
            for (int i = 3;i < viewList.size()-1;i++) {
                grid_home.addView(viewList.get(i));
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.workScan:
                intent = new Intent(activity,WorkScanActivity.class);
                break;
            case R.id.plate_find:
                intent = new Intent(activity, PlateFindActivity.class);
                break;
            case R.id.planNo_scan:
                intent = new Intent(activity, PlanNoScanActivity.class);
                break;
            case R.id.storage_find:
                intent = new Intent(activity, StorageFindActivity.class);
                break;
            case R.id.in_storage:
                intent = new Intent(activity, InStorageActivity.class);
                break;
            case R.id.out_storage:
                intent = new Intent(activity, OutStorageActivity.class);
                break;
            case R.id.send_out:
                intent = new Intent(activity, SendOutActivity.class);
                break;
            case R.id.send_out_verify:
                intent = new Intent(activity, SendOutVerifyActivity.class);
                break;
            case R.id.move_storage:
                intent = new Intent(activity, MoveStorageActivity.class);

                break;
            case R.id.transfer:
                intent = new Intent(activity, TransferActivity.class);
                break;

        }
        activity.startActivity(intent);

    }
}
