/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sw.util.SecCrypt;
import com.sw.util.SecStore;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
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
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Transform;
import org.ini4j.Wini;
import sw.com.rp.config.RPConfig;
import sw.com.rp.config.RPInitApp;
import sw.com.rp.connection.ADconnection;
import sw.com.rp.connection.ADserver;
import sw.com.rp.connection.SAPConnection;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dao.BapiConfiguration;
import sw.com.rp.dao.SAPserver;
import sw.com.rp.dto.SystemPOJO;
import sw.com.rp.transformer.JsonTransformer;

/**
 * REST Web Service
 *
 * @author msaini
 */
@Path("Systems")
public class SystemsResource {

    @Context
    private UriInfo context;
    static final Logger logger = LogManager.getLogger(SystemsResource.class.getName());

    /**
     * Creates a new instance of SystemsResource
     */
    public SystemsResource() {
    }

    /**
     * Retrieves representation of an instance of com.sw.pa.rest.SystemsResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        ListWrapper<SapSystem> w = new ListWrapper<SapSystem>();
        ArrayList<SapSystem> systemList = new ArrayList<SapSystem>();
        for (Iterator<SapSystem> it = RPConfig.getSapSystemList().iterator(); it.hasNext();) {

            SapSystem SAPSystem = it.next();
            SapSystem sapSys = new SapSystem();

            sapSys.setName(SAPSystem.getName());
            sapSys.setIpaddress(SAPSystem.getIpaddress());
            sapSys.setRfcname(SAPSystem.getRfcname());
            sapSys.setUser(SAPSystem.getUser());
            sapSys.setPassword("");
            sapSys.setLang(SAPSystem.getLang());
            sapSys.setClient(SAPSystem.getClient());
            sapSys.setSyno(SAPSystem.getSyno());
            sapSys.setHost(SAPSystem.getHost());
//            sapSys.setGroup(SAPSystem.getGroup());
            sapSys.setSncMode(SAPSystem.getSncMode());
            sapSys.setSncName(SAPSystem.getSncName());
            sapSys.setSncService(SAPSystem.getSncService());
            sapSys.setSncPartner(SAPSystem.getSncPartner());
            sapSys.setSncLevel(SAPSystem.getSncLevel());
            sapSys.setSncFlag(SAPSystem.getSncFlag());
            sapSys.setLoadBalancing(SAPSystem.getLoadBalancing());
            sapSys.setLbService(SAPSystem.getLbService());
            sapSys.setLbGroupName(SAPSystem.getLbGroupName());
            sapSys.setLbR3Name(SAPSystem.getLbR3Name());
            sapSys.setRouterFlag(SAPSystem.getRouterFlag());
            sapSys.setRouterString(SAPSystem.getRouterString());
            sapSys.setQueryAction(SAPSystem.getQueryAction());
            sapSys.setBackup(SAPSystem.getBackup());
            sapSys.setENFlag(SAPSystem.getENFlag());

            systemList.add(sapSys);

        }

        for (String key : RPConfig.getBackupSapSystemMap().keySet()) {
            SapSystem SAPsys = RPConfig.getBackupSapSystemMap().get(key);
            SapSystem sapn = new SapSystem();
            sapn.setName(SAPsys.getName());
            sapn.setIpaddress(SAPsys.getIpaddress());
            sapn.setRfcname(SAPsys.getRfcname());
            sapn.setUser(SAPsys.getUser());
            sapn.setPassword("");
            sapn.setLang(SAPsys.getLang());
            sapn.setClient(SAPsys.getClient());
            sapn.setSyno(SAPsys.getSyno());
            sapn.setHost(SAPsys.getHost());
//            sapn.setGroup(SAPsys.getGroup());
            sapn.setSncMode(SAPsys.getSncMode());
            sapn.setSncName(SAPsys.getSncName());
            sapn.setSncService(SAPsys.getSncService());
            sapn.setSncPartner(SAPsys.getSncPartner());
            sapn.setSncLevel(SAPsys.getSncLevel());
            sapn.setSncFlag(SAPsys.getSncFlag());
            sapn.setLoadBalancing(SAPsys.getLoadBalancing());
            sapn.setLbService(SAPsys.getLbService());
            sapn.setLbGroupName(SAPsys.getLbGroupName());
            sapn.setLbR3Name(SAPsys.getLbR3Name());
            sapn.setRouterFlag(SAPsys.getRouterFlag());
            sapn.setRouterString(SAPsys.getRouterString());
            sapn.setQueryAction(SAPsys.getQueryAction());
            sapn.setBackup(SAPsys.getBackup());
            sapn.setENFlag(SAPsys.getENFlag());

            systemList.add(sapn);
        }

        w.setData(systemList);
        w.setSuccess(Boolean.TRUE);
        w.setTotal(systemList.size());

        String jsonData = "";
        try {
            JsonTransformer transformer = new JsonTransformer();
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        w = null;
        //System.out.println(jsonData);
        return jsonData;

        // throw new UnsupportedOperationException();
    }

    @Path("List")
    @GET
    @Produces("application/json")
    public String getList() {
        //TODO return proper representation object
        ListWrapper<SystemPOJO> w = new ListWrapper<SystemPOJO>();
        ArrayList<SystemPOJO> systemList = new ArrayList<SystemPOJO>();
        // systemList = RPConfig.getSapSystemList();
        systemList.clear();
        SystemPOJO sysdropvalue = new SystemPOJO();
        /*
         * rpSystemsTreeMap to sort system buy Name
         * Case #16265 : Zekelman Industries
         * RP system selection order.
         */

