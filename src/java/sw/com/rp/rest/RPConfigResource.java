/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.dto.Config_flag;
import sw.com.rp.transformer.JsonTransformer;

/**
 * REST Web Service
 *
 * @author Admin
 */
@Path("RPConfig")
public class RPConfigResource {

    @Context
    private UriInfo context;
    static final Logger logger = LogManager.getLogger(RPConfigResource.class.getName());

    /** Creates a new instance of PAConfigResource */
    public RPConfigResource() {
    }

    /**
     * Retrieves representation of an instance of com.sw.pa.rest.PAConfigResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        logger.info("Start of  getJson() WebServices method ...");
        //TODO return proper representation object
        //throw new UnsupportedOperationException();
        String jsonData = null;

        ListWrapper<Config_flag> w = new ListWrapper<Config_flag>();
        w.setData(RPConfig.getPropertyList());
        w.setTotal(RPConfig.getPropertyList().size());
        if (RPConfig.getPropertyList().isEmpty()) {
            w.setSuccess(Boolean.FALSE);
            w.setMessage("Please check the configuration");
        } else {
            w.setSuccess(Boolean.TRUE);
        }
        try {
            JsonTransformer transformer = new JsonTransformer();
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        logger.info("return RP Properties json success.");
        logger.info("End of  getJson() WebServices method ...");
        return jsonData;
    }

    /**
     * PUT method for updating or creating an instance of PAConfigResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    @Path("getLangList")
    @GET
    @Produces("application/json")
    public String getLanguageList() {
        //TODO return proper representation object
        logger.info("Start of  getLangugaeList() WebService method ...");
        String jsonData = "{\"message\" : \" Session destroyed \",\"success\" : false}";
        String message = "";
        try {
            HashMap<String, String> langList = RPConfig.getLangListMap();
            JsonTransformer transformer = new JsonTransformer();
            jsonData = transformer.transformToJson(langList);
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            jsonData = "{\"message\" : \" Session destroyed \",\"success\" : false}";
        }
        logger.info("End of  getLangList() WebServices method ...");
        return jsonData;
    }
}
