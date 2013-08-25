package ru.albemuth.frontend.metadata;

public class DirectActionDescriptor extends ComponentDescriptor {

    public DirectActionDescriptor(ComponentClassBuilder componentClassBuilder, String name, Class componentClass) {
        super(componentClassBuilder, name, componentClass);
    }

    public String getName() {
        return getListener() != null ? getListener().getTargetMethodName() : super.getName();
    }

}
