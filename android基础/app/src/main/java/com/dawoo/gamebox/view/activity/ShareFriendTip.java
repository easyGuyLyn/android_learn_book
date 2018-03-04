package com.dawoo.gamebox.view.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.gamebox.BuildConfig;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.UserPlayerRecommend;
import com.dawoo.gamebox.mvp.presenter.MineFramentPresenter;
import com.dawoo.gamebox.mvp.view.IGetUserPlayerRecommendView;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.fragment.ShareRecodingFragment;
import com.dawoo.gamebox.view.view.CustomHeightViewPager;
import com.dawoo.gamebox.view.view.NoticeDialog;
import com.dawoo.gamebox.view.view.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 18-2-12.
 */

public class ShareFriendTip extends BaseActivity implements IGetUserPlayerRecommendView {

    @BindView(R.id.hend_back)
    ImageView hendBack;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.share_friend_iv)
    ImageView share_friend_iv;
    @BindView(R.id.share_name)
    TextView shareName;
    @BindView(R.id.share_reward)
    TextView shareReward;
    @BindView(R.id.share_Reciprocity)
    TextView shareReciprocity;
    @BindView(R.id.share_share)
    TextView shareShare;
    @BindView(R.id.view_pager)
    CustomHeightViewPager viewPager; //动态设置viewopager的高度
    @BindView(R.id.share_rule)
    Button shareRule;
    @BindView(R.id.share_record)
    Button shareRecord;
    @BindView(R.id.ed_focusable)
    EditText edFocusable;
    @BindView(R.id.btn_copy)
    Button btnCopy;
    private Context context;
    private final int TAB_rule = 0;
    private final int TAB_recoding = 1;
    private boolean isInit = false;


    private MineFramentPresenter mineFramentPresenter;
    private UserPlayerRecommend userPlayerRecommend;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_sharefriendtip);
        context = this;
    }

    @Override
    protected void initViews() {
        mineFramentPresenter = new MineFramentPresenter(this, this);
    }

    @Override
    protected void initData() {
        mineFramentPresenter.getUserPlayerRecommend();
        List<String> titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.reward_rule));
        titles.add(getResources().getString(R.string.share_recording));
        List<Fragment> fragments = new ArrayList<>();
        titles.add(getString(R.string.reward_rule));
        titles.add(getString(R.string.share_recording));
        fragments.add(new ShareRuleRecodingFragmeng());
        fragments.add(new ShareRecodingFragment());
        OneViewPagerAdapter adapter = new OneViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        isInit = true;
        switchTab(TAB_rule);

    }


    @OnClick({R.id.share_rule, R.id.share_record, R.id.share_friend_iv, R.id.btn_copy, R.id.hend_back})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (view.getId()) {
            case R.id.share_rule:
                switchTab(TAB_rule);
                break;

            case R.id.share_record:
                switchTab(TAB_recoding);
                break;

            case R.id.share_friend_iv:
                List<String> strings = new ArrayList<>();
                strings.add(new String(userPlayerRecommend.getActivityRules()));
                NoticeDialog noticeDialog = new NoticeDialog(context, strings);
                noticeDialog.setTitleName(getResources().getString(R.string.notice_titlename));
                noticeDialog.setTextColor(getResources().getColor(R.color.text_color_gray_333333));
                break;

            case R.id.btn_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("share_text", edFocusable.getText().toString());
                cm.setPrimaryClip(mClipData);
                break;

            case R.id.hend_back:
                finish();
                break;
        }
    }

    @Override
    public void onResult(Object o) {
        setDate(o);
    }

    /**
     * 塞数据
     *
     * @param o
     */
    private void setDate(Object o) {
        userPlayerRecommend = (UserPlayerRecommend) o;
        shareName.setText(userPlayerRecommend.getRecommend().getUser() + "");
        shareReward.setText(userPlayerRecommend.getRecommend().getSingle() + "");
        shareReciprocity.setText(userPlayerRecommend.getRecommend().getCount() + "");
        shareShare.setText(userPlayerRecommend.getRecommend().getBonus() + "");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            edFocusable.setText(Html.fromHtml(BuildConfig.HOST_URL + userPlayerRecommend.getCode(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            edFocusable.setText(Html.fromHtml(BuildConfig.HOST_URL + userPlayerRecommend.getCode()));
        }
        Log.e("TAG", "userPlayerRecommend.getCode()" + userPlayerRecommend.getCode());
    }


    public class OneViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragments = new ArrayList<>();
        List<String> mTitles = new ArrayList<>();

        public OneViewPagerAdapter(FragmentManager fm, List fragments, List titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
        }
    }


    void switchTab(int tabIndex) {
        if (!isInit) {
            SoundUtil.getInstance().playVoiceOnclick();
            isInit = false;
        }
        switch (tabIndex) {
            //shareRule shareRecord
            case TAB_rule:
                shareRule.setSelected(true);
                shareRecord.setSelected(false);
                viewPager.setCurrentItem(TAB_rule);
                shareRule.setTextColor(getResources().getColor(R.color.white));
                shareRecord.setTextColor(getResources().getColor(R.color.text_color_gray_333333));
                break;
            case TAB_recoding:
                shareRecord.setSelected(true);
                shareRule.setSelected(false);
                viewPager.setCurrentItem(TAB_recoding);
                shareRule.setTextColor(getResources().getColor(R.color.text_color_gray_333333));
                shareRecord.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        mineFramentPresenter.onDestory();
        super.onDestroy();
    }
}
