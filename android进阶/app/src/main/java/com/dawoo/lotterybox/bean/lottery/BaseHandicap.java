package com.dawoo.lotterybox.bean.lottery;

/**
 * 盘口
 * Created by benson on 18-2-8.
 */

public class BaseHandicap {
    /**
     * expect : 20180110001        期号
     * leftTime : 9668             距离封盘时间
     * leftOpenTime : 9188         距离下期开盘时间
     * opening : true              是否开盘
     */

    protected String expect;
    protected int leftTime;
    protected int leftOpenTime;
    protected boolean opening;

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public int getLeftOpenTime() {
        return leftOpenTime;
    }

    public void setLeftOpenTime(int leftOpenTime) {
        this.leftOpenTime = leftOpenTime;
    }

    public boolean isOpening() {
        return opening;
    }

    public void setOpening(boolean opening) {
        this.opening = opening;
    }
}
