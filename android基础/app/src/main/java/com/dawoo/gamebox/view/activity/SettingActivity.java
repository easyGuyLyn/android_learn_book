package com.dawoo.gamebox.view.activity;

import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.SharePreferenceUtil;
import com.dawoo.gamebox.bean.Logout;
import com.dawoo.gamebox.mvp.presenter.UserPresenter;
import com.dawoo.gamebox.mvp.view.IBaseView;
import com.dawoo.gamebox.mvp.view.ILoginOutView;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.view.HeaderView;
import com.guoqi.iosdialog.IOSDialog;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements ILoginOutView, IBaseView {

    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.voice_switch)
    Switch mVoiceSwitch;
    @BindView(R.id.setting_voice_rl)
    RelativeLayout mSettingVoiceRl;
    @BindView(R.id.gisture_switch)
    Switch mGistureSwitch;
    @BindView(R.id.setting_gisture_rl)
    RelativeLayout mSettingGistureRl;
    @BindView(R.id.logout_btn)
    Button mLogoutBtn;


    private UserPresenter userPresenter;


    private Context context;


    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onDestroy() {
        userPresenter.onDestory();
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        context = this;
        mHeadView.setHeader(getString(R.string.setting_acitivity), true);
        initGestureListen();
    }

    @Override
    protected void initData() {
        userPresenter = new UserPresenter(this, this);
        if (SharePreferenceUtil.getGestureFlag()) {
            mGistureSwitch.setChecked(true);
        } else {
            mGistureSwitch.setChecked(false);
        }

        if (SharePreferenceUtil.getVoiceStatus(context)) {
            mVoiceSwitch.setChecked(true);
        } else {
            mVoiceSwitch.setChecked(false);
        }


        mVoiceSwitch.setOnClickListener(v -> {
            if (mVoiceSwitch.isChecked()) {
                SoundUtil.getInstance().open();
            } else {
                SoundUtil.getInstance().close();
            }
        });
    }

    private void initGestureListen() {
        mGistureSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundUtil.getInstance().playVoiceOnclick();
                Intent intent = new Intent(SettingActivity.this, GestureActivity.class);
                if (SharePreferenceUtil.getGestureFlag()) {
                    intent.putExtra(GestureActivity.GEST_FLAG, GestureActivity.CLEAR_PWD);
                } else {
                    intent.putExtra(GestureActivity.GEST_FLAG, GestureActivity.SET_PWD);
                }
                startActivityForResult(intent, 1);
            }
        });
    }


    @OnClick(R.id.logout_btn)
    public void onViewClicked() {
        SoundUtil.getInstance().playVoiceOnclick();
        final IOSDialog alertDialog = new IOSDialog(context);
        alertDialog.builder();
        alertDialog.setCancelable(true);
        alertDialog.setMsg("是否确定退出账号?");
        alertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundUtil.getInstance().playVoiceOnclick();
                userPresenter.LoginOut();
                alertDialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundUtil.getInstance().playVoiceOnclick();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
//        IcPropmtDialog icPropmtDialog = new IcPropmtDialog(context);
//        icPropmtDialog.setTitle("确定退出?");
//        String[] but = new String[]{"取消", "确定"};
//        icPropmtDialog.setBtnNames(but);
//        icPropmtDialog.show();
//        icPropmtDialog.setOnCancelListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                icPropmtDialog.dismiss();
//            }
//        });
    }

    @Override
    public void onClickResult(Object o) {
//        Logout logout = (Logout) o;
//        if (logout.isSuccess()) {
//            ExitActivity();
//        } else {
//            ToastUtil.showToastShort(context, logout.getMessage().toString());
//        }

        Logout logout = (Logout) o;
        if (logout.isSuccess()) {
            ActivityUtil.logout();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            initData();
        }
    }


}
