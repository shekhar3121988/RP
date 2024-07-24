/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sw.com.rp.dto;

import java.io.Serializable;

/**
 *
 * @author msaini
 */
public class POJO_QuesAnsSubmitUpdate implements Serializable {

    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String passSync;
    private String userIDOther;
    private String charUrl;

    public POJO_QuesAnsSubmitUpdate() {
        this.question1 = "";
        this.question2 = "";
        this.question3 = "";
        this.question4 = "";
        this.question5 = "";
        this.answer1 = "";
        this.answer2 = "";
        this.answer3 = "";
        this.answer4 = "";
        this.answer5 = "";
        this.passSync = "";
        this.userIDOther = "";
        this.charUrl = "";
    }

    public POJO_QuesAnsSubmitUpdate(String question1, String question2, String question3, String question4, String question5, String answer1, String answer2, String answer3, String answer4, String answer5) {
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.question4 = question4;
        this.question5 = question5;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
    }

    public String getPassSync() {
        return passSync;
    }

    public void setPassSync(String passSync) {
        this.passSync = passSync;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    public String getUserIDOther() {
        return userIDOther;
    }

    public void setUserIDOther(String userIDOther) {
        this.userIDOther = userIDOther;
    }

    public String getCharUrl() {
        return charUrl;
    }

    public void setCharUrl(String charUrl) {
        this.charUrl = charUrl;
    }
    
}
