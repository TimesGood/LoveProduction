package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.mvp.contract.ToFillInContract;
import com.google.gson.Gson;

import io.reactivex.rxjava3.core.Observable;

public class ToFillInModel implements ToFillInContract.Model{
    private final Gson gson = new Gson();
    public static IBaseModel newInstance() {
        return new ToFillInModel();
    }


}
