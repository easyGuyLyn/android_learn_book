package com.dawoo.gamebox.view.activity;

import android.content.Intent;

import com.dawoo.coretool.CleanLeakUtils;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.util.BackGroundUtil;
import com.dawoo.gamebox.util.SharePreferenceUtil;
import com.dawoo.gamebox.view.view.Gesture.util.AlertUtil;
import com.dawoo.gamebox.view.view.Gesture.widget.EasyGestureView;
import com.dawoo.gamebox.view.view.HeaderView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Archar on 2018
 */
public class GestureActivity extends BaseActivity implements EasyGestureView.GestureCallBack {
    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.gesture)
    EasyGestureView mGesture;


    public static final String GEST_FLAG = "gestureFlg";
    public static final int SET_PWD = 0;//初设手势密码
    public static final int CLEAR_PWD = 1;//清除手势密码
    public static final int RESET_PWD = 2;//重置手势密码时的验证
    public static final int CHECK_PWD = 3;//普通的检验手势密码
    public static final int CHECK_PWD_BACKGROUND = 4;//切回前台的检验手势密码

    private int mGeatureFlg = CHECK_PWD; //默认是检验手势密码

    private Boolean isCheckBackGroudSuccess = false; //专门用来检验切回前台的手势密码的成功

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_setting_pattern_psw);
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader(getString(R.string.gesture_pwd), true);
        if (getIntent().getExtras() != null) {
            mGeatureFlg = getIntent().getIntExtra(GEST_FLAG, CHECK_PWD);
        }
    }

    @Override
    protected void initData() {
        mGesture.setGestureCallBack(this);
        initCache();
    }

    private void initCache() {
        switch (mGeatureFlg) {
            case SET_PWD:
                mHeadView.setHeader(getString(R.string.set_gesture_pwd), true);
                mGesture.clearCache();
                break;
            case CLEAR_PWD:
                mHeadView.setHeader(getString(R.string.clear_gesture_pwd), true);
                mGesture.clearCacheLogin();
                break;
            case RESET_PWD:
                mHeadView.setHeader(getString(R.string.check_gesture_pwd), true);
                break;
            case CHECK_PWD:
                mHeadView.setHeader(getString(R.string.check_gesture_pwd), true);
                break;
            case CHECK_PWD_BACKGROUND:
                mHeadView.setHeader(getString(R.string.check_gesture_pwd), false);
                break;
        }
    }


    @Override
    public void gestureVerifySuccessListener(int stateFlag, List<EasyGestureView.GestureBean> data, boolean success) {
        switch (mGeatureFlg) {
            case SET_PWD:
                if (stateFlag == EasyGestureView.STATE_LOGIN) {
                    SharePreferenceUtil.putGestureFlag(true);
                    AlertUtil.t(getApplicationContext(), getString(R.string.set_gesture_pwd_ok));
                    finish();
                }
                break;
            case CLEAR_PWD:
                if (!success) {
                    finish();
                    return;
                }
                //删除密码
                SharePreferenceUtil.putGestureFlag(false);
                mGesture.clearCache();
                AlertUtil.t(getApplicationContext(), getString(R.string.clear_gesture_pwd_ok));
                finish();
                break;
            case RESET_PWD:
                if (!success) {
                    finish();
                    return;
                }
                //删除旧密码
                SharePreferenceUtil.putGestureFlag(false);
                mGesture.clearCache();
                AlertUtil.t(getApplicationContext(), getString(R.string.check_gesture_pwd_ok_andReset));
                Intent intent = getIntent();
                intent.removeExtra(GEST_FLAG);
                intent.putExtra(GEST_FLAG, SET_PWD);
                startActivity(intent);
                finish();
                break;
            case CHECK_PWD:
                if (!success) {
                    finish();
                    return;
                }
                setResult(RESULT_OK);
                finish();
                break;
            case CHECK_PWD_BACKGROUND:
                if (!success) {
                    return;
                }
                BackGroundUtil.isShouldJumpGesture = false;
                isCheckBackGroudSuccess = true;
                finish();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        if (mGeatureFlg == CHECK_PWD_BACKGROUND && !isCheckBackGroudSuccess) {
            BackGroundUtil.isShouldJumpGesture = false;
            ActivityStackManager.getInstance().finishAllActivity();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }
}
