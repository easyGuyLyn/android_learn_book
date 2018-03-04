package com.dawoo.lotterybox.view.fragment.cq_ssc_b;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.playType.PlayDetailBean;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;
import com.dawoo.lotterybox.util.SingleToast;
import com.dawoo.lotterybox.util.ThreadUtils;
import com.dawoo.lotterybox.util.lottery.initdata.CQSSCBDataUtils;
import com.dawoo.lotterybox.view.activity.shishicai.CQSSCBActivity;
import com.dawoo.lotterybox.view.fragment.BaseFragment;
import com.dawoo.lotterybox.view.view.SscBFragmentManager;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by archar on 18-2-27.
 */

public class SSC_YZDW_Fragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.rv_szp_play_type)
    RecyclerView mRvSzpPlayType;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private DetailAdapter mDetailAdapter;

    private List<PlayTypeBean.PlayBean> mList = new ArrayList<>();
    private LinkedHashMap<String, List<PlayDetailBean>> mListLinkedHashMap = new LinkedHashMap<>();
    private List<PlayDetailBean> extraData = new ArrayList<>();//万位 额外的总和数据

    private List<PlayDetailBean> selectedBean = new ArrayList<>();

    @Override
    public void onDestroyView() {
        RxBus.get().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void loadData() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ssc_b_yzdw, container, false);
        unbinder = ButterKnife.bind(this, v);
        RxBus.get().register(this);
        initViews();
        initData();
        initListener();
        return v;
    }


    private void initViews() {
        mList = CQSSCBDataUtils.initYZDWData();
        initTab();
        mDetailAdapter = new DetailAdapter(R.layout.click_play_type_item_layout);
        mRvSzpPlayType.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRvSzpPlayType.setAdapter(mDetailAdapter);
    }


    private void initData() {
        for (PlayTypeBean.PlayBean playBean : mList) {
            mListLinkedHashMap.put(playBean.getPlayTypeName(), CQSSCBDataUtils.init_YZDW_0_9_DXDSZH_Data(false
                    , playBean.getPlayTypeName(), ""));
        }
        extraData.clear();
        extraData.addAll(CQSSCBDataUtils.init_YZDW_0_9_DXDSZH_Data(true, getString(R.string.ww), getString(R.string.zh)));
        mListLinkedHashMap.get(getString(R.string.ww)).addAll(extraData);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetailAdapter.setNewData(mListLinkedHashMap.get(getString(R.string.ww)));
            }
        });
    }


    /**
     * 初始化 tab ui
     */
    private void initTab() {
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(i).getPlayTypeName()), true);
            } else {
                mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(i).getPlayTypeName()), false);
            }
        }
    }


    private void initListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((CQSSCBActivity) getActivity()).offTouZhuView();
                chooseType(mList.get(tab.getPosition()).getPlayTypeName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void chooseType(String playName) {
        SingleToast.showMsg(playName);
        mDetailAdapter.setNewData(mListLinkedHashMap.get(playName));
        if (selectedBean.size() > 0) {
            selectedBean.clear();
            extraData.clear();
            extraData.addAll(CQSSCBDataUtils.init_YZDW_0_9_DXDSZH_Data(true, getString(R.string.ww), getString(R.string.zh)));
            for (PlayTypeBean.PlayBean playBean : mList) {
                mListLinkedHashMap.put(playBean.getPlayTypeName(), CQSSCBDataUtils.init_YZDW_0_9_DXDSZH_Data(false
                        , playBean.getPlayTypeName(), ""));
            }
            mListLinkedHashMap.get(getString(R.string.ww)).addAll(extraData);
            mDetailAdapter.notifyDataSetChanged();
        }
    }


    @Subscribe(tags = {@Tag(SscBFragmentManager.TOP_FRGMENT)})
    public void clearSelectedData(String id) {
        if (!id.equals(this.getId())) {
            ThreadUtils.newThread(new Runnable() {
                @Override
                public void run() {
                    if (selectedBean.size() > 0) {
                        selectedBean.clear();
                        initData();
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mTabLayout.getSelectedTabPosition() != 0){
                                    mTabLayout.getTabAt(0).select();
                                    mDetailAdapter.setNewData(mListLinkedHashMap.get(getString(R.string.ww)));
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    private class DetailAdapter extends BaseQuickAdapter {

        public DetailAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            PlayDetailBean playDetailBean = (PlayDetailBean) item;
            helper.setText(R.id.tv_playType, playDetailBean.getNum());
            helper.itemView.setSelected(playDetailBean.isSelected());
            helper.setText(R.id.tv_odds, playDetailBean.getOdd());
            helper.itemView.setSelected(playDetailBean.isSelected());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingleToast.showMsg(playDetailBean.getType() + " " + playDetailBean.getChildType() + " " + playDetailBean.getKind() + " " + playDetailBean.getNum());
                    if (playDetailBean.isSelected()) {
                        playDetailBean.setSelected(false);
                        selectedBean.remove(playDetailBean);
                    } else {
                        playDetailBean.setSelected(true);
                        selectedBean.add(playDetailBean);
                    }
                    ((CQSSCBActivity) getActivity()).onTouZhuView(playDetailBean);
                    mDetailAdapter.notifyDataSetChanged();
                }
            });
        }
    }


}
