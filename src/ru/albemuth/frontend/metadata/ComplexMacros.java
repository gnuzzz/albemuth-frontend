package ru.albemuth.frontend.metadata;

import java.util.List;
import java.util.ArrayList;

public class ComplexMacros extends Macros {

    private List<Macros> macroses = new ArrayList<Macros>();

    public void addMacros(Macros macros) {
        macroses.add(macros);
    }

    public StringBuffer apply(StringBuffer source) {
        for (Macros macros: macroses) {
            source = macros.apply(source);
        }
        return source;
    }
    
}
