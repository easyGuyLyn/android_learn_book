package com.dawoo.lotterybox.net;

import android.content.Context;

import com.dawoo.lotterybox.BoxApplication;

/**
 * Created by benson on 17-12-21.
 */

public class ApiException extends RuntimeException {

    public static final String NO_DATA = "ED001";//查无数据

    public ApiException(String resultCode, String msg) {
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
    private static String getApiExceptionMessage(String code, String msg) {
        Context context = BoxApplication.getContext();
        switch (code) {
            case NO_DATA:
              //  ActivityUtil.gotoLogin();
                return msg;
        }
        return msg;
    }

}
