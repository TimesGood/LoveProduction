package com.aige.loveproduction.util;

import android.util.Base64;

import com.aige.loveproduction.listener.OnWriteFileListener;
import com.aige.loveproduction.mpr.MprBohrData;
import com.aige.loveproduction.mpr.MprCuttingData;
import com.aige.loveproduction.mpr.MprData;
import com.aige.loveproduction.mpr.MprDataWrap;
import com.aige.loveproduction.mpr.MprMaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;

/**
 * IO流显相关
 */
public class IOUtil {

    /**
     * 使用正则表达式截取双引号中的值
     * @param text 有双引号的字符串
     * @return 双引号中的值
     */
    public static Float patternText(String text) {
        Matcher m = Pattern.compile("\"(.*?)\"").matcher(text);
        if (m.find()) {
            return Float.parseFloat(Objects.requireNonNull(m.group(1)));
        }
        return null;
    }

    /**
     * 将图片文件转为Base64字符串
     */
    public static String imageToBase64(String path) {
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //一次性读取出来，图片不能太大
            data = new byte[is.available()];
            is.read(data);
            result = Base64.encodeToString(data,Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 从网络请求ResponseBody中获取流下载文件
     */
    public static void writeResponseBody(ResponseBody response, File file,OnWriteFileListener listener) {
        if (response == null) return;
        InputStream is = response.byteStream();
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            os.flush();
            if(listener != null) listener.onSuccess(file.getAbsolutePath());
        } catch (IOException e) {
            if(listener != null) listener.onFail(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //*****************************************读取Mpr文件*******************************************************
    /**
     * 读取mpr文件
     */
    private static byte i;
    public static MprDataWrap readMprFile(File file) {
        if(!file.isFile()) return null;
        //主图形
        MprMaster master = new MprMaster();
        //侧钉子数值
        List<MprData> bohrHoriz = new ArrayList<>();
        //表面钉子数值
        List<MprData> bohrVert = new ArrayList<>();
        List<MprData> bohrVertCross = new ArrayList<>();
        //切割线
        List<MprData> cutting = new ArrayList<>();
        MprData mprData = null;
        boolean flag = false;
        FileReader fr = null;
        BufferedReader buff = null;
        try {
            fr = new FileReader(file);
            buff = new BufferedReader(fr);
            String name = null;
            while (buff.ready()) {
                String readLine = buff.readLine();
                //查找图形组件
                if(readLine.contains("[H")) {
                    //主图形
                    name = "master";
                    flag = true;
                }else if(readLine.contains("BM=\"XP\"") || readLine.contains("BM=\"XM\"") || readLine.contains("BM=\"YM\"")) {
                    //侧钉子
                    mprData = new MprBohrData(master);
                    name = "bohrHoriz";
                    flag = true;
                }else if(readLine.contains("BM=\"LS\"")) {
                    //表面钉子样式1
                    mprData = new MprBohrData(master);
                    name = "bohrVert";
                    flag = true;
                }else if(readLine.contains("BM=\"LSU\"")) {
                    //表面钉子样式2
                    mprData = new MprBohrData(master);
                    name = "bohrVertCross";
                    flag = true;
                }
                else if(readLine.contains("$E0")) {
                    //切割数据
                    mprData = new MprCuttingData(master);
                    name = "cutting";
                    flag = true;
                    i = 0;
                }
                //检索到某图形数据时开启通道
                if(flag) {
                    //判断那个图形的通道，并对数据进行整理归纳储存于集合中
                    if("master".equals(name)) {
                        flag = parseData(readLine, master, null);
                    }else if("bohrHoriz".equals(name)) {
                        flag = parseData(readLine, mprData, bohrHoriz);
                    }else if("bohrVert".equals(name)) {
                        flag = parseData(readLine, mprData, bohrVert);
                    }else if("bohrVertCross".equals(name)) {
                        flag = parseData(readLine, mprData, bohrVertCross);
                    }
                    else if("cutting".equals(name)) {
                        flag = parseData(readLine,mprData,cutting);
                    }
                }
            }
            //当最终数据通道都关闭时，数据解析成功，反之解析失败
            if(flag) return null;
            MprDataWrap wrap = new MprDataWrap();
            //解析成功储存数据
            wrap.setMaster(master);
            wrap.setBohrHoriz(bohrHoriz);
            wrap.setBohrVert(bohrVert);
            wrap.setBohrVertCross(bohrVertCross);
            wrap.setCutting(cutting);
            wrap.initData();
            return wrap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(buff != null) {
                try {
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static MprDataWrap readMpr(List<String> data) {
        //主图形
        MprMaster master = new MprMaster();
        //侧钉子数值
        List<MprData> bohrHoriz = new ArrayList<>();
        //表面钉子数值
        List<MprData> bohrVert = new ArrayList<>();
        List<MprData> bohrVertCross = new ArrayList<>();
        //切割线
        List<MprData> cutting = new ArrayList<>();
        MprData mprData = null;
        boolean flag = false;
        Iterator<String> iterator = data.iterator();
        String name = "";
        while(iterator.hasNext()) {
            String next = iterator.next();
            //查找图形组件
            if(next.contains("[H")) {
                //主图形
                name = "master";
                flag = true;
            }else if(next.contains("BM=\"XP\"") || next.contains("BM=\"XM\"") || next.contains("BM=\"YM\"")) {
                //侧钉子
                mprData = new MprBohrData(master);
                name = "bohrHoriz";
                flag = true;
            }else if(next.contains("BM=\"LS\"")) {
                //表面钉子样式1
                mprData = new MprBohrData(master);
                name = "bohrVert";
                flag = true;
            }else if(next.contains("BM=\"LSU\"")) {
                //表面钉子样式2
                mprData = new MprBohrData(master);
                name = "bohrVertCross";
                flag = true;
            }
            else if(next.contains("$E0")) {
                //切割数据
                mprData = new MprCuttingData(master);
                name = "cutting";
                flag = true;
                i = 0;
            }
            //检索到某图形数据时开启通道
            if(flag) {
                //判断那个图形的通道，并对数据进行整理归纳储存于集合中
                if("master".equals(name)) {
                    flag = parseData(next, master, null);
                }else if("bohrHoriz".equals(name)) {
                    flag = parseData(next, mprData, bohrHoriz);
                }else if("bohrVert".equals(name)) {
                    flag = parseData(next, mprData, bohrVert);
                }else if("bohrVertCross".equals(name)) {
                    flag = parseData(next, mprData, bohrVertCross);
                }
                else if("cutting".equals(name)) {
                    flag = parseData(next,mprData,cutting);
                }
            }
        }
        //当最终数据通道都关闭时，数据解析成功，反之解析失败
        if(flag) return null;
        MprDataWrap wrap = new MprDataWrap();
        //解析成功储存数据
        wrap.setMaster(master);
        wrap.setBohrHoriz(bohrHoriz);
        wrap.setBohrVert(bohrVert);
        wrap.setBohrVertCross(bohrVertCross);
        wrap.setCutting(cutting);
        wrap.initData();
        return wrap;
    }
    private static boolean parseData(String data, MprData mprData, List<MprData> list) {
        //矩形数据解析
        if(data.contains("_BSX=")) ((MprMaster)mprData).setMaster_x(Float.parseFloat(data.split("=")[1]));
        if(data.contains("_BSY=")){
            ((MprMaster)mprData).setMaster_y(Float.parseFloat(data.split("=")[1]));
//            list.add(mprData);
            return false;
        }
        //钉子数据解析
        if (data.contains("XA=")) ((MprBohrData)mprData).setXA(patternText(data));
        if (data.contains("YA=")) ((MprBohrData)mprData).setYA(patternText(data));
        if (data.contains("DU=")) ((MprBohrData)mprData).setDU(patternText(data));
        if (data.contains("TI=")) {
            ((MprBohrData)mprData).setTI(patternText(data));
            list.add(mprData);
            return false;
        }
        //切割数据解析
        if (data.contains("X=") && !data.contains(".X=") && mprData instanceof MprCuttingData) ((MprCuttingData)mprData).addData("X"+i,Float.parseFloat(data.split("=")[1]));
        if (data.contains("Y=") && !data.contains(".Y=") && mprData instanceof MprCuttingData) {
            ((MprCuttingData)mprData).addData("Y"+i,Float.parseFloat(data.split("=")[1]));
            i++;
        }
        if(data.contains("]") || data.contains("[001")) {
            list.add(mprData);
            return false;
        }
        return true;
    }
}