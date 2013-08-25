package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestEmptyTags extends TestFrontend {

    public void testEmptyTagWithEndTag() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TEmptyTagDocument", "<html></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TEmptyTagDocument");
            TEmptyTagDocument doc = (TEmptyTagDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testEmptyTagWithoutEndTag() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TEmptyTagDocument", "<html />");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TEmptyTagDocument");
            TEmptyTagDocument doc = (TEmptyTagDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html />", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testInnerEmptyTagWithEndTag() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TEmptyTagDocument", "<html><body></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TEmptyTagDocument");
            TEmptyTagDocument doc = (TEmptyTagDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testInnerEmptyTagWithoutEndTag() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TEmptyTagDocument", "<html><body /></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TEmptyTagDocument");
            TEmptyTagDocument doc = (TEmptyTagDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body /></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testInnerBr() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TEmptyTagDocument", "<html><body><br></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TEmptyTagDocument");
            TEmptyTagDocument doc = (TEmptyTagDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><br></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testLinkComponent() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TEmptyTagAnchor", "<a href=\"$link\" $titleString><span cid=\"NestedContent\" /></a>");
            parser.setComponentContent("/ru/albemuth/frontend/components/TEmptyTagDocument", "<html><body><a cid=\"TEmptyTagAnchor\" value=\"$value\">Value</a></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TEmptyTagDocument");
            TEmptyTagDocument doc = (TEmptyTagDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><a href=\"http://localhost/Value123\" title=\"ttt\"  >Value</a></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TEmptyTagDocument extends Document {

    private String value;

    public TEmptyTagDocument(String name) {
        super(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

abstract class TEmptyTagAnchor extends Component {

    public TEmptyTagAnchor(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract String getValue();

    public String getLink() {
        return "http://localhost/" + getValue();
    }

    public String getTitleString() {
        return "title=\"ttt\"";
    }

}