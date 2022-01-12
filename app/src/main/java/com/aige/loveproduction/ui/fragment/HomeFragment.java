package com.aige.loveproduction.ui.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.SharePreferencesAction;
import com.aige.loveproduction.base.AppFragment;
import com.aige.loveproduction.ui.activity.MprActivity;
import com.aige.loveproduction.ui.activity.MainActivity;
import com.aige.loveproduction.ui.adapter.HomeAdapter;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.bean.HomeBean;
import com.aige.loveproduction.enums.PermissionEnum;
import com.aige.loveproduction.ui.activity.CreateTaskActivity;
import com.aige.loveproduction.ui.activity.InStorageActivity;
import com.aige.loveproduction.ui.activity.MixedLotActivity;
import com.aige.loveproduction.ui.activity.MoveStorageActivity;
import com.aige.loveproduction.ui.activity.OutStorageActivity;
import com.aige.loveproduction.ui.activity.PlateFindActivity;
import com.aige.loveproduction.ui.activity.SendOutActivity;
import com.aige.loveproduction.ui.activity.SendOutVerifyActivity;
import com.aige.loveproduction.ui.activity.SpecialShapedActivity;
import com.aige.loveproduction.ui.activity.StorageFindActivity;
import com.aige.loveproduction.ui.activity.ToFillInActivity;
import com.aige.loveproduction.ui.activity.TransferActivity;
import com.aige.loveproduction.ui.activity.TransferVerifyActivity;
import com.aige.loveproduction.ui.activity.TransfersActivity;
import com.aige.loveproduction.ui.activity.WorkScanActivity;
import com.aige.loveproduction.ui.activity.PlanNoScanActivity;
import com.aige.loveproduction.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面
 */
public class HomeFragment extends AppFragment<MainActivity> implements BaseAdapter.OnItemClickListener{
    private RecyclerView recyclerview_data;
    private HomeAdapter adapter;
    private List<RelativeLayout> listView = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.homefragment;
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
                    16,17
            },
            {
                    R.drawable.plan_scan_img,R.drawable.plate_img,R.drawable.work_scan,R.drawable.transfer,
                    R.drawable.special,R.drawable.mixed, R.drawable.storage, R.drawable.in_storage_img,
                    R.drawable.out_storage_img,R.drawable.send_out_img,R.drawable.send_out_verify_img, R.drawable.move_storage_img,
                    R.drawable.transfer,R.drawable.transfer_verify,R.drawable.apply_img,R.drawable.print_img,
                    R.drawable.to_fill_in,R.drawable.apply_img
            }
    };
    private final String[] text = new String[]{
            "批次扫描","板件查询","工单扫描","转运扫描",
            "异形板件扫描","混批扫描","库位查询","入库扫描",
            "出库扫描","发货扫描","发货验证","移库扫描",
            "转运扫描+","转运验证","孔图查看","无纸化打印",
            "内改补","功能测试"
    };
    /**
     * 根据角色显示对应的界面
     */
    private void showRoles() {
        List<HomeBean> list = new ArrayList<>();
        String roleName = SharedPreferencesUtils.getValue(getAttachActivity(), SharePreferencesAction.LoginInfo, "roleName");
        if(PermissionEnum.Administrator.getMessage().equals(roleName)) {
            list = setData(text);
        }else if(PermissionEnum.Distributor.getMessage().equals(roleName)) {

        }else if(PermissionEnum.Workshop.getMessage().equals(roleName)) {
            String[] function = {"批次扫描","板件查询","工单扫描","转运扫描","转运扫描+", "异形板件扫描","混批扫描","孔图查看","内改补"};
            list = setData(function);
        }else if(PermissionEnum.Storage.getMessage().equals(roleName)) {
            String[] function = {"库位查询","入库扫描", "出库扫描","发货扫描","发货验证","移库扫描","转运验证"};
            list = setData(function);
        }else{
            //老是有人来提说什么主页不显示任何功能菜单的情况，权限也不整理规划一下，算了，没有在清单内的权限就全部放开吧，爱咋咋滴
            list = setData(text);
        }
        recyclerview_data = findViewById(R.id.recyclerview_data);
        adapter = new HomeAdapter(getAttachActivity());
        adapter.setOnItemClickListener(this);
        adapter.setData(list);
        GridLayoutManager manager = new GridLayoutManager(getAttachActivity(),4);
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
            if("无纸化打印".equals(s)) continue;
            if("功能测试".equals(s)) continue;
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
        switch (itemView.getId()) {
            case 0:
                startActivity(PlanNoScanActivity.class);
                break;
            case 1:
                startActivity(PlateFindActivity.class);
                break;
            case 2:
                startActivity(WorkScanActivity.class);
                break;
            case 3:
                startActivity(TransferActivity.class);
                break;
            case 4:
                startActivity(SpecialShapedActivity.class);
                break;
            case 5:
                startActivity(MixedLotActivity.class);
                break;
            case 6:
                startActivity(StorageFindActivity.class);
                break;
            case 7:
                startActivity(InStorageActivity.class);
                break;
            case 8:
                startActivity(OutStorageActivity.class);
                break;
            case 9:
                startActivity(SendOutActivity.class);
                break;
            case 10:
                startActivity(SendOutVerifyActivity.class);
                break;
            case 11:
                startActivity(MoveStorageActivity.class);
                break;
            case 12 :
                startActivity(TransfersActivity.class);
                break;
            case 13 :
                startActivity(TransferVerifyActivity.class);
                break;
            case 14 :
                startActivity(MprActivity.class);
                break;
            case 15:
                startActivity(CreateTaskActivity.class);
                break;
            case 16:
                startActivity(ToFillInActivity.class);
                break;
            case 17:
                startActivity(MprActivity.class);
        }
    }
}
