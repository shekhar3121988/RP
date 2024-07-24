/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.ADconnection;
import sw.com.rp.connection.ADserver;
import sw.com.rp.connection.SAPConnection;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dto.POJO_QuesAns;
import sw.com.rp.dto.RPRequest;
import sw.com.rp.dto.RPResponse;
import sw.com.rp.rest.EncryptDecrypt;

/**
 *
 * @author msaini
 */
public class ProcessRPRequest {

    JCoTable codes;
    JCoFunction function;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(ProcessRPRequest.class.getName());

    public ProcessRPRequest() {
        this.function = null;
        this.codes = null;
        this.connection = null;
    }

    public RPResponse getRPrequestResponse(RPRequest rpRequest, String _system, HttpSession session) throws Exception, Throwable {
        function = null;
        connection = null;
        String language = "EN";
        String lang = (String) session.getAttribute("LANG");
        if (lang != null) {
            if (lang.equalsIgnoreCase("English")) {
                language = "EN";
            } else if (lang.equalsIgnoreCase("German")) {
                language = "DE";
            }
        }
        RPResponse rpResponse = new RPResponse();
//        String system = rpRequest.getHostSystem();
//        SapSystem isystem = RPConfig.getSapSystemMap().get(system);
        connection = new SAPConnection(language);
        try {
            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));

            function = connection.getFunction("/PSYNG/RP_041");

            JCoParameterList inputList = function.getImportParameterList();
            logger.info("BAPI /PSYNG/RP_041 : USERID " + rpRequest.getUserID());
            rpResponse.setUserID(rpRequest.getUserID());

            inputList.setValue("LASTNAME", rpRequest.getLastName());
            inputList.setValue("FIRSTNAME", rpRequest.getFirstName());
            inputList.setValue("EMAILID", rpRequest.getEmailID());

            if (rpRequest.getUserIDApp().length() > 0) {
                inputList.setValue("I_REQUESTER", rpRequest.getUserID());
                inputList.setValue("USER", rpRequest.getUserIDApp());
            } else {
                inputList.setValue("I_REQUESTER", "");
                inputList.setValue("USER", rpRequest.getUserID());
            }
//            inputList.setValue("SYSID", rpRequest.getSapSystem());
            inputList.setValue("RPURL", RPConfig.getConfig_flag().getRP_URL());
            if (RPConfig.getConfig_flag().isQUES_ANS_WORKFLOW()) {
                inputList.setValue("QANSFLAG", "Y");
                if (!RPConfig.getConfig_flag().isEMAIL_QUES_LINK()) {
                    inputList.setValue("QANSFLAG", "Q");
                }
            } else {
                inputList.setValue("QANSFLAG", "N");
            }
            JCoTable combdataTable = function.getTableParameterList().getTable("IT_SYSTEMS");

            Iterator<String> itrc = rpRequest.getMultipleSystem().iterator();

            combdataTable.appendRow();
            combdataTable.setValue("RFCNAME", rpRequest.getSapSystem());

            logger.info("Executing bapi /PSYNG/RP_041");
            connection.execute(function);

