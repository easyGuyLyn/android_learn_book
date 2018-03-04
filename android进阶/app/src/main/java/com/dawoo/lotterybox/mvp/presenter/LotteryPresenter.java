package com.dawoo.lotterybox.mvp.presenter;

import android.content.Context;

import com.dawoo.lotterybox.adapter.LotteryRcdAdapter.LotteryRcdAdapter;
import com.dawoo.lotterybox.bean.lottery.HandicpWithOpening;
import com.dawoo.lotterybox.bean.lottery.LotteryLastOpenAndOpening;
import com.dawoo.lotterybox.bean.record.NoteRecordHis;
import com.dawoo.lotterybox.mvp.model.Lottery.ILotteryModel;
import com.dawoo.lotterybox.mvp.model.Lottery.LotteryModel;
import com.dawoo.lotterybox.mvp.view.IBaseView;
import com.dawoo.lotterybox.mvp.view.ILastLotteryRecView;
import com.dawoo.lotterybox.mvp.view.INoteRecordHisView;
import com.dawoo.lotterybox.mvp.view.IRecentOpenRecView;
import com.dawoo.lotterybox.net.rx.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 彩票相关的presenter
 * Created by benson on 18-2-13.
 */

public class LotteryPresenter<T extends IBaseView> extends BasePresenter {
    private final Context mContext;
    private T mView;
    private final ILotteryModel mModel;

    public LotteryPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new LotteryModel();
    }

    /**
     * 最新一期彩票开奖和下一期未开奖开票
     */
    public void getLastOpenedAndOpeningResult() {
        Subscription subscription = mModel.getLastOpenedAndOpeningResult(new ProgressSubscriber(o ->
                ((ILastLotteryRecView) mView).onLastLotteryRecResult((List<LotteryLastOpenAndOpening>) o), mContext));
        subList.add(subscription);
    }

    /**
     * 最新一期彩票开奖和下一期未开奖开票
     */
    public void getRefreshResult() {
        Subscription subscription = mModel.getLastOpenedAndOpeningResult(new ProgressSubscriber(o ->
                ((ILastLotteryRecView) mView).onfreshResult((List<LotteryLastOpenAndOpening>) o), mContext));
        subList.add(subscription);
    }


    public List<LotteryLastOpenAndOpening> addItemType(List<LotteryLastOpenAndOpening> lastOpenAndOpenings) {
        List<LotteryLastOpenAndOpening> list = new ArrayList();
        list.clear();
        LotteryLastOpenAndOpening itemData = null;
        for (int i = 0; i < lastOpenAndOpenings.size(); i++) {
            itemData = lastOpenAndOpenings.get(i);
            if (LotteryRecEnum.SSC.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.SSC.getCode());
                list.add(itemData);
            } else if (LotteryRecEnum.PK10.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.PK10.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.LHC.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.LHC.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.K3.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.K3.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.XYNC.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.XYNC.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.XY28.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.XY28.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.FC3D.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.FC3D.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.KL8.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.KL8.getCode());
                list.add(itemData);
            }
        }

        return list;
    }

    @Override
    public void onDestory() {
        super.onDestory();
    }


    enum LotteryRecEnum {
        SSC("ssc", 1),
        PK10("pk10", 2),
        LHC("lhc", 3),
        K3("k3", 4),
        XYNC("sfc", 5),
        KL8("keno", 6),
        FC3D("pl3", 7),
        XY28("xy28", 8);

        private String type;
        private int code;


        LotteryRecEnum(String type, int code) {
            this.type = type;
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }



    /**
     * 最近开奖记录
     */
    public void getRecentRecords(String code, String pageSize) {
        Subscription subscription = mModel.getRecentRecords(new ProgressSubscriber(o ->
                ((IRecentOpenRecView) mView).onRecentRecResult((List<HandicpWithOpening>) o), mContext),
                code,
                pageSize);
        subList.add(subscription);
    }
    /**
     * 刷新最近开奖记录
     */
    public void refreshRecentRecords(String code, String pageSize) {
        Subscription subscription = mModel.getRecentRecords(new ProgressSubscriber(o ->
                ((IRecentOpenRecView) mView).onRefreshRecResult((List<HandicpWithOpening>) o), mContext),
                code,
                pageSize);
        subList.add(subscription);
    }
    /**
     * 加载更多最近开奖记录
     */
    public void loadMoreRecentRecords(String code, String pageSize) {
        Subscription subscription = mModel.getRecentRecords(new ProgressSubscriber(o ->
                ((IRecentOpenRecView) mView).onLoadMoreRecResult((List<HandicpWithOpening>) o), mContext),
                code,
                pageSize);
        subList.add(subscription);
    }


    public List<HandicpWithOpening> addItemType2(List<HandicpWithOpening> lastOpenAndOpenings) {
        List<HandicpWithOpening> list = new ArrayList();
        list.clear();
        HandicpWithOpening itemData = null;
        for (int i = 0; i < lastOpenAndOpenings.size(); i++) {
            itemData = lastOpenAndOpenings.get(i);
            if (LotteryRecEnum.SSC.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.SSC.getCode());
                list.add(itemData);
            } else if (LotteryRecEnum.PK10.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.PK10.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.LHC.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.LHC.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.K3.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.K3.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.XYNC.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.XYNC.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.XY28.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.XY28.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.FC3D.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.FC3D.getCode());
                list.add(itemData);

            } else if (LotteryRecEnum.KL8.getType().equals(itemData.getType())) {
                itemData.setItemType(LotteryRecEnum.KL8.getCode());
                list.add(itemData);
            }
        }

        return list;
    }


}
