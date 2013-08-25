package ru.albemuth.frontend.properties;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 12.10.2007
 * Time: 2:05:57
 */
public class ObjectiveProperty extends PlainProperty {

    protected Class valueClass;

    public ObjectiveProperty(String name, Class targetClass, Class valueClass, Method getterMethod, Method setterMethod, Field field) {
        super(name, targetClass, getterMethod, setterMethod, field);
        this.valueClass = valueClass;
    }

    public Class getValueClass() {
        return valueClass;
    }

    public void setValue(Object target, String value) throws PropertyException {
        setValue(target, (Object)value);
    }

    public void clearValue(Object target) throws PropertyException {
        setValue(target, (Object)null);
    }

}
