package com.dawoo.lotterybox.adapter.HallAdapter;


import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;

/**
 * 父布局Item点击监听接口
 */

public interface ItemClickListener {
    /**
     * 展开子Item
     * @param bean
     */
    void onExpandChildren(TypeAndLotteryBean bean, int posion);

    /**
     * 隐藏子Item
     *
     */
    void onHideChildren();
}
