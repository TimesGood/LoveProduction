package com.aige.loveproduction.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.ActivityAction;
import com.aige.loveproduction.action.BundleAction;
import com.aige.loveproduction.action.ClickAction;
import com.aige.loveproduction.action.KeyboardAction;
import com.aige.loveproduction.action.SharePreferencesAction;
import com.aige.loveproduction.action.TitleBarAction;
import com.aige.loveproduction.animation.BaseAnimation;
import com.aige.loveproduction.permission.Permission;
import com.aige.loveproduction.util.SoundUtils;
import com.google.android.material.appbar.AppBarLayout;

/**
 * 业务基类
 * 只是基于AppCompatActivity的封装
 * 当没有网络请求时，使用这个
 */

public abstract class AppActivity extends AppCompatActivity
        implements
        BundleAction,
        ActivityAction,
        ClickAction,
        KeyboardAction,
        TitleBarAction,
        SharePreferencesAction {
    protected Toolbar toolbar;//标题栏
    protected SoundUtils soundUtils;//声音管理
    private Toast toast;//Toast广播
    protected Permission permission;//权限申请
    protected BaseAnimation mAnimation;//动画
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //锁定竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        initLayout();
        initView();
        initData();
        initSDK();
    }

    /**
     * 对Toast广播进行一些初始化的改造，把广播通知带有的软件名擦掉
     */
    public void showToast(String msg) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 设置layout布局
     * @return layout Id
     */
    @LayoutRes
    protected abstract int getLayoutId();
    /**
     * 初始化布局
     */
    private void initLayout() {
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            initToolbar();
        }
    }
    /**
     * 获取标题
     */
    private void initToolbar() {
        toolbar = getToolbar();
        if(toolbar == null) return;
        hideTitle();
        setLeftIcon(R.drawable.back);
        setSupportActionBar(toolbar);
    }
    /**
     * 初始化视图
     */
    protected abstract void initView();
    protected abstract void initData();
    /**
     * 初始化一些类或第三方库
     */
    private void initSDK() {
        //初始化音乐库
        soundUtils = new SoundUtils(this,SoundUtils.MEDIA_SOUND);
        soundUtils.putSound(0,R.raw.ok);
        soundUtils.putSound(1,R.raw.no);
        if(permission == null) {
            permission = new Permission(this);
        }
        if(mAnimation == null) {
            mAnimation = new BaseAnimation();
        }
    }
    /**
     * 获取界面中的Toolbar
     */
    private Toolbar getToolbar() {
        ViewGroup contentView = getContentView();
        ViewGroup v = (ViewGroup) contentView.getChildAt(0);
        for (int i = 0;i < v.getChildCount();i++) {
            if(v.getChildAt(i) instanceof Toolbar) {
                return (Toolbar) v.getChildAt(i);
            }
            if(v.getChildAt(i) instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) v.getChildAt(i);
                for(int j = 0;j < appBarLayout.getChildCount();j++) {
                    if(appBarLayout.getChildAt(j) instanceof Toolbar) {
                        return (Toolbar) appBarLayout.getChildAt(j);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取当前Activity视图
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }

    /**
     * 重写菜单监听，监听几乎每个页面都有的返回按钮
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            hideKeyboard(getCurrentFocus());
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent);
    }
    /**
     * 解除绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(permission != null) {
            permission = null;
        }
    }
    //*********************************意图接口需要的******************************************
    /**
     * ActivityAction意图需要的Context
     */
    @Override
    public Context getContext() {
        return this;
    }
    /**
     * BundleAction接口需要的Bundle
     */
    @Override
    public Bundle getBundle() {
        return getIntent().getExtras();
    }
    /**
     * 获取标题栏，Toolbar的Id必须是toolbar_title，否则获取不到
     */
    @Override
    @Nullable
    public Toolbar getTitleBar() {
        return toolbar;
    }

    //******************************对页面跳转封装***************************************
    private SparseArray<OnActivityCallback> mActivityCallbacks;
    public void startActivityForResult(Intent intent, OnActivityCallback callback) {
        startActivityForResult(intent, 1, callback);
    }
    public void startActivityForResult(Intent intent, int requestCode, OnActivityCallback callback) {
        //只存储一个监听
        if (mActivityCallbacks == null) {
            mActivityCallbacks = new SparseArray<>(1);
        }
        mActivityCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //拿到监听
        OnActivityCallback callback;
        if (mActivityCallbacks != null && (callback = mActivityCallbacks.get(requestCode)) != null) {
            //交给监听回调
            callback.onActivityResult(resultCode, data);
            //之后把该监听删除了
            mActivityCallbacks.remove(requestCode);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //监听页面回调
    public interface OnActivityCallback {

        /**
         * 结果回调
         *
         * @param resultCode        结果码
         * @param data              数据
         */
        void onActivityResult(int resultCode, @Nullable Intent data);
    }
}
