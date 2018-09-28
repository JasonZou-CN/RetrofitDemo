package com.jasonzou.retrofitdemo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：
 * 类描述：正则匹配
 * 创建人：jasonzou
 * 创建时间：2017/8/16 11:41
 * 修改人：jasonzou
 * 修改时间：2017/8/16 11:41
 * 修改备注：
 */
public class MMatcher {
    public static boolean isWord(String fileNameWithSuffix) {
        String pattern = ".+\\.(docx|doc|dot|dotx|docm|dotm)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileNameWithSuffix.toLowerCase());
        return m.find();
    }

    public static boolean isImage(String fileNameWithSuffix) {
        String pattern = ".+\\.(jpg|jpeg|png|bmp|gif)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileNameWithSuffix.toLowerCase());
        return m.find();
    }

    public static boolean isExcel(String fileNameWithSuffix) {
        String pattern = ".+\\.(xlsm|xlsx|xltx|xltm|xlsb|xlam|xls|xlt|xltx)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileNameWithSuffix.toLowerCase());
        return m.find();
    }

    public static boolean isPPT(String fileNameWithSuffix) {
        String pattern = ".+\\.(ppt|pptx)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileNameWithSuffix.toLowerCase());
        return m.find();
    }

    public static boolean isPDF(String fileNameWithSuffix) {
        String pattern = ".+\\.(pdf)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileNameWithSuffix.toLowerCase());
        return m.find();
    }

    public static boolean isEmail(String email) {
        String pattern = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email.toLowerCase());
        return m.find();
    }

    public static boolean isLawyerNumber(String lawyerNumber) {
        String pattern = "^[a-zA-Z0-9]+$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(lawyerNumber.toLowerCase());
        return m.find();
    }

    public static boolean isPhone(String lawyerNumber) {
        if (lawyerNumber == null || lawyerNumber.isEmpty()) {
            return false;
        } else {
            String pattern = "^1(3|4|5|7|8|9)\\d{9}$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(lawyerNumber.toLowerCase());
            return m.find();
        }
    }

    public static boolean isHttpOrHttpsUrl(String lawyerNumber) {
        String pattern = "^(http|https)://.+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(lawyerNumber.toLowerCase());
        return m.find();
    }

    /**
     * 匹配2f -> @
     *
     * @param source
     * @return
     */
    public static String replace2f2AtTag(String source, String nameStr) {
        String pattern = "(\\[2f.*\\])";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(source);
        if (m.find()) {
            return m.replaceFirst("@" + nameStr + "：");
        } else {
            return source;
        }
    }
}
