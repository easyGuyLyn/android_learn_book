package com.dawoo.lotterybox.bean.lottery;

/**
 * 获取近期开奖结果（已开奖）
 * Created by benson on 18-2-8.
 */

public class Handicap extends BaseHandicap{

    /**
     * code : cqssc                   彩种代号
     * openCode : 1,3,6,7,8           开奖号码（多个号码时用逗号隔开）
     * openingTime : 1514447400000    开盘时间
     * closeTime : 1514447880000      封盘时间
     * gatherTime : 1514447951244     采集时间
     * openTime : 1514448000000       开奖时间
     * type : ssc                     彩种类型代号
     */

    protected String code;
    protected String openCode;
    protected long openingTime;
    protected long closeTime;
    protected long gatherTime;
    protected long openTime;
    protected String type;

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
