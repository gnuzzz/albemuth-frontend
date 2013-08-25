package ru.albemuth.frontend.properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 11.10.2007
 * Time: 1:51:26
 */
public class CompositeProperty extends Property {

    protected Property[] properties;

    public CompositeProperty(String name, Property[] properties) {
        super(name);
        assert properties.length > 1;
        this.properties = properties;
    }

    public Class getTargetClass() {
        return properties[0].getTargetClass();
    }

    public Class getValueClass() {
        return properties[properties.length - 1].getValueClass();
    }

    public Object getValue(Object target) throws PropertyException {
        Object ret = target;
        for (int i = 0; i < properties.length && ret != null; i++) {
            ret = properties[i].getValue(ret);
        }
        return ret;
    }

    protected Object getTargetObject(Object target) throws PropertyException {
        for (int i = 0; i < properties.length - 1 && target != null; i++) {
            target = properties[i].getValue(target);
        }
        return target;
    }

    public void setValue(Object target, Object value) throws PropertyException {
        properties[properties.length - 1].setValue(getTargetObject(target), value);
    }

    public void setValue(Object target, String value) throws PropertyException {
        properties[properties.length - 1].setValue(getTargetObject(target), value);
    }

    public void clearValue(Object target) throws PropertyException {
        properties[properties.length - 1].clearValue(target);
    }

}
