/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import sw.com.rp.config.RPConfig;
import sw.com.rp.dao.CheckLogin;
import sw.com.rp.dto.User;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.transformer.JsonTransformer;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * REST Web Service
 *
 * @author msaini
 */
@Path("login")
public class LoginResource {

    @Context
    private UriInfo context;
    HttpSession session;
    static final Logger logger = LogManager.getLogger(LoginResource.class.getName());

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }

    /**
     * Retrieves representation of an instance of com.sw.pa.rest.LoginResource
     *
     * @return an instance of java.lang.String
     */
    @POST
    @Produces("application/json")
    public String getJson(@FormParam("userid") String useridParam, @FormParam("password") String passwdParam, @Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException, Throwable {
        logger.info("Start of  getJson() WebServices method ...");
        HttpSession session = request.getSession(false);
        String user = useridParam;
        String password = passwdParam;

        String Sys = "";
        String jsonData2 = "";
        try {
            password = new String(Base64.decodeBase64(password), StandardCharsets.UTF_8);
            if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
                Sys = RPConfig.getSingleHostSystem().getName();
            } else {
                try {
                    Sys = (String) session.getAttribute("SYS");
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return "{\"message\" : \"" + "Please select system" + "\",\"success\" : false}";
                }
            }
            if (Sys == null || Sys.trim().length() == 0) {
                return "{\"message\" : \"" + "Please select system" + "\",\"success\" : false}";
            }
            System.out.println("login resource loginData : " + request.getParameter("loginData"));
            CheckLogin login = new CheckLogin();
            User uzer = new User();

            logger.info("requested user logon : " + user);// + ", " + uzer.getPassword());
//        
            SapSystem sapSystem = null;
            boolean isUserFound = false;
            sapSystem = RPConfig.getSapSystemMap().get(Sys);
            String Syskey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
            if (sapSystem.getENFlag().equalsIgnoreCase("AD")) {
                logger.info("Inside AD");
                try {
                    isUserFound = login.validateUserWithAD(user, session);
                } catch (Exception e) {
                    uzer.setSuccess(false);
                    uzer.setMessage(e.getMessage());
                    //String jsonData2 = "";
                    try {
                        JsonTransformer transformer = new JsonTransformer();
                        jsonData2 = transformer.transformToJson(uzer);
                    } catch (Exception ex) {
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";

                    }
                    return jsonData2;
                }
                if (!isUserFound) {

                    uzer.setSuccess(false);
                    uzer.setMessage("User not found in active directory.");
                    //String jsonData2 = "";
                    try {
                        JsonTransformer transformer = new JsonTransformer();
                        jsonData2 = transformer.transformToJson(uzer);
                    } catch (Exception ex) {
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";

                    }
                    return jsonData2;
                }

            }
            try {
                uzer = login.checkUserLogin(user, password, Syskey, session);
            } catch (Exception e) {
                //Logger.getLogger(LoginResource.class.getName()).log(Level.SEVERE, null, e);
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                return "{\"message\" : \"" + e.getMessage() + "\",\"success\" : false}";

            }

            if (uzer.isSuccess()) {
                String token = generateCSRFToken(32);
                int cookieMaxAge = request.getSession(false).getMaxInactiveInterval();
                String cookieContext = request.getServletContext().getContextPath();

                String cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                if (request.isSecure()) {
                    cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                }

                response.addHeader("Set-Cookie", cookieSpec);

                session.setAttribute("userdata", uzer.getUserid());
                session.setAttribute("x-csrf-token-rp", token);
                session.setAttribute("x-selectedSystem", Sys);

                EncryptDecrypt rpc = new EncryptDecrypt();

                String encTID = rpc.encrypt(token);
                uzer.setxTID(encTID);
            }

            JsonTransformer transformer = new JsonTransformer();
            jsonData2 = transformer.transformToJson(uzer);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";

        }
        logger.info("End of  getJson() WebServices method ...");
        return jsonData2;
    }

    /**
     * PUT method for updating or creating an instance of LoginResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
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
//   
}
