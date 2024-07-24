/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sw.com.rp.dto;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author msaini
 */
//@JsonTypeInfo(use=Id.NAME, include=As.WRAPPER_OBJECT)
//@JsonTypeName("User")
public class User implements Serializable{

    protected String userid;
    protected String password;
    protected String host;
    protected String hostName;
    protected String newpassword;
    protected boolean success ;
    protected String message;
    protected String sapsystem;
    protected String xTID = "";
    //protected ArrayList myList;


    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }
    public User(){
//        myList = new ArrayList();
//        myList.add("item1");
//        myList.add("item2");
//        myList.add("item3");
    }

    /**
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

   


    
    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Get the value of userid
     *
     * @return the value of userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Set the value of userid
     *
     * @param userid new value of userid
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSapsystem() {
        return sapsystem;
    }

    public void setSapsystem(String sapsystem) {
        this.sapsystem = sapsystem;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public User(String userid) {
        this.userid = userid;
    }

    public User(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public User(String userid, String password, String newpassword) {
        this.userid = userid;
        this.password = password;
        this.newpassword = newpassword;
    }

    public User(String userid, String password, String newpassword, boolean success) {
        this.userid = userid;
        this.password = password;
        this.newpassword = newpassword;
        this.success = success;
    }

    public User(String userid, String password, String newpassword, boolean success, String message) {
        this.userid = userid;
        this.password = password;
        this.newpassword = newpassword;
        this.success = success;
        this.message = message;
    }

    public User(String userid, String password, String newpassword, boolean success, String message, String sapsystem) {
        this.userid = userid;
        this.password = password;
        this.newpassword = newpassword;
        this.success = success;
        this.message = message;
        this.sapsystem = sapsystem;
    }

//    public User() {
//    }
    
    

//    public ArrayList getMyList() {
//        return myList;
//    }
//
//    public void setMyList(ArrayList myList) {
//        this.myList = myList;
//    }
    private int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        //log("Generated : " + randomNumber);
        return randomNumber;
    }

    public String encrypt(String st1) {
        String encoded = "";
        char[] cast1 = st1.toCharArray();
        int l = st1.length() * 2;
        int encLen = l;
        if (l < 9) {
            encLen = 18;
        } else if (l % 16 != 0) {
            encLen = encLen + (l % 16) + 3;
        } else {
            encLen = encLen + 3;
        }
        //Random randomGenerator = new Random();
        //int randomInt = randomGenerator.nextInt();
        int START = 97;
        int END = 122;
        Random random = new Random();
        for (int idx = 1; idx <= encLen; ++idx) {
            int c = showRandomInteger(START, END, random);
            char ch = (char) c;
            encoded = encoded + ch;
        }
        //log("Done. " + "length = " + encoded.length() + " Model : " + encoded);
        char[] cast2 = cast1;
        int j = 1;
        int count = 0;
        for (char c : cast1) {
            if (j == 5) {
                j = 1;
            }
            int ci = (int) c;
            ci = ci + j;
            j++;
            cast2[count] = (char) ci;
            count++;
        }
        String encoded2 = "";
        for (char c : cast2) {
            encoded2 = encoded2 + c;
        }
        //log("encoded2 : " + encoded2);
        char[] cast3 = encoded.toCharArray();
        char[] cast4 = encoded2.toCharArray();
        int k = 1;
        for (int i = 0; i < cast4.length; i++) {
            cast3[k] = cast4[i];
            k = k + 2;
        }
        int lenth = st1.length() + 33;
        char lenthc = (char) (lenth);
        cast3[cast3.length - 2] = lenthc;
        String encoded3 = "";
        for (char c : cast3) {
            encoded3 = encoded3 + c;
        }
        //log("encoded3 : " + encoded3);
        return encoded3;
    }

    public String decrypt(String encoded3) {
        char[] enchar1 = encoded3.toCharArray();
        String realenst = "";
        int reallen = (int) enchar1[enchar1.length - 2] - 33;
        //log("" + reallen);
        int m = 1;
        for (int i = 0; i < reallen; i++) {
            realenst = realenst + enchar1[m];
            m = m + 2;
        }
        //log(realenst);
        char[] realenchar1 = realenst.toCharArray();
        int n = 1;
        int countn = 0;
        for (char c : realenchar1) {
            if (n == 5) {
                n = 1;
            }
            int ci = (int) c;
            ci = ci - n;
            n++;
            realenchar1[countn] = (char) ci;
            countn++;
        }
        String decoded = "";
        for (char c : realenchar1) {
            decoded = decoded + c;
        }
        //log("decoded : " + decoded);
        return decoded;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getxTID() {
        return xTID;
    }

    public void setxTID(String xTID) {
        this.xTID = xTID;
    }
    

}

