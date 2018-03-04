package com.dawoo.lotterybox.view.fragment.cq_ssc_b;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SSC_SZP_Fragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.rv_szp_play_type)
    RecyclerView mRvSzpPlayType;
    private SZPAdapter mAdapter;

    private List<PlayTypeBean.PlayBean> mList = new ArrayList<>();
    private LinkedHashMap<String, List<PlayDetailBean>> mListLinkedHashMap = new LinkedHashMap<>();

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
        View v = inflater.inflate(R.layout.fragment_ssc_b_szp, container, false);
        unbinder = ButterKnife.bind(this, v);
        RxBus.get().register(this);
        initViews();
        initData();
        initListener();
        return v;
    }


    private void initViews() {
        mAdapter = new SZPAdapter(R.layout.layout_ssc_b_szp_item);
        mRvSzpPlayType.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRvSzpPlayType.setAdapter(mAdapter);
    }


    private void initData() {
        mList = CQSSCBDataUtils.initSZPData();
        for (PlayTypeBean.PlayBean playBean : mList) {
            mListLinkedHashMap.put(playBean.getPlayTypeName(), CQSSCBDataUtils.init_SZP_0_9Data(playBean.getPlayTypeName()));
        }
        mAdapter.setNewData(mList);
    }

    private void initListener() {

    }


    @Subscribe(tags = {@Tag(SscBFragmentManager.TOP_FRGMENT)})
    public void clearSelectedData(String id) {
        if (!id.equals(this.getId()) && selectedBean.size() > 0) {
            ThreadUtils.newThread(new Runnable() {
                @Override
                public void run() {
                    for (PlayTypeBean.PlayBean playBean : mList) {
                        mListLinkedHashMap.put(playBean.getPlayTypeName(), CQSSCBDataUtils.init_SZP_0_9Data(playBean.getPlayTypeName()));
                    }
                    selectedBean.clear();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                }
            });
        }
    }

    private class SZPAdapter extends BaseQuickAdapter {

        public SZPAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            PlayTypeBean.PlayBean playBean = (PlayTypeBean.PlayBean) item;
            helper.setText(R.id.tv_play_type_name, playBean.getPlayTypeName());
            helper.getView(R.id.tv_play_type_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingleToast.showMsg(playBean.getPlayTypeExplain());
                }
            });

            RecyclerView recyclerView = helper.getView(R.id.rv_detail);
            SZPDetailAdapter szpDetailAdapter = new SZPDetailAdapter(R.layout.click_play_type_item_layout);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setAdapter(szpDetailAdapter);
            szpDetailAdapter.setNewData(mListLinkedHashMap.get(playBean.getPlayTypeName()));
        }
    }


    private class SZPDetailAdapter extends BaseQuickAdapter {

        public SZPDetailAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            PlayDetailBean playDetailBean = (PlayDetailBean) item;
            helper.setText(R.id.tv_playType, playDetailBean.getNum());
            helper.setText(R.id.tv_odds, playDetailBean.getOdd());
            helper.itemView.setSelected(playDetailBean.isSelected());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingleToast.showMsg(playDetailBean.getType() + " " + playDetailBean.getKind() + " " + playDetailBean.getNum());
                    if (playDetailBean.isSelected()) {
                        playDetailBean.setSelected(false);
                        selectedBean.remove(playDetailBean);
                    } else {
                        playDetailBean.setSelected(true);
                        selectedBean.add(playDetailBean);
                    }
                    mAdapter.notifyDataSetChanged();
                    ((CQSSCBActivity) getActivity()).onTouZhuView(playDetailBean);
                }
            });
        }
    }


}
