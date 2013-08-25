package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestValueDocument extends TestFrontend {

    public void test() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TValueDocument", "<html><head><meta cid=\"DirectAction\" listener=\"valueDocumentAction\" /></head><body>aaa</body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TValueDocument");
            TValueDocument doc = (TValueDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName() + "?valueDocumentAction");
            engine.service(request, createHttpServletResponse());
            assertEquals("<html><body>value document</body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TValueDocument extends Document {

    private boolean value;

    public TValueDocument(String name) {
        super(name);
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Document valueDocumentAction() {
        return new ValueDocument(getRequestContext(), "<html><body>value document</body></html>");
    }

}