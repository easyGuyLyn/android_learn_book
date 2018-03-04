package com.dawoo.gamebox.bean;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.dawoo.coretool.util.packageref.PackageInfoUtil;
import com.dawoo.gamebox.BuildConfig;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;

/**
 * 系统信息
 * 版本码
 * 版本号
 * mac待定
 * 识别号
 * 主题
 * 分辨率
 * Created by benson on 18-2-1.
 */

public class SysInfo {
    private String versionName;
    private String versionCode;
    private String terminal;
    private String theme;
    private String resolution;
    private String is_native;
    private String locale;

    public SysInfo() {
    }

    public SysInfo(String versionName, String versionCode, String terminal, String theme, String resolution, String isNative, String locale) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.terminal = terminal;
        this.theme = theme;
        this.resolution = resolution;
        this.is_native = isNative;
        this.locale = locale;
    }

    public void initSysInfo(Context context) {

        this.versionName = PackageInfoUtil.getVersionName(context);
        this.versionCode = PackageInfoUtil.getVersionCode(context);
        this.terminal = BuildConfig.terminal;
        this.theme = getThemeString(context);
        this.resolution = BuildConfig.resolution;
        this.is_native = "false";
        this.locale = PackageInfoUtil.getLanguage(context);
    }


    public String getIs_native() {
        return is_native;
    }

    public void setIs_native(String is_native) {
        this.is_native = is_native;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getThemeString(Context context) {
        int colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary);
        int colorBlue = ContextCompat.getColor(context, R.color.color_theme_blue);
        int colorRed = ContextCompat.getColor(context, R.color.color_theme_red);
        int colorGreen = ContextCompat.getColor(context, R.color.color_theme_green);
        int colorBlack = ContextCompat.getColor(context, R.color.color_theme_black);
        if (colorPrimary == colorBlue) {
            return ConstantValue.THEME_COLOR_BLUE;
        } else if (colorPrimary == colorRed) {
            return ConstantValue.THEME_COLOR_RED;
        } else if (colorPrimary == colorGreen) {
            return ConstantValue.THEME_COLOR_GREEN;
        } else if (colorPrimary == colorBlack) {
            return ConstantValue.THEME_COLOR_BALCK;
        }

        return ConstantValue.THEME_COLOR_BLUE;
    }

    public String getPostString() {
        StringBuilder sb = new StringBuilder();
        sb.append("terminal=").append(terminal).append("&")
                .append("version=").append(versionName).append("&")
                .append("theme=").append(theme).append("&")
                .append("resolution=").append(resolution).append("&")
                .append("is_native=").append("true").append("&")
                .append("locale=").append(locale);
        return sb.toString();
    }
}
