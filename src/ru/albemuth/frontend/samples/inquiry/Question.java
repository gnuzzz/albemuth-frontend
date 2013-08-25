package ru.albemuth.frontend.samples.inquiry;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.10.2007
 * Time: 1:10:05
 */
public class Question {

    private Inquiry inquiry;
    private String question;
    private boolean multiple;
    private List<Answer> answers;

    public Question(Inquiry inquiry, String question, boolean multiple) {
        this.inquiry = inquiry;
        this.question = question;
        this.multiple = multiple;
        this.answers = new ArrayList<Answer>();
    }

    public Inquiry getInquiry() {
        return inquiry;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public int getUserAnswersNumber() {
        int userAnswersNumber = 0;
        for (Answer answer: getAnswers()) {
            userAnswersNumber += answer.getUserAnswersNumber();
        }
        return userAnswersNumber;
    }

}
