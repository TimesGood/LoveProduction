package com.aige.loveproduction.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aige.loveproduction.R;
import com.aige.loveproduction.ui.adapter.FragmentPagerAdapter;
import com.aige.loveproduction.animation.BaseAnimation;
import com.aige.loveproduction.base.AppActivity;
import com.aige.loveproduction.manager.ActivityManager;
import com.aige.loveproduction.ui.fragment.ReportFragment;
import com.aige.loveproduction.ui.fragment.UserFragment;
import com.aige.loveproduction.ui.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

/**
 * 主页
 */
public class MainActivity extends AppActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewPager;
    //动画
    private BaseAnimation animation;
    private FragmentPagerAdapter<Fragment> fragmentPagerAdapter;
    private BottomNavigationView home_navigation;
    public static void start(Context context) {
        start(context, HomeFragment.class);
    }

    public static void start(Context context, Class<? extends Fragment> fragmentClass) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("index", fragmentClass);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    //初始化界面
    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.main_body);
    }

    @Override
    protected void initData() {
        setCenterTitle("首页");
        hideLeftIcon();
        fragmentPagerAdapter = new FragmentPagerAdapter<>(this);
        fragmentPagerAdapter.addFragment(new HomeFragment());
        fragmentPagerAdapter.addFragment(new ReportFragment());
        fragmentPagerAdapter.addFragment(new UserFragment());
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setCurrentItem(0);
        animation = new BaseAnimation();
        home_navigation = findViewById(R.id.home_navigation);
        home_navigation.setOnNavigationItemSelectedListener(this);
        home_navigation.setItemIconTintList(null);
        // 屏蔽底部导航栏长按文本提示
        Menu menu = home_navigation.getMenu();
        for (byte i = 0; i < menu.size(); i++) {
            home_navigation.findViewById(menu.getItem(i).getItemId()).setOnLongClickListener(v -> true);
        }
//        onNewIntent(getIntent());
    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        int index = fragmentPagerAdapter.getFragmentIndex(getSerializable("index"));
//        if(index == -1) return;
//        mViewPager.setCurrentItem(index);
//        switch (index) {
//            case 0:
//                home_navigation.setSelectedItemId(R.id.home);
//                setCenterTitle("首页");
//                break;
//            case 1:
//                home_navigation.setSelectedItemId(R.id.report);
//                setCenterTitle("报表");
//                break;
//            case 2:
//                home_navigation.setSelectedItemId(R.id.user);
//                setCenterTitle("个人中心");
//                break;
//        }
//    }

    //双击返回手机返回键，关闭软件
    protected long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000) {
                Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
                toast.setText("再按一次退出爱生产");
                toast.show();
                exitTime = System.currentTimeMillis();
            }else {
                ActivityManager.getInstance().finishAllActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int itemId = item.getItemId();
        View viewById = findViewById(itemId);
        if(itemId == R.id.home) {
            mViewPager.setCurrentItem(0);
            setCenterTitle("首页");
        }else if(itemId == R.id.report){
            mViewPager.setCurrentItem(1);
            setCenterTitle("报表");
        }else if(itemId == R.id.user) {
            mViewPager.setCurrentItem(2);
            setCenterTitle("个人中心");
        }
        animation.scaleXYAnimation(viewById,0.5f,1f,1000);
        return true;
    }
}