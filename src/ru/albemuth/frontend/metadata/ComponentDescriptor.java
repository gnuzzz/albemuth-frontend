package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ComponentDescriptor implements Cloneable, Serializable {

    public    static final ComponentDescriptor[] EMPTY_ARRAY                       = new ComponentDescriptor[0];
    protected static final Map<ComponentDescriptor, Component> COMPONENTS_CACHE    = new HashMap<ComponentDescriptor, Component>();

    protected transient ComponentClassBuilder componentClassBuilder;
    protected String name;
    protected Class componentClass;
    protected Class componentImplClass;
    protected ComponentDescriptor[] children = ComponentDescriptor.EMPTY_ARRAY;
    protected LinkDescriptor[] links = LinkDescriptor.EMPTY_ARRAY;
    protected ListenerDescriptor listener;
    protected List<LinkDescriptor> complexPropertiesLinks = new ArrayList<LinkDescriptor>();

    public ComponentDescriptor(ComponentClassBuilder componentClassBuilder, String name, Class componentClass) {
        this.componentClassBuilder = componentClassBuilder;
        this.name = name;
        this.componentClass = componentClass;
    }

    public String getName() {
        return name;
    }

    public Class getComponentClass() {
        return componentClass;
    }

    public Class getComponentImplClass() {
        return componentImplClass;
    }

    public void setComponentImplClass(Class componentImplClass) {
        this.componentImplClass = componentImplClass;
    }

    public ComponentDescriptor[] getChildren() {
        return children;
    }

    public void setChildren(ComponentDescriptor[] children) {
        this.children = children;
    }

    public LinkDescriptor[] getLinks() {
        return links;
    }

    public void setLinks(LinkDescriptor[] links) {
        this.links = links;
    }

    public ListenerDescriptor getListener() {
        return listener;
    }

    public void setListener(ListenerDescriptor listener) {
        this.listener = listener;
    }

    public List<LinkDescriptor> getComplexPropertiesLinks() {
        return complexPropertiesLinks;
    }

    public void addComplexPropertyLink(LinkDescriptor complexPropertyLink) {
        complexPropertiesLinks.add(complexPropertyLink);
    }

    public Component createComponent(Component parent) throws MetadataException {
        Class<Component> componentClass = componentClassBuilder.getComponentClass(this);
        Component component = createComponentInstance(componentClass, parent, parent != null ? parent.getDocument() : null);
        initComponentInstance(component);
        COMPONENTS_CACHE.put(this, component);
        List<Component> componentChildrenList = new ArrayList<Component>();
        for (ComponentDescriptor child: children) {
            if (!(child instanceof ConstantTextDescriptor) && !(child instanceof PropertyValueDescriptor) && !(child instanceof ComplexPropertyValueDescriptor)) {
                Component childComponent = child.createComponent(component);
                if (childComponent != null) {
                    componentChildrenList.add(childComponent);
                }
            }
        }
        Component[] componentChildren = new Component[componentChildrenList.size()];
        component.setChildren(componentChildrenList.toArray(componentChildren));
        return component;
    }

    protected Component createComponentInstance(Class<Component> componentClass, Component parent, Document document) throws MetadataException {
        try {
            Constructor<Component> c = null;
            for (Class documentClass = document.getClass(); !documentClass.equals(Component.class); documentClass = documentClass.getSuperclass()) {
                try {
                    c = componentClass.getDeclaredConstructor(String.class, Component.class, documentClass);
                    if (c != null) {
                        break;
                    }
                } catch (NoSuchMethodException e) {/* do nothing*/}
            }
            if (c != null) {
                return c.newInstance(getName(), parent, document);
            } else {
                throw new MetadataException("Can't create component " + this + ": can't find component constructor");
            }
/*
            Constructor<Component> c = componentClass.getDeclaredConstructor(String.class, Component.class, Document.class);
            return c.newInstance(getName(), parent, document);

        } catch (NoSuchMethodException e) {
            throw new MetadataException("Can't create component " + this + ": can't find component constructor", e);
*/
        } catch (InstantiationException e) {
            throw new MetadataException("Can't create component " + this + " instance", e);
        } catch (IllegalAccessException e) {
            throw new MetadataException("Can't create component " + this + " instance", e);
        } catch (InvocationTargetException e) {
            throw new MetadataException("Can't create component " + this + " instance", e);
        }
    }

    protected void initComponentInstance(Component component) throws MetadataException {
        Set<ComponentDescriptor> sourcesDescriptors = new HashSet<ComponentDescriptor>();
        for (LinkDescriptor l: getLinks()) {
            if (l.getSource() instanceof PropertyDescriptor) {
                sourcesDescriptors.add(((PropertyDescriptor)l.getSource()).getComponentDescriptor());
            } else if (l.getSource() instanceof ComplexPropertyDescriptor) {
                PropertyDescriptor property = null;
                for (LinkSide ls: ((ComplexPropertyDescriptor)l.getSource()).getSources()) {
                    if (ls instanceof PropertyDescriptor) {
                        property = (PropertyDescriptor)ls;
                        break;
                    }
                }
                if (property != null) {
                    sourcesDescriptors.add(property.getComponentDescriptor());
                } else {
                    sourcesDescriptors.add(((ComplexPropertyDescriptor)l.getSource()).getOwner());
                }
            } else if (l.getSource() instanceof PropertiesArrayDescriptor) {
                for (LinkSide ls: ((PropertiesArrayDescriptor)l.getSource()).getSources()) {
                    if (ls instanceof PropertyDescriptor) {
                        sourcesDescriptors.add(((PropertyDescriptor)ls).getComponentDescriptor());
                    } else if (ls instanceof ComplexPropertyDescriptor) {
                        for (LinkSide cls: ((ComplexPropertyDescriptor)ls).getSources()) {
                            if (cls instanceof PropertyDescriptor) {
                                sourcesDescriptors.add(((PropertyDescriptor)cls).getComponentDescriptor());
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (ComponentDescriptor cd: getChildren()) {
            if (cd instanceof PropertyValueDescriptor && !((PropertyDescriptor)cd.getLinks()[0].getSource()).getComponentDescriptor().equals(this)) {
                sourcesDescriptors.add(((PropertyDescriptor)cd.getLinks()[0].getSource()).getComponentDescriptor());
            } else if (cd instanceof ComplexPropertyValueDescriptor) {
                for (ComponentDescriptor ccd: cd.getChildren()) {
                    if (ccd instanceof PropertyValueDescriptor && !((PropertyDescriptor)ccd.getLinks()[0].getSource()).getComponentDescriptor().equals(this)) {
                        sourcesDescriptors.add(((PropertyDescriptor)ccd.getLinks()[0].getSource()).getComponentDescriptor());
                    }
                }
            }
        }
        if (getListener() != null) {
            sourcesDescriptors.add(getListener().getTargetDescriptor());
        }
        Map<ComponentDescriptor, String> sources = componentClassBuilder.getSourcesMap(this);
        for (ComponentDescriptor cd: sourcesDescriptors) {
            setComponentSource(component, COMPONENTS_CACHE.get(cd), cd.getComponentClass(), sources.get(cd));
        }
    }

    protected void setComponentSource(Component component, Component source, Class sourceSuperClass, String sourceName) throws MetadataException {
        try {
            //Method setter = component.getClass().getMethod(ComponentClassBuilder.getPropertyMethodName("set", sourceName), sourceSuperClass);
            Method setter = component.getClass().getMethod(ComponentClassBuilder.getPropertyMethodName("set", sourceName), source.getClass());
            setter.invoke(component, source);
        } catch (NoSuchMethodException e) {
            throw new MetadataException("Can't set source " + source.getClass().getName() + " " + sourceName + " for component " + component.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new MetadataException("Can't set source " + source.getClass().getName() + " " + sourceName + " for component " + component.getClass().getName(), e);
        } catch (InvocationTargetException e) {
            throw new MetadataException("Can't set source " + source.getClass().getName() + " " + sourceName + " for component " + component.getClass().getName(), e);
        }
    }

    public int hashCode() {
        return componentClass.hashCode() * name.hashCode();
    }

    public boolean equals(Object o) {
        return o instanceof ComponentDescriptor && ((ComponentDescriptor)o).componentClass != null && ((ComponentDescriptor)o).componentClass.equals(componentClass) && ((ComponentDescriptor)o).name.equals(name);
    }

    public String toString() {
        return getComponentClass().getName() + getName();
    }

    /*public ComponentDescriptor clone() throws CloneNotSupportedException {
        return (ComponentDescriptor)super.clone();
    }*/

    public ComponentDescriptor clone() throws CloneNotSupportedException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(1024 * 1024);
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(this);
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
            ComponentDescriptor ret = (ComponentDescriptor)in.readObject();
            in.close();
            return ret;
        } catch (IOException e) {
            throw new CloneNotSupportedException("Can't clone component descriptor " + this + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new CloneNotSupportedException("Can't clone component descriptor " + this + ": " + e.getMessage());
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(componentClassBuilder.getId());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        componentClassBuilder = ComponentClassBuilder.getInstance((String)in.readObject());
    }

}
