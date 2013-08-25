package ru.albemuth.frontend.components;

import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.RenderException;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.context.ApplicationContext;
import ru.albemuth.frontend.context.ProcessContext;
import ru.albemuth.frontend.context.RequestContext;
import ru.albemuth.frontend.context.SessionContext;

public abstract class Component<D extends Document> {

    public static final String PARAMETER_NAME_ITEM_ID               = "itemid";
    public static final Component[] EMPTY_ARRAY                     = {};

    protected String name;
    protected Component parent;
    protected D document;
    protected Component[] children = Component.EMPTY_ARRAY;

    protected Component(String name, Component parent, D document) {
        this.name = name;
        this.parent = parent;
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public Component getParent() {
        return parent;
    }

    public D getDocument() {
        return document;
    }

    public Component[] getChildren() {
        return children;
    }

    public void setChildren(Component[] children) {
        this.children = children;
    }

    public RequestContext getRequestContext() {
        return getDocument().getRequestContext();
    }

    public ProcessContext getProcessContext() {
        return getRequestContext().getProcessContext();
    }

    public SessionContext getSessionContext() {
        return getRequestContext().getSessionContext();
    }

    public ApplicationContext getApplicationContext() {
        return getRequestContext().getApplicationContext();
    }

    public void setContextProperties() throws RequestException {
        setContextPropertiesValues();
        setChildrenContextProperties();
    }

    protected void setChildrenContextProperties() throws RequestException {
        for (Component child: getChildren()) {
            child.setContextProperties();
        }
    }

    protected void setContextPropertiesValues() throws RequestException {}

    public void processEvents() throws EventException {
        processChildrenEvents();
    }

    protected void processChildrenEvents() throws EventException {
        for (Component child: getChildren()) {
            child.processEvents();
            if (getRequestContext().getResponseDocument() != null) {
                break;
            }
        }
    }

    public void render() throws RenderException {
        /*for (Component child: getChildren()) {
            child.render(itemid);
        }*/
        renderContent();
    }

    protected abstract void renderContent() throws RenderException;

    public void release() throws ReleaseException {
        releaseProperties();
        releaseChildren();
    }

    protected void releaseChildren() throws ReleaseException {
        for (Component child: getChildren()) {
            child.release();
        }
    }

    protected void releaseProperties() throws ReleaseException {}

    public String getItemid() {
        return getRequestContext().getItemid();
    }

}
