package com.dawoo.gamebox.update.model;

/**
 * 更新信息
 * Created by fei on 16-11-21.
 */
public class UpdateInfo {
    /** 版本代号 */
    public Integer versionCode;
    /** 版本名称 */
    public String versionName;
    /** 下载地址 */
    public String appUrl;
    /** 更新信息 */
    public String memo;
    /** 更新时间 */
    public Long updateTime;
    /** 应用名称 */
    public String appName;

    @Override
    public String toString() {
        return "版本代号: " + versionCode + ", 版本名称: " + versionName + ", 下载地址: " + appUrl
                + ", 更新信息: " + memo + ", 更新时间: " + updateTime + ", 应用名称: " + appName;
    }
}
