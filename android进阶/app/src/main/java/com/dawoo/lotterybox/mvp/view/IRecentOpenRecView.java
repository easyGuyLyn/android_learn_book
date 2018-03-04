package com.dawoo.lotterybox.mvp.view;

import com.dawoo.lotterybox.bean.lottery.HandicpWithOpening;
import com.dawoo.lotterybox.net.HttpResult;

import java.util.List;

/**
 * 最近的开奖结果
 * Created by benson on 18-2-19.
 */

public interface IRecentOpenRecView extends IBaseView{
    void onRecentRecResult(List<HandicpWithOpening> list);
    void onRefreshRecResult(List<HandicpWithOpening> list);
    void onLoadMoreRecResult(List<HandicpWithOpening> list);
}
