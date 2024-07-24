/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.String;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SAPConnection;
import sw.com.rp.dto.ResetPassRequest;

/**
 *
 * @author msaini
 */
public class ResetPass {

    JCoFunction function;
    JCoTable codes;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(ResetPass.class.getName());

    public ResetPass() {
        this.function = null;
        this.codes = null;
        this.connection = null;
    }

    public ResetPassRequest resetPassword(ResetPassRequest rpResponse, String _system, HttpSession session) throws Exception, Throwable {
        function = null;
        connection = null;
        boolean etFlag = true;
        ResetPassRequest repr = new ResetPassRequest();
        String language = "EN";
        String lang = (String) session.getAttribute("LANG");
        if (lang != null) {
            if (lang.equalsIgnoreCase("English")) {
                language = "EN";
            } else if (lang.equalsIgnoreCase("German")) {
                language = "DE";
            }
        }
        connection = new SAPConnection(language);
        try {
            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));

            function = connection.getFunction("/PSYNG/RP_SET_PRODUCTIVE_PASS");

            JCoParameterList inputList = function.getImportParameterList();
            logger.error("BAPI /PSYNG/RP_SET_PRODUCTIVE_PASS : reprID " + rpResponse.getUserID());
//            rpResponse.setreprID(rpRequest.getreprID());
            inputList.setValue("I_BNAME", rpResponse.getUserID());
            inputList.setValue("I_CUR_PASSWORD", rpResponse.getCurrentPass());//passcode from session
            inputList.setValue("I_NEW_PASSWORD", rpResponse.getNewPass());
            inputList.setValue("I_CHARURL", rpResponse.getCharUrl());
            
            //inputList.setValue("I_SYSID", rpResponse.getSapSystem());
            inputList.setValue("I_SYNC_PASS_ALL_SYS", rpResponse.getPassSync());
            JCoTable combdataTable = function.getTableParameterList().getTable("IT_SYSTEMS");

            combdataTable.appendRow();
            combdataTable.setValue("SAPSYSTEM", rpResponse.getSapSystem());
            logger.error("Executing bapi /PSYNG/RP_SET_PRODUCTIVE_PASS");
            connection.execute(function);

            connection.release();
            connection = null;
            JCoTable protb = null;

            protb = function.getTableParameterList().getTable("ET_RETURN");
            String msg[] = new String[protb.getNumRows()];
            // boolean etFlag = true;
            logger.info("ET_RETURN");
            ArrayList<String> sysErrMsgList = new ArrayList<String>();//B13115 fix
            if (protb.getNumRows() != 0) {
                for (int i = 0; i < protb.getNumRows(); i++) {
                    protb.setRow(i);
                    String text = "[ERROR] : "+protb.getString("MESSAGE");
                    logger.info("TYPE:: "+protb.getString("TYPE")+", MESSAGE:: "+protb.getString("MESSAGE"));
                    msg[i] = text;
                    //String[] sys = text.split("-");
                    sysErrMsgList.add(text);
                    etFlag = false;
                }
            }
            String finalmsg = "";
            for (int j = 0; j < msg.length; j++) {
                if (msg[j] != null) {
                    finalmsg = finalmsg + msg[j] + "\n";
                }
            }
            String finalmsgNew = "";
            if (RPConfig.getConfig_flag().isPassword_SYNC()) {
                protb = function.getTableParameterList().getTable("IT_SYSTEMS");
                logger.info("IT_SYSTEMS");
                
                String msgNew[] = new String[protb.getNumRows()];
                if (protb.getNumRows() == 0) {
                    logger.error("table is empty");
                    throw new Exception("table is empty");
                } else {

                    for (int i = 0; i < protb.getNumRows(); i++) {
                        protb.setRow(i);
                        String sys = protb.getString("SAPSYSTEM");
                        logger.info("SAPSYSTEM:: " +sys);
                        boolean sysErrMsgFound = false;
                        for (String sysErrMsg : sysErrMsgList) {
                            if (sysErrMsg.contains(sys)) {
                                sysErrMsgFound = true;
                                break;

                            }
                        }
                        if (!sysErrMsgFound) {
                            String text = getTranslatedText("PassResetmsg2", session);
                            if (text == null || text.trim().length() == 0) {
                                text = "Your password has been reset successfully";
                            }
                            msgNew[i] = sys + " : " + text;
                        }
                    }
                }
                finalmsgNew = "";
                for (int j = 0; j < msgNew.length; j++) {
                    if (msgNew[j] != null) {
                        finalmsgNew = finalmsgNew + msgNew[j] + "\n";
                    }
                }
                repr.setMessage(finalmsgNew);
            }

