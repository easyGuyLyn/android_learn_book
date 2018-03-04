package com.dawoo.lotterybox.util.lottery;

/**
 * 快三
 * Created by benson on 18-2-15.
 */

public class K3Util {
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
     * 三不同号 二同号  豹子
     *
     * @return
     */
    public static String getK3SanBuTongHao(String[] arr) {
        if (arr[0] != arr[1] && arr[0] != arr[2] && arr[1] != arr[2]) {
            return "三不同号";
        } else if (arr[0] == arr[1] && arr[0] == arr[2]) {
            return "豹子";
        }
        return "二同号";
    }
}
