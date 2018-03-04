package com.dawoo.lotterybox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.lotterybox.ConstantValue;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.adapter.HallAdapter.AdapterSet.BannerConfigInit;
import com.dawoo.lotterybox.bean.BannerBean;
import com.dawoo.lotterybox.bean.Bulletin;
import com.dawoo.lotterybox.bean.DataCenter;
import com.dawoo.lotterybox.bean.User;
import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;
import com.dawoo.lotterybox.mvp.presenter.HallPresenter;
import com.dawoo.lotterybox.mvp.presenter.UserPresenter;
import com.dawoo.lotterybox.mvp.view.IHallView;
import com.dawoo.lotterybox.mvp.view.ILotteryHallView;
import com.dawoo.lotterybox.adapter.HallAdapter.RecyclerAdapter;
import com.dawoo.lotterybox.util.ActivityUtil;
import com.dawoo.lotterybox.util.NetUtil;
import com.dawoo.lotterybox.util.SoundUtil;
import com.dawoo.lotterybox.view.activity.ActivitiesActivity;
import com.dawoo.lotterybox.view.activity.LoginActivity;
import com.dawoo.lotterybox.view.view.MarqueeTextView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.dawoo.lotterybox.bean.TypeAndLottery.DataBean_.CHILD_ITEM;
import static com.dawoo.lotterybox.bean.TypeAndLottery.DataBean_.PARENT_ITEM;


public class HallFragment extends BaseFragment implements ILotteryHallView,IHallView {

    private UserPresenter mPresenter;

    @BindView(R.id.tv_login_or_register)
    TextView mTvLoginOrRegister;
    @BindView(R.id.tv_title_name)
    TextView mTvTitleName;
    @BindView(R.id.iv_activities)
    ImageView mIvActivities;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.btn_notice)
    ImageView mBtnNotice;
    @BindView(R.id.notice_tv)
    MarqueeTextView mNoticeTv;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    private Unbinder mUnbinder;
    private RecyclerAdapter mAdapter;
    private HallPresenter mHallPresenter;

    public HallFragment() {
    }

    public static HallFragment newInstance() {
        HallFragment fragment = new HallFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hall, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestory();
        mUnbinder.unbind();
        super.onDestroyView();
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected void loadData() {
        if (DataCenter.getInstance().getUser().isLogin()) {
            mPresenter.getUerInfo();
        }
        mHallPresenter.getBanner();
        mHallPresenter.getBulletin();
        mHallPresenter.getTypeAndLottery();
    }

    public void initViews() {
    }

    private void initData() {
        mPresenter = new UserPresenter<>(mContext, this);
        mHallPresenter = new HallPresenter(mContext,this);
    }

    @Override
    public void onGetUserInfoResult(User user) {
        if (user != null) {
            mPresenter.setUser(user);
        }

    }

    @OnClick({R.id.tv_login_or_register,R.id.iv_activities})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tv_login_or_register:
                startActivity(new Intent(getActivity(),LoginActivity.class));
                break;

            case R.id.iv_activities:
                startActivity(new Intent(getActivity(), ActivitiesActivity.class));
                break;
        }
    }


    @Override
    public void onBanner(List<BannerBean> bannerBeans) {
        if (bannerBeans != null){
            List images = new ArrayList();
            List titles = new ArrayList();
            List<String> Links = new ArrayList();

            for (int i = 0; i < bannerBeans.size(); i++) {
                images.add(NetUtil.handleUrl(DataCenter.getInstance().getDomain(), bannerBeans.get(i).getCover()));
                titles.add(bannerBeans.get(i).getName());
                Links.add(bannerBeans.get(i).getLink());
            }
            BannerConfigInit init = new BannerConfigInit(mBanner, images, titles, position ->
                    ActivityUtil.startWebView(Links.get(position), "", ConstantValue.WEBVIEW_TYPE_ORDINARY));

            init.start();
        }
    }

    @Override
    public void onBulletin(List<Bulletin> bulletins) {
        if (bulletins!=null){
            ArrayList<String> titleList = new ArrayList<>();

            for (Bulletin bean : bulletins)
                titleList.add(bean.getContent());

            mNoticeTv.setTextArraysAndClickListener(titleList, view -> {
                SoundUtil.getInstance().playVoiceOnclick();
            });

        }
    }

    @Override
    public void onTypeAndLottery(List<TypeAndLotteryBean> lotteryTypes) {
        if (lotteryTypes!=null){
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (lotteryTypes.get(position).getType() == PARENT_ITEM)
                        return 1;

                    if (lotteryTypes.get(position).getType() == CHILD_ITEM)
                        return 2;
                    return 1;
                }
            });

            mRecycleView.setNestedScrollingEnabled(false);
            mRecycleView.setLayoutManager(manager);
            mRecycleView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new RecyclerAdapter(getActivity(), lotteryTypes);
            mRecycleView.setAdapter(mAdapter);
        }
    }
}