            if (!RPConfig.getConfig_flag().isEMAIL_QUES_LINK()) {
                JCoParameterList export = function.getExportParameterList();
                String charurl = export.getString("CHARURL");
                if (charurl != null && charurl.length() > 0) {
                    rpResponse.setPassCode(charurl);
                }
            }
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
                    if (!(type.equals("") || type.equals("S"))) {
                        if (code.equals("788") && RPConfig.getConfig_flag().isHELP_DESK()) {
//                        rpResponse.setMessage(returnStructure.getString("MESSAGE"));
                            String msg1 = message;
//                    String msg2 = "";
//                    String msg3 = "";
//                    String msg4 = "";
//                    String msg5 = "";
//                    if (!RPConfig.getConfig_flag().getHELP_PHONE().equalsIgnoreCase("NA")) {
//                        msg2 = "Help Desk Phone Number : " + RPConfig.getConfig_flag().getHELP_PHONE() + "<br>";
//                    }
//                    if (!RPConfig.getConfig_flag().getHELP_CONTACT().equalsIgnoreCase("NA")) {
//                        msg3 = "Contact Name : " + RPConfig.getConfig_flag().getHELP_CONTACT() + "<br>";
//                    }
//                    if (!RPConfig.getConfig_flag().getHELP_EMAIL().equalsIgnoreCase("NA")) {
//                        msg4 = "Email : " + RPConfig.getConfig_flag().getHELP_EMAIL() + "<br>";
//                    }
//                    if (!RPConfig.getConfig_flag().getHELP_PAGE().equalsIgnoreCase("NA")) {
//                        msg5 = "Support Web Page : <a href='" + RPConfig.getConfig_flag().getHELP_PAGE() + "' target='_blank' >Click Here</a>";
//                    }
                            msg[i] = message;
                            //rpResponse.setMessage(msg1);

                            rpResponse.setLocked(true);
                            rpResponse.setHelpDesk(true);
                            rpResponse.setSuccess(false);
                            //return rpResponse;
                            String helpDeskLabels[] = new String[5];
                            helpDeskLabels[0] = getTranslatedText("RPHelpDeskCustomMsg", session);
                            if (helpDeskLabels[0].equals(".") || helpDeskLabels[0].equalsIgnoreCase("NA")
                                    || helpDeskLabels[0].equalsIgnoreCase("N/A")) {
                                helpDeskLabels[0] = "";
                            }
                            helpDeskLabels[1] = getTranslatedText("RPHelpDeskNameMsg", session);
                            helpDeskLabels[2] = getTranslatedText("RPHelpDeskNumberMsg", session);
                            helpDeskLabels[3] = getTranslatedText("RPHelpDeskEmailMsg", session);
                            helpDeskLabels[4] = getTranslatedText("RPHelpDeskWebLinkMsg", session);
                            rpResponse.setHelpDeskLabels(helpDeskLabels);

                        }
                        if (message.contains("locked")) {
                            rpResponse.setLocked(true);
                        }
                        msg[i] = message;
                        //rpResponse.setMessage(message);
                        rpResponse.setSuccess(false);
                        // return rpResponse;
                    } else if (!RPConfig.getConfig_flag().isEMAIL_QUES_LINK() && RPConfig.getConfig_flag().isQUES_ANS_WORKFLOW()) {
                        rpResponse.setMessage("Showing Question-Answer Screen.");
                        rpResponse.setShowQA(true);
                        rpResponse.setSuccess(true);
                        // return rpResponse;
                    } else {
                        msg[i] = message;
                        //rpResponse.setMessage(message);
                        rpResponse.setSuccess(true);
                        //return rpResponse;
                    }
                }
                String multimessage = "";
                for (int j = 0; j < msg.length; j++) {
                    if (msg[j] != null) {
                        multimessage = multimessage + msg[j];
                    }
                }
//                getTranslatedText("ProcessRPRequestmsg1", session);

                rpResponse.setMessage(multimessage);
                return rpResponse;
            }

//            JCO.Table output_QuesTable = function.getTableParameterList().getTable("QUESTIONS");
//            if (output_QuesTable.getNumRows() > 0) {
//
//                POJO_QuesAns question = null;
//                quesAnsList = new ArrayList<POJO_QuesAns>();
//                int rows = output_QuesTable.getNumRows();
//                for (int row = 0; row < rows; row++) {
//                    if (row == 0) {
//                        logMsg("Reading All Questions from QUESTIONS Table .");
//                    }
//                    output_QuesTable.setRow(row);
//                    question = new POJO_QuesAns();
//                    question.setQuestionID(output_QuesTable.getString("QUESTIONID"));
//                    question.setQuestion(output_QuesTable.getString("QUESTION"));
//                    quesAnsList.add(question);
//                    question = null;
//                }
//            } else {
//                logMsg("QUESTIONS table EMPTY");
//            }
//            JCO.ParameterList exportParameters = function.getExportParameterList();
//            numberOfQuestionsToBeShown = exportParameters.getValue("RANDOM_QUESTIONS").toString().trim();
//            JCO.Table output_QuesAnsTable = function.getTableParameterList().getTable("QANA");
//            if (output_QuesAnsTable.getNumRows() > 0) {
//                logMsg("User Login to update Question Answers");
//                POJO_QuesAns question = null;
//                existingQuesAnsList = new ArrayList<POJO_QuesAns>();
//                int rows = output_QuesAnsTable.getNumRows();
//                for (int row = 0; row < rows; row++) {
//                    if (row == 0) {
//                        logMsg("Reading Questions-Answers from QANA Table .");
//                    }
//                    output_QuesAnsTable.setRow(row);
//                    question = new POJO_QuesAns();
//                    question.setQuestionID(output_QuesAnsTable.getString("QUESTIONID"));
//                    question.setQuestion(output_QuesAnsTable.getString("QUESTION"));
//                    if (RPConfig.isJavaEncryption()) {
//                        logMsg("Decrypting answers...");
//                        EncryptDecrypt rpc = new EncryptDecrypt();
////                        RPConfig rpc = new RPConfig();
//                        String ans = "";
//                        try {
//                            String encAns = output_QuesAnsTable.getString("ANSWER");
////                            System.out.println("encAns : "+encAns);
//                            ans = rpc.decrypt(encAns);
//                        } catch (Exception e) {
//                            logMsg("decrypting error : " + e.toString());
//                        }
//                        question.setAnswers(ans);
//
//                    } else {
//                        question.setAnswers(output_QuesAnsTable.getString("ANSWER"));
//                    }
//                    existingQuesAnsList.add(question);
//                    question = null;
//                }
//            } else {
//                logMsg("QANA Table empty : User Login first time to save questions-answers.");
//            }
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
//        function = null;
//        connection = null;
//        userRegistrationMap.put("1", quesAnsList);
//        userRegistrationMap.put("2", numberOfQuestionsToBeShown);
//        userRegistrationMap.put("3", existingQuesAnsList);
//        return userRegistrationMap;
    }
