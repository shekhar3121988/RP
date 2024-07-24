/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import sw.com.rp.connection.ADserver;
import sw.com.rp.connection.SapSystem;
import sw.com.rp.dto.Admininfo;
import sw.com.rp.dto.Config_flag;
import sw.com.rp.dto.Prop_flag;

/**
 *
 * @author msaini
 */
public class RPConfig {

    static private String RP_context = "RP";
    public static boolean syncSystemDesc = true;
    public static boolean syncConfig = true;
    //private static boolean sncFlag = false;
    //private static boolean loadBalancing = false;
    static private String RP_home;
    private static boolean singleHost = false;
    static private Config_flag config_flag = new Config_flag();
    private static ArrayList<Config_flag> propertyList = new ArrayList<Config_flag>();
    static private ArrayList<Prop_flag> propertyFlagsList = new ArrayList<Prop_flag>();
    static private ArrayList<Prop_flag> missedPropertyFlagsList = new ArrayList<Prop_flag>();
    private static ArrayList<SapSystem> sapSystemList = new ArrayList<SapSystem>();
    private static ArrayList<String> sapSystemNamelist = new ArrayList<String>();
    private static HashMap<String, SapSystem> backupSapSystemMap = new HashMap<String, SapSystem>();
    static private HashMap<String, Properties> appClientProperties = new HashMap<String, Properties>();
    static private SapSystem singleHostSystem = new SapSystem();
    private static HashMap<String, SapSystem> sapSystemMap = new HashMap<String, SapSystem>();
    private static HashMap<String, Admininfo> admininfoMap = new HashMap<String, Admininfo>();
    private static HashMap<String, String> maxlengthMap = new HashMap<String, String>();
    private static ArrayList<Admininfo> admininfoList = new ArrayList<Admininfo>();
    static private ArrayList<ADserver> AdServerList = new ArrayList<ADserver>();
    static private HashMap<String, ADserver> AdServerMap = new HashMap<String, ADserver>();
    static private HashMap<String, HashMap<String, String>> ResetPasswordResourcesLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> RPRequestResourceLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> AdminLoginResourceLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> CheckLoginLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> ProcessRPRequestLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> SystemResourcesLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> RequestNewPasswordLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> ResetPassLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> SAPserverLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> SaveUpdateQuesAnsLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> SupportEmailLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> UserQAResourceLangMap = new HashMap();
    static private HashMap<String, HashMap<String, String>> ValidatePasswordLangMap = new HashMap();
    static private HashMap<String, String> langListMap = new HashMap<String, String>();
    static private ArrayList<String> langList = new ArrayList<String>();
    static private Set<String> csrfExcludeUrlSet;
    public static final String JKS_FILE_NAME="/rp.store";

    public static ArrayList<String> getSapSystemNamelist() {
        return sapSystemNamelist;
    }

    public static void setSapSystemNamelist(ArrayList<String> sapSystemNamelist) {
        RPConfig.sapSystemNamelist = sapSystemNamelist;
    }

    public static HashMap<String, HashMap<String, String>> getSystemResourcesLangMap() {
        return SystemResourcesLangMap;
    }

