package com.dawoo.gamebox.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.adapter.HomeAdapter.HomeFragmentAdapter;
import com.dawoo.gamebox.bean.Banner;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.FAB;
import com.dawoo.gamebox.bean.GameLink;
import com.dawoo.gamebox.bean.GetPacket;
import com.dawoo.gamebox.bean.HongbaoCount;
import com.dawoo.gamebox.bean.HongbaoTemp;
import com.dawoo.gamebox.bean.Notice;
import com.dawoo.gamebox.bean.RefreshhApis;
import com.dawoo.gamebox.bean.SiteApiRelation;
import com.dawoo.gamebox.bean.SiteTemp;
import com.dawoo.gamebox.bean.UrlBean;
import com.dawoo.gamebox.bean.UserAccount;
import com.dawoo.gamebox.bean.UserAssert;
import com.dawoo.gamebox.mvp.presenter.HomeFramentPresenter;
import com.dawoo.gamebox.mvp.view.IHomeFragmentView;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.AutoLogin;
import com.dawoo.gamebox.util.NetUtil;
import com.dawoo.gamebox.util.SharePreferenceUtil;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.activity.LoginActivity;
import com.dawoo.gamebox.view.activity.MainActivity;
import com.dawoo.gamebox.view.activity.VideoGameListActivity;
import com.dawoo.gamebox.view.view.CustomProgressDialog;
import com.dawoo.gamebox.view.view.HongBaoDialog;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends BaseFragment implements IHomeFragmentView, OnRefreshListener {

    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.fab_iv)
    ImageView mFabIv;
    @BindView(R.id.fab_closee_iv)
    ImageView mFabCloseIv;
    @BindView(R.id.hongbao_fl)
    FrameLayout mHongbaoFl;

    @BindView(R.id.unLogined_ll)
    LinearLayout mUnLoginedLL;
    @BindView(R.id.lonin_btn)
    Button mLoginBtn;
    @BindView(R.id.register_btn)
    Button mRegisterBtn;

    @BindView(R.id.Logined_rl)
    RelativeLayout mLoginedRl;
    @BindView(R.id.user_name_tv)
    TextView mUserNameTv;
    @BindView(R.id.user_account_tv)
    TextView mUserAccountTv;
    @BindView(R.id.root_view_popupwindow)
    View mRootViewPopup;
    Unbinder unbinder;

    private HomeFragmentAdapter mAdapter;
    private HomeFramentPresenter mPresenter;
    private HongBaoDialog mHBDialog;
    private PopupWindow mPopWindow;
    private FAB.ActivityBean mActivityBean;
    private UserAccount mAccount;
    private Timer mTimer;
    private long mInterval = 29 * 60 * 1000;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.setBannerStartAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mPresenter.onDestory();
        if (mAdapter != null) {
            mAdapter.setBannerStopAutoPlay();
            mAdapter.noticeReleaseResources();
        }
        mHBDialog = null;
        mPopWindow = null;
        RxBus.get().unregister(this);
        unbinder.unbind();
    }

    private void initViews() {
        // 设置 不刷新，不加载更多
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.setOnRefreshListener(this);


        // 设置recycleview
        mAdapter = new HomeFragmentAdapter(mContext, getChildFragmentManager(), new OnGameItemClickListener());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }


    @OnClick({R.id.fab_iv, R.id.fab_closee_iv, R.id.lonin_btn, R.id.register_btn, R.id.Logined_rl})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (view.getId()) {
            case R.id.fab_iv:
                if (!DataCenter.getInstance().isLogin()) {
                    // ToastUtil.showToastShort(mContext, getString(R.string.login_first));
                    ActivityUtil.gotoLogin();
                    return;
                }
                getCoutTime();
                break;
            case R.id.fab_closee_iv:
                mHongbaoFl.setVisibility(View.GONE);
                break;
            case R.id.lonin_btn:
                // 登录
                startActivity(new Intent(mContext, LoginActivity.class));
                break;
            case R.id.register_btn:
                // 注册
                String url = DataCenter.getInstance().getDomain() + ConstantValue.REGISTER_URL;
                ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_ORDINARY);
                break;
            case R.id.Logined_rl:
                doTogglePopupWindow();
                break;
        }
    }

    /**
     * 初始化数据
     */
    void initData() {
        // 初始化 presenter
        mPresenter = new HomeFramentPresenter<IHomeFragmentView>(mContext, this);
        RxBus.get().register(this);
        if (AutoLogin.isSuccessLogin) {
            RxBus.get().post(ConstantValue.EVENT_TYPE_LOGINED, "login");
        }
        keepSessionAlive();
    }

    @Override
    protected void loadData() {
        mPresenter.getTimeZone();
        mPresenter.getBanners();
        mPresenter.getNotice();
        mPresenter.getSiteApiRelation();
        mPresenter.getFAB();
    }

    @Override
    public void onBannerResult(Object o) {
        if (o != null && o instanceof Banner) {
            // 设置baner
            mAdapter.setBanners(((Banner) o).getBanner());
        }
    }

    @Override
    public void onNoticeResult(Object o) {
        if (o != null && o instanceof Notice) {
            // 设置公告
            mAdapter.setNotice(((Notice) o).getAnnouncement());
        }
    }

    @Override
    public void onSiteApiRelationResult(Object o) {
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }

        if (o != null && o instanceof List) {
            // 设置游戏列表
            mAdapter.setContent(((List<SiteApiRelation>) o));
        }
    }

    @Override
    public void onFloatResult(Object o) {
        if (o != null && o instanceof FAB && ((FAB) o).getActivity() != null) {
            // 设置浮动图
            FAB.ActivityBean activityBean = ((FAB) o).getActivity();
            mActivityBean = activityBean;
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.hongbao_default);
            Glide.with(mContext).
                    load(DataCenter.getInstance().getDomain() + "/" + activityBean.getNormalEffect())
                    .apply(options)
                    .into(mFabIv);
        }
    }

    @Override
    public void onAccountResult(Object o) {
        setPopuPWindData(o);
    }

    @Override
    public void onAssertResult(Object o) {
        Object o1 = mPresenter.rePlaceData(o, mAccount);
        setPopuPWindData(o1);
    }


    @Override
    public void onLoadGameLink(Object o) {
        if (o != null && o instanceof GameLink) {
            GameLink gameLink = (GameLink) o;
            ActivityUtil.startWebView(gameLink.getGameLink(), gameLink.getGameMsg(), ConstantValue.WEBVIEW_TYPE_GAME);
        }
    }


    @Override
    public void onHongBaoCountResult(Object o) {
        if (o != null && o instanceof HongbaoCount) {
            // 红包次数和时间
            HongbaoCount hongbaoCount = (HongbaoCount) o;
            mHBDialog = new HongBaoDialog(mContext, hongbaoCount, mActivityBean, new OpenRedPacketOnClickListener());
        }
    }

    @Override
    public void onGetPacketResult(Object o) {
        if (o != null && o instanceof GetPacket && mHBDialog != null) {
            GetPacket getPacket = (GetPacket) o;
            mHBDialog.setGetPacketResult(getPacket);
        }
    }

    @Override
    public void onRecoveryResult(Object o) {
        refreshApis(o);
    }

    @Override
    public void onRefreshResult(Object o) {
        refreshApis(o);
    }


    /**
     * 获取时区
     *
     * @param o
     */
    @Override
    public void getTimeZone(Object o) {
        if (o != null && o instanceof String) {
            String timeZone = (String) o;
            SharePreferenceUtil.saveTimeZone(mContext, timeZone);
        }
    }


    @Override
    public void onAlwaysRequestResult(Object o) {

    }

    @Override
    public void onGameLinkResult(Object o) {
        mPresenter.doResultGameLink(o);
    }


    /**
     * 登录成功后，回调加载账户
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGINED)})
    public void loginedCallBack(String s) {
        //  改变登录状态
        mUnLoginedLL.setVisibility(View.GONE);
        mLoginedRl.setVisibility(View.VISIBLE);
        //  加载账户数据
        mPresenter.getAccount();
    }

    /**
     * 刷新我的数据
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_REFRESH_MINE_DATA)})
    public void refreshMineFragmentData(String s) {
        //  加载资产数据
        mPresenter.getAccount();
    }


    void setPopuPWindData(Object o) {
        if (o != null && o instanceof UserAccount) {
            // 设置账户
            mAccount = ((UserAccount) o);
            RxBus.get().post(ConstantValue.EVENT_TYPE_MINE_LINK, mAccount);
            UserAccount.UserBean userBean = mAccount.getUser();
            mUserNameTv.setText(userBean.getUsername());
            mUserAccountTv.setText(userBean.getCurrency() + BigDemicalUtil.moneyFormat(userBean.getTotalAssets()));
            showPopupWindow(mAccount);
        }
    }

    void refreshApis(Object o) {
        if (o != null && o instanceof RefreshhApis) {
            // 刷新apis
            // 请求刷新 account
            mPresenter.getAssert();
        }
    }

    @Override
    public void onRefresh() {
        loadData();
        if (DataCenter.getInstance().isLogin()) {
            mPresenter.getAccount();
        }
    }


    class OnGameItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SoundUtil.getInstance().playVoiceOnclick();
            if (!DataCenter.getInstance().isLogin()) {
                ActivityUtil.gotoLogin();
                return;
            }

            SiteTemp siteTemp = (SiteTemp) v.getTag(R.id.list_item_data_id);
            if (siteTemp != null) {
                mPresenter.onApiItemClick(siteTemp.isLevel(), siteTemp.getGameListBean(), siteTemp.getSiteApisBean());
            }
        }
    }


    View mContentView;
    ViewHolder mViewHolder;

    private void showPopupWindow(UserAccount account) {
        UserAccount.UserBean userBean = account.getUser();
        //设置contentView
        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_account_popuwindow_view, null);
        }
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder(mContentView, userBean);
        }
        mViewHolder.mTotalAssetValue.setText(BigDemicalUtil.moneyFormat(userBean.getTotalAssets())); // 总资产
        mViewHolder.mWalletValue.setText(BigDemicalUtil.moneyFormat(userBean.getWalletBalance())); // 钱包余额

        if (userBean.isAutoPay()) { // 免转 一键回收
            mViewHolder.mOneKeyBackBtn.setText(getString(R.string.account_one_key_back));
        } else {  // 非免转 一键刷新
            mViewHolder.mOneKeyBackBtn.setText(R.string.account_one_key_refersh);
        }


        AccountQuickAdapter adapter = new AccountQuickAdapter(R.layout.recycleview_item_homefragment_account_view, account.getUser().getApis());
        mViewHolder.mAccountGameRecycylervieww.setLayoutManager(new LinearLayoutManager(mContext));
        mViewHolder.mAccountGameRecycylervieww.setAdapter(adapter);

        if (mPopWindow == null) {
            mPopWindow = new PopupWindow(mContentView, DensityUtil.dp2px(mContext, 189), DensityUtil.dp2px(mContext, 420), true);
            mPopWindow.setTouchable(true);
            mPopWindow.setOutsideTouchable(true);
            mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        }
    }

    /**
     * 快速适配
     */
    class AccountQuickAdapter extends BaseQuickAdapter {
        public AccountQuickAdapter(int layoutResId, @Nullable List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            UserAccount.UserBean.ApisBean apisBean = (UserAccount.UserBean.ApisBean) item;
            helper.setText(R.id.game_lable, apisBean.getApiName());
            helper.setText(R.id.account_value, BigDemicalUtil.moneyFormat(apisBean.getBalance()));
            if (!"normal".equals(apisBean.getStatus())) {
                helper.itemView.setBackgroundColor(getResources().getColor(R.color.text_color_gray_cccccc));
            } else {
                helper.itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }


    private void doTogglePopupWindow() {
        if (mPopWindow == null) {
            return;
        }

        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        } else {
            mPopWindow.showAsDropDown(mRootViewPopup);
        }
    }


    /**
     * 抢红包次数和时间
     */
    void getCoutTime() {

        if (mActivityBean == null) {
            ToastUtil.showToastShort(mContext, getString(R.string.hongbao_get_null));
            return;
        }

        mPresenter.countDrawTimes(mActivityBean.getActivityId());
    }


    /**
     * 红包点击
     */
    class OpenRedPacketOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SoundUtil.getInstance().playVoiceOnclick();
            //getPacket();
            HongbaoTemp temp = (HongbaoTemp) v.getTag(R.id.list_item_data_id);
            if (temp != null) {
                mPresenter.getPacket(temp.getActivityMessageId(), temp.getToken());
            }
        }
    }


    static class ViewHolder {
        @BindView(R.id.total_asset_value)
        TextView mTotalAssetValue;
        @BindView(R.id.wallet_value)
        TextView mWalletValue;
        @BindView(R.id.one_key_back_btn)
        Button mOneKeyBackBtn;
        @BindView(R.id.deposit_btn)
        Button mDepositBtn;
        @BindView(R.id.account_game_recycylervieww)
        RecyclerView mAccountGameRecycylervieww;
        private boolean isAuto;
        UserAccount.UserBean userBean;

        ViewHolder(View view, UserAccount.UserBean userBean) {
            ButterKnife.bind(this, view);
            this.userBean = userBean;
        }

        @OnClick({R.id.one_key_back_btn, R.id.deposit_btn})
        public void onViewClicked(View view) {
            SoundUtil.getInstance().playVoiceOnclick();
            switch (view.getId()) {
                case R.id.one_key_back_btn:
                    RxBus.get().post(ConstantValue.EVENT_TYPE_ONE_KEY_BACK, userBean);

                    break;
                case R.id.deposit_btn:
                    RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_DEPOSIT, "gotodeposit");
                    break;
            }
        }
    }


    /**
     * 回调关闭popup
     *
     * @param s
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_GOTOTAB_DEPOSIT)})
    public void closePopup(String s) {
        if (mPopWindow == null) {
            return;
        }

        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }


    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_ONE_KEY_BACK)})
    public void oneKeyBack(UserAccount.UserBean userBean) {
        LogUtils.e(ConstantValue.EVENT_TYPE_ONE_KEY_BACK);
        if (userBean.isAutoPay()) {
            mPresenter.recovery();
        } else {
            mPresenter.refreshAPI();
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGOUT)})
    public void logout(String s) {
        LogUtils.d(s);
        mUnLoginedLL.setVisibility(View.VISIBLE);
        mLoginedRl.setVisibility(View.GONE);
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_ACCOUNT)})
    public void updateAccount(String s) {
        LogUtils.d(s);
        //  加载账户数据
        mPresenter.getAccount();
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_NETWORK_EXCEPTION)})
    public void shrinkRefreshView(String s) {
        LogUtils.d(s);
        //  收起刷新
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_GOTOTAB_HOME)})
    public void backHome(String s) {
        LogUtils.d(s);
        ((MainActivity) mContext).switchTab(MainActivity.TAB_INDEX_HOME);
    }


    /**
     * 保持session更新
     * 不掉线
     */
    private void keepSessionAlive() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mPresenter.alwaysRequest();
            }
        }, mInterval, mInterval);
    }
}
