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
import sw.com.rp.dto.Support;

/**
 *
 * @author msuppahiya
 */
public class SupportEmail {

    JCoFunction function;
    JCoTable codes;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(SupportEmail.class.getName());

    public SupportEmail() {
        this.function = null;
        this.codes = null;

        this.connection = null;
    }

    public String processSupportRequest(Support support, HttpSession session) throws Exception, Throwable {
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
        String _system = support.getSystemName();

        try {
            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));


            function = connection.getFunction("/PSYNG/RP_010");

            JCoParameterList inputList = function.getImportParameterList();
            inputList.setValue("TEXT", support.getComments());// Set Data
            inputList.setValue("TEXTSUBJECT", support.getSubject());
            inputList.setValue("FROMEMAIL", support.getEmail());
            inputList.setValue("SUPPORTEMAIL", RPConfig.getConfig_flag().getSUPPORT_EMAIL());

            logger.info("Executing bapi /PSYNG/RP_010");
            connection.execute(function);

            connection.release();
            connection = null;
            String text = getTranslatedText("SupportEmailmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "Message sent successfully.";
            }
            return text;



        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            function = null;
            connection = null;
            throw new Exception(ex.getMessage());
        }

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
}
