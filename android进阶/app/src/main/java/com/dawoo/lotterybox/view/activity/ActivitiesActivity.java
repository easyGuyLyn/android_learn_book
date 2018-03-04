package com.dawoo.lotterybox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.view.HeaderView;
import com.dawoo.lotterybox.view.view.SwipeToLoadLayout.LoadMoreFooterView;
import com.dawoo.lotterybox.view.view.SwipeToLoadLayout.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 18-2-8.
 */

public class ActivitiesActivity extends BaseActivity {
    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_activities);
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader(getString(R.string.activities_activity), true);
    }

    @Override
    protected void initData() {
        List<Object> datas = new ArrayList<>();
        for (int i = 0;i<10;i++){
            datas.add(i);
        }
        ActivitiesQuickAdapter activitiesQuickAdapter = new ActivitiesQuickAdapter(R.layout.item_activities_rlv);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        activitiesQuickAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, null));
        mSwipeTarget.setAdapter(activitiesQuickAdapter);
        activitiesQuickAdapter.setNewData(datas);
    }

    class ActivitiesQuickAdapter extends BaseQuickAdapter {
        public ActivitiesQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {

        }
    }

}
