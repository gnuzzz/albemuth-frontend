package ru.albemuth.frontend.samples.inquiry;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.10.2007
 * Time: 1:09:43
 */
public class Inquiry {

    private String name;
    private List<Question> questions;

    public Inquiry(String name) {
        this.name = name;
        this.questions = new ArrayList<Question>();
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

}
