package ru.albemuth.frontend.metadata;

public class PropertyDescriptor extends LinkSide {

    private ComponentDescriptor componentDescriptor;
    private String name;

    public PropertyDescriptor(ComponentDescriptor componentDescriptor, String name) {
        this.componentDescriptor = componentDescriptor;
        this.name = name;
    }

    public ComponentDescriptor getComponentDescriptor() {
        return componentDescriptor;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return componentDescriptor.hashCode() * name.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof PropertyDescriptor && ((PropertyDescriptor)obj).componentDescriptor.equals(componentDescriptor) && ((PropertyDescriptor)obj).name.equals(name);
    }

    public String toString() {
        return getComponentDescriptor() + "." + getName();
    }

}
