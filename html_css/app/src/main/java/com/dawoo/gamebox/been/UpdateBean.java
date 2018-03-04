package com.dawoo.gamebox.been;

/**
 * Created by lenovo on 2017/3/14.
 */

public class UpdateBean {

    /**
     * id : 3
     * appName : Gamebox-2.0.0.apk
     * appType : android
     * versionCode : 3
     * versionName : 2.0.0
     * appUrl : /app/app.apk
     * memo : This is 2.0.0
     * updateTime : 1481122007000
     * md5 : 9a1dd17fdab667c0af2ffd737c97c936
     */

    private int id;
    private String appName;
    private String appType;
    private int versionCode;
    private String versionName;
    private String appUrl;
    private String memo;
    private long updateTime;
    private String md5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
