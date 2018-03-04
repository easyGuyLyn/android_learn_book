package com.dawoo.gamebox.been;

import java.util.List;

/**
 * Created by lenovo on 2017/3/7.
 */

public class BalanceBean {

    /**
     * apis : [{"apiName":"BBIN游戏大厅","balance":1002,"apiId":"10","status":"normal"},{"apiName":"KG彩票","balance":120,"apiId":"2","status":"normal"}]
     * playerWallet : 9,124.64
     * playerAssets : 11,885.14
     * currSign : ￥
     * username : bi****ll
     */

    private String playerWallet;
    private String playerAssets;
    private String currSign;
    private String username;
    private List<ApisBean> apis;

    public String getPlayerWallet() {
        return playerWallet;
    }

    public void setPlayerWallet(String playerWallet) {
        this.playerWallet = playerWallet;
    }

    public String getPlayerAssets() {
        return playerAssets;
    }

    public void setPlayerAssets(String playerAssets) {
        this.playerAssets = playerAssets;
    }

    public String getCurrSign() {
        return currSign;
    }

    public void setCurrSign(String currSign) {
        this.currSign = currSign;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ApisBean> getApis() {
        return apis;
    }

    public void setApis(List<ApisBean> apis) {
        this.apis = apis;
    }

    public static class ApisBean {
        /**
         * apiName : BBIN游戏大厅
         * balance : 1002.0
         * apiId : 10
         * status : normal
         */

        private String apiName;
        private double balance;
        private String apiId;
        private String status;

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getApiId() {
            return apiId;
        }

        public void setApiId(String apiId) {
            this.apiId = apiId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
