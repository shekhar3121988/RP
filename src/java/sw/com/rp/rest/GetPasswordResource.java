/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import com.google.common.base.CharMatcher;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.POST;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dao.DecryptDAO;
import sw.com.rp.dao.OtherTabVisDao;
import sw.com.rp.dao.ProcessRPRequest;
import sw.com.rp.dao.RequestNewPassword;
import sw.com.rp.dto.EmailDTO;
import sw.com.rp.dto.RPResponse;
import sw.com.rp.transformer.JsonTransformer;

/**
 * REST Web Service
 *
 * @author msuppahiya
 */
@Path("getPassword")
public class GetPasswordResource {

    @Context
    private UriInfo context;
    static final Logger logger = LogManager.getLogger(GetPasswordResource.class.getName());

    /**
     * Creates a new instance of GetPasswordResource
     */
    public GetPasswordResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.sw.rp.rest.GetPasswordResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public void getPassword(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  getPassword() WebServices method ...");
        String user = "";
        String code = "";
        try {
            request.setCharacterEncoding("UTF-8");
            String queryString = java.net.URLDecoder.decode(request.getQueryString(), "UTF-8");
            System.out.println("queryString: " + queryString);
            request.setAttribute(queryString, queryString);
            int endindex = queryString.indexOf("&charurl");
            int bigin = queryString.indexOf("&charurl=");
            int end = queryString.indexOf("&SYSID");
            user = queryString.substring(0, endindex);
            code = queryString.substring(bigin, end);
            code = code.substring(9);
            user = user.substring(4);
            System.out.println("useruser" + user);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;

        }

        HttpSession session = request.getSession(true);
        if (session.isNew() == false) {
            session.invalidate();
            logger.info("Creating new Sesssion");
            session = request.getSession(true);
        }

        String userid = user;
        if (userid == null) {
            logger.error("usr parameter missing.");
        }

        String passCode = code;
        if (passCode == null) {
            logger.error("charurl parameter missing.");
        }
        String SAPsystem = request.getParameter("SYSID");
        if (SAPsystem == null) {
            logger.error("SYSID parameter missing.");
        }
        String lang = request.getParameter("lang");
        if (lang == null) {
            logger.error("lang parameter missing.");
        }
        session.setAttribute("SYS", SAPsystem);
        if (CharMatcher.ascii().matchesAllOf(userid)) {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid.toUpperCase());
        } else {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid);
        }
        session.setAttribute("passcode", passCode);
        if (lang.equalsIgnoreCase("EN")) {
            session.setAttribute("LANG", "English");
        } else if (lang.equalsIgnoreCase("DE")) {
            session.setAttribute("LANG", "German");
        }
        if ((RPConfig.getConfig_flag().isPassword_SYNC()) && (!RPConfig.getConfig_flag().isResetPOPUP())) {
            try {
                String contextPath = request.getContextPath();
                logger.info("context path : " + contextPath);
                response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Sync"));
                return;
            } catch (IOException ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
            }
        } else {
            int passLogic = Integer.parseInt(RPConfig.getConfig_flag().getPASS_LOGIC());
            RPResponse resp = new RPResponse();
            resp.setUserID(userid);
            resp.setPassCode(passCode);
            resp.setPassLogic(passLogic);
            resp.setSapSystem(SAPsystem);

            resp.setLang(lang);
            String sysKey = "";
            if (!RPConfig.getConfig_flag().isMULTI_HOST()) {

                sysKey = RPConfig.getSingleHostSystem().getSystemKey();
            } else {
                sysKey = RPConfig.getSapSystemMap().get(SAPsystem).getSystemKey();
            }
            RequestNewPassword rnp = new RequestNewPassword();
            JsonTransformer t = new JsonTransformer();
            try {
                resp = rnp.getRPnewPassword(resp, sysKey, session);
                if (!resp.isPassByUser()) {
                    if (resp.isSuccess()) {
                        SapSystem isystem = RPConfig.getSapSystemMap().get(SAPsystem);
                        if (isystem.getENFlag().equalsIgnoreCase("AD")) {
                            logger.info("Inside AD");
                            resp.setPassword(resp.getPassword() + "$3");
                            ProcessRPRequest prp = new ProcessRPRequest();
                            try {
                                prp.resetActiveDirectoryPassword(userid, resp.getPassword(), session);
                            } catch (Throwable ex) {
                                StringWriter stack = new StringWriter();
                                ex.printStackTrace(new PrintWriter(stack));
                                logger.error(stack.toString());
                                stack = null;
                                resp.setSuccess(false);
                                resp.setMessage(ex.getMessage());
                            }
                        }
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

            session.setAttribute("response", resp);
            try {
                String contextPath = request.getContextPath();
                logger.info("context path : " + contextPath);
                response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Pass"));
                logger.info("End of  getPassword() WebServices method ...");
                return;
            } catch (IOException ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
            }
        }
        return;

    }

    @Path("getPasswordSync")
    @POST
    @Produces("application/json")
    public RPResponse getPasswordSync(RPResponse resp, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  getPasswordSync() WebServices method ...");
        HttpSession session = request.getSession(true);

        boolean isSessionOK = true;

        String userid = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
        if (userid == null) {
            isSessionOK = false;
            logger.error("usr parameter missing.");
        }

        String passCode = (String) session.getAttribute("passcode");
        if (passCode == null) {
            isSessionOK = false;
            logger.error("charurl parameter missing.");
        }
        String SAPsystem = (String) session.getAttribute("SYS");
        if (SAPsystem == null) {
            isSessionOK = false;
            logger.error("SYSID parameter missing.");
        }
        String lang = (String) session.getAttribute("LANG");
        if (lang == null) {
            isSessionOK = false;
            logger.error("lang parameter missing.");
        }
        if (!isSessionOK) {
            resp.setMessage("Invalid or missing parameters! Request can not be fulfilled.");
            resp.setSuccess(false);
        } else {
            int passLogic = Integer.parseInt(RPConfig.getConfig_flag().getPASS_LOGIC());
            resp.setUserID(userid);
            resp.setPassCode(passCode);
            resp.setPassLogic(passLogic);
            resp.setSapSystem(SAPsystem);

            resp.setLang(lang);
            String sysKey = "";
            if (!RPConfig.getConfig_flag().isMULTI_HOST()) {

                sysKey = RPConfig.getSingleHostSystem().getSystemKey();
            } else {
                sysKey = RPConfig.getSapSystemMap().get(SAPsystem).getSystemKey();
            }
            RequestNewPassword rnp = new RequestNewPassword();
            JsonTransformer t = new JsonTransformer();
            try {
                resp = rnp.getRPnewPassword(resp, sysKey, session);
                session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), null);// prevent refresh resume
                if (!resp.isPassByUser()) {
                    if (resp.isSuccess()) {
                        SapSystem isystem = RPConfig.getSapSystemMap().get(SAPsystem);
                        if (isystem.getENFlag().equalsIgnoreCase("AD")) {
                            logger.info("Inside AD");
                            resp.setPassword(resp.getPassword() + "$3");
                            ProcessRPRequest prp = new ProcessRPRequest();
                            try {
                                prp.resetActiveDirectoryPassword(userid, resp.getPassword(), session);
                            } catch (Throwable ex) {
                                StringWriter stack = new StringWriter();
                                ex.printStackTrace(new PrintWriter(stack));
                                logger.error(stack.toString());
                                stack = null;
                                resp.setSuccess(false);
                                resp.setMessage(ex.getMessage());
                            }
                        }
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
        logger.info("End of  getPasswordSync() WebServices method ...");
        return resp;
    }

    @Path("getResponse")
    @GET
    @Produces("application/json")
    public String getResponse(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  getResponse() WebServices method ...");
        HttpSession session = request.getSession(false);
        RPResponse resp = (RPResponse) session.getAttribute("response");
        session.setAttribute("Token", null); //URL token: prevent page refresh resume
        String jsonData = "";
        try {
            JsonTransformer t = new JsonTransformer();
            jsonData = t.transformToJson(resp);
            logger.info("End of  getResponse() WebServices method ...");
            return jsonData;
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }
    }

    @Path("getParamUrl")
    @POST
    // @Produces("application/json")
    public String getParam(EmailDTO emaiiData, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        logger.info("Start of  getParam() WebServices method ...");
        System.out.println("fsafsd" + emaiiData);
        HttpSession session = request.getSession(true);
        if (session.isNew() == false) {
            session.invalidate();
            logger.info("Creating new Sesssion");
            session = request.getSession(true);
        }
        try {
            //String userID = java.net.URLDecoder.decode(emaiiData.getUserid(), "UTF-8");

            String token = emaiiData.getToken();
            String system = emaiiData.getSystem();
            session.setAttribute("Token", token);
            System.out.println("token" + token);
            DecryptDAO decy = new DecryptDAO();
            EmailDTO emData = decy.getDecryptResponse(token, system);
            String result = Password(emData, request, response);
            if (result.equals("Sync")) {
                logger.info("End of  getParam() WebServices method ...");
                return "{\"message\" : \"Sync\",\"success\" : true}";
            } else if (result.equals("Pass")) {
                logger.info("End of  getParam() WebServices method ...");
                return "{\"message\" : \"Pass\",\"success\" : true}";
            } else {
                logger.info("End of  getParam() WebServices method ...");
                return "{\"message\" : \"Error\",\"success\" : false}";
            }
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            session.setAttribute("Error", ex.getMessage());
            stack = null;
            return "{\"message\" : \" Error \",\"success\" : false}";

        }
    }

    public String encrypt(String str) {
        //  ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

        try {
            // return engine.eval(String.format("escape(\"%s\")",
            return str.replaceAll("%20", " ").toString().replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%3C", "<").replaceAll("%3E", ">").replaceAll("%3D", "=").replaceAll("%26", "&").replaceAll("%25", "%").replaceAll("%24", "$").replaceAll("%23", "#").replaceAll("%2B", "+").replaceAll("%2C", ",").replaceAll("%3F", "?");
        } catch (Exception ex) {

            return null;
        }
    }

    public String Password(EmailDTO emailData, HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  Password() WebServices method ...");
        String userid = emailData.getUserid();
        String passCode = emailData.getCharurl();
        String SAPsystem = emailData.getSystem();
        String lang = emailData.getLang();
        String requestId = emailData.getRequestId();
        HttpSession session = request.getSession(true);
        String sysKey = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {

            sysKey = RPConfig.getSingleHostSystem().getSystemKey();
        } else {
            sysKey = RPConfig.getSapSystemMap().get(SAPsystem).getSystemKey();
        }
//        if (session.isNew() == false) {
//            session.invalidate();
//            logger.info("Creating new Sesssion");
//            session = request.getSession(true);
//        }

        if (userid == null) {
            logger.error("usr parameter missing.");
        }

        if (passCode == null) {
            logger.error("charurl parameter missing.");
        }
        if (SAPsystem == null) {
            logger.error("SYSID parameter missing.");
        }
        if (lang == null) {
            logger.error("lang parameter missing.");
        }
        session.setAttribute("SYS", SAPsystem);
        boolean isPassSetforOther = false;
        try {
            OtherTabVisDao otherTabVisDao = new OtherTabVisDao();
            isPassSetforOther = otherTabVisDao.validateUserForOtherTab(userid, sysKey, session);
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }

        if (CharMatcher.ascii().matchesAllOf(userid)) {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid.toUpperCase());
            if (isPassSetforOther) {
                session.setAttribute("userIDApp", requestId.toUpperCase());
            }

        } else {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid);
            if (isPassSetforOther) {
                session.setAttribute("userIDApp", requestId);
            }
        }
        session.setAttribute("passcode", passCode);
        if (lang.equalsIgnoreCase("EN")) {
            session.setAttribute("LANG", "English");
        } else if (lang.equalsIgnoreCase("DE")) {
            session.setAttribute("LANG", "German");
        }
        if ((RPConfig.getConfig_flag().isPassword_SYNC()) && (!RPConfig.getConfig_flag().isResetPOPUP())) {
            return "Sync";
//            try {
//                String contextPath = request.getContextPath();
//                logger.info("context path : " + contextPath);
//                response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Sync"));
//                return;
//            } catch (IOException ex) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//                stack = null;
//            }
        } else {
            int passLogic = Integer.parseInt(RPConfig.getConfig_flag().getPASS_LOGIC());
            RPResponse resp = new RPResponse();
            resp.setUserID(userid);
            resp.setPassCode(passCode);
            resp.setPassLogic(passLogic);
            resp.setSapSystem(SAPsystem);
            if (isPassSetforOther) {
            resp.setUserIDOther(requestId);
            }

            resp.setLang(lang);

            RequestNewPassword rnp = new RequestNewPassword();
            JsonTransformer t = new JsonTransformer();
            try {
                resp = rnp.getRPnewPassword(resp, sysKey, session);
                if (!resp.isPassByUser()) {
                    if (resp.isSuccess()) {
                        SapSystem isystem = RPConfig.getSapSystemMap().get(SAPsystem);
                        if (isystem.getENFlag().equalsIgnoreCase("AD")) {
                            logger.info("Inside AD");
                            resp.setPassword(resp.getPassword() + "$3");
                            ProcessRPRequest prp = new ProcessRPRequest();
                            try {
                                prp.resetActiveDirectoryPassword(userid, resp.getPassword(), session);
                            } catch (Throwable ex) {
                                StringWriter stack = new StringWriter();
                                ex.printStackTrace(new PrintWriter(stack));
                                logger.error(stack.toString());
                                stack = null;
                                resp.setSuccess(false);
                                resp.setMessage(ex.getMessage());
                            }
                        }
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

            session.setAttribute("response", resp);
            logger.info("End of  Password() WebServices method ...");
            return "Pass";
//            try {
//                String contextPath = request.getContextPath();
//                logger.info("context path : " + contextPath);
//                response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Pass"));
//                return;
//            } catch (IOException ex) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//                stack = null;
//            }
        }

    }

    /**
     * PUT method for updating or creating an instance of GetPasswordResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Produces("application/json")
    public void putXml(String content) {
    }
}
