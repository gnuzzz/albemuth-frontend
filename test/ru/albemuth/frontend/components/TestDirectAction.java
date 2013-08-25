package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 07.03.2008
 * Time: 1:17:10
 */
public class TestDirectAction extends TestFrontend {

    public void test() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TDirectActionDocument", "<html><head><meta cid=\"DirectAction\" listener=\"triggerValue\" /></head><body>$value</body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TDirectActionDocument");
            TDirectActionDocument doc = (TDirectActionDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName() + "?triggerValue");
            engine.service(request, createHttpServletResponse());
            assertEquals("<html><head></head><body>true</body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TDirectActionDocument extends Document {

    private boolean value;

    public TDirectActionDocument(String name) {
        super(name);
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Document triggerValue() {
        value = !value;
        return null;
    }
    
}