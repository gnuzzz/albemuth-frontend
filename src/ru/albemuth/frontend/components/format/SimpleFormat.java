package ru.albemuth.frontend.components.format;

import ru.albemuth.util.Convertor;
import ru.albemuth.util.ConvertorException;

public class SimpleFormat extends Format {

    public static final SimpleFormat INSTANCE               = new SimpleFormat();

    public String getStringValue(Object value) {
        return value != null ? value.toString() : null;
    }

    public String getStringValue(boolean value) {
        return value + "";
    }

    public String getStringValue(byte value) {
        return value + "";
    }

    public String getStringValue(short value) {
        return value + "";
    }

    public String getStringValue(char value) {
        return value + "";
    }

    public String getStringValue(int value) {
        return value + "";
    }

    public String getStringValue(long value) {
        return value + "";
    }

    public String getStringValue(float value) {
        return value + "";
    }

    public String getStringValue(double value) {
        return value + "";
    }

    public Object getObjectValue(String value) {
        return value;
    }

    public boolean getBooleanValue(String value) throws ConvertorException {
        return Convertor.parseBooleanValue(value, false);
    }

    public byte getByteValue(String value) throws ConvertorException {
        return Convertor.parseByteValue(value, (byte)0);
    }

    public short getShortValue(String value) throws ConvertorException {
        return Convertor.parseShortValue(value, (short)0);
    }

    public char getCharValue(String value) throws ConvertorException {
        return Convertor.parseCharValue(value, (char)0);
    }

    public int getIntValue(String value) throws ConvertorException {
        return Convertor.parseIntValue(value, 0);
    }

    public long getLongValue(String value) throws ConvertorException {
        return Convertor.parseLongValue(value, 0);
    }

    public float getFloatValue(String value) throws ConvertorException {
        return Convertor.parseFloatValue(value, 0);
    }

    public double getDoubleValue(String value) throws ConvertorException {
        return Convertor.parseDoubleValue(value, 0);
    }

}
