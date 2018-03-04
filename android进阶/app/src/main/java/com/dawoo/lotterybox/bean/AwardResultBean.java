package com.dawoo.lotterybox.bean;

/**
 * Created by b on 18-2-23.
 */

public class AwardResultBean {
    /**
     * expect : 20180110074
     * fmOpenTime : 2018-01-10 18:20:00
     * code : cqssc
     * openCode :
     * codeMemo : 重庆时时彩
     * openingTime : 1515579000000
     * closeTime : 1515579480000
     * leftOpenTime : 0
     * leftTime : 0
     * openTime : 1515579600000
     * type : ssc
     */

    private String expect;             //期号
    private String fmOpenTime;          //彩种代号
    private long openTime;              //开奖时间
    private String type;                //彩种类型
    private String code;                //彩种类型
    private String openCode;            //开奖结果（多个号码时，用逗号隔开）
    private String codeMemo;            //游戏种类国际化（）
    private long openingTime;           //开盘时间
    private long closeTime;             //封盘时间
    private int leftOpenTime;           //距离开盘时间
    private int leftTime;               //距离封盘时间


    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getFmOpenTime() {
        return fmOpenTime;
    }

    public void setFmOpenTime(String fmOpenTime) {
        this.fmOpenTime = fmOpenTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenCode() {
        return openCode;
    }

    public void setOpenCode(String openCode) {
        this.openCode = openCode;
    }

    public String getCodeMemo() {
        return codeMemo;
    }

    public void setCodeMemo(String codeMemo) {
        this.codeMemo = codeMemo;
    }

    public long getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(long openingTime) {
        this.openingTime = openingTime;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public int getLeftOpenTime() {
        return leftOpenTime;
    }

    public void setLeftOpenTime(int leftOpenTime) {
        this.leftOpenTime = leftOpenTime;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
