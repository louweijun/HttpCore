package core.android.xuele.net.crhlibcore.uti;

import android.graphics.Color;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关于基本数据类型的强制转化。
 * 待补充 short byte 等数据类型的相互转化
 * Created by 陈俊杰 on 2015/10/10.
 * fixed by louweijun on 2016-8-26.
 */
public class ConvertUtil {

    /**
     * 精确到小数点后两位
     * 30.000 没办法转换成30.00,因此如果做展示用，使用{@link FormatUtils#formatFloat(float)}
     *
     * @param value 值
     * @return float 值 xx.xx
     */
    public static float toFloatWithPoint(float value) {
        return toFloatWithPoint(value, 2);
    }

    /**
     * 精确到小数点后n位
     * 30.000 没办法转换成30.00,因此如果做展示用，使用{@link FormatUtils#formatFloat(float)}
     *
     * @param value 值
     * @param point 保留的位数
     * @return 保留n位小数的值
     */
    public static float toFloatWithPoint(float value, int point) {
        try {
            String format = "%." + (point + 1) + "f";
            String formatValue = String.format(format, value).replaceAll(",", "");
            BigDecimal b = new BigDecimal(formatValue);
            return b.setScale(point, BigDecimal.ROUND_HALF_UP).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }

    }

    /**
     * 处理服务器返回值中需要强制转化的int型<br>
     * 可以处理95.0或者95%此类数据
     *
     * @param string 值
     * @return 考虑四舍五入
     */
    public static int toIntForServer(String string) {
        if (TextUtils.isEmpty(string))
            return 0;

        try {
            //服务端有可能会返回100.0这种整数类型，直接转会报错，需要截取掉.后面的数据
            //服务器数据有时会带上%
            if (string.endsWith("%")) {
                string = string.substring(0, string.length() - 1);
            }

            return (int) (Float.parseFloat(string) + 0.5);//需要考虑四舍五入
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * string 值转化为 long
     *
     * @param string 值
     * @return 如果string 值错误 返回0
     */
    public static long toLong(String string) {
        if (TextUtils.isEmpty(string))
            return 0;

        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * string 值转化为 int
     *
     * @param string 值
     * @return 如果string 值错误 返回0
     */
    public static int toInt(String string) {
        return toInt(string, 0);
    }

    /**
     * string 值转化为 int
     *
     * @param string       值
     * @param defaultValue defaultValue
     * @return 如果string 值错误 返回defaultValue
     */
    public static int toInt(String string, int defaultValue) {
        if (TextUtils.isEmpty(string)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * string 值转化为 float
     *
     * @param string 值
     * @return 如果string 值错误 返回0
     */
    public static float toFloat(String string) {
        if (TextUtils.isEmpty(string))
            return 0;

        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * string 值转化为 double
     *
     * @param string 值
     * @return 如果string 值错误 返回0
     */
    public static double toDouble(String string) {
        if (TextUtils.isEmpty(string))
            return 0;

        try {
            return Double.parseDouble(string);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 转换成double可以控制精度
     * 3.156 -> 3.15(true)：3.16(false)
     *
     * @param obj   字符串
     * @param scale 小数点后保留位数
     * @param isCut 是否截取，否则4舍五入
     * @return 精确的double值
     */
    public static double toDouble(String obj, int scale, boolean isCut) {
        double result = toDouble(obj);

        if (result == 0) return 0.00;

        BigDecimal bd = new BigDecimal(toDouble(obj));
        bd = bd.setScale(scale, isCut ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_HALF_UP);
//        double d = bd.doubleValue();
//        bd = null;
        return bd.doubleValue();
    }

    /**
     * 两个string 转化为 百分比
     *
     * @param str  被除数
     * @param str2 除数
     * @return 考虑四舍五入 数字错位返回 0
     */
    public static int toPercent(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (TextUtils.isEmpty(str2) || "0".equals(str2)) {
            return 0;
        }
        try {
            float f1 = Float.parseFloat(str);
            float f2 = Float.parseFloat(str2);
            return (int) (f1 / f2 * 100 + 0.5);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 半角转化为全角的方法
     *
     * @param input 半角字符串
     * @return 全角
     */
    public static String toSBC(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127 && c[i] > 32)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    public static String toDBC(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        String returnString = new String(c);
        //横线转减号（负号）
        returnString = returnString.replaceAll("—", "-");
        //过滤空格
        returnString = returnString.replaceAll(" ", "");
        //过滤搜狗减号
        returnString = returnString.replaceAll("﹣", "-");

        return returnString;
    }

    /**
     * 得到数据中的中文
     *
     * @return 中文文字
     */
    public static String getChinese(String data) {
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FFF]+");
        Matcher matcher = pattern.matcher(data);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }


    /**
     * 判断数据中是否有中文
     *
     * @return
     */
    public static boolean isChinese(String data) {
        String sb = getChinese(data);
        return sb.length() > 0;
    }

    /**
     * 判断数据中是否有中文
     *
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static String toMillionCount(String str) {
        int count = toInt(str);
        if (count < 10000) {
            return str;
        }
        if (count >= 10000 && count < 100000) {
            return String.format("%s万", toFloatWithPoint(toFloat(str) / 10000, 1)).replace(".0", "");
        }
        if (count >= 100000) {
            return String.format("%s万", (int) toFloatWithPoint(toFloat(str) / 10000, 0));
        }
        return str;
    }

    public static String toMillionCount(int str) {
        return toMillionCount(String.valueOf(str));
    }

    public static int getColor(String str) {
        if (TextUtils.isEmpty(str)) return -1;

        try {
            return Color.parseColor(str);
        } catch (Exception e) {
            return -1;
        }

    }
}
