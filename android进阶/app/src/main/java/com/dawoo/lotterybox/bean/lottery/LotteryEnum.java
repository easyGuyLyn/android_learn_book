package com.dawoo.lotterybox.bean.lottery;


public enum LotteryEnum implements ICodeEnum{
    JSK3("k3","jsk3", "江苏快3","1"),
    AHK3("k3","ahk3","安徽快3","1"),
    HBK3("k3","hbk3","湖北快3","1"),
    GXK3("k3","gxk3","广西快3","1"),

    HKLHC("lhc","hklhc", "香港六合彩","3"),

    XJSSC("ssc","xjssc", "新疆时时彩","1"),
    TJSSC("ssc","tjssc", "天津时时彩","1"),
    FFSSC("ssc","ffssc", "分分时时彩","3"),
    EFSSC("ssc","efssc", "二分时时彩","3"),
    SFSSC("ssc","sfssc", "三分时时彩","3"),
    WFSSC("ssc","wfssc", "五分时时彩","3"),
    CQSSC("ssc","cqssc", "重庆时时彩","1"),

    BJPK10("pk10","bjpk10", "北京PK10","1"),
    XYFT("pk10","xyft", "幸运飞艇","3"),
    JSPK10("pk10","jspk10", "极速PK10","3"),

    CQXYNC("sfc","cqxync", "重庆幸运农场","3"),
    GDKL10("sfc","gdkl10", "广东快乐十分","3"),

    BJKL8("keno","bjkl8", "北京快乐8","3"),
    XY28("xy28","xy28", "幸运28","3"),

    FC3D("pl3","fc3d", "福彩3D","1"),
    TCPL3("pl3","tcpl3", "体彩排列3","1");

    private String type;
    private String code;
    private String trans;
    private String genre;

    LotteryEnum(String type,String code, String trans,String genre) {
        this.type = type;
        this.code = code;
        this.trans = trans;
        this.genre = genre;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


}
