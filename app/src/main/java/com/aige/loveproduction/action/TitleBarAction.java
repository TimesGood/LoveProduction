package com.aige.loveproduction.action;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aige.loveproduction.R;
import com.aige.loveproduction.listener.OnTitleBarListener;


/**
 *    desc   : 标题栏意图
 */
public interface TitleBarAction {

    @Nullable
    Toolbar getTitleBar();
    default boolean isToolbar(){
        return getTitleBar() != null;
    }
    /**
     * 隐藏默认标题
     */
    default void hideTitle() {
        if(isToolbar()) {
            getTitleBar().setTitle("");
        }
    }

    /**
     * 设置标题栏默认的标题
     */
    default void setTitle(@StringRes int id) {
        if (isToolbar()) {
            setTitle(getTitleBar().getResources().getString(id));
        }
    }

    /**
     * 设置标题栏默认的标题
     */
    default void setTitle(CharSequence title) {
        if (isToolbar()) {
            getTitleBar().setTitle(title);
        }
    }

    /**
     * 设置居中的标题，居中的标题必须在layout文件的Toolbar标签中定义一个TextView
     * @param title 标题名
     */
    default void setCenterTitle(CharSequence title) {
        if (isToolbar() && getTitleBar().getChildCount() > 0) {
            for(int i = 0; i < getTitleBar().getChildCount(); i++) {
                if(getTitleBar().getChildAt(i) instanceof TextView) {
                    ((TextView) getTitleBar().getChildAt(i)).setText(title);
                }
            }
        }
    }

    /**
     * 设置左图标，一般是返回图标
     */
    default void setLeftIcon(@DrawableRes int id) {
        if (isToolbar()) {
            getTitleBar().setNavigationIcon(id);
        }
    }

    /**
     * 隐藏左图标
     */
    default void hideLeftIcon() {
        if (isToolbar()) {
            getTitleBar().setNavigationIcon(null);
        }
    }

    /**
     * 获取菜单数量
     */
    default int getMenuSize(){
        if(isToolbar()) {
            return getTitleBar().getMenu().size();
        }
        return 0;
    }

    /**
     * 设置某菜单字体
     * @param index 菜单索引
     * @param title 要设置的字体
     */
    default void setMenuTitle(int index,String title){
        if(isToolbar()) {
            getTitleBar().getMenu().getItem(index).setTitle(title);
        }
    }
    /**
     * 设置某菜单字体
     * @param index 菜单索引
     * @param title 要设置的字体
     */
    default void setMenuTitle(int index,@StringRes int title){
        if(isToolbar()) {
            getTitleBar().getMenu().getItem(index).setTitle(title);
        }
    }
}