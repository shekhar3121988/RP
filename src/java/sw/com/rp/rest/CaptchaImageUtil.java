/**
 * @author : Security Weaver, LLC
 * ----------------------------------------------------------------------*
 * COPYRIGHTS Security Weaver, LLC
 *
 * WARNING: THIS COMPUTER PROGRAM IS PROTECTED BY COPYRIGHT LAW AND
 * INTERNATIONAL TREATIES. UNAUTHORIZED REPRODUCTION OR DISTRIBUTION IS STRICTLY
 * PROHIBITED AND MAY RESULT IN SEVERE CIVIL AND CRIMINAL PENALTIES AND WILL BE
 * PROSECUTED TO THE MAXIMUM EXTENT POSSIBLE UNDER THE LAW.
 * ----------------------------------------------------------------------
 */
package sw.com.rp.rest;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.net.URLDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CaptchaImageUtil {

    static final Logger LOGGER = LogManager.getLogger(CaptchaImageUtil.class.getName());

    String captchaString = "";

    /**
     *
     * Function to generate random Captcha image and returns the BufferedImage
     *
     * @return BufferedImage
     */
    public BufferedImage getCaptchaImage() {
        try {
            Color backgroundColor = Color.white;
            Color borderColor = new Color(190, 191, 188);
            Color textColor = Color.black;
            Color circleColor = new Color(190, 160, 150);

            String fullPath = URLDecoder.decode(this.getClass().getClassLoader().getResource("").getPath(), "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");

            fullPath = pathArr[0];
            if (Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()).contains("Avara")) {

            } else {

                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fullPath + "/images/Avara.ttf")));
            }

            Font textFont = new Font("Avara", Font.BOLD, 24);

            int charsToPrint = 6;
            int width = 160;
            int height = 50;
            int circlesToDraw = 25;
            float horizMargin = 10.0f;
            double rotationRange = 0.7;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(backgroundColor);
            g.fillRect(0, 0, width, height);

            // lets make some noisey circles
            g.setColor(circleColor);
            for (int i = 0; i < circlesToDraw; i++) {
                int L = (int) (Math.random() * height / 2.0);
                int X = (int) (Math.random() * width - L);
                int Y = (int) (Math.random() * height - L);
                g.draw3DRect(X, Y, L * 2, L * 2, true);
            }
            g.setColor(textColor);
            g.setFont(textFont);
            FontMetrics fontMetrics = g.getFontMetrics();
            int maxAdvance = fontMetrics.getMaxAdvance();
            int fontHeight = fontMetrics.getHeight();

            // i removed 1 and l and i because there are confusing to users...
            // Z, z, and N also get confusing when rotated
            // this should ideally be done for every language...
            // 0, O and o removed because there are confusing to users...
            // i like controlling the characters though because it helps prevent confusion
            String elegibleChars = "ABDEFGHKLMNPQRTUVWXYabdefghkmnqrtuvwxy23456789";
            char[] chars = elegibleChars.toCharArray();
            float spaceForLetters = -horizMargin * 2 + width;
            float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);
            StringBuffer finalString = new StringBuffer();
            for (int i = 0; i < charsToPrint; i++) {
                double randomValue = Math.random();
                int randomIndex = (int) Math.round(randomValue * (chars.length - 1));
                char characterToShow = chars[randomIndex];
                finalString.append(characterToShow);

                // this is a separate canvas used for the character so that
                // we can rotate it independently
                int charWidth = fontMetrics.charWidth(characterToShow);
                int charDim = Math.max(maxAdvance, fontHeight);
                int halfCharDim = (int) (charDim / 2);
                BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
                Graphics2D charGraphics = charImage.createGraphics();
                charGraphics.translate(halfCharDim, halfCharDim);
                double angle = (Math.random() - 0.5) * rotationRange;
                charGraphics.transform(AffineTransform.getRotateInstance(angle));
                charGraphics.translate(-halfCharDim, -halfCharDim);
                charGraphics.setColor(textColor);
                charGraphics.setFont(textFont);
                int charX = (int) (0.5 * charDim - 0.5 * charWidth);
                charGraphics.drawString("" + characterToShow, charX, (int) ((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));
                float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
                int y = (int) ((height - charDim) / 2);
                g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);
                charGraphics.dispose();
            }
            g.setColor(borderColor);
            g.drawRect(0, 0, width - 1, height - 1);
            g.dispose();
            captchaString = finalString.toString();

            return bufferedImage;
        } catch (FontFormatException | IOException ex) {
            LOGGER.error("Error while decrypting :  ");
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            LOGGER.error(stack.toString());
            stack = null;
            throw new RuntimeException("Unable to build captcha image - ", ex);
        }
    }

    // Function to return the Captcha string
    public String getCaptchaString() {
        return captchaString;
    }
}
