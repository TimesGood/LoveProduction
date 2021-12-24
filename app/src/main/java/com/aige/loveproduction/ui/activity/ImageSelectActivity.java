package com.aige.loveproduction.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aige.loveproduction.R;
import com.aige.loveproduction.action.HandlerAction;
import com.aige.loveproduction.action.StatusAction;
import com.aige.loveproduction.ui.adapter.ImageSelectAdapter;
import com.aige.loveproduction.base.AppActivity;
import com.aige.loveproduction.base.BaseAdapter;
import com.aige.loveproduction.bean.AlbumInfo;
import com.aige.loveproduction.manager.ThreadPoolManager;
import com.aige.loveproduction.ui.customui.StatusLayout;
import com.aige.loveproduction.ui.dialogin.AlbumDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 图片浏览+选择图片功能
 */
public final class ImageSelectActivity extends AppActivity
        implements StatusAction, Runnable,
        HandlerAction,
        BaseAdapter.OnItemClickListener,
        BaseAdapter.OnItemLongClickListener,
        BaseAdapter.OnChildClickListener {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingView;
    private ImageSelectAdapter mAdapter;
    /** 专辑选择对话框 */
    private AlbumDialog.Builder mAlbumDialog;
    /** 最大选中 */
    private int mMaxSelect = 1;
    /** 选中列表 */
    private final ArrayList<String> mSelectImage = new ArrayList<>();
    /** 全部图片 */
    private final ArrayList<String> mAllImage = new ArrayList<>();
    /** 图片专辑 */
    private final HashMap<String, List<String>> mAllAlbum = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.image_select_activity;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.rv_image_select_list);
        mFloatingView = findViewById(R.id.fab_image_select_floating);
    }

    @Override
    protected void initData() {
        setCenterTitle("选择图片");
        setOnClickListener(mFloatingView);
        mAdapter = new ImageSelectAdapter(this, mSelectImage);
        mAdapter.setOnChildClickListener(R.id.fl_image_select_check, this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        // 禁用动画效果
        mRecyclerView.setItemAnimator(null);
        // Bundle中获取最大的选择数
        mMaxSelect = getInt("amount");
        // 显示加载进度条
        showLoadings();
        //开启线程池加载图片列表
        ThreadPoolManager.getInstance().execute(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu,menu);
        return true;
    }

    /**
     * 点击菜单右侧选择专辑
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.select_album) {
            if (mAllImage.isEmpty()) return super.onOptionsItemSelected(item);
            ArrayList<AlbumInfo> data = new ArrayList<>(mAllAlbum.size() + 1);
            int count = 0;
            Set<String> keys = mAllAlbum.keySet();
            for (String key : keys) {
                List<String> list = mAllAlbum.get(key);
                if (list != null && !list.isEmpty()) {
                    count += list.size();
                    data.add(new AlbumInfo(list.get(0), key, String.format(getString(R.string.image_select_total), list.size()), mAdapter.getData() == list));
                }
            }
            data.add(0, new AlbumInfo(mAllImage.get(0), getString(R.string.image_select_all), String.format(getString(R.string.image_select_total), count), mAdapter.getData() == mAllImage));
            if (mAlbumDialog == null) {
                mAlbumDialog = new AlbumDialog.Builder(this)
                        .setListener((dialog, position, bean) -> {
                            setMenuTitle(0,bean.getName());
                            // 滚动回第一个位置
                            mRecyclerView.scrollToPosition(0);
                            if (position == 0) {
                                mAdapter.setData(mAllImage);
                            } else {
                                mAdapter.setData(mAllAlbum.get(bean.getName()));
                            }
                            // 执行列表动画
                            mRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.from_bottom_layout));
                            mRecyclerView.scheduleLayoutAnimation();
                        });
            }
            mAlbumDialog.setData(data)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public StatusLayout getStatusLayout() {
        return findViewById(R.id.loading);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_image_select_floating) {
            if (mSelectImage.isEmpty()) {
//                // 点击拍照
                CameraActivity.start(this, file -> {

                    // 当前选中图片的数量必须小于最大选中数
                    if (mSelectImage.size() < mMaxSelect) {
                        mSelectImage.add(file.getPath());
                    }

                    // 这里需要延迟刷新，否则可能会找不到拍照的图片
                    postDelayed(() -> {
                        // 重新加载图片列表
                        ThreadPoolManager.getInstance().execute(ImageSelectActivity.this);
                    }, 1000);
                });
                return;
            }

            // 完成选择
            setResult(RESULT_OK, new Intent().putStringArrayListExtra("picture", mSelectImage));
            finish();
        }
    }

    /**
     * 条目点击监听,查看大图
     */
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        ImagePreviewActivity.start(getActivity(), mAdapter.getData(), position);
    }

    /**
     * 条目长按监听，选中图片
     */
    @Override
    public boolean onItemLongClick(RecyclerView recyclerView, View itemView, int position) {
        if (mSelectImage.size() < mMaxSelect) {
            // 长按的时候模拟选中
            return itemView.findViewById(R.id.fl_image_select_check).performClick();
        }
        return false;
    }

    /**
     * 点击某条目的子View监听，选中图片
     */
    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {
        if (childView.getId() == R.id.fl_image_select_check) {
            String path = mAdapter.getItemData(position);
            File file = new File(path);
            if (!file.isFile()) {
                mAdapter.removeItemData(position);
                showToast("无法选中，该图片已被删除");
                return;
            }

            if (mSelectImage.contains(path)) {
                mSelectImage.remove(path);

                if (mSelectImage.isEmpty()) {
                    mFloatingView.hide();
                    postDelayed(() -> {
                        mFloatingView.setImageResource(R.drawable.camera);
                        mFloatingView.show();
                    }, 200);
                }

                mAdapter.notifyItemChanged(position);
                return;
            }

            if (mMaxSelect == 1 && mSelectImage.size() == 1) {

                List<String> data = mAdapter.getData();
                if (data != null) {
                    int index = data.indexOf(mSelectImage.remove(0));
                    if (index != -1) {
                        mAdapter.notifyItemChanged(index);
                    }
                }
                mSelectImage.add(path);

            } else if (mSelectImage.size() < mMaxSelect) {

                mSelectImage.add(path);

                if (mSelectImage.size() == 1) {
                    mFloatingView.hide();
                    postDelayed(() -> {
                        mFloatingView.setImageResource(R.drawable.tick);
                        mFloatingView.show();
                    }, 200);
                }
            } else {
                showToast("最多选择"+mMaxSelect);
            }
            mAdapter.notifyItemChanged(position);
        }
    }

    /**
     * 线程池加载显示图片
     */
    @Override
    public void run() {
        mAllAlbum.clear();
        mAllImage.clear();
        //*******************需要查询多媒体文件************************
        //MediaStore的数据库文件位于/data/data/com.android.providers/databases
        //指定要查询的SQLite表名称
        final Uri contentUri = MediaStore.Files.getContentUri("external");
        //指定查询结果的排序方式
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        //指定查询条件
        final String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
        //***************************指定查询数据库中哪几列****************************
        String[] projections = new String[]{
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE
        };
        //要查询什么类型的数据
        String[] selectionArgs = new String[]{
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)//图片
        };
        //获取ContentProvider的查询接口
        ContentResolver contentResolver = getContentResolver();
        //执行查询
        Cursor cursor = contentResolver.query(contentUri, projections, selection, selectionArgs, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            //获取某字段在第几列
            int pathIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int mimeTypeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
            int sizeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);

            do {
                long size = cursor.getLong(sizeIndex);
                if (size < 1024) continue;// 图片大小不得小于 1 KB
                String type = cursor.getString(mimeTypeIndex);//获取图片类型
                String path = cursor.getString(pathIndex);//获取图片的绝对路径
                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(type)) continue;//图片路径、图片类型不能为空
                File file = new File(path);
                if (!file.exists() || !file.isFile()) continue;//图片路径不能是文件夹
                File parentFile = file.getParentFile();//获得父目录
                if (parentFile != null) {
                    String albumName = parentFile.getName();//获取目录名
                    //目录名作为专辑名称
                    List<String> data = mAllAlbum.get(albumName);
                    if (data == null) {
                        data = new ArrayList<>();
                        mAllAlbum.put(albumName, data);
                    }
                    data.add(path);
                    mAllImage.add(path);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        postDelayed(() -> {
            // 滚动回第一个位置
            mRecyclerView.scrollToPosition(0);
            // 设置新的列表数据
            mAdapter.setData(mAllImage);
            //设置悬浮按钮
            if (mSelectImage.isEmpty()) {
                mFloatingView.setImageResource(R.drawable.camera);
            } else {
                mFloatingView.setImageResource(R.drawable.tick);
            }
            // 执行列表动画
            mRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.from_bottom_layout));
            mRecyclerView.scheduleLayoutAnimation();
            if (mAllImage.isEmpty()) {
                // 显示空布局
                showEmpty();
                // 设置右菜单
                setMenuTitle(0,null);
            } else {
                // 显示加载完成
                showComplete();
                // 设置右标题
                setMenuTitle(0,R.string.image_select_all);
            }
        }, 500);
    }

    /**
     * 图片选择监听
     */
    public interface OnPhotoSelectListener {

        /**
         * 选择回调
         *
         * @param data          图片列表
         */
        void onSelected(List<String> data);

        /**
         * 取消回调
         */
        default void onCancel() {}
    }
    public static void start(AppActivity activity, OnPhotoSelectListener listener) {
        start(activity, 1, listener);
    }

    public static void start(AppActivity activity, int maxSelect, OnPhotoSelectListener listener) {
        if (maxSelect < 1) {
            // 最少要选择一个图片
            throw new IllegalArgumentException("are you ok?");
        }
        //储存最大选择数在Bundle中
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra("amount", maxSelect);
        //页面跳转并接收回调参数
        activity.startActivityForResult(intent, new OnActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
                if (listener == null || data == null) {
                    return;
                }
                //如果没有选择文件路径
                ArrayList<String> list = data.getStringArrayListExtra("picture");
                if (list == null || list.isEmpty()) {
                    listener.onCancel();
                    return;
                }
                //如果选择有文件路径，但是选择的路径并不是文件，遍历剔除非文件路径
                Iterator<String> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (!new File(iterator.next()).isFile()) {
                        iterator.remove();
                    }
                }
                //返回选择的文件路径
                if (resultCode == RESULT_OK && !list.isEmpty()) {
                    listener.onSelected(list);
                    return;
                }
                listener.onCancel();
            }
        });
    }

}