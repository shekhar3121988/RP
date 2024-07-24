/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.connection;

import java.io.Serializable;

/**
 *
 * @author swadmin
 */
public class ADserver implements Serializable {

    private String domain;
    private String ip;
    private String port;
    private String userid;
    private String password;
    private String queryAction;

    public ADserver() {
        domain = "";
        ip = "";
        port = "";
        userid = "";
        password = "";
        queryAction = "Test";
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getQueryAction() {
        return queryAction;
    }

    public void setQueryAction(String queryAction) {
        this.queryAction = queryAction;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public ADserver(String domain) {
        this.domain = domain;
    }

    public ADserver(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
    }

    public ADserver(String domain, String ip, String port) {
        this.domain = domain;
        this.ip = ip;
        this.port = port;
    }

    public ADserver(String domain, String ip, String port, String userid) {
        this.domain = domain;
        this.ip = ip;
        this.port = port;
        this.userid = userid;
    }

    public ADserver(String domain, String ip, String port, String userid, String password) {
        this.domain = domain;
        this.ip = ip;
        this.port = port;
        this.userid = userid;
        this.password = password;
    }

    public ADserver(String domain, String ip, String port, String userid, String password, String queryAction) {
        this.domain = domain;
        this.ip = ip;
        this.port = port;
        this.userid = userid;
        this.password = password;
        this.queryAction = queryAction;
    }
    
}
