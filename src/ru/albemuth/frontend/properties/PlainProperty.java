package ru.albemuth.frontend.properties;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 11.10.2007
 * Time: 2:21:56
 */
public abstract class PlainProperty extends Property {

    private static  final Object[] EMPTY_ARRAY                          = {};

    protected Class targetClass;
    protected Method getterMethod;
    protected Method setterMethod;
    protected Field field;

    protected PlainProperty(String name, Class targetClass, Method getterMethod, Method setterMethod, Field field) {
        super(name);
        this.targetClass = targetClass;
        this.setterMethod = setterMethod;
        this.getterMethod = getterMethod;
        this.field = field;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public Object getValue(Object target) throws PropertyException {
        Object ret;
        try {
            if (getterMethod != null) {
                ret = getterMethod.invoke(target, EMPTY_ARRAY);
            } else {
                ret = field.get(target);
            }
        } catch (IllegalAccessException e) {
            throw new PropertyException("Can't get property " + name + " of class " + targetClass.getName(), e);
        } catch (InvocationTargetException e) {
            throw new PropertyException("Can't get property " + name + " of class " + targetClass.getName(), e);
        }
        return ret;
    }

    public void setValue(Object target, Object value) throws PropertyException {
        try {
            if (setterMethod != null) {
                setterMethod.invoke(target, value);
            } else  if (field != null) {
                field.set(target, value);
            } else {
                throw new PropertyException("Can't set property " + getName() + " of class " + getTargetClass().getName() + ": property is readonly");
            }
        } catch (IllegalArgumentException e) {
            throw new PropertyException("Can't set property " + getName() + " of class " + getTargetClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new PropertyException("Can't set property " + getName() + " of class " + getTargetClass().getName(), e);
        } catch (InvocationTargetException e) {
            throw new PropertyException("Can't set property " + getName() + " of class " + getTargetClass().getName(), e);
        }
    }
    
}
