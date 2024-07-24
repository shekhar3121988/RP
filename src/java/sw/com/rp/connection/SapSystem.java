/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.connection;

import sw.com.rp.dto.*;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class SapSystem implements Serializable {

    public static final String UID = "UID";
    public static final String PWD = "PWD";
    private String name;
    private String user;
    private String password;
    private String client;
    private String ipaddress;
    private String syno;
    private String rfcname;
    private String hostrfc;
    private String lang;
    private String host;
    private String sodflag;
//    private String group;
    private String sncMode;
    private String sncName;
    private String sncService;
    private String sncPartner;
    private String sncLevel;
    private String sncFlag;
    private String loadBalancing;
    private String backup;
    private String queryAction;
    private String SystemKey;
    private String ENFlag;
    private String description;
    private String lbR3Name;
    private String lbGroupName;
    private String lbService;
    private String routerFlag;
    private String routerString;
    ///sap

    public SapSystem() {
        routerFlag = "OFF";
        routerString = "";
        sncFlag = "OFF";
        loadBalancing = "OFF";
        host = "OFF";
        lang = "EN";
//        group = "TestGroup";
        sncMode = "";
        sncName = "";
        sncService = "";
        sncPartner = "";
        sncLevel = "";
        backup = "NO";
        queryAction = "Test";
        ENFlag = "NA";
        description = "";
        lbR3Name = "";
        lbGroupName = "";
        lbService = "";

    }

    public String getRouterFlag() {
        return routerFlag;
    }

    public void setRouterFlag(String routerFlag) {
        this.routerFlag = routerFlag;
    }

    public String getRouterString() {
        return routerString;
    }

    public void setRouterString(String routerString) {
        this.routerString = routerString;
    }

    public String getLbGroupName() {
        return lbGroupName;
    }

    public void setLbGroupName(String lbGroupName) {
        this.lbGroupName = lbGroupName;
    }

    public String getLbR3Name() {
        return lbR3Name;
    }

    public void setLbR3Name(String lbR3Name) {
        this.lbR3Name = lbR3Name;
    }

    public String getLbService() {
        return lbService;
    }

    public void setLbService(String lbService) {
        this.lbService = lbService;
    }

    public String getENFlag() {
        return ENFlag;
    }

    public void setENFlag(String ENFlag) {
        this.ENFlag = ENFlag;
    }

    public String getSystemKey() {
        return SystemKey;
    }

    public void setSystemKey(String SystemKey) {
        this.SystemKey = SystemKey;
    }

    public String getQueryAction() {
        return queryAction;
    }

    public void setQueryAction(String queryAction) {
        this.queryAction = queryAction;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHostrfc() {
        return hostrfc;
    }

    public void setHostrfc(String hostrfc) {
        this.hostrfc = hostrfc;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLoadBalancing() {
        return loadBalancing;
    }

    public void setLoadBalancing(String loadBalancing) {
        this.loadBalancing = loadBalancing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRfcname() {
        return rfcname;
    }

    public void setRfcname(String rfcname) {
        this.rfcname = rfcname;
    }

    public String getSncFlag() {
        return sncFlag;
    }

    public void setSncFlag(String sncFlag) {
        this.sncFlag = sncFlag;
    }

    public String getSncLevel() {
        return sncLevel;
    }

    public void setSncLevel(String sncLevel) {
        this.sncLevel = sncLevel;
    }

    public String getSncMode() {
        return sncMode;
    }

    public void setSncMode(String sncMode) {
        this.sncMode = sncMode;
    }

    public String getSncName() {
        return sncName;
    }

    public void setSncName(String sncName) {
        this.sncName = sncName;
    }

    public String getSncPartner() {
        return sncPartner;
    }

    public void setSncPartner(String sncPartner) {
        this.sncPartner = sncPartner;
    }

    public String getSncService() {
        return sncService;
    }

    public void setSncService(String sncService) {
        this.sncService = sncService;
    }

    public String getSodflag() {
        return sodflag;
    }

    public void setSodflag(String sodflag) {
        this.sodflag = sodflag;
    }

    public String getSyno() {
        return syno;
    }

    public void setSyno(String syno) {
        this.syno = syno;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
