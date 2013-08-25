package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.components.TComponentParser;
import ru.albemuth.frontend.components.TEngine;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestComplexProperties extends TestFrontend {

    public void testProperty() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TComplexPropertiesDocument", "<if condition=\"${$valid && getValue().length() > 0}\">aaa</if>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TComplexPropertiesDocument");
            TComplexPropertiesDocument doc = (TComplexPropertiesDocument)dd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("", doc.getRequestContext().getResponse().toString());

            doc.setValid(true);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("aaa", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testArrayProperty() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TComplexPropertiesDocument", "<foreach array=\"{${getValue1()}, ${getValue2()}}\" item=\"$valueItem\">$valueItem</if>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TComplexPropertiesDocument");
            TComplexPropertiesDocument doc = (TComplexPropertiesDocument)dd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("value1value2", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCodeOnlyProperty() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TComplexPropertiesDocument", "<if condition=\"${isValid() > 0}\">aaa</if>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TComplexPropertiesDocument");
            TComplexPropertiesDocument doc = (TComplexPropertiesDocument)dd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("", doc.getRequestContext().getResponse().toString());

            doc.setValid(true);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("aaa", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

