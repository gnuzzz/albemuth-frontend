package ru.albemuth.frontend.components;

import org.apache.log4j.Logger;
import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentDescriptor;
import ru.albemuth.util.Convertor;
import ru.albemuth.util.ConvertorException;
import ru.albemuth.util.Configuration;

import java.util.Properties;

public class TestImageButton extends TestFrontend {

    public void testFormSubmit() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TImageButtonDocument",
                    "<html>" +
                    "<body>" +
                    "$selectedValue.value<br />" +
                    "<form cid=\"Form\" listener=\"setValueFromFormSubmit\" response=\"FORWARD\" name=\"ssv1\">" +
                    "<input type=\"text\" name=\"selected-value\" value=\"\" />" +
                    "<input cid=\"ImageButton\" listener=\"setValueFromButtonSubmit\" response=\"FORWARD\" src=\"/image.gif\" type=\"image\" name=\"ib1\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TImageButtonDocument");
            TImageButtonDocument doc = (TImageButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("ssv1", "");
            request.mockSetParameter("selected-value", "10");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<html><body>10<br /><form action=\"/" + doc.getName() + "\" method=\"post\" name=\"ssv1\"  >") != -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testButtonSubmit() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TImageButtonDocument",
                    "<html>" +
                    "<body>" +
                    "$selectedValue.value<br />" +
                    "<form cid=\"Form\" listener=\"setValueFromFormSubmit\" response=\"FORWARD\" name=\"ssv1\">" +
                    "<input type=\"text\" name=\"selected-value\" value=\"\" />" +
                    "<input cid=\"ImageButton\" listener=\"setValueFromButtonSubmit\" response=\"FORWARD\" type=\"image\" src=\"/image.gif\" name=\"ib1\" value=\"Ok\" />" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TImageButtonDocument");
            TImageButtonDocument doc = (TImageButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("ssv1", "");
            request.mockSetParameter("ib1", "Ok");
            request.mockSetParameter("selected-value", "11");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<html><body>11<br /><form action=\"/" + doc.getName() + "\" method=\"post\" name=\"ssv1\"  >") != -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testMultipleFormsButtonSubmit() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TImageButtonDocument",
                    "<html>" +
                    "<body>" +
                    "$selectedValue.value<br />" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\">" +
                    "<form cid=\"Form\" listener=\"setValueFromMultipleFormSubmit\" response=\"FORWARD\" name=\"ssv1\">" +
                    "<input type=\"text\" name=\"selected-value\" value=\"\" />" +
                    "<input cid=\"ImageButton\" listener=\"setValueFromMultipleFormButtonSubmit\" response=\"FORWARD\" type=\"image\"src=\"/image.gif\"  name=\"ib1\" value=\"Ok\" />" +
                    "</form>" +
                    "</span>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TImageButtonDocument");
            TImageButtonDocument doc = (TImageButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("ssv1", "");
            request.mockSetParameter("itemid", "30");
            request.mockSetParameter("ib1.30", "Ok");
            request.mockSetParameter("selected-value", "11");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<html><body>31<br /><form action=\"/" + doc.getName() + "\" method=\"post\" name=\"ssv1\"  >") != -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testMultipleButtonSubmit() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TImageButtonDocument",
                    "<html>" +
                    "<body>" +
                    "$selectedValue.value<br />" +
                    "<form cid=\"Form\" listener=\"setValueFromFormSubmit\" response=\"FORWARD\" name=\"ssv1\">" +
                    "<input type=\"text\" name=\"selected-value\" value=\"\" />" +
                    "<span cid=\"Repetition\" array=\"$selectedFormValue.buttonValues\" item=\"$valueItem\">" +
                    "<input cid=\"ImageButton\" listener=\"setValueFromMultipleButtonSubmit\" response=\"FORWARD\" type=\"image\" src=\"$valueItem.toString\" name=\"ib1\" value=\"Ok\" />" +
                    "</span>" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TImageButtonDocument");
            TImageButtonDocument doc = (TImageButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("ssv1", "");
            request.mockSetParameter("ib1.12", "Ok");
            request.mockSetParameter("selected-value", "11");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<html><body>12<br /><form action=\"/" + doc.getName() + "\" method=\"post\" name=\"ssv1\"  >") != -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testMultipleFormMultipleButtonSubmit() {
        try {
            TComponentParser parser = new TComponentParser(new ComponentClassBuilderJavassistImpl("test"));
            parser.configure(new Configuration(new Properties()));
            parser.setComponentContent("/ru/albemuth/frontend/components/TImageButtonDocument",
                    "<html>" +
                    "<body>" +
                    "$selectedValue.value<br />" +
                    "<span cid=\"Repetition\" array=\"$formValues\" item=\"$formValueItem\">" +
                    "<form cid=\"Form\" listener=\"setValueFromMultipleFormSubmit\" response=\"FORWARD\" name=\"ssv1\">" +
                    "<input type=\"text\" name=\"selected-value\" value=\"\" />" +
                    "<span cid=\"Repetition\" array=\"$formValueItem.buttonValues\" item=\"$valueItem\">" +
                    "<input cid=\"ImageButton\" listener=\"setValueFromMultipleButtonMultipleFormSubmit\" response=\"FORWARD\" type=\"image\" src=\"$valueItem.toString\" name=\"ib1\" value=\"Ok\" />" +
                    "</span>" +
                    "</form>" +
                    "</body>" +
                    "</html>"
            );
            ComponentDescriptor cd = parser.getDocumentDescriptor("TImageButtonDocument");
            TImageButtonDocument doc = (TImageButtonDocument)cd.createComponent(null);
            TEngine engine = new TEngine();
            engine.setRequestDocument(doc);
            HTTPServletRequest request = createHttpServletRequest(doc);
            request.mockSetRequestURI("http://localhost/" + doc.getName());
            request.mockSetParameter("ssv1", "");
            request.mockSetParameter("itemid", "20");
            request.mockSetParameter("ib1.20.22", "Ok");
            request.mockSetParameter("selected-value", "22");
            engine.service(request, createHttpServletResponse());
            assertTrue(doc.getRequestContext().getResponse().indexOf("<html><body>22<br /><form action=\"/" + doc.getName() + "\" method=\"post\" name=\"ssv1\"  >") != -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

abstract class TImageButtonDocument extends Document {

    private static final Logger LOG             = Logger.getLogger(TImageButtonDocument.class);

    private FormValue[] formValues = {
            new FormValue(new Value(10), new Value[]{new Value(11), new Value(12)}),
            new FormValue(new Value(20), new Value[]{new Value(21), new Value(22)}),
            new FormValue(new Value(30), new Value[]{new Value(31), new Value(32)})
    };
    private FormValue selectedFormValue = formValues[0];
    private Value selectedValue = Value.DEFAULT;
    private FormValue formValueItem;
    private Value valueItem;

    public TImageButtonDocument(String name) {
        super(name);
    }

    public FormValue[] getFormValues() {
        return formValues;
    }

    public void setFormValues(FormValue[] formValues) {
        this.formValues = formValues;
    }

    public FormValue getSelectedFormValue() {
        return selectedFormValue;
    }

    public Value getSelectedValue() {
        return selectedValue;
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

    public void release() throws ReleaseException {
        super.release();
        selectedValue = formValues != null && formValues.length > 0 ? formValues[0].getFormValue() : Value.DEFAULT;
    }

    public Document setValueFromFormSubmit() {
        try {
            int value = Convertor.parseIntValue(getRequestContext().getParameterValue("selected-value"), 0);
            for (FormValue v: formValues) {
                if (v.getFormValue().getValue() == value) {
                    selectedValue = v.getFormValue();
                    break;
                }
            }
        } catch (ConvertorException e) {
            LOG.error("Invalid value " + getRequestContext().getParameterValue("selected-value"), e);
        }
        return null;
    }

    public Document setValueFromMultipleFormSubmit() {
        selectedValue = formValueItem.getFormValue();
        return null;
    }

    public Document setValueFromButtonSubmit() {
        try {
            int value = Convertor.parseIntValue(getRequestContext().getParameterValue("selected-value"), 0);
            for (FormValue fv: formValues) {
                for (Value v: fv.getButtonValues()) {
                    if (v.getValue() == value) {
                        selectedValue = v;
                        break;
                    }
                }
            }
        } catch (ConvertorException e) {
            LOG.error("Invalid value " + getRequestContext().getParameterValue("selected-value"), e);
        }
        return null;
    }

    public Document setValueFromMultipleFormButtonSubmit() {
        selectedValue = formValueItem.getButtonValues()[0];
        return null;
    }

    public Document setValueFromMultipleButtonSubmit() {
        selectedValue = valueItem;
        return null;
    }

    public Document setValueFromMultipleButtonMultipleFormSubmit() {
        selectedValue = valueItem;
        return null;
    }

    class FormValue {

        private Value formValue;
        private Value[] buttonValues;

        FormValue(Value formValue, Value[] buttonValues) {
            this.formValue = formValue;
            this.buttonValues = buttonValues;
        }

        public Value getFormValue() {
            return formValue;
        }

        public Value[] getButtonValues() {
            return buttonValues;
        }

        public int hashCode() {
            return formValue.hashCode();
        }

    }

}