package com.dawoo.lotterybox.util.lottery;

/**
 * 时时彩工具类
 * Created by benson on 18-2-13.
 */

public class SSCUtil {

    /**
     * 和值
     *
     * @param arr
     * @return
     */
    public static int getHeZhi(String[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += Integer.parseInt(arr[i]);
        }
        return sum;
    }

    /**
     * 组六 或者 组三
     *
     * @return
     */
    public static String getZuLiuOrZUSan(String[] arr) {
        if (arr[2] != arr[3] && arr[2] != arr[4] && arr[3] != arr[4]) {
            return "组六";
        } else if (arr[2] == arr[3] && arr[2] == arr[4]) {
            return "豹子";
        }
        return "组三";
    }

    /**
     * 组六 或者 组三
     *
     * @return
     */
    public static String getZuLiuOrZUSan2(String[] arr) {
        if (arr[2] != arr[3] && arr[2] != arr[4] && arr[3] != arr[4]) {
            return "六";
        } else if (arr[2] == arr[3] && arr[2] == arr[4]) {
            return "豹子";
        }
        return "三";
    }
}
