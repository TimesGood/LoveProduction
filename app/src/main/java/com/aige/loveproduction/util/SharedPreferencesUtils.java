package com.aige.loveproduction.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * SharedPreferences数据存储管理类
 */

public class SharedPreferencesUtils {

    /**
     * 保存文件
     * @param context 当前页面的activity
     * @param filename 保存在的文件民称
     * @param key key值，String
     * @param flag value值，boolean
     */
    public static void saveSetting(Context context,String filename,String key,boolean flag) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,flag);
        edit.commit();
    }

    /**
     * 保存文件
     * @param context 当前页面的activity
     * @param filename 保存在的文件民称
     * @param key key值，String
     * @param value value值，String
     */
    public static void saveSetting(Context context,String filename,String key,String value) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }
    /**
     * 根据key获取文件内的value
     * @param context 当前页面的activity
     * @param filename 要获取数据的文件民称
     * @param key key值，String
     * @return 获取的value值，boolean
     */
    public static boolean getBoolean(Context context,String filename,String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        boolean status = sp.getBoolean(key, false);
        return status;
    }
    /**
     * 根据key获取文件内的value
     * @param context 当前页面的activity
     * @param filename 要获取数据的文件民称
     * @param key key值，String
     * @return 获取的value值，String
     */
    public static String getValue(Context context,String filename,String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String status = sp.getString(key, "");
        return status;
    }

    /**
     * 清空文件内容
     * @param context 当前页面的activity
     * @param filename 要清空的文件民称
     */
    public static void detailSetting(Context context,String filename) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    /**
     * 获取设置中单选为true的key值
     * @param context 当前页面的activity
     * @param filename 文件民称
     * @return
     */
    public static String getRadioSetting(Context context,String filename) {
        StringBuffer buffer = new StringBuffer();
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Map<String, ?> all = sp.getAll();
        Iterator<? extends Map.Entry<String, ?>> iterator = all.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if("true".equals(entry.getValue()+"")) {
                return entry.getKey().toString();
            }
        }
        return "";
    }

    /**
     * 获取设置多选为true的key列表
     * @param context
     * @param filename
     * @return
     */
    public static List<String> getCheckBoxSetting(Context context,String filename) {
        StringBuffer str = new StringBuffer();
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Map<String, ?> all = sp.getAll();
        Iterator<? extends Map.Entry<String, ?>> iterator = all.entrySet().iterator();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if("true".equals(entry.getValue()+"")) {
                list.add(entry.getKey()+"");
            }
        }
        return list;
    }
}
