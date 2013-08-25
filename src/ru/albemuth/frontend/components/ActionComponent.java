package ru.albemuth.frontend.components;

import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.Engine;
import ru.albemuth.frontend.context.RequestContext;

public abstract class ActionComponent extends Component<Document> {

    public ActionComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getResponse() {
        return Engine.ResponseType.REDIRECT.name();
    }

    public Engine.ResponseType getResponseType() {
        return getResponse() != null ? Engine.ResponseType.valueOf(getResponse()) : null;
    }

    public void setContextProperties() throws RequestException {
        if (isSource()) {
            getRequestContext().setEventSource(this);
        }
    }

    public void processEvents() throws EventException {
        if (isSource()) {
            Document ret = processAction();
            if (ret == null) {
                ret = getDocument();
            }
            getRequestContext().setResponseDocument(ret, ret.getResponseType() == null ? getResponseType() : ret.getResponseType());
        } else {
            processChildrenEvents();
        }
    }

    protected abstract Document processAction() throws EventException;

    protected boolean isSource() {
        RequestContext rc = getRequestContext();
        if (rc.getParameterValue(getName()) != null) {
            if (getItemid() != null) {
                return getItemid().equals(rc.getParameterValue(Component.PARAMETER_NAME_ITEM_ID));
            } else {
                return rc.getParameterValue(Component.PARAMETER_NAME_ITEM_ID) == null;
            }
        }
        return false;
    }

    public ActionComponent.Parameter[] getParameters() {
        return ActionComponent.Parameter.EMPTY_ARRAY;
    }

    public static interface Parameter {

        public static final Parameter[] EMPTY_ARRAY = new Parameter[0];

        public String getParameterName();

        public String getParameterValue();

    }

}
