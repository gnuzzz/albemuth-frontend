package ru.albemuth.frontend.samples.inquiry;

import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.context.SessionContext;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.util.Accessor;

public abstract class InquirySample extends Document {

    private InquiryController inquiryController = Accessor.getAccessor(InquiryController.class).getDefaultInstance();

    public InquirySample(String name) {
        super(name);
    }

    public Inquiry getInquiry() {
        return inquiryController.getInquiry("sample");
    }

    public boolean isLogged() {
        SessionContext sessionContext = getRequestContext().getSessionContextIfExists();
        return sessionContext != null && sessionContext.getParameterValue("user") != null;
    }

}
