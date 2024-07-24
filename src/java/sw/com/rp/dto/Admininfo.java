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
public class Admininfo implements Serializable {

    private String userID;
    private String password;
    private String confirmpassword;

    public Admininfo() {
        userID = "";
        password = "";
        confirmpassword = "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}
