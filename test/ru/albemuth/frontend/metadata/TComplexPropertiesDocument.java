package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Document;

public abstract class TComplexPropertiesDocument extends Document {

    private boolean valid;
    private String valueItem;

    public TComplexPropertiesDocument(String name) {
        super(name);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getValue() {
        return "value";
    }

    public String getValue1() {
        return "value1";
    }

    public String getValue2() {
        return "value2";
    }

    public String getValueItem() {
        return valueItem;
    }

    public void setValueItem(String valueItem) {
        this.valueItem = valueItem;
    }

}
