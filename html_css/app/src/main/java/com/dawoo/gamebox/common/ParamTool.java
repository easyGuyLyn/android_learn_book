package com.dawoo.gamebox.common;

import android.content.Context;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.enums.SiteTypeEnum;

/**
 * 常用参数工具类
 * Created by fei on 2017/8/31.
 */
public class ParamTool {
    public static boolean isLotterySite(Context context) {
        return SiteTypeEnum.LOTTERY.getCode().equals(context.getString(R.string.site_type));
    }
}
