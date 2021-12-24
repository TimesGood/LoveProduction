package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.UserBean;
import com.aige.loveproduction.mvp.contract.LoginContract;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.util.MD5Utils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginModel implements LoginContract.Model {

    public static IBaseModel newInstance() {
        return new LoginModel();
    }
    @Override
    public Observable<BaseBean<UserBean>> getUser(String username, String password) {
        String sign = MD5Utils.md5("LoveProduction2021" + username);
        Map<String,String> map = new HashMap<>();
        map.put("userName",username);
        map.put("password",password);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getUser(body);
    }
}
