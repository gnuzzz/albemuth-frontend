package ru.albemuth.frontend.metadata;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 19.12.2007
 * Time: 2:22:51
 */
public class ConstantValueDescriptor extends LinkSide {

    private String constantValue;

    public ConstantValueDescriptor(String constantValue) {
        this.constantValue = constantValue;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public String toString() {
        return "\"" + constantValue + "\"";
    }

}
