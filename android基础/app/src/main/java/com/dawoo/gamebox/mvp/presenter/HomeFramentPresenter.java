package com.dawoo.gamebox.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.SiteApiRelation;
import com.dawoo.gamebox.bean.UrlBean;
import com.dawoo.gamebox.bean.UserAccount;
import com.dawoo.gamebox.bean.UserAssert;
import com.dawoo.gamebox.mvp.model.account.AccountModel;
import com.dawoo.gamebox.mvp.model.game.GameModel;
import com.dawoo.gamebox.mvp.model.home.HomeModel;
import com.dawoo.gamebox.mvp.view.IBaseView;
import com.dawoo.gamebox.mvp.view.IHomeFragmentView;
import com.dawoo.gamebox.net.rx.ProgressSubscriber;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.NetUtil;
import com.dawoo.gamebox.view.activity.VideoGameListActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscription;


/**
 * 首页presenter
 */

public class HomeFramentPresenter<T extends IBaseView> extends BasePresenter {

    private final Context mContext;
    private T mView;
    private final HomeModel mModel;
    private final GameModel mGameModel;
    private final AccountModel mAccountModel;

    public HomeFramentPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new HomeModel();
        mGameModel = new GameModel();
        mAccountModel = new AccountModel();
    }


    /**
     * 获取轮播图片
     */
    public void getBanners() {
        Subscription subscription = mModel.getBanner(new ProgressSubscriber(o -> ((IHomeFragmentView) mView).onBannerResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取公告
     */
    public void getNotice() {
        Subscription subscription = mModel.getNotice(new ProgressSubscriber(o -> ((IHomeFragmentView) mView).onNoticeResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取游戏列表
     */
    public void getSiteApiRelation() {
        Subscription subscription = mModel.getSiteApiRelation(new ProgressSubscriber(o -> ((IHomeFragmentView) mView).onSiteApiRelationResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取浮动图
     */
    public void getFAB() {
        Subscription subscription = mModel.getFAB(new ProgressSubscriber(o -> ((IHomeFragmentView) mView).onFloatResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取账户数据
     */
    public void getAccount() {
        Subscription subscription = mModel.getAccount(new ProgressSubscriber(((IHomeFragmentView) mView)::onAccountResult, mContext));
        subList.add(subscription);
    }

    /**
     * 获取用户资产
     */
    public void getAssert() {
        Subscription subscription = mAccountModel.getUserAssert(new ProgressSubscriber(((IHomeFragmentView) mView)::onAssertResult, mContext));
        subList.add(subscription);
    }


    /**
     * 获取游戏链接
     */
    public void getGameLink(int apiId, int apiTypeId, @Nullable int gameId, @Nullable String gameCode) {
        Subscription subscription = mGameModel.getGameLink(new ProgressSubscriber(o ->
                        ((IHomeFragmentView) mView).onLoadGameLink(o), mContext),
                apiId,
                apiTypeId,
                gameId,
                gameCode);
        subList.add(subscription);
    }

    /**
     * 获取游戏链接
     */
    public void getGameLink(int apiId, int apiTypeId) {
        Subscription subscription = mGameModel.getGameLink(new ProgressSubscriber(o ->
                        ((IHomeFragmentView) mView).onLoadGameLink(o), mContext),
                apiId,
                apiTypeId);
        subList.add(subscription);
    }

    /**
     * 获取红包次数
     */
    public void countDrawTimes(String activityMessageId) {
        Subscription subscription = mModel.countDrawTimes(new ProgressSubscriber(o ->
                        ((IHomeFragmentView) mView).onHongBaoCountResult(o), mContext),
                activityMessageId);
        subList.add(subscription);
    }

    /**
     * 打开红包
     *
     * @param activityMessageId
     * @param token
     */
    public void getPacket(String activityMessageId, String token) {
        Subscription subscription = mModel.getPacket(new ProgressSubscriber(o ->
                        ((IHomeFragmentView) mView).onGetPacketResult(o), mContext),
                activityMessageId,
                token);
        subList.add(subscription);
    }

    /**
     * 一键回收
     */
    public void recovery() {
        Subscription subscription = mModel.recovery(new ProgressSubscriber(o ->
                ((IHomeFragmentView) mView).onRecoveryResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 一键刷新
     */
    public void refreshAPI() {
        Subscription subscription = mModel.refresh(new ProgressSubscriber(o ->
                ((IHomeFragmentView) mView).onRefreshResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取时区
     */
    public void getTimeZone() {
        Subscription subscription = mModel.getTimeZone(new ProgressSubscriber(o ->
                ((IHomeFragmentView) mView).getTimeZone(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取时区
     */
    public void alwaysRequest() {
        Subscription subscription = mModel.alwaysRequest(new ProgressSubscriber(o ->
                ((IHomeFragmentView) mView).onAlwaysRequestResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 替换不同类型数据
     * 资产数据填充到账户类型数据
     *
     * @param o
     * @return
     */
    public Object rePlaceData(Object o, UserAccount account) {
        if (o != null && o instanceof UserAssert) {
            UserAssert userAssert = (UserAssert) o;
            UserAccount.UserBean userBean = account.getUser();
            userBean.setUsername(userAssert.getUsername());
            userBean.setCurrency(userAssert.getCurrSign());
            userBean.setTotalAssets(userAssert.getAssets());
            userBean.setWalletBalance(userAssert.getBalance());

            // 替换api
            List<UserAccount.UserBean.ApisBean> apis = userBean.getApis();
            List<UserAssert.ApisBean> apis2 = userAssert.getApis();
            UserAccount.UserBean.ApisBean api;
            UserAssert.ApisBean api2;
            for (int i = 0; i < apis.size(); i++) {
                api = new UserAccount.UserBean.ApisBean();
                api2 = apis2.get(i);
                api.setApiId(api2.getApiId());
                api.setApiName(api2.getApiName());
                api.setBalance(api2.getBalance());
                api.setStatus(api2.getStatus());
                apis.set(i, api);
            }
            userBean.setApis(apis);
            account.setUser(userBean);
        }

        return account;
    }


    /**
     * 处理api游戏的点击事件
     *
     * @param mLevel
     * @param gameListBean
     * @param siteApisBean
     */
    public void onApiItemClick(boolean mLevel, SiteApiRelation.SiteApisBean.GameListBean gameListBean,
                               SiteApiRelation.SiteApisBean siteApisBean) {
        // 请求链接
        if (mLevel) {
            if (gameListBean != null) {
                // 请求链接
                dealwithItemURL(gameListBean);
            }
        } else {
            // 处理非彩票
            dealwithItemURL(siteApisBean);
        }
    }

    /**
     * 处理api的链接
     *
     * @param o
     */
    void dealwithItemURL(Object o) {
        if (o == null) {
            return;
        }
        String domain = DataCenter.getInstance().getDomain();
        // 有第三列数据类型
        if (o instanceof SiteApiRelation.SiteApisBean.GameListBean) {
            SiteApiRelation.SiteApisBean.GameListBean gameListBean = (SiteApiRelation.SiteApisBean.GameListBean) o;
            dealRealGameURL_level(gameListBean.getApiTypeId(), gameListBean.getApiId(), gameListBean.getName(), domain, gameListBean.getGameLink());
        } else if (o instanceof SiteApiRelation.SiteApisBean) {
            // typeId == 2 是电子游戏
            SiteApiRelation.SiteApisBean siteApisBean = (SiteApiRelation.SiteApisBean) o;
            dealRealGameURL(siteApisBean.getApiTypeId(), siteApisBean.getApiId(), siteApisBean.getName(), domain, siteApisBean.getGameLink());
        }
    }

    private void dealRealGameURL_level(int apiTypeId, int apiId, String name, String domain, String link) {
        if (link == null) {
            return;
        }
        getRealGameURL(domain, link, apiId);
    }

    private void dealRealGameURL(int apiTypeId, int apiId, String name, String domain, String link) {
        if (link == null) {
            return;
        }

        if (2 == apiTypeId) {
            // 电子游戏
            startVideoGameActivity(apiTypeId, apiId, name);
        } else {
            getRealGameURL(domain, link, apiId);
        }
    }

    private void getRealGameURL(String domain, String link, int apiId) {
        if (link == null) {
            return;
        }

        if (link.startsWith("/mobile-api")) {
            // link含有 /mobile-api
            // 非电子游戏 请求链接
            getGameLink(link);
        } else {
            if (22 == apiId) {
                ActivityUtil.startWebView(domain + link, "", ConstantValue.WEBVIEW_TYPE_GAME_WITH_HEAD_VIEW);
            } else {
                ActivityUtil.startWebView(domain + link, "", ConstantValue.WEBVIEW_TYPE_GAME);
            }
        }
    }


    /**
     * 进入电子游戏
     */
    public void startVideoGameActivity(int apiTypeId, int apiId, String name) {
        Intent intent = new Intent(mContext, VideoGameListActivity.class);
        intent.putExtra(VideoGameListActivity.API_TYPE_ID, apiTypeId);
        intent.putExtra(VideoGameListActivity.API_ID, apiId);
        intent.putExtra(VideoGameListActivity.GEME_NAME, name);

        mContext.startActivity(intent);
    }

    /**
     * 获取游戏link
     */
    public void getGameLink(String link) {
        Subscription subscription = mModel.getAPILink(new ProgressSubscriber(o ->
                        ((IHomeFragmentView) mView).onGameLinkResult(o), mContext),
                link);
        subList.add(subscription);
    }


    /**
     * 处理返回的游戏链接
     *
     * @param o
     */
    public void doResultGameLink(Object o) {
        if (o != null && o instanceof UrlBean) {
            UrlBean urlBean = (UrlBean) o;
            if (urlBean.getGameLink() == null) {
                ToastUtil.showToastShort(mContext, urlBean.getGameMsg());
                return;
            }

            String domain = DataCenter.getInstance().getDomain();
            String url = urlBean.getGameLink();
            url = NetUtil.handleUrl(domain, url);
            if (url.endsWith("ad=22")) {
                ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_GAME_WITH_HEAD_VIEW);
            } else {
                ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_GAME);
            }
        }
    }

    @Override
    public void onDestory() {
        super.onDestory();
    }
}
