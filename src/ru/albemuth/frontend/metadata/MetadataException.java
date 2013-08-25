package ru.albemuth.frontend.metadata;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 28.11.2007
 * Time: 1:25:57
 */
public class MetadataException extends Exception {

    public MetadataException(String s) {
        super(s);
    }

    public MetadataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MetadataException(Throwable throwable) {
        super(throwable);
    }

}
