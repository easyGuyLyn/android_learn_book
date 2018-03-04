package com.dawoo.gamebox.been;

/**
 * Created by benson on 18-1-7.
 */

public class AutoRecovery {
    /**
     * resultStatus : PROCCESSING
     * orderId : 订单号码
     * resultCode : 2
     * msg : 回收太频繁，请休息片刻!
     */

    private String resultStatus;
    private String orderId;
    private int resultCode;
    private String msg;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
