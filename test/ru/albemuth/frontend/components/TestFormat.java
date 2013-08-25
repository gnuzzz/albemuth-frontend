package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.components.format.Format;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestFormat extends TestFrontend {

    public void testSimple() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormatComponent", "<span cid=\"Label\" value=\"$value\" class=\"label\" format=\"$labelFormat\" />");
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormatDocument", "<html><head><title>testSimple</title></head><body><span cid=\"TFormatComponent\" value=\"$value\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TFormatDocument");
            TFormatDocument doc = (TFormatDocument)cd.createComponent(null);
            doc.setValue(2);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><head><title>testSimple</title></head><body><span class=\"label\" >2 дня</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testSimple2() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormatComponent", "<span cid=\"Label\" value=\"$name.length\" class=\"label\" format=\"$labelFormat\" />");
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormatDocument", "<html><head><title>testSimple2</title></head><body><span cid=\"TFormatComponent\" value=\"$value\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TFormatDocument");
            TFormatDocument doc = (TFormatDocument)cd.createComponent(null);
            doc.setValue(2);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><head><title>testSimple2</title></head><body><span class=\"label\" >3 дня</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComplex() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormatComponent", "<span cid=\"Label\" value=\'${$value == 0 ? 0 : $value}' class=\"label\" format=\"$labelFormat\" />");
            parser.setComponentContent("/ru/albemuth/frontend/components/TFormatDocument", "<html><head><title>testComplex</title></head><body><span cid=\"TFormatComponent\" value=\"$value\" /></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TFormatDocument");
            TFormatDocument doc = (TFormatDocument)cd.createComponent(null);
            doc.setValue(2);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><head><title>testComplex</title></head><body><span class=\"label\" >2 дня</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TFormatComponent extends Component {

    public TFormatComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract int getValue();

    public String getName() {
        return "aaa";
    }

    public Format getLabelFormat() {
        return ru.albemuth.frontend.components.format.DaysLabelFormat.INSTANCE;
    }

}

abstract class TFormatDocument extends Document {

    private int value;

    public TFormatDocument(String name) {
        super(name);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}