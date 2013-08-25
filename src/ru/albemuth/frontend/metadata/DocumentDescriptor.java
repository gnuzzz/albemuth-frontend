package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DocumentDescriptor extends ComponentDescriptor {

    public DocumentDescriptor(ComponentClassBuilder componentClassBuilder, String name, Class componentClass) {
        super(componentClassBuilder, name, componentClass);
    }

    protected Component createComponentInstance(Class<Component> componentClass, Component parent, Document document) throws MetadataException {
        try {
            Constructor<Component> c = componentClass.getDeclaredConstructor(String.class);
            return c.newInstance(getName());
        } catch (NoSuchMethodException e) {
            throw new MetadataException("Can't create document " + getComponentClass().getName() + " " + getName() + ": can't find document constructor", e);
        } catch (InstantiationException e) {
            throw new MetadataException("Can't create document " + getComponentClass().getName() + " " + getName() + " instance", e);
        } catch (IllegalAccessException e) {
            throw new MetadataException("Can't create document " + getComponentClass().getName() + " " + getName() + " instance", e);
        } catch (InvocationTargetException e) {
            throw new MetadataException("Can't create document " + getComponentClass().getName() + " " + getName() + " instance", e);
        }
    }

}
