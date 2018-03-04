package com.dawoo.gamebox.enums;

/**
 * 站点类型
 * Created by fei on 17-7-27.
 */
public enum SiteTypeEnum {
    INTEGRATED("integrated", "综合站"),
    LIVE("live", "真人站"),
    CASINO("casino", "电子站"),
    SPORT("sport", "体育站"),
    LOTTERY("lottery", "彩票站");

    private String code;
    private String name;

    SiteTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
