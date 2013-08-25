package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.DocumentDescriptor;
import ru.albemuth.frontend.test.HTTPServletResponse;
import ru.albemuth.util.Configuration;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.12.2007
 * Time: 1:23:50
 */
public class TestCondition extends TestFrontend {

    public void test() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            tCondition(parser);
            tNegate(parser);
            tMultipleCondition(parser);
            tObjectCondition(parser);
            tStringCondition(parser);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tCondition(TComponentParser parser) {
        try {
            parser.setComponentContent("/ru/albemuth/frontend/components/TConditionDocument", "<html><body><span cid=\"Condition\" condition=\"$condition\"><span cid=\"Label\" value=\"$value\" class=\"label\" /></span></body></html>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("TConditionDocument");
            TConditionDocument doc = (TConditionDocument)dd.createComponent(null);
            doc.setValue("Value345");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            doc.setCondition(false);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body></body></html>", doc.getRequestContext().getResponse().toString());

            doc.setCondition(true);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span class=\"label\" >Value345</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tNegate(TComponentParser parser) {
        try {
            parser.setComponentContent("/ru/albemuth/frontend/components/TConditionDocument", "<html><body><span cid=\"Condition\" condition=\"$condition\" negate=\"true\"><span cid=\"Label\" value=\"$value\" class=\"label\" /></span></body></html>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("TConditionDocument");
            TConditionDocument doc = (TConditionDocument)dd.createComponent(null);
            doc.setValue("Value567");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            doc.setCondition(false);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span class=\"label\" >Value567</span></body></html>", doc.getRequestContext().getResponse().toString());

            doc.setCondition(true);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tMultipleCondition(TComponentParser parser) {
        try {
            parser.setComponentContent("/ru/albemuth/frontend/components/TConditionDocument", "<html><body><span cid=\"Condition\" condition=\"$condition\"><span cid=\"Label\" value=\"$value\" class=\"condition\" /></span><span cid=\"Condition\" condition=\"$condition\" negate=\"true\"><span cid=\"Label\" value=\"$value\" class=\"negate\" /></span></body></html>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("TConditionDocument");
            TConditionDocument doc = (TConditionDocument)dd.createComponent(null);
            doc.setValue("Value890");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            doc.setCondition(false);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span class=\"negate\" >Value890</span></body></html>", doc.getRequestContext().getResponse().toString());

            doc.setCondition(true);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span class=\"condition\" >Value890</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tObjectCondition(TComponentParser parser) {
        try {
            parser.setComponentContent("/ru/albemuth/frontend/components/TConditionDocument", "<html><body><span cid=\"Condition\" condition=\"$value\"><span cid=\"Label\" value=\"$stringValue\" class=\"condition\" /></span></body></html>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("TConditionDocument");
            TConditionDocument doc = (TConditionDocument)dd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            engine.service(createHttpServletRequest(doc), createHttpServletResponse());

            doc.setValue("value");
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tStringCondition(TComponentParser parser) {
        try {
            parser.setComponentContent("/ru/albemuth/frontend/components/TConditionDocument", "<html><body><span cid=\"Condition\" condition=\"$stringValue\" negate=\"true\"><span cid=\"Label\" value=\"$stringValue\" class=\"condition\" /></span></body></html>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("TConditionDocument");
            TConditionDocument doc = (TConditionDocument)dd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            HTTPServletResponse response = createHttpServletResponse();
            engine.service(createHttpServletRequest(doc), response);
            assertEquals("<html><body><span class=\"condition\" ></span></body></html>", response.getContent());

            response = createHttpServletResponse();
            doc.setStringValue("");
            engine.service(createHttpServletRequest(doc), response);
            assertEquals("<html><body><span class=\"condition\" ></span></body></html>", response.getContent());

            response = createHttpServletResponse();
            doc.setStringValue("value");
            engine.service(createHttpServletRequest(doc), response);
            assertEquals("<html><body></body></html>", response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TConditionDocument extends Document {

    private boolean condition;
    private Object value;
    private String stringValue;

    public TConditionDocument(String name) {
        super(name);
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

}