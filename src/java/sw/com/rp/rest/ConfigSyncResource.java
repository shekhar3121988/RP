/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.config.RPInitApp;
import static sw.com.rp.rest.SystemsResource.logger;

/**
 *
 * @author msaini
 */
@Path("ConfigSync")
public class ConfigSyncResource {

    static final Logger logger = LogManager.getLogger(ConfigSyncResource.class.getName());

    @GET
    @Produces("application/json")
    public String getConfigSync(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("Start of  getConfigSync() WebServices method ...");
        if(request.getParameter("sync") == null && !RPConfig.syncConfig){
            logger.info("- - - ");
            logger.info("- - - ");
            logger.info("------------> RP ConfigSync. already loaded.");
            logger.info("- - - ");
            logger.info("- - - ");
            
            return "{\"message\" : \"RP Config. already loaded \",\"success\" : true}";
        }else if(request.getParameter("sync") != null){
            logger.info("- - - ");
            logger.info("- - - ");
            logger.info("********************Sync RP Config by Admin***************************");
            logger.info("- - - ");
            logger.info("- - - ");
        }else{
            logger.info("- - - ");
            logger.info("- - - ");
            logger.info("********************Sync Config first time***************************");
            logger.info("- - - ");
            logger.info("- - - ");
        }
        HttpSession session = request.getSession(false);
        try {
            RPInitApp Init = new RPInitApp();
            Init.init(RPInitApp.servletConfig);
            RPConfig.syncConfig = false;
            logger.info("End of  getConfigSync() WebServices method ...");
            return "{\"message\" : \" Successfully load\",\"success\" : true}";
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            logger.info("End of  getConfigSync() WebServices method ...");
            return "{\"message\" : \"" + e.getMessage() + "\",\"success\" : false}";
        }
    }
}