            String lastFinalMsg = finalmsg + "\n" + finalmsgNew;
            repr.setMessage(lastFinalMsg);
        } catch (Throwable e) {
            connection.release();
            connection = null;
            String s = e.toString().trim();
            logger.error("Message From SAP:" + e.toString());
            if (s.contains("CHANGE_NOT_ALLOWED")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg5", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Password change is not allowed";
                }
                repr.setMessage(text);
                logger.error("Password change is not allowed");
            } else if (s.contains("PASSWORD_NOT_ALLOWED")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg6", session);
                if (text == null || text.trim().length() == 0) {
                    text = "New password is not allowed";
                }
                repr.setMessage(text);
                logger.error("New password is not allowed");
            } else if (s.contains("INTERNAL_ERROR")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg7", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Something went wrong in bapi";
                }
                repr.setMessage(text);
                logger.error("Something went wrong in bapi");
            } else if (s.contains("CANCELED_BY_USER")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg8", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Password change dialog is canceled";
                }
                repr.setMessage(text);
                logger.error("Password change dialog is canceled");
            } else if (s.contains("SYSTEM_NOT_CONFIGURED")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg9", session);
                if (text == null || text.trim().length() == 0) {
                    text = "System is not configured properly";
                }
                repr.setMessage(text);
                logger.error("System is not configured properly");
            } else if (s.contains("CUA_NOT_CONFIGURED")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg10", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please Identify your System with CUA Or Non CUA , Contact Your Administrator !";
                }
                repr.setMessage(text);
                logger.error("Please Identify your System with CUA Or Non CUA , Contact Your Administrator !");
            } else if (s.contains("Name or password is incorrect. Please re-enter")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg11", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Current password is incorrect, Please re-enter";
                }
                repr.setMessage(text);
                logger.error("Current password is incorrect, Please re-enter");

            } else if (s.contains("Choose a password that is different from your last")) {
                //multilanguage code
                String text = getTranslatedText("PassResetmsg12", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please choose a password that is different from your last 5 password";
                }
                repr.setMessage(text);
                logger.error("Please choose a password that is different from your last 5 password");

            } else {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                repr.setMessage(s);
            }

            connection = null;
            function = null;
            repr.setSuccess(false);
            return repr;
        }
        repr.setSuccess(true);
        if (etFlag) {
            if (!RPConfig.getConfig_flag().isPassword_SYNC()) {
                String text = getTranslatedText("PassResetmsg2", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Your password has been reset successfully";
                }
                repr.setMessage(text);
            }
        }else{
            repr.setSuccess(false);
        }
        return repr;
//        return rpResponse;
    }

    public String getTranslatedText(String textID, HttpSession session) {
        logger.info(" getTranslatedText() method starts..");
        if (textID != null && textID != "") {
            String selectedLang = "";
            try {
                selectedLang = session.getAttribute("LANG").toString();
            } catch (Exception e) {
                selectedLang = "English";
            }

            logger.info("selected language" + selectedLang);
            if (selectedLang != null && selectedLang.length() > 0) {
                String text = "";
                try {
                    text = RPConfig.getResetPasswordResourcesLangMap().get(selectedLang).get(textID);
                } catch (Exception e) {
                    e.toString();
                    logger.error(e.toString());
                    logger.info("SupportPage Error in translation  for " + textID);
                    return null;
                }
                if (text != null && text.trim().length() > 0) {
                    logger.info(" getTranslatedText() method ends..");
                    return text;
                }
            } else {
                logger.info("SupportPage Error in translation  for " + textID);
                return null;
            }
        }
        logger.info("SupportPage Error in translation  for " + textID);
        return null;
    }

    public String getPasswordLength(String _system, HttpSession session) throws Exception, Throwable {
        String passLength = "";
        function = null;
        connection = null;
        String language = "EN";
        String lang = (String) session.getAttribute("LANG");
        connection = new SAPConnection(language);
        try {
            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));

            function = connection.getFunction("/PSYNG/RP_SET_PASSWORD_LEN");

            JCoParameterList inputList = function.getImportParameterList();
            //  inputList.setValue("UNAME", rpResponse.getUserID());
            connection.execute(function);
            JCoParameterList export = function.getExportParameterList();
            passLength = export.getString("E_MAX_PASS_LEN");

        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
//            quesAnsList = null;
            function = null;
            connection = null;
            throw new Exception(ex.getMessage());
        }

        return passLength;
    }
}
