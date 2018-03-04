package com.dawoo.lotterybox.bean.lottery;

import java.util.List;

/**
 * Created by b on 18-2-25.
 */

public class TypeAndLotteryBean {

    public static final int PARENT_ITEM = 0;//父布局
    public static final int CHILD_ITEM = 1;//子布局
    private int type;// 显示类型
    private boolean isExpand;// 是否展开

    /**
     * typeCode : ssc
     * typeName : 时时彩
     * frequency : high
     * unfold : false
     * lotteries : [{"code":"cqssc","name":"重庆时时彩","status":"1"},{"code":"tjssc","name":"天津时时彩","status":"1"},{"code":"xjssc","name":"新疆时时彩","status":"1"},{"code":"ffssc","name":"分分时时彩","status":"1"},{"code":"efssc","name":"二分时时彩","status":"1"},{"code":"sfssc","name":"三分时时彩","status":"1"},{"code":"wfssc","name":"五分时时彩","status":"1"}]
     */

    private String typeCode;
    private String typeName;
    private String frequency;
    private boolean unfold;
    private List<LotteriesBean> lotteries;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isUnfold() {
        return unfold;
    }

    public void setUnfold(boolean unfold) {
        this.unfold = unfold;
    }

    public List<LotteriesBean> getLotteries() {
        return lotteries;
    }

    public void setLotteries(List<LotteriesBean> lotteries) {
        this.lotteries = lotteries;
    }

    public static class LotteriesBean {
        /**
         * code : cqssc
         * name : 重庆时时彩
         * status : 1
         */

        private String code;
        private String name;
        private String status;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
