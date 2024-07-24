/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dao;

import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.com.rp.config.RPConfig;

/**
 *
 * @author msaini
 */
public class ValidatePassword {

    static final Logger logger = LogManager.getLogger(ValidatePassword.class.getName());
    private final Pattern hasUppercase = Pattern.compile("\\p{javaUpperCase}");
    private final Pattern hasLowercase = Pattern.compile("\\p{javaLowerCase}");
    private final Pattern hasNumber = Pattern.compile("\\p{javaDigit}");
    private final Pattern hasSpecialChar = Pattern.compile("[^\\p{javaLetterOrDigit} ]");

    public String validateNewPass(String pass1, HttpSession session) {
        if (pass1 == null) {
            logger.info("Passwords = null");
            // /multilanguage code
            String text = getTranslatedText("ValidatePasswordmsg1", session);
            if (text == null || text.trim().length() == 0) {
                text = "Password can't be null";
            }
            return text;
        }

        StringBuilder retVal = new StringBuilder();

        if (pass1.isEmpty()) {
            // /multilanguage code
            String text = getTranslatedText("ValidatePasswordmsg2", session);
            if (text == null || text.trim().length() == 0) {
                text = "Empty fields";
            }
            retVal.append(text + " \n");
        }



        if (pass1.length() < 8) {
            logger.info(pass1 + " is length < 8");
            // /multilanguage code
            String text = getTranslatedText("ValidatePasswordmsg3", session);
            if (text == null || text.trim().length() == 0) {
                text = "Password is too short. Needs to have 8 characters";
            }
            retVal.append(text + " \n");
        }

        if (!hasUppercase.matcher(pass1).find()) {
            logger.info(pass1 + " <-- needs uppercase");
            // /multilanguage code
            String text = getTranslatedText("ValidatePasswordmsg4", session);
            if (text == null || text.trim().length() == 0) {
                text = "Password needs an upper case";
            }
            retVal.append(text + " \n");
        }

        if (!hasLowercase.matcher(pass1).find()) {
            logger.info(pass1 + " <-- needs lowercase");
            // /multilanguage code
            String text = getTranslatedText("ValidatePasswordmsg5", session);
            if (text == null || text.trim().length() == 0) {
                text = "Password needs a lowercase";
            }
            retVal.append(text + " \n");
        }

        if (!hasNumber.matcher(pass1).find()) {
            logger.info(pass1 + "<-- needs a number");
            // /multilanguage code
            String text = getTranslatedText("ValidatePasswordmsg6", session);
            if (text == null || text.trim().length() == 0) {
                text = "Password needs a number";
            }
            retVal.append(text + " \n");
        }

        if (!hasSpecialChar.matcher(pass1).find()) {
            logger.info(pass1 + "<-- needs a specail character");
            // /multilanguage code
            String text = getTranslatedText("ValidatePasswordmsg7", session);
            if (text == null || text.trim().length() == 0) {
                text = "Password needs a special character i.e. !,@,#, etc.";
            }
            retVal.append(text + " \n");
        }

        if (retVal.length() == 0) {
            logger.info("Password validates");
            retVal.append("Success");
        }

        return retVal.toString();
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
                    text = RPConfig.getValidatePasswordLangMap().get(selectedLang).get(textID);
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
