package ru.albemuth.frontend.components.format;

import ru.albemuth.util.ConvertorException;

public class AbstractFormat extends Format {

    public String getStringValue(Object value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(boolean value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(byte value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(short value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(char value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(int value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(long value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(float value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public String getStringValue(double value) {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public Object getObjectValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public boolean getBooleanValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public byte getByteValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public short getShortValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public char getCharValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public int getIntValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public long getLongValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public float getFloatValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }

    public double getDoubleValue(String value) throws ConvertorException {
        throw new AbstractMethodError("this method not implament in " + getClass().getName());
    }
    
}
