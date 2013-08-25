package ru.albemuth.frontend.properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 19.10.2007
 * Time: 1:01:36
 */
public abstract class Link {

    public static final Link[] EMPTY_ARRAY                      = {};

    public abstract void processLink() throws PropertyException;

    public abstract void clearLink() throws PropertyException;

}
