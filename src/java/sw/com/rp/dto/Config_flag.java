/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dto;

import java.io.Serializable;

/**
 *
 * @author msaini
 */
public class Config_flag implements Serializable {

    private boolean EMAIL_QUES_LINK;
    private boolean GETSSO;
    private boolean GREY_UID;
    private boolean Password_SYNC;
    private String HELP_CONTACT;
    private boolean HELP_DESK;
    private String HELP_EMAIL;
    private String HELP_PAGE;
    private String HELP_PHONE;
    private boolean HIDE_EMAIL;
    private boolean HIDE_NAME;
    private boolean JAVA_ENCRYPTION;
    private boolean HIDE_CONFIG;
    private boolean HIDE_RESET_PASS;
    private boolean HIDE_SUPPORT;
    private boolean HIDE_USER_REG;
    private boolean HIDE_HOME;
    private String PASS_LOGIC;
    private boolean QUES_ANS_WORKFLOW;
    private String REQPARAM;
    private String RP_URL;
    private boolean SSO;
    private boolean MULTI_HOST;
    private boolean ResetPOPUP;
    private boolean SAP_SEND_PASSWORD_BY_EMAIL;
    private String SUPPORT_EMAIL;

    public Config_flag() {
        EMAIL_QUES_LINK = false;
        GETSSO = false;
        GREY_UID = false;
        HELP_CONTACT = "";
        HELP_DESK = false;
        HELP_EMAIL = "";
        HELP_PAGE = "";
        HELP_PHONE = "";
        HIDE_EMAIL = false;
        HIDE_NAME = false;
        JAVA_ENCRYPTION = false;
        HIDE_CONFIG = false;
        HIDE_RESET_PASS = false;
        HIDE_SUPPORT = false;
        HIDE_USER_REG = false;
        HIDE_HOME = false;
        PASS_LOGIC = "";
        QUES_ANS_WORKFLOW = false;
        REQPARAM = "";
        RP_URL = "";
        SSO = false;
        SUPPORT_EMAIL = "";
        MULTI_HOST = false;
        Password_SYNC = false;
        SAP_SEND_PASSWORD_BY_EMAIL = false;
    }

    public boolean isSAP_SEND_PASSWORD_BY_EMAIL() {
        return SAP_SEND_PASSWORD_BY_EMAIL;
    }

    public void setSAP_SEND_PASSWORD_BY_EMAIL(boolean SAP_SEND_PASSWORD_BY_EMAIL) {
        this.SAP_SEND_PASSWORD_BY_EMAIL = SAP_SEND_PASSWORD_BY_EMAIL;
    }

    public boolean isPassword_SYNC() {
        return Password_SYNC;
    }

    public void setPassword_SYNC(boolean Password_SYNC) {
        this.Password_SYNC = Password_SYNC;
    }

    public boolean isResetPOPUP() {
        return ResetPOPUP;
    }

    public void setResetPOPUP(boolean ResetPOPUP) {
        this.ResetPOPUP = ResetPOPUP;
    }

    public boolean isMULTI_HOST() {
        return MULTI_HOST;
    }

    public void setMULTI_HOST(boolean MULTI_HOST) {
        this.MULTI_HOST = MULTI_HOST;
    }

    public boolean isEMAIL_QUES_LINK() {
        return EMAIL_QUES_LINK;
    }

    public void setEMAIL_QUES_LINK(boolean EMAIL_QUES_LINK) {
        this.EMAIL_QUES_LINK = EMAIL_QUES_LINK;
    }

    public boolean isGETSSO() {
        return GETSSO;
    }

    public void setGETSSO(boolean GETSSO) {
        this.GETSSO = GETSSO;
    }

    public boolean isGREY_UID() {
        return GREY_UID;
    }

    public void setGREY_UID(boolean GREY_UID) {
        this.GREY_UID = GREY_UID;
    }

    public String getHELP_CONTACT() {
        return HELP_CONTACT;
    }

    public void setHELP_CONTACT(String HELP_CONTACT) {
        this.HELP_CONTACT = HELP_CONTACT;
    }

