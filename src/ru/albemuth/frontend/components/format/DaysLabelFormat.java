package ru.albemuth.frontend.components.format;

import ru.albemuth.util.ConvertorException;

public class DaysLabelFormat extends AbstractFormat {

    public static final DaysLabelFormat INSTANCE                = new DaysLabelFormat();
    public static final String[] SUFFIXES                       = {"день", "дня", "дней"};

    public String getStringValue(int value) {
        return value + " " + getSuffix(value, SUFFIXES);
    }

    public int getIntValue(String value) throws ConvertorException {
        return SimpleFormat.INSTANCE.getIntValue(value.substring(0, value.indexOf(' ')));
    }

}
