package com.jasonzou.retrofitdemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目:  LawyerOA <br>
 * 类名:  com.lcoce.lawyeroa.utils.TimeStampUtil<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/5/29 14:03<br>
 */
public class TimeStampUtil {

    public static String timestampToDate(long timestamp, boolean hasMills, String format) {
        if (!hasMills) {
            timestamp *= 1000;
        }
        Date date;
        if (timestamp != 0l)
            date = new Date(timestamp);
        else
            date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);

    }

    /**
     * 最近更新时间：
     * 【领取大厅-动态】
     * 【刚刚：<1min】
     * 【x小时前|后：<1d】
     * 【x分钟前|后：<1h】
     * 【x分钟前|后：<1h】
     * 【昨|明天：=1d】
     * 【前|后天：=2d】
     * 【MM/dd HH:mm：< 1y;>2d】
     * 【yyyy/MM/dd HH:mm：>1y】
     *
     * @param timestamp
     * @param hasMills
     * @return
     * @author 邹旭
     */
    public static String waitingToServeTimeParser(long timestamp, boolean hasMills) {
        if (timestamp <= 0) {
            return "暂无";
        }
        String nowDate = timestampToDate(0, hasMills, "yyyy/MM/dd");
        String thatDate = timestampToDate(timestamp, hasMills, "yyyy/MM/dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        Date that = new Date(timestamp * (hasMills ? 1 : 1000));
        long hoursAbs = Math.abs(now.getTime() - that.getTime()) / 3600000;
        long minutesAbs = Math.abs(now.getTime() - that.getTime()) / 60000;
        long yearAbs = Math.abs(now.getYear() - that.getYear());
        try {
            long nowDateStamp = format.parse(nowDate).getTime();
            long thatTimestamp = format.parse(thatDate).getTime();
            long subTimestamp = nowDateStamp - thatTimestamp;//今日-那日
            long subDay = Math.abs(subTimestamp) / (24 * 3600 * 1000);
            if (!that.after(now)) {//当前时间 >= 给定的时间
                if (subDay == 0) {
                    if (hoursAbs > 0) {
                        return hoursAbs + "小时前";
                    } else if (hoursAbs == 0) {
                        if (minutesAbs > 0) {
                            return minutesAbs + "分钟前";
                        } else {
                            return "刚刚";
                        }
                    }
                } else if (subDay == 1) {
                    return "昨天 " + timestampToDate(timestamp, hasMills, "HH:mm");
                } else if (subDay == 2) {
                    return "前天 " + timestampToDate(timestamp, hasMills, "HH:mm");
                } else if (yearAbs > 0) {
                    return timestampToDate(timestamp, hasMills, "yyyy/MM/dd");
                } else if (yearAbs == 0) {
                    return timestampToDate(timestamp, hasMills, "MM/dd");
                }
            } else {//当前时间 < 给定的时间
                if (subDay == 0) {
                    if (hoursAbs > 0) {
                        return hoursAbs + "小时后";
                    } else if (hoursAbs == 0) {
                        if (minutesAbs > 0) {
                            return minutesAbs + "分钟后";
                        } else {
                            return "即将";
                        }
                    }
                } else if (subDay == 1) {
                    return "明天 " + timestampToDate(timestamp, hasMills, "HH:mm");
                } else if (subDay == 2) {
                    return "后天 " + timestampToDate(timestamp, hasMills, "HH:mm");
                } else if (yearAbs > 0) {
                    return timestampToDate(timestamp, hasMills, "yyyy/MM/dd");
                } else if (yearAbs == 0) {
                    return timestampToDate(timestamp, hasMills, "MM/dd");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未知时间";
    }
}
