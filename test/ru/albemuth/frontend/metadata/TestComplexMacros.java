package ru.albemuth.frontend.metadata;

import junit.framework.TestCase;

public class TestComplexMacros extends TestCase {

    public void testApply() {
        String source = "<if condition=\"$condition\">\n" +
                "<span raw_attr=\"$disabledString\" raw_attr=\"$readonlyString\">aaa</span>\n" +
                "</if>";
        SimpleMacros smIfStart = new SimpleMacros("<if", "<span");
        SimpleMacros smIfEnd = new SimpleMacros("</if>", "</span>");
        SimpleMacros smRawAttr = new SimpleMacros("raw_attr=\"([^\"]*)\"", "{0}");
        ComplexMacros cm = new ComplexMacros();
        cm.addMacros(smIfStart);
        cm.addMacros(smIfEnd);
        cm.addMacros(smRawAttr);
        String result = cm.apply(new StringBuffer(source)).toString();
        assertEquals("<span condition=\"$condition\">\n" +
                "<span $disabledString $readonlyString>aaa</span>\n" +
                "</span>", result);

    }

}
