package com.dawoo.lotterybox.view.activity.record;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.record.NoteRecordHis;
import com.dawoo.lotterybox.mvp.presenter.RecordPresenter;
import com.dawoo.lotterybox.mvp.view.INoteRecordDetailView;
import com.dawoo.lotterybox.view.activity.BaseActivity;
import com.dawoo.lotterybox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dawoo.lotterybox.view.activity.record.HistoryRepotFormFragment.nowin;
import static com.dawoo.lotterybox.view.activity.record.HistoryRepotFormFragment.pending;
import static com.dawoo.lotterybox.view.activity.record.HistoryRepotFormFragment.revocation;
import static com.dawoo.lotterybox.view.activity.record.HistoryRepotFormFragment.revoke_self;
import static com.dawoo.lotterybox.view.activity.record.HistoryRepotFormFragment.revoke_sys;
import static com.dawoo.lotterybox.view.activity.record.HistoryRepotFormFragment.wining;

/**
 * Created by archar on 18-2-20.
 */

public class NoteRecordDetailActivity extends BaseActivity implements INoteRecordDetailView {

    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.iv_status_record)
    ImageView mIvStatusRecord;
    @BindView(R.id.tv_expect)
    TextView mTvExpect;
    @BindView(R.id.tv_play_code)
    TextView mTvPlayCode;
    @BindView(R.id.tv_lottery_code)
    TextView mTvLotteryCode;
    @BindView(R.id.tv_betNum)
    TextView mTvBetNum;
    @BindView(R.id.tv_openCode)
    TextView mTvOpenCode;
    @BindView(R.id.tv_betTime)
    TextView mTvBetTime;
    @BindView(R.id.tv_betCount_multiple)
    TextView mTvBetCountMultiple;
    @BindView(R.id.tv_note_idNum)
    TextView mTvNoteIdNum;
    @BindView(R.id.tv_payout)
    TextView mTvPayout;
    @BindView(R.id.tv_bonusModel)
    TextView mTvBonusModel;
    @BindView(R.id.tv_profit)
    TextView mTvProfit;
    @BindView(R.id.tv_betAmount)
    TextView mTvBetAmount;
    @BindView(R.id.tv_rebate)
    TextView mTvRebate;
    @BindView(R.id.tv_idNum)
    TextView mTvIdNum;
    @BindView(R.id.ll_availPrized)
    LinearLayout mLlAvailPrized;
    @BindView(R.id.tv_again_note)
    TextView mTvAgainNote;
    @BindView(R.id.tv_cancel_order)
    TextView mTvCancelOrder;

    private RecordPresenter mRecordPresenter;
    public static final String NOTE_ID = "note_id";
    private int mId;//注单号

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_record_note_detail_layout);
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader(getString(R.string.note_detail), true);
        mId = getIntent().getIntExtra(NOTE_ID, -1);
    }

    @Override
    protected void initData() {
        mRecordPresenter = new RecordPresenter(this, this);
        mRecordPresenter.getOrderDetail(mId + "");
    }


    @Override
    public void onRefreshResult(Object o) {
        NoteRecordHis noteRecordHis = (NoteRecordHis) o;
        if (noteRecordHis.getStatus().equals(pending)) {
            mIvStatusRecord.setImageResource(R.mipmap.img_status_weikaijiang);
            mTvAgainNote.setVisibility(View.VISIBLE);
        } else if (noteRecordHis.getStatus().equals(wining)) {
            mIvStatusRecord.setImageResource(R.mipmap.img_status_zhongjiang);
        } else if (noteRecordHis.getStatus().equals(nowin)) {
            mIvStatusRecord.setImageResource(R.mipmap.img_status_weizhongjiang);
        } else if (noteRecordHis.getStatus().equals(revoke_sys)) {
            mIvStatusRecord.setImageResource(R.mipmap.img_status_cd_system);
        } else if (noteRecordHis.getStatus().equals(revoke_self)) {
            mIvStatusRecord.setImageResource(R.mipmap.img_status_cd_zd);
        } else if (noteRecordHis.getStatus().equals(revocation)) {
            mIvStatusRecord.setImageResource(R.mipmap.img_status_cd_system);
        }
        mTvExpect.setText(getResources().getString(R.string.the_expect, noteRecordHis.getExpect()));
        mTvPlayCode.setText(noteRecordHis.getPlayCode() + "");
        mTvLotteryCode.setText(noteRecordHis.getCode() + "");
        mTvBetNum.setText(noteRecordHis.getBetNum() + "");
        mTvOpenCode.setText(noteRecordHis.getOpenCode() + "");
        mTvBetTime.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, noteRecordHis.getBetTime()));
        mTvBetCountMultiple.setText(noteRecordHis.getBetCount() + "注," + noteRecordHis.getMultiple() + "倍");
        mTvNoteIdNum.setText(noteRecordHis.getId());
        mTvPayout.setText(noteRecordHis.getPayout() + "");
        mTvBonusModel.setText(noteRecordHis.getBonusModel());
        mTvProfit.setText(noteRecordHis.getProfit() + "");
        mTvBetAmount.setText(noteRecordHis.getBetAmount() + "");
        mTvRebate.setText(noteRecordHis.getRebate() + "");
        mTvIdNum.setText("");
    }


    @OnClick({R.id.tv_again_note, R.id.tv_cancel_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_again_note:
                break;
            case R.id.tv_cancel_order:
                break;
        }
    }
}
