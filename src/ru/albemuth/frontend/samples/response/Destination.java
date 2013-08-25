package ru.albemuth.frontend.samples.response;

import ru.albemuth.frontend.components.Document;

public abstract class Destination extends Document {

    public static String value;

    public Destination(String name) {
        super(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        Destination.value = value;
    }
    
}
