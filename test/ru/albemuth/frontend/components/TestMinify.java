package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

/**
 * User: -
 * Date: 29.06.2010
 * Time: 20:38:03
 */
public class TestMinify extends TestFrontend {

    public void testMinify() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            Properties props = new Properties();
            props.put("minify-html", "true");
            parser.configure(new Configuration(props));
            parser.setComponentContent("/ru/albemuth/frontend/components/TMinifyDocument", "<html>\n" +
                    "\t\t<body>\n" +
                    "aaa  bb ccc \n" +
                    "\t\t\t\t<pre>" +
                    "  ccc  $value    eee \n" +
                    "  ffff\n" +
                    "\t\t\t\t</pre>\n" +
                    "\t\t</body>\n" +
                    "</html>");
            ComponentDescriptor cd = parser.getDocumentDescriptor("TMinifyDocument");
            TMinifyDocument doc = (TMinifyDocument)cd.createComponent(null);
            doc.setValue("Value123");
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            engine.service(createHttpServletRequest(doc), createHttpServletResponse());
            assertEquals("<html>\n" +
                    "<body>\n" +
                    "aaa bb ccc\n" +
                    "<pre>" +
                    "  ccc  Value123    eee \n" +
                    "  ffff\n" +
                    "\t\t\t\t</pre>\n" +
                    "</body>\n" +
                    "</html>", doc.getRequestContext().getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TMinifyDocument extends Document {

    private String value;

    public TMinifyDocument(String name) {
        super(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}