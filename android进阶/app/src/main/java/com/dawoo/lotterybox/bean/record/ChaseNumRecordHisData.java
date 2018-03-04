package com.dawoo.lotterybox.bean.record;

import java.util.List;

/**
 * Created by benson on 18-2-18.
 */

public class ChaseNumRecordHisData {
    private int error;
    private String code;
    private String message;
    private List<ChaseNumRecordHis> data;
    private Count extend;

    public Count getExtend() {
        return extend;
    }

    public void setExtend(Count extend) {
        this.extend = extend;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChaseNumRecordHis> getData() {
        return data;
    }

    public void setData(List<ChaseNumRecordHis> data) {
        this.data = data;
    }


    public static class Count {
        private int totalCount;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

}
