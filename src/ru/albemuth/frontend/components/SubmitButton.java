package ru.albemuth.frontend.components;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 29.12.2007
 * Time: 19:00:17
 */
public abstract class SubmitButton extends ActionComponent {

    public SubmitButton(String name, Component parent, Document document) {
        super(name, parent, document);
    }

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
