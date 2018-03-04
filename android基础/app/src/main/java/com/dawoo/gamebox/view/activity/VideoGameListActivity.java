package com.dawoo.gamebox.view.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.GameLink;
import com.dawoo.gamebox.bean.VideoGame;
import com.dawoo.gamebox.bean.VideoGameType;
import com.dawoo.gamebox.mvp.presenter.GamePresenter;
import com.dawoo.gamebox.mvp.view.ICasinoGameListView;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.NetUtil;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.view.HeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 电子游戏列表
 * 带搜索
 */
public class VideoGameListActivity extends BaseActivity implements ICasinoGameListView, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.head_view)
    HeaderView mHeaderView;
    @BindView(R.id.game_name_et)
    EditText mGameNameEt;
    @BindView(R.id.search_btn)
    Button mSearchBtn;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;


    public static final String API_ID = "apiId";
    public static final String API_TYPE_ID = "apiTypeId";
    public static final String GEME_NAME = "game_name";
    private GamePresenter mPresenter;
    private int mPageNumber = ConstantValue.RECORD_LIST_page_Number;
    private int mPageSize = 20;
    private VideoGameQuickAdapter mAdapter;
    private int mApiId;
    private int mApiTypeId;
    private List<Integer> mPageNumberTags = new ArrayList<>();//存不同tab的PageNumber
    private List<VideoGameType> mTypeList = new ArrayList<>();//全局类型
    private List<List<VideoGame.CasinoGamesBean>> mTabVideoGames = new ArrayList<>();//不同tab的数据列表
    private int mPosition;
    private String mName = "";


    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_video_game_list);
    }

    @Override
    protected void initViews() {
        String name = getIntent().getStringExtra(GEME_NAME);
        mHeaderView.setHeader(name, true);

        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);

        mAdapter = new VideoGameQuickAdapter(R.layout.recyclerview_list_item_videogame_activity_view);
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, null));

        mRecyclerView.setPadding(0, DensityUtil.dp2px(this, 10), 0, 0);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mApiId = intent.getIntExtra(API_ID, 0);
        mApiTypeId = intent.getIntExtra(API_TYPE_ID, 0);
        mName = mGameNameEt.getText().toString().trim();
        mPresenter = new GamePresenter(this, this);
        mPresenter.getGameTag();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestory();
        super.onDestroy();
    }

    @Override
    public void onLoadResult(Object o) {
        setData(o);
    }

    @Override
    public void onLoadGameTagResult(Object o) {
        if (o != null && o instanceof List) {
            mTypeList.clear();
            mTypeList.add(new VideoGameType("all", getString(R.string.all_games)));
            mTypeList.addAll((List<VideoGameType>) o);
            // 创建tabs数据列表
            for (int i = 0; i < mTypeList.size(); i++) {
                mTabVideoGames.add(new ArrayList<VideoGame.CasinoGamesBean>());
                mPageNumberTags.add(i);
            }

            setGameType(mTypeList);
        }
    }

    @Override
    public void loadMoreData(Object o) {
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }

        if (o != null && o instanceof VideoGame) {
            VideoGame videoGame = (VideoGame) o;
            List<VideoGame.CasinoGamesBean> casinoGames = videoGame.getCasinoGames();

            if (mAdapter != null) {
                int size = mAdapter.getItemCount();
                int totalSize = videoGame.getPage().getPageTotal();
                if (size >= totalSize) {
                    ToastUtil.showToastShort(this, getString(R.string.NO_MORE_DATA));
                    return;
                }
            }

            if (0 != casinoGames.size()) {

                mAdapter.addData(mTabVideoGames.get(mPosition));

                List<VideoGame.CasinoGamesBean> tempList = mTabVideoGames.get(mPosition);
                tempList.addAll(videoGame.getCasinoGames());
                mTabVideoGames.set(mPosition, tempList);
            } else {
                ToastUtil.showResShort(this, R.string.NO_MORE_DATA);
            }
        }
    }

    @Override
    public void onRefreshData() {

    }


    @Override
    public void doSearch() {
        // String newName = mGameNameEt.getText().toString().trim();
        // if (!mName.equals(newName)) {
        // 清空当前序号的数据
        mTabVideoGames.get(mPosition).clear();
        requstData(mPosition);
        // }

    }

    @Override
    public void onLoadGameLink(Object o) {
        GameLink gameLink = (GameLink) o;
        ActivityUtil.startWebView(gameLink.getGameLink(), gameLink.getGameMsg(), ConstantValue.WEBVIEW_TYPE_GAME);
       // finish();
    }

    @Override
    public void onRefresh() {

    }


    @Override
    public void onLoadMore() {
        mPageNumber = mPageNumberTags.get(mPosition);
        mPageNumber++;
        mPageNumberTags.set(mPosition, mPageNumber);
        mName = mGameNameEt.getText().toString().trim();
        if (0 == mPosition) {
            mPresenter.loadMoreCasinoGameList(mApiId, mApiTypeId, mPageNumber, mPageSize, mName);
        } else {
            VideoGameType videoGameType = mTypeList.get(mPosition);
            mPresenter.loadMoreCasinoGameList(mApiId, mApiTypeId, mPageNumber, mPageSize, mName, videoGameType.getValue());
        }
    }

    class VideoGameQuickAdapter extends BaseQuickAdapter {

        public VideoGameQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            VideoGame.CasinoGamesBean casinoGamesBean = (VideoGame.CasinoGamesBean) item;
            helper.setText(R.id.game_name_tv, casinoGamesBean.getName());
            String url = NetUtil.handleUrl(DataCenter.getInstance().getDomain(), casinoGamesBean.getCover());
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_launcher_round);
            Glide.with(mContext).load(url).apply(options).into((ImageView) helper.getView(R.id.game_icon_iv));
            helper.itemView.setOnClickListener(v -> {
                SoundUtil.getInstance().playVoiceOnclick();
               /* startWebView(casinoGamesBean.getGameLink(),casinoGamesBean.getGameMsg());*/
                mPresenter.getGameLink(casinoGamesBean.getApiId(), casinoGamesBean.getApiTypeId(), casinoGamesBean.getGameId(), casinoGamesBean.getCode());
            });
        }
    }


    void setGameType(List<VideoGameType> list) {
        mTabLayout.removeAllTabs();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabSize = mTabVideoGames.get(0).size();
                mPosition = tab.getPosition();
                requstData(mPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        VideoGameType videoGameType = null;
        for (int i = 0; i < list.size(); i++) {
            videoGameType = list.get(i);
            if (0 == i) {
                mTabLayout.addTab(mTabLayout.newTab().setText(videoGameType.getValue()), true);
            } else {
                mTabLayout.addTab(mTabLayout.newTab().setText(videoGameType.getValue()));
            }

        }
    }


    void requstData(int position) {
        List list = mTabVideoGames.get(position);
        mName = mGameNameEt.getText().toString().trim();
        if (0 == list.size()) { // 请求数据
            mPageNumber = ConstantValue.RECORD_LIST_page_Number;
            mPageNumberTags.set(position, mPageNumber);
            if (0 == position) {
                mPresenter.getCasinoGameList(mApiId, mApiTypeId, mPageNumber, mPageSize, mName);
            } else {
                VideoGameType videoGameType = mTypeList.get(mPosition);
                mPresenter.getCasinoGameList(mApiId, mApiTypeId, mPageNumber, mPageSize, mName, videoGameType.getKey());
            }

        } else {
            // 填充数据
            mAdapter.setNewData(list);
        }
    }


    @OnClick({R.id.search_btn})
    void onViewClicked() {
        doSearch();
    }

    void setData(Object o) {
        if (o != null && o instanceof VideoGame) {
            VideoGame videoGame = (VideoGame) o;
            mAdapter.setNewData(videoGame.getCasinoGames());
            mTabVideoGames.get(mPosition).clear();
            mTabVideoGames.set(mPosition, videoGame.getCasinoGames());
        }
    }




}
