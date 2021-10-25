package com.aige.loveproduction.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 读取文件的工具类
 */
public class FileUtil {

    private Context context;

    public FileUtil() {
    }

    public FileUtil(Context context) {
        super();
        this.context = context;
    }

    /**
     * 保存文件，默认保存app目录之下
     * @param filename 文件名，只允许有一层文件夹
     * @param filecontent 文件内容
     * @throws Exception
     */
    public void saveFile(String filename, String filecontent) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String[] split = filename.split("/");
            if(split.length>1) {
                filename = context.getExternalFilesDir(split[0])+"/"+split[1];
            }else{
                filename = context.getExternalFilesDir(null)+"/"+filename;

            }
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(file,true);
            //将String字符串以字节流的形式写入到输出流中
            output.write(filecontent.getBytes());

            output.close();
            //关闭输出流
        } else Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }

    /**
     * 读取文件，默认读取app目录之下
     * @param filename 文件名，只允许有一层文件夹
     * @return 内容
     * @throws IOException
     */
    public String readFrom(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String[] split = filename.split("/");
            if(split.length>1) {
                filename = context.getExternalFilesDir(split[0])+"/"+split[1];
            }else{
                filename = context.getExternalFilesDir(null)+"/"+filename;

            }
//            //打开文件输入流
//            FileInputStream input = new FileInputStream(filename);
//            byte[] temp = new byte[1024];
//            int len = 0;
//            //读取文件内容:
//            while ((len = input.read(temp)) > 0) {
//                sb.append(new String(temp, 0, len)).append("#");
//            }
//            //关闭输入流
//            input.close();
            FileReader fr = new FileReader(filename);
            BufferedReader buff = new BufferedReader(fr);
            while (buff.ready()) {
                sb.append(buff.readLine()).append("#");
            }
        }
        return sb.toString();
    }
}