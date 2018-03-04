package com.dawoo.lotterybox.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 18-2-9.
 */

public class SecurityCenter extends BaseActivity {
    @BindView(R.id.head_view)
    HeaderView headView;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.securtycentr_name)
    TextView securtycentrName;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.account_name)
    TextView accountName;
    @BindView(R.id.account_change_passwold)
    RelativeLayout accountChangePasswold;
    @BindView(R.id.securitycenter_funding_password)
    RelativeLayout securitycenterFundingPassword;
    @BindView(R.id.securitycenter_lock_screen_gesture)
    RelativeLayout securitycenterLockScreenGesture;
    @BindView(R.id.securitycenter_device_lock_open)
    ImageView securitycenterDeviceLockOpen;
    @BindView(R.id.securitycenter_device_lock_close)
    ImageView securitycenterDeviceLockClose;
    @BindView(R.id.securitycenter_device_lock)
    RelativeLayout securitycenterDeviceLock;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_securtycenter);
    }

    @Override
    protected void initViews() {
        headView.setHeader(getResources().getString(R.string.securitycenter), true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.account_name, R.id.account_change_passwold, R.id.securitycenter_funding_password, R.id.securitycenter_lock_screen_gesture, R.id.securitycenter_device_lock_open, R.id.securitycenter_device_lock_close, R.id.securitycenter_device_lock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.account_name:
                break;
            case R.id.account_change_passwold:
                break;
            case R.id.securitycenter_funding_password:
                break;
            case R.id.securitycenter_lock_screen_gesture:
                break;
            case R.id.securitycenter_device_lock_open:
                break;
            case R.id.securitycenter_device_lock_close:
                break;
            case R.id.securitycenter_device_lock:
                break;
            default:
        }
    }
}
