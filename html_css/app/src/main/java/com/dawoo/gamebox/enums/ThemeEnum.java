package com.dawoo.gamebox.enums;

/**
 * 主题
 * Created by fei on 17-8-9.
 */
public enum ThemeEnum {
    DEFAULT("default.skin", "默认主题"),
    BLUE("blue.skin", "UED蓝"),
    GREEN("green.skin", "BET365绿"),
    PINK("pink.skin", "Phoenix粉"),
    LOTTERY("lottery.skin", "默认彩票主题");

    private String code;
    private String name;

    ThemeEnum(String code, String name) {
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
