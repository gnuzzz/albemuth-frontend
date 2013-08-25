package ru.albemuth.frontend.properties;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 11.10.2007
 * Time: 2:34:45
 */
public class PrimitiveLongProperty extends PlainProperty {

    public PrimitiveLongProperty(String name, Class targetClass, Method getterMethod, Method setterMethod, Field field) {
        super(name, targetClass, getterMethod, setterMethod, field);
    }

    public Class getValueClass() {
        return long.class;
    }

    public void setValue(Object target, String value) throws PropertyException {
        try {
            setValue(target, Long.valueOf(value));
        } catch (NumberFormatException e) {
            throw new PropertyException("Wrong number format of value " + value + " property " + getName() + " of class " + getTargetClass().getName(), e);
        }
    }

    public void clearValue(Object target) throws PropertyException {
        setValue(target, 0);
    }

}