        TreeMap<String, SapSystem> rpSystemsTreeMap = new TreeMap<String, SapSystem>();
        rpSystemsTreeMap.putAll(RPConfig.getSapSystemMap());
        for (String key : rpSystemsTreeMap.keySet()) {

            if (systemList.isEmpty()) {
                sysdropvalue = new SystemPOJO();
                sysdropvalue.setSyskey("Select");
                sysdropvalue.setSystemname("Select");
                systemList.add(sysdropvalue);
            }
            sysdropvalue = new SystemPOJO();
            sysdropvalue.setSyskey(rpSystemsTreeMap.get(key).getName());
            if (rpSystemsTreeMap.get(key).getDescription() != null && rpSystemsTreeMap.get(key).getDescription().length() > 0) {
                sysdropvalue.setSystemname(rpSystemsTreeMap.get(key).getName() + "-" + rpSystemsTreeMap.get(key).getDescription());
            } else {
                sysdropvalue.setSystemname(rpSystemsTreeMap.get(key).getName());
            }
            systemList.add(sysdropvalue);
        }

        w.setData(systemList);
        w.setSuccess(Boolean.TRUE);
        w.setTotal(systemList.size());
        String jsonData = "";
        try {
            JsonTransformer transformer = new JsonTransformer();
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        w = null;
        //System.out.println(jsonData);
        return jsonData;
    }

    @Path("SyncSystemDesc")
    @GET
    @Produces("application/json")
    public String getSyncSystemDesc(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        logger.info("start of getSyncSystemDesc()");

        if (request.getParameter("sync") == null && !RPConfig.syncSystemDesc) {
            logger.info("- - - ");
            logger.info("- - - ");
            logger.info("------------> System(s) Desc. already loaded.");
            logger.info("- - - ");
            logger.info("- - - ");
            return "{\"message\" : \" System(s) Desc. already loaded \",\"success\" : true}";
        } else if (request.getParameter("sync") != null) {
            logger.info("- - - ");
            logger.info("- - - ");
            logger.info("********************Sync RP SystemDesc by Admin***************************");
            logger.info("- - - ");
            logger.info("- - - ");
        } else {
            logger.info("- - - ");
            logger.info("- - - ");
            logger.info("********************Sync SystemDesc first time***************************");
            logger.info("- - - ");
            logger.info("- - - ");
        }

        HttpSession session = request.getSession(true);
        String language = "EN";
        String errMsg = "";
        String lang = (String) session.getAttribute("LANG");
        if (lang != null) {
            if (lang.equalsIgnoreCase("English")) {
                language = "EN";
            } else if (lang.equalsIgnoreCase("German")) {
                language = "DE";
            }
        }
        String jsonData = "{\"message\" : \" null \",\"success\" : false}";
        try {
            String error = (String) session.getAttribute("Error");

            if (error == null || error.length() == 0) {
                BapiConfiguration sysDesc = new BapiConfiguration();
                if (RPConfig.getConfig_flag().isMULTI_HOST()) {
                    for (String key : RPConfig.getSapSystemMap().keySet()) {
                        String syskey = RPConfig.getSapSystemMap().get(key).getSystemKey();
                        sysDesc.setSyncSystemDesc(syskey, language);
                    }
                } else {
                    String sysKey = RPConfig.getSingleHostSystem().getSystemKey();
                    sysDesc.setSyncSystemDesc(sysKey, language);
                }
                ArrayList<SapSystem> systemList = sysDesc.sapSystemHashmapToArray(RPConfig.getSapSystemMap());
                for (SapSystem sys : systemList) {
                    String pass = sys.getPassword();
                    sys.setPassword(encrypt(pass));
                }
                SaveSystemInfoToFile(systemList);
                SAPserver sapProperty = new SAPserver();
                sapProperty.LoadSAPinfo(RPConfig.getRP_home());
                RPConfig.syncSystemDesc = false;

            } else {
                logger.error("Session(Error): " + error);
            }
        } catch (Throwable e) {
//             try {
//            JsonTransformer transformer = new JsonTransformer();
//            errMsg = transformer.transformToJson( e.getMessage());
//        } catch (Exception ex) {
//            StringWriter stack = new StringWriter();
//            ex.printStackTrace(new PrintWriter(stack));
//            logger.error(stack.toString());
//            stack = null;
//        }
            session.setAttribute("Error", e.getMessage());
            return jsonData = "{\"message\" : \" null \",\"success\" : false}";
//            try {
//                
//                String contextPath = request.getContextPath();
//                logger.info("context path : " + contextPath);
//              //  response.sendRedirect(response.encodeRedirectURL(contextPath + "?View=Error"));
//                return;
//            } catch (IOException ex) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//                stack = null;
//            }
        }
        logger.info("end of getSyncSystemDesc()");
        return jsonData = "{\"message\" : \" null \",\"success\" : true}";

    }

