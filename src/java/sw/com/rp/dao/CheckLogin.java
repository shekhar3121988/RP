/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;
import sw.com.rp.connection.ADconnection;
import sw.com.rp.connection.ADserver;
import sw.com.rp.dto.User;
import sw.com.rp.connection.SAPConnection;

/**
 *
 * @author msaini
 */
public class CheckLogin {

    JCoFunction function;
    SAPConnection connection;
    static final Logger logger = LogManager.getLogger(CheckLogin.class.getName());

    public CheckLogin() {
        this.function = null;
        this.connection = null;
    }

    public User checkUserLogin(String userid, String password, String _system, HttpSession session) throws Throwable {
        logger.info("checkUserLogin() method start..");
        User user = new User();
        String language = "EN";
        String lang = (String) session.getAttribute("LANG");
        if (lang != null) {
            if (lang.equalsIgnoreCase("English")) {
                language = "EN";
            } else if (lang.equalsIgnoreCase("German")) {
                language = "DE";
            }
        }
        connection = new SAPConnection(language);
        try {

            connection.prepareConnection(RPConfig.getAppClientProperties().get(_system));

            function = connection.getFunction("SUSR_LOGIN_CHECK_RFC");

            JCoParameterList inputList = function.getImportParameterList();

            inputList.setValue("BNAME", userid);
            inputList.setValue("PASSWORD", password);

            connection.execute(function);

            connection.release();
            connection = null;
        } catch (Throwable e) {
            connection.release();
            connection = null;
            String s = e.toString().trim();
            logger.info("Message From SAP:" + e.toString());
            if (s.contains("WRONG_PASSWORD")) {
                //multilanguage code
                String text = getTranslatedText("CheckLoginmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Incorrect login credentials. Please try again.";
                }
                user.setMessage(text);
            } else if (s.contains("NO_CHECK_FOR_THIS_USER")) {
                //multilanguage code
                String text = getTranslatedText("CheckLoginmsg1", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Incorrect login credentials. Please try again.";
                }
                user.setMessage(text);
                logger.info("User ID not available in System");
            } else if (s.contains("USER_LOCKED")) {
                //multilanguage code
                String text = getTranslatedText("CheckLoginmsg3", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User is locked..Please contact your administrator";
                }
                user.setMessage(text);
                logger.info("User is locked..Please contact your administrator");
            } else if (s.contains("WAIT")) {
                //multilanguage code
                String text = getTranslatedText("CheckLoginmsg4", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Please Wait";
                }
                user.setMessage(text);
                logger.info("Please Wait");
            } else if (s.contains("USER_NOT_ACTIVE")) {
                //multilanguage code
                String text = getTranslatedText("CheckLoginmsg5", session);
                if (text == null || text.trim().length() == 0) {
                    text = "User is expired..Please contact your administrator";
                }
                user.setMessage(text);
                logger.info("User is expired..Please contact your administrator");
            } else if (s.contains("PASSWORD_EXPIRED")) {
                //multilanguage code
                String text = getTranslatedText("CheckLoginmsg6", session);
                if (text == null || text.trim().length() == 0) {
                    text = "Password needs reset. Please try in SAP System or contact your administrator";
                }
                user.setMessage(text);
                logger.info("Password needs reset. Please try in SAP System or contact your administrator");
            } else {
                user.setMessage(s);
                logger.error(s);

            }
            String text = getTranslatedText("CheckLoginmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "Incorrect login credentials. Please try again.";
            }
            user.setMessage(text);
            connection = null;
            function = null;
            return user;
        }
        user.setSuccess(true);
        //multilanguage code
        String text = getTranslatedText("CheckLoginmsg10", session);
        if (text == null || text.trim().length() == 0) {
            text = "Login Successful.";
        }
        user.setMessage(text);
        user.setMessage("Login Successful.");
        logger.info("checkUserLogin() method end..");
        return user;

    }

