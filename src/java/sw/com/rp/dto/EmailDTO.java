/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sw.com.rp.dto;

import java.io.Serializable;

/**
 *
 * @author msainii
 */
public class EmailDTO implements Serializable {

    private String userid;
    private String charurl;
    private String system;
    private String lang;
    private String token;
    private String requestId;

   

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getCharurl() {
        return charurl;
    }

    public void setCharurl(String charurl) {
        this.charurl = charurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    
    

}
