package ru.albemuth.frontend.components;

import javassist.ClassPool;
import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 29.12.2007
 * Time: 2:36:22
 */
public class TestRepetition extends TestFrontend {

    public void testList() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TRepetitionDocument", "<html><body><span cid=\"Repetition\" items=\"$valuesList\" item=\"$valueItem\"><span cid=\"Label\" value=\"$valueItem\" class=\"repetition\" /></span></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TRepetitionDocument");
            TRepetitionDocument doc = (TRepetitionDocument)cd.createComponent(null);
            List<String> values = new ArrayList<String>();
            values.add("123");
            values.add("456");
            values.add("789");
            doc.setValuesList(values);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span class=\"repetition\" >123</span><span class=\"repetition\" >456</span><span class=\"repetition\" >789</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testArray() {
        try {

            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TRepetitionDocument", "<html><body><span cid=\"Repetition\" array=\"$values\" item=\"$valueItem\"><span cid=\"Label\" value=\"$valueItem\" class=\"label\" /></span></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TRepetitionDocument");
            TRepetitionDocument doc = (TRepetitionDocument)cd.createComponent(null);
            String[] values = {
                    "987",
                    "654",
                    "321"
            };
            doc.setValues(values);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body><span class=\"label\" >987</span><span class=\"label\" >654</span><span class=\"label\" >321</span></body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testIndex() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TRepetitionDocument", "<html><body><span cid=\"Repetition\" array=\"$values\" item=\"$valueItem\" index=\"$valueIndex\">$value</span></body></html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TRepetitionDocument");
            TRepetitionDocument doc = (TRepetitionDocument)cd.createComponent(null);
            String[] values = {
                    "1",
                    "2",
                    "3"
            };
            doc.setValues(values);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html><body>1, 2, 3</body></html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TRepetitionDocument extends Document {

    private List<String> valuesList;
    private String[] values;
    private String valueItem;
    private int valueIndex;

    public TRepetitionDocument(String name) {
        super(name);
    }

    public List<String> getValuesList() {
        return valuesList;
    }

    public void setValuesList(List<String> valuesList) {
        this.valuesList = valuesList;
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

    public int getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        this.valueIndex = valueIndex;
    }

    public String getValue() {
        return ((getValueIndex() != 0) ? ", " : "") + getValueItem();
    }

}
