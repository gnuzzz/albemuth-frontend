package ru.albemuth.frontend.context;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 25.10.2007
 * Time: 0:55:48
 */
public abstract class Context<T> {

    public T getParameterValue(String name, String itemid) {
        if (itemid != null) {
            name += "." + itemid;
        }
        return getParameterValue(name);
    }

    public abstract T getParameterValue(String name);

    public void setParameterValue(String name, String itemid, T value) {
        if (itemid != null) {
            name += "." + itemid;
        }
        setParameterValue(name, value);
    }

    public abstract void setParameterValue(String name, T value);

    /*public abstract Parameter getParameter(String name);

    public abstract List<Parameter> getParameters();

    public abstract void setParameter(Parameter parameter);

    public <T> void setParameter(String name, T value) {
        setParameter(new Parameter<T>(name, value));
    }*/

}
