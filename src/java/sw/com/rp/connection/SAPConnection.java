/* and open the template in the editor.
 */
package sw.com.rp.connection;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;
import com.sw.jco.SWJcoDestinationProvider;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Set;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sw.com.rp.config.RPConfig;

/**
 *
 * @author msaini
 */
public class SAPConnection extends HttpServlet {

    static final Logger logger = LogManager.getLogger(SAPConnection.class.getName());
    private String hostxname = "";
    private String hostxmluser = "";
    private String hostxmlpass = "";
    private String hostxsyno = "";
    private String hostxclient = "";
    private String hostxipaddress = "";
    private String hostrfc = "";
    private String sys40rfc = "";
    private String hostHostrfc = "";
    String portalhostsysno = "";
    private String hostGroup = "";
    private String hostSNCMode = "";
    private String hostSNCName = "";
    private String hostSNCService = "";
    private String hostSNCPartner = "";
    private String hostSNCLevel = "";
    private String hostlang = "EN";
    StringBuffer mess = new StringBuffer();

    public SAPConnection(String iHostlang) {
        this.hostlang = iHostlang;
    }

    public String getHostxmlpass() {
        return hostxmlpass;
    }

    public void setHostxmlpass(String hostxmlpass) {
        this.hostxmlpass = hostxmlpass;
    }

    public String getHostxmluser() {
        return hostxmluser;
    }

    public void setHostxmluser(String hostxmluser) {
        this.hostxmluser = hostxmluser;
    }

    public String getPortalhostsysno() {
        return portalhostsysno;
    }

    public void setPortalhostsysno(String portalhostsysno) {
        this.portalhostsysno = portalhostsysno;
    }

    public String getHostxclient() {
        return hostxclient;
    }

    public void setHostxclient(String hostxclient) {
        this.hostxclient = hostxclient;
    }

    public String getHostxipaddress() {
        return hostxipaddress;
    }

    public void setHostxipaddress(String hostxipaddress) {
        this.hostxipaddress = hostxipaddress;
    }

    public String getHostxsyno() {
        return hostxsyno;
    }

    public void setHostxsyno(String hostxsyno) {
        this.hostxsyno = hostxsyno;
    }

    public String getHostrfc() {
        return hostrfc;
    }

    public void setHostrfc(String hostrfc) {
        this.hostrfc = hostrfc;
    }

    public String getHostxname() {
        return hostxname;
    }

    public void setHostxname(String hostxname) {
        this.hostxname = hostxname;
    }

    public String getHostGroup() {
        return hostGroup;
    }

    public void setHostGroup(String hostGroup) {
        this.hostGroup = hostGroup;
    }

    public String getHostSNCLevel() {
        return hostSNCLevel;
    }

    public void setHostSNCLevel(String hostSNCLevel) {
        this.hostSNCLevel = hostSNCLevel;
    }

    public String getHostSNCMode() {
        return hostSNCMode;
    }

    public void setHostSNCMode(String hostSNCMode) {
        this.hostSNCMode = hostSNCMode;
    }

    public String getHostSNCName() {
        return hostSNCName;
    }

    public void setHostSNCName(String hostSNCName) {
        this.hostSNCName = hostSNCName;
    }

    public String getHostSNCPartner() {
        return hostSNCPartner;
    }

    public void setHostSNCPartner(String hostSNCPartner) {
        this.hostSNCPartner = hostSNCPartner;
    }

    public String getHostSNCService() {
        return hostSNCService;
    }

    public void setHostSNCService(String hostSNCService) {
        this.hostSNCService = hostSNCService;
    }
    private JCoRepository repos;
    private JCoDestination dest;

