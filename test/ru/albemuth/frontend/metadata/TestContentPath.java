package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.components.TComponentParser;

public class TestContentPath extends TestFrontend {

    public void test() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            assertEquals("/ru/albemuth/frontend/metadata/ContentPath.content", parser.getComponentContentPath(TContentPathDocument.class));
            assertEquals("/ru/albemuth/frontend/metadata/TComplexPropertiesDocument.content", parser.getComponentContentPath(TComplexPropertiesDocument.class));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

@ContentFile(path = "/ru/albemuth/frontend/metadata/ContentPath.content")
abstract class TContentPathDocument extends Document {

    public TContentPathDocument(String name) {
        super(name);
    }

}