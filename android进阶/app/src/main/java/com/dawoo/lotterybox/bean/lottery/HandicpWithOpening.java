package com.dawoo.lotterybox.bean.lottery;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 获取近期数据（包含未开奖期）
 * Created by benson on 18-2-8.
 */

public class HandicpWithOpening extends Handicap implements MultiItemEntity {

    /**
     * fmOpenTime : 2018-01-10 18:20:00  时间文本（yyyy-mm-dd hh:mm:ss）
     * codeMemo : 重庆时时彩               游戏种类国际化（）
     */

    protected String fmOpenTime;
    protected String codeMemo;
    private int itemType;

    public String getFmOpenTime() {
        return fmOpenTime;
    }

    public void setFmOpenTime(String fmOpenTime) {
        this.fmOpenTime = fmOpenTime;
    }

    public String getCodeMemo() {
        return codeMemo;
    }

    public void setCodeMemo(String codeMemo) {
        this.codeMemo = codeMemo;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
