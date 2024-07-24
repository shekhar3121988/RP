/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * session.setAttribute("passcode", rpResponse.getPassCode()); return String
 * session.setAttribute("param", param); return String
 * ArrayList<POJO_QuesAns> : session.setAttribute("QuesAnsList", quesAnsList);
 */
package sw.com.rp.rest;

import com.google.common.base.CharMatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dao.DecryptDAO;
import sw.com.rp.dao.ProcessRPRequest;
import sw.com.rp.dao.RequestNewPassword;
import sw.com.rp.dto.EmailDTO;
import sw.com.rp.dto.POJO_QuesAns;
import sw.com.rp.dto.POJO_QuesAnsSubmitUpdate;
import sw.com.rp.dto.RPRequest;
import sw.com.rp.dto.RPResponse;
import sw.com.rp.transformer.JsonTransformer;
import org.apache.commons.codec.binary.Base64;

/**
 * REST Web Service
 *
 * @author msaini
 */
@Path("RPRequest")
public class RPRequestResource {

    @Context
    private UriInfo context;
    static final Logger logger = LogManager.getLogger(RPRequestResource.class.getName());

    /**
     * Creates a new instance of RPRequestResource
     */
    public RPRequestResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.sw.rp.rest.RPRequestResource
     *
     * @return an instance of java.lang.String
     */
    @POST
    @Produces("application/json")
    public String getRPRequest(RPRequest reqData, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  getRPRequest() WebServices method ...");
        HttpSession session = request.getSession(false);
        String Sys = "";
//        String selectedSys = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
            Sys = RPConfig.getSingleHostSystem().getName();
        } else {
            try {
                Sys = (String) session.getAttribute("SYS");

            } catch (Exception e) {
                logger.error(e.getMessage());
                //multilanguage code
                String text = getTranslatedText("RPRequestmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";

            }

        }

        if (Sys == null || Sys.trim().length() == 0) {
            //multilanguage code
            String text = getTranslatedText("RPRequestmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "Please select system";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        if (RPConfig.getConfig_flag().isSSO()) {
            if (RPConfig.getConfig_flag().isGREY_UID()) {
                String GID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
                if (!reqData.getUserID().equalsIgnoreCase(GID)) {
                    //multilanguage code
                    String text = getTranslatedText("RPRequestmsg3", session);
                    if (text == null || text.trim().length() == 0) {
                        text = "SSO Id is not matched with provided user Id";
                    }
                    logger.info("End of  getRPRequest() WebServices method ...");
                    return "{\"message\" : \"" + text + "\",\"success\" : false}";
                }
            }
        }
        RPResponse rpResponse = new RPResponse();
//        String reqData = request.getParameter("reqData");
        JsonTransformer t = new JsonTransformer();
        ArrayList<RPRequest> rpRequest = null;
        try {

            rpRequest = (ArrayList<RPRequest>) JsonTransformer.transformToJavaObjects(reqData, RPRequest.class);
            logger.info(rpRequest.toString());
            //session.setAttribute("MULTIPLESYSTEM", rpRequest.get(0).getMultipleSystem());
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            logger.info("End of  getRPRequest() WebServices method ...");
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";

        }

        ProcessRPRequest prp = new ProcessRPRequest();
//        String system = rpRequest.getSapSystem();
        String Syskey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
//        if (RPConfig.getConfig_flag().isMULTI_HOST()) {
        rpRequest.get(0).setSapSystem((String) session.getAttribute("SYS"));
//        }
        SapSystem isystem = RPConfig.getSapSystemMap().get(Sys);
        if (isystem.getENFlag().equalsIgnoreCase("AD")) {
            logger.info("Inside AD...");
            if (rpRequest.get(0).getUserID() != null && rpRequest.get(0).getUserID().toString().trim().length() > 0) {
                try {
                    prp.validateUIDWithAD(rpRequest.get(0).getUserID(), session);
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
                    logger.info("End of  getRPRequest() WebServices method ...");
                    return jsonData;
                }

            } else {
                try {
                    prp.validateUIDemailWithAD(rpRequest.get(0).getUserID(), rpRequest.get(0).getEmailID(), session);
                    logger.info("UIDemail validated with AD");
                } catch (Throwable ex) {
                    rpResponse = new RPResponse();
                    rpResponse.setSuccess(false);
                    rpResponse.setMessage(ex.getMessage());
                    if (ex.getMessage().contains("lock") && RPConfig.getConfig_flag().isHELP_DESK()) {
                        //multilanguage code
                        String text = getTranslatedText("RPRequestmsg2", session);
                        if (text == null || text.trim().length() == 0) {
                            text = "User is locked in active directory.";
                        }
                        String msg1 = text;
//                        String msg2 = "";
//                        String msg3 = "";
//                        String msg4 = "";
//                        String msg5 = "";
//                        if (!RPConfig.getConfig_flag().getHELP_PHONE().equalsIgnoreCase("NA")) {
//                            msg2 = "Help Desk Phone Number : " + RPConfig.getConfig_flag().getHELP_PHONE() + "<br>";
//                        }
//                        if (!RPConfig.getConfig_flag().getHELP_CONTACT().equalsIgnoreCase("NA")) {
//                            msg3 = "Contact Name : " + RPConfig.getConfig_flag().getHELP_CONTACT() + "<br>";
//                        }
//                        if (!RPConfig.getConfig_flag().getHELP_EMAIL().equalsIgnoreCase("NA")) {
//                            msg4 = "Email : " + RPConfig.getConfig_flag().getHELP_EMAIL() + "<br>";
//                        }
//                        if (!RPConfig.getConfig_flag().getHELP_PAGE().equalsIgnoreCase("NA")) {
//                            msg5 = "Support Web Page : <a href='" + RPConfig.getConfig_flag().getHELP_PAGE() + "' target='_blank' >Click Here</a>";
//                        }
                        rpResponse.setMessage(msg1);

                        rpResponse.setLocked(true);
                        rpResponse.setHelpDesk(true);
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
                    logger.info("End of  getRPRequest() WebServices method ...");
                    return jsonData;
                }

            }

        }
        session.setAttribute("rpRequest", rpRequest.get(0));

        try {
            rpResponse = prp.getRPrequestResponse(rpRequest.get(0), Syskey, session);
            if (rpResponse.getPassCode() != null && rpResponse.getPassCode().length() > 0) {
                session.setAttribute("passcode", rpResponse.getPassCode());

                if (!RPConfig.getPropertyList().get(0).isSSO()) {
                    String token = generateCSRFToken(32);
                    int cookieMaxAge = request.getSession(false).getMaxInactiveInterval();
                    String cookieContext = request.getServletContext().getContextPath();

                    String cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                    if (request.isSecure()) {
                        cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                    }
                    response.addHeader("Set-Cookie", cookieSpec);

                    session.setAttribute("userdata", rpResponse.getUserID());
                    session.setAttribute("x-csrf-token-rp", token);
                    session.setAttribute("x-selectedSystem", Sys);

                    EncryptDecrypt rpc = new EncryptDecrypt();

                    String encTID = rpc.encrypt(token);
                    rpResponse.setxTID(encTID);
                }

            }
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            rpResponse.setSuccess(false);
            rpResponse.setMessage(ex.getMessage());
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            rpResponse.setSuccess(false);
            rpResponse.setMessage(ex.getMessage());
        }
//        rpResponse.setUserID(rpRequest.getUserID());
        String jsonData = "";
        try {
            jsonData = JsonTransformer.transformToJson(rpResponse);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        logger.info("End of  getRPRequest() WebServices method ...");
        return jsonData;
    }

    @Path("getQues")
    @GET
    @Produces("application/json")
    public String getRPQARequest(@Context HttpServletRequest request) {
        logger.info("Start of  getRPQARequest() WebServices method ...");
        ListWrapper<POJO_QuesAns> w = new ListWrapper<POJO_QuesAns>();
        HttpSession session = request.getSession(false);
        String Sys = "";
        String userID = "";
        String userIDApp = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
            Sys = RPConfig.getSingleHostSystem().getName();
        } else {
            try {
                Sys = (String) session.getAttribute("SYS");
            } catch (Exception e) {
                logger.error(e.getMessage());
                //multilanguage code
                String text = getTranslatedText("RPRequestmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
        }
        if (Sys == null || Sys.trim().length() == 0) {
            String text = getTranslatedText("RPRequestmsg1", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "Please select system";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        try {
            userID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
            if (session.getAttribute("userIDApp") != null) {
                userIDApp = (String) session.getAttribute("userIDApp");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            String text = getTranslatedText("getQuesmsg3", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";

        }
        if (userID == null || userID.trim().length() == 0) {
            String text = getTranslatedText("getQuesmsg3", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        w.setUserid(userID);

        String passCode = (String) session.getAttribute("passcode");
        String Syskey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
        ProcessRPRequest prp = new ProcessRPRequest();
        RPRequest rpRequest = new RPRequest();
        rpRequest.setUserID(userID);
        if (userIDApp != null) {
            rpRequest.setUserIDApp(userIDApp);
        } else {
            rpRequest.setUserIDApp("");
        }

        rpRequest.setMultipleSystem((ArrayList<String>) session.getAttribute("MULTIPLESYSTEM"));
        String selectedSys = (String) session.getAttribute("SYS");
        rpRequest.setSapSystem(selectedSys);
        try {
            HashMap<String, Object> quesAnsMap = prp.getRPQesAns(rpRequest, Syskey, passCode, session);
            String param = (String) quesAnsMap.get("param");
            session.setAttribute("param", param);
            session.setAttribute("attempts", "0");
            ArrayList<POJO_QuesAns> quesAnsList = (ArrayList<POJO_QuesAns>) quesAnsMap.get("QuesAnsList");
            session.setAttribute("QuesAnsList", quesAnsList);
            ArrayList<POJO_QuesAns> quesList = new ArrayList<POJO_QuesAns>();
            for (POJO_QuesAns qa : quesAnsList) {
                POJO_QuesAns qa1 = new POJO_QuesAns();
                qa1.setQuestion(qa.getQuestion());
                quesList.add(qa1);
            }

            w.setData(quesList);
            w.setSuccess(Boolean.TRUE);
            w.setTotal(quesList.size());
            String text = getTranslatedText("getQuesmsg4", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "Total questions to be displayed.";
            }
            w.setMessage(text);
            String jsonData = "";
            JsonTransformer transformer = new JsonTransformer();
            try {
                jsonData = transformer.transformToJson(w);
            } catch (Exception ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
//                return ex.toString();
                return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
            }
            //System.out.println("Owner INBOX : \n"+jsonData);
            return jsonData;
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
//            return "Exception error : "+ex.getMessage();
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
//            return "Exception error : "+ex.getMessage();
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }

    }

    @Path("ans")
    @POST
    @Produces("application/json")
    public String getAns(POJO_QuesAnsSubmitUpdate ans, @Context HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String Sys = "";
        String userID = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {
            if (RPConfig.getConfig_flag().isEMAIL_QUES_LINK()) {
                Sys = (String) session.getAttribute("SYS");
            } else {
                Sys = RPConfig.getSingleHostSystem().getName();
            }
        } else {
            try {
                Sys = (String) session.getAttribute("SYS");
            } catch (Exception e) {
                logger.error(e.getMessage());
                String text = getTranslatedText("RPRequestmsg1", session);
                //multilanguage code
                if (text == null || text.trim().length() == 0) {
                    text = "Please select system";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
        }
        if (Sys == null || Sys.trim().length() == 0) {
            String text = getTranslatedText("RPRequestmsg1", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "Please select system";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        try {
            userID = (String) session.getAttribute(RPConfig.getConfig_flag().getREQPARAM());
        } catch (Exception e) {
            logger.error(e.getMessage());
            String text = getTranslatedText("getQuesmsg3", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";

        }
        if (userID == null || userID.trim().length() == 0) {
            String text = getTranslatedText("getQuesmsg3", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "User ID is missing!";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        ArrayList<POJO_QuesAnsSubmitUpdate> answ = null;
        JsonTransformer t = new JsonTransformer();
        try {
            answ = (ArrayList<POJO_QuesAnsSubmitUpdate>) JsonTransformer.transformToJavaObjects(ans, POJO_QuesAnsSubmitUpdate.class);
//            log(rpRequest.toString());
            ArrayList<POJO_QuesAns> quesAnsList = (ArrayList<POJO_QuesAns>) session.getAttribute("QuesAnsList");
            int correctCount = 0;

            for (int i = 0; i < quesAnsList.size(); i++) {
                POJO_QuesAns quesAns = quesAnsList.get(i);
                if (i == 0) {
                    if (quesAns.getAnswers().equalsIgnoreCase(answ.get(0).getAnswer1())) {
                        correctCount++;
                    }
                }
                if (i == 1) {
                    if (quesAns.getAnswers().equalsIgnoreCase(answ.get(0).getAnswer2())) {
                        correctCount++;
                    }
                }
                if (i == 2) {
                    if (quesAns.getAnswers().equalsIgnoreCase(answ.get(0).getAnswer3())) {
                        correctCount++;
                    }
                }
                if (i == 3) {
                    if (quesAns.getAnswers().equalsIgnoreCase(answ.get(0).getAnswer4())) {
                        correctCount++;
                    }
                }
                if (i == 4) {
                    if (quesAns.getAnswers().equalsIgnoreCase(answ.get(0).getAnswer5())) {
                        correctCount++;
                    }

                }

            }
            String param = (String) session.getAttribute("param");
            int userParam = Integer.parseInt(param);
            String attempts = (String) session.getAttribute("attempts");
            int attemptCount = Integer.parseInt(attempts);
            attemptCount = attemptCount + 1;
            session.setAttribute("attempts", "" + attemptCount + "");
            RPResponse rpResponse = new RPResponse();
            String jsonData = "";
            ProcessRPRequest prp = new ProcessRPRequest();
            RequestNewPassword pass = new RequestNewPassword();
            RPRequest rpRequest = (RPRequest) session.getAttribute("rpRequest");
            String Syskey = "";
            if (!RPConfig.getConfig_flag().isMULTI_HOST()) {

                Syskey = RPConfig.getSingleHostSystem().getSystemKey();
            } else {
                Syskey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
            }
            // String Syskey = RPConfig.getSapSystemMap().get(Sys).getSystemKey();
            if (correctCount >= userParam && attemptCount <= 4) {
                try {
                    // RESET Password query
                    logger.info("Resetting Password.");
                    rpResponse.setUserID(userID);

                    rpResponse.setUserIDOther(answ.get(0).getUserIDOther());
                    rpResponse.setPassCode(answ.get(0).getCharUrl());

                    rpResponse.setPassSync(answ.get(0).getPassSync());
                    rpResponse.setSapSystem((String) session.getAttribute("SYS"));
                    int passLogic = Integer.parseInt(RPConfig.getConfig_flag().getPASS_LOGIC());
                    rpResponse.setPassLogic(passLogic);
                    rpResponse = pass.getRPnewPassword(rpResponse, Syskey, session);

                    // rpResponse = prp.getPasswordforQA(userID, Sys, Syskey);
                    if (rpResponse.isSuccess()) {
                        if (!rpResponse.isPassByUser()) {
                            SapSystem isystem = new SapSystem();
                            if (RPConfig.getConfig_flag().isMULTI_HOST()) {
                                String selectedSys = (String) session.getAttribute("SYS");
                                isystem = RPConfig.getSapSystemMap().get(selectedSys);

                            } else {
                                isystem = RPConfig.getSapSystemMap().get(Sys);
                            }

                            if (isystem.getENFlag().equalsIgnoreCase("AD")) {
                                logger.info("Inside AD...");
                                try {
                                    rpResponse.setPassword(rpResponse.getPassword() + "$3");
                                    prp.resetActiveDirectoryPassword(rpResponse.getUserID(), rpResponse.getPassword(), session);
                                } catch (Throwable ex) {
                                    StringWriter stack = new StringWriter();
                                    ex.printStackTrace(new PrintWriter(stack));
                                    logger.error(stack.toString());
                                    stack = null;
                                    rpResponse.setSuccess(false);
                                    rpResponse.setMessage(ex.getMessage());
                                    jsonData = t.transformToJson(rpResponse);
                                    return jsonData;
                                }
                            }
                        }
                    }
                    jsonData = t.transformToJson(rpResponse);
                    return jsonData;
                } catch (Throwable ex) {
                    StringWriter stack = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;
                    rpResponse.setSuccess(false);
                    rpResponse.setMessage(ex.getMessage());
                    jsonData = t.transformToJson(rpResponse);
                    return jsonData;
                }
//                return "{\"message\" : \"" + "ready for reset password." + "\",\"success\" : true}";
            } else {
                if (attemptCount == 3) {
                    // LAST CHANCE Alert
                    rpResponse.setSuccess(false);
                    String text = getTranslatedText("getQuesmsg5", session);
                    //multilanguage code
                    if (text == null || text.trim().length() == 0) {
                        text = "Incorrect answer(s) entered 3 times, User will be locked, you have last chance.";
                    }
                    rpResponse.setMessage(text);
                    jsonData = t.transformToJson(rpResponse);
                    return jsonData;
                    //return "{\"message\" : \"" + "Wrong Answers, Last Chance!"+ "\",\"success\" : false}";
                } else if (attemptCount >= 4) {
                    // Lock User
                    logger.info("Locking User.");
                    try {
                        //if ((answ.get(0).getUserIDOther() != null) && (answ.get(0).getUserIDOther().length() > 0)) {
                        //    rpResponse.setUserID(answ.get(0).getUserIDOther());
                        //} else {
                            rpResponse.setUserID(userID);
                            rpResponse.setUserIDOther(answ.get(0).getUserIDOther());
                       // }

                        rpResponse.setSapSystem((String) session.getAttribute("SYS"));
                        rpResponse.setPassCode(answ.get(0).getCharUrl());
                        rpResponse = prp.lockUserforWrongQA(rpResponse, Syskey, session);
                        jsonData = t.transformToJson(rpResponse);
                        return jsonData;
                    } catch (Throwable ex) {
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        rpResponse.setSuccess(false);
                        rpResponse.setMessage(ex.getMessage());
                        jsonData = t.transformToJson(rpResponse);
                        return jsonData;
                    }
//                    jsonData = t.transformToJson(rpResponse);
//                    return jsonData;
//                    return "{\"message\" : \"" + "User Locked!" + "\",\"success\" : false}";
                } else {
                    // Alert for Wrong Answers
                    rpResponse.setSuccess(false);
                    String text = getTranslatedText("getQuesmsg6", session);
                    //multilanguage code
                    if (text == null || text.trim().length() == 0) {
                        text = "Incorrect answer(s) entered in one of the question(s).";
                    }
                    rpResponse.setMessage(text);
                    jsonData = t.transformToJson(rpResponse);
                    return jsonData;
                    //return "{\"message\" : \"" + "Wrong Answers."+ "\",\"success\" : false}";
                }
            }

        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }

        //System.out.println("Ans:\n"+ans);
//        String count = session.getAttribute("param").toString();
//        return "{\"message\" : \"" + count + "\",\"success\" : true}";
    }

    @Path("getQuestions")
    @GET
//    @Produces("application/json")
    @Produces("text/html")
    public void getRPQuesAnsRequest(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String user = "";
        String code = "";
        try {
            request.setCharacterEncoding("UTF-8");
            // String queryString = java.net.URLDecoder.decode(request.getQueryString(), "UTF-8");
//           String queryString =request.getQueryString();
            Map queryString = request.getParameterMap();
            String[] queryString1 = request.getParameterValues("usr");
            System.out.println("queryString: " + queryString);
            //  request.setAttribute(queryString, queryString);
            DecryptDAO decy = new DecryptDAO();
            // decy.getDecryptResponse("" ,"");
            //int usrStartindex= queryString.indexOf("usr=");
            //  int endindex = queryString.indexOf("&charurl");
            //  int bigin = queryString.indexOf("&charurl=");
            //  int end = queryString.indexOf("&SYSID");
            //  user = queryString.substring(0, endindex);
            //  code = queryString.substring(bigin, end);
            code = code.substring(9);
            user = user.substring(4);
            System.out.println("useruser" + user);
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;

        }
        HttpSession session = request.getSession(true);
        String userid = user;
        if (userid == null) {
            logger.error("usr parameter missing.");
        }
        String passCode = code;
        if (passCode == null) {
            logger.error("passCode parameter missing.");
        }
        String sysID = request.getParameter("SYSID");
        if (sysID == null) {
            logger.error("sysID parameter missing.");
        }
        String lang = request.getParameter("lang");
        if (lang == null) {
            logger.error("lang parameter missing.");
        }
        session.setAttribute("SYS", sysID);
        if (CharMatcher.ascii().matchesAllOf(userid)) {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid.toUpperCase());
        } else {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid);
        }
        //session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid.toUpperCase());
        session.setAttribute("passcode", passCode);
        if (lang.equalsIgnoreCase("EN")) {
            session.setAttribute("LANG", "English");
        } else if (lang.equalsIgnoreCase("DE")) {
            session.setAttribute("LANG", "German");
        }

        JsonTransformer t = new JsonTransformer();
        RPRequest rpRequest = new RPRequest();
        rpRequest.setUserID(userid);

        rpRequest.setSapSystem(sysID);

        String Syskey = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {

            Syskey = RPConfig.getSingleHostSystem().getSystemKey();
        } else {
            Syskey = RPConfig.getSapSystemMap().get(sysID).getSystemKey();
        }
        ProcessRPRequest prp = new ProcessRPRequest();
        String error = "";
        try {
            HashMap<String, Object> quesAnsMap = prp.getRPQesAns(rpRequest, Syskey, passCode, session);
            if (quesAnsMap.get("processed") != null) {
                error = "Password Reset Request already Processed.";
                throw new Exception(error);
                //session.setAttribute("Error", error);
//                return "Password Reset Request already Processed.";
                // return "<br><h2> Password Reset Request already Processed.</h2>";
            }
            String param = (String) quesAnsMap.get("param");
            session.setAttribute("param", param);
            session.setAttribute("attempts", "0");
            session.setAttribute("rpRequest", rpRequest);
            ArrayList<POJO_QuesAns> quesAnsList = (ArrayList<POJO_QuesAns>) quesAnsMap.get("QuesAnsList");
            session.setAttribute("QuesAnsList", quesAnsList);
            ArrayList<POJO_QuesAns> quesList = new ArrayList<POJO_QuesAns>();
            for (POJO_QuesAns qa : quesAnsList) {
                POJO_QuesAns qa1 = new POJO_QuesAns();
                qa1.setQuestion(qa.getQuestion());
                quesList.add(qa1);
            }
            session.setAttribute("QuesList", quesList);
            try {
                String contextPath = request.getContextPath();
                logger.info("context path : " + contextPath);
                response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Question"));
                return;
            } catch (IOException ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                error = ex.getMessage();
                session.setAttribute("Error", error);
                // return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
            }
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            error = ex.getMessage();
            session.setAttribute("Error", error);
//            return "Exception error : "+ex.getMessage();
            // return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            error = ex.getMessage();
            session.setAttribute("Error", error);
//            return "Exception error : "+ex.getMessage();
            // return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }
        //
        if (error.trim().length() < 1) {
            String text = getTranslatedText("getQuesmsg8", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "Sorry! Something went wrong.";
            }
            error = text;
        }
        session.setAttribute("Error", error);

        String contextPath = request.getContextPath();
        logger.info("context path : " + contextPath);
        try {
            response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Error"));
            // return "Sorry! Something went wrong.";
        } catch (IOException ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }

    }

    @Path("getQuestionz")
    @GET
    @Produces("application/json")
    public String getRPQuesAnszRequest(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ArrayList<POJO_QuesAns> quesList = (ArrayList<POJO_QuesAns>) session.getAttribute("QuesList");
        if (quesList == null) {
            String text = getTranslatedText("getQuesmsg7", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "No questions found.";
            }
            return "{\"message\" : \"" + text + "\",\"success\" : false}";
        }
        ListWrapper<POJO_QuesAns> w = new ListWrapper<POJO_QuesAns>();
        w.setData(quesList);
        w.setSuccess(Boolean.TRUE);
        w.setTotal(quesList.size());
        String text = getTranslatedText("getQuesmsg4", session);
        //multilanguage code
        if (text == null || text.trim().length() == 0) {
            text = "Total questions to be displayed.";
        }
        w.setMessage(text);
        String jsonData = "No Data";
        JsonTransformer transformer = new JsonTransformer();
        try {
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
//                return ex.toString();
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }
        //System.out.println("Owner INBOX : \n"+jsonData);
        session.setAttribute("Token", null);//No resume on page QA link refresh
        return jsonData;
    }
//

    @Path("getParamUrl")
    @POST
    // @Produces("application/json")
    public String getParam(EmailDTO emaiiData, @Context HttpServletRequest request, @Context HttpServletResponse response) {
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
            String result = RPQuesAnsRequest(emData, request, response);
            if (result.equals("Error")) {
                return "{\"message\" : \" Error \",\"success\" : false}";
            } else {
                return "{\"message\" : \"success \",\"success\" : true}";
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

    public String RPQuesAnsRequest(EmailDTO emailData, HttpServletRequest request, HttpServletResponse response) {
        String userid = emailData.getUserid();
        String passCode = emailData.getCharurl();
        String system = emailData.getSystem();
        String lang = emailData.getLang();
        String requestId = emailData.getRequestId();

        HttpSession session = request.getSession(true);
        if (userid == null) {
            logger.error("usr parameter missing.");
        }
        if (passCode == null) {
            logger.error("passCode parameter missing.");
        }
        if (system == null) {
            logger.error("sysID parameter missing.");
        }
        if (lang == null) {
            logger.error("lang parameter missing.");
        }
        session.setAttribute("SYS", system);
        if (CharMatcher.ascii().matchesAllOf(userid)) {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid.toUpperCase());
            session.setAttribute("userIDApp", requestId.toUpperCase());
        } else {
            session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid);
            session.setAttribute("userIDApp", requestId);
        }
        //session.setAttribute(RPConfig.getConfig_flag().getREQPARAM(), userid.toUpperCase());
        session.setAttribute("passcode", passCode);
        if (lang.equalsIgnoreCase("EN")) {
            session.setAttribute("LANG", "English");
        } else if (lang.equalsIgnoreCase("DE")) {
            session.setAttribute("LANG", "German");
        }

        JsonTransformer t = new JsonTransformer();
        RPRequest rpRequest = new RPRequest();

        rpRequest.setUserID(userid);
        rpRequest.setUserIDApp(requestId);

        rpRequest.setSapSystem(system);

        String Syskey = "";
        if (!RPConfig.getConfig_flag().isMULTI_HOST()) {

            Syskey = RPConfig.getSingleHostSystem().getSystemKey();
        } else {
            Syskey = RPConfig.getSapSystemMap().get(system).getSystemKey();
        }
        ProcessRPRequest prp = new ProcessRPRequest();
        String error = "";
        try {
            HashMap<String, Object> quesAnsMap = prp.getRPQesAns(rpRequest, Syskey, passCode, session);
            if (quesAnsMap.get("processed") != null) {
                error = "Password Reset Request already Processed.";
                throw new Exception(error);
                //session.setAttribute("Error", error);
//                return "Password Reset Request already Processed.";
                // return "<br><h2> Password Reset Request already Processed.</h2>";
            }
            String param = (String) quesAnsMap.get("param");
            session.setAttribute("param", param);
            session.setAttribute("attempts", "0");
            session.setAttribute("rpRequest", rpRequest);
            ArrayList<POJO_QuesAns> quesAnsList = (ArrayList<POJO_QuesAns>) quesAnsMap.get("QuesAnsList");
            session.setAttribute("QuesAnsList", quesAnsList);
            ArrayList<POJO_QuesAns> quesList = new ArrayList<POJO_QuesAns>();
            for (POJO_QuesAns qa : quesAnsList) {
                POJO_QuesAns qa1 = new POJO_QuesAns();
                qa1.setQuestion(qa.getQuestion());
                quesList.add(qa1);
            }
            session.setAttribute("QuesList", quesList);
            return "Question";
//            try {
//                String contextPath = request.getContextPath();
//                logger.info("context path : " + contextPath);
//               // response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Question"));
//                return "Question";
//            } catch (IOException ex) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//                stack = null;
//                error = ex.getMessage();
//                session.setAttribute("Error", error);
//                 return "Error";
//                // return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
//            }
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            error = ex.getMessage();
            session.setAttribute("Error", error);
//            return "Error";
//            return "Exception error : "+ex.getMessage();
            // return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            error = ex.getMessage();
            session.setAttribute("Error", error);
//             return "Error";
//            return "Exception error : "+ex.getMessage();
            // return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }
        //
        if (error.trim().length() < 1) {
            String text = getTranslatedText("getQuesmsg8", session);
            //multilanguage code
            if (text == null || text.trim().length() == 0) {
                text = "Sorry! Something went wrong.";
            }
            error = text;
        }
        session.setAttribute("Error", error);

        String contextPath = request.getContextPath();
        logger.info("context path : " + contextPath);
        return "Error";

    }

//
    /**
     * PUT method for updating or creating an instance of RPRequestResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
//    public void log(String msg) {
//        try {
//            if (RPConfig.isLogging()) {
//                System.out.println(msg);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//
//        }
//    }

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
                    text = RPConfig.getRPRequestResourceLangMap().get(selectedLang).get(textID);
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
