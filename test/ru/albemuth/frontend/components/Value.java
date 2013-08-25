package ru.albemuth.frontend.components;

class Value implements ActionComponent.Parameter {

    public static final Value DEFAULT           = new Value(0);

    private int value;

    public Value(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getParameterName() {
        return "value";
    }

    public String getParameterValue() {
        return "" + getValue();
    }

    public int hashCode() {
        return value;
    }
    
}