    @Path("List2")
    @GET
    @Produces("application/json")
    public String getList2() {
        //TODO return proper representation object
        ListWrapper<String> w = new ListWrapper<String>();
        ArrayList<String> systemList = new ArrayList<String>();
        // systemList = RPConfig.getSapSystemList();
        systemList.clear();
        SystemPOJO sysdropvalue = new SystemPOJO();
        for (SapSystem syslist : RPConfig.getSapSystemList()) {
            if (!syslist.getENFlag().equalsIgnoreCase("AD")) {
                systemList.add(syslist.getName());
            }
        }

        w.setData(systemList);
        w.setSuccess(Boolean.TRUE);
        w.setTotal(systemList.size());
        String jsonData = "";
        try {
            JsonTransformer transformer = new JsonTransformer();
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        w = null;
        //System.out.println(jsonData);
        return jsonData;
    }

    @Path("MultiList")
    @GET
    @Produces("application/json")
    public String getMultiList() {
        //TODO return proper representation object
        ListWrapper<SystemPOJO> w = new ListWrapper<SystemPOJO>();
        ArrayList<SystemPOJO> systemList = new ArrayList<SystemPOJO>();
        // systemList = RPConfig.getSapSystemList();
        systemList.clear();
        SystemPOJO sysdropvalue = new SystemPOJO();
        for (SapSystem syslist : RPConfig.getSapSystemList()) {
            sysdropvalue = new SystemPOJO();
            sysdropvalue.setSyskey(syslist.getName());
            sysdropvalue.setSystemname(syslist.getName());
            systemList.add(sysdropvalue);
        }

        w.setData(systemList);
        w.setSuccess(Boolean.TRUE);
        w.setTotal(systemList.size());
        String jsonData = "";
        try {
            JsonTransformer transformer = new JsonTransformer();
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        w = null;
        //System.out.println(jsonData);
        return jsonData;
    }

    @Path("getADSysList")
    @GET
    @Produces("application/json")
    public String getADsystemList() {
        //TODO return proper representation object
        ListWrapper<ADserver> w = new ListWrapper<ADserver>();
        ArrayList<ADserver> systemList = new ArrayList<ADserver>();

        for (Iterator<ADserver> it = RPConfig.getAdServerList().iterator(); it.hasNext();) {
            ADserver ADSystem = new ADserver();

            ADserver adSystem = it.next();

            ADSystem.setDomain(adSystem.getDomain());
            ADSystem.setIp(adSystem.getIp());
            ADSystem.setPort(adSystem.getPort());
            ADSystem.setUserid(adSystem.getUserid());
            ADSystem.setPassword("");
            ADSystem.setQueryAction(adSystem.getQueryAction());
            systemList.add(ADSystem);
        }

        w.setData(systemList);
        w.setSuccess(Boolean.TRUE);
        w.setTotal(systemList.size());
        String jsonData = "";
        try {
            JsonTransformer transformer = new JsonTransformer();
            jsonData = transformer.transformToJson(w);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        w = null;
        //System.out.println(jsonData);
        return jsonData;

        // throw new UnsupportedOperationException();
    }
//

    @POST
    @Produces("application/json")
    public String postJson(SapSystem sysData, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        ArrayList<SapSystem> sapnew = new ArrayList<SapSystem>();
        HttpSession session = request.getSession(false);
        SapSystem sapSYS = new SapSystem();
        HashMap<String, String> keyStoreMap = new HashMap<String, String>();// add or modify system
        HashMap<String, String> keyStoreRemoveMap = new HashMap<String, String>(); // delete system
        try {
            sapnew = (ArrayList<SapSystem>) JsonTransformer.transformToJavaObjects(sysData, SapSystem.class);

            sapSYS = sapnew.get(0);
            SapSystem existingSystem = RPConfig.getSapSystemMap().get(sapSYS.getName());
            if (existingSystem != null && sapSYS.getBackup().equalsIgnoreCase("No")
                    && sapSYS.getQueryAction().equalsIgnoreCase("N")) {
                //multilanguage code
                String text = getTranslatedText("Systemsmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Systems can not have same \'System Name\'.";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }
            SapSystem backupSAPSystem = RPConfig.getBackupSapSystemMap().get(sapSYS.getName());
            if (backupSAPSystem != null && sapSYS.getBackup().equalsIgnoreCase("Yes")
                    && sapSYS.getQueryAction().equalsIgnoreCase("N")) {
                //multilanguage code
                String text = getTranslatedText("Systemsmsg2", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Backup System(s) can not have same \'System Name\'.";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }

            if (sapSYS.getQueryAction().equalsIgnoreCase("D")) {
                int i = 0;
                for (SapSystem sapSys : RPConfig.getSapSystemList()) {
                    if (sapSys.getName().equalsIgnoreCase(sapSYS.getName())) {
                        keyStoreRemoveMap.put(sapSYS.getName() + SecCrypt.KEY_PADDING + SapSystem.UID, sapSYS.getName());
                        keyStoreRemoveMap.put(sapSYS.getName() + SecCrypt.KEY_PADDING + SapSystem.PWD, sapSYS.getName());
                        break;
                    }
                    i++;
                }
                if (sapSYS.getBackup().equalsIgnoreCase("No")) {
                    RPConfig.getSapSystemList().remove(i);
                    System.out.println("removed from System : " + sapSYS.getName());
                } else if (sapSYS.getBackup().equalsIgnoreCase("Yes")) {
                    RPConfig.getBackupSapSystemMap().remove(sapSYS.getName());
                    System.out.println("removed from backup System : " + sapSYS.getName());
                }
                try {
                    String language = "EN";
                    String lang = (String) session.getAttribute("LANG");
                    if (lang != null) {
                        if (lang.equalsIgnoreCase("English")) {
                            language = "EN";
                        } else if (lang.equalsIgnoreCase("German")) {
                            language = "DE";
                        }
                    }
                    SAPConnection connection = new SAPConnection(language);
                    if (connection.removeFromDestinationMap(sapSYS.getName() + sapSYS.getLang())) {

                        logger.info(" system configuration removed in Destination DP Map successfully. ");

//                    return (" Success");
                    } else {
                        logger.error("Error in updating : " + sapSYS.getName() + " system, Record not found in destination repository! ");
//                    return ("Error in updating : " + modifysap.getName() + " system, Record not found in destination repository! ");
                    }
                } catch (Throwable e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                }

//                System.out.println(" ----  " + RPConfig.getSapSystemList().size());
//                return "{\"message\" : \"" + "hello" + "\",\"success\" : false}";
            }

            if (!sapSYS.getQueryAction().equalsIgnoreCase("D")) {
                String clear_pwd = new String(Base64.decodeBase64(sapSYS.getPassword()), StandardCharsets.UTF_8);
                keyStoreMap.put(sapSYS.getName() + SecCrypt.KEY_PADDING + SapSystem.UID, sapSYS.getUser());
                keyStoreMap.put(sapSYS.getName() + SecCrypt.KEY_PADDING + SapSystem.PWD, clear_pwd);
                clear_pwd = null;
                sapSYS.setPassword("");
            }

//            RPConfig pac = new RPConfig();
//            System.out.println(sapSYS.toString());
            //if (sapSYS.getQueryAction().equalsIgnoreCase("N")) {
            //String pwd = sapSYS.getPassword();
            // pwd = encrypt(pwd);
//            pwd = pac.encrypt(pwd);
            //sapSYS.setPassword(pwd);
            // }
            ArrayList<SapSystem> systemList = new ArrayList<SapSystem>();

            for (Iterator<SapSystem> it = RPConfig.getSapSystemList().iterator(); it.hasNext();) {
                SapSystem sapSys = new SapSystem();

                SapSystem SAPSystem = it.next();

                sapSys.setName(SAPSystem.getName());
                sapSys.setIpaddress(SAPSystem.getIpaddress());
                sapSys.setRfcname(SAPSystem.getRfcname());
                sapSys.setUser(SAPSystem.getUser());
                sapSys.setPassword(SAPSystem.getPassword());
                sapSys.setLang(SAPSystem.getLang());
                sapSys.setClient(SAPSystem.getClient());
                sapSys.setSyno(SAPSystem.getSyno());
                sapSys.setHost(SAPSystem.getHost());
//                sapSys.setGroup(SAPSystem.getGroup());
                sapSys.setSncMode(SAPSystem.getSncMode());
                sapSys.setSncName(SAPSystem.getSncName());
                sapSys.setSncService(SAPSystem.getSncService());
                sapSys.setSncPartner(SAPSystem.getSncPartner());
                sapSys.setSncLevel(SAPSystem.getSncLevel());
                sapSys.setSncFlag(SAPSystem.getSncFlag());
                sapSys.setLoadBalancing(SAPSystem.getLoadBalancing());
                sapSys.setLbService(SAPSystem.getLbService());
                sapSys.setLbGroupName(SAPSystem.getLbGroupName());
                sapSys.setLbR3Name(SAPSystem.getLbR3Name());

                sapSys.setRouterFlag(SAPSystem.getRouterFlag());
                sapSys.setRouterString(SAPSystem.getRouterString());

                sapSys.setQueryAction(SAPSystem.getQueryAction());
                sapSys.setBackup(SAPSystem.getBackup());
                sapSys.setENFlag(SAPSystem.getENFlag());
                sapSys.setDescription(SAPSystem.getDescription());

                //String pswd = sapSys.getPassword();
                //pswd = encrypt(pswd);
//                pswd = pac.encrypt(pswd);
                sapSys.setPassword(SAPSystem.getPassword());

                if (sapSYS.getQueryAction().equalsIgnoreCase("M")) {

                    if (sapSYS.getBackup().equalsIgnoreCase("No")) {
                        if (sapSys.getName().equalsIgnoreCase(sapSYS.getName())) {
                            sapSys = sapSYS;
                            logger.info("existing System modified " + sapSYS.getName());
                        }

                    }
                }
                systemList.add(sapSys);
                sapSys = null;
            }

            if (sapSYS.getQueryAction().equalsIgnoreCase("N")) {

                //if (sapSYS.getBackup().equalsIgnoreCase("No")) {
                systemList.add(sapSYS);
                logger.info("New System added");
                // }

            }

            for (String key : RPConfig.getBackupSapSystemMap().keySet()) {
                SapSystem SAPsys = RPConfig.getBackupSapSystemMap().get(key);
                SapSystem sapn = new SapSystem();
                sapn.setName(SAPsys.getName());
                sapn.setIpaddress(SAPsys.getIpaddress());
                sapn.setRfcname(SAPsys.getRfcname());
                sapn.setUser(SAPsys.getUser());
                sapn.setPassword(SAPsys.getPassword());
                sapn.setLang(SAPsys.getLang());
                sapn.setClient(SAPsys.getClient());
                sapn.setSyno(SAPsys.getSyno());
                sapn.setHost(SAPsys.getHost());
//                sapn.setGroup(SAPsys.getGroup());
                sapn.setSncMode(SAPsys.getSncMode());
                sapn.setSncName(SAPsys.getSncName());
                sapn.setSncService(SAPsys.getSncService());
                sapn.setSncPartner(SAPsys.getSncPartner());
                sapn.setSncLevel(SAPsys.getSncLevel());
                sapn.setSncFlag(SAPsys.getSncFlag());
                sapn.setLoadBalancing(SAPsys.getLoadBalancing());
                sapn.setLbService(SAPsys.getLbService());
                sapn.setLbGroupName(SAPsys.getLbGroupName());
                sapn.setLbR3Name(SAPsys.getLbR3Name());

                sapn.setRouterFlag(SAPsys.getRouterFlag());
                sapn.setRouterString(SAPsys.getRouterString());

                sapn.setQueryAction(SAPsys.getQueryAction());
                sapn.setBackup(SAPsys.getBackup());
                sapn.setENFlag(SAPsys.getENFlag());
                sapn.setDescription(SAPsys.getDescription());

//                String pswd = SAPsys.getPassword();
//                pswd = encrypt(pswd);
//                sapn.setPassword(pswd);
                if (sapSYS.getQueryAction().equalsIgnoreCase("M")) {
                    if (sapSYS.getBackup().equalsIgnoreCase("yes")) {
                        if (sapn.getName().equalsIgnoreCase(sapSYS.getName())) {
                            sapn = sapSYS;
                            logger.info("existing System modified " + sapSYS.getName());
                        }

                    }
                }
                systemList.add(sapn);
            }
            int hostCount = 0;
            for (SapSystem sAPsystem : systemList) {
                if (sAPsystem.getHost().trim().equalsIgnoreCase("on")) {
                    hostCount++;
                    if (hostCount == 1) {
                        RPConfig.setSingleHost(true);//control HOST
                        RPConfig.setSingleHostSystem(sAPsystem);
                    }
                    if (hostCount > 1) {
                        systemList.clear();
                        //multilanguage code
                        String text = getTranslatedText("Systemsmsg3", session);
                        if (text == null || text.trim().length() == 0) {
                            text = "Host field is On for other system already.";
                        }
                        return "{\"message\" : \"" + text + "\",\"success\" : false}";
                    }
                }
            }

            if (hostCount == 0) {
                RPConfig.setSingleHost(false);
            } else {
                RPConfig.setSingleHost(true);
            }
            try {
                SaveSystemInfoToFile(systemList);
            } catch (Throwable ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
            }

            if (keyStoreMap.size() > 0) {
                for (Map.Entry<String, String> entry : keyStoreMap.entrySet()) {

                    try {
                        SecCrypt.encryptClearTextAndSave(entry.getKey(), entry.getValue(), RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME);
                    } catch (Exception ex) {
                        keyStoreMap.clear();
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        keyStoreMap.clear();
                        if (ex.getMessage().contains("Illegal key size")) { //Unicode character for newline \\u000a
                            return "{\"message\" : \"" + "Error while encrypting the sensitive data! \\u000a Requires Java runtime version 1.8 with Unlimited cryptography policy i.e. Java 1.8 update 161 or latest. \\u000a The server installed Java version is " + System.getProperty("java.version") + "\",\"success\" : false}";
                        } else {
                            return "{\"message\" : \"" + "Error in  encrypting/saving the system data!" + "\",\"success\" : false}";
                        }

                    }

                }
                keyStoreMap.clear();
            }
            if (keyStoreRemoveMap.size() > 0) {
                for (Map.Entry<String, String> entry : keyStoreRemoveMap.entrySet()) {

                    try {
                        SecStore.removeKsEntry(entry.getKey(), RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME);
                    } catch (Exception ex) {
                        keyStoreRemoveMap.clear();
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        keyStoreRemoveMap.clear();
                        return "{\"message\" : \"" + "Error in  removing the system data!" + "\",\"success\" : false}";
                    }

                }
                keyStoreRemoveMap.clear();
            }

            RPConfig.getSapSystemMap().clear();
            RPConfig.getBackupSapSystemMap().clear();
            RPConfig.getSapSystemList().clear();
            RPInitApp app = new RPInitApp();
            try {
                app.init(RPInitApp.servletConfig);
            } catch (Exception ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
            }

            if (sapSYS.getQueryAction().equalsIgnoreCase("N")) {
                //multilanguage code
                String text = getTranslatedText("Systemsmsg4", session);
                if (text == null || text.trim().length() == 0) {
                    text = "New system is sadded successfully.";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : true}";
            } else if (sapSYS.getQueryAction().equalsIgnoreCase("M")) {
                try {
                    String sysKey = RPConfig.getSapSystemMap().get(sapSYS.getName()).getSystemKey();
                    String language = "EN";
                    if (session.getAttribute("LANG") != null) {
                        String lang = (String) session.getAttribute("LANG");
                        if (lang != null) {
                            if (lang.equalsIgnoreCase("English")) {
                                language = "EN";
                            } else if (lang.equalsIgnoreCase("German")) {
                                language = "DE";
                            }
                        }
                    }
                    SAPConnection connection = new SAPConnection(language);
                    if (connection.removeFromDestinationMap(sapSYS.getName() + sapSYS.getLang())) {
                        logger.info(" system configuration removed in Destination DP Map successfully. ");
                    } else {
                        logger.error("Error in updating : " + sapSYS.getName() + " system, Record not found in destination repository! ");
                    }
                    if (connection.updateDestinationMap(sapSYS.getName(), RPConfig.getAppClientProperties().get(sysKey))) {
                        logger.info(" system configuration updated in Destination DP Map successfully. ");
                        //logger.info(" system configuration updated in Destination DP Map successfully. ");
                        // return " Success";
                    } else {
                        logger.info("in updating : " + sysKey + " system, Record not found in destination repository! ");
                        //  return "Error in updating : " + sysKey + " system, Record not found in destination repository! ";
                    }
                } catch (Throwable ex) {
                    StringWriter stack = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;
                }
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg5", session);
                if (text == null || text.trim().length() == 0) {
                    text = "System configuration modified successfully.";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : true}";
            } else if (sapSYS.getQueryAction().equalsIgnoreCase("D")) {
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg6", session);
                if (text == null || text.trim().length() == 0) {
                    text = "System configuration deleted successfully.";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : true}";
            }
            return "{\"message\" : \"" + "success" + "\",\"success\" : true}";

        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            // /multilanguage code
            String text = getTranslatedText("Systemsmsg7", session);
            if (text == null || text.trim().length() == 0) {
                text = "Sorry, Something went wrong !";
            }
            return "{\"message\" : \"" + "Sorry, Something went wrong !" + "\",\"success\" : false}";
        }
    }

    @Path("ADSystemEdit")
    @POST
    @Produces("application/json")
    public String ADSystemEdit(ADserver sysData, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        //TODO return proper representation object
        HttpSession session = request.getSession(false);
        ArrayList<ADserver> ADSystem = new ArrayList<ADserver>();
        ADserver adServer = new ADserver();
        HashMap<String, String> keyStoreMapAD = new HashMap<String, String>();
        HashMap<String, String> keyStoreRemoveMapAD = new HashMap<String, String>();
        try {
            ADSystem = (ArrayList<ADserver>) JsonTransformer.transformToJavaObjects(sysData, ADserver.class);
            adServer = ADSystem.get(0);
            ADserver existingSystem = RPConfig.getAdServerMap().get(adServer.getDomain());
            if (existingSystem != null && adServer.getQueryAction().equalsIgnoreCase("N")) {
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg11", session);
                if (text == null || text.trim().length() == 0) {
                    text = "AD Server can not have same \'Domain Name\'.";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }

            if (adServer.getQueryAction().equalsIgnoreCase("D")) {
                int i = 0;
                for (ADserver sapSys : RPConfig.getAdServerList()) {
                    if (sapSys.getDomain().equalsIgnoreCase(adServer.getDomain())) {
                        break;
                    }
                    i++;
                }
                keyStoreRemoveMapAD.put(RPConfig.getAdServerList().get(i).getIp() + SecCrypt.KEY_PADDING + SapSystem.UID, RPConfig.getAdServerList().get(i).getIp());
                keyStoreRemoveMapAD.put(RPConfig.getAdServerList().get(i).getIp() + SecCrypt.KEY_PADDING + SapSystem.PWD, RPConfig.getAdServerList().get(i).getIp());

                RPConfig.getAdServerList().remove(i);
            }

//            String pwd = adServer.getPassword();
//            pwd = encrypt(pwd);
//            adServer.setPassword(pwd);
            if (!adServer.getQueryAction().equalsIgnoreCase("D")) {
                String clear_pwd = new String(Base64.decodeBase64(adServer.getPassword()), StandardCharsets.UTF_8);
                keyStoreMapAD.put(adServer.getIp() + SecCrypt.KEY_PADDING + SapSystem.UID, adServer.getUserid());
                keyStoreMapAD.put(adServer.getIp() + SecCrypt.KEY_PADDING + SapSystem.PWD, clear_pwd);
                clear_pwd = null;
            }
            adServer.setUserid("");
            adServer.setPassword("");

            ArrayList<ADserver> systemList = new ArrayList<ADserver>();

            for (Iterator<ADserver> it = RPConfig.getAdServerList().iterator(); it.hasNext();) {
                ADserver sapSys = new ADserver();

                ADserver SAPSystem = it.next();

                sapSys.setDomain(SAPSystem.getDomain());
                sapSys.setIp(SAPSystem.getIp());
                sapSys.setPassword(SAPSystem.getPassword());
                sapSys.setPort(SAPSystem.getPort());
                sapSys.setQueryAction(SAPSystem.getQueryAction());
                sapSys.setUserid(SAPSystem.getUserid());

                //String pswd = sapSys.getPassword();
//                pswd = pac.encrypt(pswd);
                // pswd = encrypt(pswd);
                //sapSys.setPassword(pswd);
                if (adServer.getQueryAction().equalsIgnoreCase("M")) {
                    if (sapSys.getDomain().equalsIgnoreCase(adServer.getDomain())) {
                        sapSys = adServer;
                    }
                }
                systemList.add(sapSys);
                sapSys = null;
            }

            if (adServer.getQueryAction().equalsIgnoreCase("N")) {
                systemList.add(adServer);
            }
            try {
                SaveADserverInfoToFile(systemList);
            } catch (Throwable ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg7", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Sorry, Something went wrong !";
                }
                return "{\"message\" : \"" + text + "\",\"success\" : false}";
            }

            if (keyStoreMapAD.size() > 0) {
                for (Map.Entry<String, String> entry : keyStoreMapAD.entrySet()) {

                    try {
                        SecCrypt.encryptClearTextAndSave(entry.getKey(), entry.getValue(), RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME);
                    } catch (Exception ex) {
                        keyStoreMapAD.clear();
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        keyStoreMapAD.clear();
                        return "{\"message\" : \"" + "Error in  encrypting/saving the system data!" + "\",\"success\" : false}";
                    }

                }
                keyStoreMapAD.clear();
            }
            if (keyStoreRemoveMapAD.size() > 0) {
                for (Map.Entry<String, String> entry : keyStoreRemoveMapAD.entrySet()) {

                    try {
                        logger.info("removing " + entry.getKey());
                        SecStore.removeKsEntry(entry.getKey(), RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME);
                    } catch (Exception ex) {
                        keyStoreRemoveMapAD.clear();
                        StringWriter stack = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stack));
                        logger.error(stack.toString());
                        stack = null;
                        keyStoreRemoveMapAD.clear();
                        return "{\"message\" : \"" + "Error in  removing the system data!" + "\",\"success\" : false}";
                    }

                }
                keyStoreRemoveMapAD.clear();
            }

//            RPConfig.getAdServerMap().clear();
//            for (ADserver sAPsystem : systemList) {
//                String pwd1 = sAPsystem.getPassword();
//                pwd1 = decrypt(pwd1);
//                sAPsystem.setPassword(pwd1);
//                RPConfig.getAdServerMap().put(sAPsystem.getDomain(), sAPsystem);
//            }
//            RPConfig.setAdServerList(systemList);
            RPInitApp app = new RPInitApp();
            app.init(RPInitApp.servletConfig);
            if (adServer.getQueryAction().equalsIgnoreCase("N")) {
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg8", session);
                if (text == null || text.trim().length() == 0) {
                    text = "is added successfully.";
                }
                return "{\"message\" : \"AD Server :" + adServer.getIp() + " " + text + "\",\"success\" : true}";
            } else if (adServer.getQueryAction().equalsIgnoreCase("M")) {
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg9", session);
                if (text == null || text.trim().length() == 0) {
                    text = " is modified successfully.";
                }
                return "{\"message\" : \"AD Server :" + adServer.getIp() + " " + text + "\",\"success\" : true}";
            } else if (adServer.getQueryAction().equalsIgnoreCase("D")) {
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg10", session);
                if (text == null || text.trim().length() == 0) {
                    text = "is deleted successfully.";
                }
                return "{\"message\" : \"AD Server :" + adServer.getDomain() + " " + text + "\",\"success\" : true}";
            }
            return "{\"message\" : \"" + "success" + "\",\"success\" : true}";

        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + "Sorry, Something went wrong !" + "\",\"success\" : false}";
        }
    }
//    @Path("getSysList")
//    @GET
//    @Produces("application/json")
//    public String getSystemList(@Context HttpServletRequest request) {
//        ArrayList<SelectSystem> systemNameList = new ArrayList<SelectSystem>();
//        ListWrapper<SelectSystem> wrapper = new ListWrapper<SelectSystem>();
//        RPConfig.getSapSystemList();
//        for (SapSystem sapSYS : RPConfig.getSapSystemList()) {
//            SelectSystem selSys = new SelectSystem();
//            selSys.setSysName(sapSYS.getSystemName());
////            if (!selSys.getSysName().startsWith("@")) {
//            systemNameList.add(selSys);
////            }
//            selSys = null;
//        }
//        wrapper.setData(systemNameList);
//        wrapper.setSuccess(Boolean.TRUE);
//        wrapper.setTotal(systemNameList.size());
//
//        String jsonData3 = "";
//
//        try {
//            JsonTransformer transformer = new JsonTransformer();
//            jsonData3 = transformer.transformToJson(wrapper);
//        } catch (Exception ex) {
//            StringWriter stack = new StringWriter();
//            ex.printStackTrace(new PrintWriter(stack));
//            logger.error(stack.toString());
//            stack = null;
//            return "{\"message\" : \"" + "Sorry, Something went wrong !" + "\",\"success\" : false}";
//        }
//        logger.info("System list sent.");
//        return jsonData3;
//
//    }
//

    @Path("doTest")
    @GET
    @Produces("application/json")
    public String testSAPconnection(@Context HttpServletRequest request) {

        logger.info("testSAPconnection() method start...");
        HttpSession session = request.getSession(false);
        String result = "";
        String language = "EN";
        String lang = (String) session.getAttribute("LANG");
        if (lang != null) {
            if (lang.equalsIgnoreCase("English")) {
                language = "EN";
            } else if (lang.equalsIgnoreCase("German")) {
                language = "DE";
            }
        }
        SAPConnection connection = new SAPConnection(language);
        String sysName = request.getParameter("name");
        String backup = request.getParameter("backup");
        String sysKey = "";
        if (backup.equalsIgnoreCase("YES")) {
            sysKey = RPConfig.getBackupSapSystemMap().get(sysName).getSystemKey();
        } else {
            sysKey = RPConfig.getSapSystemMap().get(sysName).getSystemKey();
        }
        try {
            connection.prepareConnection(RPConfig.getAppClientProperties().get(sysKey));
            //if (connection.testConnection(sysKey)) {
            connection.testConnection(RPConfig.getAppClientProperties().get(sysKey).getProperty(DestinationDataProvider.JCO_DEST));
            connection.release();
            logger.info("testSAPconnection() method end...");
            // /multilanguage code
            String text = getTranslatedText("Systemsmsg15", session);
            if (text == null || text.trim().length() == 0) {
                text = "is connected successfully.";
            }
            result = sysName + " " + text;
            return "{\"message\" : \"SAP system - " + result + "\",\"success\" : true}";
            //}

        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            if (ex.getMessage().contains("Name or password is incorrect.")) {
                // /multilanguage code
                String text = getTranslatedText("Systemsmsg13", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Name or password is incorrect.";
                }
                result = "Name or password is incorrect.";
                return "{\"message\" : \" " + text + " \",\"success\" : false}";
            } else {
                result = ex.getMessage();

                return "{\"message\" : \"" + Transform.escapeJsonControlCharacters(result) + "\",\"success\" : false}";
            }
        }
        // result = "JCO Error in getting SAP client instance.";
        // return "{\"message\" : \"" + result + "\",\"success\" : false}";
    }
//

    @Path("doADTest")
    @GET
    @Produces("application/json")
    public String testADconnection(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String result = "";
        String domain = request.getParameter("sysName");
        ADserver server = RPConfig.getAdServerMap().get(domain);
        DirContext ldapContext = null;
        ADconnection conn = new ADconnection();
        try {
            logger.info("Connecting " + server.getDomain());
            ldapContext = conn.getDirContext(server, session);
            // /multilanguage code
            String text = getTranslatedText("Systemsmsg15", session);
            if (text == null || text.trim().length() == 0) {
                text = "is Connected successfully.";
            }
            result = "AD Server :" + server.getIp() + " " + text;
        } catch (Throwable ex) {
            try {
                if (ldapContext != null) {
                    ldapContext.close();
                }
            } catch (NamingException ex1) {
                StringWriter stack = new StringWriter();
                ex1.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                result = ex.getMessage();
                return "{\"message\" : \"" + result + "\",\"success\" : false}";
            } catch (Throwable ex1) {
                StringWriter stack = new StringWriter();
                ex1.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                result = ex.getMessage();
                return "{\"message\" : \"" + result + "\",\"success\" : false}";
            }
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            return "{\"message\" : \"" + "Connection is unsuccessfull !" + "\",\"success\" : false}";

        }
        return "{\"message\" : \"" + result + "\",\"success\" : true}";

    }
//

    /**
     * PUT method for updating or creating an instance of SystemsResource
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
//                logger.info(msg);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//
//    public void logex(Exception ex) {
//        try {
//            if (RPConfig.isLogging()) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//    public void logTh(Throwable ex) {
//        try {
//            if (RPConfig.isLogging()) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }

    public void SaveSystemInfoToFile(ArrayList<SapSystem> systemList) throws Throwable {
        Wini ini = new Wini(new File(RPConfig.getRP_home() + "/System.info"));
        ini.clear();
        int i = 1;
        for (SapSystem sAPsystem : systemList) {
            String key = sAPsystem.getName() + "_" + i;
            i++;

            ini.put(key, "SystemName ", sAPsystem.getName());
            ini.put(key, "IPaddress", sAPsystem.getIpaddress());
            ini.put(key, "RfcName", sAPsystem.getRfcname());
            //ini.put(key, "UserID", sAPsystem.getUser());
            //ini.put(key, "Password", sAPsystem.getPassword());
            ini.put(key, "Language", sAPsystem.getLang());
            ini.put(key, "Client", sAPsystem.getClient());
            ini.put(key, "SystemNo", sAPsystem.getSyno());
            ini.put(key, "HostFlag", sAPsystem.getHost());
//            ini.put(key, "group", sAPsystem.getGroup());
            ini.put(key, "sncMode", sAPsystem.getSncMode());
            ini.put(key, "sncName", sAPsystem.getSncName());
            ini.put(key, "sncService", sAPsystem.getSncService());
            ini.put(key, "sncPartner", sAPsystem.getSncPartner());
            ini.put(key, "sncLevel", sAPsystem.getSncLevel());// sapSys.setBackup(section.get(""));
            ini.put(key, "Backup", sAPsystem.getBackup());
            ini.put(key, "sncFlag", sAPsystem.getSncFlag());
            ini.put(key, "ENFlag", sAPsystem.getENFlag());
            ini.put(key, "loadBalancing", sAPsystem.getLoadBalancing());
            ini.put(key, "lbService", sAPsystem.getLbService());
            ini.put(key, "lbGroupName", sAPsystem.getLbGroupName());
            ini.put(key, "lbR3Name", sAPsystem.getLbR3Name());
            ini.put(key, "routerFlag", sAPsystem.getRouterFlag());
            ini.put(key, "routerString", sAPsystem.getRouterString());
            ini.put(key, "Description", sAPsystem.getDescription());

        }
        ini.store();
    }

//    public void SaveSystemInfoToFile(ArrayList<SapSystem> systemList) throws Throwable {
//        Wini ini = new Wini(new File(RPinitApp.sapSystemFileLocation));
//        ini.clear();
//        int i = 1;
//        for (SapSystem sAPsystem : systemList) {
////            String key = sAPsystem.getSystemName();
//            String key = sAPsystem.getSystemName() + "_" + i;
//            i++;
//            ini.put(key, "SystemName", sAPsystem.getSystemName());
//            ini.put(key, "IPaddress", sAPsystem.getHostName());
//            ini.put(key, "RfcName", sAPsystem.getRfcName());
//            ini.put(key, "UserID", sAPsystem.getUserName());
//            ini.put(key, "Password", sAPsystem.getPassword());
//            ini.put(key, "Language", sAPsystem.getLanguage());
//            ini.put(key, "Client", sAPsystem.getClient());
//            ini.put(key, "SystemNo", sAPsystem.getSystemNumber());
//            ini.put(key, "HostFlag", sAPsystem.getHostFlag());
//            ini.put(key, "Backup", sAPsystem.getBackup());
//
//        }
//        ini.store();
//    }
//
    public void SaveADserverInfoToFile(ArrayList<ADserver> systemList) throws Throwable {
        logger.info("Start of  SaveADServerInfoToFile() WebServices method ...");
        File adConfigFile = new File(RPConfig.getRP_home() + "/server.info");
        if (adConfigFile.exists() && !adConfigFile.isDirectory()) {
            try {
                File spStore = new File(RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME);
                if (spStore.exists()) {
                    File directory = new File(RPConfig.getRP_home() + "/backup");
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    String bkpSuffix = getUUIDperSec();
                    Files.copy(adConfigFile.toPath(), new File(RPConfig.getRP_home() + "/backup/server.info_" + bkpSuffix).toPath());
                    Files.copy(spStore.toPath(), new File(RPConfig.getRP_home() + "/backup/rp.store_" + bkpSuffix).toPath());
                }
            } catch (IOException ex) {
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
            }
        }
        Wini ini = new Wini(new File(RPConfig.getRP_home() + "/server.info"));
        ini.clear();
        int i = 1;

        for (ADserver sAPsystem : systemList) {

            String key = sAPsystem.getDomain() + "_" + i;
            i++;
            ini.put(key, "Domain", sAPsystem.getDomain());
            ini.put(key, "IPaddress", sAPsystem.getIp());
            //ini.put(key, "UserID", sAPsystem.getUserid());
            //ini.put(key, "Password", sAPsystem.getPassword());
            ini.put(key, "Port", sAPsystem.getPort());

        }
        ini.store();
    }

    public String getUUIDperSec() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String datetime = ft.format(dNow);
        return datetime;
    }

    /**
     *
     * This method is used to decrypt the string
     *
     * @param String
     * @return decrypted String
     *
     */
    public String decrypt(String encoded) {
        logger.info("inside decryption");
        String decoded = "";
        byte[] decodedBytes = Base64.decodeBase64(encoded);
        decoded = new String(decodedBytes, StandardCharsets.UTF_8);
        return decoded;
    }

    /**
     *
     * This method is used to encrypt the string
     *
     * @param String
     * @return encrypted String
     *
     */
    public String encrypt(String st1) {
        String encoded = "";
        byte[] PassBytes = st1.getBytes();
        String passEncoded = Base64.encodeBase64String(PassBytes);
        logger.info("inside encryption");
        encoded = new String(passEncoded);
        return encoded;
    }

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
                    text = RPConfig.getSystemResourcesLangMap().get(selectedLang).get(textID);
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
}
