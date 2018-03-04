package com.dawoo.lotterybox.util.lottery;

import android.content.Context;

import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.lotterybox.BoxApplication;
import com.dawoo.lotterybox.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by benson on 18-2-15.
 */

public class LHCUtil {
    private static String[] mYears = BoxApplication.getContext().getResources().getStringArray(R.array.sheng_xiao_array);

    /**
     * 根据六合彩传入的球获取生肖
     *
     * @param code
     * @return
     */
    public static String getShengXiao(String code) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        try {
            String tYear = sdf.format(new Date());
            int tYearInt = Integer.parseInt(tYear);
            // 今年生肖
            String year = getYear(tYearInt);
            int tIndex = getIndex(year);

            // code 转型并根据新组成的生肖返回
            int index = (Integer.parseInt(code) + tIndex - 1) % 12;

            if (index > 0 && index < 12) {
                return mYears[index];
            } else {
                return mYears[0];
            }

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 根据传入的年获取生肖
     *
     * @param year
     * @return
     */
    private static String getYear(Integer year) {
        String[] years = new String[]{
                "鼠", "牛", "虎", "兔",
                "龙", "蛇", "马", "羊",
                "猴", "鸡", "狗", "猪"
        };
        if (year < 1900) {
            return "未知";
        }

        Integer start = 1900;
        return years[(year - start) % years.length];
    }


    /**
     * 根据传入的生肖获取下标
     *
     * @param year
     * @return
     */
    private static int getIndex(String year) {
        for (int i = 0; i < mYears.length; i++) {
            if (mYears[i].equals(year)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 获取红蓝绿波
     */
    public static String getBallColor(String code) {
        int codeInt = Integer.parseInt(code);
        int[] red = new int[]{1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46};
        int[] blue = new int[]{3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48};
        int[] green = new int[]{5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49};
        for (int i = 0; i < red.length; i++) {
            if (codeInt == red[i]) {
                return "red";
            } else if (codeInt == blue[i]) {
                return "blue";
            } else if (codeInt == green[i]) {
                return "green";
            }
        }
        return "";
    }


}
