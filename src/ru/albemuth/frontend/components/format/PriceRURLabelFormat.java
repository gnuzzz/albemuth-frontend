package ru.albemuth.frontend.components.format;

import ru.albemuth.util.ConvertorException;

public class PriceRURLabelFormat extends AbstractFormat {

    public static final PriceRURLabelFormat INSTANCE                = new PriceRURLabelFormat();

    public String getStringValue(int value) {
        return (value / 100) + " р.";
    }

    public int getIntValue(String value) throws ConvertorException {
        return SimpleFormat.INSTANCE.getIntValue(value.substring(0, value.length() - " р.".length()));
    }

}