//POJO_QuesAns

    public HashMap<String, Object> getRPQesAns(RPRequest rpRequest, String _system, String passCode, HttpSession session) throws Exception, Throwable {
        function = null;
        connection = null;
        HashMap<String, Object> quesAnsMap = new HashMap<String, Object>();

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

            function = connection.getFunction("/PSYNG/RP_011");

            JCoParameterList inputList = function.getImportParameterList();
            logger.info("BAPI /PSYNG/RP_011 : USERID " + rpRequest.getUserID());
            //inputList.setValue("USERID", rpRequest.getUserID());
            if (rpRequest.getUserIDApp().length() > 0) {
                inputList.setValue("I_REQUESTER", rpRequest.getUserID());
                inputList.setValue("USERID", rpRequest.getUserIDApp());
            } else {
                inputList.setValue("I_REQUESTER", "");
                inputList.setValue("USERID", rpRequest.getUserID());
            }
            inputList.setValue("CHARURL", passCode);
            // inputList.setValue("SYSID", rpRequest.getSapSystem());

            if (RPConfig.getConfig_flag().isJAVA_ENCRYPTION()) {
                inputList.setValue("I_JENCRYPTLOGIC", "Y");
            } else {
                inputList.setValue("I_JENCRYPTLOGIC", "N");
            }
            JCoTable combdataTable = function.getTableParameterList().getTable("IT_SYSTEMS");

            combdataTable.appendRow();
            combdataTable.setValue("RFCNAME", rpRequest.getSapSystem());

            connection.execute(function);

            connection.release();
            connection = null;
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
//            quesAnsList = null;
            connection.release();
            connection = null;
            throw new Exception(ex.getMessage());

        }
        try {
            JCoTable protb = null;
            protb = function.getTableParameterList().getTable("ET_RETURN");
            if (protb.getNumRows() != 0) {

                String msg[] = new String[protb.getNumRows()];
                for (int i = 0; i < protb.getNumRows(); i++) {
                    protb.setRow(i);
                    String type = protb.getString("TYPE");
                    String code = protb.getString("CODE");
                    String message = protb.getString("MESSAGE");
                    if (!(type.equals("") || type.equals("S"))) {
                        logger.info("/PSYNG/RP_011 return message : " + message);
                        quesAnsMap.put("processed", "processed");

                        return quesAnsMap;
                    }
                }
            }
            // JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");

            JCoParameterList list = function.getExportParameterList();
            String userparam = list.getValue("USER_PARAM").toString();
            logger.info("Count of Answer(s) to be correct : " + userparam);
            quesAnsMap.put("param", userparam);
            JCoTable request1 = function.getTableParameterList().getTable("QANA");
            ArrayList<POJO_QuesAns> quesAnsList = new ArrayList<POJO_QuesAns>();
            if (request1.getNumRows() > 0) {
                for (int i = 0; i
                        < request1.getNumRows(); i++) {

                    request1.setRow(i);
                    POJO_QuesAns quesAns = new POJO_QuesAns();
                    quesAns.setQuestionID(request1.getString("QUESTIONID"));
                    quesAns.setQuestion(request1.getString("QUESTION"));
                    if (RPConfig.getConfig_flag().isJAVA_ENCRYPTION()) {
                        EncryptDecrypt rpc = new EncryptDecrypt();
                        String ans = "";
                        try {
                            logger.info("Decrypting answers...");
                            ans = rpc.decrypt(request1.getString("ANSWER"));
                            quesAns.setAnswers(ans);
                        } catch (Exception ex) {
                            logger.error("Error in Decrypting answers : ");
                            StringWriter stack = new StringWriter();
                            ex.printStackTrace(new PrintWriter(stack));
                            logger.error(stack.toString());
                            stack = null;
                            //multilanguage code
                            String text = getTranslatedText("ProcessRPRequestmsg1", session);
                            if (text == null || text.trim().length() == 0) {
                                text = "Error in Decrypting answers";
                            }
                            throw new Exception(text);
                        }
                    } else {
                        quesAns.setAnswers(request1.getString("ANSWER"));
                    }
                    quesAnsList.add(quesAns);
                }
            }
            quesAnsMap.put("QuesAnsList", quesAnsList);
            return quesAnsMap;
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            throw new Exception(ex.getMessage());
        }

