package com.aige.loveproduction.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatDateUtil {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.CHINA);

    /**
     * 日期格式化
     * @param date_str 需要格式化的字符串格式，例：2021-12-05T21:02:41.267
     * @param format_date 格式化格式：例：yyyy-MM-dd
     * @return 格式化结果，例：2021-12-05
     */
    public static String FormatDate(String date_str,String format_date){
        if (date_str == null) return "";
        Date parse;
        DateFormat dateFormat;
        try {
            parse = format.parse(date_str);
            dateFormat = new SimpleDateFormat(format_date,Locale.CHINA);
            return parse == null ? "":dateFormat.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
            return "日期解析异常";
        }
    }
}
