package com.dawoo.gamebox.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.view.SwipeToLoadLayout.LoadMoreFooterView;
import com.dawoo.gamebox.view.view.SwipeToLoadLayout.RefreshHeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by jack on 18-2-13.
 */

public class ShareRecodingFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    Unbinder unbinder;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.fv)
    TextView fv;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private ShareAdapter shareAdapter;

    public ShareRecodingFragment() {
    }

    public static ShareRecodingFragment newInstance() {
        ShareRecodingFragment fragment = new ShareRecodingFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_recoding, container, false);
        unbinder = ButterKnife.bind(this, v);
        initView();
        initDate();
        return v;
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 初始化組件
     */
    private void initView() {

        shareAdapter = new ShareAdapter(R.layout.recycleview_item_promofragment_activity_view);
        swipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        shareAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, null));
        swipeTarget.setAdapter(shareAdapter);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshEnabled(true);
        swipeToLoadLayout.setLoadMoreEnabled(false);

    }

    /**
     * 初始化数据
     */
    private void initDate() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    @OnClick({R.id.tv_start_time, R.id.fv, R.id.tv_end_time, R.id.swipe_refresh_header, R.id.swipe_target, R.id.swipe_load_more_footer, R.id.swipeToLoadLayout})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (view.getId()) {
            case R.id.tv_start_time:
                break;
            case R.id.fv:
                break;
            case R.id.tv_end_time:
                break;
            case R.id.swipe_refresh_header:
                break;
            case R.id.swipe_target:
                break;
            case R.id.swipe_load_more_footer:
                break;
            case R.id.swipeToLoadLayout:
                break;
        }
    }

    class ShareAdapter extends BaseQuickAdapter {
        public ShareAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {

        }


    }
}
