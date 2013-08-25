package ru.albemuth.frontend.samples.inquiry;

import ru.albemuth.util.*;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class InquiryController implements Configured, Closed {

    private Map<String, User> users;
    private Map<String, Inquiry> inquiryMap;

    public void configure(Configuration configuration) throws ConfigurationException {
        this.users = new ConcurrentHashMap<String, User>();
        this.inquiryMap = new HashMap<String, Inquiry>();
        this.inquiryMap.put("sample", createInquiry());
        Accessor.getAccessor(InquiryController.class).setDefaultInstance(this);
    }

    public void close() throws CloseException {}

    public Inquiry getInquiry(String id) {
        return inquiryMap.get(id);
    }

    public User getUser(String name) {
        return users.get(name);
    }

    public User createUser(String name) {
        User user = null;
        if (users.get(name) == null) {
            user = new User(name);
            users.put(name, user);
        }
        return user;
    }

    public boolean isUserAnswered(User user, Inquiry inquiry) {
        for (User2Answer u2a: user.getAnswers()) {
            if (u2a.getAnswer().getQuestion().getInquiry().equals(inquiry)) {
                return true;
            }
        }
        return false;
    }

    protected Inquiry createInquiry() {
        Inquiry inquiry = new Inquiry("Ктулху ф'хтагн?");
        Question question1 = new Question(inquiry, "Укажите ЖЖЖ", false);
        question1.getAnswers().add(new Answer(question1, "АА"));
        question1.getAnswers().add(new Answer(question1, "ББ"));
        question1.getAnswers().add(new Answer(question1, "ВВ"));
        inquiry.getQuestions().add(question1);
        Question question2 = new Question(inquiry, "Укажите 000", true);
        question2.getAnswers().add(new Answer(question2, "1111"));
        question2.getAnswers().add(new Answer(question2, "2222"));
        question2.getAnswers().add(new Answer(question2, "3333"));
        question2.getAnswers().add(new Answer(question2, "4444"));
        inquiry.getQuestions().add(question2);
        Question question3 = new Question(inquiry, "Укажите GGG", false);
        question3.getAnswers().add(new Answer(question3, "X"));
        question3.getAnswers().add(new Answer(question3, "Y"));
        inquiry.getQuestions().add(question3);
        Question question4 = new Question(inquiry, "Укажите ###", true);
        question4.getAnswers().add(new Answer(question4, "@"));
        question4.getAnswers().add(new Answer(question4, "$"));
        inquiry.getQuestions().add(question4);
        return inquiry;
    }

}
