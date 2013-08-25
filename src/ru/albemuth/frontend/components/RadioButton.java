package ru.albemuth.frontend.components;

public abstract class RadioButton extends Component<Document> {

    public RadioButton(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public boolean isChecked() {
        return false;
    }

    public abstract void setChecked(boolean checked);

    public String getValue() {
        return "";
    }

    public String getFullValue() {
        String value = getValue();
        if (getItemid() != null && value.length() == 0) {
            value += "." + getItemid();
        }
        return value;
    }

    public String getCheckedString() {
        return isChecked() ? "checked" : "";
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
            String value = getRequestContext().getParameterValue(getName());
            if (value != null && value.equals(getFullValue())) {
                setChecked(true);
            } else {
                setChecked(false);
            }
        }
    }

}
