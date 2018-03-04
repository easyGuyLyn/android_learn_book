package com.dawoo.lotterybox.mvp.view;

import com.dawoo.lotterybox.bean.lottery.LotteryLastOpenAndOpening;

import java.util.List;

/**
 * 最新一期的开奖记录和将要开奖的彩票倒计时
 * Created by benson on 18-2-13.
 */

public interface ILastLotteryRecView extends IBaseView {
    void onLastLotteryRecResult(List<LotteryLastOpenAndOpening> lastOpenAndOpenings);

    void getNextRec();

    void onfreshResult(List<LotteryLastOpenAndOpening> lastOpenAndOpenings);
}
