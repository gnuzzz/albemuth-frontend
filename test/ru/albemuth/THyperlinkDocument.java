package ru.albemuth;

import ru.albemuth.frontend.components.Document;

public abstract class THyperlinkDocument extends Document {

    private boolean value;

    public THyperlinkDocument(String name) {
        super(name);
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Document triggerValue() {
        value = !value;
        return null;
    }

}
