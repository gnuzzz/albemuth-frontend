package ru.albemuth.frontend.metadata;

import java.io.Serializable;

public class LinkDescriptor implements Serializable {

    public static final LinkDescriptor[] EMPTY_ARRAY                = new LinkDescriptor[0];

    private LinkSide source;
    private PropertyDescriptor destination;

    public LinkDescriptor(LinkSide source, PropertyDescriptor destination) {
        this.source = source;
        this.destination = destination;
    }

    public LinkSide getSource() {
        return source;
    }

    public PropertyDescriptor getDestination() {
        return destination;
    }

    public int hashCode() {
        return source.hashCode() * destination.hashCode();
    }

    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof LinkDescriptor) {
            LinkDescriptor ld = (LinkDescriptor)obj;
            ret =  ld.source.equals(source)
                && ld.destination.equals(destination);
        }
        return ret;
    }

    public String toString() {
        return source + " - " + destination;
    }

}
