package com.dawoo.gamebox.tool;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by fei on 17-3-27.
 */
public class ResourceTool {
    /**
     * 根据图片的名称获取对应的资源id
     * @param resourceName
     * @return
     */
    public static int getDrawResID(Context context, String resourceName) {
        Resources res = context.getResources();
        return res.getIdentifier(resourceName, "drawable", context.getPackageName());
    }

    public static int getMipMapResId(Context context, String resourceName) {
        Resources res = context.getResources();
        return res.getIdentifier(resourceName, "mipmap", context.getPackageName());
    }
}
