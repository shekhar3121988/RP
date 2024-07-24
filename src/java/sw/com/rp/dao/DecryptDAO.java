/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SAPConnection;
import sw.com.rp.dto.EmailDTO;
import sw.com.rp.dto.RPResponse;

/**
 *
 * @author msainii
 */
public class DecryptDAO {

    JCoFunction function;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(DecryptDAO.class.getName());

    public DecryptDAO() {
        this.function = null;
        this.connection = null;
    }

    public EmailDTO getDecryptResponse(String token, String sys) throws Exception, Throwable {
        function = null;
        connection = null;
        String language = "EN";
        EmailDTO rpResponse = new EmailDTO();
        connection = new SAPConnection(language);
        String _system = "";
        if (RPConfig.getConfig_flag().isMULTI_HOST()) {
            _system = RPConfig.getSapSystemMap().get(sys).getSystemKey();
        } else {
            _system = RPConfig.getSingleHostSystem().getSystemKey();

        }
        try {
            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));

            function = connection.getFunction("/PSYNG/RP_TOKEN_DTL");

            JCoParameterList inputList = function.getImportParameterList();
            logger.error("BAPI /PSYNG/RP_TOKEN_DTL : token " + token);
            inputList.setValue("I_TOKENID", token);

            logger.info("Executing bapi /PSYNG/RP_TOKEN_DTL");
            connection.execute(function);


            JCoStructure returnStructure =
                    function.getExportParameterList().getStructure("E_RETURN");
            if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
                throw new Exception(returnStructure.getString("MESSAGE"));
            }

            JCoTable protb = null;
            protb = function.getTableParameterList().getTable("I_TOKEN");
            String user = protb.getString("UNAME");
            String charurl = protb.getString("CHARURL");
            String system = protb.getString("SYSID");
            String lang = protb.getString("LANGID");
            String requestId = protb.getString("APPLICANT_ID");
            connection.release();
            connection = null;
            rpResponse.setUserid(user);
            rpResponse.setSystem(system);
            rpResponse.setCharurl(charurl);
            rpResponse.setLang(lang);
            rpResponse.setRequestId(requestId);
            return rpResponse;
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
}
