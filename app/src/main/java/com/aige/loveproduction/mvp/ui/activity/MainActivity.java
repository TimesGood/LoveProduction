package com.aige.loveproduction.mvp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.ActivityAction;
import com.aige.loveproduction.adapter.FragmentPagerAdapter;
import com.aige.loveproduction.animation.BaseAnimation;
import com.aige.loveproduction.manager.ActivityManager;
import com.aige.loveproduction.mvp.ui.fragment.UserFragment;
import com.aige.loveproduction.mvp.ui.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ActivityAction {
    private TextView toolbar_text;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    //动画
    private BaseAnimation animation;
    private FragmentPagerAdapter<Fragment> fragmentPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void bindViews() {
        toolbar = findViewById(R.id.toolbar_title);
        toolbar_text = findViewById(R.id.toolbar_text);
        mViewPager = findViewById(R.id.main_body);
    }

    //初始化界面
    private void initView() {
        bindViews();
        toolbar.setTitle("");
        toolbar.setBackground(ContextCompat.getDrawable(this,R.color.blue));
        setSupportActionBar(toolbar);

        fragmentPagerAdapter = new FragmentPagerAdapter<>(this);
        fragmentPagerAdapter.addFragment(new HomeFragment());
        fragmentPagerAdapter.addFragment(new UserFragment());
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        toolbar_text.setText("首页");
        animation = new BaseAnimation();

        BottomNavigationView view = findViewById(R.id.home_navigation);
        view.setItemIconTintList(null);
        Menu menu = view.getMenu();
        MenuItem item0 = menu.findItem(R.id.home);
        item0.setIcon(R.drawable.home_image_on);
        MenuItem item1 = menu.findItem(R.id.user);
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int itemId = item.getItemId();
                View viewById1 = findViewById(R.id.home);
                View viewById2 = findViewById(R.id.user);
                if(itemId == R.id.home) {
                    animation.scaleXYAnimation(viewById1,0.5f,1f,1000);
                    mViewPager.setCurrentItem(0);
                    item.setIcon(R.drawable.home_image_on);
                    item1.setIcon(R.drawable.user_image_off);
                }else if(itemId == R.id.user) {
                    animation.scaleXYAnimation(viewById2,0.5f,1f,1000);
                    mViewPager.setCurrentItem(1);
                    item.setIcon(R.drawable.user_image_on);
                    item0.setIcon(R.drawable.home_image_off);
                }
                return true;
            }
        });
    }

    //Page页面切换监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                //setSelectedStatus(0);
                toolbar_text.setText("首页");
                break;
            case 1:
                //setSelectedStatus(1);
                toolbar_text.setText("个人中心");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

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
    public Context getContext() {
        return this;
    }
}