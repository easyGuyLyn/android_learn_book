package com.dawoo.gamebox.bean;

import java.util.List;

/**
 * Created by jack on 18-2-13.
 */

public class UserPlayerRecommend {

    /**
     * reward : 1
     * code : /register.html?c=ELP0GAHECA2CL
     * theWay : 100
     * money : 10
     * isBonus : true
     * witchWithdraw : 100
     * sign : ￥
     * recommend : {"single":0,"bonus":0,"count":0,"user":0}
     * gradientTempArrayList : [{"id":null,"playerNum":1,"proportion":10},{"id":null,"playerNum":2,"proportion":20},{"id":null,"playerNum":3,"proportion":30},{"id":null,"playerNum":4,"proportion":40},{"id":null,"playerNum":5,"proportion":50}]
     * command : []
     * activityRules : 这是什么鬼？
     */

    private String reward;
    private String code;
    private String theWay;
    private String money;
    private boolean isBonus;
    private String witchWithdraw;
    private String sign;
    private RecommendBean recommend;
    private String activityRules;
    private List<GradientTempArrayListBean> gradientTempArrayList;
    private List<?> command;

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTheWay() {
        return theWay;
    }

    public void setTheWay(String theWay) {
        this.theWay = theWay;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public boolean isIsBonus() {
        return isBonus;
    }

    public void setIsBonus(boolean isBonus) {
        this.isBonus = isBonus;
    }

    public String getWitchWithdraw() {
        return witchWithdraw;
    }

    public void setWitchWithdraw(String witchWithdraw) {
        this.witchWithdraw = witchWithdraw;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public RecommendBean getRecommend() {
        return recommend;
    }

    public void setRecommend(RecommendBean recommend) {
        this.recommend = recommend;
    }

    public String getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(String activityRules) {
        this.activityRules = activityRules;
    }

    public List<GradientTempArrayListBean> getGradientTempArrayList() {
        return gradientTempArrayList;
    }

    public void setGradientTempArrayList(List<GradientTempArrayListBean> gradientTempArrayList) {
        this.gradientTempArrayList = gradientTempArrayList;
    }

    public List<?> getCommand() {
        return command;
    }

    public void setCommand(List<?> command) {
        this.command = command;
    }

    public static class RecommendBean {
        /**
         * single : 0
         * bonus : 0
         * count : 0
         * user : 0
         */

        private int single;
        private int bonus;
        private int count;
        private int user;

        public int getSingle() {
            return single;
        }

        public void setSingle(int single) {
            this.single = single;
        }

        public int getBonus() {
            return bonus;
        }

        public void setBonus(int bonus) {
            this.bonus = bonus;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }
    }

    public static class GradientTempArrayListBean {
        /**
         * id : null
         * playerNum : 1
         * proportion : 10
         */

        private Object id;
        private int playerNum;
        private int proportion;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public int getPlayerNum() {
            return playerNum;
        }

        public void setPlayerNum(int playerNum) {
            this.playerNum = playerNum;
        }

        public int getProportion() {
            return proportion;
        }

        public void setProportion(int proportion) {
            this.proportion = proportion;
        }
    }
}
