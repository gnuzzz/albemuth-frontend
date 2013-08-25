package ru.albemuth.frontend.metadata;

import java.util.Arrays;

public class ComplexPropertyDescriptor extends LinkSide {

    private String methodName;
    private ComponentDescriptor owner;
    private LinkSide[] sources;

    public ComplexPropertyDescriptor(LinkSide[] sources, String methodName, ComponentDescriptor owner) {
        this.sources = sources;
        this.methodName = methodName;
        this.owner = owner;
    }

    public String getMethodName() {
        return methodName;
    }

    public LinkSide[] getSources() {
        return sources;
    }

    public ComponentDescriptor getOwner() {
        return owner;
    }

    public int hashCode() {
        int ret = 1;
        for (LinkSide ls: sources) {
            ret *= ls.hashCode();
        }
        return ret;
    }

    public boolean equals(Object o) {
        return o instanceof ComplexPropertyDescriptor && Arrays.equals(((ComplexPropertyDescriptor)o).getSources(), sources);
    }

    public String toString() {
        String ret = "{";
        for (int i = 0; i < sources.length; i++) {
            if (i > 0) {
                ret += ", ";
            }
            ret += sources[i];
        }
        return ret + "}";
    }

}
