package ru.albemuth.frontend.components;

import ru.albemuth.frontend.RenderException;
import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.Engine;
import ru.albemuth.frontend.context.RequestContext;

public class ValueDocument extends Document {

    private String content;

    public ValueDocument(RequestContext requestContext, String content) {
        super("content");
        setRequestContext(requestContext);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    protected void renderContent() throws RenderException {
        //do nothing
    }

    public void render() throws RenderException {
        getRequestContext().getResponse().append(content);
    }

    public void release() throws ReleaseException {
        getRequestContext().setResponseDocument(null, Engine.ResponseType.FORWARD);
    }

}
