package com.dawoo.lotterybox.util.lottery;

import com.dawoo.lotterybox.bean.lottery.LotteryEnum;

/**
 * 彩票工具类
 * Created by benson on 18-2-13.
 */

public class LotteryUtil {

    /**
     *根据彩种代码获取彩种名称
     * @param code
     * @return
     */
    public static String getLotteryNameByCode(String code) {
        if (LotteryEnum.JSK3.getCode().equals(code)) {
            return LotteryEnum.JSK3.getTrans();

        } else if (LotteryEnum.AHK3.getCode().equals(code)) {
            return LotteryEnum.AHK3.getTrans();

        } else if (LotteryEnum.HBK3.getCode().equals(code)) {
            return LotteryEnum.HBK3.getTrans();

        } else if (LotteryEnum.GXK3.getCode().equals(code)) {
            return LotteryEnum.GXK3.getTrans();

        } else if (LotteryEnum.HKLHC.getCode().equals(code)) {
            return LotteryEnum.HKLHC.getTrans();

        } else if (LotteryEnum.XJSSC.getCode().equals(code)) {
            return LotteryEnum.XJSSC.getTrans();

        } else if (LotteryEnum.TJSSC.getCode().equals(code)) {
            return LotteryEnum.TJSSC.getTrans();

        } else if (LotteryEnum.FFSSC.getCode().equals(code)) {
            return LotteryEnum.FFSSC.getTrans();

        } else if (LotteryEnum.EFSSC.getCode().equals(code)) {
            return LotteryEnum.EFSSC.getTrans();

        } else if (LotteryEnum.SFSSC.getCode().equals(code)) {
            return LotteryEnum.SFSSC.getTrans();

        } else if (LotteryEnum.WFSSC.getCode().equals(code)) {
            return LotteryEnum.WFSSC.getTrans();

        } else if (LotteryEnum.CQSSC.getCode().equals(code)) {
            return LotteryEnum.CQSSC.getTrans();

        } else if (LotteryEnum.BJPK10.getCode().equals(code)) {
            return LotteryEnum.BJPK10.getTrans();

        } else if (LotteryEnum.XYFT.getCode().equals(code)) {
            return LotteryEnum.XYFT.getTrans();

        } else if (LotteryEnum.JSPK10.getCode().equals(code)) {
            return LotteryEnum.JSPK10.getTrans();

        } else if (LotteryEnum.CQXYNC.getCode().equals(code)) {
            return LotteryEnum.CQXYNC.getTrans();

        } else if (LotteryEnum.GDKL10.getCode().equals(code)) {
            return LotteryEnum.GDKL10.getTrans();

        } else if (LotteryEnum.BJKL8.getCode().equals(code)) {
            return LotteryEnum.BJKL8.getTrans();

        } else if (LotteryEnum.XY28.getCode().equals(code)) {
            return LotteryEnum.XY28.getTrans();

        } else if (LotteryEnum.FC3D.getCode().equals(code)) {
            return LotteryEnum.FC3D.getTrans();

        } else if (LotteryEnum.TCPL3.getCode().equals(code)) {
            return LotteryEnum.TCPL3.getTrans();

        }
        return "";
    }
}
