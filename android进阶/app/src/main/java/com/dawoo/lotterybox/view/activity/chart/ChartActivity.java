package com.dawoo.lotterybox.view.activity.chart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 时时彩的图表
 * Created by benson on 18-2-19.
 */

public class ChartActivity extends BaseActivity {
    @BindView(R.id.head_view)
    ChartHeaderView mHeadView;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private ChartViewPagerAdapter mChartViewPagerAdapter;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_chart_ssc);
    }

    @Override
    protected void initViews() {
        List<String> titles = new ArrayList<>();
        titles.add("开奖");
        titles.add("百位走势");
        titles.add("十位走势");
        titles.add("个位走势");

        List<ChartFragment> fragments = new ArrayList<>();
        fragments.add(new ChartFragment());
        fragments.add(new ChartFragment());
        fragments.add(new ChartFragment());
        fragments.add(new ChartFragment());


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab(), true);
        mTabLayout.addTab(mTabLayout.newTab(), false);
        mTabLayout.addTab(mTabLayout.newTab(), false);
        mTabLayout.addTab(mTabLayout.newTab(), false);

        mChartViewPagerAdapter = new ChartViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mChartViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }

    class ChartViewPagerAdapter extends FragmentPagerAdapter {
        List<ChartFragment> fragments;
        List<String> titles;

        public ChartViewPagerAdapter(FragmentManager fm, List<ChartFragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
