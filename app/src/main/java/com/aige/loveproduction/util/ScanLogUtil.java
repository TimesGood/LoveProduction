package com.aige.loveproduction.util;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class ScanLogUtil {
    private static String PATH_LOGCAT;
    public static void saveLog(Context context,String log) {
        byte[] buff = new byte[]{};
        FileOutputStream fos = null;
        //换行写入
        String str = log + System.getProperty("line.separator");
        //检查SD卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "loveproductionlog";//目录/storage/emulated/0/loveproductionlog
        } else {
            PATH_LOGCAT = context.getFilesDir().getAbsolutePath()
                    + File.separator + "loveproductionlog";
        }
        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            buff = str.getBytes();
            fos = new FileOutputStream(file+"/scanLog.txt", true);
            fos.write(buff);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //读取文件
    public static String read(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "loveproductionlog";//目录/storage/emulated/0/loveproductionlog
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = context.getFilesDir().getAbsolutePath()
                    + File.separator + "loveproductionlog";
        }
        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileReader fr = null;
        BufferedReader br = null;
        String content = "";
        String line = null;
        try {
            fr = new FileReader(file+"/scanLog.txt");
            br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                content = content + "#" + line;
            }
        } catch (FileNotFoundException e) {
            System.out.println("指定的文件不存在：" + file.getName());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("读取文件出错");
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

}
