package ru.albemuth.frontend.components;

public abstract class HiddenField extends Component<Document> {

    public HiddenField(String name, Component parent, Document document) {
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

    public void setContextProperties() {
        if (getRequestContext().isInSourceEventForm()) {
            String value = getRequestContext().getParameterValue(getFullName());
            if (value != null) {
                setValue(value);
            }
        }
    }

}
