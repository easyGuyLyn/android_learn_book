package com.dawoo.lotterybox.view.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jack on 18-2-8.
 */

public class UserInforMationActivity extends BaseActivity {
    @BindView(R.id.head_view)
    HeaderView headView;
    @BindView(R.id.mine_avatar)
    CircleImageView mine_avatar;
    @BindView(R.id.account_name)
    TextView accountName;
    @BindView(R.id.account_account)
    TextView accountAccount;
    @BindView(R.id.account_ID)
    TextView accountID;
    @BindView(R.id.account_funding_passwold)
    RelativeLayout accountFundingPasswold;
    @BindView(R.id.account_lock_screen_gesture)
    RelativeLayout accountLockScreenGesture;
    @BindView(R.id.account_device_lock_open)
    ImageView accountDeviceLockOpen;
    @BindView(R.id.account_device_lock_close)
    ImageView accountDeviceLockClose;
    //退出登录
    @BindView(R.id.logout)
    Button login;
    private Context context;

    @Override
    protected void createLayoutView() {
//        setContentView(R.layout.activity_usermation);
        setContentView(R.layout.activity_usermation);
        context = this;
    }

    @Override
    protected void initViews() {
        headView.setHeader(getResources().getString(R.string.account_title), true);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.logout, R.id.mine_avatar, R.id.account_name, R.id.account_account, R.id.account_ID, R.id.account_funding_passwold, R.id.account_lock_screen_gesture, R.id.account_device_lock_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_avatar:
                showMes("头像");
                break;
            case R.id.account_name:
                showMes("昵称");
                break;
            case R.id.account_account:
                showMes("账户");
                break;
            case R.id.account_ID:
                showMes("ID");
                break;
            case R.id.account_funding_passwold:
                showMes("资金密码");
                break;
            case R.id.account_lock_screen_gesture:
                showMes("手势开");
                break;
            case R.id.account_device_lock_open:
                showMes("手势关");
                break;
            case R.id.logout:
                showMes("退出登录");
                break;
            default:
        }
    }

    private void showMes(String msg) {
        ToastUtil.showToastShort(context, msg);
    }

}
