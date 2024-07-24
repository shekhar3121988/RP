/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.dao.AdminManage;
import sw.com.rp.dto.Admininfo;
import static sw.com.rp.rest.RPRequestResource.logger;
import sw.com.rp.transformer.JsonTransformer;

/**
 *
 * @author msaini
 */
@Path("/AdminLogin")
public class AdminLoginResource {

    private static final Logger logger = LogManager.getLogger(AdminLoginResource.class.getName());
    Admininfo adminf = new Admininfo();
    AdminManage adminManage = new AdminManage();
    String json = null;
    String returned = null;

    /**
     *
     * This method is used to Authenticate Admin
     *
     * @param usern
     * @param passwd
     * @param request
     * @param response
     * @param jCaptchaTxt
     * @return Message Authenticated or not
     *
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getResult(@FormParam("adminId") String usern, @FormParam("pass") String passwd, @FormParam("captchaTxt") String jCaptchaTxt, @Context HttpServletRequest request, @Context HttpServletResponse response)
            throws IOException, Throwable {
        logger.info("Start of  getResult() WebServices method ...");

        synchronized (request) {
            try {
                request.wait(1500);//Paralyse Brute force
            } catch (Throwable ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
            }
        }
        HttpSession session = request.getSession(false);
        
        if(session == null){
            return "{\"message\" : \"Client session not available!\",\"success\" : false}";
        }

        if (session.getAttribute("AdminCaptchaStr") != null) {
            if (!session.getAttribute("AdminCaptchaStr").toString().equals(jCaptchaTxt)) {
                logger.info("" + session.getAttribute("AdminCaptchaStr").toString() + "::" + jCaptchaTxt);
                return "{\"message\" : \"Incorrect captcha !\",\"success\" : false}";
            }

        } else {
            logger.info("AdminCaptchaStr not found in session!");
        }
        String userId = usern;
        String pass = passwd;

        try {
            pass = new String(Base64.decodeBase64(pass), StandardCharsets.UTF_8);
            EncryptDecrypt utilEncDec = new EncryptDecrypt();
            String hashedPwd = utilEncDec.getHashedWebAdminPassword(usern.toLowerCase() + pass);
            //System.out.println(hashedPwd);
            if (RPConfig.getAdmininfoMap().containsKey(userId.toLowerCase())) {
                if (!(RPConfig.getAdmininfoMap().get(userId.toLowerCase()).getPassword().equals(hashedPwd))) {
                    String text = getTranslatedText("AdminLoginmsg1", session);
                    //multilanguage code
                    if (text == null || text.trim().length() == 0) {
                        text = "Incorrect User ID or Password !";
                    }

                    return "{\"message\" : \"" + text + "\",\"success\" : false}";
                }
            } else {
                String text = getTranslatedText("AdminLoginmsg1", session);
                //multilanguage code
                if (text == null || text.trim().length() == 0) {
                    text = "Incorrect User ID or Password !";
                }

                return "{\"message\" : \"" + text + "\",\"success\" : false}";

            }
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        String text = getTranslatedText("AdminLoginmsg2", session);
        //multilanguage code
        if (text == null || text.trim().length() == 0) {
            text = "Login successful";
        }
        if (request.getSession(false) == null) {
            session = request.getSession(true);
        }

        String token = "rp_admin_" + generateCSRFToken(32);
        int cookieMaxAge = request.getSession(false).getMaxInactiveInterval();
        String cookieContext = request.getServletContext().getContextPath();

        String cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
        if (request.isSecure()) {
            cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
        }

        response.addHeader("Set-Cookie", cookieSpec);
        session.setAttribute("x-csrf-token-rp", token);
        session.setAttribute("userdata", null);
        session.setAttribute("x-selectedSystem", null);

        EncryptDecrypt rpc = new EncryptDecrypt();

        String encTID = rpc.encrypt(token);
        //rpResponse.setxTID(encTID);

        logger.info("End of  getResult() WebServices method ...");
        return "{\"xTID\" : \"" + encTID + "\",\"success\" : true}";

    }

    /**
     *
     * This method is used to ADD Admin info
     *
     * @param String id
     * @param String Password
     * @return Message Successfuly added or not
     *
     */
    @Path("/ADD")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getADDAdmin(@FormParam("adminId") String usern, @FormParam("pass") String passwd, @Context HttpServletRequest request)
            throws IOException, Throwable {
        logger.info("Start of  getADDAdmin() WebServices method ...");
        HttpSession session = request.getSession(false);
        String userId = usern;
        String pass = passwd;
        try {
            adminf = new Admininfo();
            if (userId.trim().length() == 0) {
                String text = getTranslatedText("AdminLoginmsg3", session);
                //multilanguage code
                if (text == null || text.trim().length() == 0) {
                    text = "username required !";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            } else if (pass.trim().length() == 0) {
                return "{\"message\" : \"" + "password required !" + "\",\"success\" : false}";
            } else if (RPConfig.getAdmininfoMap().containsKey(userId.toLowerCase())) {
                String text = getTranslatedText("AdminLoginmsg4", session);
                //multilanguage code
                if (text == null || text.trim().length() == 0) {
                    text = "Admin User is already in list !";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            } else {

                EncryptDecrypt utilEncDec = new EncryptDecrypt();
                String hashedPwd = utilEncDec.getHashedWebAdminPassword(userId.toLowerCase() + pass);

                adminf.setUserID(userId);
                adminf.setPassword(hashedPwd);

                adminManage.addAdmin(adminf);

            }
        } catch (Exception e) {
            return "{\"message\" : \"" + e.getMessage() + "\",\"success\" : false}";
        }
        String text = getTranslatedText("AdminLoginmsg5", session);
        //multilanguage code
        if (text == null || text.trim().length() == 0) {
            text = "Admin user is added successfully";
        }
        logger.info("End of  getADDAdmin() WebServices method ...");
        return "{\"message\" : \"" + text + "\",\"success\" : true}";

    }

    /**
     *
     * This method is used to Delete Admin info
     *
     * @param String id
     * @param String Password
     * @return Message Successfuly deleted or not
     *
     */
    @Path("/DELETE")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDeleteAdmin(@Context HttpServletRequest request)
            throws IOException, Throwable {
        logger.info("Start of  getDeleteAdmin() WebServices method ...");
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("Id");
        try {
            adminf = new Admininfo();
            if (userId.trim().length() == 0) {
                return "{\"message\" : \"" + "username required !" + "\",\"success\" : false}";
            } else {

                adminManage.deleteAdmin(userId);

            }
        } catch (Exception e) {
            return "{\"message\" : \"" + e.getMessage() + "\",\"success\" : false}";
        }
        String text = getTranslatedText("AdminLoginmsg6", session);
        //multilanguage code
        if (text == null || text.trim().length() == 0) {
            text = "Admin user is deleted  successfully";
        }
        logger.info("End of  getDeleteAdmin() WebServices method ...");
        return "{\"message\" : \"" + text + "\",\"success\" : true}";

    }

    /**
     *
     * This method is used to Modify Admin info
     *
     * @param String id
     * @param String Password
     * @return Message Successfuly modified or not
     *
     */
    @Path("/MODIFY")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getModifyAdmin(@FormParam("adminId") String usern, @FormParam("pass") String passwd, @Context HttpServletRequest request)
            throws IOException, Throwable {
        logger.info("Start of  getModifyAdmin() WebServices method ...");
        HttpSession session = request.getSession(false);
        String userId = usern;
        String pass = passwd;
        try {
            adminf = new Admininfo();
            if (userId.trim().length() == 0) {
                return "{\"message\" : \"" + "username required !" + "\",\"success\" : false}";
            } else if (pass.trim().length() == 0) {
                return "{\"message\" : \"" + "password required !" + "\",\"success\" : false}";
            } else {
                EncryptDecrypt utilEncDec = new EncryptDecrypt();
                String hashedPwd = utilEncDec.getHashedWebAdminPassword(userId.toLowerCase() + pass);

                adminf.setUserID(userId);
                adminf.setPassword(hashedPwd);

                adminManage.modifyAdmin(adminf);

            }
        } catch (Exception e) {
            return "{\"message\" : \"" + e.getMessage() + "\",\"success\" : false}";
        }
        String text = getTranslatedText("AdminLoginmsg7", session);
        //multilanguage code
        if (text == null || text.trim().length() == 0) {
            text = "Admin user is modified successfully";
        }
        logger.info("End of  getModifyAdmin() WebServices method ...");
        return "{\"message\" : \"" + text + "\",\"success\" : true}";

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
                    text = RPConfig.getAdminLoginResourceLangMap().get(selectedLang).get(textID);
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

    private String generateCSRFToken(int size) throws Exception {
        logger.debug("start of generateCSRFToken()");
        SecureRandom sr;
        byte[] random = new byte[size];
        String digest = null;
        sr = SecureRandom.getInstance("SHA1PRNG");
        sr.nextBytes(random);
        digest = Base64.encodeBase64String(random).replaceAll("[^a-zA-Z0-9]", "_");
        logger.debug("end of generateCSRFToken()");
        return digest;
    }
}
