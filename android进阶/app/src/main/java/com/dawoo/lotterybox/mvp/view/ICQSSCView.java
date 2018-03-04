package com.dawoo.lotterybox.mvp.view;

import com.dawoo.lotterybox.bean.AwardResultBean;
import com.dawoo.lotterybox.bean.CQSSCAwardResultBean;
import com.dawoo.lotterybox.bean.ExpectDataBean;

import java.util.List;

/**
 * Created by b on 18-2-22.
 */

public interface ICQSSCView extends IBaseView{
    void onAwardResults(List<CQSSCAwardResultBean> awardResultBeans);

    void onAwardResultsAndNoOpen(List<AwardResultBean> awardResultBeans);

    void onExpectData(ExpectDataBean expectDataBean);

    void onLotteryOdd(Object o);
}
