package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.test.HTTPServletResponse;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestNestedContent extends TestFrontend {

    public void testSimpleNestedContent() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TNestedDocument", "<div cid=\"/ru/albemuth/frontend/components/TContent1\">aaa</div>");
            parser.setComponentContent("/ru/albemuth/frontend/components/TContent1", "<div><div cid=\"NestedContent\" /></div>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TNestedDocument");
            TNestedDocument doc = (TNestedDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            HTTPServletResponse response = createHttpServletResponse();
            engine.service(request, response);
            assertEquals("<div  >aaa</div>", response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testTwoLevelSimpleNestedContent() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TNestedDocument", "<div cid=\"/ru/albemuth/frontend/components/TContent2\">aaa</div>");
            parser.setComponentContent("/ru/albemuth/frontend/components/TContent1", "<div><div cid=\"NestedContent\" /></div>");
            parser.setComponentContent("/ru/albemuth/frontend/components/TContent2", "<div cid=\"/ru/albemuth/frontend/components/TContent1\"><div cid=\"NestedContent\" /></div>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TNestedDocument");
            TNestedDocument doc = (TNestedDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            HTTPServletResponse response = createHttpServletResponse();
            engine.service(request, response);
            assertEquals("<div  >aaa</div>", response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testTwoLevelComplexNestedContent() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TNestedDocument", "<div cid=\"/ru/albemuth/frontend/components/TContent2\">$title</div>");
            parser.setComponentContent("/ru/albemuth/frontend/components/TContent1", "<div><div cid=\"NestedContent\" /></div>");
            parser.setComponentContent("/ru/albemuth/frontend/components/TContent2", "<div cid=\"/ru/albemuth/frontend/components/TContent1\"><div cid=\"NestedContent\" /></div>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TNestedDocument");
            TNestedDocument doc = (TNestedDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            HTTPServletResponse response = createHttpServletResponse();
            engine.service(request, response);
            assertEquals("<div  >aaa</div>", response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
abstract class TNestedDocument extends Document {

    public TNestedDocument(String name) {
        super(name);
    }

    public String getTitle() {
        return "aaa";
    }

}

abstract class TContent1 extends Component {

    public TContent1(String name, Component parent, Document document) {
        super(name, parent, document);
    }

}

abstract class TContent2 extends Component {

    public TContent2(String name, Component parent, Document document) {
        super(name, parent, document);
    }

}
