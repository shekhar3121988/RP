/**
 * @author : Security Weaver, LLC
 * ----------------------------------------------------------------------*
 * COPYRIGHTS Security Weaver, LLC
 *
 * WARNING: THIS COMPUTER PROGRAM IS PROTECTED BY COPYRIGHT LAW AND
 * INTERNATIONAL TREATIES. UNAUTHORIZED REPRODUCTION OR DISTRIBUTION IS STRICTLY
 * PROHIBITED AND MAY RESULT IN SEVERE CIVIL AND CRIMINAL PENALTIES AND WILL BE
 * PROSECUTED TO THE MAXIMUM EXTENT POSSIBLE UNDER THE LAW.
 * ----------------------------------------------------------------------
 */
package sw.com.rp.filter;

import sw.com.rp.config.RPConfig;
import sw.com.rp.rest.EncryptDecrypt;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;

public class CsrfProtectionFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(CsrfProtectionFilter.class.getName());

    private FilterConfig filterConfig = null;

    public CsrfProtectionFilter() {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        //doBeforeProcessing(request, response);
        LOGGER.debug("Inside CsrfProtectionFilter :: doFilter()");
        Throwable problem = null;
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        try {
            String msg;
            String sRequestURI = httpReq.getRequestURI().substring(httpReq.getRequestURI().indexOf("/resources"));
            /**
             * *
             * If front-end not connected with backend then no check
             */
            if (RPConfig.getPropertyList().isEmpty()
                    || RPConfig.getPropertyList().get(0).getRP_URL() == null
                    || RPConfig.getPropertyList().get(0).getRP_URL().equals("")) {
                try {
                    chain.doFilter(httpReq, httpResp);
                    return;
                } catch (IOException | ServletException e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    LOGGER.error(stack.toString());
                    stack = null;
                    if (!httpResp.isCommitted()) {
                        msg = "Oops! Something went wrong!";
                        httpResp.addHeader("errorMessage", msg);
                        httpResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Oops! Something went wrong!");
                    }
                }
            }

            String targetOrigin = RPConfig.getPropertyList().get(0).getRP_URL();

            if (targetOrigin != null && targetOrigin.trim().length() > 0) {
                /* STEP 1: Verifying Same Origin with Standard Headers */
                //Try to get the source from the "Origin" header
                String source = httpReq.getHeader("Origin");
                if (source == null || source.trim().length() == 0) {
                    //If empty then fallback on "Referer" header
                    source = httpReq.getHeader("Referer");
                    //If this one is empty too then we trace the event and we block the request 
                    //(recommendation of the article)...
                    if (source == null || source.trim().length() == 0) {
                        LOGGER.debug("requestURI:: " + sRequestURI);
                        logHeadersInfo(httpReq);

                        msg = "Unknown request source! Request can not be fulfilled.";

                        LOGGER.error("source[Origin/Referer] not found! " + msg);
                        httpResp.addHeader("errorMessage", msg);
                        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Origin/Referer not found! " + msg);
                        return;
                    }

                }
                //Compare the source against the expected target origin
                URL targetOriginURL = new URL(targetOrigin);

                URL sourceURL = null;
                try {
                    sourceURL = new URL(source);

                } catch (MalformedURLException e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    LOGGER.error(stack.toString());
                    stack = null;
                    logHeadersInfo(httpReq);
                    if (!httpResp.isCommitted()) {
                        msg = "Invalid/Malformed request! Request can not be fulfilled.";

                        httpResp.addHeader("errorMessage", msg);
                        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid/Malformed URL format! " + msg);
                    }
                    return;
                } catch (Exception e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    LOGGER.error(stack.toString());
                    stack = null;
                    logHeadersInfo(httpReq);
                    if (!httpResp.isCommitted()) {
                        msg = "Invalid/Malformed request! Request can not be fulfilled.";

                        httpResp.addHeader("errorMessage", msg);
                        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid/Malformed URL format! " + msg);
                    }
                    return;
                }

                if (RPConfig.getPropertyList().get(0).isSSO()) {
                    RPConfig.getCsrfExcludeUrlSet().remove("/resources/RPRequest");
                }

//                if (!(RPConfig.getCsrfExcludeUrlSet().contains(sRequestURI))) {
                if (!targetOriginURL.getProtocol().equals(sourceURL.getProtocol())
                        || !targetOriginURL.getHost().equals(sourceURL.getHost())
                        || targetOriginURL.getPort() != sourceURL.getPort()) {
                    //One the part do not match so we trace the event and we block the request
                    String accessDeniedReason = String.format("Protocol/Host/Port "
                            + "do not fully matches so we block the request! (%s != %s) ",
                            targetOrigin, sourceURL);
                    LOGGER.error(accessDeniedReason);
                    if (!httpResp.isCommitted()) {
                        msg = "Unknown access URL! URL not matched with the URL maintained in backend parameters configuration. Please retry with a valid URL.";

                        httpResp.addHeader("errorMessage", msg);
                        httpResp.sendError(HttpServletResponse.SC_FORBIDDEN, "URL not matched with the URL maintained in backend parameters configuration! " + msg);
                    }
                    LOGGER.debug("requestURI:: " + sRequestURI + " :: checked Protocol/Host/Port");
                    logHeadersInfo(httpReq);
                    return;
                }
//                } else {
//                    LOGGER.debug("requestURI:: " + sRequestURI + " ::NOT::CHECKED:: Protocol/Host/Port");
//                }
            }
            boolean adminConfigSync = false;
            if (sRequestURI.contains("/resources/ConfigSync")) {
                if (httpReq.getParameter("sync") != null) {
                    adminConfigSync = true;
                }
            }
            boolean adminSyncSystemDesc = false;
            if (sRequestURI.contains("/resources/Systems/SyncSystemDesc")) {
                if (httpReq.getParameter("sync") != null) {
                    adminSyncSystemDesc = true;
                }
            }

            if (RPConfig.getCsrfExcludeUrlSet().contains(sRequestURI)) {

                if (!(httpReq.getMethod().equals("POST")
                        && sRequestURI.equals("/resources/Systems"))
                        && !adminConfigSync
                        && !adminSyncSystemDesc) {

                    httpResp.addHeader("X-Csrf-Checked", "N");
                    try {
                        chain.doFilter(httpReq, httpResp);
                        return;
                    } catch (Throwable e) {
                        StringWriter stack = new StringWriter();
                        e.printStackTrace(new PrintWriter(stack));
                        LOGGER.error(stack.toString());
                        stack = null;
                        if (!httpResp.isCommitted()) {
                            msg = "Oops! Something went wrong. Please try again after sometime. If the issue still persists contact support.";
                            httpResp.addHeader("errorMessage", msg);
                            httpResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Oops! Something went wrong. Please try again after sometime. If the issue still persists contact support.");
                        }
                    }

                    LOGGER.debug("requestURI:: " + sRequestURI);

                    return;
                }
            }
            /**
             * X-Requested-With: XMLHttpRequest
             */

            //if (!sRequestURI.contains("/resources/files")) {
            String x_requested = null;
            if (httpReq.getHeader("X-Requested-With") != null) {
                x_requested = httpReq.getHeader("X-Requested-With");
            }
            if (x_requested == null
                    || !"XMLHttpRequest".equals(x_requested)) {

                logHeadersInfo(httpReq);
                if (!httpResp.isCommitted()) {
                    msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";
                    LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                    httpResp.addHeader("errorMessage", msg);
                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "X-Requested-With/XMLHttpRequest missing! " + "Error: e001 - Sorry! Request can not be completed.");
                }
                return;
            }

            String sessionToken = null;
            if (httpReq.getSession(false) == null) {

                if (!httpResp.isCommitted()) {
                    msg = "Session timeout! Please re-try with new browser session.";
                    LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                    httpResp.addHeader("errorMessage", msg);

                    httpResp.addHeader("sessionAlive", "N");

                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No valid session found! Please re-try with new browser session.");
                }
                return;

            } else if (httpReq.getSession(false).getAttribute("x-csrf-token-rp") == null) {
                if (!httpResp.isCommitted()) {
                    msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";
                    LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                    httpResp.addHeader("errorMessage", msg);

                    if (httpReq.getSession(false) != null) {
                        httpReq.getSession(false).invalidate();
                    }
                    httpResp.addHeader("sessionAlive", "N");

                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "X-token missing in session! Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.");
                }
                return;

            } else {
                sessionToken = httpReq.getSession(false).getAttribute("x-csrf-token-rp").toString();
            }
            // }
            /**
             * Check Cookie x-csrf-token-rp
             */
            String cookieToken = null;
            if (httpReq.getCookies() != null) {
                for (Cookie curCookie : httpReq.getCookies()) {
                    if (curCookie.getName().equals("x-csrf-token-rp")) {
                        cookieToken = curCookie.getValue();
                    }
                }
            }
            if (cookieToken == null || sessionToken == null) {

                logHeadersInfo(httpReq);

                if (!httpResp.isCommitted()) {
                    msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";

                    LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                    httpResp.addHeader("errorMessage", msg);

                    if (httpReq.getSession(false) != null) {
                        httpReq.getSession(false).invalidate();
                    }
                    httpResp.addHeader("sessionAlive", "N");

                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cookie x-token missing! Error: e002 - Sorry! Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.");
                }
                return;
            }

            if (!cookieToken.equals(sessionToken)) {

                logHeadersInfo(httpReq);

                if (!httpResp.isCommitted()) {

                    msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";

                    LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                    httpResp.addHeader("errorMessage", msg);

                    if (httpReq.getSession(false) != null) {
                        httpReq.getSession(false).invalidate();
                    }
                    httpResp.addHeader("sessionAlive", "N");

                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "cookie & session x-token not matched! Error: e003 - Sorry! Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.");
                }
                return;
            }

            if (httpReq.getHeader("X-Enc-Csrf-Token") == null) {

                logHeadersInfo(httpReq);

                if (!httpResp.isCommitted()) {

                    msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";

                    LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                    httpResp.addHeader("errorMessage", msg);

                    if (httpReq.getSession(false) != null) {
                        httpReq.getSession(false).invalidate();
                    }
                    httpResp.addHeader("sessionAlive", "N");

                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Encypted x-token missing! Error: e004 - Sorry! Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.");
                }
                return;
            }
            String requestToken = httpReq.getHeader("X-Enc-Csrf-Token");
            EncryptDecrypt rpc = new EncryptDecrypt();
            requestToken = rpc.decrypt(requestToken);
            rpc = null;

            if (sRequestURI.contains("/resources/Systems")
                    || sRequestURI.contains("/resources/AdminLogin")
                    || sRequestURI.contains("/resources/AdminList")
                    || adminConfigSync) {
                if (!requestToken.startsWith("rp_admin_")) {

                    logHeadersInfo(httpReq);

                    if (!httpResp.isCommitted()) {

                        msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";

                        LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                        httpResp.addHeader("errorMessage", msg);

                        if (httpReq.getSession(false) != null) {
                            httpReq.getSession(false).invalidate();
                        }
                        httpResp.addHeader("sessionAlive", "N");

                        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Admin x-token not valid! Error: e005 - Sorry! Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.");
                    }
                    return;
                }
            } else {
                if (requestToken == null
                        || requestToken.trim().length() < 32) {
                    //|| requestToken.startsWith("rr_admin_")) {

                    logHeadersInfo(httpReq);

                    if (!httpResp.isCommitted()) {

                        msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";

                        LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                        httpResp.addHeader("errorMessage", msg);

                        if (httpReq.getSession(false) != null) {
                            httpReq.getSession(false).invalidate();
                        }
                        httpResp.addHeader("sessionAlive", "N");

                        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "RP x-token missing or invalid! Error: e006 - Sorry! Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.");
                    }
                    return;
                }
            }

            //LOGGER.info("token r: " + requestToken);
            //LOGGER.info("token c:  " + cookieToken);
            if (!requestToken.equals(cookieToken)) {

                logHeadersInfo(httpReq);

                if (!httpResp.isCommitted()) {
                    msg = "Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.";

                    LOGGER.error("requestURI:: " + sRequestURI + "\n" + msg);

                    httpResp.addHeader("errorMessage", msg);

                    if (httpReq.getSession(false) != null) {
                        httpReq.getSession(false).invalidate();
                    }
                    httpResp.addHeader("sessionAlive", "N");

                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Header and cookie x-token not matched! Error: e007 - Sorry! Invalid or missing credentials! Request can not be fulfilled. Please retry in new browser session.");
                }
                return;

            }