//            JCO.Table output_QuesTable = function.getTableParameterList().getTable("QUESTIONS");
//            if (output_QuesTable.getNumRows() > 0) {
//
//                POJO_QuesAns question = null;
//                quesAnsList = new ArrayList<POJO_QuesAns>();
//                int rows = output_QuesTable.getNumRows();
//                for (int row = 0; row < rows; row++) {
//                    if (row == 0) {
//                        logMsg("Reading All Questions from QUESTIONS Table .");
//                    }
//                    output_QuesTable.setRow(row);
//                    question = new POJO_QuesAns();
//                    question.setQuestionID(output_QuesTable.getString("QUESTIONID"));
//                    question.setQuestion(output_QuesTable.getString("QUESTION"));
//                    quesAnsList.add(question);
//                    question = null;
//                }
//            } else {
//                logMsg("QUESTIONS table EMPTY");
//            }
//            JCO.ParameterList exportParameters = function.getExportParameterList();
//            numberOfQuestionsToBeShown = exportParameters.getValue("RANDOM_QUESTIONS").toString().trim();
//            JCO.Table output_QuesAnsTable = function.getTableParameterList().getTable("QANA");
//            if (output_QuesAnsTable.getNumRows() > 0) {
//                logMsg("User Login to update Question Answers");
//                POJO_QuesAns question = null;
//                existingQuesAnsList = new ArrayList<POJO_QuesAns>();
//                int rows = output_QuesAnsTable.getNumRows();
//                for (int row = 0; row < rows; row++) {
//                    if (row == 0) {
//                        logMsg("Reading Questions-Answers from QANA Table .");
//                    }
//                    output_QuesAnsTable.setRow(row);
//                    question = new POJO_QuesAns();
//                    question.setQuestionID(output_QuesAnsTable.getString("QUESTIONID"));
//                    question.setQuestion(output_QuesAnsTable.getString("QUESTION"));
//                    if (RPConfig.isJavaEncryption()) {
//                        logMsg("Decrypting answers...");
//                        EncryptDecrypt rpc = new EncryptDecrypt();
////                        RPConfig rpc = new RPConfig();
//                        String ans = "";
//                        try {
//                            String encAns = output_QuesAnsTable.getString("ANSWER");
////                            System.out.println("encAns : "+encAns);
//                            ans = rpc.decrypt(encAns);
//                        } catch (Exception e) {
//                            logMsg("decrypting error : " + e.toString());
//                        }
//                        question.setAnswers(ans);
//
//                    } else {
//                        question.setAnswers(output_QuesAnsTable.getString("ANSWER"));
//                    }
//                    existingQuesAnsList.add(question);
//                    question = null;
//                }
//            } else {
//                logMsg("QANA Table empty : User Login first time to save questions-answers.");
//            }
//        function = null;
//        connection = null;
//        userRegistrationMap.put("1", quesAnsList);
//        userRegistrationMap.put("2", numberOfQuestionsToBeShown);
//        userRegistrationMap.put("3", existingQuesAnsList);
//        return quesAnsList;
    }
