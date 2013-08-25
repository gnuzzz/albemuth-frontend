package ru.albemuth.frontend.components;

public abstract class ImageButton extends ActionComponent {

    public ImageButton(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getValue() {
        return "";
    }

    public String getSrc() {
        return "";
    }

    public String getAlt() {
        return "";
    }

    public String getFullName() {
        String name = getName();
        if (getItemid() != null) {
            name += "." + getItemid();
        }
        return name;
    }

    public boolean isDisabled() {
        return false;
    }

    public String getDisabledString() {
        return isDisabled() ? "disabled" : "";
    }

    protected boolean isSource() {
        if (getRequestContext().isInSourceEventForm()) {
            String buttonName = getFullName();
            return getValue() != null && getValue().equals(getRequestContext().getParameterValue(buttonName));
        } else {
            return false;
        }
    }

}
