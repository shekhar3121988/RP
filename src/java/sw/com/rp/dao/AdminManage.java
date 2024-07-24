/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.Wini;
import sw.com.rp.config.RPConfig;
import sw.com.rp.dto.Admininfo;

/**
 *
 * @author msaini
 */
public class AdminManage {

    private static final Logger logger = LogManager.getLogger(AdminManage.class.getName());

    /**
     * This method is used to Read Admin login information
     *
     * @param directory path
     * @return HashMap of admin info
     */
    public void load_admin_Info(String dir) throws Throwable {
        logger.info("load_admin_Info() method start...");
        Ini ini = new Ini(new FileReader(dir + "/admin.info"));
        Admininfo adinfo = null;
        RPConfig.getAdmininfoList().clear();
        RPConfig.getAdmininfoMap().clear();
        System.out.println("admin info");
        for (Ini.Section section : ini.values()) {
            logger.info("Fetching [" + section.getName() + "]");
            System.out.println("\n");
            System.out.println("Fetching [" + section.getName() + "]");
            adinfo = null;
            adinfo = new Admininfo();

            adinfo.setUserID(section.get("UserID"));
            adinfo.setPassword(section.get("Password"));
            //String pwd = adinfo.getPassword();
            //pwd = decrypt(pwd);
            //adinfo.setPassword(pwd);
            RPConfig.getAdmininfoList().add(adinfo);
            RPConfig.getAdmininfoMap().put(adinfo.getUserID().toLowerCase(), adinfo);

        }
        logger.info("load_admin_Info() method end...");
    }

    /**
     * This method is used ADD Admin login
     *
     * @param Admininfo object
     * @return message Admin Successfully added
     */
    public void addAdmin(Admininfo Addadmin) throws Throwable {
        logger.info("addAdmin method start...");
        ArrayList<Admininfo> Admin = new ArrayList<Admininfo>();
        try {
            RPConfig.getAdmininfoMap().put(Addadmin.getUserID().toLowerCase(), Addadmin);
            Admin = adminhashmap_to_array(RPConfig.getAdmininfoMap());
            SaveAdminInfoToFile(Admin);
//
            logger.info("Admin Successfully added");
            load_admin_Info(RPConfig.getRP_home());
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            throw new Exception(stack.toString());
        }

        logger.info("addAdmin method end...");
    }

    /**
     * This method is used Delete Admin login
     *
     * @param Admininfo object
     * @return message Delete Successfully
     */
    public void deleteAdmin(String deleadmin) throws Throwable {
        logger.info("deleteAdmin method start...");
        ArrayList<Admininfo> admin = new ArrayList<Admininfo>();

        try {
            if (RPConfig.getAdmininfoMap().size() > 1) {
                RPConfig.getAdmininfoMap().remove(deleadmin.toLowerCase());
                admin = adminhashmap_to_array(RPConfig.getAdmininfoMap());
                SaveAdminInfoToFile(admin);
                load_admin_Info(RPConfig.getRP_home());
                logger.info("Admin User Successfully deleted");
            } else {
                throw new Exception("You can't delete single entry but can modify ");
            }
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            throw new Exception(ex.getMessage());
        }

        logger.info("deleteAdmin method end...");
    }

    /**
     * This method is used modify Admin login
     *
     * @param Admininfo object
     * @return message modify Successfully
     */
    public void modifyAdmin(Admininfo modifyadmin) throws Throwable {
        logger.info("modifyAdmin method start...");
        ArrayList<Admininfo> adminsystem = new ArrayList<Admininfo>();

        try {
            RPConfig.getAdmininfoMap().remove(modifyadmin.getUserID().toLowerCase());
            RPConfig.getAdmininfoMap().put(modifyadmin.getUserID().toLowerCase(), modifyadmin);
            adminsystem = adminhashmap_to_array(RPConfig.getAdmininfoMap());
            SaveAdminInfoToFile(adminsystem);
            load_admin_Info(RPConfig.getRP_home());
        } catch (Throwable ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            throw new Exception(ex.getMessage());
        }

        logger.info("modifyAdmin method end...");
    }

    /**
     * This method is used to write admin info in file
     *
     * @param ArrayList Admininfo object
     * @return message write Successfully
     */
    public void SaveAdminInfoToFile(ArrayList<Admininfo> systemList) throws Throwable {
        Wini ini = new Wini(new File(RPConfig.getRP_home() + "/admin.info"));
        ini.clear();
        for (Admininfo adminUser : systemList) {
            String key = adminUser.getUserID();
            ini.put(key, "UserID", adminUser.getUserID());
            ini.put(key, "Password", adminUser.getPassword());
        }
        ini.store();
    }

    /**
     * This method is used to convert HashMap to ArrayList
     *
     * @param ArrayList Admininfo object
     * @return message write Successfully
     */
    public ArrayList adminhashmap_to_array(HashMap sytemmap) {
        ArrayList<Admininfo> data = new ArrayList<Admininfo>();
        for (Object key : sytemmap.keySet()) {
            data.add((Admininfo) sytemmap.get(key));
        }
        return data;
    }

    /**
     * This method is used to encrypt the string
     *
     * @param String
     * @return encrypted String
     */
    public String encrypt(String st1) {
        String encoded = "";
        byte[] PassBytes = st1.getBytes();
        String passEncoded = Base64.encodeBase64String(PassBytes);
        logger.info("inside encryption");
        encoded = new String(passEncoded);
        return encoded;
    }

    /**
     * This method is used to decrypt the string
     *
     * @param String
     * @return decrypted String
     */
    public String decrypt(String encoded) {
        logger.info("inside decryption");
        String decoded = "";
        byte[] decodedBytes = Base64.decodeBase64(encoded);
        decoded = new String(decodedBytes, StandardCharsets.UTF_8);
//
        return decoded;
    }
}
