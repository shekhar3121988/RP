/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sw.com.rp.dao;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SAPConnection;
import static sw.com.rp.dao.RequestNewPassword.logger;

/**
 *
 * @author Vivek Kumar
 */
public class OtherTabVisDao {
    
     JCoFunction function;
    JCoTable codes;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(OtherTabVisDao.class.getName());

    public OtherTabVisDao() {
        this.function = null;
        this.codes = null;
        this.connection = null;
    }
    public boolean validateUserForOtherTab(String userID, String system, HttpSession session) throws Throwable {
        logger.info("Start of validateUserForOtherTab ...");
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
            connection.prepareConnection(RPConfig.getAppClientProperties().get(system));

            function = connection.getFunction("/PSYNG/RP_ALLOW_REQ_FOR_OTHERS");

            JCoParameterList inputList = function.getImportParameterList();
            logger.debug("BAPI /PSYNG/RP_ALLOW_REQ_FOR_OTHERS : USERID " + userID);
            inputList.setValue("I_USER_ID", userID);
            

            
            connection.execute(function);

            connection.release();
            connection = null;

             String allowedUser = function.getExportParameterList().getString("E_SUCCESS");
             logger.info("End of validateUserForOtherTab ...");
             if(allowedUser.equalsIgnoreCase("X")){
                 return true;
             }else{
                 return false;
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
    }
    
    
}
