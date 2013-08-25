package ru.albemuth.frontend.samples.response;

import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.components.Document;

public abstract class Source extends Document {

    public Source(String name) {
        super(name);
    }

    public Document forwardResponse() throws EventException {
        try {
            Destination ret = (Destination)getRequestContext().getDocument("/ru/albemuth/frontend/samples/response/Destination");
            ret.setValue("forward");
            return ret;
        } catch (RequestException e) {
            throw new EventException("Can't obtain response document instance", e);
        }
    }

    public Document redirectResponse() throws EventException {
        try {
            Destination ret = (Destination)getRequestContext().getDocument("/ru/albemuth/frontend/samples/response/Destination");
            ret.setValue("redirect");
            return ret;
        } catch (RequestException e) {
            throw new EventException("Can't obtain response document instance", e);
        }
    }
}
