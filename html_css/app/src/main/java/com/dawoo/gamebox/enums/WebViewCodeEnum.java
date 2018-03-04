package com.dawoo.gamebox.enums;

/**
 * WebView错误代码
 * Created by fei on 17-8-18.
 */
public enum WebViewCodeEnum {
    NOT_RESOLVED(-6, "net::ERR_NAME_NOT_RESOLVED");

    private int code;
    private String name;

    WebViewCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
