package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.test.HTTPServletResponse;
import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.components.TComponentParser;
import ru.albemuth.frontend.components.TEngine;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestCovariantReturnTypes extends TestFrontend {

    public void test() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TCovariantReturnTypesDocument", "<div cid=\"/ru/albemuth/frontend/metadata/TCovariantReturnTypesComponent\" />");
            parser.setComponentContent("/ru/albemuth/frontend/metadata/TCovariantReturnTypesComponent", "$document.title");
            ComponentDescriptor cd = parser.getDocumentDescriptor("/ru/albemuth/frontend/metadata/TCovariantReturnTypesDocument");
            TCovariantReturnTypesDocument doc = (TCovariantReturnTypesDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            HTTPServletResponse response = createHttpServletResponse();
            engine.service(request, response);
            assertEquals("covariant", response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TCovariantReturnTypesDocument extends Document {

    public TCovariantReturnTypesDocument(String name) {
        super(name);
    }

    public String getTitle() {
        return "covariant";
    }

}

abstract class TCovariantReturnTypesComponent extends Component {

    public TCovariantReturnTypesComponent(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public TCovariantReturnTypesDocument getDocument() {
        return (TCovariantReturnTypesDocument)super.getDocument();
    }

}