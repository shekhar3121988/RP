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

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sw.com.rp.config.RPConfig;

public class ResourcesFilter implements Filter {

    private static final boolean debug = true;
    private String mode = "DENY";

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

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
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        String configMode = filterConfig.getInitParameter("mode");
        if (configMode != null) {
            this.mode = configMode;
//             System.out.println("Mode-1 "+ mode); 
        } else {
//              System.out.println("Mode-2 "+ mode); 
        }
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
//                log("resourcesFilter:Initializing filter");
            }
        }
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    public ResourcesFilter() {
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
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

//        log("resourcesFilter:doFilter()");

        //doBeforeProcessing(request, response);
        Throwable problem = null;
        try {

//            System.out.println("mode- " + mode);
            HttpServletRequest httpReq = (HttpServletRequest) request;
            String reqURI = httpReq.getRequestURI();
            HttpServletResponse res = (HttpServletResponse) response;
            res.addHeader("X-FRAME-OPTIONS", mode);
            res.addHeader("X-XSS-Protection", "1; mode=block");
            res.addHeader("X-Content-Type-Options", "nosniff");
            if (reqURI.contains("/i18n/")
                    || reqURI.contains("/images/")
                    || reqURI.contains("/css/")
                    || reqURI.contains("/preload/")
                    || reqURI.contains("/modules/")
                    || reqURI.contains("/js/")
                    || reqURI.contains("/Component")) {

                //renews cached static-files only if the files are changed on the server
                res.addHeader("Cache-Control", "private, no-cache, must-revalidate, max-age=0");

            } else {
                //No-Cache
                res.addHeader("Cache-Control", "private, no-store, no-cache, must-revalidate, max-age=0");
            }
            res.addHeader("Pragma", "no-cache");
            //Set content type for i18n properties file
            if (reqURI.endsWith(".properties")) {
                res.addHeader("Content-Type", "text/plain;charset=UTF-8");
            }
            /* handle redirect to sso - commented, not needed.
            if (RPConfig.getConfig_flag().isSSO()
                    && reqURI.contains("index.html")
                    && httpReq.getParameter("View") == null) {

                if (httpReq.getSession(false) != null) {
                    if (httpReq.getSession(false).getAttribute(RPConfig.getConfig_flag().getREQPARAM()) == null) {
                        if (!res.isCommitted()) {
                            res.sendRedirect(res.encodeRedirectURL(httpReq.getContextPath()));
                            return;
                        }
                    }
                    //RPConfig.getConfig_flag().getREQPARAM()))
                }
            }
             */

            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }

        doAfterProcessing(request, response);

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

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
//            log("resourcesFilter:DoBeforeProcessing");
        }

    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
//            log("resourcesFilter:DoAfterProcessing");
        }

    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("resourcesFilter()");
        }
        StringBuffer sb = new StringBuffer("resourcesFilter(");
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
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
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
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
