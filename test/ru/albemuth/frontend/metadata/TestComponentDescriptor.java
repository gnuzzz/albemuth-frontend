package ru.albemuth.frontend.metadata;

import org.apache.log4j.Logger;
import ru.albemuth.frontend.*;
import ru.albemuth.frontend.components.*;
import ru.albemuth.frontend.context.ApplicationContext;
import ru.albemuth.frontend.context.ProcessContext;
import ru.albemuth.frontend.context.RequestContext;
import ru.albemuth.frontend.context.SessionContext;
import ru.albemuth.util.Configuration;
import ru.albemuth.util.ConvertorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 28.11.2007
 * Time: 3:31:23
 */
public class TestComponentDescriptor extends TestFrontend {

    private static final Logger LOG                                 = Logger.getLogger(TestComponentDescriptor.class);

    public void testSimpleProperties() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            componentClassBuilder.configure(new Configuration(new Properties()));
            ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootsimple", RootComponent.class);
            ComponentDescriptor nameComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "namesimple", NameComponent.class);
            ComponentDescriptor valueComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "valuesimple", ValueComponent.class);
            ComponentDescriptor booleanComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "booleansimple", BooleanComponent.class);
            rootComponentDescriptor.setChildren(new ComponentDescriptor[]{nameComponentDescriptor, valueComponentDescriptor, booleanComponentDescriptor});
            nameComponentDescriptor.setLinks(new LinkDescriptor[] {new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "componentName"), new PropertyDescriptor(nameComponentDescriptor, "componentName"))});
            valueComponentDescriptor.setLinks(new LinkDescriptor[] {new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "componentValue"), new PropertyDescriptor(valueComponentDescriptor, "componentValue"))});
            booleanComponentDescriptor.setLinks(new LinkDescriptor[] {new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "booleanValue"), new PropertyDescriptor(booleanComponentDescriptor, "booleanValue"))});
            RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
            RequestContext requestContext = new TRequestContext(root, createHttpServletRequest(root), createHttpServletResponse()) {
                private Map<String, String> parameters = new HashMap<String, String>();
                public String getParameterValue(String name) {
                    return parameters.get(name);
                }
                public void setParameterValue(String name, String value) {
                    parameters.put(name, value);
                }
            };
            requestContext.setParameterValue("out", null, "");
            root.setRequestContext(requestContext);
            root.setComponentName("aaa");
            root.setComponentValue("bbb");
            root.setBooleanValue(true);
            root.render();
            LOG.info(requestContext.getParameterValue("out", null));
            String out = requestContext.getParameterValue("out", null);
            assertTrue(out.contains("name: aaa"));
            assertTrue(out.contains("value: bbb"));
            assertTrue(out.contains("boolean value: true"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCompositeProperties() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            componentClassBuilder.configure(new Configuration(new Properties()));
            ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootcomposite", RootComponent.class);
            ComponentDescriptor nameComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "namecomposite", NameComponent.class);
            ComponentDescriptor valueComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "valuecomposite", ValueComponent.class);
            ComponentDescriptor nameLengthComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "namelengthcomposite", LengthComponent.class);
            ComponentDescriptor valueLengthComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "valuelengthcomposite", LengthComponent.class);
            rootComponentDescriptor.setChildren(new ComponentDescriptor[]{nameComponentDescriptor, valueComponentDescriptor});
            nameComponentDescriptor.setLinks(new LinkDescriptor[] {new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "componentName"), new PropertyDescriptor(nameComponentDescriptor, "componentName"))});
            nameComponentDescriptor.setChildren(new ComponentDescriptor[]{nameLengthComponentDescriptor});
            valueComponentDescriptor.setLinks(new LinkDescriptor[] {new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "componentValue"), new PropertyDescriptor(valueComponentDescriptor, "componentValue"))});
            valueComponentDescriptor.setChildren(new ComponentDescriptor[]{valueLengthComponentDescriptor});
            nameLengthComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(nameComponentDescriptor, "componentName.length"), new PropertyDescriptor(nameLengthComponentDescriptor, "length"))});
            valueLengthComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(valueComponentDescriptor, "componentValue.length"), new PropertyDescriptor(valueLengthComponentDescriptor, "length"))});
            RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
            RequestContext requestContext = new TRequestContext(root, createHttpServletRequest(root), createHttpServletResponse()) {
                private Map<String, String> parameters = new HashMap<String, String>();
                public String getParameterValue(String name) {
                    return parameters.get(name);
                }
                public void setParameterValue(String name, String value) {
                    parameters.put(name, value);
                }
            };
            requestContext.setParameterValue("out", null, "");
            root.setRequestContext(requestContext);
            root.setComponentName("aaa");
            root.setComponentValue("bbbb");
            root.render();
            LOG.info(requestContext.getParameterValue("out", null));
            String out = requestContext.getParameterValue("out", null);
            assertTrue(out.contains("name: aaa"));
            assertTrue(out.contains("length: 3"));
            assertTrue(out.contains("value: bbbb"));
            assertTrue(out.contains("length: 4"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testSetContextProperties() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            componentClassBuilder.configure(new Configuration(new Properties()));
            ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootsetcontextvalues", RootComponent.class);
            ComponentDescriptor contextComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "contextsetcontextvalues", ContextComponent.class);
            rootComponentDescriptor.setChildren(new ComponentDescriptor[]{contextComponentDescriptor});
            contextComponentDescriptor.setLinks(new LinkDescriptor[]{
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "componentName"), new PropertyDescriptor(contextComponentDescriptor, "requestContextProperty")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "primitiveIntValue"), new PropertyDescriptor(contextComponentDescriptor, "primitiveIntValue")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "objectiveIntValue"), new PropertyDescriptor(contextComponentDescriptor, "objectiveIntValue")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "dateValue"), new PropertyDescriptor(contextComponentDescriptor, "dateValue")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "processIntProperty"), new PropertyDescriptor(contextComponentDescriptor, "processIntProperty")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "processIntegerProperty"), new PropertyDescriptor(contextComponentDescriptor, "integerProperty"))
            });
            RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
            RequestContext requestContext = new TRequestContext(root, createHttpServletRequest(root), createHttpServletResponse()) {
                private Map<String, String> parameters = new HashMap<String, String>();
                public String getParameterValue(String name) {
                    return parameters.get(name);
                }
                public void setParameterValue(String name, String value) {
                    parameters.put(name, value);
                }
            };
            requestContext.setParameterValue("out", null, "");
            root.setRequestContext(requestContext);
            root.setContextProperties();
            assertEquals("requestContextProperty", root.getComponentName());
            assertEquals(1, root.getPrimitiveIntValue());
            assertEquals(Integer.valueOf(2), root.getObjectiveIntValue());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2007-12-04 00:00:00"), root.getDateValue());
            assertEquals(3, root.getProcessIntProperty());
            assertEquals(Integer.valueOf(4), root.getProcessIntegerProperty());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testReleaseProperties() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            componentClassBuilder.configure(new Configuration(new Properties()));
            ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootrelease", RootComponent.class);
            ComponentDescriptor contextComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "contextrelease", ContextComponent.class);
            rootComponentDescriptor.setChildren(new ComponentDescriptor[]{contextComponentDescriptor});
            contextComponentDescriptor.setLinks(new LinkDescriptor[]{
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "componentName"), new PropertyDescriptor(contextComponentDescriptor, "requestContextProperty")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "primitiveIntValue"), new PropertyDescriptor(contextComponentDescriptor, "primitiveIntValue")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "objectiveIntValue"), new PropertyDescriptor(contextComponentDescriptor, "objectiveIntValue")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "dateValue"), new PropertyDescriptor(contextComponentDescriptor, "dateValue")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "processIntProperty"), new PropertyDescriptor(contextComponentDescriptor, "processIntProperty")),
                    new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptor, "processIntegerProperty"), new PropertyDescriptor(contextComponentDescriptor, "integerProperty"))
            });
            RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
            RequestContext requestContext = new TRequestContext(root, createHttpServletRequest(root), createHttpServletResponse()) {
                private Map<String, String> parameters = new HashMap<String, String>();

                public String getParameterValue(String name) {
                    return parameters.get(name);
                }
                public void setParameterValue(String name, String value) {
                    parameters.put(name, value);
                }
            };
            requestContext.setParameterValue("out", null, "");
            root.setRequestContext(requestContext);
            root.setComponentName("requestContextProperty");
            root.setPrimitiveIntValue(1);
            root.setObjectiveIntValue(2);
            root.setDateValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2007-12-04 00:00:00"));
            root.setProcessIntProperty(3);
            root.setProcessIntegerProperty(4);
            root.release();
            assertNull(root.getComponentName());
            assertEquals(0, root.getPrimitiveIntValue());
            assertNull(root.getObjectiveIntValue());
            assertNull(root.getDateValue());
            assertEquals(0, root.getProcessIntProperty());
            assertNull(root.getProcessIntegerProperty());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testProcessAction() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            {
                componentClassBuilder.configure(new Configuration(new Properties()));
                ComponentDescriptor rootComponentDescriptorWithLinks = new DocumentDescriptor(componentClassBuilder, "rootactionwithlinks", RootComponent.class);
                ComponentDescriptor simpleActionComponentWithLinksDescriptor = new ComponentDescriptor(componentClassBuilder, "simpleactioncomponentwithlinks", SimpleActionComponent.class);
                rootComponentDescriptorWithLinks.setChildren(new ComponentDescriptor[]{simpleActionComponentWithLinksDescriptor});
                simpleActionComponentWithLinksDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(rootComponentDescriptorWithLinks, "processIntProperty"), new PropertyDescriptor(simpleActionComponentWithLinksDescriptor, "intProperty"))});
                simpleActionComponentWithLinksDescriptor.setListener(new ListenerDescriptor(rootComponentDescriptorWithLinks, "incIntProperty"));
                RootComponent rootWithLinks = (RootComponent)rootComponentDescriptorWithLinks.createComponent(null);
                rootWithLinks.setRequestContext(new TRequestContext(rootWithLinks, createHttpServletRequest(rootWithLinks), createHttpServletResponse()));
                rootWithLinks.setProcessIntProperty(1);
                rootWithLinks.getRequestContext().setEventSource((ActionComponent)rootWithLinks.getChildren()[0]);
                rootWithLinks.processEvents();
                assertEquals(rootWithLinks, rootWithLinks.getRequestContext().getResponseDocument());
                assertEquals(2, rootWithLinks.getProcessIntProperty());
            }
            {
                ComponentDescriptor rootComponentDescriptorWithoutLinks = new DocumentDescriptor(componentClassBuilder, "rootactionwithoutlinks", RootComponent.class);
                ComponentDescriptor simpleActionComponentWithoutLinksDescriptor = new ComponentDescriptor(componentClassBuilder, "simpleactioncomponentwithoutlinks", SimpleActionComponent.class);
                rootComponentDescriptorWithoutLinks.setChildren(new ComponentDescriptor[]{simpleActionComponentWithoutLinksDescriptor});
                simpleActionComponentWithoutLinksDescriptor.setListener(new ListenerDescriptor(rootComponentDescriptorWithoutLinks, "incIntProperty"));
                RootComponent rootWithoutLinks = (RootComponent)rootComponentDescriptorWithoutLinks.createComponent(null);
                rootWithoutLinks.setRequestContext(new TRequestContext(rootWithoutLinks, createHttpServletRequest(rootWithoutLinks), createHttpServletResponse()));
                rootWithoutLinks.setProcessIntProperty(2);
                rootWithoutLinks.getRequestContext().setEventSource((ActionComponent)rootWithoutLinks.getChildren()[0]);
                rootWithoutLinks.processEvents();
                assertEquals(rootWithoutLinks, rootWithoutLinks.getRequestContext().getResponseDocument());
                assertEquals(3, rootWithoutLinks.getProcessIntProperty());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testConstValueDescriptor() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            componentClassBuilder.configure(new Configuration(new Properties()));
            ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootnullvalue", RootComponent.class);
            ComponentDescriptor constValueComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "lengthconstvalue", LengthComponent.class);
            rootComponentDescriptor.setChildren(new ComponentDescriptor[]{constValueComponentDescriptor});
            constValueComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new ConstantValueDescriptor("2"), new PropertyDescriptor(constValueComponentDescriptor, "length"))});
            RootComponent rootConstValue = (RootComponent)rootComponentDescriptor.createComponent(null);
            LengthComponent lc = (LengthComponent)rootConstValue.getChildren()[0];
            assertEquals(2, lc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testNullValue() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            componentClassBuilder.configure(new Configuration(new Properties()));
            ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootconstvalue", RootComponent.class);
            ComponentDescriptor nullValueComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "componentvalue", ValueComponent.class);
            rootComponentDescriptor.setChildren(new ComponentDescriptor[]{nullValueComponentDescriptor});
            nullValueComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new ConstantValueDescriptor(null), new PropertyDescriptor(nullValueComponentDescriptor, "componentValue"))});
            RootComponent rootConstValue = (RootComponent)rootComponentDescriptor.createComponent(null);
            ValueComponent vc = (ValueComponent)rootConstValue.getChildren()[0];
            assertEquals(null, vc.getComponentValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testPropertyArrayDescriptor() {
        try {
            ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("test");
            componentClassBuilder.configure(new Configuration(new Properties()));
            {
                ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootpropertyarray1", RootComponent.class);
                ComponentDescriptor propertyArrayComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "propertyarray1", PropertyArrayComponent.class);
                rootComponentDescriptor.setChildren(new ComponentDescriptor[]{propertyArrayComponentDescriptor});
                propertyArrayComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertiesArrayDescriptor(new LinkSide[]{
                        new PropertyDescriptor(rootComponentDescriptor, "primitiveIntValue"),
                        new PropertyDescriptor(rootComponentDescriptor, "processIntProperty")
                }), new PropertyDescriptor(propertyArrayComponentDescriptor, "intValues"))});
                RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
                root.setPrimitiveIntValue(1);
                root.setProcessIntProperty(2);
                PropertyArrayComponent pac = (PropertyArrayComponent)root.getChildren()[0];
                assertEquals(2, pac.getIntValues().length);
                assertEquals(1, pac.getIntValues()[0]);
                assertEquals(2, pac.getIntValues()[1]);
            }

            {
                ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootpropertyarray2", RootComponent.class);
                ComponentDescriptor propertyArrayComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "propertyarray2", PropertyArrayComponent.class);
                rootComponentDescriptor.setChildren(new ComponentDescriptor[]{propertyArrayComponentDescriptor});
                propertyArrayComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertiesArrayDescriptor(new LinkSide[]{
                        new PropertyDescriptor(rootComponentDescriptor, "primitiveIntValue"),
                        new ConstantValueDescriptor("5")
                }), new PropertyDescriptor(propertyArrayComponentDescriptor, "intValues"))});
                RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
                root.setPrimitiveIntValue(3);
                root.setProcessIntProperty(4);
                PropertyArrayComponent pac = (PropertyArrayComponent)root.getChildren()[0];
                assertEquals(2, pac.getIntValues().length);
                assertEquals(3, pac.getIntValues()[0]);
                assertEquals(5, pac.getIntValues()[1]);
            }

            {
                ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootpropertyarray3", RootComponent.class);
                ComponentDescriptor propertyArrayComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "propertyarray3", PropertyArrayComponent.class);
                rootComponentDescriptor.setChildren(new ComponentDescriptor[]{propertyArrayComponentDescriptor});
                propertyArrayComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertiesArrayDescriptor(new LinkSide[]{
                        new ConstantValueDescriptor("8"),
                        new PropertyDescriptor(rootComponentDescriptor, "processIntProperty")
                }), new PropertyDescriptor(propertyArrayComponentDescriptor, "intValues"))});
                RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
                root.setPrimitiveIntValue(6);
                root.setProcessIntProperty(7);
                PropertyArrayComponent pac = (PropertyArrayComponent)root.getChildren()[0];
                assertEquals(2, pac.getIntValues().length);
                assertEquals(8, pac.getIntValues()[0]);
                assertEquals(7, pac.getIntValues()[1]);
            }

            {
                ComponentDescriptor rootComponentDescriptor = new DocumentDescriptor(componentClassBuilder, "rootpropertyarray4", RootComponent.class);
                ComponentDescriptor propertyArrayComponentDescriptor = new ComponentDescriptor(componentClassBuilder, "propertyarray4", PropertyArrayComponent.class);
                rootComponentDescriptor.setChildren(new ComponentDescriptor[]{propertyArrayComponentDescriptor});
                propertyArrayComponentDescriptor.setLinks(new LinkDescriptor[]{new LinkDescriptor(new PropertiesArrayDescriptor(new LinkSide[]{
                        new ConstantValueDescriptor("11"),
                        new ConstantValueDescriptor("12")
                }), new PropertyDescriptor(propertyArrayComponentDescriptor, "intValues"))});
                RootComponent root = (RootComponent)rootComponentDescriptor.createComponent(null);
                root.setPrimitiveIntValue(9);
                root.setProcessIntProperty(10);
                PropertyArrayComponent pac = (PropertyArrayComponent)root.getChildren()[0];
                assertEquals(2, pac.getIntValues().length);
                assertEquals(11, pac.getIntValues()[0]);
                assertEquals(12, pac.getIntValues()[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComplexPropertyValueDescriptor() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/RootComponent", "<html><body><div style='display: ${$booleanValue ? \"block\" : \"none\"}'></div></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/RootComponent");
            RootComponent doc = (RootComponent)cd.createComponent(null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComplexPropertyDescriptor() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/RootComponent", "<html><body><div cid=\"Condition\" condition='${$booleanValue && \"aaa\".length() > 2 ? true : false}'>aaa</div></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/RootComponent");
            RootComponent doc = (RootComponent)cd.createComponent(null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testArrayPropertyWithComplexProperty() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/RootComponent", "<html><body><div cid=\"/ru/albemuth/frontend/metadata/PropertyArrayComponent\" intValues='{$primitiveIntValue, ${\"aaa\".length() > 2 ? $primitiveIntValue : $processIntProperty}}'>aaa</a></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/RootComponent");
            RootComponent doc = (RootComponent)cd.createComponent(null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

class RootComponent extends Document {

    private String componentName;
    private String componentValue;
    private boolean booleanValue;
    private int primitiveIntValue;
    private Integer objectiveIntValue;
    private Date dateValue;
    private int processIntProperty;
    private Integer processIntegerProperty;

    public RootComponent(String name) {
        super(name);
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(String componentValue) {
        this.componentValue = componentValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public int getPrimitiveIntValue() {
        return primitiveIntValue;
    }

    public void setPrimitiveIntValue(int primitiveIntValue) {
        this.primitiveIntValue = primitiveIntValue;
    }

    public Integer getObjectiveIntValue() {
        return objectiveIntValue;
    }

    public void setObjectiveIntValue(Integer objectiveIntValue) {
        this.objectiveIntValue = objectiveIntValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public int getProcessIntProperty() {
        return processIntProperty;
    }

    public void setProcessIntProperty(int processIntProperty) {
        this.processIntProperty = processIntProperty;
    }

    public Integer getProcessIntegerProperty() {
        return processIntegerProperty;
    }

    public void setProcessIntegerProperty(Integer processIntegerProperty) {
        this.processIntegerProperty = processIntegerProperty;
    }

    public Document incIntProperty() {
        this.processIntProperty++;
        return null;
    }

    protected void renderContent() {}

}

abstract class NameComponent extends Component {

    public NameComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract String getComponentName();
    public abstract void setComponentName(String componentName);

    public void render() throws RenderException {
        String out = getRequestContext().getParameterValue("out");
        out += "name: " + getComponentName() +"\n";
        getRequestContext().setParameterValue("out", out);
        super.render();
    }
}

abstract class ValueComponent extends Component {

    public ValueComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract String getComponentValue();
    public abstract void setComponentValue(String componentValue);

    public void render() throws RenderException {
        String out = getRequestContext().getParameterValue("out");
        out += "value: " + getComponentValue() +"\n";
        getRequestContext().setParameterValue("out", out);
        super.render();
    }

}

abstract class BooleanComponent extends Component {

    public BooleanComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract boolean isBooleanValue();
    public abstract void setBooleanValue(boolean value);

    public void render() {
        String out = getRequestContext().getParameterValue("out");
        out += "boolean value: " + isBooleanValue() +"\n";
        getRequestContext().setParameterValue("out", out);
    }

}

abstract class LengthComponent extends Component {

    public LengthComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract int getLength();

    public void render() {
        String out = getRequestContext().getParameterValue("out");
        out += "length: " + getLength() +"\n";
        getRequestContext().setParameterValue("out", out);
    }

}

abstract class ContextComponent extends Component {

    private RequestContext requestContext;

    {
        try {
            requestContext = new TRequestContext(getDocument(), new TestFrontend().createHttpServletRequest(getDocument()), new TestFrontend().createHttpServletResponse()) {
                public String getParameterValue(String name) {
                    if (name.contains("requestContextProperty")) {
                        return "requestContextProperty";
                    } else if (name.contains("primitiveIntValue")) {
                        return "1";
                    } else if (name.contains("objectiveIntValue")) {
                        return "2";
                    } else if (name.contains("dateValue")) {
                        return "2007-12-04 00:00:00";
                    } else {
                        return null;
                    }
                }
            };
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    private ProcessContext processContext = new ProcessContext() {
        public Object getParameterValue(String name) {
            if (name.contains("processIntProperty")) {
                return 3;
            } else if (name.contains("processIntegerProperty")) {
                return 4;
            } else {
                return null;
            }
        }
    };
    private SessionContext sessionContext = new SessionContext();
    private ApplicationContext applicationContext = new ApplicationContext();

    public ContextComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public ProcessContext getProcessContext() {
        return processContext;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public abstract String getRequestContextProperty();

    @ContextProperty(type = ContextType.REQUEST)
    public abstract void setRequestContextProperty(String requestContextProperty);

    public abstract int getPrimitiveIntValue();

    @ContextProperty(type = ContextType.REQUEST)
    public abstract void setPrimitiveIntValue(int primitiveIntValue);

    public abstract Integer getObjectiveIntValue();

    @ContextProperty(type = ContextType.REQUEST)
    public abstract void setObjectiveIntValue(Integer objectiveIntValue);

    public abstract Date getDateValue();

    @ContextProperty(type = ContextType.REQUEST)
    public abstract void setDateValue(Date dateValue);

    public abstract int getProcessIntProperty();

    @ContextProperty(type = ContextType.PROCESS)
    public abstract void setProcessIntProperty(int processIntProperty);

    public abstract Integer getIntegerProperty();

    @ContextProperty(type = ContextType.PROCESS, contextName = "processIntegerProperty")
    public abstract void setIntegerProperty(Integer integerProperty);

    protected Date parseDateValueValue(String value) throws ConvertorException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            throw new ConvertorException("Can't parse date value " + value, e);
        }
    }

}

abstract class SimpleActionComponent extends ActionComponent {

    public SimpleActionComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract int getIntProperty();

    public abstract void setIntProperty(int intProperty);

    public void processEvents() throws EventException {
        Document ret = processAction();
        if (ret != null) {
            getRequestContext().setResponseDocument(ret, Engine.ResponseType.FORWARD);
        }
    }

}

abstract class PropertyArrayComponent extends Component {

    public PropertyArrayComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract int[] getIntValues();

    public abstract void setIntValues(int[] intValues);

}