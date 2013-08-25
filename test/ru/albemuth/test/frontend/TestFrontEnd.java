package ru.albemuth.test.frontend;

import junit.framework.TestCase;

import java.util.*;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import javassist.*;

/**
 * Created by IntelliJ IDEA.
 * User: vovan
 * Date: 03.11.2007
 * Time: 23:38:47
 */
public class TestFrontEnd extends TestCase {

    public void ctest() {
        System.out.println(RootComponent.class.isAssignableFrom(Component.class));
        System.out.println(Component.class.isAssignableFrom(RootComponent.class));
    }

    public void btest() {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass valueClass = pool.makeClass("ru.albemuth.test.frontend.ValueImpl");
            CtClass valueSuperClass = pool.get("ru.albemuth.test.frontend.Value");
            CtConstructor valueSuperClassConstructor = valueSuperClass.getConstructors()[0];
            if ((valueSuperClassConstructor.getModifiers() & Modifier.PUBLIC) == 0) {
                valueSuperClassConstructor.setModifiers(Modifier.PUBLIC);
            }
            valueClass.setSuperclass(valueSuperClass);
            CtField valueField = CtField.make("private String value;", valueClass);
            valueClass.addField(valueField);
            CtMethod valueGetterMethod = CtMethod.make("public String getValue() {return value;}", valueClass);
            valueClass.addMethod(valueGetterMethod);
            CtMethod valueSetterMethod = CtMethod.make("public void setValue(String value) {this.value = value;}", valueClass);
            valueClass.addMethod(valueSetterMethod);

            Class clazz = valueClass.toClass();
            Value v = (Value)clazz.newInstance();
            v.setValue("aaa");
            System.out.println(v.getValue());

            for (CtMethod method: valueClass.getMethods()) {
                System.out.println(method.getName() + ", " + method.getSignature());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void test() {
        try {
            ComponentDescriptor rootComponentDescriptor = new ComponentDescriptor(RootComponent.class);
            ComponentDescriptor nameComponentDescriptor = new ComponentDescriptor(NameComponent.class);
            ComponentDescriptor valueComponentDescriptor = new ComponentDescriptor(ValueComponent.class);
            rootComponentDescriptor.setChildren(new ComponentDescriptor[]{nameComponentDescriptor, valueComponentDescriptor});
            nameComponentDescriptor.setLinks(new LinkDescriptor[] {new LinkDescriptor(rootComponentDescriptor, new PropertyDescriptor("name"), nameComponentDescriptor, new PropertyDescriptor("name"))});
            valueComponentDescriptor.setLinks(new LinkDescriptor[] {new LinkDescriptor(rootComponentDescriptor, new PropertyDescriptor("value"), valueComponentDescriptor, new PropertyDescriptor("value"))});
            Component root = rootComponentDescriptor.createComponent();
            root.setName("aaa");
            root.setValue("bbb");
            root.render();
        } catch (ComponentCreationException e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class Value {

    public abstract String getValue();
    public abstract void setValue(String value);

    public RootComponent getRootComponent() {return null;}
    public void setrootComponent(RootComponent root) {}
}

abstract class Component {

    public static final Component[] EMPTY_ARRAY                         = new Component[0];

    protected Component[] children = EMPTY_ARRAY;

    public void setChildren(Component[] children) {
        this.children = children;
    }

    public abstract String getName();
    public abstract void setName(String name);
    public abstract String getValue();
    public abstract void setValue(String value);

    public void render() {
        for (Component c: children) {
            c.render();
        }
    }

}

class RootComponent extends Component {

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

abstract class NameComponent extends Component {

    public void render() {
        System.out.println("name: " + getName());
    }
}

abstract class ValueComponent extends Component {

    public void render() {
        System.out.println("value: " + getValue());
    }
}

class ComponentDescriptor {

    public static final ComponentDescriptor[] EMPTY_ARRAY                       = new ComponentDescriptor[0];
    public static final Map<ComponentDescriptor, Component> COMPONENTS_CACHE    = new HashMap<ComponentDescriptor, Component>();
    public static final Map<ComponentDescriptor, Class<Component>> CLASS_CACHE  = new HashMap<ComponentDescriptor, Class<Component>>();

    private Class componentClass;
    private ComponentDescriptor[] children = ComponentDescriptor.EMPTY_ARRAY;
    private LinkDescriptor[] links = LinkDescriptor.EMPTY_ARRAY;

    public ComponentDescriptor(Class componentClass) {
        this.componentClass = componentClass;
    }

    public Class getComponentClass() {
        return componentClass;
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

    public Component createComponent() throws ComponentCreationException {
        Component component = createComponentInstance();
        COMPONENTS_CACHE.put(this, component);
        Component[] componentChildren = new Component[children.length];
        for (int i = 0; i < componentChildren.length; i++) {
            componentChildren[i] = children[i].createComponent();
        }
        component.setChildren(componentChildren);
        return component;
    }

    protected Component createComponentInstance() throws ComponentCreationException {
        Class<Component> clazz = CLASS_CACHE.get(this);
        if (clazz == null) {
            clazz = createComponentClass();
            CLASS_CACHE.put(this, clazz);
        }
        try {
            Component component = clazz.newInstance();
            Set<ComponentDescriptor> sourcesDescriptors = new HashSet<ComponentDescriptor>();
            for (LinkDescriptor l: getLinks()) {
                sourcesDescriptors.add(l.getSource());
            }
            Map<ComponentDescriptor, String> sources = getSourcesMap();
            for (ComponentDescriptor cd: sourcesDescriptors) {
                setComponentSource(component, COMPONENTS_CACHE.get(cd), cd.getComponentClass(), sources.get(cd));
            }

            return component;
        } catch (IllegalAccessException e) {
            throw new ComponentCreationException("Can't create component instance", e);
        } catch (InstantiationException e) {
            throw new ComponentCreationException("Can't create component instance", e);
        }
    }

    protected Map<ComponentDescriptor, String> getSourcesMap() {
        Map<ComponentDescriptor, String> sources = new HashMap<ComponentDescriptor, String>();
        for (int i = 0 ; i < links.length; i++) {
            if (sources.get(links[i].getSource()) == null) {
                sources.put(links[i].getSource(), "source" + i);
            }
        }
        return sources;
    }

    protected void setComponentSource(Component component, Component source, Class sourceSuperClass, String sourceName) throws ComponentCreationException {
        try {
            Method setter = component.getClass().getMethod(getPropertyMethodName("set", sourceName), sourceSuperClass);
            setter.invoke(component, source);
        } catch (NoSuchMethodException e) {
            throw new ComponentCreationException("Can't set component source", e);
        } catch (IllegalAccessException e) {
            throw new ComponentCreationException("Can't set component source", e);
        } catch (InvocationTargetException e) {
            throw new ComponentCreationException("Can't set component source", e);
        }
    }

    protected String getPropertyMethodName(String prefix, String propertyName) {
        return prefix + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    protected Class<Component> createComponentClass() throws ComponentCreationException {
        try {
            Map<ComponentDescriptor, String> sources = getSourcesMap();

            ClassPool pool = ClassPool.getDefault();
            CtClass clazz = pool.makeClass(componentClass.getName() + "Impl");
System.out.println(componentClass.getName() + "Impl");
            CtClass superClass = pool.get(componentClass.getName());
System.out.println("extends " + componentClass.getName());
            CtConstructor valueSuperClassConstructor = superClass.getDeclaredConstructor(null);
            if ((valueSuperClassConstructor.getModifiers() & Modifier.PUBLIC) == 0) {
                valueSuperClassConstructor.setModifiers(Modifier.PUBLIC);
            }
            clazz.setSuperclass(superClass);

            for (ComponentDescriptor cd: sources.keySet()) {
                CtField sourceField = CtField.make("private " + cd.getComponentClass().getName() + " " + sources.get(cd) + ";", clazz);
System.out.println("private " + cd.getComponentClass().getName() + " " + sources.get(cd) + ";");
                clazz.addField(sourceField);
                CtMethod sourceSetterMethod = CtMethod.make("public void " + getPropertyMethodName("set", sources.get(cd)) + "(" + cd.getComponentClass().getName() + " source) {" + sources.get(cd) + " = source;}", clazz);
System.out.println("public void " + getPropertyMethodName("set", sources.get(cd)) + "(" + cd.getComponentClass().getName() + " source) {" + sources.get(cd) + " = source;}");
                clazz.addMethod(sourceSetterMethod);
            }

            for (LinkDescriptor link: links) {
                CtMethod superClassGetter = null;
                for (CtMethod method: superClass.getMethods()) {
                    if (method.getName().equals(getPropertyMethodName("get", link.getDestinationProperty().getName()))) {
                        superClassGetter = method;
                    }
                }
                if (superClassGetter != null && (superClassGetter.getModifiers() & Modifier.ABSTRACT) != 0) {
                    CtMethod getterMethod = CtMethod.make("public " + superClassGetter.getReturnType().getName() + " " + getPropertyMethodName("get", link.getDestinationProperty().getName()) + "() {return " + sources.get(link.getSource()) + "." + getPropertyMethodName("get", link.getSourceProperty().getName()) + "();}", clazz);
System.out.println("public " + superClassGetter.getReturnType().getName() + " " + getPropertyMethodName("get", link.getDestinationProperty().getName()) + "() {return " + sources.get(link.getSource()) + "." + getPropertyMethodName("get", link.getSourceProperty().getName()) + "();}");
                    clazz.addMethod(getterMethod);
                }

                CtMethod superClassSetter = null;
                for (CtMethod method: superClass.getMethods()) {
                    if (method.getName().equals(getPropertyMethodName("set", link.getDestinationProperty().getName()))) {
                        superClassSetter = method;
                    }
                }
                if (superClassSetter != null && (superClassSetter.getModifiers() & Modifier.ABSTRACT) != 0) {
                    CtMethod setterMethod = CtMethod.make("public void " + getPropertyMethodName("set", link.getDestinationProperty().getName()) + "(" + superClassSetter.getParameterTypes()[0].getName() + " value) {" + sources.get(link.getSource()) + "." + getPropertyMethodName("set", link.getSourceProperty().getName()) + "(value);}", clazz);
System.out.println("public void " + getPropertyMethodName("set", link.getDestinationProperty().getName()) + "(" + superClassSetter.getParameterTypes()[0].getName() + " value) {" + sources.get(link.getSource()) + "." + getPropertyMethodName("set", link.getSourceProperty().getName()) + "(value);}");
                    clazz.addMethod(setterMethod);
                }
            }

            Class ret = clazz.toClass();
            if (!Component.class.isAssignableFrom(ret)) {
                throw new ComponentCreationException("Can't create component class for class " + componentClass.getName() + ": class cast exception: class " + componentClass.getName() + " isn't a subclass of Component");
            }
            return ret;
        } catch (NotFoundException e) {
            throw new ComponentCreationException("Can't create component class for class " + componentClass.getName(), e);
        } catch (CannotCompileException e) {
            throw new ComponentCreationException("Can't create component class for class " + componentClass.getName(), e);
        }
    }

    public int hashCode() {
        return componentClass.hashCode();
    }

    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof ComponentDescriptor) {
            ComponentDescriptor cd = (ComponentDescriptor)obj;
            ret = cd.componentClass.equals(componentClass) && Arrays.equals(cd.children, children) && Arrays.equals(cd.links, links);
        }
        return ret;
    }

}

class PropertyDescriptor {

    private String name;

    public PropertyDescriptor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof PropertyDescriptor && ((PropertyDescriptor)obj).name.equals(name);
    }

}

class LinkDescriptor {

    public static final LinkDescriptor[] EMPTY_ARRAY                = new LinkDescriptor[0];

    private ComponentDescriptor source;
    private PropertyDescriptor sourceProperty;
    private ComponentDescriptor destination;
    private PropertyDescriptor destinationProperty;

    public LinkDescriptor(ComponentDescriptor source, PropertyDescriptor sourceProperty, ComponentDescriptor destination, PropertyDescriptor destinationProperty) {
        this.source = source;
        this.sourceProperty = sourceProperty;
        this.destination = destination;
        this.destinationProperty = destinationProperty;
    }

    public ComponentDescriptor getSource() {
        return source;
    }

    public PropertyDescriptor getSourceProperty() {
        return sourceProperty;
    }

    public ComponentDescriptor getDestination() {
        return destination;
    }

    public PropertyDescriptor getDestinationProperty() {
        return destinationProperty;
    }

    public int hashCode() {
        return source.hashCode() * sourceProperty.hashCode() * destination.hashCode() * destinationProperty.hashCode();
    }

    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof LinkDescriptor) {
            LinkDescriptor ld = (LinkDescriptor)obj;
            ret =  ld.source.getComponentClass().equals(source.getComponentClass())
                && ld.sourceProperty.equals(sourceProperty)
                && ld.destination.getComponentClass().equals(destination.getComponentClass())
                && ld.destinationProperty.equals(destinationProperty);
        }
        return ret;
    }

}

class ComponentCreationException extends Exception {

    public ComponentCreationException(String message) {
        super(message);
    }

    public ComponentCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentCreationException(Throwable cause) {
        super(cause);
    }

}