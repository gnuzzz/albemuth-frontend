package ru.albemuth.frontend.properties;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 17.10.2007
 * Time: 3:50:34
 */
public class TestCompositeProperty extends TestCase {

    public void testSetValue() {
        PropertyFactory pf = new PropertyFactory();
        Foo1 foo1 = new Foo1();
        Foo2 foo2 = new Foo2();
        foo2.setF1(foo1);
        Foo3 foo3 = new Foo3();
        foo3.setF2(foo2);

        try {
            Property p = pf.getProperty(foo3.getClass(), "f2.f1.s");
            p.setValue(foo3, "property");
            assertEquals("property", foo1.getS());

            p = p.clone();
            p.setValue(foo3, "property1");
            assertEquals("property1", foo1.getS());
        } catch (PropertyException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testGetValue() {
        PropertyFactory pf = new PropertyFactory();
        Foo1 foo1 = new Foo1();
        foo1.setS("property");
        Foo2 foo2 = new Foo2();
        foo2.setF1(foo1);
        Foo3 foo3 = new Foo3();
        foo3.setF2(foo2);

        try {
            Property p = pf.getProperty(foo3.getClass(), "f2.f1.s");
            String property = (String)p.getValue(foo3);
            assertEquals("property", property);

            p = p.clone();
            property = (String)p.getValue(foo3);
            assertEquals("property", property);
        } catch (PropertyException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static class Foo1 {

        private String s;

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }

    public static class Foo2 {

        private Foo1 f1;

        public Foo1 getF1() {
            return f1;
        }

        public void setF1(Foo1 f1) {
            this.f1 = f1;
        }

    }

    public static class Foo3 {

        private Foo2 f2;

        public Foo2 getF2() {
            return f2;
        }

        public void setF2(Foo2 f2) {
            this.f2 = f2;
        }

    }

}
