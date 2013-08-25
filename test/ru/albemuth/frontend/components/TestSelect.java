package ru.albemuth.frontend.components;

import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class TestSelect extends TestFrontend {

    public void testOneSelectSingle() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TSelectDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"sf\">" +
                    "<select cid=\"Select\" name=\"selected-value\" array=\"$selectedFormValue.values\" item=\"$valueItem\" selectedItem=\"$selectedValue\" itemValue=\"$valueItem.value\" itemTitle=\"$valueItem.value\"></select>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TSelectDocument");
            TSelectDocument doc = (TSelectDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("sf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value", "12");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option value=\"11\">11</option>") != -1);
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option  selected value=\"12\">12</option>") != -1);
            assertEquals("12", doc.getSelectedValue().getValue());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testOneSelectSingleWithTitle() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TSelectDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"sf\">" +
                    "<select cid=\"Select\" name=\"selected-value\" title=\"Please, select value\" array=\"$selectedFormValue.values\" item=\"$valueItem\" selectedItem=\"$selectedValue\" itemValue=\"$valueItem.value\" itemTitle=\"$valueItem.value\"></select>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TSelectDocument");
            TSelectDocument doc = (TSelectDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("sf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value", "null");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option value=\"null\">Please, select value</option>") != -1);
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option value=\"11\">11</option>") != -1);
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option value=\"12\">12</option>") != -1);
            assertNull(doc.getSelectedValue());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testOneSelectMultiple() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TSelectDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"sf\">" +
                    "<select cid=\"Select\" name=\"selected-value\" multiple=\"true\" array=\"$selectedFormValue.values\" item=\"$valueItem\" selectedItems=\"$selectedValues\" itemValue=\"$valueItem.value\" itemTitle=\"$valueItem.value\"></select>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TSelectDocument");
            TSelectDocument doc = (TSelectDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("sf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value", "12");
            request.mockSetParameter("selected-value", "13");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option value=\"11\">11</option>") != -1);
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option  selected value=\"12\">12</option>") != -1);
            assertTrue(doc.getRequestContext().getResponse().indexOf("<option  selected value=\"13\">13</option>") != -1);
            assertTrue(contains(doc.getSelectedValues(), new TSelectDocument.Value("12")));
            assertTrue(contains(doc.getSelectedValues(), new TSelectDocument.Value("13")));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testOneFormWithManySelects() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TSelectDocument",
                    "<html>" +
                    "<body>" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"sf\">" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\" itemid=\"$formValueItem.itemid\">" +
                    "<select cid=\"Select\" name=\"selected-value\" array=\"$formValueItem.values\" item=\"$valueItem\" selectedItem=\"$formValueItem.selectedValue\" itemValue=\"$valueItem.value\" itemTitle=\"$valueItem.value\"></select>" +
                    "</span>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TSelectDocument");
            TSelectDocument doc = (TSelectDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("sf", "");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value.11", "11");
            request.mockSetParameter("selected-value.21", "22");
            request.mockSetParameter("selected-value.31", "33");
            engine.service(request, createHttpServletResponse());
            assertEquals("11", doc.getFormValues()[0].getSelectedValue().getValue());
            assertEquals("22", doc.getFormValues()[1].getSelectedValue().getValue());
            assertEquals("33", doc.getFormValues()[2].getSelectedValue().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testManyFormWithOneSelect() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TSelectDocument",
                    "<html>" +
                    "<body>" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\" itemid=\"$formValueItem.itemid\">" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"sf\">" +
                    "<select cid=\"Select\" name=\"selected-value\" array=\"$formValueItem.values\" item=\"$valueItem\" selectedItem=\"$formValueItem.selectedValue\" itemValue=\"$valueItem.value\" itemTitle=\"$valueItem.value\"></select>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TSelectDocument");
            TSelectDocument doc = (TSelectDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("sf", "");
            request.mockSetParameter("itemid", "21");
            request.mockSetParameter("sb.21", "Ok");
            request.mockSetParameter("selected-value.21", "22");
            engine.service(request, createHttpServletResponse());
            assertEquals("11", doc.getFormValues()[0].getSelectedValue().getValue());
            assertEquals("22", doc.getFormValues()[1].getSelectedValue().getValue());
            assertEquals("31", doc.getFormValues()[2].getSelectedValue().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testManyFormWithManySelects() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TSelectDocument",
                    "<html>" +
                    "<body>" +
                    "<span cid=\"Repetition\" array=\"$fvs\" item=\"$fvItem\" itemid=\"$fvItemid\">" +
                    "<form cid=\"Form\" listener=\"setFormValues\" response=\"FORWARD\" name=\"sf\">" +
                    "<span cid=\"Repetition\" array=\"$fvItem\" item=\"$formValueItem\">" +
                    "<select cid=\"Select\" name=\"selected-value\" array=\"$formValueItem.values\" item=\"$valueItem\" selectedItem=\"$formValueItem.selectedValue\" itemValue=\"$valueItem.value\" itemTitle=\"$valueItem.value\"></select>" +
                    "</span>" +
                    "<input cid=\"SubmitButton\" listener=\"setFormValues\" response=\"FORWARD\" type=\"submit\" name=\"sb\" value=\"Ok\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TSelectDocument");
            TSelectDocument doc = (TSelectDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("sf", "");
            request.mockSetParameter("itemid", "2");
            request.mockSetParameter("sb", "Ok");
            request.mockSetParameter("selected-value.2." + doc.getFormValues()[0].hashCode(), "11");
            request.mockSetParameter("selected-value.2." + doc.getFormValues()[1].hashCode(), "22");
            request.mockSetParameter("selected-value.2." + doc.getFormValues()[2].hashCode(), "33");
            engine.service(request, createHttpServletResponse());
            assertEquals("11", doc.getFormValues()[0].getSelectedValue().getValue());
            assertEquals("22", doc.getFormValues()[1].getSelectedValue().getValue());
            assertEquals("33", doc.getFormValues()[2].getSelectedValue().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private boolean contains(List<TSelectDocument.Value> values, TSelectDocument.Value value) {
        boolean ret = false;
        for (TSelectDocument.Value v: values) {
            if (v.equals(value)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

}

abstract class TSelectDocument extends Document {

    private FormValue[] formValues = {
            new FormValue(new Value[]{new Value("11"), new Value("12"), new Value("13")}),
            new FormValue(new Value[]{new Value("21"), new Value("22"), new Value("23")}),
            new FormValue(new Value[]{new Value("31"), new Value("32"), new Value("33")})
    };
    private FormValue selectedFormValue = formValues[0];
    private Value selectedValue = selectedFormValue.selectedValue;
    private List<Value> selectedValues = selectedFormValue.selectedValues;
    private FormValue formValueItem;
    private Value valueItem;

    private FormValue[][] fvs = {formValues, formValues};
    private FormValue[] fvSelected = fvs[0];
    private FormValue[] fvItem;
    private int fvCounter;

    public TSelectDocument(String name) {
        super(name);
    }

    public FormValue[] getFormValues() {
        return formValues;
    }

    public FormValue getSelectedFormValue() {
        return selectedFormValue;
    }

    public void setSelectedFormValue(FormValue selectedFormValue) {
        this.selectedFormValue = selectedFormValue;
    }

    public Value getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Value selectedValue) {
        this.selectedValue = selectedValue;
    }

    public List<Value> getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(List<Value> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public FormValue getFormValueItem() {
        return formValueItem;
    }

    public void setFormValueItem(FormValue formValueItem) {
        this.formValueItem = formValueItem;
    }

    public Value getValueItem() {
        return valueItem;
    }

    public void setValueItem(Value valueItem) {
        this.valueItem = valueItem;
    }

    public FormValue[][] getFvs() {
        return fvs;
    }

    public FormValue[] getFvSelected() {
        return fvSelected;
    }

    public void setFvSelected(FormValue[] fvSelected) {
        this.fvSelected = fvSelected;
    }

    public FormValue[] getFvItem() {
        return fvItem;
    }

    public void setFvItem(FormValue[] fvItem) {
        this.fvCounter++;
        this.fvItem = fvItem;
    }

    public String getFvItemid() {
        return fvCounter % 2 == 0 ? "2" : "1";
    }

    public Document setFormValues() {
        return null;
    }

    public static class Value {

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

        public int hashCode() {
            return value.hashCode();
        }

        public boolean equals(Object o) {
            return o instanceof Value && ((Value)o).value.equals(value);
        }

    }

    public static class FormValue {

        private Value[] values;
        private Value selectedValue;
        private List<Value> selectedValues;

        public FormValue(Value[] values) {
            this.values = values;
            this.selectedValue = values[0];
            this.selectedValues = Collections.EMPTY_LIST;
        }

        public Value[] getValues() {
            return values;
        }

        public Value getSelectedValue() {
            return selectedValue;
        }

        public void setSelectedValue(Value selectedValue) {
            this.selectedValue = selectedValue;
        }

        public List<Value> getSelectedValues() {
            return selectedValues;
        }

        public void setSelectedValues(List<Value> selectedValues) {
            this.selectedValues = selectedValues;
        }

        public String getItemid() {
            return values[0].getValue();
        }

        public int hashCode() {
            return getItemid().hashCode();
        }

    }

}