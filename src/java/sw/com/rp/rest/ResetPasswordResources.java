/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dao.ProcessRPRequest;
import sw.com.rp.dao.ResetPass;
import sw.com.rp.dao.ValidatePassword;
import sw.com.rp.dto.MultipleSystemDTO;
import sw.com.rp.dto.ResetPassRequest;
import sw.com.rp.transformer.JsonTransformer;

/**
 *
 * @author msaini
 */
@Path("PassReset")
public class ResetPasswordResources {

    static final Logger logger = LogManager.getLogger(ConfigSyncResource.class.getName());

    /**
     * Retrieves representation of an instance of com.sw.rp.rest.RPRequestResource
     * @return an instance of java.lang.String
     */
    @POST
    @Produces("application/json")
    public String getRPRequest(ResetPassRequest reqData, @Context HttpServletRequest request) {
        //TODO return proper representation object
        HttpSession session = request.getSession(false);
        String Sys = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
            Sys = RPConfig.getSingleHostSystem().getName();
        } else {
            try {
                Sys = (String) session.getAttribute("SYS");
            } catch (Exception e) {
                logger.error(e.getMessage());
                //multilanguage code
                String text = getTranslatedText("PassResetmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";

            }

        }

        if (Sys == null || Sys.trim().length() == 0) {
            //multilanguage code
            String text = getTranslatedText("PassResetmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "Please select system";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }

        ResetPassRequest resp = new ResetPassRequest();
        ResetPassRequest respbck = new ResetPassRequest();
        ResetPass rnp = new ResetPass();
//        String reqData = request.getParameter("reqData");
        JsonTransformer t = new JsonTransformer();
        ArrayList<ResetPassRequest> rpRequest = null;
        try {

            rpRequest = (ArrayList<ResetPassRequest>) JsonTransformer.transformToJavaObjects(reqData, ResetPassRequest.class);
            logger.info(rpRequest.toString());
            resp = rpRequest.get(0);
            String selectedSys = (String) session.getAttribute("SYS");
            resp.setSapSystem(selectedSys);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }
        String sysKey = "";

        sysKey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
        String userID = resp.getUserID();
        String newpass = resp.getNewPass();
        respbck = resp;
        if (resp.getPassSync().equalsIgnoreCase("X")) {

            try {
                String msg[] = new String[RPConfig.getSapSystemList().size()];
                        resp = respbck;
                        String rfc = RPConfig.getSapSystemMap().get(Sys).getRfcname();
                        String length = RPConfig.getMaxlengthMap().get(rfc);
                        logger.info(rfc+ "max pwd len "+length);
                        int len = Integer.parseInt(length);
                        if (resp.getNewPass().length() <= 6) {
                            //multilanguage code
                            String text = getTranslatedText("PassResetmsg3", session);
                            if (text == null || text.trim().length() == 0) {
                                text = "Password too short";
                            }
                            return "{\"message\" : \" " + text + " \",\"success\" : false}";
                        } else if (resp.getNewPass().length() > len) {
                            //multilanguage code
                            String text = getTranslatedText("PassResetmsg4", session);
                            if (text == null || text.trim().length() == 0) {
                                text = "Password too long";
                            }
                            return "{\"message\" : \" " + text + " \",\"success\" : false}";
                        } else {
                            resp = rnp.resetPassword(resp, sysKey, session);
                        }
//                }
              
               
            } catch (Exception ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                resp.setSuccess(false);
                resp.setMessage(ex.getMessage());
            } catch (Throwable ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                resp.setSuccess(false);
                resp.setMessage(ex.getMessage());

            }
        } else {
            try {
                SapSystem isystem = RPConfig.getSapSystemMap().get(Sys);
                if (isystem.getENFlag().equalsIgnoreCase("AD")) {
                    ValidatePassword vpass = new ValidatePassword();
                    String result = vpass.validateNewPass(resp.getNewPass(), session);
                    if (result.equalsIgnoreCase("Success")) {
                        logger.info("Inside AD");
                        ProcessRPRequest prp = new ProcessRPRequest();
                        try {
                            prp.resetActiveDirectoryPassword(resp.getUserID(), resp.getNewPass(), session);
                            //multilanguage code
                            String text = getTranslatedText("PassResetmsg2", session);
                            if (text == null || text.trim().length() == 0) {
                                text = "Your password has been successfully reset";
                            }
                            return "{\"message\" : \" " + text + "\",\"success\" : true}";
                        } catch (Throwable ex) {
                            StringWriter stack = new StringWriter();
                            ex.printStackTrace(new PrintWriter(stack));
                            logger.error(stack.toString());
                            stack = null;
                            resp.setSuccess(false);
                            resp.setMessage(ex.getMessage());
                        }
                    } else {
                        resp.setMessage(result);
                        resp.setSuccess(false);
                        return t.transformToJson(resp);
                    }
                } else {
                    
                    String length;
                     if (RPConfig.getConfig_flag().isMULTI_HOST()) {
                        length = rnp.getPasswordLength(sysKey, session);
                     }else{
                        String rfc = RPConfig.getSapSystemMap().get(Sys).getRfcname();
                        length = RPConfig.getMaxlengthMap().get(rfc);
                     }
                     logger.info(RPConfig.getSapSystemMap().get(Sys).getRfcname()+ "max pwd len "+length);
                    int len = Integer.parseInt(length);
                    if (resp.getNewPass().length() <= 6) {
                        //multilanguage code
                        String text = getTranslatedText("PassResetmsg3", session);
                        if (text == null || text.trim().length() == 0) {
                            text = "Password too short";
                        }
                        return "{\"message\" : \" " + text + " \",\"success\" : false}";
                    } else if (resp.getNewPass().length() > len) {
                        //multilanguage code
                        String text = getTranslatedText("PassResetmsg4", session);
                        if (text == null || text.trim().length() == 0) {
                            text = "Password too long";
                        }
                        return "{\"message\" : \" " + text + " \",\"success\" : false}";
                    } else {
                        resp = rnp.resetPassword(resp, sysKey, session);
                    }
                }
            } catch (Exception ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                resp.setSuccess(false);
                resp.setMessage(ex.getMessage());
            } catch (Throwable ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                resp.setSuccess(false);
                resp.setMessage(ex.getMessage());

            }
        }
        String jsonData = "";
        try {
            jsonData = t.transformToJson(resp);
            return jsonData;
        } catch (Exception ex) {
            resp.setSuccess(false);
            resp.setMessage(ex.getMessage());
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
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
}
