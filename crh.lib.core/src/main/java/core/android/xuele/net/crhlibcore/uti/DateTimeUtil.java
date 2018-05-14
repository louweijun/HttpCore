package core.android.xuele.net.crhlibcore.uti;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * date相关工具类<br>
 * Create 2014-5-28<br>
 * fixed by louweijun on 2016-8-26.<br>
 * Version 1.1.0
 */
public class DateTimeUtil {
    /**
     * 星期几数组
     */
    public static final String[] WEEKS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    public static final String[] WEEKS2 = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    //一天的毫秒常量
    public static final long DAY_MILLIS = 24L * 60L * 60L * 1000L;


    /**
     * The FieldPosition.
     */
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);
    /**
     * This Format for format the data to special format.
     */
    private final static Format dateFormat = new SimpleDateFormat("MMddHHmmssS", Locale.US);
    /**
     * This Format for format the number to special format.
     */
    private final static NumberFormat numberFormat = new DecimalFormat("0000");
    private static final int MAX = 9999;
    /**
     * This int is the sequence number ,the default value is 0.
     */
    private static int seq = 0;

    /**
     * 把符合日期格式的字符串转换为日期类型
     *
     * @param dateStr    符合日期格式的字符串
     * @param formatType 日期格式 {@link DateTimeUtil}中常量
     * @return 日期类型
     */
    public static Date stringToDate(String dateStr, String formatType) {
        Date d;
        SimpleDateFormat format = new SimpleDateFormat(formatType, Locale.getDefault());
        try {
            format.setLenient(false);//严格控制日期格式
            d = format.parse(dateStr);
        } catch (Exception e) {
            d = null;
        }
        return d;
    }

    /**
     * 把日期转换为指定格式的字符串
     *
     * @param date       日期
     * @param formatType 指定格式
     * @return 指定格式的字符串
     */
    public static String dateToString(Date date, String formatType) {
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(formatType, Locale.getDefault());
        try {
            result = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String chineseYearMonthDay(long dateLong) {
        return dateToString(new Date(dateLong), "yyyy年MM月dd日");
    }

    public static String toYYYYMMdd(long dateLong) {
        return dateToString(new Date(dateLong), "yyyy-MM-dd");
    }

    /**
     *
     * @param datetime
     * @return 2017-10-28 20:30
     */
    public static String toYYYYMMddHHmm(long datetime) {
        return dateToString(new Date(datetime), "yyyy-MM-dd HH:mm");
    }
    /**
     *年月日时分
     * @param datetime
     * @return 10-28 20:30
     */
    public static String toMMddHHmm(long datetime) {
        return dateToString(new Date(datetime), "MM-dd HH:mm");
    }

    public static String chineseYearMonthDayTime(long dateLong) {
        return dateToString(new Date(dateLong), "yyyy年MM月dd日HH时mm分");
    }

    public static String toHourMinsTime(long dateLong) {
        return dateToString(new Date(dateLong), "HH:mm");
    }

    /**
     * 把符合日期格式的字符串转换为long型时间格式
     *
     * @param dateStr    符合日期格式的字符串
     * @param formatType 日期格式 {@link DateTimeUtil}中常量
     * @return long型日期格式
     */
    public static long dateStrToLong(String dateStr, String formatType) {
        return stringToDate(dateStr, formatType).getTime();
    }

    /**
     * 把符合日期格式long日期格式转化为String日期格式
     *
     * @param dateLong   符合日期格式long
     * @param formatType 日期格式 {@link DateTimeUtil}中常量
     * @return long型日期格式
     */
    public static String longToDateStr(long dateLong, String formatType) {
        return dateToString(new Date(dateLong), formatType);
    }

    /**
     * 把符合日期格式long日期格式转化为String日期格式
     *
     * @param dateLong   符合日期格式long
     * @param formatType 日期格式 {@link DateTimeUtil}中常量
     * @return long型日期格式
     */
    public static String longToDateStr(String dateLong, String formatType) {
        return dateToString(new Date(ConvertUtil.toLong(dateLong)), formatType);
    }

    /**
     * 格式化时间 08月26日  星期五16:48
     *
     * @param date 时间
     **/
    public static String formatDateWithWeekAndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.format("%s  %s%s", new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(date), WEEKS[cal.get(Calendar.DAY_OF_WEEK) - 1], new
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
    }

    /**
     * 格式化时间 08月26日  星期五
     *
     * @param date 时间
     **/
    public static String formatDateWithWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.format("%s %s", new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(date), WEEKS[cal.get(Calendar.DAY_OF_WEEK) - 1]);
    }

    /**
     * 获取时间中的年份值
     *
     * @param date 时间
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获得当前时间几天以后的时间
     *
     * @param day 天数
     */
    public static Date getTimeAfterSomeDay(int day) {
        return getTimeAfterSomeDay(new Date(), day);
    }

    /**
     * 获得某个时间几天以后的时间
     *
     * @param day  天数
     * @param date 参照时间
     */
    public static Date getTimeAfterSomeDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    /**
     * 获得当前时间几个小时候的时间
     *
     * @param hour 小时数
     */
    public static Date getTimeAfterSomeHours(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    /**
     * 两个日期的年份差 注意：
     *
     * @param originalDate 大日期
     * @param compareDate  小日期
     * @return 只比较年份 2015-12-30和2016-01-01 返回也相差1年
     */
    public static int yearDiff(Date originalDate, Date compareDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(compareDate);
        int compareYear = aCalendar.get(Calendar.YEAR);
        aCalendar.setTime(originalDate);
        int originalYear = aCalendar.get(Calendar.YEAR);

        return originalYear - compareYear;
    }


    /**
     * 两个日期的月份差 注意：
     *
     * @param originalDate 大日期
     * @param compareDate  小日期
     * @return 只比较年份和月份
     */
    public static int monthDiff(Date originalDate, Date compareDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(compareDate);
        int compareYear = aCalendar.get(Calendar.YEAR);
        int compareMonth = aCalendar.get(Calendar.MONTH);

        aCalendar.setTime(originalDate);
        int originalYear = aCalendar.get(Calendar.YEAR);
        int originMonth = aCalendar.get(Calendar.MONTH);

        return (originalYear*12 + originMonth) - (compareYear*12 + compareMonth);
    }

    /**
     * 两个日期天数差
     *
     * @param originalDate 小日期
     * @param compareDate  大日期
     * @return 两个日期的天数差
     */
    public static int daysDiff(Date originalDate, Date compareDate) {
        return daysDiff(originalDate.getTime(), compareDate.getTime());
    }

    /**
     * 两个日期差的周数
     *
     * @param originalDate 小日期
     * @param compareDate  大日期
     * @return 两个日期的天数差
     */
    public static int weeksDiff(Date originalDate, Date compareDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(originalDate);
        int originalWeek = aCalendar.get(Calendar.WEEK_OF_YEAR);
        aCalendar.setTime(compareDate);
        int compareWeek = aCalendar.get(Calendar.WEEK_OF_YEAR);
        return compareWeek - originalWeek;
    }

    /**
     * 两个日期天数差
     *
     * @param originalDate 小日期
     * @param compareDate  大日期
     * @return 两个日期的天数差
     */
    public static int daysDiff(long originalDate, long compareDate) {
        return (int) ((getTimeInDay(compareDate) - getTimeInDay(originalDate)));
    }

    /**
     * 获取毫秒数，清零了时、分、秒字段上的数据
     * 可用于计算两个日期的天数差
     *
     * @param millionSeconds
     * @return
     */
    public static final long getTimeInDay(long millionSeconds) {
        Calendar originalCal = Calendar.getInstance();
        originalCal.setTimeInMillis(millionSeconds);
        originalCal.set(Calendar.HOUR_OF_DAY, 0);
        originalCal.set(Calendar.MINUTE, 0);
        originalCal.set(Calendar.SECOND, 0);
        originalCal.set(Calendar.MILLISECOND, 0);
        int dayUnit = 1000 * 60 * 60 * 24;
        return originalCal.getTimeInMillis() / dayUnit;
    }

    public static int minuteDiff(Date originalDate, Date compareDate) {
        return (int) (compareDate.getTime() / 60000L - originalDate.getTime() / 60000L);
    }

    /**
     * 判断一个日期和今天相差几天
     *
     * @param date 时间形
     * @return 相差的天数
     */
    public static int daysFromToday(Date date) {
        return daysDiff(new Date(), date);
    }

    /**
     * 判断一个日期和今天相差几天的绝对值
     * 用于判断当前日期前面的时间
     *
     * @param date 时间形
     * @return 相差的天数
     */

    public static int daysFromTodayAbs(Date date) {
        return Math.abs(daysDiff(new Date(), date));
    }

    /**
     * 通用友好的日期格式
     *
     * @param millsString String型毫秒
     * @return 返回格式
     * 刚刚(小于1分钟)，
     * 50分钟前(小于60分钟)，
     * 5小时前(小于24小时)，
     * 5天前(小于7天)，
     * 3周前(小于30天)，
     * 3个月前(小于1年)，
     * 2年前
     */
    public static String friendlyTime1(String millsString) {
        if (TextUtils.isEmpty(millsString)) {
            return "";
        }
        return friendlyTime1(ConvertUtil.toLong(millsString));
    }

    /**
     * 通用友好的日期格式
     *
     * @param mills 毫秒
     * @return 返回格式
     * <br>
     * 刚刚(小于1分钟)<br>
     * 50分钟前(小于60分钟)<br>
     * 5小时前(小于24小时)<br>
     * 5天前(小于7天)<br>
     * 3周前(小于30天)<br>
     * 3个月前(小于1年)<br>
     * 2年前
     */
    public static String friendlyTime1(long mills) {
        Date compareDate = new Date(mills);
        Date nowDate = new Date();
        int minuteDiff = minuteDiff(nowDate, compareDate);
        String timeWord = minuteDiff > 0 ? "后" : "前";

        minuteDiff = Math.abs(minuteDiff);

        if (minuteDiff < 1) {
            return "刚刚";
        }

        String str;

        if (minuteDiff < 60) {
            str = minuteDiff + "分钟";
        } else if (minuteDiff < 60 * 24) {
            str = minuteDiff / 60 + "小时";
        } else if (minuteDiff < 60 * 24 * 7) {
            str = minuteDiff / (60 * 24) + "天";
        } else if (minuteDiff < 60 * 24 * 30) {
            str = minuteDiff / (60 * 24 * 7) + "周";
        } else if (minuteDiff < 60 * 24 * 365) {
            str = minuteDiff / (60 * 24 * 30) + "个月";
        } else {
            str = minuteDiff / (60 * 24 * 365) + "年";
        }
        return str + timeWord;
    }

    /**
     * 通用友好的日期格式
     *
     * @param millsString String型毫秒
     * @return 友好时间格式
     * <p>
     * 后天13:15<br>明天13:15<br>
     * 今天13:15<br>昨天13:15<br>
     * 前天13:15<br>
     * 不在同一年 2015-06-15<br>
     * 其他   06-15 16-24<br>
     */
    public static String friendlyTime2(String millsString) {
        if (TextUtils.isEmpty(millsString)) {
            return "";
        }
        return friendlyTime2(new Date(ConvertUtil.toLong(millsString)));
    }

    /**
     * 通用友好的日期格式
     *
     * @param mills 毫秒
     * @return 友好时间格式
     * <p>
     * 后天13:15<br>明天13:15<br>
     * 今天13:15<br>昨天13:15<br>
     * 前天13:15<br>
     * 不在同一年 2015-06-15<br>
     * 其他   06-15 16-24<br>
     */
    public static String friendlyTime2(long mills) {
        return friendlyTime2(new Date(mills));
    }

    /**
     * 通用友好的日期格式
     *
     * @param compareDate 时间
     * @return 友好时间格式
     * <p>
     * 后天13:15<br>明天13:15<br>
     * 今天13:15<br>昨天13:15<br>
     * 前天13:15<br>
     * 不在同一年 2015-06-15<br>
     * 其他   06-15 16-24<br>
     */
    public static String friendlyTime2(Date compareDate) {
        Date nowDate = new Date();

        int dayDiff = daysDiff(nowDate, compareDate);
        String formatStr;

        switch (dayDiff) {
            case 2:
                formatStr = "后天 HH:mm";
                break;
            case 1:
                formatStr = "明天 HH:mm";
                break;
            case 0:
                formatStr = "HH:mm";
                break;
            case -1:
                formatStr = "昨天 HH:mm";
                break;
            case -2:
                formatStr = "前天 HH:mm";
                break;
            default:
                //不在同一年内
                if (yearDiff(nowDate, compareDate) > 0) {
                    formatStr = "yyyy-MM-dd";
                } else {
                    formatStr = "MM-dd HH:mm";
                }
                break;
        }
        return dateToString(compareDate, formatStr);
    }

    /**
     * 通用友好的日期格式
     *
     * @param millsString String型毫秒
     * @return 友好时间格式
     * <p>
     * 后天13:15<br>明天13:15<br>
     * 今天13:15<br>昨天13:15<br>
     * 前天13:15<br>
     * 不在同一年 2015日06月15日<br>
     * 其他   06月15日 16月24日<br>
     */
    public static String friendlyTimeVariant2(String millsString) {
        if (TextUtils.isEmpty(millsString)) {
            return "";
        }
        return friendlyTimeVariant2(new Date(ConvertUtil.toLong(millsString)));
    }

    /**
     * 通用友好的日期格式
     *
     * @param compareDate 时间
     * @return 友好时间格式
     * <p>
     * 后天13:15<br>明天13:15<br>
     * 今天13:15<br>昨天13:15<br>
     * 前天13:15<br>
     * 不在同一年 2015年06月15日<br>
     * 其他   06月15日 16月24日<br>
     */
    private static String friendlyTimeVariant2(Date compareDate) {
        Date nowDate = new Date();

        int dayDiff = daysDiff(nowDate, compareDate);
        String formatStr;

        switch (dayDiff) {
            case 2:
                formatStr = "后天 HH:mm";
                break;
            case 1:
                formatStr = "明天 HH:mm";
                break;
            case 0:
                formatStr = "HH:mm";
                break;
            case -1:
                formatStr = "昨天 HH:mm";
                break;
            case -2:
                formatStr = "前天 HH:mm";
                break;
            default:
                //不在同一年内
                if (yearDiff(nowDate, compareDate) > 0) {
                    formatStr = "yyyy年MM月dd日";
                } else {
                    formatStr = "MM月dd日 HH:mm";
                }
                break;
        }
        return dateToString(compareDate, formatStr);
    }

    /**
     * 成长轨迹中，时间统一按昨天、前天显示，一周内其他日期按星期显示，再往前显示日期
     * <p>
     * e.g今天是5.4星期四，5.3显示昨天，5.2显示前天，5.1显示星期一，4.30直接显示日期，5.7显示星期日
     *
     * @param times 时间毫秒
     * @return
     */
    public static String friendlyTime3(long times) {
        Date nowDate = new Date();
        Date compareDate = new Date(times);
        int dayDiff = daysDiff(nowDate, compareDate);
        int weekDiff = weeksDiff(nowDate, compareDate);
        int yearDiff = yearDiff(nowDate, compareDate);
        String formatStr;
        if (Math.abs(dayDiff) <= 2 || yearDiff > 0) {
            return friendlyTime2(compareDate);
        }
        //在同一周或者下一周
        if (weekDiff == 0 || weekDiff == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(compareDate);
            int index = calendar.get(Calendar.DAY_OF_WEEK);
            boolean isSunday = index == Calendar.SUNDAY;
            if (weekDiff == 0) {
                if (isSunday) {
                    formatStr = "MM-dd HH:mm";
                } else {
                    formatStr = String.format("%s HH:mm", WEEKS[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
                }
            } else if (isSunday) {
                formatStr = "星期日 HH:mm";
            } else {
                formatStr = "MM-dd HH:mm";
            }

        } else {
            formatStr = "MM-dd HH:mm";
        }
        return dateToString(compareDate, formatStr);
    }

    /**
     * 通用友好的日期格式
     *
     * @param mills 时间毫秒
     * @return 友好时间格式
     * <p>
     * 后天<br>明天<br>
     * 今天<br>昨天<br>
     * 前天<br>
     * 不在同一年 2015-06-15<br>
     * 其他   06-15 16-24<br>
     */
    public static String friendlyDate(long mills) {
        return friendlyTime2(new Date(mills));
    }


    public static String messageTimeFormat(String millsString) {
        if (TextUtils.isEmpty(millsString)) {
            return "";
        }
        return messageTimeFormat(ConvertUtil.toLong(millsString));
    }

    public static String messageTimeFormat(long mills) {
        return messageTimeFormat(new Date(mills));
    }

    /**
     * 通用友好的日期格式
     * <p>
     * 在同一年：3月7日
     * 不在同一年：2017年3月7日
     */
    public static String friendlyDate2(long mills) {
        Date nowDate = new Date();
        Date compareDate = new Date(mills);
        String formatStr;
        if (yearDiff(nowDate, compareDate) != 0) {
            //不在同一年内
            formatStr = "yyyy年M月d日";
        } else {
            formatStr = "M月d日";
        }
        return dateToString(compareDate, formatStr);
    }

    /**
     * 通用友好的日期格式
     * <p>
     * 在同一年：03-07
     * 不在同一年：2017-03-07
     */
    public static String friendlyDate3(long mills) {
        Date nowDate = new Date();
        Date compareDate = new Date(mills);
        String formatStr;
        if (yearDiff(nowDate, compareDate) != 0) {
            //不在同一年内
            formatStr = "yyyy-M-d";
        } else {
            formatStr = "MM-dd";
        }
        return dateToString(compareDate, formatStr);
    }

    /**
     * 通用友好的日期格式
     * <p>
     * 信息在在今天以内显示：今天12:29
     * 信息在今天之前并在今年以内显示：10月7日 18:21
     * 信息在今年以前显示：2015年7月7日
     */
    public static String messageTimeFormat(Date compareDate) {
        Date nowDate = new Date();

        int dayDiff = daysDiff(nowDate, compareDate);
        String formatStr;

        if (dayDiff == 0) {
            formatStr = "今天 HH:mm";
        } else {
            //不在同一年内
            if (yearDiff(nowDate, compareDate) != 0) {
                formatStr = "yyyy年M月d日";
            } else {
                formatStr = "M月d日 HH:mm";
            }
        }
        return dateToString(compareDate, formatStr);
    }

    /**
     * 将时间转换成分秒格式，如果大于一小时，则出现时
     * 如 61秒:01:01 ；3601秒：01:00:01
     *
     * @param timeMillis 时长，单位毫秒
     */
    public static String smartFormatMillionForClock(int timeMillis) {
        return smartFormatSecondsForClock(timeMillis / 1000);
    }

    /**
     * 将时间转换成分秒格式，如果大于一小时，则出现时
     * 如 61秒:01:01 ；3601秒：01:00:01
     *
     * @param timeMillis 时长，单位毫秒
     */
    public static String smartFormatMillionForClock(long timeMillis) {
        return smartFormatSecondsForClock((int) (timeMillis / 1000));
    }

    /***
     * 把01:00:01 转化为3601秒 01:01 转化为61秒
     */
    public static int smartFormatClockForSeconds(String clock) {
        String[] clockStrings = clock.split(":");
        int time = 0;
        if (clockStrings.length == 3) {
            time = ConvertUtil.toInt(clockStrings[0]) * 60 * 60 + ConvertUtil.toInt(clockStrings[1]) * 60 + ConvertUtil.toInt(clockStrings[2]);
        } else if (clockStrings.length == 2) {
            time = ConvertUtil.toInt(clockStrings[0]) * 60 + ConvertUtil.toInt(clockStrings[1]);
        }
        return time;
    }

    /**
     * 将时间转换成分秒格式
     * 如 00:01:01
     *
     * @param millionSeconds 时长，单位秒
     */
    public static String formatMillionForClock(int millionSeconds) {
        int hourUnit = 3600;
        int minuteUnit = 60;
        int timeSeconds = millionSeconds / 1000;

        int hours = timeSeconds / hourUnit;
        timeSeconds %= hourUnit;
        int minutes = timeSeconds / minuteUnit;
        timeSeconds %= minuteUnit;
        return String.format("%02d:%02d:%02d", hours, minutes, timeSeconds);
    }

    public static String formatMillionSecondsWithMinutesAndSeconds(int millionSeconds) {
        int timeSeconds = millionSeconds / 1000;

        int minuteUnit = 60;

        //否则只显示分秒部分
        int minutes = timeSeconds / minuteUnit;
        timeSeconds %= minuteUnit;
        return String.format("%02d:%02d", minutes, timeSeconds);
    }

    /**
     * 将时间转换成分秒格式，如果大于一小时，则出现时
     * 如 61秒:01:01 ；3601秒：01:00:01
     *
     * @param timeSeconds 时长，单位秒
     */
    public static String smartFormatSecondsForClock(int timeSeconds) {
        int hourUnit = 3600;
        int minuteUnit = 60;

        //如果大于一小时，则显示小时部分
        if (timeSeconds > hourUnit) {
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            timeSeconds %= minuteUnit;
            return String.format("%02d:%02d:%02d", hours, minutes, timeSeconds);
        } else {
            //否则只显示分秒部分
            int minutes = timeSeconds / minuteUnit;
            timeSeconds %= minuteUnit;
            return String.format("%02d:%02d", minutes, timeSeconds);
        }
    }

    /**
     * 将时间转换成时分格式
     * 如 1小时20分,不足一分钟显示秒
     *
     * @param timeSeconds 时长，单位秒
     */
    public static String formatSecondForClockFriendly(int timeSeconds) {
        int hourUnit = 3600;
        int minuteUnit = 60;
        int dayUnit = 24 * 3600;

        if (timeSeconds > dayUnit) {
            //如果大于一天，则显示天部分
            int days = timeSeconds / dayUnit;
            timeSeconds %= dayUnit;
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            return String.format("%d天%d小时%d分钟", days, hours, minutes);
        } else if (timeSeconds > hourUnit) {
            //如果大于一小时，则显示小时部分
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            return String.format("%d小时%d分钟", hours, minutes);
        } else if (timeSeconds >= 60) {
            //否则只显示分部分
            int minutes = timeSeconds / minuteUnit;
            int secound = timeSeconds % minuteUnit;
            return String.format("%d分钟%d秒", minutes, secound);
        } else {
            return String.format("%d秒", timeSeconds);
        }
    }

    /**
     * 将时间转换成时分格式
     * 如 1小时20分,不足一分钟显示秒； 不显示0的值如0分钟、0秒
     *
     * @param timeSeconds 时长，单位秒
     */
    public static String formatSecondForClockFriendly2(int timeSeconds) {
        final int minuteUnit = 60;
        final int hourUnit = 60 * minuteUnit;
        final int dayUnit = 24 * hourUnit;
        final StringBuilder stringBuilder = new StringBuilder();
        if (timeSeconds > dayUnit) {
            //如果大于一天，则显示天部分
            int days = timeSeconds / dayUnit;
            timeSeconds %= dayUnit;
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            if (days != 0) {
                stringBuilder.append(days).append("天");
            }
            if (hours != 0) {
                stringBuilder.append(hours).append("小时");
            }
            if (minutes != 0) {
                stringBuilder.append(minutes).append("分钟");
            }
        } else if (timeSeconds > hourUnit) {
            //如果大于一小时，则显示小时部分
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            if (hours != 0) {
                stringBuilder.append(hours).append("小时");
            }
            if (minutes != 0) {
                stringBuilder.append(minutes).append("分钟");
            }
        } else if (timeSeconds >= 60) {
            //否则只显示分部分
            int minutes = timeSeconds / minuteUnit;
            int secound = timeSeconds % minuteUnit;
            if (minutes != 0) {
                stringBuilder.append(minutes).append("分钟");
            }
            if (secound != 0) {
                stringBuilder.append(secound).append("秒");
            }
        } else {
            stringBuilder.append(timeSeconds).append("秒");
        }
        return stringBuilder.toString();
    }

    /**
     * 将时间转换成时分秒格式
     * 如 5天1小时20分3秒,不足一秒显示小数，如0.3秒
     */
    public static String formatForClockDecimal(long milliseconds) {
        int timeSeconds = (int) (milliseconds / 1000);

        int hourUnit = 3600;
        int minuteUnit = 60;
        int dayUnit = 24 * 3600;

        if (timeSeconds > dayUnit) {
            //如果大于一天，则显示天部分
            int days = timeSeconds / dayUnit;
            timeSeconds %= dayUnit;
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            timeSeconds %= minuteUnit;
            return String.format("%d天%d小时%d分钟%d秒", days, hours, minutes, timeSeconds);
        } else if (timeSeconds > hourUnit) {
            //如果大于一小时，则显示小时部分
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            timeSeconds %= minuteUnit;
            return String.format("%d小时%d分钟%d秒", hours, minutes, timeSeconds);
        } else if (timeSeconds >= 60) {
            //否则只显示分部分
            int minutes = timeSeconds / minuteUnit;
            timeSeconds %= minuteUnit;
            return String.format("%d分钟%d秒", minutes, timeSeconds);
        } else if (timeSeconds < 1) {
            // 小于1秒显示1位小数，如0.5秒
            double seconds = milliseconds / 1000.0;
            return String.format("%.1f秒", seconds);
        } else {
            return String.format("%d秒", timeSeconds);
        }
    }

    /**
     * 将时间转换成时分格式
     * 如 1小时20分,不足一分钟显示秒
     *
     * @param timeSeconds 时长，单位秒
     */
    public static String formatSecondForCoachCardView(int timeSeconds) {
        int hourUnit = 3600;
        int minuteUnit = 60;

        if (timeSeconds > hourUnit) {
            //如果大于一小时，则显示小时部分
            int hours = timeSeconds / hourUnit;
            timeSeconds %= hourUnit;
            int minutes = timeSeconds / minuteUnit;
            return String.format("%d小时%d分", hours, minutes);
        } else if (timeSeconds >= 60) {
            //否则只显示分部分
            int minutes = timeSeconds / minuteUnit;
            int secound = timeSeconds % minuteUnit;
            return String.format("%d分%d秒", minutes, secound);
        } else {
            return String.format("%d秒", timeSeconds);
        }
    }

    /**
     * 时间格式生成序列
     *
     * @return String
     */
    public static synchronized String generateSequenceNo() {
        Calendar rightNow = Calendar.getInstance();
        StringBuffer sb = new StringBuffer();
        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);
        numberFormat.format(seq, sb, HELPER_POSITION);
        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }
        return sb.toString();
    }

    /**
     * 由于规范未确定 暂时做区分
     */
    public static String friendlyTime3(String milliStr) {
        return friendlyTime2(milliStr);
    }

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month)) + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    /**
     * 返回周几
     *
     * @param time
     * @return
     */
    public static String getWeekByMillis(long time) {
        Calendar calendar = Calendar.getInstance();//获得一个日历
        calendar.setTimeInMillis(time);
        return WEEKS2[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }


    /**
     * 获得一周的时间格式 例如:9月1日- 9月7日
     */
    public static String getWeekString(long date) {
        Date startDate = DateTimeUtil.getFirstDayOfWeek(date);
        Date endDate = DateTimeUtil.getLastDayOfWeek(date);

        return String.format(Locale.CHINESE, "%s — %s", dateToString(startDate, "MM月dd日"), dateToString(endDate, "MM月dd日"));
    }


    /**
     * 获得一周的时间格式 例如:9月1日- 9月7日
     */
    public static String getWeekString(long date, long endTime) {
        return getWeekString(date, endTime, "MM月dd日");
    }

    public static String getWeekString(long date, long endTime, String formatType) {
        Date startDate = DateTimeUtil.getFirstDayOfWeek(date);
        Date endDate = DateTimeUtil.getLastDayOfWeek(date);

        if (endDate.getTime() > endTime) {
            endDate.setTime(endTime);
        }
        return String.format(Locale.CHINESE, "%s — %s", dateToString(startDate, formatType), dateToString(endDate, formatType));
    }


    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(long date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    // 获取当前时间所在周的结束日期
    public static Date getLastDayOfWeek(long date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }


    /**
     * 返回每个月几号
     *
     * @param time
     * @return
     */
    public static String getDayByMillis(long time) {
        Calendar calendar = Calendar.getInstance();//获得一个日历
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

    public static Calendar parseFrom(String pubTime) {
        try {
            Long lastTime = Long.parseLong(pubTime);
            Calendar lastCal = Calendar.getInstance();

            lastCal.setTimeInMillis(lastTime);

            return lastCal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeDisPlay(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(calendar.getTime());
        }
        return null;
    }

    /**
     * 得到制定日期日期几个月后（前）的日期 参数传负数即可
     */

    public static String getAfterMonth(int month, Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        c.add(Calendar.MONTH, month);//在日历的月份上增加
        String strDate = sdf.format(c.getTime());//的到你想要
        return strDate;

    }

    /**
     * 返回小时分钟，24小时制
     * 10:25，15:23
     */
    public static String formatTimeMinute(long timeMills) {
        return dateToString(new Date(timeMills), "HH:mm");
    }

    public static boolean isSameDay(String lastPubTime, String nowPubTime) {
        return isSameDay(ConvertUtil.toLong(lastPubTime), ConvertUtil.toLong(nowPubTime));
    }

    public static boolean isSameDay(long lastTime, long nowTime) {
        try {
            Calendar lastCal = Calendar.getInstance();
            Calendar nowCal = Calendar.getInstance();

            lastCal.setTimeInMillis(lastTime);
            nowCal.setTimeInMillis(nowTime);

            if (lastCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR)) {
                if (lastCal.get(Calendar.MONTH) == nowCal.get(Calendar.MONTH)) {
                    if (lastCal.get(Calendar.DAY_OF_MONTH) == nowCal.get(Calendar.DAY_OF_MONTH)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isSameYear(String lastPubTime, String nowPubTime) {
        return isSameYear(ConvertUtil.toLong(lastPubTime), ConvertUtil.toLong(nowPubTime));
    }

    public static boolean isSameYear(long lastTime, long nowTime) {
        try {
            Calendar lastCal = Calendar.getInstance();
            Calendar nowCal = Calendar.getInstance();

            lastCal.setTimeInMillis(lastTime);
            nowCal.setTimeInMillis(nowTime);

            if (lastCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}