package sw.com.rp.rest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.common.base.CharMatcher;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import sw.com.rp.config.RPConfig;

/**
 *
 * @author msuppahiya
 */
public class SSO extends HttpServlet {

    static final Logger logger = LogManager.getLogger(SSO.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SSO</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SSO at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!response.isCommitted()) {
            String msg = null; //SPConfig.getJavaMessageLangMap().get(lang).get("MISC_MSG177");
            if (msg == null || msg.length() < 1) {
                msg = "Unauthorized request can not be fulfilled.";
            }
            logger.error(msg);
            response.addHeader("errorMessage", msg);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        //String queryString = java.net.URLDecoder.decode(request.getQueryString(), "UTF-8");
        //System.out.println("queryString: " + queryString);
        //request.setAttribute(queryString, queryString);
        //String user[] = queryString.split("=");
        //System.out.println("user id " + user[1]);
        PrintWriter out = response.getWriter();
        try {
            ConfigSyncResource configsync = new ConfigSyncResource();
            configsync.getConfigSync(request, response);
            String contextPath = request.getContextPath();
            HttpSession session = request.getSession(false);

            if (session == null) {
                session = request.getSession(true);
                session.setAttribute("Error", "No SSO session found.");
                try {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "/index.html?View=Error"));
                } catch (IOException ex) {
                    StringWriter stack = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;
                }

            }
//            boolean resumeSession = false;
//            if (session.getAttribute("resumeSession") != null) {
//                if (session.getAttribute("resumeSession").equals("Y")) {
//                    resumeSession = true;
//                    logger.info("Resuming Login Session");
//                }
//            }
//            if (!resumeSession && session.isNew() == false) {
//                session.invalidate();
//                logger.info("gNew Sesssion");
//                session = request.getSession(false);
//            }
            //String reqParameter = RPConfig.getConfig_flag().getREQPARAM();
            //session.setAttribute(reqParameter, null);
            String jsonData = "{\"success\":  false}";
            if (RPConfig.getConfig_flag().isSSO()) {

                logger.info("context path : " + contextPath);
                // String userID = new String(request.getParameter(RPConfig.getConfig_flag().getREQPARAM()).getBytes("ASCII"), "UTF8");
                //String userID = user[1];
                String valueReg = null;
                if (request.getParameter("View") != null) {
                    valueReg = request.getParameter("View");
                }
                String userID = null;
                if (request.getSession(false).getAttribute(RPConfig.getConfig_flag().getREQPARAM()) != null) {
                    userID = request.getSession(false).getAttribute(RPConfig.getConfig_flag().getREQPARAM()).toString();
                }

                // String userID2=java.net.URLDecoder.decode(request.getParameter(RPConfig.getConfig_flag().getREQPARAM()), "UTF-8");
                if (userID != null && userID.length() > 0) {
                    System.out.println("session id: " + userID);
//                    if (CharMatcher.ascii().matchesAllOf(userID)) {
//                        session.setAttribute(reqParameter, userID.toUpperCase());
//                    } else {
//                        session.setAttribute(reqParameter, userID);
//                    }
                    jsonData = "{\"success\":  true}";
                    try {
                        if (valueReg != null && valueReg.length() > 0) {
                            response.sendRedirect(response.encodeRedirectURL(contextPath + "/index.html?View=" + valueReg));
                        } else {
                            response.sendRedirect(response.encodeRedirectURL(contextPath + "/index.html"));
                        }
                    } catch (IOException ex) {
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                    }
                } else {
                    session.setAttribute("Error", "No SSO ID found in session!");
                    try {
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "/index.html?View=Error"));
                    } catch (IOException ex) {
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                    }

                }

            } else {
                jsonData = "<B>SSO not configured</B>";
                logger.info("SSO not configured");
            }
            out.println(jsonData);

        } finally {
            out.close();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
