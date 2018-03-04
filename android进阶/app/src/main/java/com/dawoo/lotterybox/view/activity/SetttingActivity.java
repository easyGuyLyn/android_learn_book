package com.dawoo.lotterybox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 18-2-8.
 */

public class SetttingActivity extends BaseActivity {
    @BindView(R.id.head_view)
    HeaderView headView;
    @BindView(R.id.setting_account_information)
    TextView settingAccountInformation;
    @BindView(R.id.setting_push_and_remind)
    TextView settingPushAndRemind;
    @BindView(R.id.setting_shake_shake_election_open)
    ImageView settingShakeShakeElectionOpen;
    @BindView(R.id.setting_shake_shake_election_close)
    ImageView settingShakeShakeElectionClose;
    @BindView(R.id.setting_sound_open)
    ImageView settingSoundOpen;
    @BindView(R.id.setting_sound_close)
    ImageView settingSoundClose;
    @BindView(R.id.setting_lock_screen_gesture_open)
    ImageView settingLockScreenGestureOpen;
    @BindView(R.id.setting_lock_screen_gesture_close)
    ImageView settingLockScreenGestureClose;
    @BindView(R.id.setting_winning_animation_open)
    ImageView settingWinningAnimationOpen;
    @BindView(R.id.setting_winning_animation_close)
    ImageView settingWinningAnimationClose;
    @BindView(R.id.logout)
    Button logout;
    private Context context;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_setting);
        context = this;
    }

    @Override
    protected void initViews() {
        headView.setHeader(getResources().getString(R.string.pay_title), true);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.head_view, R.id.setting_account_information, R.id.setting_push_and_remind, R.id.setting_shake_shake_election_open, R.id.setting_shake_shake_election_close, R.id.setting_sound_open, R.id.setting_sound_close, R.id.setting_lock_screen_gesture_open, R.id.setting_lock_screen_gesture_close, R.id.setting_winning_animation_open, R.id.setting_winning_animation_close, R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_view:
                break;
            //账号资料
            case R.id.setting_account_information:
                startActivity(new Intent(context, UserInforMationActivity.class));
                break;
            //推送和提醒
            case R.id.setting_push_and_remind:
                ShowMessage("推送和提醒");
                break;
            //摇一摇机选 开
            case R.id.setting_shake_shake_election_open:
                ShowMessage("摇一摇机选 开");
                break;
            //摇一摇机选 关闭
            case R.id.setting_shake_shake_election_close:
                ShowMessage("摇一摇机选 关闭");
                break;
            //声音 开
            case R.id.setting_sound_open:
                ShowMessage("声音 开");
                break;
            //声音 关闭
            case R.id.setting_sound_close:
                ShowMessage("声音 关闭");
                break;
            //手勢   开
            case R.id.setting_lock_screen_gesture_open:
                ShowMessage("手勢   开");
                break;
            // 手势  关闭
            case R.id.setting_lock_screen_gesture_close:
                ShowMessage("手势  关闭");
                break;
            //中奖动画 开
            case R.id.setting_winning_animation_open:
                ShowMessage("中奖动画 开");
                break;
            //中奖动画 关闭
            case R.id.setting_winning_animation_close:
                ShowMessage("中奖动画 关闭");
                break;
            //退出登录
            case R.id.logout:
                ShowMessage("退出登录");
                break;

            default:
        }
    }

    private void ShowMessage(String Msg) {
        ToastUtil.showToastLong(context, Msg);

    }
}
