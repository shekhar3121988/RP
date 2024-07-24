<%-- 
    Document   : index
    Created on : Jun 7, 2016, 12:10:47 PM
    Author     : SW
--%>

<%@page import="com.google.common.base.CharMatcher"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="sw.com.rp.config.RPConfig" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html id ="htmlMain" class = "loading">
    <head>
        <link href="images/favicon.ico" rel="icon" type="image/x-icon"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Security-Policy"
              content="default-src 'self' ;
              script-src 'self' ;
              style-src 'self' 'unsafe-inline' ;
              ">
        <title>Reset Password (RP)</title>
    </head>

    <%
        Logger log = LogManager.getLogger("home.jsp");
        // String name = request.getParameter("GID");
        // out.print("welcome " + name);
        log.info("home page");
        try {
            String name = null;
            String value = null;
            String view = null;
            String sys = null;
            //  String pageValue;
            String contextPath = request.getContextPath();
            log.info("contextPath: " + contextPath);
            if (RPConfig.getConfig_flag().getREQPARAM() == null) {

                log.error("Configuration not synced.... Please check frontend system configuation.");
                out.println("<br><br><br><center><div style='width:25%'><h3 style='background-color:#FFFAA8;color:#FF2C02'>SP Configuration not synced !!</h3></div></center>");

                out.println("<form name='frmParam' action='" + contextPath + "/index.html' method='GET'> ");
                out.println("<input type='hidden' name='View' value='Config'>");
                out.println("</form>");
                //out.println("<script language='Javascript'>");
                //out.println("document.frmParam.submit();");
                //out.println("</script>");
                out.println("<script src='js/ssoForm.js'></script>");
                out.close();
            }
            out.println("<body style='background-color: rgb(239, 249, 254);'>");

            name = RPConfig.getConfig_flag().getREQPARAM();

            //System.out.println(":" + SPConfig.getProConfigList().getWEB_CNF_HTTP_HEADER());
            value = request.getParameter(RPConfig.getConfig_flag().getREQPARAM());
            if (value == null) {
                if (request.getSession(false) != null) {
                    if (request.getSession(false).getAttribute(RPConfig.getConfig_flag().getREQPARAM()) != null) {
                        value = (String) request.getSession(false).getAttribute(RPConfig.getConfig_flag().getREQPARAM());
                        request.getSession(false).setAttribute("resumeSession", "Y");
                        log.info("Resuming Login Session");
                    }
                    //RPConfig.getConfig_flag().getREQPARAM()))
                }
            } else {
                if (request.getSession(false) == null) {
                    request.getSession(true);
                } else {
                    request.getSession(false).invalidate();
                    request.getSession(true);
                }
                log.info("New Login Session");
                if (CharMatcher.ascii().matchesAllOf(value)) {
                    request.getSession(false).setAttribute(RPConfig.getConfig_flag().getREQPARAM(), value.toUpperCase());
                } else {
                    request.getSession(false).setAttribute(RPConfig.getConfig_flag().getREQPARAM(), value);
                }

            }
            view = request.getParameter("View");
            sys = request.getParameter("sys");

            //out.print("welcome " + name);
            if (value == null || value.length() <= 0) {
                Enumeration headerNames = request.getHeaderNames();
                String key = "";
                while (headerNames.hasMoreElements()) {

                    key = (String) headerNames.nextElement();

                    if (key.equalsIgnoreCase(RPConfig.getConfig_flag().getREQPARAM())) {
                        value = request.getHeader(key);
                    }

                    //   out.print("key= " + key);
                    //   out.print("value= " + value);
                }

            }

            if (RPConfig.getConfig_flag().isSSO()) {
                //                       // if(false){
                //String s = RPConfig.getConfig_flag().isGETSSO();
                // System.out.println("s:::"+s);
                if (!RPConfig.getConfig_flag().isGETSSO() && request.getMethod().equalsIgnoreCase("GET")) {
                    out.println("<div style='color:red; margin-left:10px; width: 250px;margin-top:15px'><hr/>Error: Invalid SSO request!!<hr/></div>");
                } else {
                    if (value != null && value.length() > 0) {

                        // out.println("<body style='background-color: rgb(239, 249, 254);'>");
                        out.println("<br><br><br><center><h3>Please wait...</h3></center>");
                        //  out.println("<br><br><br><center><img src='./images/loading.gif' ></center>");
                        //     out.println(" <div id='splash-screen'> <div class='splash-screen-text'>Loading</div> <div class='splash-screen-circle-outer'></div><div class='splash-screen-circle-inner'></div> </div>");
                        out.println("<form name='frmParam' action='" + contextPath + "/SSO' method='POST'>");
                        //out.println("<input type='hidden' name='" + name + "' value='" + value + "'>");
                        if ((view != null) && (view.length() > 0)) {
                            out.println("<input type='hidden' name='View' value='" + view + "'>");
                        }

                        out.println("</form>");
                        out.println("<script src='js/ssoForm.js'></script>");
                        //out.println("<script language='Javascript'>");
                        //out.println("document.frmParam.submit();");
                        //out.println("</script>");
                        out.println("</body>");
                    } else {
                        out.println("<div style='color:red; margin-left:10px; width: 250px;margin-top:15px'>Error: Missing SSO credentials!!<hr/></div>");
                    }
                }

            } else {
                //out.println("<body style='background-color: rgb(239, 249, 254);'>");
                out.println("<br><br><br><center><h3>Please wait...</h3></center>");
                // out.println("<br><br><br><center><img src='./images/loading.gif' ></center>");
                // out.println(" <div id='splash-screen'> <div class='splash-screen-text'>Loading</div> <div class='splash-screen-circle-outer'></div><div class='splash-screen-circle-inner'></div> </div>");

                out.println("<form name='frmParam' action='" + contextPath + "/index.html' method='GET'> ");

                if ((view != null) && (view.length() > 0)) {
                    out.println("<input type='hidden' name='View' value='" + view + "'>");
                }
                out.println("</form>");
                out.println("<script src='js/ssoForm.js'></script>");
                //out.println("<script language='Javascript'>");
                //out.println("document.frmParam.submit();");
                //out.println("</script>");

                // out.println("<h3>Invalid SSO request!! </h3>");
            }

        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            log.error(stack.toString());
            stack = null;
            out.println("<body style='background-color: rgb(239, 249, 254);'>");
            out.println("<div style='color:red; margin-left:10px; width: 250px;margin-top:15px'>"
                    + "Opps.. something went wrong!<hr/>"
                    + "</div>");
            out.println("</body>");

        }
        out.println("</body>");


    %>
</html>
