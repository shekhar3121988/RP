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
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SAPConnection;
import sw.com.rp.dto.POJO_QuesAns;
import sw.com.rp.rest.EncryptDecrypt;

/**
 *
 * @author msaini
 */
public class UserRegistration {

    JCoTable codes;
    // JCoClient client;
    JCoFunction function;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(UserRegistration.class.getName());

    public UserRegistration() {
        this.function = null;
        this.codes = null;
        this.connection = null;
    }

    public HashMap<String, Object> getQuesAns(String _system, String userID, HttpSession session) throws Exception, Throwable {
        function = null;
        connection = null;
        HashMap<String, Object> userRegistrationMap = new HashMap<String, Object>();
        ArrayList<POJO_QuesAns> quesAnsList = new ArrayList<POJO_QuesAns>();
        ArrayList<POJO_QuesAns> existingQuesAnsList = new ArrayList<POJO_QuesAns>();
        String numberOfQuestionsToBeShown = "";

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

            function = connection.getFunction("/PSYNG/RP_002");



            JCoParameterList inputList = function.getImportParameterList();

            logger.info("BAPI /PSYNG/RP_002 : USERID " + userID);
            inputList.setValue("USERID", userID);
            if (RPConfig.getConfig_flag().isJAVA_ENCRYPTION()) {
                inputList.setValue("I_JENCRYPTLOGIC", "Y");
            } else {
                inputList.setValue("I_JENCRYPTLOGIC", "N");
            }
            connection.execute(function);

            connection.release();
            connection = null;

        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            quesAnsList = null;
            connection.release();
            connection = null;
            throw new Exception(ex.getMessage());
        }
        try {
            JCoStructure returnStructure =
                    function.getExportParameterList().getStructure("RETURN");
            if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
                throw new Exception(returnStructure.getString("MESSAGE"));
            }
            JCoTable output_QuesTable = function.getTableParameterList().getTable("QUESTIONS");
            if (output_QuesTable.getNumRows() > 0) {

                POJO_QuesAns question = null;
                quesAnsList = new ArrayList<POJO_QuesAns>();
                int rows = output_QuesTable.getNumRows();
                for (int row = 0; row < rows; row++) {
                    if (quesAnsList.isEmpty()) {
                        question = new POJO_QuesAns();
                        question.setAnswers("");
                        question.setQuestion("Select");
                        question.setQuestionID("Select");
                        quesAnsList.add(question);

                    }
                    if (quesAnsList.size() == 1) {
                        question = new POJO_QuesAns();
                        question.setAnswers("");
                        question.setQuestion("Create your own Question");
                        question.setQuestionID("Create");
                        quesAnsList.add(question);

                    }
                    if (row == 0) {
                        logger.info("Reading All Questions from QUESTIONS Table .");
                    }
                    output_QuesTable.setRow(row);
                    question = new POJO_QuesAns();
                    question.setQuestionID(output_QuesTable.getString("QUESTIONID"));
                    question.setQuestion(output_QuesTable.getString("QUESTION"));
                    quesAnsList.add(question);
                    question = null;
                }
            } else {
                logger.info("QUESTIONS table EMPTY");
            }
            JCoParameterList exportParameters = function.getExportParameterList();
            numberOfQuestionsToBeShown = exportParameters.getValue("RANDOM_QUESTIONS").toString().trim();
            JCoTable output_QuesAnsTable = function.getTableParameterList().getTable("QANA");
            if (output_QuesAnsTable.getNumRows() > 0) {
                logger.info("User Login to update Question Answers");
                POJO_QuesAns question = null;
                existingQuesAnsList = new ArrayList<POJO_QuesAns>();
                int rows = output_QuesAnsTable.getNumRows();
                for (int row = 0; row < rows; row++) {
                    if (row == 0) {
                        logger.info("Reading Questions-Answers from QANA Table .");
                    }
                    output_QuesAnsTable.setRow(row);
                    question = new POJO_QuesAns();
                    question.setQuestionID(output_QuesAnsTable.getString("QUESTIONID"));
                    question.setQuestion(output_QuesAnsTable.getString("QUESTION"));
                    if (RPConfig.getConfig_flag().isJAVA_ENCRYPTION()) {
                        logger.info("Decrypting answers...");
                        EncryptDecrypt rpc = new EncryptDecrypt();
//                        RPConfig rpc = new RPConfig();
                        String ans = "";
                        try {
                            String encAns = output_QuesAnsTable.getString("ANSWER");
//                            System.out.println("encAns : "+encAns);
                            ans = rpc.decrypt(encAns);
                        } catch (Exception ex) {
                            logger.error("decrypting error : ");
                            StringWriter stack = new StringWriter();
                            ex.printStackTrace(new PrintWriter(stack));
                            logger.error(stack.toString());
                            stack = null;
                        }
                        question.setAnswers(ans);

                    } else {
                        question.setAnswers(output_QuesAnsTable.getString("ANSWER"));
                    }
                    existingQuesAnsList.add(question);
                    question = null;
                }
            } else {
                logger.info("QANA Table empty : User Login first time to save questions-answers.");
            }
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            quesAnsList = null;

            throw new Exception(ex.getMessage());
        }

        userRegistrationMap.put("1", quesAnsList);
        userRegistrationMap.put(
                "2", numberOfQuestionsToBeShown);
        userRegistrationMap.put(
                "3", existingQuesAnsList);








        return userRegistrationMap;
    }
}
