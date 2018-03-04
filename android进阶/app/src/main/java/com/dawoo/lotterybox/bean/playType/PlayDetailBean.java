package com.dawoo.lotterybox.bean.playType;


import java.io.Serializable;

/**
 * Created by b on 18-2-19.
 * 官方玩法
 */

public class PlayDetailBean implements Serializable {

    private String type;//代表的玩法
    private String childType;//2级玩法
    private String kind;//代表所属的 位数
    private String num;//球或数字
    private String odd;//赔率
    private String lr;//右上角的数字
    private boolean selected;


    public String getChildType() {
        return childType;
    }

    public void setChildType(String childType) {
        this.childType = childType;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }

    public String getLr() {
        return lr;
    }

    public void setLr(String lr) {
        this.lr = lr;
    }
}
