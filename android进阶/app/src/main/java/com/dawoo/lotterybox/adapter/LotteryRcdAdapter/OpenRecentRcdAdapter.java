package com.dawoo.lotterybox.adapter.LotteryRcdAdapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.lottery.HandicpWithOpening;
import com.dawoo.lotterybox.util.lottery.K3Util;
import com.dawoo.lotterybox.util.lottery.LHCUtil;
import com.dawoo.lotterybox.util.lottery.SSCUtil;

import java.util.Date;
import java.util.List;

/**
 * 彩票最近开奖记录
 * Created by benson on 18-2-19.
 */

public class OpenRecentRcdAdapter extends BaseMultiItemQuickAdapter {
    public static final int ITEM_TYPE_SSC = 1; //时时彩
    public static final int ITEM_TYPE_PK10 = 2; //pk10
    public static final int ITEM_TYPE_LHC = 3; //六合彩
    public static final int ITEM_TYPE_K3 = 4; //快三
    public static final int ITEM_TYPE_SFC = 5; // (重庆幸运农场,广东快乐十分)
    public static final int ITEM_TYPE_KENO = 6;//(keno,北京快乐8)
    public static final int ITEM_TYPE_FC3D = 7;//(福彩3D,体彩排列3)
    public static final int ITEM_TYPE_XY28 = 8;//(幸运28)

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public OpenRecentRcdAdapter(List data) {
        super(data);
        addItemType(ITEM_TYPE_SSC, R.layout.activity_lottery_rcd_ssc);
        addItemType(ITEM_TYPE_PK10, R.layout.activity_lottery_rcd_pk10);
        addItemType(ITEM_TYPE_LHC, R.layout.activity_lottery_rcd_lhc);
        addItemType(ITEM_TYPE_K3, R.layout.activity_lottery_rcd_k3);
        addItemType(ITEM_TYPE_SFC, R.layout.activity_lottery_rcd_sfc);
        addItemType(ITEM_TYPE_KENO, R.layout.activity_lottery_rcd_keno);
        addItemType(ITEM_TYPE_FC3D, R.layout.activity_lottery_rcd_fc3d);
        addItemType(ITEM_TYPE_XY28, R.layout.activity_lottery_rcd_xy28);
    }


    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        if (item == null || !(item instanceof HandicpWithOpening)) {
            return;
        }
        HandicpWithOpening rcd = (HandicpWithOpening) item;

        // 彩种名称
        if (rcd.getCodeMemo() != null) {
            helper.setText(R.id.ssc_name_tv, rcd.getCodeMemo());
        } else {
            helper.setText(R.id.ssc_name_tv, "");
        }
        // 这期期数
        if (rcd.getExpect() != null) {
            helper.setText(R.id.ssc_expect, mContext.getResources().getString(R.string.the_expect, rcd.getExpect()));
        } else {
            helper.setText(R.id.ssc_expect, "");
        }
        // 下期期数
       // helper.setText(R.id.lable_count_time, mContext.getResources().getString(R.string.the_expect_note_end, rcd.getOpeningExpect()));

