//package com.aige.loveproduction.mvp.ui.webview;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Environment;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.tencent.smtt.sdk.TbsReaderView;
//
//import java.io.File;
//
//public class SuperFileView extends FrameLayout implements TbsReaderView.ReaderCallback {
//
//    private static String TAG = "SuperFileView";
//    private Context context;
//    private TbsReaderView mTbsReaderView;
//    private OnGetFilePathListener mOnGetFilePathListener;
//
//    public OnGetFilePathListener getOnGetFilePathListener() {
//        return mOnGetFilePathListener;
//    }
//
//    public void setOnGetFilePathListener(OnGetFilePathListener mOnGetFilePathListener) {
//        this.mOnGetFilePathListener = mOnGetFilePathListener;
//    }
//
//    public SuperFileView(@NonNull Context context) {
//        this(context, null, 0);
//    }
//
//    public SuperFileView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public SuperFileView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this.context = context;
//        mTbsReaderView = new TbsReaderView(context, this);
//        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
//    }
//
//    @Override
//    public void onCallBackAction(Integer integer, Object o, Object o1) {
//        Log.e(TAG,"****************************************************" + integer);
//    }
//
//    /***
//     * 获取File路径
//     */
//    public interface OnGetFilePathListener {
//        void onGetFilePath(SuperFileView mSuperFileView);
//    }
//
//    /**
//     * 展示
//     */
//    public void show() {
//        if(mOnGetFilePathListener!=null){
//            mOnGetFilePathListener.onGetFilePath(this);
//        }
//    }
//
//    /**
//     * 显示文件的界面，退出界面以后需要销毁，否则再次加载文件无法加载成功，会一直显示加载文件进度条。
//     */
//    public void onStopDisplay() {
//        if (mTbsReaderView != null) {
//            mTbsReaderView.onStop();
//        }
//    }
//
//    /**
//     * 显示文件
//     */
//    public void displayFile(File mFile){
//        if (mFile != null && !TextUtils.isEmpty(mFile.toString())){
//            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
//            String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";//SD卡下面文件路径
//            File bsReaderTempFile =new File(bsReaderTemp);
//            if (!bsReaderTempFile.exists()){//如果SD卡下不存在TbsReaderTemp文件夹
//                Log.e(TAG,"准备在SD卡下创建TbsReaderTemp文件夹！");
//                boolean mkdir = bsReaderTempFile.mkdir();
//                if(!mkdir){
//                    Log.e(TAG,"创建/storage/emulated/0/TbsReaderTemp失败！！！！！");
//                }
//            }
//
//            //加载文件
//            Bundle localBundle = new Bundle();
//            Log.e(TAG,"mFile.toString()="+mFile.toString());
//            localBundle.putString("filePath", mFile.toString());
//            localBundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
//
//            if (mTbsReaderView == null){
//                mTbsReaderView = new TbsReaderView(context,this);
//            }
//            boolean bool = mTbsReaderView.preOpen(getFileType(mFile.toString()), false);
//            if (bool){
//                mTbsReaderView.openFile(localBundle);
//            }
//        }else {
//            Log.e(TAG,"文件路径无效！");
//        }
//    }
//
//    /***
//     * 获取文件类型
//     *
//     * @param paramString
//     * @return
//     */
//    private String getFileType(String paramString){
//        String str = "";
//        if (TextUtils.isEmpty(paramString)) {
//            Log.e(TAG, "paramString---->null");
//            return str;
//        }
//        Log.e(TAG, "paramString:" + paramString);
//
//        int i = paramString.lastIndexOf('.');
//        if (i <= -1) {
//            Log.e(TAG, "i <= -1");
//            return str;
//        }
//
//        str = paramString.substring(i + 1);
//        Log.e(TAG, "paramString.substring(i + 1)------>" + str);
//        return str;
//    }
//}