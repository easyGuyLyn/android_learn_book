package com.dawoo.lotterybox.view.fragment.cq_ssc_b;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.adapter.CommonViewHolder;
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

public class SSC_ZX3_Fragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.rv_szp_play_type)
    RecyclerView mRvSzpPlayType;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private MainAdapter mMainAdapter;

    private List<PlayTypeBean.PlayBean> mList = new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<PlayDetailBean>>> mStringLinkedHashMapLinkedHashMap = new LinkedHashMap<>();
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
        initData(getString(R.string.q3zx3));
        initListener();
        return v;
    }


    private void initViews() {
        mList = CQSSCBDataUtils.initZX3Data();
        initTab();
    }


    private void initData(String tab) {
        for (PlayTypeBean.PlayBean playBean : mList) {
            LinkedHashMap<String, List<PlayDetailBean>> listLinkedHashMap = new LinkedHashMap<>();
            listLinkedHashMap.put("1", CQSSCBDataUtils.init_ZX3_0_9_Data(
                    playBean.getPlayTypeName(), "1"));
            mStringLinkedHashMapLinkedHashMap.put(playBean.getPlayTypeName(), listLinkedHashMap);
        }

        //下面有异步
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainAdapter = new MainAdapter(mStringLinkedHashMapLinkedHashMap.get(tab));
                mRvSzpPlayType.setLayoutManager(new LinearLayoutManager(mContext));
                mRvSzpPlayType.setAdapter(mMainAdapter);
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
        initData(playName);
    }


    @Subscribe(tags = {@Tag(SscBFragmentManager.TOP_FRGMENT)})
    public void clearSelectedData(String id) {
        if (!id.equals(this.getId())) {
            ThreadUtils.newThread(new Runnable() {
                @Override
                public void run() {
                    if (selectedBean.size() > 0) {
                        selectedBean.clear();
                        initData(getString(R.string.q3zx3));
                    } else if (mTabLayout.getSelectedTabPosition() != 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTabLayout.getTabAt(0).select();
                                initData(getString(R.string.q3zx3));
                            }
                        });
                    }
                }
            });
        }
    }

    private class MainAdapter extends RecyclerView.Adapter {
        private LinkedHashMap<String, List<PlayDetailBean>> mData = new LinkedHashMap<>();
        private List<String> titleList = new ArrayList<>();

        public MainAdapter(LinkedHashMap<String, List<PlayDetailBean>> data) {
            mData = data;
            for (String scheme : mData.keySet()) {
                titleList.add(scheme);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommonViewHolder(View.inflate(mContext, R.layout.layout_ssc_b_main_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CommonViewHolder viewHolder = (CommonViewHolder) holder;
            TextView tv_play_type_name = viewHolder.getTv(R.id.tv_play_type_name);
            tv_play_type_name.setVisibility(View.GONE);

            RecyclerView recyclerView = viewHolder.getView(R.id.rv_detail);
            DetailAdapter detailAdapter = new DetailAdapter(R.layout.click_ball_play_type_item_layout);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setAdapter(detailAdapter);
            detailAdapter.setNewData(mData.get(titleList.get(position)));
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
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
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingleToast.showMsg(playDetailBean.getType() + " " + playDetailBean.getKind() + " " + playDetailBean.getChildType() + " " + playDetailBean.getNum());
                    if (playDetailBean.isSelected()) {
                        playDetailBean.setSelected(false);
                        selectedBean.remove(playDetailBean);
                    } else {
                        playDetailBean.setSelected(true);
                        selectedBean.add(playDetailBean);
                    }
                    ((CQSSCBActivity) getActivity()).onTouZhuView(playDetailBean);
                    mMainAdapter.notifyDataSetChanged();
                }
            });
        }
    }


}
