/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.connection;

import com.sw.util.SecCrypt;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Security;
import javax.servlet.http.HttpSession;
import sw.com.rp.config.RPConfig;

/**
 *
 * @author swadmin
 */
public class ADconnection {

    static final Logger logger = LogManager.getLogger(ADconnection.class.getName());

    public DirContext getDirContext(ADserver server, HttpSession session) throws Throwable {
        DirContext ldapContext = null;
        Hashtable ldapEnv = new Hashtable(11);
        if (server.getPort().equalsIgnoreCase("636") || server.getPort().equalsIgnoreCase("3269")) {
            logger.info("Using Port : " + server.getPort());
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            System.out.println("path" + RPConfig.getRP_home() + "/store.info");
            System.setProperty("javax.net.ssl.trustStore", RPConfig.getRP_home() + "/store.info");
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL, "ldap://" + server.getIp() + ":" + server.getPort());
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

            //ldapEnv.put(Context.SECURITY_PRINCIPAL, server.getUserid() + "@" + server.getDomain());
            ldapEnv.put(Context.SECURITY_PRINCIPAL,
                    SecCrypt.getDecryptedClearText(server.getIp() + SecCrypt.KEY_PADDING + SapSystem.UID,
                            RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME)
                    + "@" + server.getDomain());

            //ldapEnv.put(Context.SECURITY_CREDENTIALS, server.getPassword());
            ldapEnv.put(Context.SECURITY_CREDENTIALS,
                    SecCrypt.getDecryptedClearText(server.getIp() + SecCrypt.KEY_PADDING + SapSystem.PWD,
                            RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME));

            ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
        } else if (server.getPort().equalsIgnoreCase("389") || server.getPort().equalsIgnoreCase("3268")) {
            logger.info("Using Port : " + server.getPort());
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL, "ldap://" + server.getIp() + ":" + server.getPort());
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

            //ldapEnv.put(Context.SECURITY_PRINCIPAL, server.getUserid() + "@" + server.getDomain());
            ldapEnv.put(Context.SECURITY_PRINCIPAL,
                    SecCrypt.getDecryptedClearText(server.getIp() + SecCrypt.KEY_PADDING + SapSystem.UID,
                            RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME)
                    + "@" + server.getDomain());
            
            //ldapEnv.put(Context.SECURITY_CREDENTIALS, server.getPassword());
            ldapEnv.put(Context.SECURITY_CREDENTIALS,
                    SecCrypt.getDecryptedClearText(server.getIp() + SecCrypt.KEY_PADDING + SapSystem.PWD,
                            RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME));

        } else {
            logger.info("Using Custom Port : " + server.getPort());
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL, "ldap://" + server.getIp() + ":" + server.getPort());
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

            //ldapEnv.put(Context.SECURITY_PRINCIPAL, server.getUserid() + "@" + server.getDomain());
            ldapEnv.put(Context.SECURITY_PRINCIPAL,
                    SecCrypt.getDecryptedClearText(server.getIp() + SecCrypt.KEY_PADDING + SapSystem.UID,
                            RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME)
                    + "@" + server.getDomain());

            //ldapEnv.put(Context.SECURITY_CREDENTIALS, server.getPassword());
            ldapEnv.put(Context.SECURITY_CREDENTIALS,
                    SecCrypt.getDecryptedClearText(server.getIp() + SecCrypt.KEY_PADDING + SapSystem.PWD,
                            RPConfig.getRP_home() + RPConfig.JKS_FILE_NAME));

        }
        try {
            ldapContext = new InitialDirContext(ldapEnv);
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(stack.toString());
            stack = null;
            String str = e.getMessage();

            String str1 = str.replace("\"", "");
            System.out.println("AD Server " + str1 + ": connection is unsuccessful.");
            // /multilanguage code
            String text = getTranslatedText("Systemsmsg14", session);
            if (text == null || text.trim().length() == 0) {
                text = ": connection is unsuccessful.";
            }
            throw new Exception("AD Server :" + str1 + " " + text);

        }
        return ldapContext;

    }

    public String getTranslatedText(String textID, HttpSession session) {
        logger.info(" getTranslatedText() method starts..");
        if (textID != null && textID != "") {
            String selectedLang = "";
            try {
                selectedLang = session.getAttribute("LANG").toString();
            } catch (Exception e) {
                selectedLang = "English";
            }

            logger.info("selected language" + selectedLang);
            if (selectedLang != null && selectedLang.length() > 0) {
                String text = "";
                try {
                    text = RPConfig.getSystemResourcesLangMap().get(selectedLang).get(textID);
                } catch (Exception e) {
                    e.toString();
                    logger.error(e.toString());
                    logger.info("SupportPage Error in translation  for " + textID);
                    return null;
                }
                if (text != null && text.trim().length() > 0) {
                    logger.info(" getTranslatedText() method ends..");
                    return text;
                }
            } else {
                logger.info("SupportPage Error in translation  for " + textID);
                return null;
            }
        }
        logger.info("SupportPage Error in translation  for " + textID);
        return null;
    }
}
