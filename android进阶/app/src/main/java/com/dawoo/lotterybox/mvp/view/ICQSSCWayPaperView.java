package com.dawoo.lotterybox.mvp.view;

import com.dawoo.lotterybox.bean.AwardResultBean;

import java.util.List;

/**
 * Created by b on 18-2-27.
 */

public interface ICQSSCWayPaperView extends IBaseView{
    void onAwardResultsAndNoOpen(List<AwardResultBean> awardResultBeans);
}
