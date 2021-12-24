package com.aige.loveproduction.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aige.loveproduction.action.ActivityAction;
import com.aige.loveproduction.action.BundleAction;
import com.aige.loveproduction.action.ClickAction;
import com.aige.loveproduction.action.KeyboardAction;
import com.aige.loveproduction.action.SharePreferencesAction;

import org.jetbrains.annotations.NotNull;

public abstract class AppFragment<A extends AppActivity> extends Fragment
        implements BundleAction,
        ActivityAction,
        ClickAction,
        KeyboardAction,
        SharePreferencesAction {
    private A mActivity;
    private Toast toast;
    private View mView;

    /**
     * 获得全局Activity
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mActivity = (A)requireActivity();
    }

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(this.getLayoutId(), container, false);
        initView();
        return mView;
    }
    /**
     * 对Toast广播进行一些初始化的改造，把广播通知带有的软件名擦掉
     */
    public void showToast(String msg) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(getContext(),"",Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }
    /**
     * 设置Layout
     * @return Layout Id
     */
    protected abstract int getLayoutId();
    /**
     * 初始化动作
     */
    protected abstract void initView();

    public A getAttachActivity() {
        return mActivity;
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }
}
