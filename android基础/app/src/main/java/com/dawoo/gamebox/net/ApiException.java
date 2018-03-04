package com.dawoo.gamebox.net;

import android.content.Context;

import com.dawoo.gamebox.BoxApplication;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.util.ActivityUtil;
import com.hwangjr.rxbus.RxBus;

/**
 * Created by benson on 17-12-21.
 */

public class ApiException extends RuntimeException {

    public static final int UNLOGIN = 1001;//您还没有登录

    public ApiException(int resultCode, String msg) {
        this(getApiExceptionMessage(resultCode, msg));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code, String msg) {
        switch (code) {
            case UNLOGIN:
                RxBus.get().post(ConstantValue.EVENT_TYPE_LOGOUT, "logout");
                DataCenter.getInstance().setLogin(false);
                ActivityUtil.gotoLogin();
                return msg;
        }
        return msg;
    }

}
