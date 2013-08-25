package ru.albemuth.frontend.components;

public abstract class CheckBox extends Component<Document> {

    public CheckBox(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public boolean isChecked() {
        return false;
    }

    public abstract void setChecked(boolean checked);

    public String getValue() {
        return "";
    }

    public String getFullName() {
        String name = getName();
        if (getItemid() != null) {
            name += "." + getItemid();
        }
        return name;
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
            String value = getRequestContext().getParameterValue(getFullName());
            if (value != null && value.equals(getValue())) {
                setChecked(true);
            } else {
                setChecked(false);
            }
        }
    }

}
