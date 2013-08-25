package ru.albemuth.frontend.components;

import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestHyperlink extends TestFrontend {

    public void testListener() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/THyperlinkDocument", "<html><body><a cid=\"Hyperlink\" name=\"trigger\" listener=\"triggerValue\" class=\"hyperlink\">$value</a></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("THyperlinkDocument");
            THyperlinkDocument doc = (THyperlinkDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName() + "?trigger");
            engine.service(request, createHttpServletResponse());
            assertEquals("<html><body><a href=\"/" + doc.getName() +"?trigger\" class=\"hyperlink\" >true</a></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testHref() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/THyperlinkDocument", "<html><body>$selectedValue.value<br /><span cid=\"Repetition\" array=\"$values\" item=\"$valueItem\"><a cid=\"Hyperlink\" href=\"/aaa/bbb/ccc/?\" class=\"hyperlink\">$valueItem.value</a></span></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("THyperlinkDocument");
            THyperlinkDocument doc = (THyperlinkDocument)cd.createComponent(null);
            doc.setValues(new Value[]{new Value(1), new Value(2), new Value(3), new Value(4), new Value(5)});
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName() + "?setSelectedValue&itemid=" + doc.getValues()[1].hashCode());
            engine.service(request, createHttpServletResponse());
            assertEquals("<html><body>1<br /><a href=\"/aaa/bbb/ccc/?itemid=1\" class=\"hyperlink\" >1</a><a href=\"/aaa/bbb/ccc/?itemid=2\" class=\"hyperlink\" >2</a><a href=\"/aaa/bbb/ccc/?itemid=3\" class=\"hyperlink\" >3</a><a href=\"/aaa/bbb/ccc/?itemid=4\" class=\"hyperlink\" >4</a><a href=\"/aaa/bbb/ccc/?itemid=5\" class=\"hyperlink\" >5</a></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testRepetitionLinksNativeItemid() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/THyperlinkDocument", "<html><body>$selectedValue.value<br /><span cid=\"Repetition\" array=\"$values\" item=\"$valueItem\"><a cid=\"Hyperlink\" name=\"setSelectedValue\" listener=\"setSelectedValue\" class=\"hyperlink\">$valueItem.value</a></span></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("THyperlinkDocument");
            THyperlinkDocument doc = (THyperlinkDocument)cd.createComponent(null);
            doc.setValues(new Value[]{new Value(1), new Value(2), new Value(3), new Value(4), new Value(5)});
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName() + "?setSelectedValue&itemid=" + doc.getValues()[1].hashCode());
            engine.service(request, createHttpServletResponse());
            assertEquals("<html><body>2<br /><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=1\" class=\"hyperlink\" >1</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=2\" class=\"hyperlink\" >2</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=3\" class=\"hyperlink\" >3</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=4\" class=\"hyperlink\" >4</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=5\" class=\"hyperlink\" >5</a></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testRepetitionLinksCustomItemid() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/THyperlinkDocument", "<html><body>$selectedValue.value<br /><span cid=\"Repetition\" array=\"$values\" item=\"$valueItem\"><a cid=\"Hyperlink\" name=\"setSelectedValue\" itemid=\"$valueItem.parameterValue\" listener=\"setSelectedValue\" class=\"hyperlink\">$valueItem.value</a></span></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("THyperlinkDocument");
            THyperlinkDocument doc = (THyperlinkDocument)cd.createComponent(null);
            doc.setValues(new Value[]{new Value(1), new Value(2), new Value(3), new Value(4), new Value(5)});
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName() + "?setSelectedValue&itemid=" + doc.getValues()[2].hashCode());
            engine.service(request, createHttpServletResponse());
            assertEquals("<html><body>3<br /><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=1\" class=\"hyperlink\" >1</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=2\" class=\"hyperlink\" >2</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=3\" class=\"hyperlink\" >3</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=4\" class=\"hyperlink\" >4</a><a href=\"/" + doc.getName() + "?setSelectedValue&itemid=5\" class=\"hyperlink\" >5</a></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testRepetitionLinksNullItemidWithParameters() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/THyperlinkDocument", "<html><body>$selectedValue.value<br /><span cid=\"Repetition\" array=\"$values\" item=\"$valueItem\"><a cid=\"Hyperlink\" name=\"setSelectedValue\" itemid=\"$null\" listener=\"setValueSelected\" parameters=\"{$valueItem}\" class=\"hyperlink\">$valueItem.value</a></span></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("THyperlinkDocument");
            THyperlinkDocument doc = (THyperlinkDocument)cd.createComponent(null);
            doc.setValues(new Value[]{new Value(1), new Value(2), new Value(3), new Value(4), new Value(5)});
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName() + "?setSelectedValue&value=" + doc.getValues()[3].hashCode());
            engine.service(request, createHttpServletResponse());
            assertEquals("<html><body>4<br /><a href=\"/" + doc.getName() + "?setSelectedValue&value=1\" class=\"hyperlink\" >1</a><a href=\"/" + doc.getName() + "?setSelectedValue&value=2\" class=\"hyperlink\" >2</a><a href=\"/" + doc.getName() + "?setSelectedValue&value=3\" class=\"hyperlink\" >3</a><a href=\"/" + doc.getName() + "?setSelectedValue&value=4\" class=\"hyperlink\" >4</a><a href=\"/" + doc.getName() + "?setSelectedValue&value=5\" class=\"hyperlink\" >5</a></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class THyperlinkDocument extends Document {

    private boolean value;
    private Value[] values;
    private Value selectedValue = Value.DEFAULT;
    private Value valueItem;

    public THyperlinkDocument(String name) {
        super(name);
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }

    public Value[] getValues() {
        return values;
    }

    public Value getSelectedValue() {
        return selectedValue;
    }

    public Value getValueItem() {
        return valueItem;
    }

    public void setValueItem(Value valueItem) {
        this.valueItem = valueItem;
    }

    public Document triggerValue() {
        value = !value;
        return null;
    }

    public Document setSelectedValue() {
        selectedValue = getValueItem();
        return null;
    }

    public Document setValueSelected() throws EventException {
        String v = getRequestContext().getParameterValue("value");
        try {
            int i = Integer.parseInt(v);
            if (v != null) {
                for (Value value: getValues()) {
                    if (value.getValue() == i) {
                        selectedValue = value;
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new EventException("Wrong value format: " + v);
        }
        return null;
    }

    public void setContextProperties() throws RequestException {
        selectedValue = getValues() != null && getValues().length > 0 ? getValues()[0] : selectedValue;
        super.setContextProperties();
    }

}

