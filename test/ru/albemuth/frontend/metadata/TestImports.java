package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.components.TComponentParser;
import ru.albemuth.frontend.components.TEngine;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestImports extends TestFrontend {

    public void testComponentNameWithPathInImports() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TImportsDocument", "<html><body><span cid=\"/ru/albemuth/frontend/components/Label\" value=\"aaa\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TImportsDocument");
            TImportsDocument doc = (TImportsDocument)cd.createComponent(null);

            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span  >aaa</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComponentNameWithoutPathInImports() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TImportsDocument", "<html><body><span cid=\"Label\" value=\"aaa\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TImportsDocument");
            TImportsDocument doc = (TImportsDocument)cd.createComponent(null);

            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span  >aaa</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComponentNameWithPathNotInImports() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TImportsDocument", "<html><body><span cid=\"/ru/albemuth/frontend/metadata/MetadataLabel\" value=\"aaa\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TImportsDocument");
            TImportsDocument doc = (TImportsDocument)cd.createComponent(null);

            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span  >aaa</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComponentNameWithoutPathNotInImports() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TImportsDocument", "<html><body><span cid=\"MetadataLabel\" value=\"aaa\" /></body></html>");
            try {
                parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TImportsDocument");
                fail();
            } catch (MetadataException e) {
                assertEquals("Can't found class for component MetadataLabel", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TImportsDocument extends Document {

    public TImportsDocument(String name) {
        super(name);
    }

}