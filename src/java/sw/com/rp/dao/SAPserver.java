/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import sw.com.rp.config.RPConfig;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sw.jco.SWJcoDestinationProvider;
import com.sw.util.SecCrypt;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ini4j.Ini;
import sw.com.rp.connection.SapSystem;

/**
 *
 * @author msaini
 */
public class SAPserver {

    private static final Logger logger = LogManager.getLogger(SAPserver.class.getName());

    public void LoadSAPinfo(String dir) throws Throwable {
        logger.info("LoadSAPinfo() method start...");
//        RPConfig.getSapSystemNamelist().clear();
        RPConfig.getSapSystemMap().clear();
        RPConfig.getSapSystemList().clear();
        RPConfig.setSingleHostSystem(null);
        RPConfig.getAppClientProperties().clear();
        Ini ini = new Ini(new FileReader(dir + "/System.info"));
        SapSystem sapSys = null;
        int hostcount = 0;
        for (Ini.Section section : ini.values()) {
            logger.info("Fetching [" + section.getName() + "]");
            System.out.println("\n");
            System.out.println("Fetching [" + section.getName() + "]");
            Properties prop = new Properties();
            sapSys = null;
            sapSys = new SapSystem();
            if (getSectionProperty("sncFlag", section).equalsIgnoreCase("OFF")) {
                //RPConfig.setSncFlag(false);
            }
            else{
                //RPConfig.setSncFlag(true);
                sapSys.setSncFlag(getSectionProperty("sncFlag", section));
                sapSys.setSncLevel(getSectionProperty("sncLevel", section));
                sapSys.setSncMode(getSectionProperty("sncMode", section));
                sapSys.setSncName(getSectionProperty("sncName", section));
                sapSys.setSncPartner(getSectionProperty("sncPartner", section));
                sapSys.setSncService(getSectionProperty("sncService", section));
            }
                
            if (getSectionProperty("loadBalancing", section).equalsIgnoreCase("OFF")) {
                //RPConfig.setLoadBalancing(false);
            }
            prop.setProperty("loadBalancing",getSectionProperty("loadBalancing", section));
            prop.setProperty("sncFlag",getSectionProperty("sncFlag", section));
            prop.setProperty("routerFlag",getSectionProperty("routerFlag", section));

            sapSys.setRfcname(getSectionProperty("RfcName", section));
            sapSys.setHost(getSectionProperty("HostFlag", section));
            sapSys.setName(getSectionProperty("SystemName", section));
            sapSys.setSystemKey(section.getName());
            sapSys.setENFlag(getSectionProperty("ENFlag", section));
            System.out.println("System name" + section.get("SystemName"));
            prop.setProperty(DestinationDataProvider.JCO_DEST, getSectionProperty("SystemName", section));


//            prop.setProperty(DestinationDataProvider.JCO_USER, getSectionProperty("UserID", section));
//            sapSys.setUser(getSectionProperty("UserID", section));
//
//            String aPass = decrypt(getSectionProperty("Password", section));
//            prop.setProperty(DestinationDataProvider.JCO_PASSWD, aPass);
//            sapSys.setPassword(aPass);
            
            sapSys.setUser("");
            sapSys.setPassword("");
            
            prop.setProperty(SWJcoDestinationProvider.JKS_UID_KEY, sapSys.getName()+SecCrypt.KEY_PADDING+SapSystem.UID);
            prop.setProperty(SWJcoDestinationProvider.JKS_PWD_KEY, sapSys.getName()+SecCrypt.KEY_PADDING+SapSystem.PWD);

            prop.setProperty(DestinationDataProvider.JCO_CLIENT, getSectionProperty("Client", section));
            sapSys.setClient(getSectionProperty("Client", section));

            prop.setProperty(DestinationDataProvider.JCO_ASHOST, getSectionProperty("IPaddress", section));
            sapSys.setIpaddress(getSectionProperty("IPaddress", section));

            prop.setProperty(DestinationDataProvider.JCO_SYSNR, getSectionProperty("SystemNo", section));
            sapSys.setSyno(getSectionProperty("SystemNo", section));

            prop.setProperty(DestinationDataProvider.JCO_LANG, getSectionProperty("Language", section));
            sapSys.setLang(getSectionProperty("Language", section));

            //prop.setProperty(DestinationDataProvider.JCO_GROUP, section.get("group"));
            sapSys.setLoadBalancing(getSectionProperty("loadBalancing", section));

            prop.setProperty(DestinationDataProvider.JCO_MSHOST, getSectionProperty("IPaddress", section));

            sapSys.setLbService(getSectionProperty("lbService", section));
            prop.setProperty(DestinationDataProvider.JCO_MSSERV, getSectionProperty("lbService", section));


            sapSys.setLbGroupName(getSectionProperty("lbGroupName", section));
            prop.setProperty(DestinationDataProvider.JCO_GROUP, getSectionProperty("lbGroupName", section));

            sapSys.setLbR3Name(getSectionProperty("lbR3Name", section));
            prop.setProperty(DestinationDataProvider.JCO_R3NAME, getSectionProperty("lbR3Name", section));

            sapSys.setRouterFlag(getSectionProperty("routerFlag", section));

            sapSys.setRouterString(getSectionProperty("routerString", section));
            prop.setProperty(DestinationDataProvider.JCO_SAPROUTER, getSectionProperty("routerString", section));


            prop.setProperty(DestinationDataProvider.JCO_SNC_MODE, getSectionProperty("sncMode", section));
            prop.setProperty(DestinationDataProvider.JCO_SNC_MYNAME, getSectionProperty("sncName", section));
            prop.setProperty(DestinationDataProvider.JCO_SNC_LIBRARY, getSectionProperty("sncService", section));
            prop.setProperty(DestinationDataProvider.JCO_SNC_PARTNERNAME, getSectionProperty("sncPartner", section));
            prop.setProperty(DestinationDataProvider.JCO_SNC_QOP, getSectionProperty("sncLevel", section));
            sapSys.setBackup(getSectionProperty("Backup", section));
            sapSys.setDescription(getSectionProperty("Description", section));


//            System_DropMap.put(section.get("SystemName"), section.get("SystemName"));
            if (sapSys.getHost().equalsIgnoreCase("ON")) {
                hostcount++;
                RPConfig.setSingleHostSystem(sapSys);
                logger.info("Single Host System : " + sapSys.getName());

            }
            if (hostcount > 1) {
                logger.info("Host flag is ON for more than one System. Please Correct this issue to avoid abnormal behavior of the application.");
            }
            if (sapSys.getBackup().equalsIgnoreCase("No")) {
                RPConfig.getSapSystemList().add(sapSys);
                RPConfig.getSapSystemMap().put(sapSys.getName(), sapSys);
            } else {
                RPConfig.getBackupSapSystemMap().put(sapSys.getName(), sapSys);
            }

            RPConfig.getSapSystemNamelist().add(sapSys.getName());


            RPConfig.getAppClientProperties().put(sapSys.getSystemKey(), prop);

        }
        logger.info("LoadSAPinfo() method end...");

    }

    public String getSectionProperty(String propertyName, Ini.Section section) {

        if (section.get(propertyName) == null) {
            logger.error("**************>    " + section.getName() + " property " + propertyName + " missing in System.info file.");
            return "NotFound";
        } else {
            return section.get(propertyName);
        }

    }

    /**
     * This method is used to decrypt the string
     * @param String
     * @return  decrypted String
     */
    public String decrypt(String encoded) {
        logger.info("inside decryption");
        String decoded = "";
        byte[] decodedBytes = Base64.decodeBase64(encoded);
        decoded = new String(decodedBytes, StandardCharsets.UTF_8);
        return decoded;
    }
}
