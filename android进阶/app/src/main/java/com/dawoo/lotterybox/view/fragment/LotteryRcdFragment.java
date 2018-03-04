package com.dawoo.lotterybox.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.lotterybox.ConstantValue;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.adapter.LotteryRcdAdapter.LotteryRcdAdapter;
import com.dawoo.lotterybox.bean.lottery.LotteryLastOpenAndOpening;
import com.dawoo.lotterybox.mvp.presenter.LotteryPresenter;
import com.dawoo.lotterybox.mvp.view.ILastLotteryRecView;
import com.dawoo.lotterybox.view.view.dialog.BettingSetDialog;
import com.dawoo.lotterybox.view.view.dialog.OrderTipDialog;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LotteryRcdFragment extends BaseFragment implements ILastLotteryRecView, OnRefreshListener {


    @BindView(R.id.message_push_tv)
    TextView mMessagePushTv;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    Unbinder unbinder;
    private LotteryRcdAdapter mAdapter;
    private OrderTipDialog mOrderTipDialog;
    private LotteryPresenter mPresenter;

    public LotteryRcdFragment() {
    }

    public static LotteryRcdFragment newInstance() {
        LotteryRcdFragment fragment = new LotteryRcdFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.get().unregister(this);
        mPresenter.onDestory();
        if (mOrderTipDialog!=null)
        mOrderTipDialog.onDestory();
      //  mAdapter.cancleTimer();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lottery_record, container, false);
        unbinder = ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    private void initViews() {
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.setOnRefreshListener(this);

        mAdapter = new LotteryRcdAdapter(new ArrayList());

        int padding = DensityUtil.dp2px(mContext, 10);
        mRecyclerView.setPadding(padding, 0, padding, 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    void initData() {
        RxBus.get().register(this);
        mPresenter = new LotteryPresenter<>(mContext, this);
    }

    @Override
    protected void loadData() {
        mPresenter.getLastOpenedAndOpeningResult();
    }


    @OnClick(R.id.message_push_tv)
    public void onViewClicked() {
//        if (mOrderTipDialog == null) {
//            mOrderTipDialog = new OrderTipDialog(mContext);
//        }
//        mOrderTipDialog.show();
        // orderTipDialog.show();
        BettingSetDialog bettingSetDialog = new BettingSetDialog(mContext);
        bettingSetDialog.show();
    }

    @Override
    public void onRefresh() {
        mPresenter.getRefreshResult();
    }


    @Override
    public void onLastLotteryRecResult(List<LotteryLastOpenAndOpening> lastOpenAndOpenings) {
        setData(lastOpenAndOpenings);

    }


    @Override
    public void onfreshResult(List<LotteryLastOpenAndOpening> lastOpenAndOpenings) {
        mSwipeToLoadLayout.setRefreshing(false);
        setData(lastOpenAndOpenings);
    }

    @Override
    public void getNextRec() {

    }

    private void setData(List<LotteryLastOpenAndOpening> list) {
        if (list != null) {
            mAdapter.setNewData(mPresenter.addItemType(list));
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_NETWORK_EXCEPTION)})
    public void stopTheRefreshStatus(String s) {
        mSwipeToLoadLayout.setRefreshing(false);
    }
}