    public boolean validateUserWithAD(String UserID, HttpSession session) throws Throwable {
        boolean userFound = false;
        String lockoutTime = "";
        String userAccountControl = "";
        String accountExpires = "";
        DirContext ldapContext = null;
        for (ADserver server : RPConfig.getAdServerList()) {
            ADconnection conn = new ADconnection();
            try {
                logger.info("Connecting " + server.getDomain());
                ldapContext = conn.getDirContext(server, session);
                StringTokenizer token = new StringTokenizer(server.getDomain(), ".");
                String searchBase = "";
                while (token.hasMoreTokens()) {
                    searchBase = searchBase + "dc=" + token.nextToken() + ",";
                }
                searchBase = searchBase.substring(0, searchBase.length() - 1).trim();
                searchBase = "cn=users," + searchBase;
                String returnedAtts[] = {"cn", "sAMAccountName", "lockoutTime", "userAccountControl", "accountExpires"};
                String searchFilter = null;
                SearchControls searchCtrls = new SearchControls();
                searchCtrls.setReturningAttributes(returnedAtts);
                searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                searchFilter = "(&(objectClass=user)(sAMAccountName=" + UserID + "))";
                NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchCtrls);
                while (answer.hasMoreElements()) {
                    userFound = true;
                    logger.info("User " + UserID + " found in " + server.getDomain());
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attrs = sr.getAttributes();
                    if (attrs != null) {
                        NamingEnumeration ne = attrs.getAll();
                        while (ne.hasMore()) {
                            Attribute attr = (Attribute) ne.next();
                            if (attr.getID().equalsIgnoreCase("lockoutTime")) {
                                lockoutTime = attr.get().toString();
                            }
                            if (attr.getID().equalsIgnoreCase("userAccountControl")) {
                                userAccountControl = attr.get().toString();
                            }
                            if (attr.getID().equalsIgnoreCase("accountExpires")) {
                                accountExpires = attr.get().toString();
                            }
                        }
                        ne.close();
                    }
                    break;
                }
            } catch (Throwable ex) {
                ldapContext.close();
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                logger.error(stack.toString());
                stack = null;
                throw new Exception(ex.getMessage());

            }
            if (userFound) {
                ldapContext.close();
                break;
            }
            ldapContext.close();
            if (!userFound) {
                logger.info("User " + UserID + " not found in " + server.getDomain());
            }
        }
        logger.info(UserID + " user lockoutTime : " + lockoutTime);
        if (lockoutTime != null && lockoutTime.trim().length() > 1) {
            //multilanguage code
            String text = getTranslatedText("CheckLoginmsg7", session);
            if (text == null || text.trim().length() == 0) {
                text = "User is locked in Active Directory ";
            }
            throw new Exception(text);
        }
        if (userAccountControl != null && (userAccountControl.equalsIgnoreCase("66050") || userAccountControl.equalsIgnoreCase("514"))) {
            //multilanguage code
            String text = getTranslatedText("CheckLoginmsg8", session);
            if (text == null || text.trim().length() == 0) {
                text = "User account disabled in active directory.";
            }
            throw new Exception(text);
        }

        String ACCOUNT_NEVER_EXPIRE_VALUE = "9223372036854775807";
        boolean accountNeverExpire = accountExpires.equals("0") || ACCOUNT_NEVER_EXPIRE_VALUE.equals(accountExpires);
        boolean accountExpired = false;
        if (!accountNeverExpire) {
            Date accountExpiresDate = getDateFrom(accountExpires);
            logger.info("account Expires Date - " + accountExpiresDate.toString());

            Date currentDateTime = new Date();
            Date currentDate = truncTimeFrom(currentDateTime);
            accountExpired = accountExpiresDate.compareTo(currentDate) < 0;
            //int daysBeforeAccountExpiration = Integer.MAX_VALUE;
            // daysBeforeAccountExpiration = (int) TimeUnit.DAYS.convert(accountExpiresDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);
        } else {
            logger.info("account set to never expires in AD.");
        }
        if (accountExpired) {
            //multilanguage code
            String text = getTranslatedText("CheckLoginmsg9", session);
            if (text == null || text.trim().length() == 0) {
                text = "User account expired in active directory.";
            }
            throw new Exception(text);
        } else {
            logger.info("user Account Not Expired in AD.");
        }

        return userFound;
    }
    private final long DIFF_NET_JAVA_FOR_DATES = 11644473600000L + 24 * 60 * 60 * 1000;

    public Date getDateFrom(String adDateStr) {
        long adDate = Long.parseLong(adDateStr);
        long milliseconds = (adDate / 10000) - DIFF_NET_JAVA_FOR_DATES;
        Date date = new Date(milliseconds);
        return date;
    }

    public Date truncTimeFrom(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
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
                    text = RPConfig.getCheckLoginLangMap().get(selectedLang).get(textID);
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
