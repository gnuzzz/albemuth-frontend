package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.util.Configured;
import ru.albemuth.util.Configuration;
import ru.albemuth.util.ConfigurationException;

import java.util.Map;
import java.util.HashMap;

public abstract class ComponentClassBuilder implements Configured {

    private static final Map<String, ComponentClassBuilder> BUILDERS = new HashMap<String, ComponentClassBuilder>();

    private String id;
    protected Map<ComponentDescriptor, Class<Component>> classCache  = new HashMap<ComponentDescriptor, Class<Component>>();

    protected ComponentClassBuilder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void configure(Configuration configuration) throws ConfigurationException {
        BUILDERS.put(id, this);
    }

    public abstract Class<Component> buildComponentClass(ComponentDescriptor componentDescriptor) throws MetadataException;

    public synchronized Class<Component> getComponentClass(ComponentDescriptor componentDescriptor) throws MetadataException {
        Class<Component> componentClass = classCache.get(componentDescriptor);
        if (componentClass == null) {
            componentClass = buildComponentClass(componentDescriptor);
            classCache.put(componentDescriptor, componentClass);
        }
        return componentClass;
    }

    protected Map<ComponentDescriptor, String> getSourcesMap(ComponentDescriptor componentDescriptor) {
        Map<ComponentDescriptor, String> sources = new HashMap<ComponentDescriptor, String>();
        int sourceIndex = 0;
        for (int i = 0; i < componentDescriptor.getLinks().length; i++) {
            if (componentDescriptor.getLinks()[i].getSource() instanceof PropertyDescriptor) {
                sourceIndex = processPropertyDescriptor(sources, (PropertyDescriptor)componentDescriptor.getLinks()[i].getSource(), sourceIndex);
            } else if (componentDescriptor.getLinks()[i].getSource() instanceof ComplexPropertyDescriptor) {
                PropertyDescriptor property = null;
                for (LinkSide ls: ((ComplexPropertyDescriptor)componentDescriptor.getLinks()[i].getSource()).getSources()) {
                    if (ls instanceof PropertyDescriptor) {
                        property = (PropertyDescriptor)ls;
                        break;
                    }
                }
                if (property != null) {
                    sourceIndex = processPropertyDescriptor(sources, property, sourceIndex);
                } else {
                    sourceIndex = processSource(sources, ((ComplexPropertyDescriptor)componentDescriptor.getLinks()[i].getSource()).getOwner(), sourceIndex);
                }
            } else if (componentDescriptor.getLinks()[i].getSource() instanceof PropertiesArrayDescriptor) {
                for (LinkSide ls: ((PropertiesArrayDescriptor)componentDescriptor.getLinks()[i].getSource()).getSources()) {
                    if (ls instanceof PropertyDescriptor) {
                        sourceIndex = processPropertyDescriptor(sources, (PropertyDescriptor)ls, sourceIndex);
                    } else if (ls instanceof ComplexPropertyDescriptor) {
                        for (LinkSide cls: ((ComplexPropertyDescriptor)ls).getSources()) {
                            if (cls instanceof PropertyDescriptor) {
                                sourceIndex = processPropertyDescriptor(sources, (PropertyDescriptor)cls, sourceIndex);
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < componentDescriptor.getChildren().length; i++) {
            ComponentDescriptor cd = componentDescriptor.getChildren()[i];
            if (cd instanceof PropertyValueDescriptor && !((PropertyDescriptor)cd.getLinks()[0].getSource()).getComponentDescriptor().equals(componentDescriptor)) {
                sourceIndex = processPropertyDescriptor(sources, (PropertyDescriptor)cd.getLinks()[0].getSource(), sourceIndex);
            } else if (cd instanceof ComplexPropertyValueDescriptor) {
                for (ComponentDescriptor ccd: cd.getChildren()) {
                    if (ccd instanceof PropertyValueDescriptor && !((PropertyDescriptor)ccd.getLinks()[0].getSource()).getComponentDescriptor().equals(componentDescriptor)) {
                        sourceIndex = processPropertyDescriptor(sources, (PropertyDescriptor)ccd.getLinks()[0].getSource(), sourceIndex);
                    }
                }
            }
        }
        if (componentDescriptor.getListener() != null) {
            if (sources.get(componentDescriptor.getListener().getTargetDescriptor()) == null) {
                sources.put(componentDescriptor.getListener().getTargetDescriptor(), "source" + sourceIndex);
            }
        }
        return sources;
    }

    private int processPropertyDescriptor(Map<ComponentDescriptor, String> sources, PropertyDescriptor propertyDescriptor, int sourceIndex) {
        if ((sources.get(propertyDescriptor.getComponentDescriptor())) == null) {
            sources.put(propertyDescriptor.getComponentDescriptor(), "source" + sourceIndex);
            sourceIndex++;
        }
        return sourceIndex;
    }

    protected int processSource(Map<ComponentDescriptor, String> sources, ComponentDescriptor sourceDescriptor, int sourceIndex) {
        if ((sources.get(sourceDescriptor)) == null) {
            sources.put(sourceDescriptor, "source" + sourceIndex);
            sourceIndex++;
        }
        return sourceIndex;
    }

    public static String getPropertyMethodName(String prefix, String propertyName) {
        return prefix + (prefix.length() > 0 ? propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1) : propertyName);
    }

    public static ComponentClassBuilder getInstance(String id) {
        return BUILDERS.get(id);
    }

}
