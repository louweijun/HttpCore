/*
 * Create by liuph@cairenhui.com on 17-11-30 上午10:48
 * Copyright (c) 2017. All rights reserved
 *
 * Last modified 17-11-30 上午9:55
 */

package core.android.xuele.net.crhlibcore.uti;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DeviceUtil {

    public static String getAndroidId(Context context) {
        String id = getUniqueID(context);
        if (TextUtils.isEmpty(id)) {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure
                    .ANDROID_ID);
        }
        return id;
    }

    private static String getUniqueID(Context context) {
        String telephonyDeviceId = "";
        String androidDeviceId = "";
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                    .TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                    PackageManager.PERMISSION_GRANTED) {
                telephonyDeviceId = tm.getDeviceId();
            } else {
                androidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings
                        .Secure.ANDROID_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(telephonyDeviceId)) return androidDeviceId;

        return telephonyDeviceId;
    }


    public static String getImei(Activity context) {
        String imei = "000000";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE,
                    context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                Log.d("info", ">>>>>>>>>>" + imei);
            } else {
                Log.e("test", "no permission");
                context.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 999);
            }
        } else {
            imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();
        }
        return imei;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private static final String INSTALL_ID_KEY = "INSTALL_ID_KEY";
    public static String versionName;
    public static String appName;
    private static String mInstallId;

    /**
     * 获得sdk版本
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获得sdk版本
     */
    public static String getSDKVersionString() {
        return Build.VERSION.SDK;
    }
    /**
     * 获得手机厂商名称
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获得机型
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 判断是否是平板（官方用法）
     *
     * @return boolean 返回类型
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * app是否安装
     *
     * @param context     上下文
     * @param packageName 程序的报名
     * @return true 程序已安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName) != null;
    }

    /**
     * 获得当前进程名字
     *
     * @return 当前进程名称
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (mActivityManager.getRunningAppProcesses() == null)
            return "";

        Iterator iterator = mActivityManager.getRunningAppProcesses().iterator();
        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!iterator.hasNext()) {
                return null;
            }
            appProcess = (ActivityManager.RunningAppProcessInfo) iterator.next();
        } while (appProcess.pid != pid);
        return appProcess.processName;
    }

    /**
     * 判断当前线程名称是否和包名一致
     *
     * @param context 上下文
     * @return true 一致
     */
    public static boolean isDefaultProcess(Context context) {
        String processName = getCurProcessName(context);

        if (TextUtils.isEmpty(processName))
            return false;

        return processName.equals(context.getPackageName());
    }

    /**
     * 判断是否是debug模式
     *
     * @return true debug ,false release
     */
    public static boolean isApkDebugAble(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断程序是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        String packageName = context.getPackageName();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    /**
     * 获取 version code
     */
    public static int getVersionCode(Context context) {
        // 获取packagemanager的实例
        int versionCode = 0;
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取version name
     */
    public static String getVersionName(Context context) {
        if (versionName != null) {
            return versionName;
        }
        versionName = "";
        if (context != null) {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo;
            try {
                packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                versionName = packInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionName;
    }

    public static String getAppName(Application application) {
        if (appName != null) {
            return appName;
        }
        appName = "";
        if (application != null) {
            try {
                PackageManager packageManager = application.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
                int labelRes = packageInfo.applicationInfo.labelRes;
                appName = application.getResources().getString(labelRes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appName;
    }

    /**
     * 获取电话号码
     */
    public String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return telephonyManager.getLine1Number();
        }
        return "0";
    }

    public static String getMacAddr(Context context) {
        // 获取mac地址：
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", "");
                else
                    return macAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }


//    public static String getMac(Context context) {
//
//        String strMac = null;
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            Log.e("=====", "6.0以下");
//            Toast.makeText(context, "6.0以下", Toast.LENGTH_SHORT).show();
//            strMac = getLocalMacAddressFromWifiInfo(context);
//            return strMac;
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
//                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.e("=====", "6.0以上7.0以下");
//            Toast.makeText(context, "6.0以上7.0以下", Toast.LENGTH_SHORT).show();
//            strMac = getMacAddress(context);
//            return strMac;
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Log.e("=====", "7.0以上");
//            if (!TextUtils.isEmpty(getMacAddress())) {
//                Log.e("=====", "7.0以上1");
//                Toast.makeText(context, "7.0以上1", Toast.LENGTH_SHORT).show();
//                strMac = getMacAddress();
//                return strMac;
//            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
//                Log.e("=====", "7.0以上2");
//                Toast.makeText(context, "7.0以上2", Toast.LENGTH_SHORT).show();
//                strMac = getMachineHardwareAddress();
//                return strMac;
//            } else {
//                Log.e("=====", "7.0以上3");
//                Toast.makeText(context, "7.0以上3", Toast.LENGTH_SHORT).show();
//                strMac = getLocalMacAddressFromBusybox();
//                return strMac;
//            }
//        }
//
//        return "02:00:00:00:00:00";
//    }
//
//    /**
//     * 根据wifi信息获取本地mac
//     * @param context
//     * @return
//     */
//    public static String getLocalMacAddressFromWifiInfo(Context context){
//        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo winfo = wifi.getConnectionInfo();
//        String mac =  winfo.getMacAddress();
//        return mac;
//    }
//
//    /**
//     * android 6.0及以上、7.0以下 获取mac地址
//     *
//     * @param context
//     * @return
//     */
//    public static String getMacAddress(Context context) {
//
//        // 如果是6.0以下，直接通过wifimanager获取
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            String macAddress0 = getMacAddress0(context);
//            if (!TextUtils.isEmpty(macAddress0)) {
//                return macAddress0;
//            }
//        }
//
//        String str = "";
//        String macSerial = "";
//        try {
//            Process pp = Runtime.getRuntime().exec(
//                    "cat /sys/class/net/wlan0/address");
//            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//
//            for (; null != str; ) {
//                str = input.readLine();
//                if (str != null) {
//                    macSerial = str.trim();// 去空格
//                    break;
//                }
//            }
//        } catch (Exception ex) {
//            Log.e("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());
//        }
//        if (macSerial == null || "".equals(macSerial)) {
//            try {
//                return loadFileAsString("/sys/class/net/eth0/address")
//                        .toUpperCase().substring(0, 17);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("----->" + "NetInfoManager",
//                        "getMacAddress:" + e.toString());
//            }
//
//        }
//        return macSerial;
//    }
//
//    private static String getMacAddress0(Context context) {
//        if (isAccessWifiStateAuthorized(context)) {
//            WifiManager wifiMgr = (WifiManager) context
//                    .getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = null;
//            try {
//                wifiInfo = wifiMgr.getConnectionInfo();
//                return wifiInfo.getMacAddress();
//            } catch (Exception e) {
//                Log.e("----->" + "NetInfoManager",
//                        "getMacAddress0:" + e.toString());
//            }
//
//        }
//        return "";
//
//    }
//
//    /**
//     * Check whether accessing wifi state is permitted
//     *
//     * @param context
//     * @return
//     */
//    private static boolean isAccessWifiStateAuthorized(Context context) {
//        if (PackageManager.PERMISSION_GRANTED == context
//                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
//            Log.e("----->" + "NetInfoManager", "isAccessWifiStateAuthorized:"
//                    + "access wifi state is enabled");
//            return true;
//        } else
//            return false;
//    }
//
//    private static String loadFileAsString(String fileName) throws Exception {
//        FileReader reader = new FileReader(fileName);
//        String text = loadReaderAsString(reader);
//        reader.close();
//        return text;
//    }
//
//    private static String loadReaderAsString(Reader reader) throws Exception {
//        StringBuilder builder = new StringBuilder();
//        char[] buffer = new char[4096];
//        int readLength = reader.read(buffer);
//        while (readLength >= 0) {
//            builder.append(buffer, 0, readLength);
//            readLength = reader.read(buffer);
//        }
//        return builder.toString();
//    }
//    /**
//     * 根据IP地址获取MAC地址
//     *
//     * @return
//     */
//    public static String getMacAddress() {
//        String strMacAddr = null;
//        try {
//            // 获得IpD地址
//            InetAddress ip = getLocalInetAddress();
//            byte[] b = NetworkInterface.getByInetAddress(ip)
//                    .getHardwareAddress();
//            StringBuffer buffer = new StringBuffer();
//            for (int i = 0; i < b.length; i++) {
//                if (i != 0) {
//                    buffer.append(':');
//                }
//                String str = Integer.toHexString(b[i] & 0xFF);
//                buffer.append(str.length() == 1 ? 0 + str : str);
//            }
//            strMacAddr = buffer.toString().toUpperCase();
//        } catch (Exception e) {
//
//        }
//
//        return strMacAddr;
//    }
//
//    /**
//     * 获取移动设备本地IP
//     *
//     * @return
//     */
//    private static InetAddress getLocalInetAddress() {
//        InetAddress ip = null;
//        try {
//            // 列举
//            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
//                    .getNetworkInterfaces();
//            while (en_netInterface.hasMoreElements()) {// 是否还有元素
//                NetworkInterface ni = (NetworkInterface) en_netInterface
//                        .nextElement();// 得到下一个元素
//                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
//                while (en_ip.hasMoreElements()) {
//                    ip = en_ip.nextElement();
//                    if (!ip.isLoopbackAddress()
//                            && ip.getHostAddress().indexOf(":") == -1)
//                        break;
//                    else
//                        ip = null;
//                }
//
//                if (ip != null) {
//                    break;
//                }
//            }
//        } catch (SocketException e) {
//
//            e.printStackTrace();
//        }
//        return ip;
//    }
//
//    /**
//     * 获取本地IP
//     *
//     * @return
//     */
//    private static String getLocalIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf
//                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 根据busybox获取本地Mac
//     *
//     * @return
//     */
//    public static String getLocalMacAddressFromBusybox() {
//        String result = "";
//        String Mac = "";
//        result = callCmd("busybox ifconfig", "HWaddr");
//        // 如果返回的result == null，则说明网络不可取
//        if (result == null) {
//            return "网络异常";
//        }
//        // 对该行数据进行解析
//        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
//        if (result.length() > 0 && result.contains("HWaddr") == true) {
//            Mac = result.substring(result.indexOf("HWaddr") + 6,
//                    result.length() - 1);
//            result = Mac;
//        }
//        return result;
//    }
//
//    private static String callCmd(String cmd, String filter) {
//        String result = "";
//        String line = "";
//        try {
//            Process proc = Runtime.getRuntime().exec(cmd);
//            InputStreamReader is = new InputStreamReader(proc.getInputStream());
//            BufferedReader br = new BufferedReader(is);
//
//            while ((line = br.readLine()) != null
//                    && line.contains(filter) == false) {
//                result += line;
//            }
//
//            result = line;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 获取设备HardwareAddress地址
//     *
//     * @return
//     */
//    public static String getMachineHardwareAddress() {
//        Enumeration<NetworkInterface> interfaces = null;
//        try {
//            interfaces = NetworkInterface.getNetworkInterfaces();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        String hardWareAddress = null;
//        NetworkInterface iF = null;
//        if (interfaces == null) {
//            return null;
//        }
//        while (interfaces.hasMoreElements()) {
//            iF = interfaces.nextElement();
//            try {
//                hardWareAddress = bytesToString(iF.getHardwareAddress());
//                if (hardWareAddress != null)
//                    break;
//            } catch (SocketException e) {
//                e.printStackTrace();
//            }
//        }
//        return hardWareAddress;
//    }
//
//    /***
//     * byte转为String
//     *
//     * @param bytes
//     * @return
//     */
//    private static String bytesToString(byte[] bytes) {
//        if (bytes == null || bytes.length == 0) {
//            return null;
//        }
//        StringBuilder buf = new StringBuilder();
//        for (byte b : bytes) {
//            buf.append(String.format("%02X:", b));
//        }
//        if (buf.length() > 0) {
//            buf.deleteCharAt(buf.length() - 1);
//        }
//        return buf.toString();
//    }
}
