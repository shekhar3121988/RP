/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import sw.com.rp.config.RPConfig;
import sw.com.rp.dao.SupportEmail;
import sw.com.rp.dto.Support;
import sw.com.rp.transformer.JsonTransformer;

/**
 * REST Web Service
 *
 * @author msuppahiya
 */
@Path("support")
public class SupportResource {

    @Context
    private UriInfo context;
    static final Logger logger = LogManager.getLogger(SupportResource.class.getName());

    /** Creates a new instance of SupportResource */
    public SupportResource() {
    }

    /**
     * Retrieves representation of an instance of com.sw.rp.rest.SupportResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Produces("application/json")
    public String getPostJson(Support supportData, @Context HttpServletRequest request) {
        Support support = new Support();
        try {
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
                    String text = getTranslatedText("SupportEmailmsg2", session);
                    if (text == null || text.trim().length() == 0) {
                        text = "Please select system";
                    }
                    return "{\"message\" : \"" + text + "\",\"success\" : false}";
                }
            }
            if (Sys == null || Sys.trim().length() == 0) {
                //multilanguage code
                String text = getTranslatedText("SupportEmailmsg2", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
            ArrayList<Support> supRequest = null;
            try {

                supRequest = (ArrayList<Support>) JsonTransformer.transformToJavaObjects(supportData, Support.class);
                // logger.info(rpRequest.toString());
            } catch (Exception ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";

            }
            String Syskey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
            support = supRequest.get(0);
            support.setSystemName(Syskey);
            String result = "";
            SupportEmail semail = new SupportEmail();
            try {
                result = semail.processSupportRequest(support, session);
                return "{\"message\" : \"" + result + "\",\"success\" : true}";
            } catch (Throwable ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
            }
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }

    }

    /**
     * PUT method for updating or creating an instance of SupportResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
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
