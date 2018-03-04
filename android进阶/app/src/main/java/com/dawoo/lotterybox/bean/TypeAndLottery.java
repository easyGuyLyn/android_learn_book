package com.dawoo.lotterybox.bean;

import java.util.List;

/**
 * Created by b on 18-2-7.
 */

public class TypeAndLottery {

    /**
     * error : 0
     * code : null
     * message : null
     * data : [{"typeCode":"pk10","typeName":"PK10","lotteries":[{"code":"xyft","name":"幸运飞艇","status":"1"},{"code":"bjpk10","name":"北京PK10","status":"1"}]}]
     * extend : null
     */

    private int error;
    private int code;
    private String message;
    private Object extend;
    private List<DataBean_> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }

    public List<DataBean_> getData() {
        return data;
    }

    public void setData(List<DataBean_> data) {
        this.data = data;
    }

    public static class DataBean_ {

        public static final int PARENT_ITEM = 0;//父布局
        public static final int CHILD_ITEM = 1;//子布局
        private int type;// 显示类型
        private boolean isExpand;// 是否展开
        /**
         * typeCode : pk10
         * typeName : PK10
         * lotteries : [{"code":"xyft","name":"幸运飞艇","status":"1"},{"code":"bjpk10","name":"北京PK10","status":"1"}]
         */

        private String typeCode;
        private String typeName;
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

        public List<LotteriesBean> getLotteries() {
            return lotteries;
        }

        public void setLotteries(List<LotteriesBean> lotteries) {
            this.lotteries = lotteries;
        }

        public static class LotteriesBean {
            /**
             * code : xyft
             * name : 幸运飞艇
             * status : 1
             */

            private String code;
            private String name;
            private String status;

            public LotteriesBean(String code, String name , String status){
                this.code = code;
                this.name = name;
                this.status = status;
            };

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
}
