package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 13.12.2007
 * Time: 1:28:38
 */
public class ConstantTextDescriptor extends ComponentDescriptor {

    private String content;
    private boolean minify;

    public ConstantTextDescriptor(ComponentClassBuilder componentClassBuilder, String name, String content, boolean minify) {
        super(componentClassBuilder, name, null);
        this.content = content;
        this.minify = minify;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMinify() {
        return minify;
    }

    public void setMinify(boolean minify) {
        this.minify = minify;
    }

    protected Component createComponentInstance(Class<Component> componentClass, Component parent, Document document) {
        return null;
    }

    public int hashCode() {
        return getContent().hashCode();
    }

    public boolean equals(Object o) {
        return o instanceof ConstantTextDescriptor && ((ConstantTextDescriptor)o).getContent().equals(getContent());
    }

    public String toString() {
        return "constant text: " + content;
    }

}
