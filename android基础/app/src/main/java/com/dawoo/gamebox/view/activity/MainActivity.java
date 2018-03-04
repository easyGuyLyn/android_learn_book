package com.dawoo.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.User;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.fragment.DepositFragment;
import com.dawoo.gamebox.view.fragment.HomeFragment;
import com.dawoo.gamebox.view.fragment.MineFragment;
import com.dawoo.gamebox.view.fragment.PromoFragment;
import com.dawoo.gamebox.view.fragment.ServiceFragment;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.deposit_ll)
    LinearLayout mDepositLl;
    @BindView(R.id.promo_ll)
    LinearLayout mPromoLl;
    @BindView(R.id.home_ll)
    LinearLayout mHomeLl;
    @BindView(R.id.service_ll)
    LinearLayout mServiceLl;
    @BindView(R.id.mine_ll)
    LinearLayout mMineLl;
    @BindView(R.id.llTab)
    LinearLayout mLlTab;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.simple_navigation_drawer)
    DrawerLayout mNavigationDrawer;


    public static final int TAB_INDEX_DEPOSIT = 0;
    private final int TAB_INDEX_PROMO = 1;
    public static final int TAB_INDEX_HOME = 2;
    public static final int TAB_INDEX_SERVICE = 3;
    private final int TAB_INDEX_MINE = 4;
    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    public static final String FLAG = "flag";
    public static final String EXIT_APP = "exit_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        mNavigationView.setNavigationItemSelectedListener(this);
        // 创建fragment 填充到viewpager中
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(DepositFragment.newInstance());
        fragmentList.add(PromoFragment.newInstance());
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(ServiceFragment.newInstance());
        fragmentList.add(MineFragment.newInstance());
        AdapterFragment adpter = new AdapterFragment(fragmentManager, fragmentList);
        mViewPager.setAdapter(adpter);

        // 初始化选中home页
        switchTab(TAB_INDEX_HOME);
        // 自定义侧栏菜单icon颜色
        // mNavigationView.setItemIconTintList(null);
        // 预加载数量
        mViewPager.setOffscreenPageLimit(4);

        // viewpager 设置滚动监听
        mViewPager.addOnPageChangeListener(this);

        RxBus.get().register(this);
        mNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    protected void onDestroy() {
        RxBus.get().unregister(this);
        DataCenter.getInstance().setUser(new User());
        super.onDestroy();
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.deposit_ll, R.id.promo_ll, R.id.home_ll, R.id.service_ll, R.id.mine_ll})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (view.getId()) {
            case R.id.deposit_ll:
                if (DataCenter.getInstance().isLogin()) {
                    switchTab(TAB_INDEX_DEPOSIT);
                } else {
                    ActivityUtil.gotoLogin();
                }
                break;
            case R.id.promo_ll:
                switchTab(TAB_INDEX_PROMO);
                break;
            case R.id.home_ll:
                switchTab(TAB_INDEX_HOME);
                break;
            case R.id.service_ll:
                switchTab(TAB_INDEX_SERVICE);
                break;
            case R.id.mine_ll:
                switchTab(TAB_INDEX_MINE);
//                if (BoxApplication.isLogin) {
//
//                } else {
//                    ActivityUtil.gotoLogin();
//                }
                break;

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                switchTab(TAB_INDEX_HOME);
                break;
            case R.id.navigation_promo:
                switchTab(TAB_INDEX_PROMO);
                break;
            case R.id.navigation_transfer:
                Toast.makeText(this, "transfer 被点击", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_deposit:
                switchTab(TAB_INDEX_DEPOSIT);
                break;
            case R.id.navigation_service:
                switchTab(TAB_INDEX_SERVICE);
                break;
            case R.id.navigation_questions:
                Toast.makeText(this, "questions 被点击", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_protocol:
                Toast.makeText(this, "protocol 被点击", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_about:
                Toast.makeText(this, "about 被点击", Toast.LENGTH_SHORT).show();
                break;
        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        changeTabState(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class AdapterFragment extends FragmentPagerAdapter {
        List<Fragment> mFragments = new ArrayList<>();

        public AdapterFragment(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments.clear();
            mFragments.addAll(fragments);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            closeApp();
        }
    }


    /**
     * 切换页面
     * 连带改变tab选中状态
     *
     * @param index tab下标
     */
    public void switchTab(int index) {
        switch (index) {
            case TAB_INDEX_DEPOSIT:
                mViewPager.setCurrentItem(TAB_INDEX_DEPOSIT, false);
                changeTabState(index);
                break;
            case TAB_INDEX_PROMO:
                mViewPager.setCurrentItem(TAB_INDEX_PROMO, false);
                changeTabState(index);
                break;
            case TAB_INDEX_HOME:
                mViewPager.setCurrentItem(TAB_INDEX_HOME, false);
                changeTabState(index);
                break;
            case TAB_INDEX_SERVICE:
                mViewPager.setCurrentItem(TAB_INDEX_SERVICE, false);
                changeTabState(index);
                break;
            case TAB_INDEX_MINE:
                mViewPager.setCurrentItem(TAB_INDEX_MINE, false);
                changeTabState(index);
                break;
        }
    }

    /**
     * 改变tab的选中和未选中状态
     *
     * @param index
     */
    private void changeTabState(int index) {
        switch (index) {
            case TAB_INDEX_DEPOSIT:
                mDepositLl.setSelected(true);
                mPromoLl.setSelected(false);
                mHomeLl.setSelected(false);
                mServiceLl.setSelected(false);
                mMineLl.setSelected(false);
                break;
            case TAB_INDEX_PROMO:
                mDepositLl.setSelected(false);
                mPromoLl.setSelected(true);
                mHomeLl.setSelected(false);
                mServiceLl.setSelected(false);
                mMineLl.setSelected(false);
                break;
            case TAB_INDEX_HOME:
                mDepositLl.setSelected(false);
                mPromoLl.setSelected(false);
                mHomeLl.setSelected(true);
                mServiceLl.setSelected(false);
                mMineLl.setSelected(false);
                break;
            case TAB_INDEX_SERVICE:
                mDepositLl.setSelected(false);
                mPromoLl.setSelected(false);
                mHomeLl.setSelected(false);
                mServiceLl.setSelected(true);
                mMineLl.setSelected(false);
                break;
            case TAB_INDEX_MINE:
                mDepositLl.setSelected(false);
                mPromoLl.setSelected(false);
                mHomeLl.setSelected(false);
                mServiceLl.setSelected(false);
                mMineLl.setSelected(true);
                break;
        }
    }

    /**
     * 回调去存款
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_GOTOTAB_DEPOSIT)})
    public void gotoTabDeposit(String s) {
        switchTab(TAB_INDEX_DEPOSIT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getStringExtra(FLAG) != null && getIntent().getStringExtra(FLAG).equals(EXIT_APP)) {
            finish();
        }
    }

    private void closeApp() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            ActivityStackManager.getInstance().finishAllActivity();
        }
    }
}
