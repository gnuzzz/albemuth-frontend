package ru.albemuth.frontend.components;

public abstract class TextArea extends Component<Document> {

    public TextArea(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getStringValue() {
        String value = getValue();
        return value == null ? "" : value;
    }

    public String getValue() {
        return "";
    }

    public abstract void setValue(String value);

    public String getFullName() {
        String name = getName();
        if (getItemid() != null) {
            name += "." + getItemid();
        }
        return name;
    }

    public boolean isReadOnly() {
        return false;
    }

    public String getReadOnlyString() {
        return isReadOnly() ? "readonly" : "";
    }

    public boolean isDisabled() {
        return false;
    }

    public String getDisabledString() {
        return isDisabled() ? "disabled" : "";
    }

    public void setContextProperties() {
        if (getRequestContext().isInSourceEventForm()) {
            String value = getRequestContext().getParameterValue(getFullName());
            if (value != null) {
                setValue(value);
            }
        }
    }

}
