package com.aige.loveproduction.mvp.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.HomeAdapter;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.bean.HomeBean;
import com.aige.loveproduction.mvp.ui.activity.ApplyActivity;
import com.aige.loveproduction.mvp.ui.activity.CreateTaskActivity;
import com.aige.loveproduction.mvp.ui.activity.InStorageActivity;
import com.aige.loveproduction.mvp.ui.activity.MixedLotActivity;
import com.aige.loveproduction.mvp.ui.activity.MoveStorageActivity;
import com.aige.loveproduction.mvp.ui.activity.OutStorageActivity;
import com.aige.loveproduction.mvp.ui.activity.PlateFindActivity;
import com.aige.loveproduction.mvp.ui.activity.SendOutActivity;
import com.aige.loveproduction.mvp.ui.activity.SendOutVerifyActivity;
import com.aige.loveproduction.mvp.ui.activity.SpecialShapedActivity;
import com.aige.loveproduction.mvp.ui.activity.StorageFindActivity;
import com.aige.loveproduction.mvp.ui.activity.ToFillInActivity;
import com.aige.loveproduction.mvp.ui.activity.TransferActivity;
import com.aige.loveproduction.mvp.ui.activity.TransferVerifyActivity;
import com.aige.loveproduction.mvp.ui.activity.TransfersActivity;
import com.aige.loveproduction.mvp.ui.activity.WorkScanActivity;
import com.aige.loveproduction.mvp.ui.activity.PlanNoScanActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面
 */
public class HomeFragment extends Fragment implements BaseAdapter.OnItemClickListener{
    private View view;
    private Activity activity;
    private RecyclerView recyclerview_data;
    private HomeAdapter adapter;
    private List<RelativeLayout> listView = new ArrayList<>();

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
        showRoles();
    }
    //id与资源id二维数组
    private final int[][] ids = new int[][]{
            {
                    0,1,2,3,
                    4,5,6,7,
                    8,9,10,11,
                    12,13,14,15,
                    16
            },
            {
                    R.drawable.plan_scan_img,R.drawable.plate_img,R.drawable.work_scan,R.drawable.transfer,
                    R.drawable.special,R.drawable.mixed, R.drawable.storage, R.drawable.in_storage_img,
                    R.drawable.out_storage_img,R.drawable.send_out_img,R.drawable.send_out_verify_img, R.drawable.move_storage_img,
                    R.drawable.transfer,R.drawable.transfer_verify,R.drawable.apply_img,R.drawable.print_img,
                    R.drawable.apply_img
            }
    };
    private final String[] text = new String[]{
            "批次扫描","板件查询","工单扫描","转运扫描",
            "异形板件扫描","混批扫描","库位查询","入库扫描",
            "出库扫描","发货扫描","发货验证","移库扫描",
            "转运扫描+","转运验证","孔图查看","无纸化打印",
            "内改补"
    };
    /**
     * 根据角色显示对应的界面
     */
    private void showRoles() {
        List<HomeBean> list = new ArrayList<>();
        String roleName = SharedPreferencesUtils.getValue(activity, "loginInfo", "roleName");
        if("Administrator".equals(roleName)) {
            list = setData(text);
        }else if("经销商".equals(roleName)) {

        }else if("车间".equals(roleName)) {
            String[] function = {"批次扫描","板件查询","工单扫描","转运扫描","转运扫描+", "异形板件扫描","混批扫描","孔图查看"};
            list = setData(function);
        }else if("入库出库发货".equals(roleName)) {
            String[] function = {"库位查询","入库扫描", "出库扫描","发货扫描","发货验证","移库扫描","转运验证"};
            list = setData(function);
        }else{
            //老是有人来提说什么主页不显示任何功能的情况，权限也不整理规划一下，算了，没有在清单内的权限就全部放开吧，爱咋咋滴
            list = setData(text);
        }
        recyclerview_data = view.findViewById(R.id.recyclerview_data);
        adapter = new HomeAdapter(activity);
        adapter.setOnItemClickListener(this);
        adapter.setData(list);
        GridLayoutManager manager = new GridLayoutManager(activity,4);
        recyclerview_data.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerview_data.setLayoutManager(manager);
        recyclerview_data.setAdapter(adapter);
    }

    /**
     * 筛选需要的功能菜单
     * @param arr 需要哪些功能菜单，自己加
     * @return 返回Recyclerview组件需要用data
     */
    private List<HomeBean> setData(String[] arr) {
        List<HomeBean> list = new ArrayList<>();
        for (String s : arr) {
            if("转运扫描".equals(s)) continue;
            for (int j = 0; j < text.length; j++) {
                if (s.equals(text[j])) {
                    HomeBean bean = new HomeBean();
                    bean.setId(ids[0][j]);
                    bean.setImg_id(ids[1][j]);
                    bean.setText(text[j]);
                    list.add(bean);
                    break;
                }
            }
        }
        return list;
    }
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        Intent intent = null;
        switch (itemView.getId()) {
            case 0:
                intent = new Intent(activity, PlanNoScanActivity.class);
                break;
            case 1:
                intent = new Intent(activity, PlateFindActivity.class);
                break;
            case 2:
                intent = new Intent(activity, WorkScanActivity.class);
                break;
            case 3:
                intent = new Intent(activity, TransferActivity.class);
                break;
            case 4:
                intent = new Intent(activity, SpecialShapedActivity.class);
                break;
            case 5:
                intent = new Intent(activity, MixedLotActivity.class);
                break;
            case 6:
                intent = new Intent(activity, StorageFindActivity.class);
                break;
            case 7:
                intent = new Intent(activity, InStorageActivity.class);
                break;
            case 8:
                intent = new Intent(activity, OutStorageActivity.class);
                break;
            case 9:
                intent = new Intent(activity, SendOutActivity.class);
                break;
            case 10:
                intent = new Intent(activity, SendOutVerifyActivity.class);
                break;
            case 11:
                intent = new Intent(activity, MoveStorageActivity.class);
                break;
            case 12 :
                intent = new Intent(activity, TransfersActivity.class);
                break;
            case 13 :
                intent = new Intent(activity, TransferVerifyActivity.class);
                break;
            case 14 :
                intent = new Intent(activity, ApplyActivity.class);
                break;
            case 15:
                intent = new Intent(activity, CreateTaskActivity.class);
                break;
            case 16:
                intent = new Intent(activity, ToFillInActivity.class);
                break;
        }
        if(intent != null) activity.startActivity(intent);
    }
}
