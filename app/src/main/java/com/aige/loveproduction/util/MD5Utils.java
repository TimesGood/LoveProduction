package com.aige.loveproduction.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密算法
 */
public class MD5Utils {
    public static String md5(String text) {
        MessageDigest digest = null;
        try {
            //拿到一个MD5转换器，
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());//digest.digest(参数)，方法得到的是个二进制byte数组，参数：把字符串转换成字节数组
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {//遍历字符数组，从第一个字节开始，对每一个字节，转换成16进制字符
                //0x表16进制数，0xff二进制就是11111111
                //&运算含义：如果对应的两个bit都是1，则那个bit结果为1，否则为0.即1&1=1,1&0=0,0&1=0,0&0=0
                //由于0xff最低的8位是1，因此numble中低8位中的&之后，如果原来是1，结果还是1，原来是0，结果位还是0.高于8位的，0xff都是0，所以无论是0还是1，结果都是0
                int number = b & 0xff;
                //Integer.toHexString()的参数是int，如果不进行&0xff，那么当一个byte转换成int时，由于int是32位，而byte只有8位，这时会进行补位
                //这种补位会造成误差。使用&0xff操作，可以把高24位位置0以避免这样错误的发生
                String hex = Integer.toHexString(number);
                //byte转换成16进制数，如果<16，那么是一个字节，如果在16~255之间，是两个字节，
                //通常我们希望用2个字符表示1个字节，前面不足补0.这样比较容易理解。
                //否则遇到字符串11，这到底是1个字节，值是17，还是2个字节，分别为1和1，值是2，会造成混淆
                //因此，对于2个字节，分别是1和1，我们就写成0101，遇到11，那一定是17；
                if(hex.length() == 1) {
                    sb.append("0" + hex);
                }else {
                    sb.append(hex);
                }
            }
            return sb.toString();//返回该加密的字符串
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
