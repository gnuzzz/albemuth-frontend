package ru.albemuth.frontend.components;

import ru.albemuth.frontend.context.RequestContext;
import ru.albemuth.frontend.*;

public abstract class Document extends Component<Document> {

    private RequestContext requestContext;
    private String path;

    public Document(String name) {
        super(name, null, null);
    }

    public Document getDocument() {
        return this;
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public Engine.ResponseType getResponseType() {
        return null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setContextProperties() throws RequestException {
        getRequestContext().setMode(Engine.Mode.SET_CONTEXT_PROPERTIES);
        super.setContextProperties();
    }

    public void processEvents() throws EventException {
        getRequestContext().setMode(Engine.Mode.PROCESS_EVENTS);
        super.processEvents();
        if (getRequestContext().getResponseDocument() == null) {
            getRequestContext().setResponseDocument(this, Engine.ResponseType.FORWARD);
        }
    }

    public String getContentType() {
        return "text/html";
    }

    public void render() throws RenderException {
        getRequestContext().setMode(Engine.Mode.RENDER);
        super.render();
    }

    public void release() throws ReleaseException {
        getRequestContext().setMode(Engine.Mode.RELEASE);
        super.release();
    }

}