//            if (!sRequestURI.contains("isSessionAlive")) {
//                LOGGER.debug("Http(s) request for " + sRequestURI + " and renewing token validity as well.");
//            }

            if (httpReq.getCookies() != null) {
                for (Cookie curCookie : httpReq.getCookies()) {
                    if (curCookie.getName().equals("x-csrf-token-rp")) {
                        String token = httpReq.getSession(false).getAttribute("x-csrf-token-rp").toString();
                        int cookieMaxAge = httpReq.getSession(false).getMaxInactiveInterval();
                        String cookieContext = request.getServletContext().getContextPath();
                        String cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                        if (request.isSecure()) {
                            cookieSpec = String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Strict", "x-csrf-token-rp", token, cookieContext, cookieMaxAge);
                        }
                        httpResp.addHeader("Set-Cookie", cookieSpec);
                    }
                }
            }

            //System.out.println("csrf Filter check:: pass");
            httpResp.addHeader("X-Csrf-Checked", "Y");
            try {
                chain.doFilter(httpReq, httpResp);
            } catch (Throwable t) {

                StringWriter stack = new StringWriter();
                t.printStackTrace(new PrintWriter(stack));
                LOGGER.error(stack.toString());
                stack = null;
                if (!httpResp.isCommitted()) {

                    msg = "Oops! Something went wrong. Please try again after sometime. If the issue still persists contact support.";

                    httpResp.addHeader("errorMessage", msg);
                    httpResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Oops! Something went wrong. Please try again after sometime. If the issue still persists contact support.");
                }
            }

            return;
            //}

        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            StringWriter stack = new StringWriter();
            t.printStackTrace(new PrintWriter(stack));
            LOGGER.error(stack.toString());
            stack = null;
            if (!httpResp.isCommitted()) {

                String msg = "Oops! Something went wrong. Please try again after sometime. If the issue still persists contact support.";

                httpResp.addHeader("errorMessage", msg);
                httpResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Oops! Something went wrong. Please try again after sometime. If the issue still persists contact support.");
            }

        }

        //doAfterProcessing(request, response);
        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(FilterConfig config) throws ServletException {

    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("CsrfProtectionFilter()");
        }
        StringBuilder sb = new StringBuilder("CsrfProtectionFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (IOException ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (IOException ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (IOException ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    private void logHeadersInfo(HttpServletRequest request) {

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            LOGGER.info(key + ": " + value);

        }

    }

}
