package ru.albemuth.frontend.samples.inquiry;

import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.util.Accessor;

public abstract class LoginForm extends Component<Document> {

    private String userName = "";
    private String message;

    public LoginForm(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void releaseProperties() throws ReleaseException {
        userName = "";
        message = null;
    }

    public Document login() {
        InquiryController inquiryController = Accessor.getAccessor(InquiryController.class).getDefaultInstance();
        User user = inquiryController.getUser(getUserName());
        if (user != null) {
            getRequestContext().getSessionContext().setParameterValue("user", user);
        } else {
            setMessage("Пользователь с именем " + getUserName() + " не найден");
        }
        return null;
    }

    public Document register() {
        InquiryController inquiryController = Accessor.getAccessor(InquiryController.class).getDefaultInstance();
        User user = inquiryController.createUser(getUserName());
        if (user != null) {
            getRequestContext().getSessionContext().setParameterValue("user", user);
        } else {
            setMessage("Пользователь с именем " + getUserName() + " уже существует");
        }
        return null;
    }
}
