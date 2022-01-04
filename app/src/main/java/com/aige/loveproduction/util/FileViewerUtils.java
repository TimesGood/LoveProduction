package com.aige.loveproduction.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 文件操作相关
 */
public class FileViewerUtils {
    /**
     * 查看文件
     * @param file
     */
    public static void viewFile(Context activity, File file) {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.fromFile(file), getMimeType(file));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * Android 4.4 系统上文件查看
     *
     * @param context 上下文
     * @param file  文件名
     */
    public static void viewFile4_4(Context context, File file) {
        String ext = getExtension(file);
        if (TextUtils.isEmpty(ext)) {
            Toast.makeText(context,"无法识别的文件",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = IntentUtils.getFileViewIntent(context, file);
        if (intent != null) {
            Intent newIntent = Intent.createChooser(intent, "请选择一个应用打开");
            context.startActivity(newIntent);
        } else {
            FileViewerUtils.viewFile(context, file);
        }
    }

    /**
     * 获取文件后缀
     * @param filePath 带有.xls .doc .ppt .pdf。。。的路径
     * @return .xls .doc .ppt .pdf。。。
     */
    public static String getExtension(final File filePath) {
        if (filePath == null) {
            return null;
        }
        String absolutePath = filePath.getAbsolutePath();
        int dot = absolutePath.lastIndexOf(".");
        if (dot >= 0) {
            return absolutePath.substring(dot);
        } else {
            return "";
        }
    }

    /**
     * 获取文件类型的拓展名
     */
    public static String getMimeType(final File file) {
        String extension = getExtension(file);
        return extension == null ? "application/octet-stream" : MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
    }
    /**
     * 判断目录是否存在，不存在则创建并判断是否创建成功
     * @param dirPath 目录路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(final String dirPath) {
        return createOrExistsDir(new File(dirPath));
    }
    /**
     * 判断目录是否存在，不存在则创建并判断是否创建成功
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(final File file) {
        // 如果存在，是目录则返回 true，是文件则返回 false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean isFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回 true，是目录则返回 false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据路径获取文件名
     */
    public static String getFileName(File file) {
        String[] split = file.getAbsolutePath().split("/");
        return split[split.length-1];
    }

    /**
     * 获取文件所在文件目录
     */
    public static String getFilePath(File file) {
        String[] split = file.getAbsolutePath().split("/");
        return file.getAbsolutePath().split(split[split.length-1])[0];
    }

    /**
     * 获取缓存大小
     */
    public static String getCacheSize(Context context){
        return getFormatSize(getFolderSize(context.getExternalCacheDir()));
    }
    /**
     * 清空缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getExternalCacheDir());
    }
    /**
     * 删除某文件夹下的文件及其目录
     */
    public static boolean deleteDir(File dir) {
        //如果缓存有很多目录
        if (dir != null && dir.isDirectory()) {
            //获取目录数组
            String[] children = dir.list();
            int size = 0;
            if (children != null) {
                size = children.length;
                for (int i = 0; i < size; i++) {
                    //拿到每个目录再递归
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        if (dir == null) {
            return true;
        } else {
            //删除
            return dir.delete();
        }
    }

    /**
     * 获取某文件中的大小
     */
    public static long getFolderSize(File file){
        long size = 0;
        File[] files = file.listFiles();
        if(files == null) return 0;
        for (File value : files) {
            //如果还有文件夹,递归找出
            if (value.isDirectory()) {
                size += getFolderSize(value);
            } else {
                //获取文件大小
                size += value.length();
            }
        }
        return size;
    }
    public static String getFormatSize(double size){
        double kiloByte = size / 1024;
        if(kiloByte < 1) return "0.0KB";
        double megaByte = kiloByte / 1024;
        if(megaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(kiloByte));
            return result.setScale(2,BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if(gigaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(megaByte));
            return result.setScale(2,BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if(teraBytes < 1) {
            BigDecimal result = new BigDecimal(Double.toString(gigaByte));
            return result.setScale(2,BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "G";
        }
        BigDecimal result = new BigDecimal(Double.toString(teraBytes));
        return result.setScale(2,BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "T";
    }
}
