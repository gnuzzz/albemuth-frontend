package ru.albemuth.frontend.properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 11.10.2007
 * Time: 1:43:26
 */
public abstract class Property implements Cloneable {

    public static final Property[] EMPTY_ARRAY              = {};

    protected String name;

    protected Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Class getTargetClass();

    public abstract Class getValueClass();

    public abstract Object getValue(Object target) throws PropertyException;

    public abstract void setValue(Object target, Object value) throws PropertyException;

    public abstract void setValue(Object target, String value) throws PropertyException;

    public abstract void clearValue(Object target) throws PropertyException;

    public int hashCode() {
        return getTargetClass().hashCode() * getName().hashCode();
    }

    public boolean equals(Object object) {
        boolean ret = object instanceof Property;
        if (ret) {
            Property p = (Property)object;
            ret = getTargetClass().equals(p.getTargetClass()) && getName().equals(p.getName());
        }
        return ret;
    }

    public Property clone() {
        try {
            return (Property)super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
