/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;

/**
 *
 * @author msuppahiya
 */
public class GetLogs extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/zip");
        response.setHeader("Content-Disposition", "attachment; filename=RP_LOGS.zip");
//        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetLogs</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetLogs at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            Configuration config = ctx.getConfiguration();
            Appender logFileAppender = config.getAppenders().get("file-log");
            RollingFileAppender rollingFileAppender = (RollingFileAppender) logFileAppender;
            String logFileName = rollingFileAppender.getFileName();

            ServletOutputStream ouputStream = response.getOutputStream();

            ZipOutputStream zos = new ZipOutputStream(ouputStream);
            ZipEntry entry = new ZipEntry("RP.log");
            zos.putNextEntry(entry);


            FileInputStream in = new FileInputStream(logFileName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
            zos.closeEntry();

            //remember close it
            zos.close();

        } finally {
//            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
