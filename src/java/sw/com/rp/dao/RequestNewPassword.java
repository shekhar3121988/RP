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
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SAPConnection;
import sw.com.rp.dto.MultipleSystemDTO;
import sw.com.rp.dto.RPResponse;

/**
 *
 * @author msuppahiya
 */
public class RequestNewPassword {

    JCoFunction function;
    JCoTable codes;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(RequestNewPassword.class.getName());

    public RequestNewPassword() {
        this.function = null;
        this.codes = null;
        this.connection = null;
    }

    public RPResponse getRPnewPassword(RPResponse rpResponse, String _system, HttpSession session) throws Exception, Throwable {
        function = null;
        connection = null;
        String token = (String) session.getAttribute("Token");
        if (token == null) {
            token = "";
        }
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

            function = connection.getFunction("/PSYNG/RP_043");

            JCoParameterList inputList = function.getImportParameterList();
            logger.error("BAPI /PSYNG/RP_043 : USERID " + rpResponse.getUserID());
            String userIDOther = "";
            if (session.getAttribute("userIDApp") != null) {
                userIDOther = (String) session.getAttribute("userIDApp");
            }

            if (userIDOther.length() <= 0) {
                userIDOther = rpResponse.getUserIDOther();
            }
            if (userIDOther.length() > 0) {
                inputList.setValue("I_REQUESTER", rpResponse.getUserID());
                inputList.setValue("UNAME", userIDOther);
            } else {
                inputList.setValue("UNAME", rpResponse.getUserID());
            }
            if (!RPConfig.getConfig_flag().isQUES_ANS_WORKFLOW()) {

                if ((rpResponse.getPassCode() != null) && (rpResponse.getPassCode().length() > 0)) {
                    inputList.setValue("CHARURL", rpResponse.getPassCode());  
                } else {
                    if (session.getAttribute("passcode") != null) {
                        inputList.setValue("CHARURL", session.getAttribute("passcode").toString());
                    }
                }

            } else {
                inputList.setValue("UQANAFLG", "1");
                if ((rpResponse.getPassCode() != null) && (rpResponse.getPassCode().length() > 0)) {
                    inputList.setValue("CHARURL", rpResponse.getPassCode());  
                } else {
                    if (session.getAttribute("passcode") != null) {
                        inputList.setValue("CHARURL", session.getAttribute("passcode").toString());
                    }
                }
            }

            inputList.setValue("PASSLOGIC", "" + rpResponse.getPassLogic());
            inputList.setValue("I_SYNC_PASS_ALL_SYS", rpResponse.getPassSync());
            inputList.setValue("I_TOKENID", token);
            JCoTable combdataTable = function.getTableParameterList().getTable("IT_SYSTEMS");

            combdataTable.appendRow();
            combdataTable.setValue("RFCNAME", rpResponse.getSapSystem());

            logger.error("Executing bapi /PSYNG/RP_043");
            connection.execute(function);

            connection.release();
            connection = null;

            JCoTable protb = null;
            protb = function.getTableParameterList().getTable("ET_RETURN");
            if (protb.getNumRows() == 0) {
                logger.error("table is empty");
                throw new Exception("table is empty");
            } else {
                String msg[] = new String[protb.getNumRows()];
                for (int i = 0; i < protb.getNumRows(); i++) {
                    protb.setRow(i);
                    String type = protb.getString("TYPE");
                    String code = protb.getString("CODE");
                    String message = protb.getString("MESSAGE");

                    if (code.equals("025")) {
                        msg[i] = message;
                        JCoTable EPASSWORD = null;
                        EPASSWORD = function.getTableParameterList().getTable("ET_SYSTEM_PASS");
                        EPASSWORD.setRow(i);
                        String rfc = EPASSWORD.getString("RFCNAME");
                        String pass = EPASSWORD.getString("PASSWORD");
                        String system = EPASSWORD.getString("SAPSYSTEM");

                        rpResponse.setPassword(pass);
//                        rpResponse.getMultipleSystemList().add(mSystem);
//                        rpResponse.getMultipleSystemMap().put(system, mSystem);
                        if (RPConfig.getConfig_flag().isResetPOPUP()) {
                            rpResponse.setEmail(false);
                            rpResponse.setPassByUser(true);

                        } else {
                            rpResponse.setEmail(true);
                        }
                        rpResponse.setSuccess(true);

                        //return rpResponse;
                    } else if (code.equals("075")) {
                        // rpResponse.setMessage(returnStructure.getString("MESSAGE"));
                        JCoTable EPASSWORD = null;
                        EPASSWORD = function.getTableParameterList().getTable("ET_SYSTEM_PASS");
                        String msg2[] = new String[EPASSWORD.getNumRows()];
                        for (int j = 0; j < EPASSWORD.getNumRows(); j++) {
                            EPASSWORD.setRow(j);
                            // String rfc = EPASSWORD.getString("RFCNAME");
                            String pass = EPASSWORD.getString("PASSWORD");
                            String system = EPASSWORD.getString("SAPSYSTEM");
                            rpResponse.setPassword(pass);
//                        rpResponse.getMultipleSystemList().add(mSystem);
//                        rpResponse.getMultipleSystemMap().put(system, mSystem);
                            if (RPConfig.getConfig_flag().isResetPOPUP()) {
                                rpResponse.setEmail(false);
                                rpResponse.setPassReset(false);
                                rpResponse.setPassByUser(true);

                            } else {
//                                msg[i] = "Your password has been reset successfully!";
                                msg2[j] = "Your password for " + system + " is " + pass;
                                //rpResponse.setMessage();
                                rpResponse.setPassReset(true);
                            }
                            rpResponse.setSuccess(true);
                            //return rpResponse;
                        }
                        if (rpResponse.isPassReset()) {//displaying multiple password in popup
                            String finalmessage = "";
                            for (int k = 0; k < msg2.length; k++) {
                                if (msg2[k] != null) {
                                    finalmessage = finalmessage + "\n" + msg2[k];
                                }
                            }
                            rpResponse.setPassword(finalmessage);
                        }
                    }//((!(message.equals(""))) || (type.equals("W")))
                    else {
                        msg[i] = message;
                        rpResponse.setSuccess(false);
                        // return rpResponse;
                    }
//                    
                }

                String multimessage = "";
                for (int j = 0; j < msg.length; j++) {
                    if (msg[j] != null) {
                        multimessage = multimessage + "\n" + msg[j];
                    }

                }
                if (!rpResponse.isEmail()) {
                    if (multimessage.length() > 0) {
                        String str = rpResponse.getPassword();
                        str += "\n" + multimessage;
                        multimessage = str;
                        rpResponse.setInfoFlag(true);

                    }
                }
                rpResponse.setMessage(multimessage);
                return rpResponse;
            }

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
//        return rpResponse;
    }
}
