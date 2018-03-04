package com.dawoo.lotterybox.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.DataCenter;
import com.dawoo.lotterybox.bean.User;
import com.dawoo.lotterybox.view.fragment.HallFragment;
import com.dawoo.lotterybox.view.fragment.NoteRcdFragment;
import com.dawoo.lotterybox.view.fragment.LotteryRcdFragment;
import com.dawoo.lotterybox.view.fragment.MCenterFragment;
import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.lottery_hall_ll)
    LinearLayout mLotteryHallLL;
    @BindView(R.id.lottery_record_ll)
    LinearLayout mLotteryRecordLL;
    @BindView(R.id.note_record_ll)
    LinearLayout mNoteRecordLL;
    @BindView(R.id.member_center_ll)
    LinearLayout mMemberCenterLL;
    @BindView(R.id.llTab)
    LinearLayout mLlTab;


    public static final int TAB_INDEX_HALL = 0;
    private final int TAB_INDEX_LOTTERY_RECORD = 1;
    public static final int TAB_INDEX_NOTE_RECORD = 2;
    public static final int TAB_INDEX_MEMBER_CENTER = 3;
    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;


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
        // 创建fragment 填充到viewpager中
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HallFragment.newInstance());
        fragmentList.add(LotteryRcdFragment.newInstance());
        fragmentList.add(NoteRcdFragment.newInstance());
        fragmentList.add(MCenterFragment.newInstance());
        AdapterFragment adpter = new AdapterFragment(fragmentManager, fragmentList);
        mViewPager.setAdapter(adpter);

        // 初始化选中home页
        switchTab(TAB_INDEX_HALL);
        // 自定义侧栏菜单icon颜色
        // mNavigationView.setItemIconTintList(null);
        // 预加载数量
        mViewPager.setOffscreenPageLimit(3);

        // viewpager 设置滚动监听
        mViewPager.addOnPageChangeListener(this);
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


    @OnClick({R.id.lottery_hall_ll, R.id.lottery_record_ll, R.id.note_record_ll, R.id.member_center_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lottery_hall_ll:
                switchTab(TAB_INDEX_HALL);
                break;
            case R.id.lottery_record_ll:
                switchTab(TAB_INDEX_LOTTERY_RECORD);
                break;
            case R.id.note_record_ll:
                switchTab(TAB_INDEX_NOTE_RECORD);
                break;
            case R.id.member_center_ll:
                switchTab(TAB_INDEX_MEMBER_CENTER);
                break;
                default:
        }
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
        closeApp();
    }


    /**
     * 切换页面
     * 连带改变tab选中状态
     *
     * @param index tab下标
     */
    public void switchTab(int index) {
        switch (index) {
            case TAB_INDEX_HALL:
                mViewPager.setCurrentItem(TAB_INDEX_HALL, false);
                changeTabState(index);
                break;
            case TAB_INDEX_LOTTERY_RECORD:
                mViewPager.setCurrentItem(TAB_INDEX_LOTTERY_RECORD, false);
                changeTabState(index);
                break;
            case TAB_INDEX_NOTE_RECORD:
                mViewPager.setCurrentItem(TAB_INDEX_NOTE_RECORD, false);
                changeTabState(index);
                break;
            case TAB_INDEX_MEMBER_CENTER:
                mViewPager.setCurrentItem(TAB_INDEX_MEMBER_CENTER, false);
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
            case TAB_INDEX_HALL:
                mLotteryHallLL.setSelected(true);
                mLotteryRecordLL.setSelected(false);
                mNoteRecordLL.setSelected(false);
                mMemberCenterLL.setSelected(false);
                break;
            case TAB_INDEX_LOTTERY_RECORD:
                mLotteryHallLL.setSelected(false);
                mLotteryRecordLL.setSelected(true);
                mNoteRecordLL.setSelected(false);
                mMemberCenterLL.setSelected(false);
                break;
            case TAB_INDEX_NOTE_RECORD:
                mLotteryHallLL.setSelected(false);
                mLotteryRecordLL.setSelected(false);
                mNoteRecordLL.setSelected(true);
                mMemberCenterLL.setSelected(false);
                break;
            case TAB_INDEX_MEMBER_CENTER:
                mLotteryHallLL.setSelected(false);
                mLotteryRecordLL.setSelected(false);
                mNoteRecordLL.setSelected(false);
                mMemberCenterLL.setSelected(true);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
