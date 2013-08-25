package ru.albemuth.frontend.properties;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 12.10.2007
 * Time: 2:02:33
 */
public class PropertyFactory {

    public Property getProperty(Class targetClass, String propertyName) throws PropertyException {
        if (propertyName.indexOf(".") != -1) {
            return getCompositeProperty(targetClass, propertyName);
        } else {
            return getPlainProperty(targetClass, propertyName);
        }
    }

    public PlainProperty getPlainProperty(Class targetClass, String propertyName) throws PropertyException {
        Method getterMethod, setterMethod;
        Class valueClass;
        Field field = null;

        getterMethod = getMethod("get" + firstCharToUpperCase(propertyName), targetClass);
        if (getterMethod == null) {
            getterMethod = getMethod("is" + firstCharToUpperCase(propertyName), targetClass);
        }
        if (getterMethod != null) {
            valueClass = getterMethod.getReturnType();
        } else {
            field = getField(propertyName, targetClass);
            if (field != null) {
                valueClass = field.getType();
            } else {
                throw new PropertyException(propertyName + " isn't a property of class " + targetClass.getName() + ": it hasn't getter method or public field");
            }
        }
        setterMethod = getMethod("set" + firstCharToUpperCase(propertyName), targetClass, valueClass);
        if (setterMethod == null && field != null && !field.getType().equals(valueClass)) {
            throw new PropertyException("Property " + propertyName + " has getter method with value class " + valueClass.getName() + ", but public field with value class " + field.getType().getName());
        }
        PlainProperty ret;
        if (valueClass.isPrimitive()) {
            if (byte.class.equals(valueClass)) {
                ret = new PrimitiveByteProperty(propertyName, targetClass, getterMethod, setterMethod, field);
            } else if (short.class.equals(valueClass)) {
                ret = new PrimitiveShortProperty(propertyName, targetClass, getterMethod, setterMethod, field);
            } else if (int.class.equals(valueClass)) {
                ret = new PrimitiveIntProperty(propertyName, targetClass, getterMethod, setterMethod, field);
            } else if (long.class.equals(valueClass)) {
                ret = new PrimitiveLongProperty(propertyName, targetClass, getterMethod, setterMethod, field);
            } else if (float.class.equals(valueClass)) {
                ret = new PrimitiveFloatProperty(propertyName, targetClass, getterMethod, setterMethod, field);
            } else if (double.class.equals(valueClass)) {
                ret = new PrimitiveDoubleProperty(propertyName, targetClass, getterMethod, setterMethod, field);
            } else if (boolean.class.equals(valueClass)) {
                ret = new PrimitiveBooleanProperty(propertyName, targetClass, getterMethod, setterMethod, field);
            } else {
                throw new PropertyException("Unknown primitive type " + valueClass.getName());
            }
        } else {
            ret = new ObjectiveProperty(propertyName, targetClass, valueClass, getterMethod, setterMethod, field);
        }
        return ret;
    }

    public CompositeProperty getCompositeProperty(Class targetClass, String propertyName) throws PropertyException {
        StringTokenizer st = new StringTokenizer(propertyName, ".");
        ArrayList<Property> props = new ArrayList<Property>();
        try {
            for (Class clazz = targetClass; st.hasMoreTokens(); ) {
                String nextPropertyName = st.nextToken();
                Property p = getProperty(clazz, nextPropertyName);
                props.add(p);
                clazz = p.getValueClass();
            }
        } catch (PropertyException e) {
            throw new PropertyException("Can't create property " + propertyName + " of class " + targetClass.getName(), e);
        }
        Property[] properties = new Property[props.size()];
        props.toArray(properties);
        return new CompositeProperty(propertyName, properties);
    }

    protected Method getMethod(String methodName, Class targetClass, Class<?>... paramsTypes) {
        /*Method ret = null;
        try {
            ret = targetClass.getMethod(methodName, paramsTypes);
        } catch(NoSuchMethodException e) {
            for (Method m: targetClass.getMethods()) {//???
                if (m.getName().equals(methodName)) {
                    ret = m;
                    break;
                }
            }
        }
        return ret;*/
        try {
            return targetClass.getMethod(methodName, paramsTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    protected Field getField(String fieldName, Class targetClass) {
        /*Field ret = null;
        try {
            ret = targetClass.getField(fieldName);
        } catch (NoSuchFieldException e) {

        }
        return ret;*/
        try {
            return targetClass.getField(fieldName);
        } catch(NoSuchFieldException e) {
            return null;
        }
    }

    protected String firstCharToUpperCase(String name) {
        return name.substring(0, 1).toUpperCase() + (name.length() > 0 ? name.substring(1) : "");
    }

}
