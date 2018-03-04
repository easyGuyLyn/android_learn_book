package com.dawoo.gamebox.been;

/**
 * Created by b on 18-1-25.
 */

public class VerifyRealNameBean {
    /**
     * playerAccount : 041071181111
     * nameSame : false
     * conflict : false
     */

    private String playerAccount;
    private boolean nameSame;
    private boolean conflict;

    public String getPlayerAccount() {
        return playerAccount;
    }

    public void setPlayerAccount(String playerAccount) {
        this.playerAccount = playerAccount;
    }

    public boolean isNameSame() {
        return nameSame;
    }

    public void setNameSame(boolean nameSame) {
        this.nameSame = nameSame;
    }

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }
}
