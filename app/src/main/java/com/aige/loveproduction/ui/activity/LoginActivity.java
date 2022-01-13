package com.aige.loveproduction.ui.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.aige.loveproduction.animation.BaseAnimation;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.UserBean;
import com.aige.loveproduction.mvp.contract.LoginContract;
import com.aige.loveproduction.mvp.presenter.LoginPresenter;
import com.aige.loveproduction.base.BaseActivity;
import com.aige.loveproduction.ui.fragment.HomeFragment;
import com.aige.loveproduction.util.SharedPreferencesUtils;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity<LoginPresenter,LoginContract.View> implements LoginContract.View {
    private EditText editUserName,editPassword;
    private Button loginBtn;
    private LinearLayout login_layout,layout_progress;

    private BaseAnimation baseAnimation;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        loginBtn = findViewById(R.id.loginBtn);
        editUserName = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        login_layout = findViewById(R.id.login_layout);
        layout_progress = findViewById(R.id.layout_progress);

    }

    @Override
    protected void initData() {
        setCenterTitle("登录");
        hideLeftIcon();
        Drawable usernameDraw = ContextCompat.getDrawable(this,R.drawable.username_img);
        usernameDraw.setBounds(0, 0, 40, 40);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        editUserName.setCompoundDrawables(usernameDraw, null, null, null);
        Drawable passwordDraw = ContextCompat.getDrawable(this,R.drawable.password_img);
        passwordDraw.setBounds(0, 0, 40, 40);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        editPassword.setCompoundDrawables(passwordDraw, null, null, null);//只放左边
        ((TextView) findViewById(R.id.welcome_text)).setText("您好,\n爱阁服务欢迎您！");
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取文本信息
                String username = editUserName.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if(TextUtils.isEmpty(username)) {//如果用户名控件内没有元素
                    showToast("请输入用户名");
                }else if(TextUtils.isEmpty(password)) {//如果密码1控件内没有元素
                    showToast("请输入密码");
                }else {
                    mPresenter.login(username,password);
                }
            }
        });
        baseAnimation = new BaseAnimation();
    }

    /**
     * 登录请求成功
     * @param bean
     */
    @Override
    public void onLoginSuccess(BaseBean<UserBean> bean) {
        if(bean.getCode() == 0) {
            UserBean data = bean.getData();
            //登录成功后清空设置
            SharedPreferencesUtils.detailSetting(LoginActivity.this,WorkgroupSettings);
            SharedPreferencesUtils.detailSetting(LoginActivity.this,MachineSettings);
            SharedPreferencesUtils.detailSetting(LoginActivity.this,HandlerSettings);
            //储存用户信息及其登录状态
            SharedPreferencesUtils.saveSetting(this,LoginInfo,"userName",data.getUserName());
            SharedPreferencesUtils.saveSetting(this,LoginInfo,"isLogin",true);
            SharedPreferencesUtils.saveSetting(this,LoginInfo,"roleName",data.getRoleName());
            MainActivity.start(this, HomeFragment.class);
            LoginActivity.this.finish();
        }else{
            showToast(bean.getMsg());
        }
    }

    /**
     * 显示加载
     */
    @Override
    public void showLoading() {
        editUserName.setEnabled(false);
        editPassword.setEnabled(false);
        loginBtn.setEnabled(false);
        startScaleAnimation(login_layout);
    }

    /**
     * 隐藏加载
     */
    @Override
    public void hideLoading() {
        editUserName.setEnabled(true);
        editPassword.setEnabled(true);
        loginBtn.setEnabled(true);
        stopScaleAnimation(layout_progress);
    }

    @Override
    public void onError(String message) {
        editUserName.setEnabled(true);
        editPassword.setEnabled(true);
        loginBtn.setEnabled(true);
        stopScaleAnimation(layout_progress);
        showToast(message);
    }

    //执行缩放动画
    private void startScaleAnimation(View view) {
        baseAnimation.scaleXAnimation(view,1f,0.5f,100);
        baseAnimation.setAnimatorListener(new BaseAnimation.AnimatorListener() {
            @Override
            public void AnimationStart(Animator animator) {

            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                layout_progress.setVisibility(View.VISIBLE);
                //X轴缩放动画
                BaseAnimation baseAnimation = new BaseAnimation();
                baseAnimation.scaleXYAnimation(layout_progress,0.5f,1f,1000);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    //停止动画
    private void stopScaleAnimation(View view) {
        baseAnimation.scaleXYAnimation(view,1f,0,500);
        baseAnimation.removeAnimatorListener();
        baseAnimation.setAnimatorListener(new BaseAnimation.AnimatorListener() {
            @Override
            public void AnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                login_layout.setVisibility(View.VISIBLE);
                baseAnimation.removeAnimatorListener();
                baseAnimation.scaleXAnimation(login_layout,0,1f,300);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}
