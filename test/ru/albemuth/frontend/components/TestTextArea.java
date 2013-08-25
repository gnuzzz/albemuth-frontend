package ru.albemuth.frontend.components;

import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestTextArea extends TestFrontend {

    public void testOneFormWithOneTextArea() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TTextAreaDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"taf\">" +
                    "<textarea cid=\"TextArea\" name=\"selected-value\" value=\"$valueSelected.value\"></textarea>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TTextAreaDocument");
            TTextAreaDocument doc = (TTextAreaDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<textarea name=\"selected-value\"     >a</textarea>") != -1);
            assertEquals("a", doc.getValueSelected().getValue());

            request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("taf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value", "d");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<textarea name=\"selected-value\"     >d</textarea>") != -1);
            assertEquals("d", doc.getValueSelected().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testOneFormWithManyTextAreas() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TTextAreaDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"taf\">" +
                    "<span cid=\"Repetition\" array=\"$formValueSelected.values\" item=\"$valueItem\">" +
                    "<textarea cid=\"TextArea\" name=\"selected-value\" value=\"$valueItem.value\" itemid=\"$valueItem.value\"></textarea>" +
                    "</span>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TTextAreaDocument");
            TTextAreaDocument doc = (TTextAreaDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("taf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value.b", "d");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<textarea name=\"selected-value.d\"     >d</textarea>") != -1);
            assertEquals("a", doc.getFormValueSelected().getValues()[0].getValue());
            assertEquals("d", doc.getFormValueSelected().getValues()[1].getValue());
            assertEquals("c", doc.getFormValueSelected().getValues()[2].getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testManyFormWithOneTextArea() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TTextAreaDocument",
                    "<html>" +
                    "<body>" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\" itemid=\"$formValueItem.itemid\" >" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"taf\">" +
                    "<textarea cid=\"TextArea\" name=\"selected-value\" value=\"$valueItem.value\"></textarea>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TTextAreaDocument");
            TTextAreaDocument doc = (TTextAreaDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("taf", "");
            request.mockSetParameter("itemid", "2");
            request.mockSetParameter("sb.2", "Ok");
            request.mockSetParameter("selected-value.2", "3");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<textarea name=\"selected-value.2\"     >3</textarea>") != -1);
            assertEquals("a", doc.getFormValues()[0].getValues()[0].getValue());
            assertEquals("b", doc.getFormValues()[0].getValues()[1].getValue());
            assertEquals("c", doc.getFormValues()[0].getValues()[2].getValue());
            assertEquals("3", doc.getFormValues()[1].getValues()[0].getValue());
            assertEquals("2", doc.getFormValues()[1].getValues()[1].getValue());
            assertEquals("xxx", doc.getFormValues()[2].getValues()[0].getValue());
            assertEquals("yyy", doc.getFormValues()[2].getValues()[1].getValue());
            assertEquals("zzz", doc.getFormValues()[2].getValues()[2].getValue());
            assertEquals("000", doc.getFormValues()[2].getValues()[3].getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testManyFormWithManyTextAreas() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TTextAreaDocument",
                    "<html>" +
                    "<body>" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\" itemid=\"$formValueItem.itemid\" >" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"taf\">" +
                    "<span cid=\"Repetition\" array=\"$formValueItem.values\" item=\"$valueItem\">" +
                    "<textarea cid=\"TextArea\" name=\"selected-value\" value=\"$valueItem.value\"></textarea>" +
                    "</span>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TTextAreaDocument");
            TTextAreaDocument doc = (TTextAreaDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("taf", "");
            request.mockSetParameter("itemid", "000");
            request.mockSetParameter("selected-value.000." + doc.getFormValues()[2].getValues()[1].hashCode(), "aaa");
            engine.service(request, createHttpServletResponse());
            assertEquals("a", doc.getFormValues()[0].getValues()[0].getValue());
            assertEquals("b", doc.getFormValues()[0].getValues()[1].getValue());
            assertEquals("c", doc.getFormValues()[0].getValues()[2].getValue());
            assertEquals("1", doc.getFormValues()[1].getValues()[0].getValue());
            assertEquals("2", doc.getFormValues()[1].getValues()[1].getValue());
            assertEquals("xxx", doc.getFormValues()[2].getValues()[0].getValue());
            assertEquals("aaa", doc.getFormValues()[2].getValues()[1].getValue());
            assertEquals("zzz", doc.getFormValues()[2].getValues()[2].getValue());
            assertEquals("000", doc.getFormValues()[2].getValues()[3].getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TTextAreaDocument extends Document {

    private FormValue[] formValues = {
            new FormValue(new Value[]{new Value("a"), new Value("b"), new Value("c")}),
            new FormValue(new Value[]{new Value("1"), new Value("2")}),
            new FormValue(new Value[]{new Value("xxx"), new Value("yyy"), new Value("zzz"), new Value("000")}),
    };
    private FormValue formValueItem;
    private FormValue formValueSelected = formValues[0];
    private Value valueItem;
    private Value valueSelected = formValueSelected.getValues()[0];

    public TTextAreaDocument(String name) {
        super(name);
    }

    public FormValue[] getFormValues() {
        return formValues;
    }

    public FormValue getFormValueItem() {
        return formValueItem;
    }

    public void setFormValueItem(FormValue formValueItem) {
        this.formValueItem = formValueItem;
        setValueItem(this.formValueItem.values[0]);
    }

    public FormValue getFormValueSelected() {
        return formValueSelected;
    }

    public Value getValueItem() {
        return valueItem;
    }

    public void setValueItem(Value valueItem) {
        this.valueItem = valueItem;
    }

    public Value getValueSelected() {
        return valueSelected;
    }

    public Document setFormValues() {
        return null;
    }

    public class Value {

        private String value;

        public Value(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public class FormValue {

        private Value[] values;

        public FormValue(Value[] values) {
            this.values = values;
        }

        public Value[] getValues() {
            return values;
        }

        public String getItemid() {
            return values[values.length - 1].getValue();
        }

    }

}