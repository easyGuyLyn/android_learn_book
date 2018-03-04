package com.dawoo.gamebox.enums;

/**
 * 错误代码
 * Created by fei on 17-7-29.
 */
public enum CodeEnum {
    OK("OK", "请求正确"),
    SUCCESS("200", "请求成功"),
    S_DUE("600", "Session过期"),
    S_KICK_OUT("606", "Session过期"),
    DUE("604", "域名过期");

    private String code;
    private String name;

    CodeEnum(String code, String name) {
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
