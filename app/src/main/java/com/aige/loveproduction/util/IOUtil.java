package com.aige.loveproduction.util;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import com.aige.loveproduction.listener.OnWriteFileListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
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
     * 读取mpr文件
     */
    private static byte i;
    public static Map<String,List<Map<String,Float>>> readMprFile(File file) {
        if(!file.isFile()) return null;
        //第一层，图形类型
        Map<String,List<Map<String,Float>>> maps = new HashMap<>();
        //第二层，每个类型的图形数量
        //矩形数值
        List<Map<String,Float>> list1 = new ArrayList<>();
        //侧钉子数值
        List<Map<String,Float>> list2 = new ArrayList<>();
        //表面钉子数值
        List<Map<String,Float>> list3 = new ArrayList<>();
        List<Map<String,Float>> list4 = new ArrayList<>();
        //切割线
        List<Map<String,Float>> list5 = new ArrayList<>();
        //第三层，每个图形属性
        Map<String,Float> map = null;
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
                    map = new HashMap<>();
                    name = "rectangle";
                    flag = true;
                }else if(readLine.contains("BM=\"XP\"") || readLine.contains("BM=\"XM\"") || readLine.contains("BM=\"YM\"")) {
                    //侧钉子
                    map = new HashMap<>();
                    name = "BohrHoriz1";
                    flag = true;
                }else if(readLine.contains("BM=\"LS\"")) {
                    //表面钉子样式1
                    map = new HashMap<>();
                    name = "BohrVert1";
                    flag = true;
                }else if(readLine.contains("BM=\"LSU\"")) {
                    //表面钉子样式2
                    map = new HashMap<>();
                    name = "BohrVert2";
                    flag = true;
                }else if(readLine.contains("$E0")) {
                    //切割数据
                    map = new HashMap<>();
                    name = "Cutting1";
                    flag = true;
                    i = 0;
                }
                //检索到某图形数据时开启通道
                if(flag) {
                    //判断那个图形的通道，并对数据进行整理归纳储存于集合中
                    if("rectangle".equals(name)) {
                        flag = parseData(readLine, map, list1);
                    }else if("BohrHoriz1".equals(name)) {
                        flag = parseData(readLine, map, list2);
                    }else if("BohrVert1".equals(name)) {
                        flag = parseData(readLine, map, list3);
                    }else if("BohrVert2".equals(name)) {
                        flag = parseData(readLine, map, list4);
                    }else if("Cutting1".equals(name)) {
                        flag = parseData(readLine,map,list5);
                    }
                }
            }
            //当最终数据通道都关闭时，数据解析成功，反之解析失败
            if(flag) return null;
            //解析成功储存数据
            maps.put("rectangle",list1);
            maps.put("BohrHoriz1",list2);
            maps.put("BohrVert1",list3);
            maps.put("BohrVert2",list4);
            maps.put("Cutting1",list5);
            return maps;
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

    /**
     * 整理图形数据
     * @param data 原数据
     * @param map 以键值对储存对应图形属性
     * @param list 一个图形的属性整合
     * @return 当数据正常解析完成返回false关闭通道，此返回值可作为关闭解析通道的值
     */
    private static boolean parseData(String data,Map<String,Float> map,List<Map<String,Float>> list) {
        //矩形数据解析
        if(data.contains("_BSX=")) map.put("BSX",Float.parseFloat(data.split("=")[1]));
        if(data.contains("_BSY=")){
            map.put("BSY",Float.parseFloat(data.split("=")[1]));
            list.add(map);
            return false;
        }
        //钉子数据解析
        if (data.contains("XA=")) map.put("XA",patternText(data));
        if (data.contains("YA=")) map.put("YA",patternText(data));
        if (data.contains("DU=")) map.put("DU",patternText(data));
        if (data.contains("TI=")) {
            map.put("TI",patternText(data));
            list.add(map);
            return false;
        }
        //切割数据解析
        if (data.contains("X=") && !data.contains(".X=")) map.put("X"+i,Float.parseFloat(data.split("=")[1]));
        if (data.contains("Y=") && !data.contains(".Y=")) {
            map.put("Y"+i,Float.parseFloat(data.split("=")[1]));
            i++;
        }
        if(data.contains("]") || data.contains("[001")) {
            list.add(map);
            return false;
        }
        return true;
    }

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
    //****************************************************************************************************
    /**
     * 读取mpr文件
     */
    public static Map<String,List<Map<String,Float>>> readMprFile(List<String> data) {
        //第一层，图形类型
        Map<String,List<Map<String,Float>>> maps = new HashMap<>();
        //第二层，每个类型的图形数量
        //矩形数值
        List<Map<String,Float>> list1 = new ArrayList<>();
        //侧钉子数值
        List<Map<String,Float>> list2 = new ArrayList<>();
        //表面钉子数值
        List<Map<String,Float>> list3 = new ArrayList<>();
        List<Map<String,Float>> list4 = new ArrayList<>();
        //切割线
        List<Map<String,Float>> list5 = new ArrayList<>();
        //第三层，每个图形属性
        Map<String,Float> map = null;
        boolean flag = false;
        Iterator<String> iterator = data.iterator();
        String name = "";
        while(iterator.hasNext()) {
            String next = iterator.next();
            //查找图形组件
            if(next.contains("[H")) {
                //主图形
                map = new HashMap<>();
                name = "rectangle";
                flag = true;
            }else if(next.contains("BM=\"XP\"") || next.contains("BM=\"XM\"") || next.contains("BM=\"YM\"")) {
                //侧钉子
                map = new HashMap<>();
                name = "BohrHoriz1";
                flag = true;
            }else if(next.contains("BM=\"LS\"")) {
                //表面钉子样式1
                map = new HashMap<>();
                name = "BohrVert1";
                flag = true;
            }else if(next.contains("BM=\"LSU\"")) {
                //表面钉子样式2
                map = new HashMap<>();
                name = "BohrVert2";
                flag = true;
            }else if(next.contains("$E0")) {
                //切割数据
                map = new HashMap<>();
                name = "Cutting1";
                flag = true;
                i = 0;
            }
            //检索到某图形数据时开启通道
            if(flag) {
                //判断那个图形的通道，并对数据进行整理归纳储存于集合中
                if("rectangle".equals(name)) {
                    flag = parseData(next, map, list1);
                }else if("BohrHoriz1".equals(name)) {
                    flag = parseData(next, map, list2);
                }else if("BohrVert1".equals(name)) {
                    flag = parseData(next, map, list3);
                }else if("BohrVert2".equals(name)) {
                    flag = parseData(next, map, list4);
                }else if("Cutting1".equals(name)) {
                    flag = parseData(next,map,list5);
                }
            }
        }
        //当最终数据通道都关闭时，数据解析成功，反之解析失败
        if(flag) return null;
        //解析成功储存数据
        maps.put("rectangle",list1);
        maps.put("BohrHoriz1",list2);
        maps.put("BohrVert1",list3);
        maps.put("BohrVert2",list4);
        maps.put("Cutting1",list5);
        return maps;
    }
}