    public void prepareConnection(Properties connectProperties) throws Throwable {

        logger.info("Connecting... " + connectProperties.getProperty(DestinationDataProvider.JCO_DEST));
        logger.debug("routerFlag... " + connectProperties.getProperty("routerFlag"));
        logger.debug("sncFlag... " + connectProperties.getProperty("sncFlag"));
        logger.debug("loadBalancing... " + connectProperties.getProperty("loadBalancing"));
        String servletContext = RPConfig.getRP_context();

        SWJcoDestinationProvider destinationDataProvider = SWJcoDestinationProvider.getInstance();

        if (!Environment.isDestinationDataProviderRegistered()) {

            logger.debug("Jco New Destination DataProvider Registered by RP.");

            Environment.registerDestinationDataProvider(destinationDataProvider);

        } else {

            logger.debug("RP Destination DataProvider Already Registered.");

        }
        if (!destinationDataProvider.isDestinationAdded(servletContext + connectProperties.get(DestinationDataProvider.JCO_DEST) + hostlang)) {

            Properties JCoProperties = new Properties();

            JCoProperties.setProperty(DestinationDataProvider.JCO_DEST, connectProperties.getProperty(DestinationDataProvider.JCO_DEST));
            JCoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, connectProperties.getProperty(DestinationDataProvider.JCO_ASHOST));
            JCoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, connectProperties.getProperty(DestinationDataProvider.JCO_SYSNR));

//            JCoProperties.setProperty(DestinationDataProvider.JCO_USER, connectProperties.getProperty(DestinationDataProvider.JCO_USER));
//            JCoProperties.setProperty(DestinationDataProvider.JCO_PASSWD, connectProperties.getProperty(DestinationDataProvider.JCO_PASSWD));
            JCoProperties.setProperty(SWJcoDestinationProvider.JKS_FILE_NAME, RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME);
            JCoProperties.setProperty(SWJcoDestinationProvider.JKS_UID_KEY, connectProperties.getProperty(SWJcoDestinationProvider.JKS_UID_KEY));
            JCoProperties.setProperty(SWJcoDestinationProvider.JKS_PWD_KEY, connectProperties.getProperty(SWJcoDestinationProvider.JKS_PWD_KEY));

            JCoProperties.setProperty(DestinationDataProvider.JCO_CLIENT, connectProperties.getProperty(DestinationDataProvider.JCO_CLIENT));
            JCoProperties.setProperty(DestinationDataProvider.JCO_LANG, hostlang);
            JCoProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "4");
            JCoProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");
            JCoProperties.setProperty(DestinationDataProvider.JCO_MAX_GET_TIME, "300000");//5minutes time to wait for connection in queue
            JCoProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "300000");//auto close connection after 5 minutes

            if (connectProperties.getProperty("routerFlag") != null && connectProperties.getProperty("routerFlag").equalsIgnoreCase("On")) {
                JCoProperties.setProperty(DestinationDataProvider.JCO_SAPROUTER, connectProperties.getProperty(DestinationDataProvider.JCO_SAPROUTER));
            }

