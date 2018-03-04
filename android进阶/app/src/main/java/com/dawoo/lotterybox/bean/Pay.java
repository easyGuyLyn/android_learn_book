package com.dawoo.lotterybox.bean;

/**
 * Created by jack on 18-2-9.
 */

public class Pay {
    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public String getPaydescription() {
        return paydescription;
    }

    public void setPaydescription(String paydescription) {
        this.paydescription = paydescription;
    }

    public int getPayimage() {
        return payimage;
    }

    public void setPayimage(int payimage) {
        this.payimage = payimage;
    }

    private String payname;
    private String paydescription;
    private int payimage;
}
