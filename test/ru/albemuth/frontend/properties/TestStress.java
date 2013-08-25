package ru.albemuth.frontend.properties;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 30.10.2007
 * Time: 2:06:29
 */
public class TestStress extends TestCase {

        public void test() {
        C c = new C(new B(new A("aaa")));
        String s = null;
        A a = c.getB().getA();
        PropertyFactory pf = new PropertyFactory();
        try {
            Property plain = pf.getProperty(A.class, "a");
            Property composite = pf.getProperty(C.class, "b.a.a");

            for (int i = 0; i < 1000000; i++) {
                s = a.getA();
            }
            System.out.println(s);
            for (int i = 0; i < 1000000; i++) {
                s = c.getB().getA().getA();
            }
            System.out.println(s);
            for (int i = 0; i < 1000000; i++) {
                s = (String)plain.getValue(a);
            }
            System.out.println(s);
            for (int i = 0; i < 1000000; i++) {
                s = (String)composite.getValue(c);
            }
            System.out.println(s);

            long t1, t2;
            int loopsNumber = 1000000;

            t1 = System.currentTimeMillis();
            for (int i = 0; i < loopsNumber; i++) {
                s = a.getA();
            }
            t2 = System.currentTimeMillis();
            System.out.println(s + ", " + (t2 - t1) + ", " + ((t2 - t1)/(double)loopsNumber) + ", " + 1000*loopsNumber/(double)(t2 - t1));
            t1 = System.currentTimeMillis();
            for (int i = 0; i < loopsNumber; i++) {
                s = c.getB().getA().getA();
            }
            t2 = System.currentTimeMillis();
            System.out.println(s + ", " + (t2 - t1) + ", " + ((t2 - t1)/(double)loopsNumber) + ", " + 1000*loopsNumber/(double)(t2 - t1));
            t1 = System.currentTimeMillis();
            for (int i = 0; i < loopsNumber; i++) {
                s = (String)plain.getValue(a);
            }
            t2 = System.currentTimeMillis();
            System.out.println(s + ", " + (t2 - t1) + ", " + ((t2 - t1)/(double)loopsNumber) + ", " + 1000*loopsNumber/(double)(t2 - t1));
            t1 = System.currentTimeMillis();
            for (int i = 0; i < loopsNumber; i++) {
                s = (String)composite.getValue(c);
            }
            t2 = System.currentTimeMillis();
            System.out.println(s + ", " + (t2 - t1) + ", " + ((t2 - t1)/(double)loopsNumber) + ", " + 1000*loopsNumber/(double)(t2 - t1));


        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }

    public static class A {

        private String a;

        A(String a) {
            this.a = a;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

    }

    public static class B {

        private A a;

        B(A a) {
            this.a = a;
        }

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

    }

    public static class C {

        private B b;

        C(B b) {
            this.b = b;
        }

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }

    }

}
