package ru.albemuth.frontend.metadata;

public @interface ContextProperty {

    public static final String DEFAULT_CONTEXT_NAME             = "[default]";

    ContextType type();

    String contextName() default DEFAULT_CONTEXT_NAME;

}
