package ru.albemuth.frontend.components;

import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.context.ProcessContext;

public abstract class Form extends ActionComponent {

    public Form(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public void setContextProperties() throws RequestException {
        if (isSource()) {
            getRequestContext().setEventSource(this);
            getRequestContext().setInSourceEventForm(true);
        }
        setChildrenContextProperties();
        getRequestContext().setInSourceEventForm(false);
    }

    public void processEvents() throws EventException {
        if (isSource()) {
            getRequestContext().setInSourceEventForm(true);
            processChildrenEvents();
            if (getRequestContext().getResponseDocument() == null) {
                Document ret = processAction();
                if (ret == null) {
                    ret = getDocument();
                }
                getRequestContext().setResponseDocument(ret, getResponseType());
            }
        } else {
            processChildrenEvents();
        }
        getRequestContext().setInSourceEventForm(false);
    }

    public String getAction() {
        return null;
    }

    public String getMethod() {
        return "post";
    }

    public String getUrl() {
        String action = getAction();
        if (action == null) {
            action = getRequestContext().getHttpServletRequest().getContextPath() + getDocument().getPath();
            //action = getRequestContext().getURI().getPath();
        }
        return action;
    }

    public String getProcessid() {
        ProcessContext pc = getRequestContext().getProcessContextIfExists();
        if (pc != null) {
            return pc.getProcessid();
        } else {
            return null;
        }
    }

    public String getFormValues() {
        String ret = "<input type=\"hidden\" name=\"" + getName() + "\" value=\"\" />";
        if (getItemid() != null) {
            ret += "<input type=\"hidden\" name=\"" + Component.PARAMETER_NAME_ITEM_ID + "\" value=\"" + getItemid() + "\" />";
        }
        String processid = getProcessid();
        if (processid != null) {
            ret += "<input type=\"hidden\" name=\"" + ProcessContext.PARAMETER_NAME_ID + "\" value=\"" + processid + "\" />";
        }
        for (ActionComponent.Parameter p: getParameters()) {
            ret += "<input type=\"hidden\" name=\"" + p.getParameterName() + "\" value=\"" + p.getParameterValue() + "\" />";
        }
        return ret;
    }

}
