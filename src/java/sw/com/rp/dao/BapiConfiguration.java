/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SAPConnection;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dto.Config_flag;

/**
 *
 * @author msaini
 */
public class BapiConfiguration {

    JCoFunction function;
    SAPConnection connection;
    private static final Logger logger = LogManager.getLogger(BapiConfiguration.class.getName());

    /**
     * This method is used to Read workflow and property value bapi
     * /PSYNG/RR_GET_CONFIG
     *
     * @param string system
     * @return Message Successfull or not .
     */
    public String setPropFromBapi(String _system) throws Throwable {
        logger.info("setPropFromBapi() method start...");
        String language = "EN";
        RPConfig.getPropertyList().add(new Config_flag());//if connection fails
        RPConfig.getPropertyList().get(0).setHIDE_CONFIG(true);//show config tab if connection fails
        connection = new SAPConnection(language);
        try {

            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));

            function = connection.getFunction("/PSYNG/RP_GET_CONFIG");

            connection.execute(function);

            connection.release();
            connection = null;

            JCoTable resulttb = null;
            RPConfig.getPropertyList().clear();
            RPConfig.getPropertyList().add(new Config_flag());

            //property table
            JCoTable protb = null;
            protb = function.getTableParameterList().getTable("ET_CONFIG");
            if (protb.getNumRows() == 0) {
                logger.error("table is empty");
                throw new Exception("table is empty");
            } else {
                for (int i = 0; i < protb.getNumRows(); i++) {
                    protb.setRow(i);
                    String name = protb.getString("PARAM");
                    String value = protb.getString("VALUE");
                    String defval = protb.getString("DEFAULT");

                    //if value is null then set default value
                    if (value.trim().length() == 0) {
                        value = defval;
                    }
                    if (name.equalsIgnoreCase("SAP_SET_PASSWORD_BY_USER")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setResetPOPUP(false);
                            RPConfig.getConfig_flag().setResetPOPUP(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setResetPOPUP(true);
                            RPConfig.getConfig_flag().setResetPOPUP(true);
                        }
                    } else if (name.equalsIgnoreCase("SAP_SEND_PASSWORD_BY_EMAIL")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setSAP_SEND_PASSWORD_BY_EMAIL(false);
                            RPConfig.getConfig_flag().setSAP_SEND_PASSWORD_BY_EMAIL(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setSAP_SEND_PASSWORD_BY_EMAIL(true);
                            RPConfig.getConfig_flag().setSAP_SEND_PASSWORD_BY_EMAIL(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_HIDE_USER_NAME")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setHIDE_NAME(true);
                            RPConfig.getConfig_flag().setHIDE_NAME(true);
                        } else {
                            RPConfig.getPropertyList().get(0).setHIDE_NAME(false);
                            RPConfig.getConfig_flag().setHIDE_NAME(false);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_HIDE_USER_EMAIL")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setHIDE_EMAIL(true);
                            RPConfig.getConfig_flag().setHIDE_EMAIL(true);
                        } else {
                            RPConfig.getPropertyList().get(0).setHIDE_EMAIL(false);
                            RPConfig.getConfig_flag().setHIDE_EMAIL(false);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_SET_EMAIL_QUES_LINK")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setEMAIL_QUES_LINK(false);
                            RPConfig.getConfig_flag().setEMAIL_QUES_LINK(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setEMAIL_QUES_LINK(true);
                            RPConfig.getConfig_flag().setEMAIL_QUES_LINK(true);
                        }
                    } else if (name.equalsIgnoreCase("SAP_SET_SYNC_PASSWORD_ALL_SYS")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setPassword_SYNC(false);
                            RPConfig.getConfig_flag().setPassword_SYNC(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setPassword_SYNC(true);
                            RPConfig.getConfig_flag().setPassword_SYNC(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_HELP_PAGE")) {
                        RPConfig.getPropertyList().get(0).setHELP_PAGE(value);
                        RPConfig.getConfig_flag().setHELP_PAGE(value);
                    } else if (name.equalsIgnoreCase("SAP_CNF_URL")) {
                       // RPConfig.getPropertyList().get(0).setRP_URL(value);
                      //  RPConfig.getConfig_flag().setRP_URL(value);
                        
                        RPConfig.getPropertyList().get(0).setRP_URL("http://localhost:8084/RP");
                        RPConfig.getConfig_flag().setRP_URL("http://localhost:8084/RP");
                        
                    } else if (name.equalsIgnoreCase("WEB_CNF_SUPPORT_EMAIL")) {
                        RPConfig.getPropertyList().get(0).setSUPPORT_EMAIL(value);
                        RPConfig.getConfig_flag().setSUPPORT_EMAIL(value);
                    } else if (name.equalsIgnoreCase("WEB_HELP_PHONE")) {
                        RPConfig.getPropertyList().get(0).setHELP_PHONE(value);
                        RPConfig.getConfig_flag().setHELP_PHONE(value);
                    } else if (name.equalsIgnoreCase("WEB_TAB_SUPPORT_DISP")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setHIDE_SUPPORT(true);
                            RPConfig.getConfig_flag().setHIDE_SUPPORT(true);
                        } else {
                            RPConfig.getPropertyList().get(0).setHIDE_SUPPORT(false);
                            RPConfig.getConfig_flag().setHIDE_SUPPORT(false);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_SET_MULTIHOST")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setMULTI_HOST(false);
                            RPConfig.getConfig_flag().setMULTI_HOST(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setMULTI_HOST(true);
                            RPConfig.getConfig_flag().setMULTI_HOST(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_HTTP_HEADER")) {
                        RPConfig.getPropertyList().get(0).setREQPARAM(value);
                        RPConfig.getConfig_flag().setREQPARAM(value);
                    } else if (name.equalsIgnoreCase("WEB_HELP_EMAIL")) {
                        RPConfig.getPropertyList().get(0).setHELP_EMAIL(value);
                        RPConfig.getConfig_flag().setHELP_EMAIL(value);
                    } else if (name.equalsIgnoreCase("WEB_CNF_GET_SSO")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setGETSSO(false);
                            RPConfig.getConfig_flag().setGETSSO(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setGETSSO(true);
                            RPConfig.getConfig_flag().setGETSSO(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_HELP_DESK")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setHELP_DESK(false);
                            RPConfig.getConfig_flag().setHELP_DESK(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setHELP_DESK(true);
                            RPConfig.getConfig_flag().setHELP_DESK(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_TAB_CONFIG_DISP")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setHIDE_CONFIG(true);
                            RPConfig.getConfig_flag().setHIDE_CONFIG(true);
                        } else {
                            RPConfig.getPropertyList().get(0).setHIDE_CONFIG(false);
                            RPConfig.getConfig_flag().setHIDE_CONFIG(false);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_GREY_UID")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setGREY_UID(false);
                            RPConfig.getConfig_flag().setGREY_UID(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setGREY_UID(true);
                            RPConfig.getConfig_flag().setGREY_UID(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_SSO")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setSSO(false);
                            RPConfig.getConfig_flag().setSSO(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setSSO(true);
                            RPConfig.getConfig_flag().setSSO(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_CNF_JAVA_ENCRYPTION")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setJAVA_ENCRYPTION(false);
                            RPConfig.getConfig_flag().setJAVA_ENCRYPTION(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setJAVA_ENCRYPTION(true);
                            RPConfig.getConfig_flag().setJAVA_ENCRYPTION(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_TAB_USER_DISP")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setHIDE_USER_REG(true);
                            RPConfig.getConfig_flag().setHIDE_USER_REG(true);
                        } else {
                            RPConfig.getPropertyList().get(0).setHIDE_USER_REG(false);
                            RPConfig.getConfig_flag().setHIDE_USER_REG(false);
                        }
                    } else if (name.equalsIgnoreCase("WEB_SET_QUES_ANS_WORKFLOW")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setQUES_ANS_WORKFLOW(false);
                            RPConfig.getConfig_flag().setQUES_ANS_WORKFLOW(false);
                        } else {
                            RPConfig.getPropertyList().get(0).setQUES_ANS_WORKFLOW(true);
                            RPConfig.getConfig_flag().setQUES_ANS_WORKFLOW(true);
                        }
                    } else if (name.equalsIgnoreCase("WEB_SET_PASS_LOGIC")) {
                        if (value.equalsIgnoreCase("N")) {
                            RPConfig.getPropertyList().get(0).setPASS_LOGIC("0");
                            RPConfig.getConfig_flag().setPASS_LOGIC("0");
                        } else {
                            RPConfig.getPropertyList().get(0).setPASS_LOGIC("1");
                            RPConfig.getConfig_flag().setPASS_LOGIC("1");
                        }

                    } else if (name.equalsIgnoreCase("WEB_HELP_CONTACT")) {
                        RPConfig.getPropertyList().get(0).setHELP_CONTACT(value);
                        RPConfig.getConfig_flag().setHELP_CONTACT(value);
                    }

                }
            }
            JCoTable MAX = null;
            MAX = function.getTableParameterList().getTable("ET_PASS_FLD_MAX_LEN");
            if (MAX.getNumRows() == 0) {
                logger.error("ET_PASS_FLD_MAX_LEN table is empty");
                throw new Exception("table is empty");
            } else {
                RPConfig.getMaxlengthMap().clear();
                for (int i = 0; i < MAX.getNumRows(); i++) {
                    MAX.setRow(i);
                    String name = MAX.getString("RFCNAME");
                    String value = MAX.getString("MAX_LEN");
                    logger.info(name+ "max pwd len "+value);
                    RPConfig.getMaxlengthMap().put(name, value);
                }
            }

        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            connection.release();
            connection = null;
            throw new Exception(stack.toString());
        }
        logger.info("setPropFromBapi() method end...");
        return "Success";

    }

    public void setSyncSystemDesc(String sysKey, String language) throws Throwable {
        logger.info("setSyncSystemDesc() method start...");
        connection = new SAPConnection(language);
        try {

            connection.prepareConnection(RPConfig.getAppClientProperties().get(sysKey));

            function = connection.getFunction("/PSYNG/RP_GET_CONFIG");

            connection.execute(function);

            connection.release();
            connection = null;

        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            connection.release();
            connection = null;
            throw new Exception(ex.getMessage());
        }
        JCoTable systemDesc = null;
        systemDesc = function.getTableParameterList().getTable("ET_SYSTEMS");
        if (systemDesc.getNumRows() == 0) {
            logger.error("ET_SYSTEMS table is empty");
            throw new Exception("table is empty");
        } else {
            String name = "";
            try {

                for (int i = 0; i < systemDesc.getNumRows(); i++) {
                    systemDesc.setRow(i);
                    name = systemDesc.getString("SAPSYSTEM");
                    String desc = systemDesc.getString("DESCRIPTION");

                    RPConfig.getSapSystemMap().get(name).setDescription(desc);
                }

            } catch (Exception e) {
                throw new Exception("This system " + name + " must be same in backend and frontend");

            }
        }
        logger.info("setSyncSystemDesc() method end...");
        return;

    }

    public ArrayList sapSystemHashmapToArray(HashMap sapOrignalmmap) {
        ArrayList<SapSystem> data = new ArrayList<SapSystem>();
        for (Object key : sapOrignalmmap.keySet()) {
            data.add((SapSystem) sapOrignalmmap.get(key));
        }
        return data;
    }
}
