package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestLabel extends TestFrontend {

    public void test() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TLabelDocument", "<html><body><span cid=\"Label\" value=\"$value\" class=\"label\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TLabelDocument");
            TLabelDocument doc = (TLabelDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span class=\"label\" >Value123</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testTag() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TLabelDocument", "<html><body><p cid=\"Label\" tag=\"p\" value=\"$value\" class=\"label\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TLabelDocument");
            TLabelDocument doc = (TLabelDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><p class=\"label\" >Value123</p></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TLabelDocument extends Document {

    private String value;

    public TLabelDocument(String name) {
        super(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}