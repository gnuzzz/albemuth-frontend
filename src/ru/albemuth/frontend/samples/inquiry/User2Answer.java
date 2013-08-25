package ru.albemuth.frontend.samples.inquiry;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.10.2007
 * Time: 1:19:52
 */
public class User2Answer {

    private User user;
    private Answer answer;
    private Date answerDate;

    public User2Answer(User user, Answer answer, Date answerDate) {
        this.user = user;
        this.answer = answer;
        this.answerDate = answerDate;
    }

    public User getUser() {
        return user;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Date getAnswerDate() {
        return answerDate;
    }

}
