/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.rest;
// Sun base64 api is NA in Java > 8
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author msaini
 */
public class EncryptDecrypt {

    static final Logger logger = LogManager.getLogger(EncryptDecrypt.class.getName());
    private final String ALGO = "AES";
    private final byte[] keyValue = new byte[]{'h', 'O', 'T', 't', 'h', 'U', 'm', 'B', 's', 'U', 'p', 'C', 'o', 'L', 'D', 'y'};

    private Key generateKey() {
        Key key = null;
        try {
            key = new SecretKeySpec(keyValue, ALGO);
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        return key;
    }

    public String encrypt(String Data) throws Exception {
        String encryptedValue = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            //encryptedValue = new BASE64Encoder().encode(encVal);
            encryptedValue = Base64.encodeBase64String(encVal);

        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        return encryptedValue;
    }

    public String decrypt(String encryptedData) throws Exception {
//        if(!((encryptedData.length())%16 ==0 )){
//            return "Data passed is not in encrypted form";
//        }
        String decryptedValue = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            //byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decordedValue = Base64.decodeBase64(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            logger.error("decrypt(String encryptedData) error :  ");
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
        }
        return decryptedValue;
    }
    
     public String getHashedWebAdminPassword(String password) {

        try {
            String salt = "$2a$14$YODeoidufrtZK2zyx.qACu"; // 12 workload; upto 31 chars gen. using BCrypt.gensalt(i) method
            //String salt = BCrypt.gensalt(15);
            //System.out.println("salt"+salt);
		String hashed_password = BCrypt.hashpw(password, salt);

		return Hex.encodeHexString(hashed_password.getBytes());

        } catch (Exception e) {
            logger.error("getHashedWebAdminPassword() error : "+ e.toString());
            throw new RuntimeException(e);
        } 
    }
//    public void logMsg(String msg) {
//        try {
//            if (RPConfig.isLogging()) {
//                logger.info(msg);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//
//    public void logex(Exception ex) {
//        try {
//            if (RPConfig.isLogging()) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//    public void logTh(Throwable ex) {
//        try {
//            if (RPConfig.isLogging()) {
//                StringWriter stack = new StringWriter();
//                ex.printStackTrace(new PrintWriter(stack));
//                logger.error(stack.toString());
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
}
