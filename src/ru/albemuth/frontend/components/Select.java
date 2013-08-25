package ru.albemuth.frontend.components;

import ru.albemuth.frontend.RequestException;

import java.util.*;

public abstract class Select extends Component<Document> {

    protected static final String TITLE_OPTION_VALUE                = "null";

    private String itemid;

    public Select(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getTitle() {
        return null;
    }

    public String getTitleValue() {
        return TITLE_OPTION_VALUE;
    }

    public Collection<?> getItems() {
        return null;
    }

    public Object[] getArray() {
        return null;
    }

    public Iterator<?> getIterator() {
        return null;
    }

    public Object getItem() {
        return null;
    }

    public void setItem(Object item) {}

    public Object getSelectedItem() {
        return null;
    }

    public void setSelectedItem(Object selectedItem) {}

    public List<?> getSelectedItems() {
        return Collections.EMPTY_LIST;
    }

    public void setSelectedItems(List<?> selectedItems) {}

    public String getFullName() {
        String name = getName();
        if (getRequestContext().getItemid() != null) {
            name += "." + getRequestContext().getItemid();
        }
        return name;
    }

    public boolean isReadOnly() {
        return false;
    }

    public String getReadOnlyString() {
        return isReadOnly() ? "readonly" : "";
    }

    public boolean isDisabled() {
        return false;
    }

    public String getDisabledString() {
        return isDisabled() ? "disabled" : "";
    }

    public boolean isMultiple() {
        return false;
    }

    public String getMultipleString() {
        return isMultiple() ? "multiple" : "";
    }

    public String getOptionsString() {
        String optionsString = "";
        if (getTitle() != null) {
            optionsString += "<option value=\"" + getTitleValue() + "\">" + getTitle() + "</option>\n";
        }
        itemid = getRequestContext().getItemid();
        List selectedItems = null;
        Object selectedItem = null;
        boolean multiple = isMultiple();
        if (multiple) {
            selectedItems = getSelectedItems();
        } else {
            selectedItem = getSelectedItem();
        }
        Iterator it;
        if (getItems() != null) {
            for (Object item: getItems()) {
                setItem(item);
                getRequestContext().setItemid(getItemid());
                if (multiple) {
                    optionsString += "<option " + (selectedItems.contains(item) ? " selected " : "")  + "value=\"" + getItemValue() + "\">" + getItemTitle() + "</option>\n";
                } else {
                    optionsString += "<option " + (item.equals(selectedItem) ? " selected " : "")  + "value=\"" + getItemValue() + "\">" + getItemTitle() + "</option>\n";
                }
            }
        } else if (getArray() != null) {
            for (Object item: getArray()) {
                setItem(item);
                getRequestContext().setItemid(getItemid());
                if (multiple) {
                    optionsString += "<option " + (selectedItems.contains(item) ? " selected " : "")  + "value=\"" + getItemValue() + "\">" + getItemTitle() + "</option>\n";
                } else {
                    optionsString += "<option " + (item.equals(selectedItem) ? " selected " : "")  + "value=\"" + getItemValue() + "\">" + getItemTitle() + "</option>\n";
                }
            }
        } else if ((it = getIterator()) != null) {
            for (; it.hasNext(); ) {
                Object item = it.next();
                setItem(item);
                getRequestContext().setItemid(getItemid());
                if (multiple) {
                    optionsString += "<option " + (selectedItems.contains(item) ? " selected " : "")  + "value=\"" + getItemValue() + "\">" + getItemTitle() + "</option>\n";
                } else {
                    optionsString += "<option " + (item.equals(selectedItem) ? " selected " : "")  + "value=\"" + getItemValue() + "\">" + getItemTitle() + "</option>\n";
                }
            }
        }
        getRequestContext().setItemid(itemid);
        return optionsString;
    }

    public String getItemid() {
        Object item = getItem();
        return (itemid != null ? itemid + "." : "") + (item != null ? item.hashCode() : "");
    }

    public String getItemValue() {
        return getItemid();
    }

    public String getItemTitle() {
        return getItemid();
    }

    public void setContextProperties() throws RequestException {
        if (getRequestContext().isInSourceEventForm() && !isDisabled()) {
            itemid = getRequestContext().getItemid();
            List selectedItems = null;
            Object selectedItem = null;
            String selectedValue = null;
            String[] selectedValues = null;
            boolean multiple = isMultiple();
            if (multiple) {
                selectedItems = new ArrayList();
                selectedValues = getRequestContext().getParameterValues(getFullName());
            } else {
                selectedValue = getRequestContext().getParameterValue(getFullName());
            }
            Iterator it;
            if (getItems() != null) {
                for (Object item: getItems()) {
                    setItem(item);
                    getRequestContext().setItemid(getItemid());
                    if(multiple) {
                        if (contains(selectedValues, getItemValue())) {
                            selectedItems.add(item);
                        }
                    } else {
                        if (getItemValue().equals(selectedValue)) {
                            selectedItem = item;
                            break;
                        }
                    }
                }
            } else if (getArray() != null) {
                for (Object item: getArray()) {
                    setItem(item);
                    getRequestContext().setItemid(getItemid());
                    if(multiple) {
                        if (contains(selectedValues, getItemValue())) {
                            selectedItems.add(item);
                        }
                    } else {
                        if (getItemValue().equals(selectedValue)) {
                            selectedItem = item;
                            break;
                        }
                    }
                }
            } else if ((it = getIterator()) != null) {
                for (; it.hasNext(); ) {
                    Object item = it.next();
                    setItem(item);
                    getRequestContext().setItemid(getItemid());
                    if(multiple) {
                        if (contains(selectedValues, getItemValue())) {
                            selectedItems.add(item);
                        }
                    } else {
                        if (getItemValue().equals(selectedValue)) {
                            selectedItem = item;
                            break;
                        }
                    }
                }
            }
            if (multiple) {
                setSelectedItems(selectedItems);
            } else {
                setSelectedItem(selectedItem);
            }
            getRequestContext().setItemid(itemid);
        }
    }

    private boolean contains(String[] values, String value) {
        boolean ret = false;
        for (String v: values) {
            if (v.equals(value)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

}
