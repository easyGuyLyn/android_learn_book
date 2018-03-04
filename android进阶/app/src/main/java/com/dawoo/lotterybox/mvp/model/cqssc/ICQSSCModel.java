package com.dawoo.lotterybox.mvp.model.cqssc;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-2-22.
 */

public interface ICQSSCModel {

    Subscription getAwardResults(Subscriber subscriber, String code, String pageSize, String pageNumber);

    Subscription getAwardResultsAndNoOpen(Subscriber subscriber , String code , String pageSize);

    Subscription getExpectData(Subscriber subscriber ,String code);

    Subscription getLotteryOdd(Subscriber subscriber ,String code ,String betCode);

}