//
//
//    public RPResponse getPasswordforQA(String UserID, String _system, String _syskey) throws Exception, Throwable {
//        function = null;
//        connection = null;
//        RPResponse rpResponse = new RPResponse();
////        HashMap<String, Object> userRegistrationMap = new HashMap<String, Object>();
////        ArrayList<POJO_QuesAns> quesAnsList = new ArrayList<POJO_QuesAns>();
////        ArrayList<POJO_QuesAns> existingQuesAnsList = new ArrayList<POJO_QuesAns>();
////        String numberOfQuestionsToBeShown = "";
//
//        connection = new SAPConnection();
//        try {
//
//            connection.prepareConnection(RPConfig.getAppClientProperties().get(_syskey));
//
//            function = connection.getFunction("/PSYNG/RP_043");
//
//            JCoParameterList inputList = function.getImportParameterList();
//            logger.info("BAPI /PSYNG/RP_043 : USERID " + UserID);
//            rpResponse.setUserID(UserID);
//            inputList.setValue("UNAME", UserID);
//
//            String passLogic = RPConfig.getConfig_flag().getPASS_LOGIC();
//            inputList.setValue("PASSLOGIC", passLogic);
//            inputList.setValue("UQANAFLG", "1");
//            inputList.setValue("SYSID", _system);
//
//            logger.info("Executing bapi /PSYNG/RP_043");
//            connection.execute(function);
//
//            connection.release();
//            connection = null;
//        } catch (Exception ex) {
//            StringWriter stack = new StringWriter();
//            ex.printStackTrace(new PrintWriter(stack));
//            logger.error(stack.toString());
//            stack = null;
//            connection.release();
//            connection = null;
//            throw new Exception(ex.getMessage());
//        }
//        try {
//            JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
//            if (!(returnStructure.getString("MESSAGE").equals("")) || (returnStructure.getString("TYPE").equals("W"))) {
//                rpResponse.setMessage(returnStructure.getString("MESSAGE"));
//                rpResponse.setSuccess(false);
//                return rpResponse;
//            }
//            if (returnStructure.getString("CODE").equals("025")) {
//                rpResponse.setMessage(returnStructure.getString("MESSAGE"));
//                rpResponse.setSuccess(true);
//                rpResponse.setEmail(true);
//                return rpResponse;
//            }
//            if (returnStructure.getString("CODE").equals("075")) {
//                // rpResponse.setMessage(returnStructure.getString("MESSAGE"));
//                JCoParameterList response = function.getExportParameterList();
//                String passwd = response.getString("EPASSWORD");
//                rpResponse.setPassword(passwd);
//                rpResponse.setMessage("Your password has been reset successfully!");
//                rpResponse.setSuccess(true);
//                rpResponse.setPassReset(true);
//                return rpResponse;
//            } else {
//                rpResponse.setMessage("Something went wrong");
//                rpResponse.setSuccess(false);
//                return rpResponse;
//            }
//        } catch (Exception ex) {
//            StringWriter stack = new StringWriter();
//            ex.printStackTrace(new PrintWriter(stack));
//            logger.error(stack.toString());
//            stack = null;
//            connection.release();
//            connection = null;
//            throw new Exception(ex.getMessage());
//        }
//    }
////

    public RPResponse lockUserforWrongQA(RPResponse rpResponse, String _systemkey, HttpSession session) throws Exception, Throwable {
        function = null;
        connection = null;
        //RPResponse rpResponse = new RPResponse();
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

            connection.prepareConnection(RPConfig.getAppClientProperties().get(_systemkey));

            function = connection.getFunction("/PSYNG/RP_008");

            JCoParameterList inputList = function.getImportParameterList();
            logger.info("BAPI /PSYNG/RP_008 : USERID " + rpResponse.getUserID());
            //rpResponse.setUserID(rpResponse.getUserID());
            inputList.setValue("USERID", rpResponse.getUserID());
            inputList.setValue("CHARURL", rpResponse.getPassCode());
            inputList.setValue("APPLICANT_ID", rpResponse.getUserIDOther());

            JCoTable combdataTable = function.getTableParameterList().getTable("IT_SYSTEMS");
            combdataTable.appendRow();
            combdataTable.setValue("RFCNAME", rpResponse.getSapSystem());

            //inputList.setValue("IM_SYSTEMNAME", system);
            logger.info("Executing bapi /PSYNG/RP_008");
            connection.execute(function);

            connection.release();
            connection = null;
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            connection.release();
            connection = null;
            throw new Exception(e.getMessage());
        }

        try {

            JCoTable returnStructure = function.getTableParameterList().getTable("RETURN");
            if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
                rpResponse.setMessage("RP_008 :  Error in locking user : " + returnStructure.getString("MESSAGE"));
                rpResponse.setSuccess(true);
                rpResponse.setPassReset(false);
                return rpResponse;
            } else if (returnStructure.getString("MESSAGE").contains("locked")) {
                //ProcessRPRequestmsg8
                //multilanguage code
                String text = getTranslatedText("ProcessRPRequestmsg8", session);
                if (text == null || text.equals("ProcessRPRequestmsg8") || text.trim().length() == 0) {
                    text = " We're sorry, but the user is locked due to incorrect answer(s) of the security questions(s)";
                }
                rpResponse.setMessage(text);
                rpResponse.setSuccess(true);
                rpResponse.setLocked(true);
                rpResponse.setUserlocked(true);
                return rpResponse;
            } else {

                rpResponse.setMessage(returnStructure.getString("MESSAGE"));
                rpResponse.setSuccess(true);
                return rpResponse;
            }
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            throw new Exception(e.getMessage());
        }
    }
