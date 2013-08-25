package ru.albemuth.frontend.properties;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 17.10.2007
 * Time: 2:31:10
 */
public class TestPlainProperty extends TestCase {

    public void testSetValue() {
        PropertyFactory pf = new PropertyFactory();
        Foo foo = new Foo();

        try {
            Property property1 = pf.getProperty(foo.getClass(), "s1");
            Property property2 = pf.getProperty(foo.getClass(), "s2");
            Property property3 = pf.getProperty(foo.getClass(), "s3");

            property1.setValue(foo, "property1");
            property2.setValue(foo, "property2");
            property3.setValue(foo, "property3");

            assertEquals("property1", foo.getS1());
            assertEquals("property2", foo.getS2());
            assertEquals("property3", foo.s3);

            property1 = property1.clone();
            property2 = property2.clone();
            property3 = property3.clone();

            property1.setValue(foo, "property4");
            property2.setValue(foo, "property5");
            property3.setValue(foo, "property6");

            assertEquals("property4", foo.getS1());
            assertEquals("property5", foo.getS2());
            assertEquals("property6", foo.s3);
        } catch (PropertyException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testGetValue() {
        PropertyFactory pf = new PropertyFactory();
        Foo foo = new Foo();
        foo.setS1("property1");
        foo.setS2("property2");
        foo.s3 = "property3";

        try {
            Property property1 = pf.getProperty(foo.getClass(), "s1");
            Property property2 = pf.getProperty(foo.getClass(), "s2");
            Property property3 = pf.getProperty(foo.getClass(), "s3");

            assertEquals("property1", property1.getValue(foo));
            assertEquals("property2", property2.getValue(foo));
            assertEquals("property3", property3.getValue(foo));

            property1 = property1.clone();
            property2 = property2.clone();
            property3 = property3.clone();

            assertEquals("property1", property1.getValue(foo));
            assertEquals("property2", property2.getValue(foo));
            assertEquals("property3", property3.getValue(foo));
        } catch (PropertyException e) {
            e.printStackTrace();
            fail();
        }
    }

    public static class Foo {

        private String s1;
        public String s2;
        public String s3;

        public String getS1() {
            return s1;
        }

        public void setS1(String s1) {
            this.s1 = s1;
        }

        public String getS2() {
            return s2;
        }

        public void setS2(String s2) {
            this.s2 = s2;
        }

    }

}
