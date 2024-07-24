/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author msaini
 */
public class RPRequest implements Serializable {

    private String lastName;
    private String firstName;
    private String userID;
    private String emailID;
    private String sapSystem;
    private String userIDApp;
    private ArrayList<String> multipleSystem;
//    private String hostSystem;
//    private String rpURL;

    public RPRequest() {
        this.lastName = "";
        this.firstName = "";
        this.userID = "";
        this.emailID = "";
        this.sapSystem = "";
        this.userIDApp = "";
        this.multipleSystem = new ArrayList<String>();
//        this.hostSystem = "";
//        this.rpURL = "";
    }

    public RPRequest(String lastName, String firstName, String userID, String emailID, String sapSystem, String userIDApp) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.userID = userID;
        this.emailID = emailID;
        this.sapSystem = sapSystem;
        this.userIDApp = userIDApp;
    }

    public ArrayList<String> getMultipleSystem() {
        return multipleSystem;
    }

    public void setMultipleSystem(ArrayList<String> multipleSystem) {
        this.multipleSystem = multipleSystem;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSapSystem() {
        return sapSystem;
    }

    public void setSapSystem(String sapSystem) {
        this.sapSystem = sapSystem;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserIDApp() {
        return userIDApp;
    }

    public void setUserIDApp(String userIDApp) {
        this.userIDApp = userIDApp;
    }

    @Override
    public String toString() {
        return "UserID: " + userID
                + ", FirstName: " + firstName
                + ", LastName: " + lastName
                + ", Email: " + emailID;
//                +", SAPSystem: "+sapSystem;
    }
//    public String getHostSystem() {
//        return hostSystem;
//    }
//
//    public void setHostSystem(String hostSystem) {
//        this.hostSystem = hostSystem;
//    }
//    public String getRpURL() {
//        return rpURL;
//    }
//
//    public void setRpURL(String rpURL) {
//        this.rpURL = rpURL;
//    }
}
