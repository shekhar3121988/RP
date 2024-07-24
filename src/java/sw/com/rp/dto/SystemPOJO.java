/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dto;

import java.io.Serializable;

/**
 *
 * @author msaini
 */
public class SystemPOJO implements Serializable {

    private String systemname;
    private String syskey;

    public SystemPOJO() {
        this.systemname = "";
        this.syskey = "";
    }

    public String getSyskey() {
        return syskey;
    }

    public void setSyskey(String syskey) {
        this.syskey = syskey;
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }
}