//            if (RPConfig.isLoadBalancing()) {//Load Balancing Properties
            if (connectProperties.getProperty("loadBalancing") != null && connectProperties.getProperty("loadBalancing").equalsIgnoreCase("YES")) {

                /*
                 * Load Balancing Properties
                 * https://scn.sap.com/thread/3692644
                 * Use the following JCo properties:
                 * jco.client.mshost=scscif
                 * jco.client.msserv=3605
                 * jco.client.group=group_name
                 * jco.client.saprouter=/H/XXX.XX.X.XXX/S/3299
                 * As 3299 is the default router port it should also be possible to omit it:
                 * jco.client.saprouter=/H/XXX.XX.X.XXX
                 * SAP Routers should only be used if there is a firewall between JCo and the targeted ABAP system.
                 * So I assume that this is the case here. Otherwise you should omit the jco.client.saprouter property completely.
                 * But if you need a SAP Router, you should always specify it separately with the saprouter property.
                 */
                //Pending: Add new fields for load balancing in front-end maintenance
                JCoProperties.setProperty(DestinationDataProvider.JCO_MSHOST, connectProperties.getProperty(DestinationDataProvider.JCO_ASHOST));
                JCoProperties.setProperty(DestinationDataProvider.JCO_MSSERV, connectProperties.getProperty(DestinationDataProvider.JCO_MSSERV));
                JCoProperties.setProperty(DestinationDataProvider.JCO_GROUP, connectProperties.getProperty(DestinationDataProvider.JCO_GROUP));
                JCoProperties.setProperty(DestinationDataProvider.JCO_R3NAME, connectProperties.getProperty(DestinationDataProvider.JCO_R3NAME));

                JCoProperties.remove(DestinationDataProvider.JCO_ASHOST);
                JCoProperties.remove(DestinationDataProvider.JCO_SYSNR);

                //} else if (RPConfig.isSncFlag()) {
            }
            if (connectProperties.getProperty("sncFlag") != null && connectProperties.getProperty("sncFlag").equalsIgnoreCase("On")) {

                JCoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, connectProperties.getProperty(DestinationDataProvider.JCO_ASHOST));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, connectProperties.getProperty(DestinationDataProvider.JCO_SYSNR));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_MODE, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_MODE));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_QOP, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_QOP));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_LIBRARY, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_LIBRARY));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_MYNAME, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_MYNAME));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_PARTNERNAME, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_PARTNERNAME));

            }
            Set<String> keys = JCoProperties.stringPropertyNames();
            for (String key : keys) {
                if (key.contains("passwd")) {
                    logger.debug(key + " : " + "*****NotShowN" + JCoProperties.getProperty(key).length());
                } else {
                    logger.debug(key + " : " + JCoProperties.getProperty(key));
                }

            }
            logger.info("Adding Destination " + servletContext + connectProperties.getProperty(DestinationDataProvider.JCO_DEST) + hostlang);
            destinationDataProvider.addDestinationByName(servletContext + connectProperties.getProperty(DestinationDataProvider.JCO_DEST) + hostlang, JCoProperties);

        }

        try {

            dest = JCoDestinationManager.getDestination(servletContext + connectProperties.getProperty(DestinationDataProvider.JCO_DEST) + hostlang);

            repos = dest.getRepository();

        } catch (JCoException e) {
            if (e.JCO_ERROR_COMMUNICATION == e.getGroup()) {
                logger.info("SAP system: " + dest.getProperties().getProperty("jco.client.dest") + " is down.");
                String systemname = dest.getProperties().getProperty("jco.client.dest");

                if (RPConfig.getBackupSapSystemMap().get(systemname) != null) {
                    SapSystem system = RPConfig.getBackupSapSystemMap().get(systemname);
//                    system.setPassword(decrypt(system.getPassword()));
                    if (system != null) {
                        logger.info("Re-Connecting through backup SAP system: " + system.getName() + ", Backup flag : " + system.getBackup());
                        Properties connProp = RPConfig.getAppClientProperties().get(system.getSystemKey());
                        connProp.setProperty(DestinationDataProvider.JCO_DEST, system.getSystemKey());

                        prepareConnection(connProp);
                    } else {
                        logger.error("Connecting system null.");
                    }
                } else {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                throw new RuntimeException(e.toString());
            }
        }
    }

    public JCoFunction getFunction(String functionStr) throws Throwable {

        logger.info("Preparing function... " + functionStr);

        JCoFunction function = null;

        try {

            function = repos.getFunction(functionStr);

        } catch (Exception e) {

            System.out.println(e.toString());

            throw new RuntimeException("Problem retrieving JCoFunction " + functionStr + " object.");
        }
        if (function == null) {

            throw new RuntimeException("Not possible to receive JCoFunction " + functionStr);
        }

        return function;
    }

    public void execute(JCoFunction function) throws Throwable {
        logger.info("Executing function... " + function.getName());
        try {
            JCoContext.begin(dest);

            function.execute(dest);

        } catch (JCoException e) {
            if (e.JCO_ERROR_COMMUNICATION == e.getGroup()) {
                logger.info("SAP system: " + dest.getProperties().getProperty("jco.client.dest") + " is down.");
                String systemname = dest.getProperties().getProperty("jco.client.dest");

                if (RPConfig.getBackupSapSystemMap().get(systemname) != null) {
                    SapSystem system = RPConfig.getBackupSapSystemMap().get(systemname);
//                    system.setPassword(decrypt(system.getPassword()));
                    if (system != null) {
                        logger.info("Re-Connecting through backup SAP system: " + system.getName() + ", Backup flag : " + system.getBackup());
                        Properties connProp = RPConfig.getAppClientProperties().get(system.getSystemKey());
                        connProp.setProperty(DestinationDataProvider.JCO_DEST, system.getSystemKey());

                        prepareConnection(connProp);
                    } else {
                        logger.error("Connecting system null.");
                    }

                } else {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    logger.error(stack.toString());
                    stack = null;
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                throw new RuntimeException(e.toString());
            }

        } finally {

            try {

                JCoContext.end(dest);

            } catch (JCoException e) {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                throw new RuntimeException("JCoException: " + e.toString());

            } catch (Exception e) {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                throw new RuntimeException("Exception: " + e.toString());

            }
        }
    }

    public void release() {

        this.dest = null;
        this.hostGroup = null;
        this.hostHostrfc = null;
        this.hostSNCLevel = null;
        this.hostSNCMode = null;
        this.hostSNCName = null;
        this.hostSNCPartner = null;
        this.hostSNCService = null;
        this.hostrfc = null;
        this.hostxclient = null;
        this.hostxipaddress = null;
        this.hostxmlpass = null;
        this.hostxmluser = null;
        this.hostxname = null;
        this.hostxsyno = null;
//        this.mConnection = null;
//        this.mRepository = null;
        this.mess = null;
        this.portalhostsysno = null;
        this.repos = null;

    }

    public boolean testConnection(String host) throws Throwable {
        logger.info("testConnection- ping start");
        try {
            String servletContext = RPConfig.getRP_context();
            logger.info("destination: " + servletContext + host + hostlang);
            JCoDestinationManager.getDestination(servletContext + host + hostlang).ping();
            logger.info("testConnection- ping Success");
            return true;
        } catch (Throwable e) {
            logger.error("testConnection- ping Error : ");

            throw e;
        }

    }

    public boolean updateDestinationMap(String host, Properties connectProperties) throws Throwable {
        try {
            String servletContext = RPConfig.getRP_context();
            SWJcoDestinationProvider destinationDataProvider = SWJcoDestinationProvider.getInstance();
            Properties JCoProperties = new Properties();

            logger.info("Updating Destination DP... " + connectProperties.getProperty(DestinationDataProvider.JCO_DEST));
            logger.info("routerFlag... " + connectProperties.getProperty("routerFlag"));
            logger.info("sncFlag... " + connectProperties.getProperty("sncFlag"));
            logger.info("loadBalancing... " + connectProperties.getProperty("loadBalancing"));

            JCoProperties.setProperty(DestinationDataProvider.JCO_DEST, connectProperties.getProperty(DestinationDataProvider.JCO_DEST));
            JCoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, connectProperties.getProperty(DestinationDataProvider.JCO_ASHOST));
            JCoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, connectProperties.getProperty(DestinationDataProvider.JCO_SYSNR));
//            JCoProperties.setProperty(DestinationDataProvider.JCO_USER, connectProperties.getProperty(DestinationDataProvider.JCO_USER));
//            JCoProperties.setProperty(DestinationDataProvider.JCO_PASSWD, connectProperties.getProperty(DestinationDataProvider.JCO_PASSWD));

            JCoProperties.setProperty(SWJcoDestinationProvider.JKS_FILE_NAME, RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME);
            JCoProperties.setProperty(SWJcoDestinationProvider.JKS_UID_KEY, connectProperties.getProperty(SWJcoDestinationProvider.JKS_UID_KEY));
            JCoProperties.setProperty(SWJcoDestinationProvider.JKS_PWD_KEY, connectProperties.getProperty(SWJcoDestinationProvider.JKS_PWD_KEY));

            JCoProperties.setProperty(DestinationDataProvider.JCO_CLIENT, connectProperties.getProperty(DestinationDataProvider.JCO_CLIENT));
            JCoProperties.setProperty(DestinationDataProvider.JCO_LANG, connectProperties.getProperty(DestinationDataProvider.JCO_LANG));
            JCoProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "4");
            JCoProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");
            JCoProperties.setProperty(DestinationDataProvider.JCO_MAX_GET_TIME, "300000");//5minutes time to wait for connection in queue
            JCoProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "300000");//auto close connection after 5 minutes

            if (connectProperties.getProperty("routerFlag") != null && connectProperties.getProperty("routerFlag").equalsIgnoreCase("On")) {
                JCoProperties.setProperty(DestinationDataProvider.JCO_SAPROUTER, connectProperties.getProperty(DestinationDataProvider.JCO_SAPROUTER));
            }

            //if (RPConfig.isLoadBalancing()) {//Load Balancing Properties
            if (connectProperties.getProperty("loadBalancing") != null && connectProperties.getProperty("loadBalancing").equalsIgnoreCase("YES")) {
                /*
                 * Load Balancing Properties
                 * https://scn.sap.com/thread/3692644
                 * Use the following JCo properties:
                 * jco.client.mshost=scscif
                 * jco.client.msserv=3605
                 * jco.client.group=group_name
                 * jco.client.saprouter=/H/XXX.XX.X.XXX/S/3299
                 * As 3299 is the default router port it should also be possible to omit it:
                 * jco.client.saprouter=/H/XXX.XX.X.XXX
                 * SAP Routers should only be used if there is a firewall between JCo and the targeted ABAP system.
                 * So I assume that this is the case here. Otherwise you should omit the jco.client.saprouter property completely.
                 * But if you need a SAP Router, you should always specify it separately with the saprouter property.
                 */
                //Pending: Add new fields for load balancing in front-end maintenance
                JCoProperties.setProperty(DestinationDataProvider.JCO_MSHOST, connectProperties.getProperty(DestinationDataProvider.JCO_ASHOST));
                JCoProperties.setProperty(DestinationDataProvider.JCO_MSSERV, connectProperties.getProperty(DestinationDataProvider.JCO_MSSERV));
                JCoProperties.setProperty(DestinationDataProvider.JCO_GROUP, connectProperties.getProperty(DestinationDataProvider.JCO_GROUP));
                JCoProperties.setProperty(DestinationDataProvider.JCO_R3NAME, connectProperties.getProperty(DestinationDataProvider.JCO_R3NAME));

                JCoProperties.remove(DestinationDataProvider.JCO_ASHOST);
                JCoProperties.remove(DestinationDataProvider.JCO_SYSNR);

            }
            if (connectProperties.getProperty("sncFlag") != null && connectProperties.getProperty("sncFlag").equalsIgnoreCase("On")) {

                JCoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, connectProperties.getProperty(DestinationDataProvider.JCO_ASHOST));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, connectProperties.getProperty(DestinationDataProvider.JCO_SYSNR));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_MODE, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_MODE));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_QOP, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_QOP));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_LIBRARY, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_LIBRARY));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_MYNAME, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_MYNAME));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SNC_PARTNERNAME, connectProperties.getProperty(DestinationDataProvider.JCO_SNC_PARTNERNAME));

            } else {

                JCoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, connectProperties.getProperty(DestinationDataProvider.JCO_ASHOST));
                JCoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, connectProperties.getProperty(DestinationDataProvider.JCO_SYSNR));

            }

            Set<String> keys = JCoProperties.stringPropertyNames();
            for (String key : keys) {
                if (key.contains("passwd")) {
                    logger.info(key + " : " + "*****NotShowN" + JCoProperties.getProperty(key).length());
                } else {
                    logger.info(key + " : " + JCoProperties.getProperty(key));
                }

            }

            //SWJcoDestinationProvider destinationDataProvider = SWJcoDestinationProvider.getInstance();
            if (destinationDataProvider.updateDestinationByName(servletContext + connectProperties.getProperty(DestinationDataProvider.JCO_DEST) + hostlang, JCoProperties)) {
                logger.info("Destination DP update SUCCESS... " + servletContext + connectProperties.getProperty(DestinationDataProvider.JCO_DEST) + hostlang);
                return true;
            } else {
                if (destinationDataProvider.addDestinationByName(servletContext + connectProperties.getProperty(DestinationDataProvider.JCO_DEST) + hostlang, JCoProperties)) {
                    logger.info("Destination DP update +add SUCCESS... " + servletContext + connectProperties.getProperty(DestinationDataProvider.JCO_DEST) + hostlang);
                    return true;
                }
                return false;
            }
        } catch (Throwable e) {

            throw e;
        }

    }

    public boolean removeFromDestinationMap(String host) throws Throwable {
        try {
            String servletContext = RPConfig.getRP_context();

            SWJcoDestinationProvider destinationDataProvider = SWJcoDestinationProvider.getInstance();
            if (destinationDataProvider.removeDestinationByName(servletContext + host)) {
                return true;
            } else {
                return false;
            }
        } catch (Throwable e) {
            throw e;
        }

    }

    /**
     *
     */
    public void afterRenderResponse() {
    }
}
