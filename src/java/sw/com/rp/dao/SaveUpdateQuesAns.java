/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import com.google.common.base.CharMatcher;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import java.util.ArrayList;
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
public class SaveUpdateQuesAns {

    JCoFunction function;
    JCoTable codes;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(SaveUpdateQuesAns.class.getName());

    public SaveUpdateQuesAns() {
        this.function = null;
        this.codes = null;

        this.connection = null;
    }

    public String saveQuesAns(String _system, String userID, ArrayList<POJO_QuesAns> quesAnsList, HttpSession session) throws Exception, Throwable {
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
        connection = new SAPConnection(language);
        try {
            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));

            function = connection.getFunction("/PSYNG/RP_003");

            JCoParameterList inputList = function.getImportParameterList();
            logger.info("BAPI /PSYNG/RP_003 : USER " + userID);
            if (CharMatcher.ascii().matchesAllOf(userID)) {
                inputList.setValue("USER", userID.toUpperCase());
            } else {
                inputList.setValue("USER", userID);
            }

            if (RPConfig.getConfig_flag().isJAVA_ENCRYPTION()) {
                inputList.setValue("I_JENCRYPTLOGIC", "Y");
                logger.info("answer decrypted from Java");
            } else {
                inputList.setValue("I_JENCRYPTLOGIC", "N");
                logger.info("answer decrypted using SAP");
            }
            JCoTable quesAnsTable = function.getTableParameterList().getTable("QANA");
            for (POJO_QuesAns quesAns : quesAnsList) {
                quesAnsTable.appendRow();
                quesAnsTable.setValue("QUESTIONID", quesAns.getQuestionID());
                quesAnsTable.setValue("QUESTION", quesAns.getQuestion());
                if (RPConfig.getConfig_flag().isJAVA_ENCRYPTION()) {
                    // Java side encryption
                    logger.info("encrypting answers");
                    EncryptDecrypt rpc = new EncryptDecrypt();
//                    RPConfig rpc = new RPConfig();
                    String ans = "";
                    try {
                        logger.info("Encrypting answers...");
                        ans = rpc.encrypt(quesAns.getAnswers());
                        logger.info("length of encrypted answers" + ans.length());
                    } catch (Exception ex) {
                        logger.error("Error in encrypting answers : ");
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        throw new Exception("Error in encrypting answers");
                    }

                    quesAnsTable.setValue("ANSWER", ans);
                } else {
                    quesAnsTable.setValue("ANSWER", quesAns.getAnswers());
                }
//                log(quesAns.getQuestionID()+"-"+quesAns.getQuestion()+"-"+quesAns.getAnswers());

            }


            logger.error("Executing bapi /PSYNG/RP_003");
            connection.execute(function);

            connection.release();
            connection = null;
            JCoStructure returnStructure =
                    function.getExportParameterList().getStructure("RETURN");
            if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
                throw new Exception(returnStructure.getString("MESSAGE"));
            } else {
                return "success";
            }


        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            quesAnsList = null;
            function = null;
            connection = null;
            throw new Exception(ex.getMessage());
        }
    }
}
