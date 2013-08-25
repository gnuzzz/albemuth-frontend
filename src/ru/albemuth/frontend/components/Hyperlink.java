package ru.albemuth.frontend.components;

import ru.albemuth.frontend.context.ProcessContext;
import ru.albemuth.frontend.Engine;

public abstract class Hyperlink extends ActionComponent {

    public Hyperlink(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getResponse() {
        return Engine.ResponseType.FORWARD.name();
    }

    public String getHref() {
        return null;
    }

    public String getProcessid() {
        ProcessContext pc = getRequestContext().getProcessContextIfExists();
        if (pc != null) {
            return pc.getProcessid();
        } else {
            return null;
        }
    }

    public String getUrl() {
        String href = getHref();
        boolean hasQuery;
        boolean needAmp;
        if (href == null) {
            href = getRequestContext().getURI().getPath() + "?" + getName();
            hasQuery = true;
            needAmp = true;
            //href = getRequestContext().getHttpServletRequest().getContextPath() + getDocument().getPath() + "?" + getName();
        } else {
            hasQuery = href.indexOf('?') != -1;
            if (hasQuery) {
                char last = href.charAt(href.length() - 1);
                needAmp = last != '?' && last != '&';
            } else {
                needAmp = false;
            }
        }
        if (getItemid() != null) {
            href += (hasQuery ? (needAmp ? "&" : "") : "?") + Component.PARAMETER_NAME_ITEM_ID + "=" + getItemid();
            hasQuery = true;
            needAmp = true;
        }
        String processid = getProcessid();
        if (processid != null) {
            href += (hasQuery ? (needAmp ? "&" : "") : "?") + ProcessContext.PARAMETER_NAME_ID + "=" + processid;
            hasQuery = true;
            needAmp = true;
        }
        for (ActionComponent.Parameter p: getParameters()) {
            href += (hasQuery ? (needAmp ? "&" : "") : "?") + p.getParameterName() + "=" + p.getParameterValue();
            hasQuery = true;
            needAmp = true;
        }
        return href;
    }

}
