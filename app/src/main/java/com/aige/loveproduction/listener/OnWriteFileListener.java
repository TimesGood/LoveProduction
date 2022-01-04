package com.aige.loveproduction.listener;

//文件下载接口
public interface OnWriteFileListener {
    void onSuccess(String path);
    void onFail(Throwable e);
}
