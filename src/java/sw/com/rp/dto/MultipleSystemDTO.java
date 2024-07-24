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
public class MultipleSystemDTO implements Serializable {
    private String key;
    private String value;
    private String sysID;

    public String getSysID() {
        return sysID;
    }

    public void setSysID(String sysID) {
        this.sysID = sysID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