//   public void logMsg(String msg) {
//        try {
//            if (RPConfig.isLogging()) {
//                logger.info(msg);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//
//    public void logex(Exception ex) {
//        try {
//            if (RPConfig.isLogging()) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//    public void logTh(Throwable ex) {
//        try {
//            if (RPConfig.isLogging()) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }

    public boolean validateUIDWithAD(String UserID, HttpSession session) throws Throwable {
        boolean userFound = false;
        String lockoutTime = "";
        String userAccountControl = "";
        String accountExpires = "";
        DirContext ldapContext = null;

        for (ADserver server : RPConfig.getAdServerList()) {
            ADconnection conn = new ADconnection();

            try {
                logger.info("Connecting " + server.getDomain());
                ldapContext = conn.getDirContext(server, session);
                StringTokenizer token = new StringTokenizer(server.getDomain(), ".");
                String searchBase = "";

                while (token.hasMoreTokens()) {
                    searchBase = searchBase + "dc=" + token.nextToken() + ",";

                }
                searchBase = searchBase.substring(0, searchBase.length() - 1).trim();
                searchBase = "cn=users," + searchBase;
                String returnedAtts[] = {"cn", "sAMAccountName", "lockoutTime", "userAccountControl", "accountExpires"};
                String searchFilter = null;
                SearchControls searchCtrls = new SearchControls();
                searchCtrls.setReturningAttributes(returnedAtts);
                searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                searchFilter = "(&(objectClass=user)(sAMAccountName=" + UserID + "))";
                NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchCtrls);

                while (answer.hasMoreElements()) {
                    userFound = true;
                    logger.info("User " + UserID + " found in " + server.getDomain());
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attrs = sr.getAttributes();

                    if (attrs != null) {
                        NamingEnumeration ne = attrs.getAll();

                        while (ne.hasMore()) {
                            Attribute attr = (Attribute) ne.next();

                            if (attr.getID().equalsIgnoreCase("lockoutTime")) {
                                lockoutTime = attr.get().toString();

                            }
                            if (attr.getID().equalsIgnoreCase("userAccountControl")) {
                                userAccountControl = attr.get().toString();

                            }
                            if (attr.getID().equalsIgnoreCase("accountExpires")) {
                                accountExpires = attr.get().toString();

                            }
                        }
                        ne.close();

                    }
                    break;

                }
            } catch (Throwable ex) {
                ldapContext.close();
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;

                throw new Exception(ex.getMessage());

            }
            if (userFound) {
                ldapContext.close();

                break;

            }
            ldapContext.close();

            if (!userFound) {
                logger.info("User " + UserID + " not found in " + server.getDomain());

                //multilanguage code
                String text = getTranslatedText("ProcessRPRequestmsg2", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User ID not found in Active Directory server.";
                }
                throw new Exception(text);

            }
        }
        String ACCOUNT_NEVER_EXPIRE_VALUE = "9223372036854775807";

        boolean accountNeverExpire = accountExpires.equals("0") || ACCOUNT_NEVER_EXPIRE_VALUE.equals(accountExpires);

        boolean accountExpired = false;

        if (!accountNeverExpire) {
            Date accountExpiresDate = getDateFrom(accountExpires);
            logger.info("account Expires Date - " + accountExpiresDate.toString());

            Date currentDateTime = new Date();
            Date currentDate = truncTimeFrom(currentDateTime);
            accountExpired = accountExpiresDate.compareTo(currentDate) < 0;
            //int daysBeforeAccountExpiration = Integer.MAX_VALUE;
            // daysBeforeAccountExpiration = (int) TimeUnit.DAYS.convert(accountExpiresDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);

        } else {
            logger.info("account set to never expires in AD.");

        }
        if (accountExpired) {
            //multilanguage code
            String text = getTranslatedText("ProcessRPRequestmsg5", session);
            if (text == null || text.trim().length() == 0) {
                text = "User account expired in active directory.";
            }
            throw new Exception(text);

        } else {
            logger.info("user Account Not Expired in AD.");

        }

        logger.info(UserID + " user lockoutTime : " + lockoutTime);

        if (lockoutTime != null && lockoutTime.trim().length() > 1) {
            //multilanguage code
            String text = getTranslatedText("ProcessRPRequestmsg3", session);
            if (text == null || text.trim().length() == 0) {
                text = "User is locked in Active Directory.";
            }
            throw new Exception(text);

        }
        if (userAccountControl != null && (userAccountControl.equalsIgnoreCase("66050") || userAccountControl.equalsIgnoreCase("514"))) {
            //multilanguage code
            String text = getTranslatedText("ProcessRPRequestmsg4", session);
            if (text == null || text.trim().length() == 0) {
                text = "User account disabled in active directory.";
            }
            throw new Exception(text);

        }

        return userFound;

    }
