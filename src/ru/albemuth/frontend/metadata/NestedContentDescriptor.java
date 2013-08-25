package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

public class NestedContentDescriptor extends ComponentDescriptor {

    public NestedContentDescriptor(ComponentClassBuilder componentClassBuilder, String name, Class componentClass) {
        super(componentClassBuilder, name, componentClass);
    }

    protected Component createComponentInstance(Class<Component> componentClass, Component parent, Document document) {
        return null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "NestedContent";
    }

}
