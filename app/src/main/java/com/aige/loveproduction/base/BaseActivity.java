package com.aige.loveproduction.base;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 业务基类
 * 当需要进行网络请求时使用这个
 * 基于MVP模式对M层与V层进行分离
 */

public abstract class BaseActivity<P extends BasePresenter,V extends IBaseView> extends AppActivity
        implements IBaseView{
    protected P mPresenter;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //创建相应的P层对象
        mPresenter = createPresenter();
        //绑定V层
        mPresenter.onAttach((V) this);
        super.onCreate(savedInstanceState);
    }

    /**
     * 绑定对应的P层实体对象，继承此类的Activity都需要创建相应的P层来实例化
     * @return new Presenter实体对象
     */
    protected abstract P createPresenter();
    /**
     * 解除绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }
    /**
     * 绑定生命周期 防止MVP内存泄漏
     * 在使用Rxjava时，当我们一个网络请求出去了，在结果还没有返回时用户退出当前Activity，而Activity被RxJava订阅了，使得Activity
     * 没有及时的释放内存，导致内存泄漏，所以我们使用AutoDispose在Activity调用onDestroy时进行解除订阅，使得内存得以及时释放
     * 使用这个需要我们的类实现LifecycleOwner接口，而Android的AppCompatActivity已经实现了此类，所以我们直接this使用即可
     */
    @Override
    public <P> AutoDisposeConverter<P> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }
}