//

    public boolean validateUIDemailWithAD(String UserID, String EmailID, HttpSession session) throws Throwable {
        boolean userFound = false;
        String lockoutTime = "";
        String userAccountControl = "";
        String accountExpires = "";
        String ADemail = "";
        String serverName = "";
        DirContext ldapContext = null;

        for (ADserver server : RPConfig.getAdServerList()) {
            ADconnection conn = new ADconnection();

            try {
                serverName = server.getDomain();
                logger.info("Connecting " + server.getDomain());
                ldapContext = conn.getDirContext(server, session);
                StringTokenizer token = new StringTokenizer(server.getDomain(), ".");
                String searchBase = "";

                while (token.hasMoreTokens()) {
                    searchBase = searchBase + "dc=" + token.nextToken() + ",";

                }
                searchBase = searchBase.substring(0, searchBase.length() - 1).trim();
                searchBase = "cn=users," + searchBase;
                String returnedAtts[] = {"cn", "mail", "sAMAccountName", "lockoutTime", "userAccountControl", "accountExpires"};
                String searchFilter = null;
                SearchControls searchCtrls = new SearchControls();
                searchCtrls.setReturningAttributes(returnedAtts);
                searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                searchFilter = "(&(objectClass=user)(sAMAccountName=" + UserID + "))";
                NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchCtrls);

                while (answer.hasMoreElements()) {
                    userFound = true;
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attrs = sr.getAttributes();

                    if (attrs != null) {
                        NamingEnumeration ne = attrs.getAll();

                        while (ne.hasMore()) {
                            Attribute attr = (Attribute) ne.next();

                            if (attr.getID().equalsIgnoreCase("mail")) {
                                ADemail = attr.get().toString();

                            }
                            if (attr.getID().equalsIgnoreCase("lockoutTime")) {
                                lockoutTime = attr.get().toString();

                            }
                            if (attr.getID().equalsIgnoreCase("userAccountControl")) {
                                userAccountControl = attr.get().toString();

                            }
                            if (attr.getID().equalsIgnoreCase("accountExpires")) {
                                accountExpires = attr.get().toString();

                            }
                        }
                        ne.close();

                    }
                }
                ldapContext.close();

            } catch (Throwable ex) {
                ldapContext.close();
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;

                throw new Exception(ex.getMessage());

            }
            ldapContext.close();

            if (userFound) {
                ldapContext.close();

                break;

            }
        }

        if (userFound) {
            String ACCOUNT_NEVER_EXPIRE_VALUE = "9223372036854775807";

            boolean accountNeverExpire = accountExpires.equals("0") || ACCOUNT_NEVER_EXPIRE_VALUE.equals(accountExpires);

            boolean accountExpired = false;

            if (!accountNeverExpire) {
                Date accountExpiresDate = getDateFrom(accountExpires);
                logger.info("account Expires Date - " + accountExpiresDate.toString());

                Date currentDateTime = new Date();
                Date currentDate = truncTimeFrom(currentDateTime);
                accountExpired = accountExpiresDate.compareTo(currentDate) < 0;
                //int daysBeforeAccountExpiration = Integer.MAX_VALUE;
                // daysBeforeAccountExpiration = (int) TimeUnit.DAYS.convert(accountExpiresDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);

            } else {
                logger.info("account set to never expires in AD.");

            }
            if (accountExpired) {
                //multilanguage code
                String text = getTranslatedText("ProcessRPRequestmsg5", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User account expired in active directory.";
                }
                throw new Exception(text);

            } else {
                logger.info("user Account Not Expired in AD.");

            }
            logger.info(UserID + " user lockoutTime : " + lockoutTime);

            if (lockoutTime != null && lockoutTime.trim().length() > 1) {
                //multilanguage code
                String text = getTranslatedText("ProcessRPRequestmsg6", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User is locked in Active Directory.";
                }
                throw new Exception(text);

            } else if (userAccountControl != null && (userAccountControl.equalsIgnoreCase("66050") || userAccountControl.equalsIgnoreCase("514"))) {
                //multilanguage code
                String text = getTranslatedText("ProcessRPRequestmsg4", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User account disabled in active directory.";
                }
                throw new Exception(text);

            } else if (!EmailID.equalsIgnoreCase(ADemail)) {
                //multilanguage code
                String text = getTranslatedText("ProcessRPRequestmsg7", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Email ID not matched in Active Directory.";
                }
                throw new Exception(text);

            } else {
                logger.info("User " + UserID + " found in server " + serverName);

                return userFound;

            }

        } else {
            //multilanguage code
            String text = getTranslatedText("ProcessRPRequestmsg2", session);
            if (text == null || text.trim().length() == 0) {
                text = "User ID not found in Active Directory server.";
            }
            throw new Exception(text);

        }
    }
    private final long DIFF_NET_JAVA_FOR_DATES = 11644473600000L + 24 * 60 * 60 * 1000;
