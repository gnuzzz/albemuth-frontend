package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestRadioButton extends TestFrontend {

    public void testOneFormWithOneRadioButton() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TRadioButtonDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"rbf\">" +
                    "<input cid=\"RadioButton\" type=\"radio\" name=\"selected-value\" checked=\"$valueSelected.checked\" value=\"$valueSelected.value\" />" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TRadioButtonDocument");
            TRadioButtonDocument doc = (TRadioButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<input name=\"selected-value\" value=\"a\"     type=\"radio\" />") != -1);
            assertEquals("a", doc.getValueSelected().getValue());
            assertFalse(doc.getValueSelected().isChecked());

            request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("rbf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value", "a");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<input name=\"selected-value\" value=\"a\" checked    type=\"radio\" />") != -1);
            assertEquals("a", doc.getValueSelected().getValue());
            assertTrue(doc.getValueSelected().isChecked());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testOneFormWithManyRadioButtons() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TRadioButtonDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"rbf\">" +
                    "<span cid=\"Repetition\" array=\"$formValueSelected.values\" item=\"$valueItem\">" +
                    "<input cid=\"RadioButton\" type=\"radio\" name=\"selected-value\" checked=\"$valueItem.checked\" value=\"$valueItem.value\" itemid=\"$valueItem.value\" />" +
                    "</span>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TRadioButtonDocument");
            TRadioButtonDocument doc = (TRadioButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("rbf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value", "b");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<input name=\"selected-value\" value=\"b\" checked    type=\"radio\" />") != -1);
            assertFalse(doc.getFormValueSelected().getValues()[0].isChecked());
            assertTrue(doc.getFormValueSelected().getValues()[1].isChecked());
            assertFalse(doc.getFormValueSelected().getValues()[2].isChecked());

            request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("rbf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value", "a");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<input name=\"selected-value\" value=\"a\" checked    type=\"radio\" />") != -1);
            assertTrue(doc.getFormValueSelected().getValues()[0].isChecked());
            assertFalse(doc.getFormValueSelected().getValues()[1].isChecked());
            assertFalse(doc.getFormValueSelected().getValues()[2].isChecked());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testManyFormWithOneRadioButton() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TRadioButtonDocument",
                    "<html>" +
                    "<body>" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\" itemid=\"$formValueItem.itemid\" >" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"rbf\">" +
                    "<input cid=\"RadioButton\" type=\"radio\" name=\"selected-value\" checked=\"$valueItem.checked\" value=\"$valueItem.value\" />" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TRadioButtonDocument");
            TRadioButtonDocument doc = (TRadioButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("rbf", "");
            request.mockSetParameter("itemid", "2");
            request.mockSetParameter("sb.2", "Ok");
            request.mockSetParameter("selected-value", "1");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<input name=\"selected-value\" value=\"1\" checked    type=\"radio\" />") != -1);
            assertFalse(doc.getFormValues()[0].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[0].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[0].getValues()[2].isChecked());
            assertTrue(doc.getFormValues()[1].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[1].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[2].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[3].isChecked());

            request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("rbf", "");
            request.mockSetParameter("itemid", "000");
            request.mockSetParameter("sb.000", "Ok");
            request.mockSetParameter("selected-value", "xxx");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<input name=\"selected-value\" value=\"1\" checked    type=\"radio\" />") != -1);
            assertTrue(doc.getRequestContext().getResponse().indexOf("<input name=\"selected-value\" value=\"xxx\" checked    type=\"radio\" />") != -1);
            assertFalse(doc.getFormValues()[0].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[0].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[0].getValues()[2].isChecked());
            assertTrue(doc.getFormValues()[1].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[1].getValues()[1].isChecked());
            assertTrue(doc.getFormValues()[2].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[2].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[3].isChecked());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testManyFormWithManyRadioButtons() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TRadioButtonDocument",
                    "<html>" +
                    "<body>" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\" itemid=\"$formValueItem.itemid\" >" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"rbf\">" +
                    "<span cid=\"Repetition\" array=\"$formValueItem.values\" item=\"$valueItem\" >" +
                    "<input cid=\"RadioButton\" type=\"radio\" name=\"selected-value\" checked=\"$valueItem.checked\" />" +
                    "</span>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TRadioButtonDocument");
            TRadioButtonDocument doc = (TRadioButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("rbf", "");
            request.mockSetParameter("itemid", "000");
            request.mockSetParameter("selected-value", ".000." + doc.getFormValues()[2].getValues()[1].hashCode());
            engine.service(request, createHttpServletResponse());
            assertFalse(doc.getFormValues()[0].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[0].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[0].getValues()[2].isChecked());
            assertFalse(doc.getFormValues()[1].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[1].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[0].isChecked());
            assertTrue(doc.getFormValues()[2].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[2].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[3].isChecked());

            request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("rbf", "");
            request.mockSetParameter("itemid", "c");
            request.mockSetParameter("selected-value", ".c." + doc.getFormValues()[0].getValues()[1].hashCode());
            engine.service(request, createHttpServletResponse());
            assertFalse(doc.getFormValues()[0].getValues()[0].isChecked());
            assertTrue(doc.getFormValues()[0].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[0].getValues()[2].isChecked());
            assertFalse(doc.getFormValues()[1].getValues()[0].isChecked());
            assertFalse(doc.getFormValues()[1].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[0].isChecked());
            assertTrue(doc.getFormValues()[2].getValues()[1].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[2].isChecked());
            assertFalse(doc.getFormValues()[2].getValues()[3].isChecked());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TRadioButtonDocument extends Document {

    private FormValue[] formValues = {
            new FormValue(new Value[]{new Value("a", false), new Value("b", false), new Value("c", false)}),
            new FormValue(new Value[]{new Value("1", false), new Value("2", false)}),
            new FormValue(new Value[]{new Value("xxx", false), new Value("yyy", false), new Value("zzz", false), new Value("000", false)}),
    };
    private FormValue formValueItem;
    private FormValue formValueSelected = formValues[0];
    private Value valueItem;
    private Value valueSelected = formValueSelected.getValues()[0];

    public TRadioButtonDocument(String name) {
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
        private boolean checked;

        public Value(String value, boolean checked) {
            this.value = value;
            this.checked = checked;
        }

        public String getValue() {
            return value;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
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