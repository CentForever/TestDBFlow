package com.example.testdbflow.xlog;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ZoneUtils {

    public static boolean is24Hour(Context mContext) {
        ContentResolver cv = mContext.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv,
                android.provider.Settings.System.TIME_12_24);
        if (strTimeFormat != null && strTimeFormat.equals("24")) {
            return true;
        }
        return false;
    }


    public static String currentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.format(date);
    }


    public static String getTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }


    public static boolean isTimeZoneAuto(Context mContext) {
        try {
            return android.provider.Settings.Global.getInt(mContext.getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME_ZONE) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
