/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * session.setAttribute("GID", userID); retrun type String
 * session.setAttribute("SYS", iSystem); retrun type String
 * session.setAttribute("COUNT", noOfDropDowns); retrun type String
 * session.setAttribute("QuestionMAP", quesMap); return type HashMap<String,POJO_QuesAns>
 * session.setAttribute("EXISTQALIST", existingQuesAnsList); return type ArrayList<POJO_QuesAns>
 */
package sw.com.rp.rest;

import com.google.common.base.CharMatcher;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dao.OtherTabVisDao;
import sw.com.rp.dao.ProcessRPRequest;
import sw.com.rp.dao.SaveUpdateQuesAns;
import sw.com.rp.dao.UserRegistration;
import sw.com.rp.dto.POJO_QuesAns;
import sw.com.rp.dto.RPRequest;
import sw.com.rp.dto.RPResponse;
import sw.com.rp.transformer.JsonTransformer;

/**
 * REST Web Service
 *
 * @author msaini
 */
@Path("UserQA")
public class UserQAResource {

    @Context
    private UriInfo context;
    static final Logger logger = LogManager.getLogger(UserQAResource.class.getName());

    /**
     * Creates a new instance of UserQAResource
     */
    public UserQAResource() {
    }

    /**
     * Retrieves representation of an instance of com.sw.rp.rest.UserQAResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @Path("getError")
    @GET
    @Produces("application/json")
    public String getError(@Context HttpServletRequest request) {
        //TODO return proper representation object
        logger.info("Start of  getError() WebServices method ...");

        String jsonData = "{\"message\" : \" Session destroyed \",\"success\" : false}";
        HttpSession session = request.getSession(false);
        String message = "";
        try {
            message = (String) session.getAttribute("Error");
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            jsonData = "{\"message\" : \" Session destroyed \",\"success\" : false}";
        }

        logger.info("session selected  GID : " + message);
        jsonData = "{\"message\" : \"" + message + "\",\"success\" : true}";
        logger.info("End of  getError() WebServices method ...");
        return jsonData;
    }

    @Path("getLanguage")
    @GET
    @Produces("application/json")
    public String getLanguage(@Context HttpServletRequest request) {
        //TODO return proper representation object
        logger.info("Start of  getLanguage() WebServices method ...");

        String jsonData = "{\"success\":  false}";

        HttpSession session = request.getSession(false);
        String LANG = "";
        if (session != null) {
            try {
                LANG = (String) session.getAttribute("LANG");
            } catch (Exception e) {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                jsonData = "{\"LANG\" : \" Session destroyed \",\"success\" : false}";
            }
            if (LANG == null || LANG.length() == 0) {
                jsonData = "{\"LANG\" : \"" + LANG + "\",\"success\" : false}";
            } else {
                jsonData = "{\"LANG\" : \"" + LANG + "\",\"success\" : true}";
            }
        }
        logger.info("End of  getLanguage() WebServices method ...");
        return jsonData;

    }

    @Path("setLanguageSession")
    @GET
    @Produces("application/json")
    public String setLanguage(@Context HttpServletRequest request) {
        logger.info("Start of  setLanguage() WebServices method ...");
        try {

            String jsonData = "{\"LANG\" : \" EN,\"success\" : false}";
            String Lang = request.getParameter("lang");
            logger.info("session selected  language : " + Lang);
            System.out.println("Creating new Sesssion");
            logger.info("Creating new Sesssion");
            HttpSession session = request.getSession(true);

            if (session.isNew()) {
                if (Lang != null && Lang.length() > 0) {
                    session.setAttribute("LANG", Lang);
                    jsonData = "{\"LANG\" : \" EN,\"success\" : true}";

                }
            } else if (RPConfig.getConfig_flag().isSSO()) {
                try {
                    String lang1 = (String) session.getAttribute("LANG");
                    if (lang1 != null && lang1.length() > 0) {
                        //String lang1 = (String) session.getAttribute("LANG");
                        String setlang = "EN";

                        if (lang1.equalsIgnoreCase("English")) {
                            setlang = "EN";
                        } else if (lang1.equalsIgnoreCase("German")) {
                            setlang = "DE";
                        }
                        jsonData = "{\"LANG\" : \"" + setlang + "\",\"success\" : true}";

                    } else {
                        //if (Lang != null && Lang.length() > 0) {
                        session.setAttribute("LANG", Lang);
                        jsonData = "{\"LANG\" : \" EN,\"success\" : true}";

                        //}
                    }
                } catch (Exception e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;
                    if (Lang != null && Lang.length() > 0) {
                        session.setAttribute("LANG", Lang);
                        jsonData = "{\"LANG\" : \" EN,\"success\" : true}";

                    }
                }

            } else {
                String lang1 = (String) session.getAttribute("LANG");
                String setlang = "EN";
                if (lang1 != null) {
                    if (lang1.equalsIgnoreCase("English")) {
                        setlang = "EN";
                    } else if (lang1.equalsIgnoreCase("German")) {
                        setlang = "DE";
                    } else if (lang1.equalsIgnoreCase("Spanish")) {
                        setlang = "ES";
                    }
                }
                jsonData = "{\"LANG\" : \"" + setlang + "\",\"success\" : true}";

            }
            logger.info("End of  setLanguage() WebServices method ...");
            return jsonData;
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            logger.error(e.getMessage());
            //multilanguage code
            String text = e.getMessage();

            return "";

        }
    }

    @Path("setLanguage")
    @GET
    @Produces("application/json")
    public String setLang(@Context HttpServletRequest request) {
        //TODO return proper representation object
        logger.info("Start of  setLang() WebServices method ...");

        String jsonData = "{\"success\":  false}";
        String Lang = request.getParameter("lang");
        logger.info("session selected  language : " + Lang);
        System.out.println("Creating new Sesssion");
        logger.info("Creating new Sesssion");
        HttpSession session = request.getSession(true);

        if (Lang != null && Lang.length() > 0) {
            session.setAttribute("LANG", Lang);
            jsonData = "{\"success\":  true}";
        } else {
            logger.info("setting default 'English' lang. in esssion");
            session.setAttribute("LANG", "English");
            jsonData = "{\"success\":  true}";
        }

//        for (int i = 0; i < RPConfig.getSapSystemNamelist().size(); i++) {
//            try {
//                String system = RPConfig.getSapSystemNamelist().get(i);
//                SAPConnection connection = new SAPConnection("");
//                if (connection.removeFromDestinationMap(system)) {
//                    logger.info(" system configuration removed in Destination DP Map successfully. ");
//                    //                    return (" Success");
//                } else {
//                    logger.error("Error in updating : " + system + " system, Record not found in destination repository! ");
//                    //                    return ("Error in updating : " + modifysap.getName() + " system, Record not found in destination repository! ");
//                }
//            } catch (Throwable ex) {
//                logger.error("Error in updating : " + ex.getMessage());
//            }
//        }
        logger.info("End of  setLang() WebServices method ...");
        return jsonData;

    }

    @Path("getGID")
    @GET
    @Produces("application/json")
    public String getGID(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  getGID() WebServices method ...");
        response.setContentType("text/html;charset=UTF-8");

        String jsonData = "{\"GID\" : \"Invalid session. Retry with new browser window. \",\"success\" : false}";
        HttpSession session = request.getSession(false);
        String GID = "";
        String userIDApp = "";
        if (session != null) {
            try {
                GID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
                if(session.getAttribute("userIDApp") != null){
                    userIDApp = (String) session.getAttribute("userIDApp");
                }
                
            } catch (Exception e) {
                jsonData = "{\"GID\" : \"Invalid session. Retry with new browser window. \",\"success\" : false}";
            }
            if (GID == null || GID.length() == 0) {
                logger.error("No SSO ID found in session!");
                jsonData = "{\"GID\" : \" Invalid session. Retry with new browser window. \",\"success\" : false}";
            } else {
                jsonData = "{\"GID\" : \"" + GID + "\",\"userIDApp\" : \"" + userIDApp + "\",\"success\" : true}";

                if (request.getParameter("getAuthToken") != null
                        && (request.getParameter("getAuthToken").equals("1") || request.getParameter("getAuthToken").equals("sso"))) {
                    if (request.getParameter("getAuthToken").equals("1") && session.getAttribute("Token") == null) {
                        jsonData = "{\"GID\" : \" Password reset request token is already processed! \",\"success\" : false}";
                    } else {

                        try {
                            String xtoken = generateCSRFToken(32);
                            int cookieMaxAge = request.getSession(false).getMaxInactiveInterval();
                            String cookieContext = request.getServletContext().getContextPath();

                            String cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Strict", "x-csrf-token-rp", xtoken, cookieContext, cookieMaxAge);
                            if (request.isSecure()) {
                                cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Strict", "x-csrf-token-rp", xtoken, cookieContext, cookieMaxAge);
                            }

                            response.addHeader("Set-Cookie", cookieSpec);

                            //session.setAttribute("userdata", rpResponse.getUserID());
                            session.setAttribute("x-csrf-token-rp", xtoken);
                            //session.setAttribute("x-selectedSystem", Sys);

                            EncryptDecrypt rpc = new EncryptDecrypt();

                            String encTID = rpc.encrypt(xtoken);
                            jsonData = "{\"GID\" : \"" + GID + "\",\"userIDApp\" : \"" + userIDApp + "\",\"xTID\" : \"" + encTID + "\",\"success\" : true}";
                        } catch (Exception e) {
                            StringWriter stack = new StringWriter();
                            e.printStackTrace(new PrintWriter(stack));
                            logger.error(stack.toString());
                            stack = null;
                            jsonData = "{\"GID\" : \"auth token error! \",\"success\" : false}";
                        }
                    }

                }
            }

        }

        logger.info("session selected  GID : " + GID);
        logger.info("End of  getGID() WebServices method ...");
        return jsonData;
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

    @Path("setGID")
    @GET
    @Produces("application/json")
    public String setGID(@Context HttpServletRequest request) {
        //TODO return proper representation object
        logger.info("Start of  setGID() WebServices method ...");
        String jsonData = "{\"success\":  false}";
        String userID = request.getParameter("userid");
        String userIDApp = request.getParameter("userIDApp");
        // String iSystem = request.getParameter("system");
        logger.info("session User,SAP : " + userID);
        if (userID != null && userID.length() > 0) {
            HttpSession session = request.getSession(false);
            if (CharMatcher.ascii().matchesAllOf(userID)) {
                session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userID.toUpperCase());
                if(userIDApp != null){
                     session.setAttribute("userIDApp", userIDApp.toUpperCase());
                }
               
            } else {
                session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userID);
                session.setAttribute("userIDApp", userIDApp);
            }
            // session.setAttribute("SYS", iSystem);
            jsonData = "{\"success\":  true}";
        }
        logger.info("End of  setGID() WebServices method ...");
        return jsonData;
    }

    @Path("getSingleHostSys")
    @GET
    @Produces("application/json")
    public String getSiglehostSystem(@Context HttpServletRequest request) {
        //TODO return proper representation object
        logger.info("Start of  getSiglehostSystem() WebServices method ...");
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
        }
        String text = getTranslatedText("getSingleHostSysMsg1", session);
        if (text == null || text.trim().length() == 0) {
            text = "Single host System not Found";
        }
        String jsonData = "{\"System\" : \" " + text + " \",\"success\" : false}";
        String iSystem = "";
        try {
            iSystem = RPConfig.getSingleHostSystem().getName();
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            jsonData = "{\"System\" : \" " + text + " \",\"success\" : false}";
        }
        logger.info("session selected  System : " + iSystem);
        if (iSystem != null && iSystem.length() > 0) {
            // HttpSession session = request.getSession(false);
//            if (session.isNew() == true) {
//                // session.invalidate();
//                System.out.println("Creating new Sesssion");
//                session = request.getSession(true);
//            }
            session.setAttribute("SYS", iSystem);
        }

        logger.info("session selected  System : " + iSystem);
        jsonData = "{\"System\" : \"" + iSystem + "\",\"success\" : true}";
        logger.info("End of  getSiglehostSystem() WebServices method ...");
        return jsonData;
    }

    @Path("getSystem")
    @GET
    @Produces("application/json")
    public String getSystem(@Context HttpServletRequest request) {
        //TODO return proper representation object
        logger.info("Start of  getSystem() WebServices method ...");

        String jsonData = "{\"System\" : \" System not Found \",\"success\" : false}";
        HttpSession session = request.getSession(false);
        String iSystem = "";
        try {
            iSystem = (String) session.getAttribute("SYS");
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            String text = getTranslatedText("getSystem", session);
            if (text == null || text.trim().length() == 0) {
                text = "System not Found";
            }
            jsonData = "{\"System\" : \"" + text + " \",\"success\" : false}";
        }

        logger.info("session selected  System : " + iSystem);

        String text1 = getTranslatedText("System", session);
        if (text1 == null || text1.trim().length() == 0) {
            text1 = "Selected System";
        }
        jsonData = "{\"System\" : \"" + iSystem + "\",\"success\" : true}";
        logger.info("End of  getSystem() WebServices method ...");
        return jsonData;
    }

    @Path("setSystem")
    @GET
    @Produces("application/json")
    public String setSystem(@Context HttpServletRequest request) {
        //TODO return proper representation object

        logger.info("Start of  setSystem() WebServices method ...");
        String jsonData = "{\"success\":  false}";
        String iSystem = request.getParameter("system");
        logger.info("session selected  System : " + iSystem);
        HttpSession session = request.getSession(true);
        String n = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
        System.out.println("n===" + n);
        if (iSystem != null && iSystem.length() > 0) {
            // HttpSession session = request.getSession(true);
            // session = request.getSession(true);
//            if (session.isNew() == false) {
//                // session.invalidate();
//                System.out.println("Creating new Sesssion");
//                session = request.getSession(true);
//            }
            String n1 = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
            System.out.println("n1===" + n1);
            session.setAttribute("SYS", iSystem);
            jsonData = "{\"success\":  true}";
        }
        logger.info("End of  setSystem() WebServices method ...");
        return jsonData;
    }

    @Path("getQA")
    @GET
    @Produces("application/json")
    public String getQesAns(@Context HttpServletRequest request) {
        logger.info("Start of  getQesAns() WebServices method ...");
        ListWrapper<POJO_QuesAns> w = new ListWrapper<POJO_QuesAns>();
        HttpSession session = request.getSession(false);
        String userID = "";
        String isystem = "";
        HashMap<String, Object> userRegistrationMap = null;
        ArrayList<POJO_QuesAns> quesAnsList = null;
        ArrayList<POJO_QuesAns> existingQuesAnsList = null;
        if (session != null) {
//            session.getAttributeNames();
            logger.info("Using Existing Session");
            try {
                userID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
            } catch (Exception e) {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                logger.error(e.getMessage());
                //multilanguage code
                String text = getTranslatedText("getQAmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User ID is missing!";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
            if (userID == null || userID.trim().length() == 0) {
                //multilanguage code
                String text = getTranslatedText("getQAmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User ID is missing!";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
            w.setUserid(userID);
            logger.info("Session User : " + userID);
//            isystem = (String) session.getAttribute("SYS");
            if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
                isystem = RPConfig.getSingleHostSystem().getName();
            } else {
                try {
                    isystem = (String) session.getAttribute("SYS");
                } catch (Exception e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;
                    logger.error(e.getMessage());
                    String text = getTranslatedText("getQAmsg2", session);
                    if (text == null || text.trim().length() == 0) {
                        text = "Please select system";
                    }
                    return "{\"message\" : \"" + text + "\",\"success\" : false}";
                }
            }
            if (isystem == null || isystem.trim().length() == 0) {
                String text = getTranslatedText("getQAmsg2", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
            //System.out.println(""+PAConfig.getSapSystem().toString());
        } else {
            if (userID == null || userID.trim().length() == 0) {
                //multilanguage code
                String text = getTranslatedText("getQAmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User ID is missing!";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
            if (isystem == null || isystem.trim().length() == 0) {
                //multilanguage code
                String text = getTranslatedText("getQAmsg2", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
        }
        String Syskey = RPConfig.getSapSystemMap().get(isystem).getSystemKey();
        SapSystem sapSystem = RPConfig.getSapSystemMap().get(isystem);
        try {
            UserRegistration reg = new UserRegistration();

            userRegistrationMap = reg.getQuesAns(Syskey, userID, session);
            quesAnsList = (ArrayList<POJO_QuesAns>) userRegistrationMap.get("1");
            existingQuesAnsList = (ArrayList<POJO_QuesAns>) userRegistrationMap.get("3");
            HashMap<String, POJO_QuesAns> quesMap = new HashMap<String, POJO_QuesAns>();
            for (POJO_QuesAns quesAns : quesAnsList) {
                quesMap.put(quesAns.getQuestionID(), quesAns);
            }
            String noOfDropDowns = (String) userRegistrationMap.get("2");
            session.setAttribute("COUNT", noOfDropDowns);
            session.setAttribute("QuestionMAP", quesMap);
            session.setAttribute("EXISTQALIST", existingQuesAnsList);
            // default value of select
//            POJO_QuesAns defque= new POJO_QuesAns();
//            defque.setAnswers("");
//            defque.setQuestion("Select");
//            defque.setQuestionID("Select");
//            quesAnsList.add(defque);
            if (quesAnsList != null) {
                System.out.println("QuesAns LIST SIZE : " + quesAnsList.size());
                w.setData(quesAnsList);
                w.setSuccess(Boolean.TRUE);
                w.setTotal(quesAnsList.size());
                if (quesAnsList.size() < 1) {
                    //multilanguage code
                    String text = getTranslatedText("getQAmsg3", session);
                    if (text == null || text.trim().length() == 0) {
                        text = "No questions maintained in RP SAP box.";
                    }
                    w.setMessage(text);
                }
//            w.setMessage(userID);

            } else if (quesAnsList == null) {
                w.setSuccess(Boolean.TRUE);
                w.setTotal(0);
                //multilanguage code
                String text = getTranslatedText("getQAmsg4", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Error in getting Questions from RP SAP box.";
                }
                w.setMessage(text);
            }

            String jsonData = "";
            JsonTransformer transformer = new JsonTransformer();
            try {
                jsonData = transformer.transformToJson(w);
            } catch (Exception ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
            }
            //System.out.println("Owner INBOX : \n"+jsonData);
            logger.info("End of  getQesAns() WebServices method ...");
            return jsonData;

        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        return null;
    }

    @Path("getEQA")
    @GET
    @Produces("application/json")
    public String getExistingQesAns(@Context HttpServletRequest request) {
        logger.info("Start of  getExistingQesAns() WebServices method ...");
        HttpSession session = request.getSession(false);
        ArrayList<POJO_QuesAns> existingQuesAnsList = (ArrayList<POJO_QuesAns>) session.getAttribute("EXISTQALIST");
        ListWrapper<POJO_QuesAns> w = new ListWrapper<POJO_QuesAns>();
        if (existingQuesAnsList != null) {
            logger.info("Existing QuesAns LIST SIZE : " + existingQuesAnsList.size());
            w.setData(existingQuesAnsList);
            w.setSuccess(Boolean.TRUE);
            w.setTotal(existingQuesAnsList.size());
            if (existingQuesAnsList.size() < 1) {
                //multilanguage code
                String text = getTranslatedText("getEQAmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User Login first Time.";
                }
                w.setMessage(text);
            }
//            w.setMessage(userID);

        } else if (existingQuesAnsList == null) {
            w.setData(new ArrayList<POJO_QuesAns>());
            w.setSuccess(Boolean.TRUE);
            w.setTotal(0);
            //multilanguage code
            String text = getTranslatedText("getEQAmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "User Login first Time.";
            }
            w.setMessage(text);
        }
        String jsonData = "";
        JsonTransformer transformer = new JsonTransformer();
        try {
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return ex.toString();
        }
        //System.out.println("Owner INBOX : \n"+jsonData);
        logger.info("End of  getExistingQesAns() WebServices method ...");
        return jsonData;

    }

    @Path("getCount")
    @GET
    @Produces("application/json")
    public String getCount(@Context HttpServletRequest request) {
        logger.info("Start of  getCount() WebServices method ...");
        HttpSession session = request.getSession(false);
        String count = "";
        try {
            count = session.getAttribute("COUNT").toString();
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"count\" : None ,\"success\" : false}";
        }

        logger.info("COUNT - " + count);
        if (count == null || count.length() == 0) {
            return "{\"count\" :  None , \"success\" : false}";
        } else {
            logger.info("End of  getCount() WebServices method ...");
            return "{\"count\" : " + count + ", \"success\" : true}";
        }
    }

    @Path("doLogOut")
    @GET
    @Produces("application/json")
    public String doLogout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        logger.info("Start of  doLogout() WebServices method ...");
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("LANG") != null) {
                    String selectedLang = session.getAttribute("LANG").toString();
                    session.invalidate();
                    session = request.getSession(true);
                    System.out.println("new system" + session.isNew());
                    session.setAttribute("LANG", selectedLang);
                } else {
                    session.setAttribute("LANG", "English");//default
                }
            } else {
                session = request.getSession(true);
                session.setAttribute("LANG", "English");//default
            }
            //multilanguage code
            String text = getTranslatedText("logoff", session);
            if (text == null || text.trim().length() == 0) {
                text = "You have logged out successfully.";
            }
            String token = null;
            int cookieMaxAge = 0;
            String cookieContext = request.getServletContext().getContextPath();

            String cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
            if (request.isSecure()) {
                cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
            }
            response.addHeader("Set-Cookie", cookieSpec);

            response.addHeader("sessionAlive", "N"); //stop UI session timer

            if (session != null) {
                session.setAttribute("userdata", null);
                session.setAttribute("x-csrf-token-rp", null);
                session.setAttribute("x-selectedSystem", null);
            }
//            String contextPath = request.getContextPath();
//            logger.info("context path : " + contextPath);
//            response.sendRedirect(response.encodeRedirectURL(contextPath));
            logger.info("End of  doLogout() WebServices method ...");
            return "{\"message\" : \"" + text + "\",\"success\" : true}";
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            logger.error(e.getMessage());
            //multilanguage code
            String text = e.getMessage();

            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }

    }

//JSON.stringify(
    @Path("submitQAns")
    @POST
    @Produces("application/json")
    public String submitQAns(ArrayList<POJO_QuesAns> submitdata, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        logger.info("Start of  submitQAns() WebServices method ...");
        SaveUpdateQuesAns suqa = new SaveUpdateQuesAns();
//        HashMap<String, POJO_QuesAns> quesMap = new HashMap<String, POJO_QuesAns>();
        HttpSession session = request.getSession(false);
        String isystem = "";
        String userID = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
            isystem = RPConfig.getSingleHostSystem().getName();
        } else {
            try {
                isystem = (String) session.getAttribute("SYS");
            } catch (Exception e) {
                logger.error(e.getMessage());
                //multilanguage code
                String text = getTranslatedText("submitQAnsmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
        }
        if (isystem == null || isystem.trim().length() == 0) {
            //multilanguage code
            String text = getTranslatedText("submitQAnsmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "Please select system";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        try {
            userID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            logger.error(e.getMessage());
            //multilanguage code
            String text = getTranslatedText("submitQAnsmsg2", session);
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        if (userID == null || userID.trim().length() == 0) {
            //multilanguage code
            String text = getTranslatedText("submitQAnsmsg2", session);
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        HashMap<String, POJO_QuesAns> quesMap = (HashMap<String, POJO_QuesAns>) session.getAttribute("QuestionMAP");
        try {
            ArrayList<POJO_QuesAns> QuesAnsData = (ArrayList<POJO_QuesAns>) JsonTransformer.transformToJavaObjects(submitdata, POJO_QuesAns.class);
            for (int i = 0; i < QuesAnsData.size(); i++) {
                String Ans = QuesAnsData.get(i).getAnswers();
                String ID = QuesAnsData.get(i).getQuestionID();
                if (Ans == null || Ans.trim().length() == 0) {
                    QuesAnsData.remove(i);

                    --i;

                } else {
                    String Ques = QuesAnsData.get(i).getQuestion();
                    if (Ques == null || Ques.trim().length() == 0) {
                        String ques = quesMap.get(ID).getQuestion();
                        QuesAnsData.get(i).setQuestion(ques);
                    }
//                    System.out.println("arraysize= "+QuesAnsData.size());
//                    System.out.println("i value= "+i);
                }
            }
            String Syskey = RPConfig.getSapSystemMap().get(isystem).getSystemKey();
            String output = suqa.saveQuesAns(Syskey, userID, QuesAnsData, session);
            if (output.equalsIgnoreCase("success")) {

                if (!RPConfig.getPropertyList().get(0).isSSO()) {
                    String token = null;
                    int cookieMaxAge = 0;
                    String cookieContext = request.getServletContext().getContextPath();

                    String cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                    if (request.isSecure()) {
                        cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                    }
                    response.addHeader("Set-Cookie", cookieSpec);

                    response.addHeader("sessionAlive", "N"); //stop UI session timer
                }

                //multilanguage code
                String text = getTranslatedText("submitQAnsmsg3", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Thank you, you are now set up to use Self Service Password Reset.";
                }
                logger.info("End of  submitQAns() WebServices method ...");
                return "{\"message\" : \"" + text + "\",\"success\" : true}";
            } else {
                //multilanguage code
                String text = getTranslatedText("submitQAnsmsg4", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Error in saving/updating";
                }
                logger.info("End of  submitQAns() WebServices method ...");
                return "{\"message\" : \"" + text + "\",\"success\" : true}";
            }
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            //multilanguage code
            String text = getTranslatedText("submitQAnsmsg4", session);
            if (text == null || text.trim().length() == 0) {
                text = "Error in saving/updating";
            }
            logger.info("End of  submitQAns() WebServices method ...");
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }

    }
//    public String validateDuplicateQuestions(@FormParam("regData") String regData, @Context HttpServletRequest request) {
////        System.out.println("Received RegData : "+regData);
//        HttpSession session = request.getSession(false);
////        String count = session.getAttribute("COUNT").toString();
////        log("COUNT - "+count);
//        POJO_QuesAnsSubmitUpdate quesAns = null;
//        JsonTransformer transformer = new JsonTransformer();
//        try {
//            quesAns = (POJO_QuesAnsSubmitUpdate) transformer.transformToJavaObject(regData, POJO_QuesAnsSubmitUpdate.class);
//            String count = session.getAttribute("COUNT").toString();
//            int _count = Integer.parseInt(count);
//            HashMap<String, String> questionkeyMap = new HashMap<String, String>();
//            if (quesAns != null) {
//                if (quesAns.getQuestion1().trim().length() > 0) {
//                    questionkeyMap.put(quesAns.getQuestion1(), quesAns.getQuestion1());
//                }
//                if (quesAns.getQuestion2().trim().length() > 0) {
//                    questionkeyMap.put(quesAns.getQuestion2(), quesAns.getQuestion2());
//                }
//                if (quesAns.getQuestion3().trim().length() > 0) {
//                    questionkeyMap.put(quesAns.getQuestion3(), quesAns.getQuestion3());
//                }
//                if (quesAns.getQuestion4().trim().length() > 0) {
//                    questionkeyMap.put(quesAns.getQuestion4(), quesAns.getQuestion4());
//                }
//                if (quesAns.getQuestion5().trim().length() > 0) {
//                    questionkeyMap.put(quesAns.getQuestion5(), quesAns.getQuestion5());
//                }
//                if (quesAns.getQuestion6().trim().length() > 0) {
//                    questionkeyMap.put(quesAns.getQuestion6(), quesAns.getQuestion6());
//                }
//            } else {
//                logger.error("ERROR : POJO_QuesAnsSubmitUpdate object quesAns NULL.");
//                return "{\"message\" : \"" + "ERROR : POJO_QuesAnsSubmitUpdate object quesAns NULL." + "\",\"success\" : false}";
//            }
//            if (questionkeyMap.size() == _count) {
//
//                // send QA ro SAP for Save
//                HashMap<String, POJO_QuesAns> quesMap = (HashMap<String, POJO_QuesAns>) session.getAttribute("QuestionMAP");
//                ArrayList<POJO_QuesAns> quesAnsList = new ArrayList<POJO_QuesAns>();
//                if (quesAns.getQuestion1().trim().length() > 0) {
//                    POJO_QuesAns quesAns1 = quesMap.get(quesAns.getQuestion1().trim());
//                    quesAns1.setAnswers(quesAns.getAnswer1());
//                    quesAnsList.add(quesAns1);
//                }
//                if (quesAns.getQuestion2().trim().length() > 0) {
//                    POJO_QuesAns quesAns2 = quesMap.get(quesAns.getQuestion2().trim());
//                    quesAns2.setAnswers(quesAns.getAnswer2());
//                    quesAnsList.add(quesAns2);
//                }
//                if (quesAns.getQuestion3().trim().length() > 0) {
//                    POJO_QuesAns quesAns3 = quesMap.get(quesAns.getQuestion3().trim());
//                    quesAns3.setAnswers(quesAns.getAnswer3());
//                    quesAnsList.add(quesAns3);
//                }
//                if (quesAns.getQuestion4().trim().length() > 0) {
//                    POJO_QuesAns quesAns4 = quesMap.get(quesAns.getQuestion4().trim());
//                    quesAns4.setAnswers(quesAns.getAnswer4());
//                    quesAnsList.add(quesAns4);
//                }
//                if (quesAns.getQuestion5().trim().length() > 0) {
//                    POJO_QuesAns quesAns5 = quesMap.get(quesAns.getQuestion5().trim());
//                    quesAns5.setAnswers(quesAns.getAnswer5());
//                    quesAnsList.add(quesAns5);
//                }
//                if (quesAns.getQuestion6().trim().length() > 0) {
//                    POJO_QuesAns quesAns6 = quesMap.get(quesAns.getQuestion6().trim());
//                    quesAns6.setAnswers(quesAns.getAnswer6());
//                    quesAnsList.add(quesAns6);
//                }
//                String userID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
//                String isystem = (String) session.getAttribute("SYS");
//                if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
//                    isystem = RPConfig.getSingleHostSystem().getSystemName();
//                } else {
//                    isystem = (String) session.getAttribute("SYS");
//                }
//                //System.out.println(""+PAConfig.getSapSystem().toString());
//                SAPsystem sapSystem = RPConfig.getSapSystemMap().get(isystem);
//                SaveUpdateQuesAns suqa = new SaveUpdateQuesAns();
//                try {
//                    String output = suqa.saveQuesAns(sapSystem, userID, quesAnsList);
//                    if (output.equalsIgnoreCase("success")) {
//                        return "{\"message\" : \"" + "Thank you, you are now set up to use Self Service Password Reset." + "\",\"success\" : true}";
//                    } else {
//                        return "{\"message\" : \"" + "Error in saving/updating" + "\",\"success\" : true}";
//                    }
//                } catch (Throwable ex) {
//                    StringWriter stack = new StringWriter();
//                    ex.printStackTrace(new PrintWriter(stack));
//                    logger.error(stack.toString());
//                    stack = null;
//                    return "{\"message\" : \"" + "Error in saving/updating." + "\",\"success\" : false}";
//                }
//
//
//
//            } else {
//                return "{\"message\" : \"" + "Duplicate questions in drop downs are not allowed to submit." + "\",\"success\" : false}";
//            }
//        } catch (Exception ex) {
//            StringWriter stack = new StringWriter();
//            ex.printStackTrace(new PrintWriter(stack));
//            logger.error(stack.toString());
//            stack = null;
//
//
//
//            return "{\"message\" : \"" + "Validation service failed." + "\",\"success\" : false}";
//        }
//
//
//
//
//    }

    /**
     * PUT method for updating or creating an instance of UserQAResource
     *
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
                    text = RPConfig.getUserQAResourceLangMap().get(selectedLang).get(textID);
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

    @Path("validateAD")
    @POST
    @Produces("application/json")
    public String validateAD(@Context HttpServletRequest request) {
        //TODO return proper representation object
        logger.info("Start of  validateAD() WebServices method ...");
        HttpSession session = request.getSession(false);
        RPRequestResource RPmultimessage = new RPRequestResource();
        String Sys = "";
        String selectedSys = "";
        String userID = "";
        try {
            userID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
        } catch (Exception e) {
            logger.error(e.getMessage());
            //multilanguage code
            String text = getTranslatedText("getQAmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        if (userID == null || userID.trim().length() == 0) {
            //multilanguage code
            String text = getTranslatedText("getQAmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        logger.info("Session User : " + userID);
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
            Sys = RPConfig.getSingleHostSystem().getName();
        } else {
            try {
                Sys = (String) session.getAttribute("SYS");

            } catch (Exception e) {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                logger.error(e.getMessage());
                //multilanguage code
                String text = RPmultimessage.getTranslatedText("RPRequestmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";

            }

        }

        if (Sys == null || Sys.trim().length() == 0) {
            //multilanguage code
            String text = RPmultimessage.getTranslatedText("RPRequestmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "Please select system";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        RPResponse rpResponse = new RPResponse();
//        String reqData = request.getParameter("reqData");
        JsonTransformer t = new JsonTransformer();
        RPRequest rpRequest = new RPRequest();
        rpRequest.setUserID(userID);

        ProcessRPRequest prp = new ProcessRPRequest();
//        String system = rpRequest.getSapSystem();
        String Syskey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
        selectedSys = (String) session.getAttribute("SYS"); //selected system
        rpRequest.setSapSystem(selectedSys);
        SapSystem isystem = RPConfig.getSapSystemMap().get(Sys);
        if (isystem.getENFlag().equalsIgnoreCase("AD")) {
            logger.info("Inside AD...");
            if (rpRequest.getUserID() != null && rpRequest.getUserID().toString().trim().length() > 0) {
                try {
                    prp.validateUIDWithAD(rpRequest.getUserID(), session);
                    logger.info("UID validated with AD");
                } catch (Throwable ex) {
                    rpResponse = new RPResponse();
                    rpResponse.setSuccess(false);
                    rpResponse.setMessage(ex.getMessage());
                    if (ex.getMessage().contains("lock")) {
                        rpResponse.setLocked(true);
                    }
                    String jsonData = "";
                    try {
                        jsonData = t.transformToJson(rpResponse);
                    } catch (Exception e1) {
                        StringWriter stack = new StringWriter();
                        e1.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                    }
                    logger.info("End of  validateAD() WebServices method ...");
                    return jsonData;
                }

            }
//            else {
//                try {
//                    prp.validateUIDemailWithAD(rpRequest.getUserID(), rpRequest.getEmailID(), session);
//                    logger.info("UIDemail validated with AD");
//                } catch (Throwable ex) {
//                    rpResponse = new RPResponse();
//                    rpResponse.setSuccess(false);
//                    rpResponse.setMessage(ex.getMessage());
//                    if (ex.getMessage().contains("lock") && RPConfig.getConfig_flag().isHELP_DESK()) {
//                        //multilanguage code
//                        String text = RPmultimessage.getTranslatedText("RPRequestmsg2", session);
//                        if (text == null || text.trim().length() == 0) {
//                            text = "User is locked in active directory.";
//                        }
//                        String msg1 = text;
////                        String msg2 = "";
////                        String msg3 = "";
////                        String msg4 = "";
////                        String msg5 = "";
////                        if (!RPConfig.getConfig_flag().getHELP_PHONE().equalsIgnoreCase("NA")) {
////                            msg2 = "Help Desk Phone Number : " + RPConfig.getConfig_flag().getHELP_PHONE() + "<br>";
////                        }
////                        if (!RPConfig.getConfig_flag().getHELP_CONTACT().equalsIgnoreCase("NA")) {
////                            msg3 = "Contact Name : " + RPConfig.getConfig_flag().getHELP_CONTACT() + "<br>";
////                        }
////                        if (!RPConfig.getConfig_flag().getHELP_EMAIL().equalsIgnoreCase("NA")) {
////                            msg4 = "Email : " + RPConfig.getConfig_flag().getHELP_EMAIL() + "<br>";
////                        }
////                        if (!RPConfig.getConfig_flag().getHELP_PAGE().equalsIgnoreCase("NA")) {
////                            msg5 = "Support Web Page : <a href='" + RPConfig.getConfig_flag().getHELP_PAGE() + "' target='_blank' >Click Here</a>";
////                        }
//                        rpResponse.setMessage(msg1);
//
//
//
//                        rpResponse.setLocked(true);
//                        rpResponse.setHelpDesk(true);
//                    }
//                    String jsonData = "";
//                    try {
//                        jsonData = t.transformToJson(rpResponse);
//                    } catch (Exception e1) {
//                        StringWriter stack = new StringWriter();
//                        e1.printStackTrace(new PrintWriter(stack));
//                        logger.error(stack.toString());
//                        stack = null;
//                    }
//                    return jsonData;
//                }
//
//            }
        }
        rpResponse.setSuccess(true);
        rpResponse.setMessage("");
        String jsonData = "";
        try {
            jsonData = t.transformToJson(rpResponse);
        } catch (Exception e1) {
            StringWriter stack = new StringWriter();
            e1.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        logger.info("End of  validateAD() WebServices method ...");
        return jsonData;

    }

    @Path("requestforother")
    @GET
    @Produces("application/json")
    public String passwordRequestForOtherUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  passwordRequestForOtherUser() WebServices method ...");

        String jsonData = "{\"message\" : \" Session destroyed \",\"success\" : false}";
        HttpSession session = request.getSession(false);
        String userID = "";

        try {
            userID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            jsonData = "{\"message\" : \" Session destroyed \",\"success\" : false}";
        }

        //String hostSystem = RPConfig.getSingleHostSystem().getName();
        String sysKey = RPConfig.getSingleHostSystem().getSystemKey();
        try {

            OtherTabVisDao otherTabVisDao = new OtherTabVisDao();
            boolean isPassSetforOther = otherTabVisDao.validateUserForOtherTab(userID, sysKey, session);
            if (isPassSetforOther) {
                jsonData = "{\"message\" : \" Log in user can reset password for other\",\"success\" : true}";
            } else {
                jsonData = "{\"message\" : \"Log in user can not reset password for other\",\"success\" : false}";
            }
        } catch (Throwable e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            String message = e.getMessage();
            if (message.contains("\"")) {
                message = message.replace('\"', '\'');
            }
            stack = null;
            jsonData = "{\"message\" : \"" + message + "\",\"success\" : false}";
        }

        logger.info("End of  passwordRequestForOtherUser() WebServices method ...");
        return jsonData;
    }
}
