package ru.albemuth.frontend.metadata;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 19.12.2007
 * Time: 4:46:11
 */
public class PropertiesArrayDescriptor extends LinkSide {

    private LinkSide[] sources;

    public PropertiesArrayDescriptor(LinkSide[] sources) {
        this.sources = sources;
    }

    public LinkSide[] getSources() {
        return sources;
    }

    public int hashCode() {
        int ret = 1;
        for (LinkSide ls: sources) {
            ret *= ls.hashCode();
        }
        return ret;
    }

    public boolean equals(Object o) {
        return o instanceof PropertiesArrayDescriptor && Arrays.equals(((PropertiesArrayDescriptor)o).getSources(), sources);
    }

    public String toString() {
        String ret = "[";
        for (int i = 0; i < sources.length; i++) {
            if (i > 0) {
                ret += ", ";
            }
            ret += sources[i];
        }
        return ret + "]";
    }

}
