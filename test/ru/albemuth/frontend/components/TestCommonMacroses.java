package ru.albemuth.frontend.components;

import org.junit.Test;
import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.DocumentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestCommonMacroses extends TestFrontend {

    public void test() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TCommonMacrosesDocument", "<html><body><if condition=\"$condition\">aaa</if><span raw_attr=\"$styleString\"><foreach array=\"$values\" item=\"$valueItem\">$valueItem</foreach></span></body></html>");
            DocumentDescriptor dd = parser.getDocumentDescriptor("TCommonMacrosesDocument");
            TCommonMacrosesDocument doc = (TCommonMacrosesDocument)dd.createComponent(null);
            doc.setCondition(false);
            doc.setValues(new String[]{"1", "2", "c"});
            doc.setStyleString("style=\"display: none;\"");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);

            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span style=\"display: none;\">12c</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TCommonMacrosesDocument extends Document {

    private boolean condition;
    private String[] values;
    private String valueItem;
    private String styleString;

    public TCommonMacrosesDocument(String name) {
        super(name);
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String getValueItem() {
        return valueItem;
    }

    public void setValueItem(String valueItem) {
        this.valueItem = valueItem;
    }

    public String getStyleString() {
        return styleString;
    }

    public void setStyleString(String styleString) {
        this.styleString = styleString;
    }

}