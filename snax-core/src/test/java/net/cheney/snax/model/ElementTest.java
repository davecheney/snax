package net.cheney.snax.model;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;

import org.junit.Assert;
import org.junit.Test;


public class ElementTest extends ParentNodeTest {
	
	@Test public void elementTest() {
		Namespace n = Namespace.valueOf("a", "http://cheney.net/a");
		ContainerNode e2 = new Element(QName.valueOf(n, "xml"));
		
		QName q = QName.valueOf(n, "xml");
		ContainerNode e3 = new Element(q);
		
		Assert.assertEquals(e2, e3);
	}
	
	@Test public void elementWithOneChildTest() {
		ContainerNode child = new Element(QName.valueOf("child"));
		ContainerNode parent = new Element(QName.valueOf("parent"), child);
		
		Assert.assertEquals(child, parent.children().first());
	}
	
	@Test
	public void toStringTest() {
		ContainerNode foo = new Element(QName.valueOf("foo"));
		Assert.assertEquals("<foo/>", foo.toString());
		
		ContainerNode bar = new Element(QName.valueOf("bar"), new Attribute(QName.valueOf("baz"), "blah"));
		Assert.assertEquals("<bar baz=\"blah\"/>", bar.toString());
		
		ContainerNode a = new Element("a", new Element("b"));
		Assert.assertEquals("<a/>", a.toString());
	}
	
	@Test public void stringContentsTest() {
		Element foo = new Element("body", new Text("This is the body contents"));
		Assert.assertEquals(foo.text(), "This is the body contents");
	}

	@Override
	public void testDetatch() {
		Attribute a = new Attribute("foo", "bar");
		ContainerNode e = new Element("e");
		ContainerNode f = new Element("f", a);
		ContainerNode g = new Element("g", a, e);
		
		Assert.assertEquals(e, e.detach());
		Assert.assertEquals(f, f.detach());
		Assert.assertEquals(new Element("g", a), g.detach());
	}

	@Override
	public void testType() {
		ContainerNode element = new Element("foo");
		Assert.assertEquals(element.type(), Node.Type.ELEMENT);
		
	}

}
