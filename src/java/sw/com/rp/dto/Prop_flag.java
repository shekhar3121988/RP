/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dto;

import java.io.Serializable;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;

/**
 *
 * @author msaini
 */
//@ManagedBean
//@RequestScoped
public class Prop_flag implements Serializable {

    private String Prop_SrNo;
    private String Prop_Name;
    private String Prop_value;
    private String Prop_Description;

    public Prop_flag() {
        Prop_Description = "";
        Prop_Name = "";
        Prop_SrNo = "";
        Prop_value = "";

    }

    public String getProp_Description() {
        return Prop_Description;
    }

    public void setProp_Description(String Prop_Description) {
        this.Prop_Description = Prop_Description;
    }

    public String getProp_Name() {
        return Prop_Name;
    }

    public void setProp_Name(String Prop_Name) {
        this.Prop_Name = Prop_Name;
    }

    public String getProp_SrNo() {
        return Prop_SrNo;
    }

    public void setProp_SrNo(String Prop_SrNo) {
        this.Prop_SrNo = Prop_SrNo;
    }

    public String getProp_value() {
        return Prop_value;
    }

    public void setProp_value(String Prop_value) {
        this.Prop_value = Prop_value;
    }
}
