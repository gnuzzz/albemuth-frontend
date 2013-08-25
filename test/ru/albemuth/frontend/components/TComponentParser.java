package ru.albemuth.frontend.components;

import ru.albemuth.frontend.metadata.*;

import java.util.Map;
import java.util.HashMap;

public class TComponentParser extends ComponentParser {

    private Map<String, String> componentsContentMap = new HashMap<String, String>();

    public TComponentParser(ComponentClassBuilder componentClassBuilder) {
        super(componentClassBuilder);
    }

    public DocumentDescriptor getDocumentDescriptor(String documentName) throws MetadataException {
        return (DocumentDescriptor)getComponentDescriptor(documentName, Math.abs(getComponentContent(getComponentClass(documentName)).hashCode()) + "");
    }

    protected synchronized ComponentDescriptor getComponentDescriptor(String componentName, String descriptorName) throws MetadataException {
        return createComponentDescriptor(getComponentClass(componentName), descriptorName);
    }

    protected String getComponentContent(Class componentClass) throws MetadataException {
        String ret = componentsContentMap.get("/" + componentClass.getName().replace('.', '/'));
        if (ret == null) {
            ret= super.getComponentContent(componentClass);
        }
        return ret;
    }

    public void setComponentContent(String componentName, String componentContent) {
        componentsContentMap.put(componentName, componentContent);
    }

    public String getComponentContentExtention() {
        return "content";
    }
}
