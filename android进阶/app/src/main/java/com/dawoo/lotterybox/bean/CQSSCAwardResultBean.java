package com.dawoo.lotterybox.bean;

/**
 * Created by b on 18-2-22.
 */

public class CQSSCAwardResultBean {

    /**
     * expect : 20171228060             //期号
     * code : cqssc                     //彩种代号
     * openCode : 1,3,6,7,8             //彩种类型代号
     * openingTime : 1514447400000      //开奖号码（多个号码时用逗号隔开）
     * closeTime : 1514447880000        //采集时间
     * gatherTime : 1514447951244       //开奖时间
     * openTime : 1514448000000         //开盘时间
     * type : ssc                       //封盘时间
     */

    private String expect;
    private String code;
    private String openCode;
    private long openingTime;
    private long closeTime;
    private long gatherTime;
    private long openTime;
    private String type;

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
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

    public long getGatherTime() {
        return gatherTime;
    }

    public void setGatherTime(long gatherTime) {
        this.gatherTime = gatherTime;
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
