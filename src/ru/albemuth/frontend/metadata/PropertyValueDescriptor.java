package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 14.12.2007
 * Time: 1:45:12
 */
public class PropertyValueDescriptor extends ComponentDescriptor {

    public PropertyValueDescriptor(ComponentClassBuilder componentClassBuilder, String name) {
        super(componentClassBuilder, name, null);
    }

    protected Component createComponentInstance(Class<Component> componentClass, Component parent, Document document) {
        return null;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(Object o) {
        return o instanceof PropertyValueDescriptor && ((PropertyValueDescriptor)o).getName().equals(getName());
    }

    public String toString() {
        return getName();
    }
    
}
