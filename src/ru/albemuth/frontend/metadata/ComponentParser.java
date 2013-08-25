package ru.albemuth.frontend.metadata;

import org.apache.log4j.Logger;
import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import ru.albemuth.frontend.components.DirectAction;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.components.NestedContent;
import ru.albemuth.util.Configuration;
import ru.albemuth.util.ConfigurationException;
import ru.albemuth.util.Configured;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentParser implements Configured {

    private static final Logger LOG                                     = Logger.getLogger(ComponentParser.class);

    public static final String ENCODING_DEFAULT                         = "Cp1251";
    public static final String DEFAULT_COMPONENT_PATH                   = "/ru/albemuth/frontend/components/";
    public static final String DEFAULT_COMPONENT_CONTENT_EXTENTION      = "content";

    public static final String ATTRIBUTE_CID                            = "cid";
    public static final String ATTRIBUTE_LISTENER                       = "listener";
    public static final String ATTRIBUTE_NULL_VALUE                     = "$null";
    public static final String ATTRIBUTE_START_PROPERTY_DYNAMIC         = "$";
    public static final Pattern TEXT_CONTENT_PATTERN                    = Pattern.compile("(\\$[a-zA-Z][a-zA-Z\\d]*(?:\\.[a-zA-Z][a-zA-Z\\d]*)*)|(\\$\\{[^}]*\\})|([^$]+)", Pattern.DOTALL);
    public static final Pattern ARRAY_PROPERTY_CONTENT_PATTERN          = Pattern.compile("(\\$[a-zA-Z][a-zA-Z\\d]*(?:\\.[a-zA-Z][a-zA-Z\\d]*)*)|(\\$\\{[^}]*\\})|([^$ ,]+)(?:, ?)?", Pattern.DOTALL);
    public static final String MACROS_ELEMENT_TAG_NAME                  = "macros";
    public static final String MACROS_PATTERN_ELEMENT_TAG_NAME          = "pattern";
    public static final String MACROS_REPLACEMENT_ELEMENT_TAG_NAME      = "replacement";
    public static final String IMPORT_ELEMENT_TAG_NAME                  = "import";
    public static final String IMPORT_PATH_ATTRIBUTE_NAME               = "path";

    private ComponentClassBuilder componentClassBuilder;
    private Map<String, ComponentDescriptor> descriptorsMap = new HashMap<String, ComponentDescriptor>();
    private int componentCounter;
    private String encoding;
    private String defaultComponentPath;
    private String componentContentExtention;
    private ComplexMacros macroses;
    private List<String> imports;
    private boolean minifyHtml;

    public ComponentParser(ComponentClassBuilder componentClassBuilder) {
        this.componentClassBuilder = componentClassBuilder;
    }

    public void configure(Configuration cfg) throws ConfigurationException {
        encoding = cfg.getStringValue(this, "encoding", ENCODING_DEFAULT);
        defaultComponentPath = cfg.getStringValue(this, "default-component-path", DEFAULT_COMPONENT_PATH);
        componentContentExtention = cfg.getStringValue(this, "component-content-extenion", DEFAULT_COMPONENT_CONTENT_EXTENTION);
        componentClassBuilder.configure(cfg);
        minifyHtml = cfg.getBooleanValue(this, "minify-html", false);
        setSettings(cfg);
    }

    public String getEncoding() {
        return encoding;
    }

    public String getDefaultComponentPath() {
        return defaultComponentPath;
    }

    public String getComponentContentExtention() {
        return componentContentExtention;
    }

    protected int getNextComponentId() {
        return ++componentCounter;
    }

    public Class getComponentClass(String componentName) throws MetadataException {
        Class componentClass = null;
        if (!componentName.startsWith("/")) {
            for (String importPath: imports) {
                componentClass = getClass((importPath + componentName).substring(1).replace('/', '.'));
                break;
            }
            if (componentClass == null) {
                componentClass = getClass((defaultComponentPath + componentName).substring(1).replace('/', '.'));
            }
        } else {
            componentClass = getClass(componentName.substring(1).replace('/', '.'));
        }
        if (componentClass != null) {
            return componentClass;
        } else {
            throw new MetadataException("Can't found class for component " + componentName);
        }
    }

    private Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public DocumentDescriptor getDocumentDescriptor(String documentName) throws MetadataException {
        //return (DocumentDescriptor)getComponentDescriptor(documentName, "" + getNextComponentId() + System.currentTimeMillis());
        return (DocumentDescriptor)getComponentDescriptor(documentName, "");
    }

    protected synchronized ComponentDescriptor getComponentDescriptor(String componentName, String descriptorName) throws MetadataException {
        try {
            Class componentClass = getComponentClass(componentName);
            ComponentDescriptor ret = descriptorsMap.get(componentClass.getName() + descriptorName);
            if (ret == null) {
                ret = createComponentDescriptor(componentClass, descriptorName);
                descriptorsMap.put(componentClass.getName() + descriptorName, ret);
            }
            return ret.clone();
        } catch (CloneNotSupportedException e) {
            throw new MetadataException("Can't create class for component " + componentName + ": can't clone component descriptor", e);
        }
    }

    protected ComponentDescriptor createComponentDescriptor(Class componentClass, String descriptorName) throws MetadataException {
        ComponentDescriptor ret;
        if (NestedContent.class.equals(componentClass)) {
            ret = new NestedContentDescriptor(componentClassBuilder, null, componentClass);
        } else {
            //componentClass = Class.forName(componentName.substring(1).replace('/', '.'));
            if (Document.class.isAssignableFrom(componentClass)) {
                ret = new DocumentDescriptor(componentClassBuilder, descriptorName, componentClass);
            } else if (DirectAction.class.isAssignableFrom(componentClass)) {
                ret = new DirectActionDescriptor(componentClassBuilder, descriptorName, componentClass);
            } else {
                ret = new ComponentDescriptor(componentClassBuilder, descriptorName, componentClass);
            }
            String componentContent = getComponentContent(componentClass);
            if (componentContent != null) {
                componentContent = macroses.apply(new StringBuffer(componentContent)).toString();
                NodeList contentRootNodes = parseComponentContent(componentContent);
                List<ComponentDescriptor> childrenList = buildComponentContent(ret, ret, contentRootNodes, 0);
                ComponentDescriptor[] children = new ComponentDescriptor[childrenList.size()];
                childrenList.toArray(children);
                ret.setChildren(children);
            }
        }
        return ret;

    }

    protected String getComponentContent(Class componentClass) throws MetadataException {
        return readContent(getComponentContentPath(componentClass));
    }

    protected String getComponentContentPath(Class componentClass) {
        String contentPath;
        if (componentClass.isAnnotationPresent(ContentFile.class)) {
            contentPath = ((ContentFile)componentClass.getAnnotation(ContentFile.class)).path();
        } else {
            contentPath = "/" + componentClass.getName().replace('.', '/') + "." + getComponentContentExtention();
        }
        return contentPath;
    }

    protected String readContent(String componentUrl) throws MetadataException {
        StringBuilder ret = new StringBuilder();
        InputStreamReader in = null;
        try {
            URL contentURL = ComponentParser.class.getResource(componentUrl);
            if (contentURL != null) {
                in = new InputStreamReader((InputStream)contentURL.getContent(), getEncoding());
                char[] buf = new char[1024];
                for (int read = in.read(buf); read != -1; read = in.read(buf)) {
                    ret.append(buf, 0, read);
                }
            } else {
                //throw new MetadataException("Can't read component content " + componentUrl + ": no resource found for this url");
                return null;
            }
        } catch (IOException e) {
            throw new MetadataException("Can't read component content " + componentUrl, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error("Can't close input reader while reading component content " + componentUrl, e);
                }
            }
        }
        return ret.toString();
    }

    protected NodeList parseComponentContent(String componentContent) throws MetadataException {
        NodeList ret;
        Parser contentParser = Parser.createParser(componentContent, getEncoding());
        try {
            ret = contentParser.parse(null);
        } catch (ParserException e) {
            throw new MetadataException("Can't parse component content:\n" + componentContent, e);
        }
        return ret;
    }

    protected List<ComponentDescriptor> buildComponentContent(ComponentDescriptor owner, ComponentDescriptor parent, NodeList componentContent, int minifyNestedLevel) throws MetadataException {
        List<ComponentDescriptor> ret = new ArrayList<ComponentDescriptor>();
        try {
            for (NodeIterator nodesIterator = componentContent.elements(); nodesIterator.hasMoreNodes(); ) {
                Node node = nodesIterator.nextNode();
                if (node instanceof Tag) {
                    Tag tag = (Tag)node;
                    String cid = tag.getAttribute(ATTRIBUTE_CID);
                    if (cid != null) {
                        ComponentDescriptor childComponentDescriptor = getComponentDescriptor(cid, parent.getName() + "2" + owner.getName() + getNextComponentId());
                        createComponentLinks(owner, childComponentDescriptor, tag);
                        NodeList childComponentContent = tag.getChildren();
                        if (childComponentContent != null) {
                            List<ComponentDescriptor> contentComponents = buildComponentContent(owner, childComponentDescriptor, childComponentContent, minifyNestedLevel);
                            setNestedContent(childComponentDescriptor, contentComponents);
                        }
                        ret.add(childComponentDescriptor);
                    } else {
                        NodeList nodeChildren = tag.getChildren();
                        if (tag.getRawTagName().equals("script") || tag.getRawTagName().equals("pre")) {
                            minifyNestedLevel++;
                        } else if (tag.getRawTagName().equals("/pre")) {
                            minifyNestedLevel--;
                        }
                        if (nodeChildren != null) {
                            ret.addAll(parseTextContent(owner, "<" + tag.getText() + ">", minifyNestedLevel));
                            ret.addAll(buildComponentContent(owner, parent, nodeChildren, minifyNestedLevel));
                            ret.add(new ConstantTextDescriptor(componentClassBuilder, null, "</" + tag.getRawTagName() + ">", minifyHtml && minifyNestedLevel == 0));
                        } else {
                            if ("![CDATA[".equals(node.getText())) {//CDATA support
                                ret.addAll(parseTextContent(owner, "<![CDATA[", minifyNestedLevel));
                            } else {
                                ret.addAll(parseTextContent(owner, "<" + node.getText() + ">", minifyNestedLevel));
                                if (tag.getEndTag() != null && tag.getEndPosition() == tag.getEndTag().getStartPosition()) {
                                    ret.add(new ConstantTextDescriptor(componentClassBuilder, null, "</" + tag.getRawTagName() + ">", minifyHtml && minifyNestedLevel == 0));
                                }
                            }
                        }
                        if (tag.getRawTagName().equals("script")) {
                            minifyNestedLevel--;
                        }
                    }
                } else if (node instanceof TextNode) {
                    ret.addAll(parseTextContent(owner, node.getText(), minifyNestedLevel));
                } else if (node instanceof RemarkNode) {
                    ret.add(new ConstantTextDescriptor(componentClassBuilder, null, "<!--" + node.getText() + "-->", minifyHtml && minifyNestedLevel == 0));
                } else {
                    throw new MetadataException("Can't bulid component " + parent.getComponentClass().getName() + ": unknown node " + node.getClass().getName() + "(" + node.getText() + ")");
                }
            }
        } catch (ParserException e) {
            throw new MetadataException("Can't build component " + parent.getComponentClass().getName(), e);
        }
        return optimizeConstantTextDescriptors(ret);
    }

    protected void setNestedContent(ComponentDescriptor componentDescriptor, List<ComponentDescriptor> nestedContent) {
        /*List<ComponentDescriptor> componentChildrenList = new ArrayList<ComponentDescriptor>();
        for (ComponentDescriptor child: componentDescriptor.getChildren()) {
            if (child instanceof NestedContentDescriptor) {
                componentChildrenList.addAll(nestedContent);
            } else {
                componentChildrenList.add(child);
            }
        }
        ComponentDescriptor[] componentChildren = new ComponentDescriptor[componentChildrenList.size()];
        componentDescriptor.setChildren(componentChildrenList.toArray(componentChildren));*/
        setNestedContentRecursive(componentDescriptor, nestedContent);
    }

    protected void setNestedContentRecursive(ComponentDescriptor componentDescriptor, List<ComponentDescriptor> nestedContent) {
        List<ComponentDescriptor> componentChildrenList = new ArrayList<ComponentDescriptor>();
        for (ComponentDescriptor child: componentDescriptor.getChildren()) {
            if (child instanceof NestedContentDescriptor) {
                componentChildrenList.addAll(nestedContent);
            } else {
                componentChildrenList.add(child);
                setNestedContentRecursive(child, nestedContent);
            }
        }
        ComponentDescriptor[] componentChildren = new ComponentDescriptor[componentChildrenList.size()];
        componentDescriptor.setChildren(componentChildrenList.toArray(componentChildren));
    }

    protected void setAdditionalAttributesContent(ComponentDescriptor componentDescriptor, List<ComponentDescriptor> additionalAttributsContent) {
        List<ComponentDescriptor> componentChildrenList = new ArrayList<ComponentDescriptor>();
        boolean add = false;
        for (ComponentDescriptor child: componentDescriptor.getChildren()) {
            if (!add && child instanceof ConstantTextDescriptor) {
                ConstantTextDescriptor text = (ConstantTextDescriptor)child;
                int gtIndex = text.getContent().indexOf("/>");
                if (gtIndex == -1) {
                    gtIndex = text.getContent().indexOf('>');
                }
                if (gtIndex != -1) {
                    componentChildrenList.add(new ConstantTextDescriptor(componentClassBuilder, null, text.getContent().substring(0, gtIndex) + " ", minifyHtml));
                    componentChildrenList.addAll(additionalAttributsContent);
                    componentChildrenList.add(new ConstantTextDescriptor(componentClassBuilder, null, " " + text.getContent().substring(gtIndex), minifyHtml));
                    add = true;
                } else {
                    componentChildrenList.add(child);
                }
            } else {
                componentChildrenList.add(child);
            }
        }
        ComponentDescriptor[] componentChildren = new ComponentDescriptor[componentChildrenList.size()];
        componentDescriptor.setChildren(componentChildrenList.toArray(componentChildren));
    }

    protected List<ComponentDescriptor> parseTextContent(ComponentDescriptor owner, String textContent, int minifyNestedLevel) throws MetadataException {
        List<ComponentDescriptor> components = new ArrayList<ComponentDescriptor>();
        Matcher m = TEXT_CONTENT_PATTERN.matcher(textContent);
        for (; m.find(); ) {
            if (m.group(1) != null) {
                PropertyValueDescriptor valueDescriptor = new PropertyValueDescriptor(componentClassBuilder, m.group(1).substring(1));
                valueDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(owner, valueDescriptor.getName()), new PropertyDescriptor(valueDescriptor, "value"))});
                components.add(valueDescriptor);
            } else if (m.group(2) != null) {
                ComplexPropertyValueDescriptor complexPropertyValueDescriptor = new ComplexPropertyValueDescriptor(componentClassBuilder, "complex value");
                complexPropertyValueDescriptor.setChildren(parseTextContent(owner, m.group(2).substring(2, m.group(2).length() - 1), minifyNestedLevel).toArray(new ComponentDescriptor[]{}));
                components.add(complexPropertyValueDescriptor);
            } else {
                ConstantTextDescriptor textComponent = new ConstantTextDescriptor(componentClassBuilder, null, m.group(3), minifyHtml && minifyNestedLevel == 0);
                components.add(textComponent);
            }
        }
        return components;
    }

    protected List<ComponentDescriptor> optimizeConstantTextDescriptors(List<ComponentDescriptor> descriptors) {
        if (descriptors.size() > 0) {
            ComponentDescriptor previous = descriptors.get(0);
            if (previous instanceof ConstantTextDescriptor && ((ConstantTextDescriptor)previous).isMinify()) {
                minifyHtml((ConstantTextDescriptor)previous);
            }
            if (descriptors.size() > 1) {
                for (Iterator<ComponentDescriptor> it = descriptors.listIterator(1); it.hasNext(); ) {
                    ComponentDescriptor cd = it.next();
                    if (cd instanceof ConstantTextDescriptor) {
                        if (previous instanceof ConstantTextDescriptor && ((ConstantTextDescriptor)cd).isMinify() == ((ConstantTextDescriptor)previous).isMinify()) {
                            String content = ((ConstantTextDescriptor)previous).getContent() + ((ConstantTextDescriptor)cd).getContent();
                            ((ConstantTextDescriptor)previous).setContent(content);
                            if (((ConstantTextDescriptor)previous).isMinify()) {
                                minifyHtml((ConstantTextDescriptor)previous);
                            }
                            it.remove();
                        } else {
                            if (((ConstantTextDescriptor)cd).isMinify()) {
                                minifyHtml((ConstantTextDescriptor)cd);
                            }
                            previous = cd;
                        }
                    } else {
                        previous = cd;
                    }
                }
            }
        }
        return descriptors;
    }

    protected void minifyHtml(ConstantTextDescriptor descriptor) {
        descriptor.setContent(descriptor.getContent().replaceAll("\\t+", " ").replaceAll(" +", " ").replaceAll(" \\n", "\n").replaceAll("\\n ", "\n").replaceAll("\\n+", "\n"));
    }

    protected void createComponentLinks(ComponentDescriptor owner, ComponentDescriptor component, Tag componentTag) throws MetadataException {
        List attributes = componentTag.getAttributesEx();
        String additionalAttributesString = "";
        if (attributes != null) {
            List<LinkDescriptor> componentLinks = new ArrayList<LinkDescriptor>();
            for (Object attribute : attributes) {
                Attribute a = (Attribute) attribute;
                if (a.getName() != null && a.getValue() != null && !ATTRIBUTE_CID.equals(a.getName())) {
                    if (ATTRIBUTE_LISTENER.equals(a.getName())) {
                        component.setListener(new ListenerDescriptor(owner, a.getValue()));
                    } else {
                        PropertyDescriptor componentProperty = new PropertyDescriptor(component, a.getName());
                        //if (!"class".equals(componentProperty.getName())) {
                        if (!"class".equals(componentProperty.getName()) && isProperty(component.getComponentClass(), componentProperty.getName())) {
                            String value = a.getValue();
                            //if (value.indexOf(',') != -1) {//array property
                            if (value.startsWith("{") && value.endsWith("}")) {//array property
                                value = value.substring(1, value.length() - 1);
                                List<LinkSide> sourcesList = new ArrayList<LinkSide>();
                                Matcher m = ARRAY_PROPERTY_CONTENT_PATTERN.matcher(value);
                                for (; m.find(); ) {
                                    if (m.group(1) != null) {
                                        if (ATTRIBUTE_NULL_VALUE.equals(m.group(1))) {
                                            sourcesList.add(new ConstantValueDescriptor(null));
                                        } else {
                                            sourcesList.add(new PropertyDescriptor(owner, m.group(1).substring(ATTRIBUTE_START_PROPERTY_DYNAMIC.length())));
                                        }
                                    } else if (m.group(2) != null) {
                                        sourcesList.add(parseComplexProperty(owner, m.group(2).substring(2, m.group(2).length() - 1)));
                                    } else {
                                        sourcesList.add(new ConstantValueDescriptor(m.group(3)));
                                    }
                                }
                                LinkSide[] sources = new LinkSide[sourcesList.size()];
                                sourcesList.toArray(sources);
                                PropertiesArrayDescriptor ownerProperty = new PropertiesArrayDescriptor(sources);
                                LinkDescriptor link = new LinkDescriptor(ownerProperty, componentProperty);
                                componentLinks.add(link);
                                for (LinkSide source: sources) {
                                    if (source instanceof ComplexPropertyDescriptor) {
                                        owner.addComplexPropertyLink(link);
                                        break;
                                    }
                                }
                            } else {
                                Matcher m = TEXT_CONTENT_PATTERN.matcher(value);
                                if (m.find()) {
                                    if (m.group(1) != null) {
                                        if (ATTRIBUTE_NULL_VALUE.equals(value)) {
                                            LinkDescriptor link = new LinkDescriptor(new ConstantValueDescriptor(null), componentProperty);
                                            componentLinks.add(link);
                                        } else {
                                            PropertyDescriptor ownerProperty = new PropertyDescriptor(owner, value.substring(ATTRIBUTE_START_PROPERTY_DYNAMIC.length()));
                                            LinkDescriptor link = new LinkDescriptor(ownerProperty, componentProperty);
                                            componentLinks.add(link);
                                        }
                                    } else if (m.group(2) != null) {
                                        ComplexPropertyDescriptor ownerComplexProperty = parseComplexProperty(owner, m.group(2).substring(2, m.group(2).length() - 1));
                                        LinkDescriptor link = new LinkDescriptor(ownerComplexProperty, componentProperty);
                                        componentLinks.add(link);
                                        owner.addComplexPropertyLink(link);
                                    } else {
                                        LinkDescriptor link = new LinkDescriptor(new ConstantValueDescriptor(m.group(3)), componentProperty);
                                        componentLinks.add(link);
                                    }
                                } else {
                                    throw new MetadataException("Can't create links for component " + owner + ": Unexpected link property value " + value);
                                }
                            }
                        } else {
                            if (additionalAttributesString.length() > 0) {
                                additionalAttributesString += " ";
                            }
                            additionalAttributesString += a.getName() + "=\"" + a.getValue() + "\"";
                        }
                    }
                }
            }
            LinkDescriptor[] links = new LinkDescriptor[componentLinks.size()];
            componentLinks.toArray(links);
            component.setLinks(links);
            List<ComponentDescriptor> additionalAttributesContent = parseTextContent(owner, additionalAttributesString, 0);
            setAdditionalAttributesContent(component, additionalAttributesContent);
        }
    }

    protected ComplexPropertyDescriptor parseComplexProperty(ComponentDescriptor owner, String value) {
        List<LinkSide> values = new ArrayList<LinkSide>();
        Matcher m = TEXT_CONTENT_PATTERN.matcher(value);
        for (; m.find(); ) {
            if (m.group(1) != null) {
                PropertyDescriptor valueDescriptor = new PropertyDescriptor(owner, m.group(1).substring(1));
                values.add(valueDescriptor);
            } else if (m.group(2) != null) {
                /*ComplexPropertyValueDescriptor complexPropertyValueDescriptor = new ComplexPropertyValueDescriptor(componentClassBuilder, "complex value");
                complexPropertyValueDescriptor.setChildren(parseTextContent(owner, m.group(2).substring(2, m.group(2).length() - 1)).toArray(new ComponentDescriptor[]{}));
                components.add(complexPropertyValueDescriptor);*/
            } else {
                ConstantValueDescriptor valueDescriptor = new ConstantValueDescriptor(m.group(3));
                values.add(valueDescriptor);
            }
        }
        return new ComplexPropertyDescriptor(values.toArray(new LinkSide[]{}), "complex" + getNextComponentId(), owner);
    }

    protected static boolean isProperty(Class clazz, String name) {
        boolean ret;
        try {
            ret = clazz.getMethod(ComponentClassBuilder.getPropertyMethodName("get", name)) != null;
        } catch (NoSuchMethodException e) {
            try {
                ret = clazz.getMethod(ComponentClassBuilder.getPropertyMethodName("is", name)) != null;
            } catch (NoSuchMethodException e1) {
                try {
                    ret = clazz.getMethod(name) != null;
                } catch (NoSuchMethodException e2) {
                    try {
                        ret = clazz.getField(name) != null;
                    } catch (NoSuchFieldException e3) {
                        ret = false;
                    }
                }
            }
        }
        return ret;
    }

    protected void setSettings(Configuration cfg) throws ConfigurationException {
        Element settingsElement = readFrontendSettings(cfg);
        this.macroses = getMacroses(settingsElement);
        this.imports = getImports(settingsElement);
    }

    protected Element readFrontendSettings(Configuration cfg) throws ConfigurationException {
        SAXBuilder builder = new SAXBuilder(false);
        builder.setEntityResolver(new NoOpEntityResolver());
        String frontendSettingsFilePath = cfg.getStringValue(this, "frontend-settings", "/frontend-settings.xml");
        try {
            org.jdom.Document descriptionDoc = builder.build(new InputSource(new StringReader(readContent(frontendSettingsFilePath))));
            return descriptionDoc.getRootElement();
        } catch (MetadataException e) {
            throw new ConfigurationException("Can't read frontend settings file " + frontendSettingsFilePath, e);
        } catch (JDOMException e) {
            throw new ConfigurationException("Can't build JDOM document from frontend settings file " + frontendSettingsFilePath, e);
        } catch (IOException e) {
            throw new ConfigurationException("Can't build JDOM document from frontend settings file " + frontendSettingsFilePath, e);
        }
    }

    protected ComplexMacros getMacroses(Element settingsElement) {
        ComplexMacros macros = new ComplexMacros();
        for (Element element: (List<Element>)settingsElement.getChildren(MACROS_ELEMENT_TAG_NAME)) {
            macros.addMacros(parseMacrosElement(element));
        }
        return macros;
    }

    protected Macros parseMacrosElement(Element macrosElement) {
        List<Element> subMacroses = (List<Element>)macrosElement.getChildren(MACROS_ELEMENT_TAG_NAME);
        if (subMacroses.size() > 0) {
            ComplexMacros complexMacros = new ComplexMacros();
            for (Element element: subMacroses) {
                complexMacros.addMacros(parseMacrosElement(element));
            }
            return complexMacros;
        } else {
            return parseSimpleMacrosElement(macrosElement);
        }
    }

    protected SimpleMacros parseSimpleMacrosElement(Element simpleMacrosElement) {
        return new SimpleMacros(simpleMacrosElement.getChild(MACROS_PATTERN_ELEMENT_TAG_NAME).getValue(), simpleMacrosElement.getChild(MACROS_REPLACEMENT_ELEMENT_TAG_NAME).getValue());
    }

    protected List<String> getImports(Element settingsElement) {
        List<String> imports = new ArrayList<String>();
        for (Element element: (List<Element>)settingsElement.getChildren(IMPORT_ELEMENT_TAG_NAME)) {
            imports.add(element.getAttribute(IMPORT_PATH_ATTRIBUTE_NAME).getValue());
        }
        return imports;
    }

    public class NoOpEntityResolver implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringReader(""));
        }

    }

}
