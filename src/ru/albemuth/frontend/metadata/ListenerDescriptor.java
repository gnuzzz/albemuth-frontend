package ru.albemuth.frontend.metadata;

import java.io.Serializable;

public class ListenerDescriptor implements Serializable {

    private ComponentDescriptor targetDescriptor;
    private String targetMethodName;

    public ListenerDescriptor(ComponentDescriptor targetDescriptor, String targetMethodName) {
        this.targetDescriptor = targetDescriptor;
        this.targetMethodName = targetMethodName;
    }

    public ComponentDescriptor getTargetDescriptor() {
        return targetDescriptor;
    }

    public String getTargetMethodName() {
        return targetMethodName;
    }

}
