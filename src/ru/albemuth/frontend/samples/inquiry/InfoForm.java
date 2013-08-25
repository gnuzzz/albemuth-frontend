package ru.albemuth.frontend.samples.inquiry;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

public abstract class InfoForm extends Component<InquirySample>  {

    public InfoForm(String name, Component parent, InquirySample document) {
        super(name, parent, document);
    }

    public User getUser() {
        return (User)getRequestContext().getSessionContext().getParameterValue("user");
    }

    public Document logout() {
        getRequestContext().getSessionContext().setParameterValue("user", null);
        return null;
    }
    
}