//

    public Date getDateFrom(String adDateStr) {
        long adDate = Long.parseLong(adDateStr);

        long milliseconds = (adDate / 10000) - DIFF_NET_JAVA_FOR_DATES;
        Date date = new Date(milliseconds);

        return date;

    }
//

    public Date truncTimeFrom(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();

    }
//

    public String resetActiveDirectoryPassword(String UserId, String Password, HttpSession session) throws Throwable {
        boolean userFound = false;
        String serverName = "";
        String commonName = "";
        String searchBase = "";
        DirContext ldapContext = null;

        for (ADserver server : RPConfig.getAdServerList()) {
            ADconnection conn = new ADconnection();

            try {
                serverName = server.getDomain();
                logger.info("Connecting " + server.getDomain());
                ldapContext = conn.getDirContext(server, session);

                StringTokenizer token = new StringTokenizer(server.getDomain(), ".");

                while (token.hasMoreTokens()) {
                    searchBase = searchBase + "dc=" + token.nextToken() + ",";

                }
                searchBase = searchBase.substring(0, searchBase.length() - 1).trim();
                searchBase = "cn=users," + searchBase;
                String returnedAtts[] = {"cn", "mail", "sAMAccountName"};
                String searchFilter = null;
                SearchControls searchCtrls = new SearchControls();
                searchCtrls.setReturningAttributes(returnedAtts);
                searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                searchFilter = "(&(objectClass=user)(sAMAccountName=" + UserId + "))";
                NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchCtrls);

                while (answer.hasMoreElements()) {
                    userFound = true;
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attrs = sr.getAttributes();

                    if (attrs != null) {
                        NamingEnumeration ne = attrs.getAll();

                        while (ne.hasMore()) {
                            Attribute attr = (Attribute) ne.next();

                            if (attr.getID().equalsIgnoreCase("cn")) {
                                commonName = attr.get().toString();

                            }
                        }
                        ne.close();

                    }
                }
            } catch (Throwable ex) {
                ldapContext.close();
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;

                throw new Exception(ex.getMessage());

            }
            if (userFound) {
                String baseName = "," + searchBase;

                try {
                    logger.info("Updating password...");
                    String quotedPassword = "\"" + Password + "\"";

                    char unicodePwd[] = quotedPassword.toCharArray();

                    byte pwdArray[] = new byte[unicodePwd.length * 2];

                    for (int i = 0; i
                            < unicodePwd.length; i++) {
                        pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
                        pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);

                    }
//                    System.out.print("encoded password: ");
//                    for (int i = 0; i < pwdArray.length; i++) {
//                        System.out.print(pwdArray[i]);
//                    }

                    ModificationItem[] mods = new ModificationItem[1];
                    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("UnicodePwd", pwdArray));

                    ldapContext.modifyAttributes("cn=" + commonName + baseName, mods);
                    logger.info(UserId + " password reset sucessful for " + serverName);
                    ldapContext.close();

                } catch (Exception e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;

                    throw new Exception("Error in reseting password : " + e.getMessage());

                }
                break;

            } else {
                //multilanguage code
                String text = getTranslatedText("ProcessRPRequestmsg2", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User ID not found in Active Directory server.";
                }
                throw new Exception(text + " : " + serverName);

            }

        }// end of Servers for loop
        return "";

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
                    text = RPConfig.getSupportEmailLangMap().get(selectedLang).get(textID);
                } catch (Exception e) {
                    e.toString();
                    logger.error(e.toString());
                    logger.info("SupportPage Error in translation  for " + textID);
                    return textID;
                }
                if (text != null && text.trim().length() > 0) {
                    logger.info(" getTranslatedText() method ends..");
                    return text;
                }
            } else {
                logger.info("SupportPage Error in translation  for " + textID);
                return textID;
            }
        }
        logger.info("SupportPage Error in translation  for " + textID);
        return textID;
    }
}
