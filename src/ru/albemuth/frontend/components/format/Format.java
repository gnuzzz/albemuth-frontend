package ru.albemuth.frontend.components.format;

import ru.albemuth.util.ConvertorException;

public abstract class Format {

    public abstract String getStringValue(Object value);
    public abstract String getStringValue(boolean value);
    public abstract String getStringValue(byte value);
    public abstract String getStringValue(short value);
    public abstract String getStringValue(char value);
    public abstract String getStringValue(int value);
    public abstract String getStringValue(long value);
    public abstract String getStringValue(float value);
    public abstract String getStringValue(double value);

    public abstract Object getObjectValue(String value) throws ConvertorException;
    public abstract boolean getBooleanValue(String value) throws ConvertorException ;
    public abstract byte getByteValue(String value) throws ConvertorException ;
    public abstract short getShortValue(String value) throws ConvertorException ;
    public abstract char getCharValue(String value) throws ConvertorException ;
    public abstract int getIntValue(String value) throws ConvertorException ;
    public abstract long getLongValue(String value) throws ConvertorException ;
    public abstract float getFloatValue(String value) throws ConvertorException ;
    public abstract double getDoubleValue(String value) throws ConvertorException ;

    public static String getSuffix(int value, String[] suffixes) {
        int rest = value % 100;
        String suffix;
        if (rest >= 11 && rest <= 19) {
            suffix = suffixes[2];
        } else {
            rest = value % 10;
            if (rest == 1) {
                suffix = suffixes[0];
            } else if (rest == 2 || rest == 3 || rest == 4) {
                suffix = suffixes[1];
            } else {
                suffix = suffixes[2];
            }
        }
        return suffix;
    }

}
