package com.dawoo.lotterybox.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.User;
import com.dawoo.lotterybox.view.activity.SecurityCenter;
import com.dawoo.lotterybox.view.activity.CustomService;
import com.dawoo.lotterybox.view.activity.LoginActivity;
import com.dawoo.lotterybox.view.activity.SetttingActivity;
import com.dawoo.lotterybox.view.activity.ShareGiftsActivity;
import com.dawoo.lotterybox.view.activity.payActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MCenterFragment extends BaseFragment {

    @BindView(R.id.service_iv)
    ImageView serviceIv;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.setting_iv)
    ImageView settingIv;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.profile_image_Fl)
    FrameLayout profileImageFl;
    @BindView(R.id.mine_account)
    TextView mine_account;
    @BindView(R.id.mine_name_id)
    TextView mine_name_id;
    @BindView(R.id.mine_name)
    TextView mine_name;
    @BindView(R.id.total_assets_lable)
    TextView totalAssetsLable;
    @BindView(R.id.total_assets_value)
    TextView totalAssetsValue;
    @BindView(R.id.purse_balance_value)
    TextView purseBalanceValue;
    @BindView(R.id.purse_balance_lable)
    TextView purseBalanceLable;
    @BindView(R.id.asset_ll)
    LinearLayout assetLl;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.mine_user_lv)
    TextView mineUserLv;
    @BindView(R.id.recharge_ll)
    LinearLayout rechargeLl;
    @BindView(R.id.point)
    View point;
    @BindView(R.id.withdraw_label_tv)
    TextView withdrawLabelTv;
    @BindView(R.id.withdraw_progress_tv)
    TextView withdrawProgressTv;
    @BindView(R.id.drawcash_rl)
    RelativeLayout drawcashRl;
    @BindView(R.id.mine_game_record_bootom_iv_view)
    View mineGameRecordBootomIvView;
    @BindView(R.id.mine_game_record_bootom_iv)
    ImageView mineGameRecordBootomIv;
    @BindView(R.id.mine_game_record_bootom_tv)
    TextView mineGameRecordBootomTv;
    @BindView(R.id.mine_game_record_bootom)
    TextView mineGameRecordBootom;
    @BindView(R.id.mine_game_record)
    RelativeLayout mineGameRecord;
    @BindView(R.id.mine_security_center_bootom_view)
    View mineSecurityCenterBootomView;
    @BindView(R.id.mine_security_center_bootom_iv)
    ImageView mineSecurityCenterBootomIv;
    @BindView(R.id.mine_security_center_bootom_tv)
    TextView mineSecurityCenterBootomTv;
    @BindView(R.id.mine_security_center_bootom)
    TextView mineSecurityCenterBootom;
    @BindView(R.id.textView)
    ImageView textView;
    @BindView(R.id.mine_security_center)
    RelativeLayout mineSecurityCenter;
    @BindView(R.id.point_views)
    View pointViews;
    @BindView(R.id.mine_account_details_bootom_iv)
    ImageView mineAccountDetailsBootomIv;
    @BindView(R.id.mine_account_details_bootom_tv)
    TextView mineAccountDetailsBootomTv;
    @BindView(R.id.mine_account_details_bootom)
    TextView mineAccountDetailsBootom;
    @BindView(R.id.mine_account_details)
    RelativeLayout mineAccountDetails;
    @BindView(R.id.mine_recharge_record_view)
    View mineRechargeRecordView;
    @BindView(R.id.mine_recharge_record_iv)
    ImageView mineRechargeRecordIv;
    @BindView(R.id.mine_recharge_record_tv)
    TextView mineRechargeRecordTv;
    @BindView(R.id.mine_recharge_record_bootom)
    TextView mineRechargeRecordBootom;
    @BindView(R.id.mine_recharge_record)
    RelativeLayout mineRechargeRecord;
    @BindView(R.id.mine_recommend_friends_view)
    View mineRecommendFriendsView;
    @BindView(R.id.mine_account_details_bootom_iv_s)
    ImageView mineAccountDetailsBootomIvS;
    @BindView(R.id.mine_account_details_bootom_tv_s)
    TextView mineAccountDetailsBootomTvS;
    @BindView(R.id.mine_account_details_bootom_s)
    TextView mineAccountDetailsBootomS;
    @BindView(R.id.mine_recommend_friends)
    RelativeLayout mineRecommendFriends;
    @BindView(R.id.mine_wallet_management_s)
    View mineWalletManagementS;
    @BindView(R.id.mine_recharge_record_iv_s)
    ImageView mineRechargeRecordIvS;
    @BindView(R.id.mine_recharge_record_tv_s)
    TextView mineRechargeRecordTvS;
    @BindView(R.id.mine_recharge_record_bootom_s)
    TextView mineRechargeRecordBootomS;
    @BindView(R.id.mine_wallet_management)
    RelativeLayout mineWalletManagement;
    @BindView(R.id.mine_recommend_friends_views)
    View mineRecommendFriendsViews;
    @BindView(R.id.mine_account_details_bootom_iv_ss)
    ImageView mineAccountDetailsBootomIvSs;
    @BindView(R.id.mine_account_details_bootom_tv_ss)
    TextView mineAccountDetailsBootomTvSs;
    @BindView(R.id.mine_account_details_bootom_ss)
    TextView mineAccountDetailsBootomSs;
    @BindView(R.id.mine_message_center)
    RelativeLayout mineMessageCenter;
    @BindView(R.id.mine_wallet_management_sss)
    View mineWalletManagementSss;
    @BindView(R.id.mine_feedback_bootom_ss)
    ImageView mineFeedbackBootomSs;
    @BindView(R.id.mine_feedback_bootom_s)
    TextView mineFeedbackBootomS;
    @BindView(R.id.mine_feedback_bootom)
    TextView mineFeedbackBootom;
    @BindView(R.id.mine_feedback)
    RelativeLayout mineFeedback;
    @BindView(R.id.mine_share_gifts_view)
    View mineShareGiftsView;
    @BindView(R.id.mine_share_gifts_bootom_tv_iv)
    ImageView mineShareGiftsBootomTvIv;
    @BindView(R.id.mine_share_gifts_bootom_tv)
    TextView mineShareGiftsBootomTv;
    @BindView(R.id.mine_share_gifts_bootom_s)
    TextView mineShareGiftsBootomS;
    @BindView(R.id.mine_share_gifts)
    RelativeLayout mineShareGifts;
    @BindView(R.id.mine_contact_custom_service_bootom_view)
    View mineContactCustomServiceBootomView;
    @BindView(R.id.mine_contact_custom_service_bootom_iv)
    ImageView mineContactCustomServiceBootomIv;
    @BindView(R.id.mine_contact_custom_service_bootom_tv)
    TextView mineContactCustomServiceBootomTv;
    @BindView(R.id.mine_contact_custom_service_bootom)
    TextView mineContactCustomServiceBootom;
    @BindView(R.id.mine_contact_custom_service)
    RelativeLayout mineContactCustomService;
    @BindView(R.id.money_prb)
    ProgressBar moneyPrb;
    Unbinder unbinder;
    private User user = new User();


    public MCenterFragment() {
    }

    public static MCenterFragment newInstance() {
        MCenterFragment fragment = new MCenterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_member_center, container, false);
        unbinder = ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected void loadData() {

    }

    public void initViews() {
        /**
         * 如果沒有登录进行登录
         */
        if (!user.isLogin()) {
            mine_account.setVisibility(View.GONE);
            mine_name_id.setVisibility(View.GONE);
            mine_name.setVisibility(View.GONE);
            mineUserLv.setVisibility(View.GONE);
            assetLl.setVisibility(View.GONE);
            tvLogin.setVisibility(View.VISIBLE);
        } else {
            /**
             *登录情况下
             */
            mine_account.setVisibility(View.VISIBLE);
            mine_name_id.setVisibility(View.VISIBLE);
            mine_name.setVisibility(View.VISIBLE);
            mineUserLv.setVisibility(View.VISIBLE);
            assetLl.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.GONE);
        }
    }

    private void initData() {


    }

    @OnClick({R.id.service_iv, R.id.setting_iv, R.id.tv_login, R.id.mine_game_record, R.id.mine_security_center, R.id.mine_account_details,
            R.id.mine_recharge_record, R.id.mine_recommend_friends, R.id.mine_wallet_management, R.id.mine_message_center, R.id.mine_feedback,
            R.id.mine_share_gifts, R.id.mine_contact_custom_service, R.id.asset_ll, R.id.recharge_ll, R.id.drawcash_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.service_iv:
                //左上角的客服
                startActivity(new Intent(getActivity(), CustomService.class));
                Toast.makeText(getContext(), "客服界面", Toast.LENGTH_LONG).show();
                break;
            case R.id.setting_iv:
                startActivity(new Intent(getActivity(), SetttingActivity.class));
                break;
            case R.id.tv_login:
                if (!user.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.mine_game_record:
                Toast.makeText(getContext(), "游戏记录", Toast.LENGTH_LONG).show();
                break;
            //安全中心
            case R.id.mine_security_center:
                startActivity(new Intent(getContext(), SecurityCenter.class));
                break;
            case R.id.mine_account_details:
                Toast.makeText(getContext(), "账户明细", Toast.LENGTH_LONG).show();
                break;
            case R.id.mine_recharge_record:
                Toast.makeText(getContext(), "充值记录", Toast.LENGTH_LONG).show();
                break;
            case R.id.mine_recommend_friends:
                Toast.makeText(getContext(), "推荐好友", Toast.LENGTH_LONG).show();
                break;
            case R.id.mine_wallet_management:
                Toast.makeText(getContext(), "钱包管理", Toast.LENGTH_LONG).show();
                break;
            case R.id.mine_message_center:
                Toast.makeText(getContext(), "消息中心", Toast.LENGTH_LONG).show();
                break;
            case R.id.mine_feedback:
                Toast.makeText(getContext(), "意见反馈", Toast.LENGTH_LONG).show();
                break;
            //分享礼金
            case R.id.mine_share_gifts:
                startActivity(new Intent(getActivity(), ShareGiftsActivity.class));
                break;
//                联系客服
            case R.id.mine_contact_custom_service:
                startActivity(new Intent(getActivity(), CustomService.class));
                break;
            //实时刷新金额  请求数据
            case R.id.asset_ll:
                ToastUtil.showToastShort(getActivity(), "进行数据刷新");

                purseBalanceValue.setVisibility(View.GONE);
                //模拟延迟显示
                Handler handler = new Handler();
                handler.postDelayed(() -> {

                    moneyPrb.setVisibility(View.VISIBLE);
                }, 500);
                /**
                 * 进行数据请求   请求成功prb进行关闭 显示金额的组件
                 */

                break;

            //充值
            case R.id.recharge_ll:
                startActivity(new Intent(getActivity(), payActivity.class));
                break;
            //提現
            case R.id.drawcash_rl:
                Toast.makeText(getContext(), "提現", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }
}