package com.dawoo.gamebox.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.dawoo.gamebox.BoxApplication;

/**
 * 保存一些基本信息
 * Created by benson on 17-12-27.
 */

public class SharePreferenceUtil {
    public static final String GESTURE_FLG = "gesture_flg"; // 判断是否设置有手势密码
    public static final String GESTURE_TIME = "gesture_time"; // 手势密码输入错误超过5次时间

    private static SharedPreferences getSharedPreferences() {
        BoxApplication app = (BoxApplication) BoxApplication.getContext();
        return app.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
    }

    /**
     * 获取保存的域名
     *
     * @param context
     * @return
     */
    public static String getDomain(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        return sp.getString("domain", "");
    }

    /**
     * 保存检测成功后的域名
     *
     * @param context
     * @param domain
     */
    public static void saveDomain(Context context, String domain) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("domain", domain);
        editor.apply();
    }

    /**
     * 获取声音状态
     *
     * @param context
     * @return
     */
    public static Boolean getVoiceStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_Info", Context.MODE_PRIVATE);
        return sp.getBoolean("VoiceStatus", true);
    }

    /**
     * 保存声音状态
     *
     * @param context
     */
    public static void saveVoiceStatus(Context context, boolean isOpen) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_Info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("VoiceStatus", isOpen);
        editor.apply();
    }

    /**
     * 获取时区
     *
     * @param context
     * @return
     */
    public static String getTimeZone(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        return sp.getString("timeZone", "GMT+08:00");
    }

    /**
     * 保存时区
     *
     * @param context
     * @param timeZone
     */
    public static void saveTimeZone(Context context, String timeZone) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("timeZone", timeZone);
        editor.apply();
    }

    /**
     * 保存存款的url
     */
    public static String getDepositUrl(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        return sp.getString("depositUrl", "");
    }

    /**
     * 保存存款的url
     *
     * @param context
     * @param url
     */
    public static void saveDepositUrl(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("depositUrl", url);
        editor.apply();
    }

    /**
     * 保存额度转换的url
     */
    public static String getQuotaUrl(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        return sp.getString("quotaUrl", "");
    }

    /**
     * 保存额度转换的url
     *
     * @param context
     * @param url
     */
    public static void saveQuotaUrl(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("quotaUrl", url);
        editor.apply();
    }

    /**
     * 保存常见问题的url
     */
    public static String getHelpUrl(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        return sp.getString("HelpUrl", "");
    }

    /**
     * 保存常见问题的url
     *
     * @param context
     * @param url
     */
    public static void saveHelpUrl(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("HelpUrl", url);
        editor.apply();
    }

    /**
     * 保存注册条款的url
     */
    public static String getTermsUrl(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        return sp.getString("TermsUrl", "");
    }

    /**
     * 保存注册条款的url
     *
     * @param context
     * @param url
     */
    public static void saveTermsUrl(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("TermsUrl", url);
        editor.apply();
    }

    /**
     * 保存关于我们的url
     */
    public static String getAboutsUrl(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        return sp.getString("TermsUrl", "");
    }

    /**
     * 保存关于我们的url
     *
     * @param context
     * @param url
     */
    public static void saveAboutsUrl(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences("Box_Base_NetInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("AboutsUrl", url);
        editor.apply();
    }

    /**
     * 保存是否手勢密碼的flag
     *
     * @param flg
     */
    public static void putGestureFlag(boolean flg) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(GESTURE_FLG, flg);
        editor.commit();
    }

    /**
     * 获取是否有手势密码
     *
     * @return
     */
    public static boolean getGestureFlag() {
        return getSharedPreferences()
                .getBoolean(GESTURE_FLG, false);
    }

    public static void putGestureTime(long time) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(GESTURE_TIME, time);
        editor.commit();
    }

    public static long getGestureTime() {
        return getSharedPreferences()
                .getLong(GESTURE_TIME, 0);
    }


}
