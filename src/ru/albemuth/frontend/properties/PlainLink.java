package ru.albemuth.frontend.properties;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 19.10.2007
 * Time: 1:06:04
 */
public class PlainLink extends Link {

    private Object source;
    private Property sourceProperty;
    private Object destination;
    private Property destinationProperty;

    public PlainLink(Object source, Property sourceProperty, Object destination, Property destinationProperty) {
        this.source = source;
        this.sourceProperty = sourceProperty;
        this.destination = destination;
        this.destinationProperty = destinationProperty;
    }

    public void processLink() throws PropertyException {
        process(sourceProperty, source, destinationProperty, destination);
    }

    public void clearLink() throws PropertyException {
        destinationProperty.clearValue(destination);
    }

    public static void process(Property fromProperty, Object fromValue, Property toProperty, Object toValue) throws PropertyException {
        toProperty.setValue(toValue, fromProperty.getValue(fromValue));
    }

    public String toString() {
        return source.getClass().getName() + "." + sourceProperty.getName() + " - " + destination.getClass().getName() + "." + destinationProperty.getName();
    }
    
}
