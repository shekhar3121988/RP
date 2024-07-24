/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sw.com.rp.rest;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author msaini
 */
public class ListWrapper<T> implements Serializable {
    private List<T> data;
   private int total;

    private int totalDisplayRecords;

    private  Boolean success = false;
    private String message="";
    private String userid="";

   public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getTotalDisplayRecords() {
        return totalDisplayRecords;
    }

    public void setTotalDisplayRecords(int totalDisplayRecords) {
        this.totalDisplayRecords = totalDisplayRecords;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    

}
