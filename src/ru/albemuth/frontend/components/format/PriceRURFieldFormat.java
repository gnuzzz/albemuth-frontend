package ru.albemuth.frontend.components.format;

import ru.albemuth.util.ConvertorException;

public class PriceRURFieldFormat extends AbstractFormat {

    public static final PriceRURFieldFormat INSTANCE                = new PriceRURFieldFormat();

    public String getStringValue(int value) {
        return (value / 100) + "";
    }

    public int getIntValue(String value) throws ConvertorException {
        return SimpleFormat.INSTANCE.getIntValue(value);
    }

}
