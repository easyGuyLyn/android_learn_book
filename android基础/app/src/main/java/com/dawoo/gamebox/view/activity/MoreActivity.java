package com.dawoo.gamebox.view.activity;

import android.view.View;
import android.widget.RelativeLayout;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.SharePreferenceUtil;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查看更多
 */
public class MoreActivity extends BaseActivity {


    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.common_problems_rl)
    RelativeLayout mCommonProblemsRl;
    @BindView(R.id.register_protocol_rl)
    RelativeLayout mRegisterProtocolRl;
    @BindView(R.id.about_us_rl)
    RelativeLayout mAboutUsRl;
    @BindView(R.id.luanguage_choice_rl)
    RelativeLayout mLuanguageChoiceRl;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_more);
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader(getString(R.string.more_acitivity), true);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.common_problems_rl, R.id.register_protocol_rl, R.id.about_us_rl, R.id.luanguage_choice_rl})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (view.getId()) {
            case R.id.common_problems_rl: {
               // String url = SharePreferenceUtil.getHelpUrl(this);
                String url = DataCenter.getInstance().getDomain() + ConstantValue.HELP_URL;
                ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_ORDINARY);
                break;
            }
            case R.id.register_protocol_rl: {
                //String url = SharePreferenceUtil.getTermsUrl(this);
                String url = DataCenter.getInstance().getDomain() + ConstantValue.REGISTER_RULE_URL;
                ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_ORDINARY);
                break;
            }
            case R.id.about_us_rl: {
               // String url = SharePreferenceUtil.getAboutsUrl(this);
                String url = DataCenter.getInstance().getDomain() + ConstantValue.ABOUT_URL;
                ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_ORDINARY);
                break;
            }
            case R.id.luanguage_choice_rl:
                ToastUtil.showResShort(this, R.string.function_to_be_continue);
                break;
        }
    }
}
