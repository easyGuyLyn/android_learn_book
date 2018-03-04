package com.dawoo.lotterybox.bean;

/**
 * Created by b on 18-2-23.
 */

public class ExpectDataBean {

    /**
     * expect : 20180110001
     * leftTime : 9668
     * leftOpenTime : 9188
     * opening : true
     */

    private String expect;          //期号
    private long leftTime;           //距离封盘时间
    private long leftOpenTime;       //距离下期开盘时间
    private boolean opening;        //是否开盘

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
    }

    public long getLeftOpenTime() {
        return leftOpenTime;
    }

    public void setLeftOpenTime(long leftOpenTime) {
        this.leftOpenTime = leftOpenTime;
    }

    public boolean isOpening() {
        return opening;
    }

    public void setOpening(boolean opening) {
        this.opening = opening;
    }
}
