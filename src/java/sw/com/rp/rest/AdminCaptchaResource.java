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
package sw.com.rp.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("adminCaptcha")
public class AdminCaptchaResource {

    static final Logger LOGGER = LogManager.getLogger(AdminCaptchaResource.class.getName());

    /**
     * Creates a new instance of AdminCaptchaResource
     */
    public AdminCaptchaResource() {
    }

    /**
     * Returns captcha image for admin login
     *
     * @param request
     * @param response
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("image/png")
    public Response getAdminCaptcha(@Context HttpServletRequest request,
            @Context HttpServletResponse response) {
        LOGGER.debug("start of getAdminCaptcha() ");

        try {
            synchronized (request) {
                request.wait(1000);//Paralyse Brute force
            }

            File tmpDir;

            // Try to create captacha image at tomcat/webapps/RR
            String fullPath = URLDecoder.decode(this.getClass().getClassLoader().getResource("").getPath(), "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            fullPath = pathArr[0] + "/temp";
            tmpDir = new File(fullPath);
            if (!tmpDir.exists()) {
                LOGGER.debug("Trying to create temp directory:  " + fullPath);
                if (!tmpDir.mkdir()) {
                    tmpDir = null;
                    LOGGER.error("No permission to create directory:  " + fullPath);
                }
            } else {
                try {
                    if (!tmpDir.isDirectory() && !tmpDir.canRead() && !tmpDir.canWrite()) {
                        LOGGER.error("No read-write access to " + tmpDir.getPath());
                        tmpDir = null;
                    }
                } catch (Exception ex) {
                    LOGGER.error("No read-write access to path " + fullPath);
                    StringWriter stack = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stack));
                    LOGGER.error(stack.toString());
                    tmpDir = null;
                }
            }

            if (tmpDir == null) {//Re-try to create captacha image at the path set in "java.io.tmpdir"
                LOGGER.warn("Try #1 - Failed to create temp directory: " + fullPath);
                LOGGER.debug("Trying to create temp directory:  " + System.getProperty("java.io.tmpdir"));
                if (System.getProperty("java.io.tmpdir") != null
                        && System.getProperty("java.io.tmpdir").trim().length() > 0) {

                    tmpDir = new File(System.getProperty("java.io.tmpdir"));

                    if (!tmpDir.exists()) {

                        LOGGER.debug("The temp directory doesn't exist. Trying to create directory:  " + System.getProperty("java.io.tmpdir"));

                        if (!tmpDir.mkdir()) {

                            tmpDir = null;

                            LOGGER.error("No permission to create temp directory:  " + System.getProperty("java.io.tmpdir"));

                        }

                    } else {
                        try {
                            if (!tmpDir.isDirectory() && !tmpDir.canRead() && !tmpDir.canWrite()) {
                                LOGGER.error("Not a directory / No read-write access:  " + System.getProperty("java.io.tmpdir"));
                                tmpDir = null;
                            }
                        } catch (Exception ex) {
                            LOGGER.error("Not a directory! or No read-write access: " + System.getProperty("java.io.tmpdir"));
                            StringWriter stack = new StringWriter();
                            ex.printStackTrace(new PrintWriter(stack));
                            LOGGER.error(stack.toString());
                            tmpDir = null;
                        }
                    }
                }

            }
            if (tmpDir == null) {
                LOGGER.error("Try #2 - Failed to create temp directory: " + System.getProperty("java.io.tmpdir"));
                LOGGER.debug("end of getAdminCaptcha() with error.");
                return Response.status(Response.Status.NOT_FOUND).entity("Failed to save image in .../webapps/RR  and  " + System.getProperty("java.io.tmpdir")).build();

            } else {
                LOGGER.info("temp directory path used:  " + tmpDir.getPath());
            }

            File outputfile = new File(tmpDir.getPath() + "//rpadmin.png");

            CaptchaImageUtil obj = new CaptchaImageUtil();
            BufferedImage ima = obj.getCaptchaImage();

            ImageIO.write(ima, "png", outputfile);

            String captchaStr = obj.getCaptchaString();

            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("AdminCaptchaStr", captchaStr);

            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //ImageIO.write(ima, "png", baos);

            //byte[] imageData = baos.toByteArray();
            LOGGER.debug("end of getAdminCaptcha(): All-OK.");
            return Response.ok((Object) outputfile).build();

        } catch (IOException | InterruptedException ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            LOGGER.error(stack.toString());
            LOGGER.debug("end of getAdminCaptcha() with error");
            return Response.status(Response.Status.NOT_FOUND).entity(" Error: " + ex.getMessage()).build();
        }

    }

}
