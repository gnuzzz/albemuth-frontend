package ru.albemuth.frontend.context;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 25.10.2007
 * Time: 0:54:07
 */
public class Parameter<T> {

    private String name;
    private T value;

    public Parameter(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

}
