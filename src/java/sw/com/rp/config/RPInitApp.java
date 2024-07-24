/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ini4j.Ini;
import sw.com.rp.connection.ADserver;
import sw.com.rp.dao.AdminManage;
import sw.com.rp.dao.BapiConfiguration;
import sw.com.rp.dao.SAPserver;
import sw.com.rp.dto.Prop_flag;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author msaini
 */
public class RPInitApp extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RPInitApp.class.getName());
    private final static Pattern COMMA = Pattern.compile(",");
    SAPserver sapProperty = new SAPserver();
    AdminManage admin = new AdminManage();
    BapiConfiguration bapiConfig = new BapiConfiguration();
    public static ServletConfig servletConfig = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletConfig = config;
        String app_Context = config.getServletContext().getContextPath();
        if (app_Context != null && app_Context.length() > 0) {
            app_Context = app_Context.substring(1) + "@";
            RPConfig.setRP_context(app_Context);
            logger.info("RP-Context: " + RPConfig.getRP_context());
        } else {
            logger.info("Default RP-Context: " + RPConfig.getRP_context());
        }
        String FileLocation = null;
        FileLocation = config.getInitParameter("RP-home");
        if (FileLocation == null) {
            System.err.println("*** No RP-home ");
            logger.error("*** No RP-home ");
        } else {
            RPConfig.setRP_home(FileLocation);
        }

        String csrfExcludeURL = config.getInitParameter("csrfExcludeURL");

        if (csrfExcludeURL != null) {
            csrfExcludeURL = csrfExcludeURL.replaceAll("[\\n\\t ]", "");
            String[] parts = COMMA.split(csrfExcludeURL);

            RPConfig.setCsrfExcludeUrlSet(new HashSet<String>(parts.length));
            int i = 1;
            for (String cur : parts) {
                logger.debug("csrfExclude URL #" + i++ + " " + cur + "\n");
                RPConfig.getCsrfExcludeUrlSet().add(cur);
            }

        } else {
            RPConfig.setCsrfExcludeUrlSet(new HashSet<String>(0));
            logger.error("*** No csrfExcludeURL list maintained in web.xml");
        }

        try {
            sapProperty.LoadSAPinfo(RPConfig.getRP_home());
            // processRPPropertyFile(RPConfig.getRP_home());
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }

        try {
            readMultiLangXmlFile(RPConfig.getRP_home() + "/language");
            String sysKey = RPConfig.getSingleHostSystem().getSystemKey();
            bapiConfig.setPropFromBapi(sysKey);
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        // load admin file
        try {
            admin.load_admin_Info(RPConfig.getRP_home());
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        //AD server file
        try {
            load_ADserver_Info(RPConfig.getRP_home());
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        super.init(config);
    }

    /**
     *
     * This is the main method which makes use of addNum method.
     *
     * @param RR home directory Path
     * @return Load AD Server information in Application Bean.
     * @exception IOException File not found.
     *
     */
    public void load_ADserver_Info(String dir) throws Throwable {
        logger.info("load_ADserver_Info() method start...");
        Ini ini = new Ini(new FileReader(dir + "/server.info"));
        ADserver ADSer = null;
        RPConfig.getAdServerList().clear();
        RPConfig.getAdServerMap().clear();
        System.out.println("AD server info");
//        RPConfig.setAdCount(1);
        for (Ini.Section section : ini.values()) {
            logger.info("Fetching [" + section.getName() + "]");
            System.out.println("\n");
            System.out.println("Fetching [" + section.getName() + "]");
            ADSer = null;
            ADSer = new ADserver();

            ADSer.setDomain(section.get("Domain"));
            ADSer.setIp(section.get("IPaddress"));
            //ADSer.setUserid(section.get("UserID"));
            //ADSer.setPassword(section.get("Password"));
            ADSer.setPort(section.get("Port"));
            //String pwd = ADSer.getPassword();
            //pwd = decrypt(pwd);
            //ADSer.setPassword(pwd);
            RPConfig.getAdServerList().add(ADSer);
//            RPConfig.getAdServernameList().add(ADSer.getDomain());
            RPConfig.getAdServerMap().put(ADSer.getDomain(), ADSer);

        }
        logger.info("load_ADserver_Info() method end...");
    }

//    public void processRPPropertyFile(String propertyFileLocation) throws Throwable {
//        logger.info("processSPPropertyFile() method start...");
//        Prop_flag pflag = null;
//        RPConfig.getPropertyFlagsList().clear();
//        RPConfig.getMissedPropertyFlagsList().clear();
//        int propertyCount = 0;
//        File propertyFile = new File(propertyFileLocation + "/properties.info");
//        if (propertyFile.exists()) {
//            FileInputStream in = null;
//            Properties pro = null;
//            try {
//                pro = new Properties();
//                in = new FileInputStream(propertyFile);
//                try {
//                    pro.load(in);
//                } catch (IOException ex) {
//                    logger.error(ex);
//                }
//            } catch (FileNotFoundException ex) {
//                logger.error(ex);
//            } finally {
//                try {
//                    in.close();
//                } catch (IOException ex) {
////                    Logger.getLogger(RPinitApp.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            Config_flag confg = new Config_flag();
//            String propValue = "";
//            propValue = loadProperty(pro, "securityweaver.HIDE_USER_TAB", "0",
//                    "Set 0 to hide user request tab and set 1 to display user request tab.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHIDE_USER_REG(false);
//                confg.setHIDE_USER_REG(false);
//            } else {
//                RPConfig.getConfig_flag().setHIDE_USER_REG(true);
//                confg.setHIDE_USER_REG(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.HIDE_PASSWORD_TAB", "0",
//                    "Set 0 to hide reset password tab and set 1 to display reset password tab.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHIDE_RESET_PASS(false);
//                confg.setHIDE_RESET_PASS(false);
//            } else {
//                RPConfig.getConfig_flag().setHIDE_RESET_PASS(true);
//                confg.setHIDE_RESET_PASS(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.HIDE_SUPPORT_TAB", "0",
//                    "Set 0 to hide support tab and set 1 to display support tab.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHIDE_SUPPORT(false);
//                confg.setHIDE_SUPPORT(false);
//            } else {
//                RPConfig.getConfig_flag().setHIDE_SUPPORT(true);
//                confg.setHIDE_SUPPORT(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.HIDE_CONFIG_TAB", "0",
//                    "Set 0 to hide configuration tab and set 1 to display configuration tab.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHIDE_CONFIG(false);
//                confg.setHIDE_CONFIG(false);
//            } else {
//                RPConfig.getConfig_flag().setHIDE_CONFIG(true);
//                confg.setHIDE_CONFIG(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.HIDE_HOME_TAB", "0",
//                    "Set 0 to hide home tab and set 1 to display home tab.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHIDE_HOME(false);
//                confg.setHIDE_HOME(false);
//            } else {
//                RPConfig.getConfig_flag().setHIDE_HOME(true);
//                confg.setHIDE_HOME(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.GETSSO", "1",
//                    "Set 0 for SSO with POST Method and 1 for SSO with GET Method.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setGETSSO(false);
//                confg.setGETSSO(false);
//            } else {
//                RPConfig.getConfig_flag().setGETSSO(true);
//                confg.setGETSSO(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.SSO", "1",
//                    "Set 1 to enable SSO Functionality and 0 to login via UserID and Password .");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setSSO(false);
//                confg.setSSO(false);
//            } else {
//                RPConfig.getConfig_flag().setSSO(true);
//                confg.setSSO(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.REQPARAM", "GID",
//                    "This parameter is used for SSO functionality.");
//            RPConfig.getConfig_flag().setREQPARAM(propValue);
//            confg.setREQPARAM(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.JAVA_ENCRYPTION", "1",
//                    "Set 1 to enable java side encryption of answers and Set 0 to enable SAP side encryption of answers.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setJAVA_ENCRYPTION(false);
//                confg.setJAVA_ENCRYPTION(false);
//            } else {
//                RPConfig.getConfig_flag().setJAVA_ENCRYPTION(true);
//                confg.setJAVA_ENCRYPTION(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.HIDE_NAME", "1",
//                    "Set 0 to hide user's first name-last name fields and set 1 to display user's first name-last name fields.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHIDE_NAME(false);
//                confg.setHIDE_NAME(false);
//            } else {
//                RPConfig.getConfig_flag().setHIDE_NAME(true);
//                confg.setHIDE_NAME(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.HIDE_EMAIL", "1",
//                    "Set 0 to hide user's email id field and set 1 to display user's email id field.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHIDE_EMAIL(false);
//                confg.setHIDE_EMAIL(false);
//            } else {
//                RPConfig.getConfig_flag().setHIDE_EMAIL(true);
//                confg.setHIDE_EMAIL(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.QUES_ANS_WORKFLOW", "1",
//                    "Set 1 to enable questions-answers workflow and set 0 to disable questions-answers workflow.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setQUES_ANS_WORKFLOW(false);
//                confg.setQUES_ANS_WORKFLOW(false);
//            } else {
//                RPConfig.getConfig_flag().setQUES_ANS_WORKFLOW(true);
//                confg.setQUES_ANS_WORKFLOW(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.RP_URL", "http://localhost:8080/RP",
//                    "Mention Reset Password URL.");
//            RPConfig.getConfig_flag().setRP_URL(propValue);
//            confg.setRP_URL(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.HELP_DESK", "1",
//                    "Set 1 to enable help desk functionality and set 0 to disable help desk functionality.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setHELP_DESK(false);
//                confg.setHELP_DESK(false);
//            } else {
//                RPConfig.getConfig_flag().setHELP_DESK(true);
//                confg.setHELP_DESK(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.EMAIL_QUES_LINK", "1",
//                    "Set 1 to get question screen link in email and set 0 to show question screen directly.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setEMAIL_QUES_LINK(false);
//                confg.setEMAIL_QUES_LINK(false);
//            } else {
//                RPConfig.getConfig_flag().setEMAIL_QUES_LINK(true);
//                confg.setEMAIL_QUES_LINK(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.PASS_LOGIC", "1",
//                    "Set 1 to enable SAP standard SAP function and set 0 to enable RP random 8-character password generation.");
//            RPConfig.getConfig_flag().setPASS_LOGIC(propValue);
//            confg.setPASS_LOGIC(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.SUPPORT_EMAIL", "support@domain.com",
//                    "Mention email address of the person responsible for providing support.");
//            RPConfig.getConfig_flag().setSUPPORT_EMAIL(propValue);
//            confg.setSUPPORT_EMAIL(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.HELP_CONTACT", "Security Weaver",
//                    "Mention Contact Name for help desk.Please mention NA If you do not want to use this property.");
//            RPConfig.getConfig_flag().setHELP_CONTACT(propValue);
//            confg.setHELP_CONTACT(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.HELP_EMAIL", "helpdesk@swindia.com",
//                    "Mention Contact Email for help desk.Please mention NA If you do not want to use this property.");
//            RPConfig.getConfig_flag().setHELP_EMAIL(propValue);
//            confg.setHELP_EMAIL(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.HELP_PHONE", "+1-800-620-4210",
//                    "Mention help desk telephone number.Please mention NA If you do not want to use this property.");
//            RPConfig.getConfig_flag().setHELP_PHONE(propValue);
//            confg.setHELP_PHONE(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.HELP_PAGE", "http://securityweaver.com/Support_Main.asp",
//                    "Mention the Web Link or URL for help desk.Please mention NA If you do not want to use this property.");
//            RPConfig.getConfig_flag().setHELP_PAGE(propValue);
//            confg.setHELP_PAGE(propValue);
//
//            propValue = loadProperty(pro, "securityweaver.GREY_UID", "1",
//                    "Set 1 to make user id field readonly or set 0 to open user id field for input.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setGREY_UID(false);
//                confg.setGREY_UID(false);
//            } else {
//                RPConfig.getConfig_flag().setGREY_UID(true);
//                confg.setGREY_UID(true);
//            }
//
//            propValue = loadProperty(pro, "securityweaver.MULTI_HOST", "1",
//                    "Set 1 to enable Multi-Host functionality or Set 0 to enable Single-Host functionality.");
//            if (propValue.equalsIgnoreCase("0")) {
//                RPConfig.getConfig_flag().setMULTI_HOST(false);
//                confg.setMULTI_HOST(false);
//            } else {
//                RPConfig.getConfig_flag().setMULTI_HOST(true);
//                confg.setMULTI_HOST(true);
//            }
//
//            //***************************************************************************
//            RPConfig.getPropertyList().add(confg);
//        } else {
//            logger.error("Property file not exist");
//        }
//        logger.info("processSPPropertyFile() method End...");
//    }
    public String loadProperty(Properties pro, String propertyName, String propertyDefaultValue, String propertyDescription) {
        Prop_flag pflag = new Prop_flag();
        int count = RPConfig.getPropertyFlagsList().size() + 1;
        pflag.setProp_SrNo("" + count);
        pflag.setProp_Name(propertyName);
        pflag.setProp_Description(propertyDescription);
        if (pro.getProperty(propertyName) != null) {
            pflag.setProp_value(pro.getProperty(propertyName).trim());
        } else {
            pflag.setProp_value(propertyDefaultValue);
            RPConfig.getMissedPropertyFlagsList().add(pflag);
            logger.info("Error: in readind property:-" + propertyName);
        }
        RPConfig.getPropertyFlagsList().add(pflag);
        return pflag.getProp_value();
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

    public void readMultiLangXmlFile(String dir) {//RPMS100
        logger.info("readMultiLangXmlFile() method starts..");

        logger.info("Trying to read multilingual XML Files from " + dir);

        File directory = new File(dir);
        String filename[] = directory.list();
        String fileExtension = "";
        logger.info("Xml files list :");
        String fileName = "";
        int dotIndex = 0;
        int userLoginMapSize = 0;
        for (int i = 0; i < filename.length; i++) {//Map Size Initialization
            dotIndex = filename[i].lastIndexOf(".");
            fileExtension = filename[i].substring(dotIndex);
            fileName = filename[i].toLowerCase();
            if (fileExtension.equalsIgnoreCase(".xml")) {
                if (fileName.startsWith("userlogin")) {
                    userLoginMapSize++;
                }
            }
        }
        fileName = "";
        dotIndex = 0;
        fileExtension = "";
        RPConfig.getResetPasswordResourcesLangMap().clear();
        RPConfig.getRPRequestResourceLangMap().clear();
        RPConfig.getAdminLoginResourceLangMap().clear();
        RPConfig.getSystemResourcesLangMap().clear();
        RPConfig.getCheckLoginLangMap().clear();
        RPConfig.getProcessRPRequestLangMap().clear();
        RPConfig.getRequestNewPasswordLangMap().clear();
        RPConfig.getResetPassLangMap().clear();
        RPConfig.getSAPserverLangMap().clear();
        RPConfig.getSaveUpdateQuesAnsLangMap().clear();
        RPConfig.getSupportEmailLangMap().clear();
        RPConfig.getUserQAResourceLangMap().clear();
        RPConfig.getValidatePasswordLangMap().clear();
        RPConfig.getLangListMap().clear();
        String mainMapKey = "";
        String subMapKey = "";
        String subMapValue = "";
        String symbol = "";
        HashMap<String, String> langMap;
        for (int i = 0; i < filename.length; i++) {
            dotIndex = filename[i].lastIndexOf(".");
            fileExtension = filename[i].substring(dotIndex);
            fileName = filename[i].toLowerCase();
            if (fileExtension.equalsIgnoreCase(".xml")) {
                langMap = new HashMap<String, String>();
                SAXBuilder builder = new SAXBuilder();
                try {
                    logger.info("Reading XML lang File : " + filename[i]);
                    Document xmlDoc = builder.build(dir + "/" + filename[i]);
                    Element root = xmlDoc.getRootElement();
                    String rootName = "";
                    rootName = root.getName();
                    if (rootName.equalsIgnoreCase("page")) {
                        List rootAttrib = root.getAttributes();
                        Iterator rootAttribItr = rootAttrib.iterator();
                        while (rootAttribItr.hasNext()) {
                            Attribute atrib = (Attribute) rootAttribItr.next();
                            String rootAtribname = atrib.getName();
                            if (rootAtribname.equalsIgnoreCase("language")) {
                                mainMapKey = atrib.getValue();//key for Page Map

                            }
                            if (rootAtribname.equalsIgnoreCase("symbol")) {
                                symbol = atrib.getValue();//key for Page Map

                            }
                        }
                        if (mainMapKey != null && mainMapKey != "" && symbol != null && symbol != "") {
                            RPConfig.getLangListMap().put(mainMapKey, symbol);

                        }
                        List rootChildren = root.getChildren();
                        Iterator rootChildrenItr = rootChildren.iterator();
                        while (rootChildrenItr.hasNext()) {
                            Element text = (Element) rootChildrenItr.next();
                            if (text.getName().equalsIgnoreCase("text")) {
                                List textAttrib = text.getAttributes();
                                Iterator textAttribItr = textAttrib.iterator();
                                while (textAttribItr.hasNext()) {
                                    Attribute atrib = (Attribute) textAttribItr.next();
                                    String textAtribName = atrib.getName();
                                    if (textAtribName.equalsIgnoreCase("id")) {
                                        subMapKey = atrib.getValue();
                                    }
                                }
                                subMapValue = text.getText();
                                if (subMapKey != null && subMapValue != null && subMapKey != "" && subMapValue != "") {
                                    langMap.put(subMapKey, subMapValue);
                                }
                            }

                        }//End of Children parse
                    } else {
                        logger.info("Invalid Xml file Format..");
                    }//end of root name check
                    if (fileName.startsWith("userqaresource")) {
                        RPConfig.getUserQAResourceLangMap().put(mainMapKey, langMap);
//                        System.out.println(mainMapKey+"s1111"+langMap);
                    }
                    if (fileName.startsWith("resetpasswordresources")) {
                        RPConfig.getResetPasswordResourcesLangMap().put(mainMapKey, langMap);
//                            System.out.println(langMap);
                    }
                    if (fileName.startsWith("rprequestresource")) {
                        RPConfig.getRPRequestResourceLangMap().put(mainMapKey, langMap);
//                            System.out.println("ss1111"+langMap);
                    }
                    if (fileName.startsWith("adminloginresource")) {
                        RPConfig.getAdminLoginResourceLangMap().put(mainMapKey, langMap);
//                            System.out.println(langMap);
                    }
                    if (fileName.startsWith("checklogin")) {
                        RPConfig.getCheckLoginLangMap().put(mainMapKey, langMap);
//                            System.out.println(langMap);
                    }
                    if (fileName.startsWith("systemsresource")) {
                        RPConfig.getSystemResourcesLangMap().put(mainMapKey, langMap);
//                            System.out.println(langMap);
                    }
                    if (fileName.startsWith("supportemail")) {
                        RPConfig.getSupportEmailLangMap().put(mainMapKey, langMap);
//                            System.out.println(langMap);
                    }
                    if (fileName.startsWith("validatepassword")) {
                        RPConfig.getValidatePasswordLangMap().put(mainMapKey, langMap);
//                            System.out.println(langMap);
                    }

                } catch (IOException e1) {
                    logger.info("\nInvalid Xml file Format\n" + e1.toString() + "\n");
                } catch (JDOMException je) {
                    logger.info("\nInvalid Xml file Format\n" + je.toString() + "\n");
                } catch (Exception e) {
                    logger.info("\nInvalid Xml file Format\n" + e.toString() + "\n");
                }

                mainMapKey = "";
                subMapKey = "";
                subMapValue = "";
                symbol = "";

            } else {
                logger.info("Invalid File found : " + filename[i] + " is not an XML file.");
            }
        }//end of Loop for xml file read
        Set<String> langSet = RPConfig.getLangListMap().keySet();
        RPConfig.getLangList().clear();
        for (String key : langSet) {
            RPConfig.getLangList().add(key);
        }
        Collections.sort(RPConfig.getLangList());
        logger.info("Total " + RPConfig.getLangList().size() + " Languages found : " + RPConfig.getLangListMap());

        logger.info("readMultiLangXmlFile() method ends..");
    }
}
