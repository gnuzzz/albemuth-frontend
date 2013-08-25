package ru.albemuth.frontend.components;

import ru.albemuth.frontend.Engine;

public abstract class DirectAction extends ActionComponent {

    public DirectAction(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getResponse() {
        return Engine.ResponseType.FORWARD.name();
    }

    public void render() {}

    public void release() {}
    
}
