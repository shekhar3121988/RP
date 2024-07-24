/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sw.com.rp.dto;

import java.io.Serializable;

/**
 *
 * @author msuppahiya
 */
public class Support implements Serializable {
    private String systemName;
    private String subject;
    private String comments;
    private String email;
    private String message;
    private boolean success;

    public Support(String systemName, String subject, String comments, String email, String message, boolean success) {
        this.systemName = systemName;
        this.subject = subject;
        this.comments = comments;
        this.email = email;
        this.message = message;
        this.success = success;
    }
    public Support() {
        this.systemName = "";
        this.subject = "";
        this.comments = "";
        this.email = "";
        this.message = "";
        this.success = false;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    

}
