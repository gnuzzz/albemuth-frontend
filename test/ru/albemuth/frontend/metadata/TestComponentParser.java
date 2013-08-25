package ru.albemuth.frontend.metadata;

import junit.framework.TestCase;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 21.12.2007
 * Time: 3:01:45
 */
public class TestComponentParser extends TestCase {

    public void testIsProperty() {
        assertTrue(ComponentParser.isProperty(String.class, "class"));
        assertTrue(ComponentParser.isProperty(String.class, "length"));
        assertTrue(ComponentParser.isProperty(Class.class, "array"));
        assertFalse(ComponentParser.isProperty(String.class, "aaa"));
    }

    public void testParseComponentContent() {
        ComponentParser parser = new ComponentParser(new ComponentClassBuilderJavassistImpl("test"));
        try {
            String testContent = "<html><head><title>Title</title></head><body>Body</body></html>";
            NodeList nodeList = parser.parseComponentContent(testContent);
            checkTestContent((Html)nodeList.elementAt(0));
        } catch (MetadataException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private void checkTestContent(Html html) {
        try {
            HeadTag head = (HeadTag)html.getChild(0);
            NodeList headChildren = head.getChildren();
            TitleTag title = (TitleTag)headChildren.elementAt(0);
            TextNode titleValue = (TextNode)title.getChild(0);
            assertEquals("Title", titleValue.getText());
            BodyTag body = (BodyTag)html.getChild(1);
            TextNode bodyContent = (TextNode)body.getChild(0);
            assertEquals("Body", bodyContent.getText());
        } catch (ClassCastException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testParseTextContent() {
        ComponentClassBuilder ccb = new ComponentClassBuilderJavassistImpl("test");
        ComponentParser parser = new ComponentParser(ccb);
        final ComponentDescriptor cd = new ComponentDescriptor(ccb, "cd", ComponentClass.class);
        try {
            checkEquals(new ComponentDescriptor[] {
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
            }, parser.parseTextContent(cd, "aaa bbb $a; += $b ... ttt $c.a", 0));
        } catch (MetadataException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testOptimizeConstantTextDescriptors() {
        ComponentClassBuilder ccb = new ComponentClassBuilderJavassistImpl("test");
        ComponentParser parser = new ComponentParser(ccb);
        final ComponentDescriptor cd = new ComponentDescriptor(ccb, "cd", ComponentClass.class);
        try {
            ComponentDescriptor[] cs;
            List<ComponentDescriptor> listCs1, listCs2;

            listCs1 = createList(new ComponentDescriptor[] {
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
            });
            listCs2 = createList(new ComponentDescriptor[] {
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
            });
            cs = new ComponentDescriptor[] {
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
            };
            listCs1.addAll(listCs2);
            checkEquals(cs, parser.optimizeConstantTextDescriptors(listCs1));

            listCs1 = createList(new ComponentDescriptor[] {
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "12345", false),
            });
            listCs2 = createList(new ComponentDescriptor[] {
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
            });
            cs = new ComponentDescriptor[] {
                    new ConstantTextDescriptor(ccb, null, "aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "12345aaa bbb ", false),
                    new PropertyValueDescriptor(ccb, "a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "a"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, "; += ", false),
                    new PropertyValueDescriptor(ccb, "b") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "b"), new PropertyDescriptor(this, "value"))};}
                    },
                    new ConstantTextDescriptor(ccb, null, " ... ttt ", false),
                    new PropertyValueDescriptor(ccb, "c.a") {
                        {links = new LinkDescriptor[]{new LinkDescriptor(new PropertyDescriptor(cd, "c.a"), new PropertyDescriptor(this, "value"))};}
                    },
            };
            listCs1.addAll(listCs2);
            checkEquals(cs, parser.optimizeConstantTextDescriptors(listCs1));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComplexPropertyValueDescriptor() {
        ComponentClassBuilder ccb = new ComponentClassBuilderJavassistImpl("test");
        ComponentParser parser = new ComponentParser(ccb);
        final ComponentDescriptor cd = new ComponentDescriptor(ccb, "cd", ComponentClass.class);
        try {
            List<ComponentDescriptor> descriptors = parser.parseTextContent(cd, "В заказе $a на ${$a aaa} сум\nму $b без учета доставки <a cid=\"Hyperlink\" name=\"toCart\" listener=\"toCart\">[Подробнее]</a>", 0);
            assertEquals(7, descriptors.size());
            assertEquals(ConstantTextDescriptor.class, descriptors.get(0).getClass());
            assertEquals(PropertyValueDescriptor.class, descriptors.get(1).getClass());
            assertEquals(ConstantTextDescriptor.class, descriptors.get(2).getClass());
            assertEquals(ComplexPropertyValueDescriptor.class, descriptors.get(3).getClass());
            assertEquals(ConstantTextDescriptor.class, descriptors.get(4).getClass());
            assertEquals(PropertyValueDescriptor.class, descriptors.get(5).getClass());
            assertEquals(ConstantTextDescriptor.class, descriptors.get(6).getClass());
            assertEquals(PropertyValueDescriptor.class, descriptors.get(3).getChildren()[0].getClass());
            assertEquals(ConstantTextDescriptor.class, descriptors.get(3).getChildren()[1].getClass());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void checkEquals(ComponentDescriptor[] expectedComponents, List<ComponentDescriptor> components) {
        assertEquals(expectedComponents.length, components.size());
        for (int i = 0; i < expectedComponents.length; i++) {
            assertEquals(expectedComponents[i], components.get(i));
        }
    }

    private List<ComponentDescriptor> createList(ComponentDescriptor[] objs) {
        return new ArrayList<ComponentDescriptor>(Arrays.asList(objs));
    }

    abstract class ComponentClass extends Component {

        private String a;
        private int b;
        private ComponentClass c;

        public ComponentClass(String name, Component parent, Document document) {
            super(name, parent, document);
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public ComponentClass getC() {
            return c;
        }

        public void setC(ComponentClass c) {
            this.c = c;
        }

    }

}
