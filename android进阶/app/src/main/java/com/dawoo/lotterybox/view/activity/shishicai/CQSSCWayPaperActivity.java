package com.dawoo.lotterybox.view.activity.shishicai;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.CQSSCAwardResultBean;
import com.dawoo.lotterybox.bean.WayPaperButtonBean;
import com.dawoo.lotterybox.view.activity.BaseActivity;
import com.dawoo.lotterybox.view.view.HeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dawoo.lotterybox.bean.TypeAndLottery.DataBean_.CHILD_ITEM;
import static com.dawoo.lotterybox.bean.TypeAndLottery.DataBean_.PARENT_ITEM;

/**
 * 重庆时时彩路纸图
 * Created by b on 18-2-26.
 */

public class CQSSCWayPaperActivity extends BaseActivity {

    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.tv_which_periods)
    TextView mTvWhichPeriods;
    @BindView(R.id.tv_award_num)
    TextView mTvAwardNum;
    @BindView(R.id.tv_periods_stop)
    TextView mTvPeriodsStop;
    @BindView(R.id.rlv_lottery_record)
    RecyclerView mRlvLotteryRecord;
    @BindView(R.id.tv_miss_top)
    TextView mTvMissTop;
    @BindView(R.id.rlv_way_paper_bt)
    RecyclerView mRlvWayPaperBt;
    @BindView(R.id.lLayout_ssc_bottom_second)
    NestedScrollView mLLayoutSscBottomSecond;

    List<WayPaperButtonBean> mButtonBeans = new ArrayList<>();

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_cqssc_way_paper);
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader(getString(R.string.way_paper),true);
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(mLLayoutSscBottomSecond);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    protected void initData() {
        initButtonData();
        initBt();
    }

    private void initBt(){
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRlvWayPaperBt.setNestedScrollingEnabled(false);
        mRlvWayPaperBt.setLayoutManager(manager);
        ButtonQuickAdapter mBtAdapter = new ButtonQuickAdapter(R.layout.item_way_paper_bt);
        mRlvWayPaperBt.setAdapter(mBtAdapter);
        mBtAdapter.setNewData(mButtonBeans);
        mBtAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.isSelected())
                    view.setSelected(false);
                else {
                    view.setSelected(true);
                    for (int i = 0; i < mButtonBeans.size(); i++){
                        if (i!=position){
                            mRlvWayPaperBt.getChildAt(i).setSelected(false);
                        }
                    }
                }
            }
        });
    }



    class ButtonQuickAdapter extends BaseQuickAdapter {
        public ButtonQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            WayPaperButtonBean buttonBean = (WayPaperButtonBean)item;
            helper.setText(R.id.tv_ball_status,buttonBean.getBtName());
        }
    }


    private void initButtonData(){
        WayPaperButtonBean wayPaperButtonBean1 = new WayPaperButtonBean();
        wayPaperButtonBean1.setBtName(getString(R.string.all_dragon_and_tiger));
        wayPaperButtonBean1.setBallNumber(-1);
        wayPaperButtonBean1.setStatus(2);
        mButtonBeans.add(wayPaperButtonBean1);
        WayPaperButtonBean wayPaperButtonBean2 = new WayPaperButtonBean();
        wayPaperButtonBean2.setBtName(getString(R.string.all_big_small));
        wayPaperButtonBean2.setBallNumber(-1);
        wayPaperButtonBean2.setStatus(0);
        mButtonBeans.add(wayPaperButtonBean2);
        WayPaperButtonBean wayPaperButtonBean3 = new WayPaperButtonBean();
        wayPaperButtonBean3.setBtName(getString(R.string.all_single_double));
        wayPaperButtonBean3.setBallNumber(-1);
        wayPaperButtonBean3.setStatus(1);
        mButtonBeans.add(wayPaperButtonBean3);
        WayPaperButtonBean wayPaperButtonBean4 = new WayPaperButtonBean();
        wayPaperButtonBean4.setBtName(getString(R.string.first_big_small));
        wayPaperButtonBean4.setBallNumber(0);
        wayPaperButtonBean4.setStatus(0);
        mButtonBeans.add(wayPaperButtonBean4);
        WayPaperButtonBean wayPaperButtonBean5 = new WayPaperButtonBean();
        wayPaperButtonBean5.setBtName(getString(R.string.first_single_double));
        wayPaperButtonBean5.setBallNumber(0);
        wayPaperButtonBean5.setStatus(1);
        mButtonBeans.add(wayPaperButtonBean5);
        WayPaperButtonBean wayPaperButtonBean6 = new WayPaperButtonBean();
        wayPaperButtonBean6.setBtName(getString(R.string.second_big_small));
        wayPaperButtonBean6.setBallNumber(1);
        wayPaperButtonBean6.setStatus(0);
        mButtonBeans.add(wayPaperButtonBean6);
        WayPaperButtonBean wayPaperButtonBean7 = new WayPaperButtonBean();
        wayPaperButtonBean7.setBtName(getString(R.string.second_single_double));
        wayPaperButtonBean7.setBallNumber(1);
        wayPaperButtonBean7.setStatus(1);
        mButtonBeans.add(wayPaperButtonBean7);
        WayPaperButtonBean wayPaperButtonBean8 = new WayPaperButtonBean();
        wayPaperButtonBean8.setBtName(getString(R.string.third_big_small));
        wayPaperButtonBean8.setBallNumber(2);
        wayPaperButtonBean8.setStatus(0);
        mButtonBeans.add(wayPaperButtonBean8);
        WayPaperButtonBean wayPaperButtonBean9 = new WayPaperButtonBean();
        wayPaperButtonBean9.setBtName(getString(R.string.third_single_double));
        wayPaperButtonBean9.setBallNumber(2);
        wayPaperButtonBean9.setStatus(1);
        mButtonBeans.add(wayPaperButtonBean9);
        WayPaperButtonBean wayPaperButtonBean10 = new WayPaperButtonBean();
        wayPaperButtonBean10.setBtName(getString(R.string.fourth_big_small));
        wayPaperButtonBean10.setBallNumber(3);
        wayPaperButtonBean10.setStatus(0);
        mButtonBeans.add(wayPaperButtonBean10);
        WayPaperButtonBean wayPaperButtonBean11 = new WayPaperButtonBean();
        wayPaperButtonBean11.setBtName(getString(R.string.fourth_single_double));
        wayPaperButtonBean11.setBallNumber(3);
        wayPaperButtonBean11.setStatus(1);
        mButtonBeans.add(wayPaperButtonBean11);
        WayPaperButtonBean wayPaperButtonBean12 = new WayPaperButtonBean();
        wayPaperButtonBean12.setBtName(getString(R.string.fifth_big_small));
        wayPaperButtonBean12.setBallNumber(4);
        wayPaperButtonBean12.setStatus(0);
        mButtonBeans.add(wayPaperButtonBean12);
        WayPaperButtonBean wayPaperButtonBean13 = new WayPaperButtonBean();
        wayPaperButtonBean13.setBtName(getString(R.string.fifth_single_double));
        wayPaperButtonBean13.setBallNumber(4);
        wayPaperButtonBean13.setStatus(1);
        mButtonBeans.add(wayPaperButtonBean13);

    }

}
