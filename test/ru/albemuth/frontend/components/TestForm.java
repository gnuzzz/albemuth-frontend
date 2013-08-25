package ru.albemuth.frontend.components;

import org.apache.log4j.Logger;
import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Convertor;
import ru.albemuth.util.ConvertorException;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestForm extends TestFrontend {

    public void testSimpleListener() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormDocument",
                    "<html>" +
                    "<body>" +
                    "$selectedValue.value<br />" +
                    "<form cid=\"Form\" listener=\"setSelectedValue1\" response=\"FORWARD\" name=\"ssv1\">" +
                    "<input type=\"text\" name=\"selected-value\" value=\"\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TFormDocument");
            TFormDocument doc = (TFormDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("ssv1", "");
            request.mockSetParameter("selected-value", "2");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<html><body>2<br /><form action=\"/" + doc.getName() + "\" method=\"post\" name=\"ssv1\"  >") != -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testItemListener() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormDocument",
                    "<html>" +
                    "<body>" +
                    "$selectedValue.value<br />" +
                    "<span cid=\"Repetition\" array=\"$values\" item=\"$valueItem\">" +
                    "<form cid=\"Form\" listener=\"setSelectedValue2\" response=\"FORWARD\" name=\"ssv1\">" +
                    "<input type=\"text\" name=\"selected-value\" value=\"$valueItem.value\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TFormDocument");
            TFormDocument doc = (TFormDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("ssv1", "");
            request.mockSetParameter("itemid", "3");
            request.mockSetParameter("selected-value", "4");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<html><body>3<br /><form action=\"/" + doc.getName() + "\" method=\"post\" name=\"ssv1\"  >") != -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TFormDocument extends Document {

    private static final Logger LOG             = Logger.getLogger(TFormDocument.class);

    private Value[] values = {new Value(1), new Value(2), new Value(3), new Value(4), new Value(5)};
    private Value selectedValue = Value.DEFAULT;
    private Value valueItem;

    public TFormDocument(String name) {
        super(name);
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

    public void release() throws ReleaseException {
        super.release();
        selectedValue = values != null && values.length > 0 ? values[0] : Value.DEFAULT;
    }

    public Document setSelectedValue1() {
        try {
            int value = Convertor.parseIntValue(getRequestContext().getParameterValue("selected-value"), 0);
            for (Value v: values) {
                if (v.getValue() == value) {
                    selectedValue = v;
                    break;
                }
            }
        } catch (ConvertorException e) {
            LOG.error("Invalid value " + getRequestContext().getParameterValue("selected-value"), e);
        }
        return null;
    }

    public Document setSelectedValue2() {
        selectedValue = valueItem;
        return null;
    }

}