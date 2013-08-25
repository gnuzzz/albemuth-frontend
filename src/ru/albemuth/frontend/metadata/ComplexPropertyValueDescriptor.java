package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

import java.util.Iterator;
import java.util.List;

public class ComplexPropertyValueDescriptor extends ComponentDescriptor {

    public ComplexPropertyValueDescriptor(ComponentClassBuilder componentClassBuilder, String name) {
        super(componentClassBuilder, name, null);
    }

    protected Component createComponentInstance(Class<Component> componentClass, Component parent, Document document) {
        return null;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(Object o) {
        boolean ret = o instanceof ComplexPropertyValueDescriptor && ((ComplexPropertyValueDescriptor)o).children.length == children.length;
        for (int i = 0; ret & i < children.length; i++) {
            ret = children[i].equals(((ComplexPropertyValueDescriptor)o).children[i]);
        }
        return ret;
    }

    public String toString() {
        return getName();
    }

}
