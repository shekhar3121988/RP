/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.dto.Admininfo;
import sw.com.rp.transformer.JsonTransformer;

/**
 *
 * @author msaini
 */
@Path("/AdminList")
public class AdminListResource {

    private static final Logger logger = LogManager.getLogger(AdminListResource.class.getName());

    /**
     *
     * This method is used to get list of Admin
     * @return ArrayList of Admin
     *
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAdminList(@Context HttpServletRequest request)
            throws IOException, Throwable {
          logger.info("Start of  getAdminList() WebServices method ...");
        ListWrapper<Admininfo> w = new ListWrapper<Admininfo>();
        ArrayList<Admininfo> newadmin = RPConfig.getAdmininfoList();
        ArrayList<Admininfo> AdminList = new ArrayList<Admininfo>();
        try {
            for (int i = 0; i < newadmin.size(); i++) {
                Admininfo ad = new Admininfo();
                ad.setUserID(newadmin.get(i).getUserID());
                AdminList.add(ad);
                if (AdminList.isEmpty()) {
                    return "{\"message\" : \" Error in reading admin file \",\"success\" : false}";
                } else {
                    w.setData(AdminList);
                    w.setSuccess(true);
                }
            }
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \" Error in reading admin file \",\"success\" : false}";

        }

        w.setTotal(AdminList.size());
        String jsonData = "";
        JsonTransformer transformer = new JsonTransformer();
        try {
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + ex.getMessage() + "\",\"success\" : false}";
        }
         logger.info("End of  getAdminList() WebServices method ...");
        return jsonData;

    }
}
