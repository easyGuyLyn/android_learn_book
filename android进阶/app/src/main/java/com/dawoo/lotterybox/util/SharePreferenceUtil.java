package com.dawoo.lotterybox.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 保存一些基本信息
 * Created by benson on 17-12-27.
 */

public class SharePreferenceUtil {


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
     * 保存 玩法集合的 json串
     */

    public static void savePlayTypeListJson(Context context, String json) {
        SharedPreferences sp = context.getSharedPreferences("PlayTypeListJson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("PlayTypeListJson", json);
        editor.apply();
    }

    /**
     * 获取 玩法集合 的json
     *
     * @param context
     * @return
     */
    public static String getPlayTypeListJson(Context context) {
        SharedPreferences sp = context.getSharedPreferences("PlayTypeListJson", Context.MODE_PRIVATE);
        return sp.getString("PlayTypeListJson", "");
    }


    /**
     * 保存 玩法界面的 json串
     */

    public static void savePlayBeanJson(Context context, String json) {
        SharedPreferences sp = context.getSharedPreferences("PlayBeanJson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("PlayBeanJson", json);
        editor.apply();
    }

    /**
     * 获取 玩法界面 的json
     *
     * @param context
     * @return
     */
    public static String getPlayBeanListJson(Context context) {
        SharedPreferences sp = context.getSharedPreferences("PlayBeanJson", Context.MODE_PRIVATE);
        return sp.getString("PlayBeanJson", "");
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

}
