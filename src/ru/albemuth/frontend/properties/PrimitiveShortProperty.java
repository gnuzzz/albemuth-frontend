package ru.albemuth.frontend.properties;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 11.10.2007
 * Time: 2:31:37
 */
public final class PrimitiveShortProperty extends PlainProperty {

    public PrimitiveShortProperty(String name, Class targetClass, Method getterMethod, Method setterMethod, Field field) {
        super(name, targetClass, getterMethod, setterMethod, field);
    }

    public Class getValueClass() {
        return short.class;
    }

    public void setValue(Object target, String value) throws PropertyException {
        try {
            setValue(target, Short.valueOf(value));
        } catch (NumberFormatException e) {
            throw new PropertyException("Wrong number format of value " + value + " property " + getName() + " of class " + getTargetClass().getName(), e);
        }
    }

    public void clearValue(Object target) throws PropertyException {
        setValue(target, 0);
    }

}