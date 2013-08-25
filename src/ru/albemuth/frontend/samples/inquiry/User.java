package ru.albemuth.frontend.samples.inquiry;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.10.2007
 * Time: 1:18:21
 */
public class User {

    private String name;
    private List<User2Answer> answers;

    public User(String name) {
        this.name = name;
        this.answers = new ArrayList<User2Answer>();
    }

    public String getName() {
        return name;
    }

    public List<User2Answer> getAnswers() {
        return answers;
    }
    
}
