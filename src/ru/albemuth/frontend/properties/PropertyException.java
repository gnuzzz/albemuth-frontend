package ru.albemuth.frontend.properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 11.10.2007
 * Time: 1:44:00
 */
public class PropertyException extends Exception {

    public PropertyException(String string) {
        super(string);
    }

    public PropertyException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public PropertyException(Throwable throwable) {
        super(throwable);
    }

}
