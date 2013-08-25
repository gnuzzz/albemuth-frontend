package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.util.Configuration;
import ru.albemuth.util.ConfigurationException;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 29.11.2007
 * Time: 1:42:09
 */
public class ComponentDescriptorFilesystemImpl extends ComponentClassBuilder {

    public ComponentDescriptorFilesystemImpl(String id) {
        super(id);
    }

    public void configure(Configuration cfg) throws ConfigurationException {
        //todo: implement this
    }

    public Class<Component> buildComponentClass(ComponentDescriptor componentDescriptor) throws MetadataException {
return null;//todo: implement this
    }
}
