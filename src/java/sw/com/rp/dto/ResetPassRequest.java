/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author msaini
 */
public class ResetPassRequest implements Serializable {

    private String userID;
    private String message;
    private String password;
    private String currentPass;
    private String newPass;
    private String confirmPass;
    private String sapSystem;
    private String lang;
    private int passLogic;
    private boolean locked;
    private boolean helpDesk;
    private boolean showQA;
    private boolean passReset;
    private boolean success;
    private boolean email;
    private String passSync;
    private String charUrl;

    public String getPassSync() {
        return passSync;
    }

    public void setPassSync(String passSync) {
        this.passSync = passSync;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isHelpDesk() {
        return helpDesk;
    }

    public void setHelpDesk(boolean helpDesk) {
        this.helpDesk = helpDesk;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public int getPassLogic() {
        return passLogic;
    }

    public void setPassLogic(int passLogic) {
        this.passLogic = passLogic;
    }

    public boolean isPassReset() {
        return passReset;
    }

    public void setPassReset(boolean passReset) {
        this.passReset = passReset;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSapSystem() {
        return sapSystem;
    }

    public void setSapSystem(String sapSystem) {
        this.sapSystem = sapSystem;
    }

    public boolean isShowQA() {
        return showQA;
    }

    public void setShowQA(boolean showQA) {
        this.showQA = showQA;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCharUrl() {
        return charUrl;
    }

    public void setCharUrl(String charUrl) {
        this.charUrl = charUrl;
    }
    
    
}
