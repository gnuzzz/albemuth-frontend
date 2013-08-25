package ru.albemuth.frontend.samples.inquiry;

import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.Engine;
import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.context.SessionContext;
import ru.albemuth.util.Accessor;

import java.util.*;

public abstract class InquiryView extends Component<InquirySample> {

    private InquiryController inquiryController = Accessor.getAccessor(InquiryController.class).getDefaultInstance();
    private String showResults;
    private boolean answersReceived;
    private Question questionItem;
    private Answer answerItem;
    private Map<Question, List<Answer>> userAnswers = new HashMap<Question, List<Answer>>();

    public InquiryView(String name, Component parent, InquirySample document) {
        super(name, parent, document);
    }

    public abstract Inquiry getInquiry();

    public Question getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(Question questionItem) {
        this.questionItem = questionItem;
    }

    public Answer getAnswerItem() {
        return answerItem;
    }

    public void setAnswerItem(Answer answerItem) {
        this.answerItem = answerItem;
    }

    public boolean isLogged() {
        SessionContext sessionContext = getRequestContext().getSessionContextIfExists();
        return sessionContext != null && sessionContext.getParameterValue("user") != null;
    }

    public boolean isAnswered() {
        SessionContext sessionContext = getRequestContext().getSessionContextIfExists();
        return sessionContext != null && inquiryController.isUserAnswered((User) sessionContext.getParameterValue("user"), getInquiry());
    }

    public boolean isShowResults() {
        return !isLogged() || Engine.Mode.SET_CONTEXT_PROPERTIES.equals(getRequestContext().getMode()) && !answersReceived || Engine.Mode.RENDER.equals(getRequestContext().getMode()) && (showResults == null ? isAnswered() : "on".equals(showResults));
    }

    public boolean isSelect() {
        return getQuestionItem().getAnswers().size() > 2;
    }

    public List<Answer> getUserAnswers() {
        return userAnswers.get(getQuestionItem());
    }

    public void setUserAnswers(List<Answer> userAnswers) {
        List<Answer> answers = getUserAnswers();
        answers.clear();
        answers.addAll(userAnswers);
    }

    public Answer getUserAnswer() {
        return getUserAnswers().size() > 0 ? getUserAnswers().get(0) : null;
    }

    public void setUserAnswer(Answer userAnswer) {
        List<Answer> answers = getUserAnswers();
        answers.clear();
        if (userAnswer != null) answers.add(userAnswer);
    }

    public boolean isAnswerSelected() {
        return getUserAnswers().contains(getAnswerItem());
    }

    public void setAnswerSelected(boolean answerSelected) {
        if (answerSelected) {
            getUserAnswers().add(getAnswerItem());
        } else {
            getUserAnswers().remove(getAnswerItem());
        }
    }

    public double getUserAnswersNumberInPercent() {
        if (getAnswerItem().getUserAnswersNumber() == 0) {
            return 0;
        } else {
            return 100 * getAnswerItem().getUserAnswersNumber() / (double)getQuestionItem().getUserAnswersNumber();
        }
    }

    public String getAnswerItemWidth() {
        return (int)getUserAnswersNumberInPercent() + "%";
    }

    protected void setContextPropertiesValues() throws RequestException {
        User user = null;
        SessionContext sessionContext = getRequestContext().getSessionContextIfExists();
        if (sessionContext != null) {
            user = (User)sessionContext.getParameterValue("user");
        }
        for (Question question: getInquiry().getQuestions()) {
            this.userAnswers.put(question, new ArrayList<Answer>());
        }
        if (user != null) {
            for (User2Answer u2a: user.getAnswers()) {
                Answer answer = u2a.getAnswer();
                if (answer.getQuestion().getInquiry().equals(getInquiry())) {
                    this.userAnswers.get(answer.getQuestion()).add(answer);
                }
            }
        }
        this.showResults = getRequestContext().getParameterValue("showResults");
        this.answersReceived = getRequestContext().getParameterValue("inquiryForm") != null;
    }

    public void releaseProperties() throws ReleaseException {
        this.userAnswers.clear();
        this.showResults = null;
        this.answersReceived = false;
    }

    public Document setUserAnswers() {
        SessionContext sessionContext = getRequestContext().getSessionContextIfExists();
        if (sessionContext != null) {
            User user = (User)sessionContext.getParameterValue("user");
            if (user != null) {
                for (Iterator<User2Answer> it = user.getAnswers().iterator(); it.hasNext(); ) {
                    User2Answer u2a = it.next();
                    if (u2a.getAnswer().getQuestion().getInquiry().equals(getInquiry())) {
                        it.remove();
                        u2a.getAnswer().getUsers().remove(u2a);
                    }
                }
                for (Question question: getInquiry().getQuestions()) {
                    List<Answer> answers = userAnswers.get(question);
                    for (Answer answer: answers) {
                        User2Answer u2a = new User2Answer(user, answer, new Date());
                        user.getAnswers().add(u2a);
                        answer.getUsers().add(u2a);
                    }
                }
            }
        }
        return null;
    }

}