    public static void setSystemResourcesLangMap(HashMap<String, HashMap<String, String>> SystemResourcesLangMap) {
        RPConfig.SystemResourcesLangMap = SystemResourcesLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getCheckLoginLangMap() {
        return CheckLoginLangMap;
    }

    public static void setCheckLoginLangMap(HashMap<String, HashMap<String, String>> CheckLoginLangMap) {
        RPConfig.CheckLoginLangMap = CheckLoginLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getAdminLoginResourceLangMap() {
        return AdminLoginResourceLangMap;
    }

    public static void setAdminLoginResourceLangMap(HashMap<String, HashMap<String, String>> AdminLoginResourceLangMap) {
        RPConfig.AdminLoginResourceLangMap = AdminLoginResourceLangMap;
    }

    public static ArrayList<String> getLangList() {
        return langList;
    }

    public static void setLangList(ArrayList<String> langList) {
        RPConfig.langList = langList;
    }

    public static HashMap<String, String> getLangListMap() {
        return langListMap;
    }

    public static void setLangListMap(HashMap<String, String> langListMap) {
        RPConfig.langListMap = langListMap;
    }

    public static HashMap<String, HashMap<String, String>> getResetPasswordResourcesLangMap() {
        return ResetPasswordResourcesLangMap;
    }

    public static void setResetPasswordResourcesLangMap(HashMap<String, HashMap<String, String>> ResetPasswordResourcesLangMap) {
        RPConfig.ResetPasswordResourcesLangMap = ResetPasswordResourcesLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getRPRequestResourceLangMap() {
        return RPRequestResourceLangMap;
    }

    public static void setRPRequestResourceLangMap(HashMap<String, HashMap<String, String>> RPRequestResourceLangMap) {
        RPConfig.RPRequestResourceLangMap = RPRequestResourceLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getProcessRPRequestLangMap() {
        return ProcessRPRequestLangMap;
    }

    public static void setProcessRPRequestLangMap(HashMap<String, HashMap<String, String>> ProcessRPRequestLangMap) {
        RPConfig.ProcessRPRequestLangMap = ProcessRPRequestLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getRequestNewPasswordLangMap() {
        return RequestNewPasswordLangMap;
    }

    public static void setRequestNewPasswordLangMap(HashMap<String, HashMap<String, String>> RequestNewPasswordLangMap) {
        RPConfig.RequestNewPasswordLangMap = RequestNewPasswordLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getResetPassLangMap() {
        return ResetPassLangMap;
    }

    public static void setResetPassLangMap(HashMap<String, HashMap<String, String>> ResetPassLangMap) {
        RPConfig.ResetPassLangMap = ResetPassLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getSAPserverLangMap() {
        return SAPserverLangMap;
    }

    public static void setSAPserverLangMap(HashMap<String, HashMap<String, String>> SAPserverLangMap) {
        RPConfig.SAPserverLangMap = SAPserverLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getSaveUpdateQuesAnsLangMap() {
        return SaveUpdateQuesAnsLangMap;
    }

    public static void setSaveUpdateQuesAnsLangMap(HashMap<String, HashMap<String, String>> SaveUpdateQuesAnsLangMap) {
        RPConfig.SaveUpdateQuesAnsLangMap = SaveUpdateQuesAnsLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getSupportEmailLangMap() {
        return SupportEmailLangMap;
    }

    public static void setSupportEmailLangMap(HashMap<String, HashMap<String, String>> SupportEmailLangMap) {
        RPConfig.SupportEmailLangMap = SupportEmailLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getUserQAResourceLangMap() {
        return UserQAResourceLangMap;
    }

    public static void setUserQAResourceLangMap(HashMap<String, HashMap<String, String>> UserQAResourceLangMap) {
        RPConfig.UserQAResourceLangMap = UserQAResourceLangMap;
    }

    public static HashMap<String, HashMap<String, String>> getValidatePasswordLangMap() {
        return ValidatePasswordLangMap;
    }

    public static void setValidatePasswordLangMap(HashMap<String, HashMap<String, String>> ValidatePasswordLangMap) {
        RPConfig.ValidatePasswordLangMap = ValidatePasswordLangMap;
    }

    public static HashMap<String, String> getMaxlengthMap() {
        return maxlengthMap;
    }

    public static void setMaxlengthMap(HashMap<String, String> maxlengthMap) {
        RPConfig.maxlengthMap = maxlengthMap;
    }

    public static ArrayList<Config_flag> getPropertyList() {
        return propertyList;
    }

    public static void setPropertyList(ArrayList<Config_flag> propertyList) {
        RPConfig.propertyList = propertyList;
    }

    public static ArrayList<ADserver> getAdServerList() {
        return AdServerList;
    }

    public static void setAdServerList(ArrayList<ADserver> AdServerList) {
        RPConfig.AdServerList = AdServerList;
    }

    public static HashMap<String, ADserver> getAdServerMap() {
        return AdServerMap;
    }

    public static void setAdServerMap(HashMap<String, ADserver> AdServerMap) {
        RPConfig.AdServerMap = AdServerMap;
    }

    public static HashMap<String, SapSystem> getBackupSapSystemMap() {
        return backupSapSystemMap;
    }

    public static void setBackupSapSystemMap(HashMap<String, SapSystem> backupSapSystemMap) {
        RPConfig.backupSapSystemMap = backupSapSystemMap;
    }

    public static ArrayList<Admininfo> getAdmininfoList() {
        return admininfoList;
    }

    public static void setAdmininfoList(ArrayList<Admininfo> admininfoList) {
        RPConfig.admininfoList = admininfoList;
    }

    public static HashMap<String, Admininfo> getAdmininfoMap() {
        return admininfoMap;
    }

    public static void setAdmininfoMap(HashMap<String, Admininfo> admininfoMap) {
        RPConfig.admininfoMap = admininfoMap;
    }

    public static Config_flag getConfig_flag() {
        return config_flag;
    }

    public static void setConfig_flag(Config_flag config_flag) {
        RPConfig.config_flag = config_flag;
    }

    public static ArrayList<SapSystem> getSapSystemList() {
        return sapSystemList;
    }

    public static void setSapSystemList(ArrayList<SapSystem> sapSystemList) {
        RPConfig.sapSystemList = sapSystemList;
    }

    public static ArrayList<Prop_flag> getMissedPropertyFlagsList() {
        return missedPropertyFlagsList;
    }

    public static void setMissedPropertyFlagsList(ArrayList<Prop_flag> missedPropertyFlagsList) {
        RPConfig.missedPropertyFlagsList = missedPropertyFlagsList;
    }

    public static ArrayList<Prop_flag> getPropertyFlagsList() {
        return propertyFlagsList;
    }

    public static void setPropertyFlagsList(ArrayList<Prop_flag> propertyFlagsList) {
        RPConfig.propertyFlagsList = propertyFlagsList;
    }

    public static HashMap<String, SapSystem> getSapSystemMap() {
        return sapSystemMap;
    }

    public static void setSapSystemMap(HashMap<String, SapSystem> sapSystemMap) {
        RPConfig.sapSystemMap = sapSystemMap;
    }

    public static SapSystem getSingleHostSystem() {
        return singleHostSystem;
    }

    public static void setSingleHostSystem(SapSystem singleHostSystem) {
        RPConfig.singleHostSystem = singleHostSystem;
    }

    public static HashMap<String, Properties> getAppClientProperties() {
        return appClientProperties;
    }

    public static void setAppClientProperties(HashMap<String, Properties> appClientProperties) {
        RPConfig.appClientProperties = appClientProperties;
    }

    public static String getRP_context() {
        return RP_context;
    }

    public static void setRP_context(String RP_context) {
        RPConfig.RP_context = RP_context;
    }

    public static String getRP_home() {
        return RP_home;
    }

    public static void setRP_home(String RP_home) {
        RPConfig.RP_home = RP_home;
    }

//    public static boolean isLoadBalancing() {
//        return loadBalancing;
//    }
//
//    public static void setLoadBalancing(boolean loadBalancing) {
//        RPConfig.loadBalancing = loadBalancing;
//    }

//    public static boolean isSncFlag() {
//        return sncFlag;
//    }
//
//    public static void setSncFlag(boolean sncFlag) {
//        RPConfig.sncFlag = sncFlag;
//    }

    public static boolean isSingleHost() {
        return singleHost;
    }

    public static void setSingleHost(boolean singleHost) {
        RPConfig.singleHost = singleHost;
    }

    public static Set<String> getCsrfExcludeUrlSet() {
        return csrfExcludeUrlSet;
    }

    public static void setCsrfExcludeUrlSet(Set<String> csrfExcludeUrlSet) {
        RPConfig.csrfExcludeUrlSet = csrfExcludeUrlSet;
    }
}
