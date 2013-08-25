package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestCDATA extends TestFrontend {

    public void testCDATA() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TCDATADocument", "<html><body><![CDATA[<noindex>]]></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TCDATADocument");
            TCDATADocument doc = (TCDATADocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><![CDATA[<noindex>]]></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCDATAComplex() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TCDATADocument", "<html><body><![CDATA[<noindex>]]><a href=\"http://aaa.com/\"></a><![CDATA[</noindex>]]></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TCDATADocument");
            TCDATADocument doc = (TCDATADocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><![CDATA[<noindex>]]><a href=\"http://aaa.com/\"></a><![CDATA[</noindex>]]></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TCDATADocument extends Document {

    private String value;

    public TCDATADocument(String name) {
        super(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}