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
public class POJO_QuesAns implements Serializable {
    private String questionID;
    private String question;
    private String answers;

    public POJO_QuesAns() {
         this.questionID = "";
         this.question = "";
        this.answers = "";
    }

    public POJO_QuesAns(String question, String answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    @Override
    public String toString() {
        return "answer-"+answers;

    }
    


}