    public boolean isHELP_DESK() {
        return HELP_DESK;
    }

    public void setHELP_DESK(boolean HELP_DESK) {
        this.HELP_DESK = HELP_DESK;
    }

    public String getHELP_EMAIL() {
        return HELP_EMAIL;
    }

    public void setHELP_EMAIL(String HELP_EMAIL) {
        this.HELP_EMAIL = HELP_EMAIL;
    }

    public String getHELP_PAGE() {
        return HELP_PAGE;
    }

    public void setHELP_PAGE(String HELP_PAGE) {
        this.HELP_PAGE = HELP_PAGE;
    }

    public String getHELP_PHONE() {
        return HELP_PHONE;
    }

    public void setHELP_PHONE(String HELP_PHONE) {
        this.HELP_PHONE = HELP_PHONE;
    }

    public boolean isHIDE_EMAIL() {
        return HIDE_EMAIL;
    }

    public void setHIDE_EMAIL(boolean HIDE_EMAIL) {
        this.HIDE_EMAIL = HIDE_EMAIL;
    }

    public boolean isHIDE_NAME() {
        return HIDE_NAME;
    }

    public void setHIDE_NAME(boolean HIDE_NAME) {
        this.HIDE_NAME = HIDE_NAME;
    }

    public boolean isJAVA_ENCRYPTION() {
        return JAVA_ENCRYPTION;
    }

    public void setJAVA_ENCRYPTION(boolean JAVA_ENCRYPTION) {
        this.JAVA_ENCRYPTION = JAVA_ENCRYPTION;
    }

    public boolean isHIDE_CONFIG() {
        return HIDE_CONFIG;
    }

    public void setHIDE_CONFIG(boolean HIDE_CONFIG) {
        this.HIDE_CONFIG = HIDE_CONFIG;
    }

    public boolean isHIDE_HOME() {
        return HIDE_HOME;
    }

    public void setHIDE_HOME(boolean HIDE_HOME) {
        this.HIDE_HOME = HIDE_HOME;
    }

    public boolean isHIDE_RESET_PASS() {
        return HIDE_RESET_PASS;
    }

    public void setHIDE_RESET_PASS(boolean HIDE_RESET_PASS) {
        this.HIDE_RESET_PASS = HIDE_RESET_PASS;
    }

    public boolean isHIDE_SUPPORT() {
        return HIDE_SUPPORT;
    }

    public void setHIDE_SUPPORT(boolean HIDE_SUPPORT) {
        this.HIDE_SUPPORT = HIDE_SUPPORT;
    }

    public boolean isHIDE_USER_REG() {
        return HIDE_USER_REG;
    }

    public void setHIDE_USER_REG(boolean HIDE_USER_REG) {
        this.HIDE_USER_REG = HIDE_USER_REG;
    }

    public String getPASS_LOGIC() {
        return PASS_LOGIC;
    }

    public void setPASS_LOGIC(String PASS_LOGIC) {
        this.PASS_LOGIC = PASS_LOGIC;
    }

    public boolean isQUES_ANS_WORKFLOW() {
        return QUES_ANS_WORKFLOW;
    }

    public void setQUES_ANS_WORKFLOW(boolean QUES_ANS_WORKFLOW) {
        this.QUES_ANS_WORKFLOW = QUES_ANS_WORKFLOW;
    }

    public String getREQPARAM() {
        return REQPARAM;
    }

    public void setREQPARAM(String REQPARAM) {
        this.REQPARAM = REQPARAM;
    }

    public String getRP_URL() {
        return RP_URL;
    }

    public void setRP_URL(String RP_URL) {
        this.RP_URL = RP_URL;
    }

    public boolean isSSO() {
        return SSO;
    }

    public void setSSO(boolean SSO) {
        this.SSO = SSO;
    }

    public String getSUPPORT_EMAIL() {
        return SUPPORT_EMAIL;
    }

    public void setSUPPORT_EMAIL(String SUPPORT_EMAIL) {
        this.SUPPORT_EMAIL = SUPPORT_EMAIL;
    }
}
