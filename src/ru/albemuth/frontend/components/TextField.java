package ru.albemuth.frontend.components;

import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.components.format.Format;
import ru.albemuth.frontend.components.format.Formatted;
import ru.albemuth.frontend.components.format.SimpleFormat;

public abstract class TextField extends Component<Document> implements Formatted {

    public TextField(String name, Component parent, Document document) {
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

    public int getMaxLength() {
        return 0;
    }

    public String getMaxLengthString() {
        return getMaxLength() > 0 ? "maxlength=\"" + getMaxLength() + "\"" : "";
    }

    public void setContextProperties() throws RequestException {
        if (getRequestContext().isInSourceEventForm()) {
            String value = getRequestContext().getParameterValue(getFullName());
            if (value != null) {
                setValue(value);
            }
        }
    }

    public Format getFormat() {
        return SimpleFormat.INSTANCE;
    }

}
