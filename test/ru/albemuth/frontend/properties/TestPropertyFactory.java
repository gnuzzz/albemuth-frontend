package ru.albemuth.frontend.properties;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 17.10.2007
 * Time: 2:32:14
 */
public class TestPropertyFactory extends TestCase {

    public void testFirstCharToUpperCase() {
        PropertyFactory pf = new PropertyFactory();
        assertEquals("A", pf.firstCharToUpperCase("a"));
        assertEquals("Ab", pf.firstCharToUpperCase("ab"));
        assertEquals("Abc", pf.firstCharToUpperCase("abc"));
        try {
            pf.firstCharToUpperCase("");
            fail();
        } catch (StringIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testGetProperty() {
        PropertyFactory pf = new PropertyFactory();
        try {
            assertEquals(PrimitiveBooleanProperty.class, pf.getProperty(Foo.class, "booleanProperty").getClass());
            assertEquals(boolean.class, pf.getProperty(Foo.class, "booleanProperty").getValueClass());
            assertEquals(PrimitiveByteProperty.class, pf.getProperty(Foo.class, "byteProperty").getClass());
            assertEquals(byte.class, pf.getProperty(Foo.class, "byteProperty").getValueClass());
            assertEquals(PrimitiveShortProperty.class, pf.getProperty(Foo.class, "shortProperty").getClass());
            assertEquals(short.class, pf.getProperty(Foo.class, "shortProperty").getValueClass());
            assertEquals(PrimitiveIntProperty.class, pf.getProperty(Foo.class, "intProperty").getClass());
            assertEquals(int.class, pf.getProperty(Foo.class, "intProperty").getValueClass());
            assertEquals(PrimitiveLongProperty.class, pf.getProperty(Foo.class, "longProperty").getClass());
            assertEquals(long.class, pf.getProperty(Foo.class, "longProperty").getValueClass());
            assertEquals(PrimitiveFloatProperty.class, pf.getProperty(Foo.class, "floatProperty").getClass());
            assertEquals(float.class, pf.getProperty(Foo.class, "floatProperty").getValueClass());
            assertEquals(PrimitiveDoubleProperty.class, pf.getProperty(Foo.class, "doubleProperty").getClass());
            assertEquals(double.class, pf.getProperty(Foo.class, "doubleProperty").getValueClass());
            assertEquals(ObjectiveProperty.class, pf.getProperty(Foo.class, "stringProperty").getClass());
            assertEquals(String.class, pf.getProperty(Foo.class, "stringProperty").getValueClass());

            assertEquals(PrimitiveBooleanProperty.class, pf.getProperty(Foo.class, "bproperty").getClass());
            assertEquals(boolean.class, pf.getProperty(Foo.class, "bproperty").getValueClass());

            assertEquals(Foo.class, pf.getProperty(Foo.class, "stringProperty").getTargetClass());

            assertEquals(ObjectiveProperty.class, pf.getProperty(Foo.class, "a").getClass());
            assertEquals("a", pf.getProperty(Foo.class, "a").getName());
            assertEquals(String.class, pf.getProperty(Foo.class, "a").getValueClass());
            assertEquals(ObjectiveProperty.class, pf.getProperty(Foo.class, "b").getClass());
            assertEquals("b", pf.getProperty(Foo.class, "b").getName());
            assertEquals(String.class, pf.getProperty(Foo.class, "b").getValueClass());
            assertEquals(ObjectiveProperty.class, pf.getProperty(Foo.class, "c").getClass());
            assertEquals("c", pf.getProperty(Foo.class, "c").getName());
            assertEquals(String.class, pf.getProperty(Foo.class, "c").getValueClass());
            try {
                assertEquals(ObjectiveProperty.class, pf.getProperty(Foo.class, "d").getClass());
                fail();
            } catch (PropertyException pe) {
                assertTrue(true);
            }
            assertEquals(ObjectiveProperty.class, pf.getProperty(Foo.class, "e").getClass());
            assertEquals("e", pf.getProperty(Foo.class, "e").getName());
            assertEquals(String.class, pf.getProperty(Foo.class, "e").getValueClass());
            assertEquals(ObjectiveProperty.class, pf.getProperty(Foo.class, "f").getClass());
            assertEquals("f", pf.getProperty(Foo.class, "f").getName());
            assertEquals(String.class, pf.getProperty(Foo.class, "f").getValueClass());

            assertEquals(CompositeProperty.class, pf.getProperty(Foo.class, "foo.a").getClass());
            assertEquals("foo.a", pf.getProperty(Foo.class, "foo.a").getName());
            assertEquals(String.class, pf.getProperty(Foo.class, "foo.a").getValueClass());
        } catch (Exception e) {
            fail();
        }
    }

    public static class Foo {

        public boolean booleanProperty;
        public byte byteProperty;
        public short shortProperty;
        public int intProperty;
        public long longProperty;
        public float floatProperty;
        public double doubleProperty;
        public String stringProperty;

        private boolean bproperty;
        public boolean isBproperty() {
            return bproperty;
        }
        public void setBproperty(boolean bproperty) {
            this.bproperty = bproperty;
        }

        public String a;
        public String getA() {
            return a;
        }
        public void setA(String a) {
            this.a = a;
        }

        private String b;
        public String getB() {
            return b;
        }
        public void setB(String b) {
            this.b = b;
        }

        private String c;
        public String getC() {
            return c;
        }

        private String d;
        public void setD(String d) {
            this.d = d;
        }

        public String getE() {
            return null;
        }
        public void setE(String e) {}

        public int f;
        public String getF() {
            return null;
        }

        public Foo foo;
    }

}