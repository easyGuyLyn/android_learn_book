package com.dawoo.lotterybox.view.activity.shishicai;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.activity.BaseActivity;
import com.dawoo.lotterybox.view.view.HeaderView;
import com.dawoo.lotterybox.view.view.dialog.SureNoteDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by b on 18-2-14.
 * 购物车
 */

public class CQSSCAShoppingCartActivity extends BaseActivity {
    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.tv_bet_state)
    TextView mTvBetState;
    @BindView(R.id.tv_timer_time)
    TextView mTvTimerTime;
    @BindView(R.id.ll_timer)
    LinearLayout mLlTimer;
    @BindView(R.id.tv_clear_list)
    TextView mTvClearList;
    @BindView(R.id.tv_comput_select)
    TextView mTvComputSelect;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.rl_menu_and_state)
    RelativeLayout mRlMenuAndState;
    @BindView(R.id.rlv_bet_list)
    RecyclerView mRlvBetList;
    @BindView(R.id.service_agreement)
    TextView mServiceAgreement;
    @BindView(R.id.tv_all_bet)
    TextView mTvAllBet;
    @BindView(R.id.tv_submit_bet)
    TextView mTvSubmitBet;
    private ShopCartQuickAdapter mMQuickAdapter;
    private SureNoteDialog mSureNoteDialog;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_base_ssc_shop_cart);
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader("时时彩A盘", true);
        mMQuickAdapter = new ShopCartQuickAdapter(R.layout.item_shop_cart);
        mMQuickAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, null));
        mRlvBetList.setLayoutManager(new LinearLayoutManager(this));
        mRlvBetList.setAdapter(mMQuickAdapter);
    }

    @Override
    protected void initData() {
        List<Object> shopList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            shopList.add(new Object());
        mMQuickAdapter.setNewData(shopList);
    }

    @OnClick({R.id.tv_submit_bet})
    public void onShopCartClick(View v) {
        switch (v.getId()){
            case R.id.tv_submit_bet:
                mSureNoteDialog = new SureNoteDialog(this);
                mSureNoteDialog.setRightBtnClickListener(mSureNoteOnclick);
                break;
        }

    }

    View.OnClickListener mSureNoteOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSureNoteDialog.onDestory();
        }
    };

    class ShopCartQuickAdapter extends BaseQuickAdapter {
        public ShopCartQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
        }
    }

}
