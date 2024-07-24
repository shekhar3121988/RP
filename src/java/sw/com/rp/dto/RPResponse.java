/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author msaini
 */
public class RPResponse implements Serializable {

    private String userID;
    private String message;
    private String password;
    private String passCode;
    private String sapSystem;
    private String lang;
    private String passSync;
    private int passLogic;
    private boolean locked;
    private boolean infoFlag;
    private boolean helpDesk;
    private String helpDeskLabels[] = new String[5];
    private boolean showQA;
    private boolean passReset;
    private boolean success;
    private boolean passByUser;
    private boolean email;
    private boolean userlocked;
    private String xTID= "";
    private String userIDOther;

    public RPResponse() {
        this.userID = "";
        this.message = "";
        this.sapSystem = "";
        this.password = "";
        this.passCode = "";
        this.lang = "";
        this.passSync = "";
        this.passLogic = 0;
        this.locked = false;
        this.success = false;
        this.passReset = false;
        this.helpDesk = false;
        this.email = false;
        this.userlocked = false;
        this.passByUser = false;
        this.infoFlag = false;
        this.userIDOther = "";
    }

    public String getPassSync() {
        return passSync;
    }

    public void setPassSync(String passSync) {
        this.passSync = passSync;
    }

    public boolean isUserlocked() {
        return userlocked;
    }

    public void setUserlocked(boolean userlocked) {
        this.userlocked = userlocked;
    }

    public RPResponse(String userID, String message, boolean locked, boolean success) {
        this.userID = userID;
        this.message = message;
        this.locked = locked;
        this.success = success;
    }

    public boolean isPassByUser() {
        return passByUser;
    }

    public void setPassByUser(boolean passByUser) {
        this.passByUser = passByUser;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPassReset() {
        return passReset;
    }

    public void setPassReset(boolean passReset) {
        this.passReset = passReset;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSapSystem() {
        return sapSystem;
    }

    public void setSapSystem(String sapSystem) {
        this.sapSystem = sapSystem;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public int getPassLogic() {
        return passLogic;
    }

    public void setPassLogic(int passLogic) {
        this.passLogic = passLogic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShowQA() {
        return showQA;
    }

    public void setShowQA(boolean showQA) {
        this.showQA = showQA;
    }

    public boolean isHelpDesk() {
        return helpDesk;
    }

    public void setHelpDesk(boolean helpDesk) {
        this.helpDesk = helpDesk;
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

    public boolean isInfoFlag() {
        return infoFlag;
    }

    public void setInfoFlag(boolean infoFlag) {
        this.infoFlag = infoFlag;
    }

    public String[] getHelpDeskLabels() {
        return helpDeskLabels;
    }

    public void setHelpDeskLabels(String[] helpDeskLabels) {
        this.helpDeskLabels = helpDeskLabels;
    }

    public String getxTID() {
        return xTID;
    }

    public void setxTID(String xTID) {
        this.xTID = xTID;
    }

    public String getUserIDOther() {
        return userIDOther;
    }

    public void setUserIDOther(String userIDOther) {
        this.userIDOther = userIDOther;
    }
    
    
}
