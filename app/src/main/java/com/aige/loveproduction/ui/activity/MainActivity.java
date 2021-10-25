package com.aige.loveproduction.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aige.loveproduction.R;
import com.aige.loveproduction.adapter.FragmentPagerAdapter;
import com.aige.loveproduction.animation.AnimationInterpolator;
import com.aige.loveproduction.animation.BaseAnimation;
import com.aige.loveproduction.manager.ActivityManager;
import com.aige.loveproduction.ui.fragment.HomeFragment;
import com.aige.loveproduction.ui.fragment.UserFragment;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private RelativeLayout home_lay,user_lay;
    private LinearLayout below_lay;//底部按钮栏
    private TextView person_text,home_text,toolbar_text;
    private ImageView home_img,user_img;
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
        home_text = findViewById(R.id.home_text);
        person_text = findViewById(R.id.person_text);
        below_lay = findViewById(R.id.below_lay);
        home_lay = findViewById(R.id.home_layout);
        user_lay = findViewById(R.id.user_layout);
        home_img = findViewById(R.id.home_img);
        user_img = findViewById(R.id.user_img);
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
        setListener();//设置监听对象
        fragmentPagerAdapter = new FragmentPagerAdapter<>(this);
        fragmentPagerAdapter.addFragment(new HomeFragment());
        fragmentPagerAdapter.addFragment(new UserFragment());
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        animation = new BaseAnimation();
        setSelectedStatus(0);

    }
    //对底部按钮设置监听事件
    private void setListener() {
        for(int i=0;i<below_lay.getChildCount();i++) {//获取子布局数量
            below_lay.getChildAt(i).setOnClickListener(this);//获取子布局并设置
        }
    }
    //底部点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.home_layout) {
            mViewPager.setCurrentItem(0);
        }else if(id == R.id.user_layout) {
            mViewPager.setCurrentItem(1);
        }
    }
    //清除选中状态
    private void clearStatus() {
        home_text.setTextColor(getColor(R.color.black));
        person_text.setTextColor(getColor(R.color.black));
        home_img.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home_image_off));
        user_img.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.user_image_off));
    }
    //标题及其底部按钮状态
    private void setSelectedStatus(int index) {
        switch (index) {
            case 0 :
                home_lay.setSelected(true);
                home_text.setTextColor(Color.parseColor("#0099FF"));
                toolbar_text.setText("首页");
                home_img.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home_image_on));
                animation.scaleXYAnimation(home_img,0.5f,1f,1000);
                break;
            case 1 :
                user_lay.setSelected(true);
                person_text.setTextColor(Color.parseColor("#0099FF"));
                toolbar_text.setText("个人中心");
                user_img.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.user_image_on));
                animation.scaleXYAnimation(user_img,0.5f,1f,1000);
                break;
            default:
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        clearStatus();
        switch (position) {
            case 0:
                setSelectedStatus(0);
                break;
            case 1:
                setSelectedStatus(1);
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
}