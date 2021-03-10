package com.kries.autotool.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class uity {
    public static String ComputeDeviceId(String token, String deviceName, String androidOS, String model, String imei) {
        String md5 = uity.Md5String(token + deviceName + androidOS + model + imei);
        return md5.substring(4, 20);
    }

    public static String Md5String(String password) {
        try {
            if (TextUtils.isEmpty(password)) {
                return "";
            }
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(password.getBytes());
                String result = "";
                for (byte b : bytes) {
                    String temp = Integer.toHexString(b & 0xff);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result += temp;
                }
                return result;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    public static String getIMEI(Context ctx) {
        String imei = "";
        try {
            TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = manager.getClass().getMethod("getImei", int.class);
            imei = (String) method.invoke(manager, 0);
            if (imei == null) {
                android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
                String macAddress = wifi.getConnectionInfo().getMacAddress();
                if (macAddress == "") {
                    return imei;
                }
                imei = Md5String(macAddress).substring(4,20);
            }
        } catch (Exception e) {}


        return imei;
    }

    public static String getMemory(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(ctx, mi.availMem);// 将获取的内存大小规格化
    }

    public static String getDisplayX(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();

        return String.valueOf(dm.widthPixels);
    }

    public static String getDisplayY(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();

        return String.valueOf(dm.heightPixels);
    }
}