        switch (helper.getItemViewType()) {
            case ITEM_TYPE_SSC:
                setSSC(helper, rcd);
                break;

            case ITEM_TYPE_PK10:
                setPk10(helper, rcd);
                break;

            case ITEM_TYPE_LHC:
                setLHC(helper, rcd);
                break;

            case ITEM_TYPE_K3:
                setK3(helper, rcd);
                break;

            case ITEM_TYPE_SFC:
                setSFC(helper, rcd);
                break;

            case ITEM_TYPE_KENO:
                setKeno(helper, rcd);
                break;

            case ITEM_TYPE_FC3D:
                setFC3D(helper, rcd);
                break;

            case ITEM_TYPE_XY28:
                setXY28(helper, rcd);
                break;

        }
    }
    private void setXY28(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_TIME));
        if (rcd.getOpenCode() != null && 5 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setText(R.id.xy28_ball_one_tv, OpenCode[0]);
            helper.setText(R.id.xy28_ball_two_tv, OpenCode[1]);
            helper.setText(R.id.xy28_ball_three_tv, OpenCode[2]);

        }
    }

    private void setFC3D(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_TIME));

        if (rcd.getOpenCode() != null && 5 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setText(R.id.fc3d_ball_one_tv, OpenCode[0]);
            helper.setText(R.id.fc3d_ball_two_tv, OpenCode[1]);
            helper.setText(R.id.fc3d_ball_three_tv, OpenCode[2]);
        }
    }

    private void setKeno(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_TIME));

        if (rcd.getOpenCode() != null && 59 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setText(R.id.keno_xy28_ball_one_tv, OpenCode[0]);
            helper.setText(R.id.keno_xy28_ball_two_tv, OpenCode[1]);
            helper.setText(R.id.keno_xy28_ball_three_tv, OpenCode[2]);
            helper.setText(R.id.keno_xy28_ball_four_tv, OpenCode[3]);
            helper.setText(R.id.keno_xy28_ball_five_tv, OpenCode[4]);
            helper.setText(R.id.keno_xy28_ball_six_tv, OpenCode[5]);
            helper.setText(R.id.keno_xy28_ball_seven_tv, OpenCode[6]);
            helper.setText(R.id.keno_xy28_ball_eight_tv, OpenCode[7]);
            helper.setText(R.id.keno_xy28_ball_nine_tv, OpenCode[8]);
            helper.setText(R.id.keno_xy28_ball_ten_tv, OpenCode[9]);
            helper.setText(R.id.keno_xy28_ball_eleven_tv, OpenCode[10]);
            helper.setText(R.id.keno_xy28_ball_twelve_tv, OpenCode[11]);
            helper.setText(R.id.keno_xy28_ball_thirteen_tv, OpenCode[12]);
            helper.setText(R.id.keno_xy28_ball_fourteen_tv, OpenCode[13]);
            helper.setText(R.id.keno_xy28_ball_fifteen_tv, OpenCode[14]);
            helper.setText(R.id.keno_xy28_ball_sixteen_tv, OpenCode[15]);
            helper.setText(R.id.keno_xy28_ball_seventeen_tv, OpenCode[16]);
            helper.setText(R.id.keno_xy28_ball_eighteen_tv, OpenCode[17]);
            helper.setText(R.id.keno_xy28_ball_nineteen_tv, OpenCode[18]);
            helper.setText(R.id.keno_xy28_ball_twenty_tv, OpenCode[19]);
        }
    }

    private void setSFC(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_TIME));

        if (rcd.getOpenCode() != null && 23 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setBackgroundRes(R.id.sfc_ball_one_tv, getBgSfc(OpenCode[0]));
            helper.setBackgroundRes(R.id.sfc_ball_two_tv, getBgSfc(OpenCode[1]));
            helper.setBackgroundRes(R.id.sfc_ball_three_tv, getBgSfc(OpenCode[2]));
            helper.setBackgroundRes(R.id.sfc_ball_four_tv, getBgSfc(OpenCode[3]));
            helper.setBackgroundRes(R.id.sfc_ball_five_tv, getBgSfc(OpenCode[4]));
            helper.setBackgroundRes(R.id.sfc_ball_six_tv, getBgSfc(OpenCode[5]));
            helper.setBackgroundRes(R.id.sfc_ball_seven_tv, getBgSfc(OpenCode[6]));
            helper.setBackgroundRes(R.id.sfc_ball_eight_tv, getBgSfc(OpenCode[7]));
        }
    }

    private void setK3(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_TIME_3));
        if (rcd.getOpenCode() != null && 5 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setBackgroundRes(R.id.k3_ball_one_tv, getBgK3(OpenCode[0]));
            helper.setBackgroundRes(R.id.k3_ball_tow_tv, getBgK3(OpenCode[1]));
            helper.setBackgroundRes(R.id.k3_ball_three_tv, getBgK3(OpenCode[2]));

            helper.setText(R.id.k3_value_sum_tv, "" + K3Util.getHeZhi(OpenCode));
            helper.setText(R.id.k3_lable_group_tv, K3Util.getK3SanBuTongHao(OpenCode));
        }
    }

    private void setLHC(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_DATE_TIME));

        if (rcd.getOpenCode() != null && 20 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setText(R.id.lhc_ball_one_tv, OpenCode[0]);
            helper.setText(R.id.lhc_ball_two_tv, OpenCode[1]);
            helper.setText(R.id.lhc_ball_three_tv, OpenCode[2]);
            helper.setText(R.id.lhc_ball_four_tv, OpenCode[3]);
            helper.setText(R.id.lhc_ball_five_tv, OpenCode[4]);
            helper.setText(R.id.lhc_ball_six_tv, OpenCode[5]);
            helper.setText(R.id.plhc_ball_eight_tv, OpenCode[6]);

            helper.setBackgroundRes(R.id.lhc_ball_one_tv, getBgLHC(OpenCode[0]));
            helper.setBackgroundRes(R.id.lhc_ball_two_tv, getBgLHC(OpenCode[1]));
            helper.setBackgroundRes(R.id.lhc_ball_three_tv, getBgLHC(OpenCode[2]));
            helper.setBackgroundRes(R.id.lhc_ball_four_tv, getBgLHC(OpenCode[3]));
            helper.setBackgroundRes(R.id.lhc_ball_five_tv, getBgLHC(OpenCode[4]));
            helper.setBackgroundRes(R.id.lhc_ball_six_tv, getBgLHC(OpenCode[5]));
            helper.setBackgroundRes(R.id.plhc_ball_eight_tv, getBgLHC(OpenCode[6]));

            helper.setText(R.id.lhc_animal_one_tv, LHCUtil.getShengXiao(OpenCode[0]));
            helper.setText(R.id.lhc_animal_two_tv, LHCUtil.getShengXiao(OpenCode[1]));
            helper.setText(R.id.lhc_animal_three_tv, LHCUtil.getShengXiao(OpenCode[2]));
            helper.setText(R.id.lhc_animal_four_tv, LHCUtil.getShengXiao(OpenCode[3]));
            helper.setText(R.id.lhc_animal_five_tv, LHCUtil.getShengXiao(OpenCode[4]));
            helper.setText(R.id.lhc_animal_six_tv, LHCUtil.getShengXiao(OpenCode[5]));
            helper.setText(R.id.lhc_animal_eight_tv, LHCUtil.getShengXiao(OpenCode[6]));
        }
    }

    private void setPk10(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_TIME_3));

        if (rcd.getOpenCode() != null && 29 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setBackgroundRes(R.id.pk10_ball_one_tv, getBgPk10(OpenCode[0]));
            helper.setBackgroundRes(R.id.pk10_ball_two_tv, getBgPk10(OpenCode[1]));
            helper.setBackgroundRes(R.id.pk10_ball_three_tv, getBgPk10(OpenCode[2]));
            helper.setBackgroundRes(R.id.pk10_ball_four_tv, getBgPk10(OpenCode[3]));
            helper.setBackgroundRes(R.id.pk10_ball_five_tv, getBgPk10(OpenCode[4]));
            helper.setBackgroundRes(R.id.pk10_ball_six_tv, getBgPk10(OpenCode[5]));
            helper.setBackgroundRes(R.id.pk10_ball_seven_tv, getBgPk10(OpenCode[6]));
            helper.setBackgroundRes(R.id.pk10_ball_eight_tv, getBgPk10(OpenCode[7]));
            helper.setBackgroundRes(R.id.pk10_ball_nine_tv, getBgPk10(OpenCode[8]));
            helper.setBackgroundRes(R.id.pk10_ball_ten_tv, getBgPk10(OpenCode[9]));
        }
    }

    private void setSSC(BaseViewHolder helper, HandicpWithOpening rcd) {
        // 这期开奖时间
        helper.setText(R.id.ssc_time, DateTool.convert2String(new Date(rcd.getOpenTime()), DateTool.FMT_TIME_3));

        if (rcd.getOpenCode() != null && 9 == rcd.getOpenCode().length()) {
            String[] OpenCode = rcd.getOpenCode().split(",");
            helper.setText(R.id.ssc_ball_one_tv, OpenCode[0]);
            helper.setText(R.id.ssc_ball_tow_tv, OpenCode[1]);
            helper.setText(R.id.ssc_ball_three_tv, OpenCode[2]);
            helper.setText(R.id.ssc_ball_four_tv, OpenCode[3]);
            helper.setText(R.id.ssc_ball_five_tv, OpenCode[4]);

            helper.setText(R.id.ssc_value_sum_tv, "" + SSCUtil.getHeZhi(OpenCode));

            if ("豹子".equals(SSCUtil.getZuLiuOrZUSan2(OpenCode))) {
                helper.setGone(R.id.ssc_lable_group_tv, false);
            } else {
                helper.setGone(R.id.ssc_lable_group_tv, true);
            }

            helper.setText(R.id.ssc_value_group_tv, SSCUtil.getZuLiuOrZUSan2(OpenCode));
        } else {
            helper.setText(R.id.ssc_ball_one_tv, "");
            helper.setText(R.id.ssc_ball_tow_tv, "");
            helper.setText(R.id.ssc_ball_three_tv, "");
            helper.setText(R.id.ssc_ball_four_tv, "");
            helper.setText(R.id.ssc_ball_five_tv, "");
            helper.setText(R.id.ssc_value_sum_tv, "");
            helper.setGone(R.id.ssc_lable_group_tv, false);
            helper.setText(R.id.ssc_value_group_tv, "");
        }

    }


    int getBgPk10(String code) {
        switch (code) {
            case "01":
                return R.mipmap.car_1;
            case "02":
                return R.mipmap.car_2;
            case "03":
                return R.mipmap.car_3;
            case "04":
                return R.mipmap.car_4;
            case "05":
                return R.mipmap.car_5;
            case "06":
                return R.mipmap.car_6;
            case "07":
                return R.mipmap.car_7;
            case "08":
                return R.mipmap.car_8;
            case "09":
                return R.mipmap.car_9;
            case "10":
                return R.mipmap.car_10;
            default:
                return 0;
        }
    }

    int getBgLHC(String code) {
        switch (LHCUtil.getBallColor(code)) {
            case "red":
                return R.drawable.shape_ball_red;
            case "blue":
                return R.drawable.shape_ball_blue;
            case "green":
                return R.drawable.shape_ball_green;
        }
        return 0;
    }

    int getBgK3(String code) {
        switch (code) {
            case "1":
                return R.mipmap.fast1;
            case "2":
                return R.mipmap.fast2;
            case "3":
                return R.mipmap.fast3;
            case "4":
                return R.mipmap.fast4;
            case "5":
                return R.mipmap.fast5;
            case "6":
                return R.mipmap.fast6;
            default:
                return 0;
        }
    }

    int getBgSfc(String code) {
        switch (code) {
            case "01":
                return R.mipmap.sfc1;
            case "02":
                return R.mipmap.sfc2;
            case "03":
                return R.mipmap.sfc3;
            case "04":
                return R.mipmap.sfc4;
            case "05":
                return R.mipmap.sfc5;
            case "06":
                return R.mipmap.sfc6;
            case "07":
                return R.mipmap.sfc7;
            case "08":
                return R.mipmap.sfc8;
            case "09":
                return R.mipmap.sfc9;
            case "10":
                return R.mipmap.sfc10;
            case "11":
                return R.mipmap.sfc11;
            case "12":
                return R.mipmap.sfc12;
            case "13":
                return R.mipmap.sfc13;
            case "14":
                return R.mipmap.sfc14;
            case "15":
                return R.mipmap.sfc15;
            case "16":
                return R.mipmap.sfc16;
            case "17":
                return R.mipmap.sfc17;
            case "18":
                return R.mipmap.sfc18;
            case "19":
                return R.mipmap.sfc19;
            case "20":
                return R.mipmap.sfc20;
            default:
                return 0;
        }
    }






}
