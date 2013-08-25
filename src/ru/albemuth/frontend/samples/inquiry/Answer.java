package ru.albemuth.frontend.samples.inquiry;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.10.2007
 * Time: 1:10:17
 */
public class Answer {

    private Question question;
    private String answer;
    private List<User2Answer> users;

    public Answer(Question question, String answer) {
        this.question = question;
        this.answer = answer;
        this.users = new ArrayList<User2Answer>();
    }

    public Question getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<User2Answer> getUsers() {
        return users;
    }

    public int getUserAnswersNumber() {
        return getUsers().size();
    }

}
